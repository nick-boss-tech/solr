/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.cloud;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.util.SuppressForbidden;
import org.apache.solr.common.util.Utils;
import org.apache.solr.embedded.JettySolrRunner;
import org.apache.solr.util.LogListener;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A replica that cannot connect to its leader during recovery logs the failure at WARN level with
 * the underlying exception, so the root cause is visible (SOLR-12991).
 *
 * <p>The unreachable leader is simulated by pointing the leader replica's {@code base_url} in the
 * collection state at a port where nothing listens, then forcing the follower into recovery with a
 * {@code REQUESTRECOVERY} core admin request. That reproduces the production shape (the leader is
 * still the registered leader in ZooKeeper, but connecting to it fails) without depending on node
 * stop and leader-election timing.
 */
public class RecoveryStrategyLeaderUnreachableLogTest extends SolrCloudTestCase {

  @BeforeClass
  public static void setupCluster() throws Exception {
    cluster = configureCluster(2).addConfig("conf", configset("cloud-minimal")).configure();
  }

  @Test
  @SuppressForbidden(
      reason = "We need to use log4J2 classes directly to assert the logged exception")
  public void testLeaderConnectFailureLogsRootCause() throws Exception {
    final String collection = "leaderUnreachable";
    CollectionAdminRequest.createCollection(collection, "conf", 1, 2)
        .process(cluster.getSolrClient());
    waitForState("Expected 1x2 collection", collection, clusterShape(1, 2));

    final DocCollection state = getCollectionState(collection);
    final Replica leader = state.getLeader("shard1");
    final Replica follower = getRandomReplica(state.getSlice("shard1"), r -> !r.equals(leader));
    final JettySolrRunner followerJetty = cluster.getReplicaJetty(follower);
    final String deadLeaderUrl = "http://127.0.0.1:" + unusedPort() + "/solr";

    setLeaderBaseUrl(collection, leader, deadLeaderUrl);
    try {
      waitForLeaderBaseUrl(followerJetty, collection, deadLeaderUrl);
      try (LogListener warnLog =
          LogListener.warn(RecoveryStrategy.class).substring("Failed to connect leader")) {
        CoreAdminRequest.RequestRecovery recovery = new CoreAdminRequest.RequestRecovery();
        recovery.setCoreName(follower.getCoreName());
        recovery.process(followerJetty.getSolrClient());

        final LogEvent event = warnLog.getQueue().poll(60, TimeUnit.SECONDS);
        assertNotNull("expected a WARN about the unreachable leader", event);
        assertNotNull("the cause must be logged with the message", event.getThrown());
        assertTrue(
            "the logged exception chain must contain the root cause (an IOException)",
            chainContains(event.getThrown(), IOException.class));
      }
    } finally {
      setLeaderBaseUrl(collection, leader, leader.getBaseUrl());
    }
    waitForReplicaActive(collection, follower);
  }

  private static int unusedPort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    }
  }

  private static boolean chainContains(Throwable thrown, Class<? extends Throwable> type) {
    for (Throwable t = thrown; t != null; t = t.getCause()) {
      if (type.isInstance(t)) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings({"unchecked"})
  private static void setLeaderBaseUrl(String collection, Replica leader, String baseUrl)
      throws Exception {
    final String path = "/collections/" + collection + "/state.json";
    Map<String, Map<String, ?>> state =
        (Map<String, Map<String, ?>>)
            Utils.fromJSON(cluster.getZkClient().getData(path, null, null));
    Map<String, Object> shards = (Map<String, Object>) state.get(collection).get("shards");
    Map<String, Object> replicas =
        (Map<String, Object>) ((Map<String, Object>) shards.get("shard1")).get("replicas");
    ((Map<String, Object>) replicas.get(leader.getName())).put("base_url", baseUrl);
    cluster.getZkClient().setData(path, Utils.toJSON(state));
  }

  private static void waitForLeaderBaseUrl(
      JettySolrRunner followerJetty, String collection, String baseUrl) throws Exception {
    final ZkStateReader reader =
        followerJetty.getCoreContainer().getZkController().getZkStateReader();
    final long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
    while (System.nanoTime() < deadline) {
      Replica leader = reader.getClusterState().getCollection(collection).getLeader("shard1");
      if (leader != null && baseUrl.equals(leader.getBaseUrl())) {
        return;
      }
      Thread.sleep(200);
    }
    fail("follower never saw the leader base_url " + baseUrl);
  }

  private static void waitForReplicaActive(String collection, Replica replica) throws Exception {
    final long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(60);
    while (System.nanoTime() < deadline) {
      for (Replica r : getCollectionState(collection).getSlice("shard1").getReplicas()) {
        if (r.getName().equals(replica.getName()) && r.getState() == Replica.State.ACTIVE) {
          return;
        }
      }
      Thread.sleep(500);
    }
    fail("replica " + replica.getName() + " did not return to ACTIVE after recovery");
  }
}
