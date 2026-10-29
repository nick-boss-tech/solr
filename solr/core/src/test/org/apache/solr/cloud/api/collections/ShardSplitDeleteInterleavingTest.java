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
package org.apache.solr.cloud.api.collections;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.RequestStatusState;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.util.TestInjection;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShardSplitDeleteInterleavingTest extends SolrCloudTestCase {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String PARENT_SHARD = "shard1";
  private static final String CHILD0 = PARENT_SHARD + "_0";
  private static final String CHILD1 = PARENT_SHARD + "_1";

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2).addConfig("conf1", configset("cloud-minimal")).configure();
  }

  @Test
  public void testDeleteOneRecoveryChildWhileSplitActivatesTheOther() throws Exception {
    String collectionName = "testSplitDeleteInterleaving";
    CollectionAdminRequest.createCollection(collectionName, "conf1", 1, 2)
        .process(cluster.getSolrClient());

    ZkStateReader stateReader = ZkStateReader.from(cluster.getSolrClient());
    stateReader.waitForState(
        collectionName, 30, TimeUnit.SECONDS, SolrCloudTestCase.activeClusterShape(1, 2));

    TestInjection.reset();
    CountDownLatch splitReached = new CountDownLatch(1);
    CountDownLatch releaseSplit = new CountDownLatch(1);
    CountDownLatch deleteReached = new CountDownLatch(1);
    CountDownLatch releaseDelete = new CountDownLatch(1);
    TestInjection.splitBeforeFirstReplicaReachedLatch = splitReached;
    TestInjection.splitBeforeFirstReplicaReleaseLatch = releaseSplit;
    TestInjection.deleteShardAfterConstructionReachedLatch = deleteReached;
    TestInjection.deleteShardAfterConstructionReleaseLatch = releaseDelete;

    String splitAsyncId = null;
    String deleteAsyncId = null;
    boolean splitStatusChecked = false;
    boolean deleteStatusChecked = false;
    try {
      splitAsyncId =
          CollectionAdminRequest.splitShard(collectionName)
              .setShardName(PARENT_SHARD)
              .processAsync(cluster.getSolrClient());
      assertTrue(
          "SPLITSHARD did not reach the first child-replica request",
          splitReached.await(60, TimeUnit.SECONDS));

      String firstReplicaShard = TestInjection.splitFirstReplicaShardForTest;
      assertNotNull("the split hook did not publish its first replica shard", firstReplicaShard);
      assertTrue(
          "unexpected split child " + firstReplicaShard,
          CHILD0.equals(firstReplicaShard) || CHILD1.equals(firstReplicaShard));
      String survivingShard = firstReplicaShard;
      String deletingShard = CHILD0.equals(firstReplicaShard) ? CHILD1 : CHILD0;

      deleteAsyncId =
          CollectionAdminRequest.deleteShard(collectionName, deletingShard)
              .processAsync(cluster.getSolrClient());
      assertTrue(
          "DELETESHARD did not reach its RECOVERY-to-CONSTRUCTION point",
          deleteReached.await(60, TimeUnit.SECONDS));
      stateReader.waitForState(
          collectionName,
          30,
          TimeUnit.SECONDS,
          state -> {
            Slice parent = state.getSlice(PARENT_SHARD);
            Slice surviving = state.getSlice(survivingShard);
            Slice deleting = state.getSlice(deletingShard);
            return parent != null
                && parent.getState() == Slice.State.ACTIVE
                && surviving != null
                && surviving.getState() == Slice.State.RECOVERY
                && deleting != null
                && deleting.getState() == Slice.State.CONSTRUCTION;
          });

      releaseSplit.countDown();
      stateReader.waitForState(
          collectionName,
          60,
          TimeUnit.SECONDS,
          state -> {
            Slice parent = state.getSlice(PARENT_SHARD);
            Slice surviving = state.getSlice(survivingShard);
            Slice deleting = state.getSlice(deletingShard);
            return parent != null
                && parent.getState() == Slice.State.INACTIVE
                && surviving != null
                && surviving.getState() == Slice.State.ACTIVE
                && deleting != null
                && deleting.getState() == Slice.State.CONSTRUCTION;
          });

      DocCollection observed = stateReader.getClusterState().getCollection(collectionName);
      assertEquals(Slice.State.INACTIVE, observed.getSlice(PARENT_SHARD).getState());
      assertEquals(Slice.State.ACTIVE, observed.getSlice(survivingShard).getState());
      assertEquals(Slice.State.CONSTRUCTION, observed.getSlice(deletingShard).getState());
      log.info(
          "Observed ZooKeeper interleaving: parent={} {}, survivingChild={} {}, deletingChild={} {}",
          PARENT_SHARD,
          observed.getSlice(PARENT_SHARD).getState(),
          survivingShard,
          observed.getSlice(survivingShard).getState(),
          deletingShard,
          observed.getSlice(deletingShard).getState());

      releaseDelete.countDown();
      RequestStatusState deleteStatus =
          CollectionAdminRequest.requestStatus(deleteAsyncId).waitFor(cluster.getSolrClient(), 120);
      deleteStatusChecked = true;
      assertEquals("DELETESHARD should complete", "completed", deleteStatus.getKey());
      log.info("DELETESHARD request {} finished with {}", deleteAsyncId, deleteStatus.getKey());

      RequestStatusState splitStatus =
          CollectionAdminRequest.requestStatus(splitAsyncId).waitFor(cluster.getSolrClient(), 120);
      if ("running".equals(splitStatus.getKey())) {
        log.info(
            "SPLITSHARD still running after 120s; waiting up to 30s more for a terminal status");
        splitStatus =
            CollectionAdminRequest.requestStatus(splitAsyncId).waitFor(cluster.getSolrClient(), 30);
      }
      splitStatusChecked = true;
      assertTrue(
          "SPLITSHARD did not reach a terminal status: " + splitStatus.getKey(),
          !"running".equals(splitStatus.getKey()));
      log.info("SPLITSHARD request {} finished with {}", splitAsyncId, splitStatus.getKey());
    } finally {
      releaseSplit.countDown();
      releaseDelete.countDown();
      if (deleteAsyncId != null && !deleteStatusChecked) {
        try {
          CollectionAdminRequest.requestStatus(deleteAsyncId).waitFor(cluster.getSolrClient(), 120);
        } catch (Exception e) {
          log.warn("DELETESHARD request did not settle during test cleanup", e);
        }
      }
      if (splitAsyncId != null && !splitStatusChecked) {
        try {
          CollectionAdminRequest.requestStatus(splitAsyncId).waitFor(cluster.getSolrClient(), 120);
        } catch (Exception e) {
          log.warn("SPLITSHARD request did not settle during test cleanup", e);
        }
      }
      TestInjection.reset();
    }
  }
}
