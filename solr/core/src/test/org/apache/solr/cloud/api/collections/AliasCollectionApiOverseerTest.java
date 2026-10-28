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

import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.RequestStatusState;
import org.apache.solr.cloud.Overseer;
import org.apache.solr.cloud.OverseerMessageHandler;
import org.apache.solr.cloud.OverseerNodePrioritizer;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.cloud.Stats;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.ZkNodeProps;
import org.apache.solr.common.params.CollectionAdminParams;
import org.apache.solr.common.params.CollectionParams.CollectionAction;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.TimeSource;
import org.apache.solr.handler.component.HttpShardHandlerFactory;
import org.apache.solr.util.TimeOut;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/** Alias validation and execution keep the collection API lock and error response consistent. */
public class AliasCollectionApiOverseerTest extends SolrCloudTestCase {
  private static final long WAIT_SECONDS = 30;

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1)
        .addConfig("conf", configset("cloud-minimal"))
        .withOverseer(true)
        .configure();
  }

  @After
  public void deleteCollections() throws Exception {
    cluster.deleteAllCollections();
  }

  private static CollectionAdminRequest.Delete deleteFollowingAliases(String collection) {
    CollectionAdminRequest.Delete request = CollectionAdminRequest.deleteCollection(collection);
    request.setFollowAliases(true);
    return request;
  }

  private static RequestStatusState state(String asyncId) {
    try {
      return CollectionAdminRequest.requestStatus(asyncId)
          .process(cluster.getSolrClient())
          .getRequestStatus();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testMultiCollectionAliasReturnsBadRequest() throws Exception {
    createCollectionsAndAlias("multiAlias", true);
    SolrException error =
        expectThrows(
            SolrException.class,
            () -> deleteFollowingAliases("multiAlias").process(cluster.getSolrClient()));
    assertEquals(SolrException.ErrorCode.BAD_REQUEST.code, error.code());
    assertTrue(error.getMessage().contains("points to more than 1 collection"));
    assertCollectionsExist();
  }

  @Test
  public void testAsyncMultiCollectionAliasPreservesErrorDetails() throws Exception {
    createCollectionsAndAlias("multiAlias", true);
    String asyncId = "deleteMultiCollectionAlias";
    deleteFollowingAliases("multiAlias").processAsync(asyncId, cluster.getSolrClient());
    new TimeOut(WAIT_SECONDS, TimeUnit.SECONDS, TimeSource.NANO_TIME)
        .waitFor("Async delete did not fail", () -> state(asyncId) == RequestStatusState.FAILED);
    var response = CollectionAdminRequest.requestStatus(asyncId).process(cluster.getSolrClient());
    assertTrue(response.getResponse().get("exception") instanceof NamedList<?>);
    SolrException error = (SolrException) response.getException();
    assertNotNull(error);
    assertEquals(SolrException.ErrorCode.BAD_REQUEST.code, error.code());
    assertTrue(error.getMessage().contains("points to more than 1 collection"));
    assertCollectionsExist();
  }

  @Test
  public void testAliasRetargetedAfterLockDoesNotDeleteUnlockedCollection() throws Exception {
    String alias = "movingAlias";
    createCollectionsAndAlias(alias, false);
    var cc = cluster.getJettySolrRunner(0).getCoreContainer();
    var reader = cc.getZkController().getZkStateReader();
    reader.aliasesManager.update();
    var overseer = cc.getZkController().getOverseer();
    var shardHandlerFactory = (HttpShardHandlerFactory) cc.getShardHandlerFactory();
    String adminPath = "/admin/cores";
    try (var handler =
        new OverseerCollectionMessageHandler(
            reader,
            "aliasLockTest",
            shardHandlerFactory,
            adminPath,
            new Stats(),
            overseer,
            new OverseerNodePrioritizer(reader, overseer, adminPath, shardHandlerFactory))) {
      ZkNodeProps message =
          new ZkNodeProps(
              Overseer.QUEUE_OPERATION,
              CollectionAction.DELETE.toLower(),
              CommonParams.NAME,
              alias,
              CollectionAdminParams.FOLLOW_ALIASES,
              "true");
      OverseerMessageHandler.Lock lock = handler.lockTask(message, 1, null);
      assertNotNull(lock);
      try {
        CollectionAdminRequest.createAlias(alias, "aliasTarget").process(cluster.getSolrClient());
        reader.aliasesManager.update();
        var response = handler.processMessage(message, CollectionAction.DELETE.toLower(), lock);
        SolrException error = (SolrException) response.getException();
        assertNotNull("DELETE must reject a collection different from its lock", error);
        assertEquals(SolrException.ErrorCode.CONFLICT.code, error.code());
        assertTrue(error.getMessage().contains("Collection alias changed"));
        assertCollectionsExist();
        assertEquals("aliasTarget", reader.getAliases().resolveSimpleAlias(alias));
      } finally {
        lock.unlock();
      }
    }
  }

  @Test
  public void testStableAliasStillDeletesCollectionAndAlias() throws Exception {
    createCollectionsAndAlias("stableAlias", false);
    deleteFollowingAliases("stableAlias").process(cluster.getSolrClient());
    var reader = cluster.getZkStateReader();
    reader.forceUpdateCollection("aliasOriginal");
    reader.aliasesManager.update();
    assertNull(reader.getClusterState().getCollectionOrNull("aliasOriginal"));
    assertFalse(reader.getAliases().hasAlias("stableAlias"));
    assertNotNull(reader.getClusterState().getCollectionOrNull("aliasTarget"));
  }

  private static void createCollectionsAndAlias(String alias, boolean multiple) throws Exception {
    CollectionAdminRequest.createCollection("aliasOriginal", "conf", 1, 1)
        .process(cluster.getSolrClient());
    CollectionAdminRequest.createCollection("aliasTarget", "conf", 1, 1)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection("aliasOriginal", 1, 1);
    cluster.waitForActiveCollection("aliasTarget", 1, 1);
    CollectionAdminRequest.createAlias(
            alias, multiple ? "aliasOriginal,aliasTarget" : "aliasOriginal")
        .process(cluster.getSolrClient());
  }

  private static void assertCollectionsExist() throws Exception {
    var reader = cluster.getZkStateReader();
    reader.forceUpdateCollection("aliasOriginal");
    reader.forceUpdateCollection("aliasTarget");
    assertNotNull(reader.getClusterState().getCollectionOrNull("aliasOriginal"));
    assertNotNull(reader.getClusterState().getCollectionOrNull("aliasTarget"));
  }
}
