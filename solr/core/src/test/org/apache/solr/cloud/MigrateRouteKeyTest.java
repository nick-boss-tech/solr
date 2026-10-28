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
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.solr.client.solrj.RemoteSolrException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RequestStatusState;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.CompositeIdRouter;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.RoutingRule;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.util.TimeSource;
import org.apache.solr.core.SolrCore;
import org.apache.solr.update.UpdateLog;
import org.apache.solr.util.TestInjection;
import org.apache.solr.util.TimeOut;
import org.apache.zookeeper.KeeperException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MigrateRouteKeyTest extends SolrCloudTestCase {

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2).addConfig("conf", configset("cloud-minimal")).configure();
  }

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private boolean waitForRuleToExpire(
      String collection, String shard, String splitKey, long finishTime)
      throws KeeperException, InterruptedException, SolrServerException, IOException {
    DocCollection state;
    Slice slice;
    boolean ruleRemoved = false;
    long expiryTime = finishTime + TimeUnit.NANOSECONDS.convert(60, TimeUnit.SECONDS);
    while (System.nanoTime() < expiryTime) {
      cluster.getZkStateReader().forceUpdateCollection(collection);
      state = getCollectionState(collection);
      slice = state.getSlice(shard);
      Map<String, RoutingRule> routingRules = slice.getRoutingRules();
      if (routingRules == null || routingRules.isEmpty() || !routingRules.containsKey(splitKey)) {
        ruleRemoved = true;
        break;
      }
      SolrInputDocument doc = new SolrInputDocument();
      doc.addField("id", splitKey + random().nextInt());
      cluster.getSolrClient().add(collection, doc);
      Thread.sleep(1000);
    }
    return ruleRemoved;
  }

  protected void invokeCollectionMigration(
      CollectionAdminRequest.AsyncCollectionAdminRequest request)
      throws IOException, SolrServerException, InterruptedException {
    request.processAndWait(cluster.getSolrClient(), 60000);
  }

  @Test
  public void testMissingSplitKey() throws Exception {
    String sourceCollection = "testMissingSplitKey-source";
    CollectionAdminRequest.createCollection(sourceCollection, "conf", 1, 1)
        .process(cluster.getSolrClient());
    String targetCollection = "testMissingSplitKey-target";
    CollectionAdminRequest.createCollection(targetCollection, "conf", 1, 1)
        .process(cluster.getSolrClient());

    RemoteSolrException remoteSolrException =
        expectThrows(
            RemoteSolrException.class,
            "Expected an exception in case split.key is not specified",
            () -> {
              CollectionAdminRequest.migrateData(sourceCollection, targetCollection, "")
                  .setForwardTimeout(45)
                  .process(cluster.getSolrClient());
            });
    assertTrue(remoteSolrException.getMessage().contains("split.key cannot be null or empty"));
  }

  @Test
  public void updateSucceedsAfterMigrateTargetIsDeleted() throws Exception {
    String sourceCollection = "deletedMigrateTarget-source";
    CollectionAdminRequest.createCollection(sourceCollection, "conf", 1, 2)
        .process(cluster.getSolrClient());
    String targetCollection = "deletedMigrateTarget-target";
    CollectionAdminRequest.createCollection(targetCollection, "conf", 1, 1)
        .process(cluster.getSolrClient());

    cluster.getSolrClient().add(sourceCollection, new SolrInputDocument("id", "a!1"));
    cluster.getSolrClient().commit(sourceCollection);

    invokeCollectionMigration(
        CollectionAdminRequest.migrateData(sourceCollection, targetCollection, "a!")
            .setForwardTimeout(45));
    waitForState(
        "Expected to find routing rule for split key a",
        sourceCollection,
        c -> {
          if (c == null) return false;
          Map<String, RoutingRule> routingRules = c.getSlice("shard1").getRoutingRules();
          return routingRules != null && routingRules.containsKey("a!");
        });

    CollectionAdminRequest.deleteCollection(targetCollection).process(cluster.getSolrClient());
    waitForState("Expected target collection deletion", targetCollection, c -> c == null);

    cluster.getSolrClient().add(sourceCollection, new SolrInputDocument("id", "a!2"));
    cluster.getSolrClient().commit(sourceCollection);

    DocCollection sourceState = getCollectionState(sourceCollection);
    assertEquals(2, sourceState.getSlice("shard1").getReplicas().size());
    for (Replica replica : sourceState.getSlice("shard1")) {
      var replicaClient = cluster.getSolrClient(replica);
      SolrQuery query = new SolrQuery("id:\"a!2\"");
      query.set("distrib", false);
      assertEquals(
          "Document missing from replica " + replica.getName(),
          1,
          replicaClient.query(query).getResults().getNumFound());
    }

    waitForState(
        "Expected dangling routing rule removal",
        sourceCollection,
        c -> {
          if (c == null) return false;
          Map<String, RoutingRule> routingRules = c.getSlice("shard1").getRoutingRules();
          return routingRules == null || !routingRules.containsKey("a!");
        });
  }

  @Test
  public void testRetryAfterFailedMigrate() throws Exception {
    String sourceCollection = "failedMigrate-source";
    CollectionAdminRequest.createCollection(sourceCollection, "conf", 1, 1)
        .process(cluster.getSolrClient());
    String targetCollection = "failedMigrate-target";
    CollectionAdminRequest.createCollection(targetCollection, "conf", 1, 1)
        .process(cluster.getSolrClient());

    cluster.getSolrClient().add(sourceCollection, new SolrInputDocument("id", "a!1"));
    cluster.getSolrClient().add(sourceCollection, new SolrInputDocument("id", "a!2"));
    cluster.getSolrClient().add(sourceCollection, new SolrInputDocument("id", "b!3"));
    cluster.getSolrClient().commit(sourceCollection);

    // MIGRATE deletes a leftover temporary collection before it starts, but can't delete one that
    // an alias refers to. Creating the temporary collection then fails, after the target leader
    // has started buffering updates.
    String tempCollection = "split_shard1_temp_shard1";
    CollectionAdminRequest.createCollection(tempCollection, "conf", 1, 1)
        .process(cluster.getSolrClient());
    CollectionAdminRequest.createAlias("failedMigrate-blocker", tempCollection)
        .process(cluster.getSolrClient());
    RemoteSolrException e =
        expectThrows(
            RemoteSolrException.class,
            () ->
                CollectionAdminRequest.migrateData(sourceCollection, targetCollection, "a!")
                    .setForwardTimeout(45)
                    .process(cluster.getSolrClient()));
    assertTrue(e.getMessage(), e.getMessage().contains("collection already exists"));
    assertFailedMigrateUndone(sourceCollection, targetCollection);

    assertEquals(
        RequestStatusState.FAILED,
        CollectionAdminRequest.migrateData(sourceCollection, targetCollection, "a!")
            .setForwardTimeout(45)
            .processAndWait(cluster.getSolrClient(), 60));
    assertFailedMigrateUndone(sourceCollection, targetCollection);

    CollectionAdminRequest.deleteAlias("failedMigrate-blocker").process(cluster.getSolrClient());
    CollectionAdminRequest.migrateData(sourceCollection, targetCollection, "a!")
        .setForwardTimeout(45)
        .process(cluster.getSolrClient());
    cluster.getSolrClient().commit(targetCollection);
    assertEquals(
        2,
        cluster
            .getSolrClient()
            .query(targetCollection, new SolrQuery("*:*"))
            .getResults()
            .getNumFound());
  }

  /** Checks that the target leader isn't buffering updates and the routing rule is gone. */
  private void assertFailedMigrateUndone(String sourceCollection, String targetCollection)
      throws Exception {
    Replica targetLeader = getCollectionState(targetCollection).getLeader("shard1");
    try (SolrCore core =
        cluster
            .getReplicaJetty(targetLeader)
            .getCoreContainer()
            .getCore(targetLeader.getCoreName())) {
      assertEquals(UpdateLog.State.ACTIVE, core.getUpdateHandler().getUpdateLog().getState());
    }
    waitForState(
        "Expected the routing rule of the failed MIGRATE to be removed",
        sourceCollection,
        c -> {
          Map<String, RoutingRule> routingRules = c.getSlice("shard1").getRoutingRules();
          return routingRules == null || !routingRules.containsKey("a!");
        });
  }

  @Test
  public void testFailedMigratePreservesEarlierForwardedUpdates() throws Exception {
    String source = "preserveUpdates-source";
    String target = "preserveUpdates-target";
    CollectionAdminRequest.createCollection(source, "conf", 1, 1).process(cluster.getSolrClient());
    CollectionAdminRequest.createCollection(target, "conf", 1, 1).process(cluster.getSolrClient());
    cluster.getSolrClient().add(source, document("id", "a!updated", "n_ti", 1));
    cluster.getSolrClient().add(source, document("id", "a!deleted", "n_ti", 1));
    cluster.getSolrClient().add(source, new SolrInputDocument("id", "b!1"));
    cluster.getSolrClient().commit(source);
    CollectionAdminRequest.migrateData(source, target, "a!")
        .setForwardTimeout(600)
        .process(cluster.getSolrClient());

    AtomicBoolean writesAcknowledged = new AtomicBoolean();
    TestInjection.migrateAfterRoutingRule =
        () -> {
          try {
            Replica sourceLeader = getCollectionState(source).getLeader("shard1");
            cluster
                .getReplicaJetty(sourceLeader)
                .getCoreContainer()
                .getZkController()
                .getZkStateReader()
                .forceUpdateCollection(source);
            cluster.getSolrClient().add(source, document("id", "a!updated", "n_ti", 2));
            cluster.getSolrClient().deleteById(source, "a!deleted");
            cluster.getSolrClient().add(source, document("id", "b!1", "n_ti", 3));
            Replica leader = getCollectionState(target).getLeader("shard1");
            try (SolrCore core =
                    cluster
                        .getReplicaJetty(leader)
                        .getCoreContainer()
                        .getCore(leader.getCoreName());
                UpdateLog.RecentUpdates updates =
                    core.getUpdateHandler().getUpdateLog().getRecentUpdates()) {
              assertEquals(
                  UpdateLog.State.BUFFERING, core.getUpdateHandler().getUpdateLog().getState());
              assertEquals(
                  "All acknowledged writes must be in the target buffer",
                  3,
                  updates.getBufferUpdates().size());
            }
            writesAcknowledged.set(true);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
          throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "injected MIGRATE failure");
        };
    try {
      RemoteSolrException failure =
          expectThrows(
              RemoteSolrException.class,
              () ->
                  CollectionAdminRequest.migrateData(source, target, "b!")
                      .process(cluster.getSolrClient()));
      assertTrue(failure.getMessage(), failure.getMessage().contains("injected MIGRATE failure"));
    } finally {
      TestInjection.migrateAfterRoutingRule = null;
    }
    assertTrue(writesAcknowledged.get());
    cluster.getSolrClient().commit(target);
    assertEquals(
        "An earlier migration's buffered update must be replayed",
        1,
        cluster
            .getSolrClient()
            .query(target, new SolrQuery("id:\"a!updated\" AND n_ti:2"))
            .getResults()
            .getNumFound());
    assertEquals(
        "An earlier migration's buffered delete must be replayed",
        0,
        cluster
            .getSolrClient()
            .query(target, new SolrQuery("id:\"a!deleted\""))
            .getResults()
            .getNumFound());
    assertNotNull(currentRoutingRule(source, "a!"));
    assertNull(currentRoutingRule(source, "b!"));
    assertEquals(
        "A failed migration may leave partial target data, but must preserve acknowledged writes",
        1,
        cluster
            .getSolrClient()
            .query(target, new SolrQuery("id:\"b!1\" AND n_ti:3"))
            .getResults()
            .getNumFound());
  }

  @Test
  public void testFailedMigrateRestoresExistingRule() throws Exception {
    String source = "restoreRule-source";
    String target = "restoreRule-target";
    CollectionAdminRequest.createCollection(source, "conf", 1, 1).process(cluster.getSolrClient());
    CollectionAdminRequest.createCollection(target, "conf", 1, 1).process(cluster.getSolrClient());
    cluster.getSolrClient().add(source, document("id", "a!1", "n_ti", 1));
    cluster.getSolrClient().commit(source);
    CollectionAdminRequest.migrateData(source, target, "a!")
        .setForwardTimeout(600)
        .process(cluster.getSolrClient());
    Map<String, Object> previousRule = currentRoutingRule(source, "a!").shallowCopy();

    TestInjection.migrateAfterRoutingRule =
        () -> {
          throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "injected MIGRATE failure");
        };
    try {
      RemoteSolrException failure =
          expectThrows(
              RemoteSolrException.class,
              () ->
                  CollectionAdminRequest.migrateData(source, target, "a!")
                      .process(cluster.getSolrClient()));
      assertTrue(failure.getMessage(), failure.getMessage().contains("injected MIGRATE failure"));
    } finally {
      TestInjection.migrateAfterRoutingRule = null;
    }
    RoutingRule restored = currentRoutingRule(source, "a!");
    assertNotNull("An existing rule must survive a failed repeated migration", restored);
    assertEquals(previousRule, restored.getProperties());
    cluster.getSolrClient().add(source, document("id", "a!1", "n_ti", 2));
    cluster.getSolrClient().commit(target);
    assertEquals(
        1,
        cluster
            .getSolrClient()
            .query(target, new SolrQuery("id:\"a!1\" AND n_ti:2"))
            .getResults()
            .getNumFound());
  }

  @Test
  public void testFailedMultiShardMigratePreservesEarlierRange() throws Exception {
    String source = "preserveRange-source";
    String target = "preserveRange-target";
    CollectionAdminRequest.createCollection(source, "conf", 1, 1).process(cluster.getSolrClient());
    CollectionAdminRequest.createCollection(target, "conf", 4, 1).process(cluster.getSolrClient());
    for (int i = 0; i < 100; i++) {
      cluster.getSolrClient().add(source, document("id", "a/1!" + i, "n_ti", 1));
    }
    cluster.getSolrClient().commit(source);
    AtomicInteger transfers = new AtomicInteger();
    AtomicReference<RoutingRule> completedRange = new AtomicReference<>();
    TestInjection.migrateAfterRoutingRule =
        () -> {
          if (transfers.incrementAndGet() == 1) {
            try {
              completedRange.set(new RoutingRule(currentRoutingRule(source, "a!").shallowCopy()));
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          } else {
            throw new SolrException(
                SolrException.ErrorCode.SERVER_ERROR, "injected MIGRATE failure");
          }
        };
    try {
      expectThrows(
          RemoteSolrException.class,
          () ->
              CollectionAdminRequest.migrateData(source, target, "a/1!")
                  .setForwardTimeout(600)
                  .process(cluster.getSolrClient()));
    } finally {
      TestInjection.migrateAfterRoutingRule = null;
    }
    assertEquals(2, transfers.get());
    RoutingRule restored = currentRoutingRule(source, "a!");
    assertNotNull("The first successful transfer's rule must survive", restored);
    assertEquals(completedRange.get().getProperties(), restored.getProperties());
    DocCollection state = getCollectionState(source);
    CompositeIdRouter router = (CompositeIdRouter) state.getRouter();
    String forwardedId = null;
    for (int i = 0; i < 100; i++) {
      String id = "a/1!" + i;
      if (restored.getRouteRanges().get(0).includes(router.sliceHash(id, null, null, state))) {
        forwardedId = id;
        break;
      }
    }
    assertNotNull(forwardedId);
    cluster.getSolrClient().add(source, document("id", forwardedId, "n_ti", 2));
    cluster.getSolrClient().commit(target);
    assertEquals(
        1,
        cluster.getSolrClient().query(target, new SolrQuery("n_ti:2")).getResults().getNumFound());
  }

  private static SolrInputDocument document(Object... fieldsAndValues) {
    SolrInputDocument doc = new SolrInputDocument();
    for (int i = 0; i < fieldsAndValues.length; i += 2) {
      doc.addField((String) fieldsAndValues[i], fieldsAndValues[i + 1]);
    }
    return doc;
  }

  private RoutingRule currentRoutingRule(String collection, String key) throws Exception {
    cluster.getZkStateReader().forceUpdateCollection(collection);
    Map<String, RoutingRule> rules =
        cluster.getZkStateReader().getCollection(collection).getSlice("shard1").getRoutingRules();
    return rules == null ? null : rules.get(key);
  }

  @Test
  public void multipleShardMigrateTest() throws Exception {

    String sourceCollection = "sourceCollection";
    CollectionAdminRequest.createCollection(sourceCollection, "conf", 2, 1)
        .process(cluster.getSolrClient());

    final String splitKey = "a";
    final int BIT_SEP = 1;
    int splitKeyCount = 0;
    for (int id = 0; id < 26 * 3; id++) {
      String shardKey =
          "" + (char) ('a' + (id % 26)); // See comment in ShardRoutingTest for hash distribution
      String key = shardKey;
      if (splitKey.equals(shardKey)) {
        key += "/" + BIT_SEP; // spread it over half the collection
      }
      SolrInputDocument doc = new SolrInputDocument();
      doc.addField("id", key + "!" + id);
      doc.addField("n_ti", id);
      cluster.getSolrClient().add(sourceCollection, doc);
      if (splitKey.equals(shardKey)) splitKeyCount++;
    }
    assertTrue(splitKeyCount > 0);

    String targetCollection = "migrate_multipleshardtest_targetCollection";
    CollectionAdminRequest.createCollection(targetCollection, "conf", 1, 1)
        .process(cluster.getSolrClient());

    Indexer indexer = new Indexer(cluster.getSolrClient(), sourceCollection, splitKey, 1, 30);
    indexer.start();

    DocCollection state = getCollectionState(targetCollection);
    Replica replica = state.replicaStream().findFirst().orElseThrow();
    SolrClient collectionClient = cluster.getSolrClient(replica);
    SolrQuery solrQuery = new SolrQuery("*:*");
    assertEquals(
        "DocCount on target collection does not match",
        0,
        collectionClient.query(solrQuery).getResults().getNumFound());

    invokeCollectionMigration(
        CollectionAdminRequest.migrateData(
                sourceCollection, targetCollection, splitKey + "/" + BIT_SEP + "!")
            .setForwardTimeout(45));

    long finishTime = System.nanoTime();

    indexer.join();
    splitKeyCount += indexer.getSplitKeyCount();

    try {
      cluster.getSolrClient().deleteById(sourceCollection, "a/" + BIT_SEP + "!104");
      splitKeyCount--;
    } catch (Exception e) {
      log.warn("Error deleting document a/{}!104", BIT_SEP, e);
    }
    cluster.getSolrClient().commit(sourceCollection);
    collectionClient.commit();

    solrQuery = new SolrQuery("*:*").setRows(1000);
    QueryResponse response = collectionClient.query(solrQuery);
    log.info("Response from target collection: {}", response);
    assertEquals(
        "DocCount on target collection does not match",
        splitKeyCount,
        response.getResults().getNumFound());

    waitForState(
        "Expected to find routing rule for split key " + splitKey,
        sourceCollection,
        c -> {
          if (c == null) return false;
          Slice shard = c.getSlice("shard2");
          if (shard == null) return false;
          if (shard.getRoutingRules() == null || shard.getRoutingRules().isEmpty()) return false;
          if (shard.getRoutingRules().get(splitKey + "!") == null) return false;
          return true;
        });

    boolean ruleRemoved = waitForRuleToExpire(sourceCollection, "shard2", splitKey, finishTime);
    assertTrue("Routing rule was not expired", ruleRemoved);
  }

  static class Indexer extends Thread {
    final int seconds;
    final CloudSolrClient cloudClient;
    final String splitKey;
    private final String collection;
    int splitKeyCount = 0;
    final int bitSep;

    public Indexer(
        CloudSolrClient cloudClient, String collection, String splitKey, int bitSep, int seconds) {
      this.cloudClient = cloudClient;
      this.collection = collection;
      this.splitKey = splitKey;
      this.bitSep = bitSep;
      this.seconds = seconds;
    }

    @Override
    public void run() {
      TimeOut timeout = new TimeOut(seconds, TimeUnit.SECONDS, TimeSource.NANO_TIME);
      for (int id = 26 * 3; id < 500 && !timeout.hasTimedOut(); id++) {
        String shardKey =
            "" + (char) ('a' + (id % 26)); // See comment in ShardRoutingTest for hash distribution
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", shardKey + (bitSep != -1 ? "/" + bitSep : "") + "!" + id);
        doc.addField("n_ti", id);
        try {
          cloudClient.add(collection, doc);
          if (splitKey.equals(shardKey)) splitKeyCount++;
        } catch (Exception e) {
          log.error("Exception while adding document id: {}", doc.getField("id"), e);
        }
        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          return;
        }
      }
    }

    public int getSplitKeyCount() {
      return splitKeyCount;
    }
  }
}
