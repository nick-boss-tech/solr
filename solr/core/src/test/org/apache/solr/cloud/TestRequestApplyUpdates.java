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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.impl.HttpJdkSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.GenericSolrRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.params.CoreAdminParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.embedded.JettySolrRunner;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Regression tests for SOLR-14098: REQUESTAPPLYUPDATES on a replica whose buffer log is empty used
 * to return EMPTY_BUFFER without publishing the replica ACTIVE, unlike the non-empty replay path,
 * leaving the cluster state stale (for example stuck at RECOVERING).
 */
public class TestRequestApplyUpdates extends SolrCloudTestCase {

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2).addConfig("conf", configset("cloud-minimal")).configure();
  }

  @Test
  public void testEmptyBufferPublishesActive() throws Exception {
    String collection = "applyupdates_empty";
    CollectionAdminRequest.createCollection(collection, "conf", 1, 1)
        .processAndWait(cluster.getSolrClient(), 60);
    cluster.waitForActiveCollection(collection, 1, 1);
    Replica replica = getReplica(collection);

    // Park the replica in RECOVERING cluster state with an empty update buffer, so the
    // only way back to ACTIVE is the publish inside REQUESTAPPLYUPDATES.
    bufferUpdates(replica);
    waitForState(
        "replica did not enter RECOVERING",
        collection,
        (liveNodes, dc) -> replicaState(dc, replica) == Replica.State.RECOVERING);

    NamedList<Object> rsp = requestApplyUpdates(replica);
    assertEquals("EMPTY_BUFFER", rsp.get("status"));

    waitForState(
        "replica did not become ACTIVE after empty-buffer apply",
        collection,
        (liveNodes, dc) -> replicaState(dc, replica) == Replica.State.ACTIVE);
  }

  @Test
  public void testNonEmptyBufferReplaysAndPublishesActive() throws Exception {
    String collection = "applyupdates_replay";
    CollectionAdminRequest.createCollection(collection, "conf", 1, 2)
        .processAndWait(cluster.getSolrClient(), 60);
    cluster.waitForActiveCollection(collection, 1, 2);
    Replica target = getNonLeaderReplica(collection);

    // Park the non-leader replica in RECOVERING with a buffering update log; the leader
    // then forwards new updates to it marked for buffering, as during a real recovery.
    bufferUpdates(target);
    waitForState(
        "replica did not enter RECOVERING",
        collection,
        (liveNodes, dc) -> replicaState(dc, target) == Replica.State.RECOVERING);

    new UpdateRequest().add("id", "1").process(cluster.getSolrClient(), collection);
    waitForBufferTlog(target);

    NamedList<Object> rsp = requestApplyUpdates(target);
    assertEquals("BUFFER_APPLIED", rsp.get("status"));

    waitForState(
        "replica did not become ACTIVE after buffered apply",
        collection,
        (liveNodes, dc) -> replicaState(dc, target) == Replica.State.ACTIVE);

    // The buffered update was replayed onto the replica.
    JettySolrRunner jetty = jettyForCore(target.getCoreName());
    try (HttpSolrClient client =
        new HttpJdkSolrClient.Builder(jetty.getBaseUrl() + "/" + target.getCoreName()).build()) {
      GenericSolrRequest req =
          new GenericSolrRequest(
              SolrRequest.METHOD.GET,
              "/get",
              SolrRequest.SolrRequestType.QUERY,
              new ModifiableSolrParams().set("id", "1"));
      NamedList<Object> getRsp = client.request(req);
      assertNotNull("buffered document was not replayed", getRsp.get("doc"));
    }
  }

  private static Replica getReplica(String collection) {
    DocCollection dc = cluster.getZkStateReader().getClusterState().getCollection(collection);
    return dc.getSlices().stream()
        .flatMap(slice -> slice.getReplicas().stream())
        .findFirst()
        .orElseThrow(() -> new AssertionError("collection has no replicas"));
  }

  private static Replica getNonLeaderReplica(String collection) {
    DocCollection dc = cluster.getZkStateReader().getClusterState().getCollection(collection);
    Slice slice = dc.getSlices().iterator().next();
    Replica leader = slice.getLeader();
    return slice.getReplicas().stream()
        .filter(r -> !r.getName().equals(leader.getName()))
        .findFirst()
        .orElseThrow(() -> new AssertionError("collection has no non-leader replica"));
  }

  private static Replica.State replicaState(DocCollection dc, Replica replica) {
    return dc.getSlices().stream()
        .flatMap(slice -> slice.getReplicas().stream())
        .filter(r -> r.getName().equals(replica.getName()))
        .findFirst()
        .orElseThrow(() -> new AssertionError("replica not found in " + dc.getName()))
        .getState();
  }

  private static JettySolrRunner jettyForCore(String coreName) {
    for (JettySolrRunner jetty : cluster.getJettySolrRunners()) {
      try (SolrCore core = jetty.getCoreContainer().getCore(coreName)) {
        if (core != null) {
          return jetty;
        }
      }
    }
    throw new AssertionError("no jetty hosts core " + coreName);
  }

  /** Publishes the replica RECOVERING in the cluster state and starts buffering updates. */
  private static void bufferUpdates(Replica replica) throws Exception {
    JettySolrRunner jetty = jettyForCore(replica.getCoreName());
    try (SolrCore core = jetty.getCoreContainer().getCore(replica.getCoreName())) {
      jetty
          .getCoreContainer()
          .getZkController()
          .publish(core.getCoreDescriptor(), Replica.State.RECOVERING);
      core.getUpdateHandler().getUpdateLog().bufferUpdates();
    }
  }

  private static NamedList<Object> requestApplyUpdates(Replica replica) throws Exception {
    JettySolrRunner jetty = jettyForCore(replica.getCoreName());
    try (HttpSolrClient client = newClient(jetty)) {
      GenericSolrRequest req =
          new GenericSolrRequest(
              SolrRequest.METHOD.GET,
              "/admin/cores",
              SolrRequest.SolrRequestType.ADMIN,
              new ModifiableSolrParams()
                  .set(
                      CoreAdminParams.ACTION,
                      CoreAdminParams.CoreAdminAction.REQUESTAPPLYUPDATES.name())
                  .set(CoreAdminParams.NAME, replica.getCoreName()));
      return client.request(req);
    }
  }

  private static Path waitForBufferTlog(Replica replica) throws Exception {
    JettySolrRunner jetty = jettyForCore(replica.getCoreName());
    Path dataDir;
    try (SolrCore core = jetty.getCoreContainer().getCore(replica.getCoreName())) {
      dataDir = Path.of(core.getDataDir());
    }
    long deadline = System.nanoTime() + 30_000_000_000L;
    while (System.nanoTime() < deadline) {
      try (Stream<Path> walk = Files.walk(dataDir)) {
        List<Path> matches =
            walk.filter(p -> p.getFileName().toString().startsWith("buffer.tlog.")).toList();
        if (!matches.isEmpty()) {
          return matches.get(0);
        }
      }
      Thread.sleep(250);
    }
    fail("no buffer transaction log appeared under " + dataDir);
    return null; // unreachable
  }

  private static HttpSolrClient newClient(JettySolrRunner jetty) {
    return new HttpJdkSolrClient.Builder(jetty.getBaseUrl().toString()).build();
  }
}
