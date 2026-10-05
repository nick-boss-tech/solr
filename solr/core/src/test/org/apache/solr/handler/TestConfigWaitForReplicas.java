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
package org.apache.solr.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.apache.solr.client.solrj.RemoteSolrException;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.jetty.HttpJettySolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.GenericSolrRequest;
import org.apache.solr.client.solrj.request.RequestWriter;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.Utils;
import org.apache.zookeeper.data.Stat;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * End to end tests for the replica wait behind a config API update ({@code SolrConfigHandler}
 * waiting for every replica to report the new config version).
 *
 * <p>Each scenario posts a real {@code set-user-property} command to one core of a two replica
 * collection. The other replica is staged so that it can never report the new version: its core
 * name in the collection state is pointed at a core that does not exist, so the wait's version
 * checks against it fail for the whole wait. The collection state is edited directly in ZooKeeper
 * (the same technique as {@code TestQueryingOnDownCollection}) because staging this through the
 * collections API would unload the replica's core before the wait observes anything, and whether
 * the replica reported the version first would depend on timing. The request itself, the wait and
 * the version checks all run through the real code paths.
 *
 * <p>What the branch under test does, and what these tests pin: a replica that failed the wait is
 * excused if, when the wait ends, it is no longer an active replica of the collection, because it
 * was removed from the collection or because it is not active anymore. A replica that is still
 * active and never reported the new version still fails the request; that case (a stale replica
 * that still exists) is not covered by the branch and {@link
 * #testActiveReplicaThatNeverReportsStillFails} pins that it keeps failing.
 */
public class TestConfigWaitForReplicas extends SolrCloudTestCase {

  private static final String CONF_NAME = "conf17363";

  @BeforeClass
  public static void setupCluster() throws Exception {
    System.setProperty("managed.schema.mutable", "true");
    configureCluster(2)
        .addConfig(
            CONF_NAME, TEST_PATH().resolve("configsets").resolve("cloud-managed").resolve("conf"))
        .configure();
  }

  @Test
  public void testReplicaRemovedDuringWaitDoesNotFailRequest() throws Exception {
    String coll = createCollection("coll17363removed");
    try {
      Outcome outcome = runConfigUpdateWhileReplicaLeaves(coll, true);
      assertConfigRequestSucceeded(outcome);
    } finally {
      deleteCollectionQuietly(coll);
    }
  }

  @Test
  public void testReplicaDownDuringWaitDoesNotFailRequest() throws Exception {
    String coll = createCollection("coll17363down");
    try {
      Outcome outcome = runConfigUpdateWhileReplicaLeaves(coll, false);
      assertConfigRequestSucceeded(outcome);
    } finally {
      deleteCollectionQuietly(coll);
    }
  }

  @Test
  public void testActiveReplicaThatNeverReportsStillFails() throws Exception {
    String coll = createCollection("coll17363stale");
    try {
      Replica[] pair = handlerAndOther(coll);
      Replica leaving = pair[1];
      String unreachableCore = breakReplicaCore(coll, leaving);

      Outcome outcome = postConfigUpdate(pair[0], propertyName("stale"));
      joinAndAssertFinished(outcome);

      assertNotNull("config request should have failed", outcome.error);
      assertTrue(
          "expected a RemoteSolrException but got: " + outcome.error,
          outcome.error instanceof RemoteSolrException);
      RemoteSolrException e = (RemoteSolrException) outcome.error;
      assertEquals(500, e.code());
      assertTrue(
          "error should be the config wait failure but was: " + e.getMessage(),
          e.getMessage().contains("the property overlay to be of version"));
      assertTrue(
          "error should name the failed core but was: " + e.getMessage(),
          e.getMessage().contains(unreachableCore));
    } finally {
      deleteCollectionQuietly(coll);
    }
  }

  @Test
  public void testConfigUpdateSucceedsWhenAllReplicasReport() throws Exception {
    String coll = createCollection("coll17363healthy");
    try {
      Replica[] pair = handlerAndOther(coll);
      String prop = propertyName("healthy");
      Outcome outcome = postConfigUpdate(pair[0], prop);
      joinAndAssertFinished(outcome);
      assertConfigRequestSucceeded(outcome);

      // the property is part of the config the other replica serves
      try (HttpJettySolrClient client =
          new HttpJettySolrClient.Builder(pair[1].getBaseUrl() + "/" + pair[1].getCoreName())
              .build()) {
        NamedList<Object> overlay =
            client.request(new GenericSolrRequest(SolrRequest.METHOD.GET, "/config/overlay"));
        assertTrue(
            "other replica should serve the new user property",
            String.valueOf(overlay).contains(prop));
      }
    } finally {
      deleteCollectionQuietly(coll);
    }
  }

  /**
   * Posts a config update whose wait includes a replica that can never report the new version, then
   * removes that replica from the collection state (or marks it down) while the wait is still
   * running, and returns the request's outcome.
   */
  private Outcome runConfigUpdateWhileReplicaLeaves(String coll, boolean removeReplica)
      throws Exception {
    Replica[] pair = handlerAndOther(coll);
    Replica leaving = pair[1];
    breakReplicaCore(coll, leaving);

    int versionBefore = overlayZnodeVersion();
    Outcome outcome = postConfigUpdate(pair[0], propertyName(removeReplica ? "removed" : "down"));
    waitForOverlayVersion(versionBefore, outcome);

    // the update is persisted, so the wait is running with the staged replica in its task list;
    // only now does the replica leave the active set
    if (removeReplica) {
      // The staged replica's real core reloads when it processes the config change, and a reload
      // looks the replica up in the collection state. Wait until that core reports the new
      // overlay version, which means it has processed the change, before removing its entry, so
      // the reload cannot race the removal.
      waitForCoreOverlayVersion(leaving, overlayZnodeVersion());
      editCollectionState(coll, collState -> replicasMap(collState).remove(leaving.getName()));
      waitForHandlerState(
          coll, dc -> findReplica(dc, leaving.getName()) == null, "replica to be removed");
    } else {
      editCollectionState(
          coll,
          collState ->
              replicaProps(collState, leaving.getName())
                  .put(ZkStateReader.STATE_PROP, Replica.State.DOWN.toString()));
      waitForHandlerState(
          coll,
          dc -> {
            Replica r = findReplica(dc, leaving.getName());
            return r != null && r.getState() == Replica.State.DOWN;
          },
          "replica to be down");
    }

    joinAndAssertFinished(outcome);
    return outcome;
  }

  /**
   * Points the replica's core name in the collection state at a core that does not exist, so the
   * wait's version checks against it can never succeed, while the replica stays an active replica
   * on a live node. Returns the replacement core name.
   */
  private String breakReplicaCore(String coll, Replica replica) throws Exception {
    String unreachableCore = replica.getCoreName() + "_gone";
    editCollectionState(
        coll,
        collState ->
            replicaProps(collState, replica.getName())
                .put(ZkStateReader.CORE_NAME_PROP, unreachableCore));
    waitForHandlerState(
        coll,
        dc -> {
          Replica r = findReplica(dc, replica.getName());
          return r != null && unreachableCore.equals(r.getCoreName());
        },
        "staged core name to be visible");
    return unreachableCore;
  }

  private void assertConfigRequestSucceeded(Outcome outcome) {
    if (outcome.error != null) {
      fail("config request should have succeeded but failed: " + outcome.error);
    }
    assertNotNull(outcome.response);
    @SuppressWarnings("unchecked")
    NamedList<Object> header = (NamedList<Object>) outcome.response.get("responseHeader");
    assertEquals(0, ((Number) header.get("status")).intValue());
  }

  private static class Outcome {
    volatile NamedList<Object> response;
    volatile Exception error;
    volatile Thread thread;
  }

  /** Posts a {@code set-user-property} command to the given replica's core, asynchronously. */
  private Outcome postConfigUpdate(Replica handlerReplica, String prop) {
    Outcome outcome = new Outcome();
    Thread thread =
        new Thread(
            () -> {
              try (HttpJettySolrClient client =
                  new HttpJettySolrClient.Builder(
                          handlerReplica.getBaseUrl() + "/" + handlerReplica.getCoreName())
                      .build()) {
                GenericSolrRequest request =
                    new GenericSolrRequest(SolrRequest.METHOD.POST, "/config");
                request.setContentWriter(
                    new RequestWriter.StringPayloadContentWriter(
                        "{\"set-user-property\": {\"" + prop + "\": \"v\"}}",
                        CommonParams.JSON_MIME));
                outcome.response = client.request(request);
              } catch (Exception e) {
                outcome.error = e;
              }
            });
    outcome.thread = thread;
    thread.start();
    return outcome;
  }

  private void joinAndAssertFinished(Outcome outcome) throws Exception {
    outcome.thread.join(TimeUnit.SECONDS.toMillis(240));
    assertFalse("config request did not finish", outcome.thread.isAlive());
  }

  private int overlayZnodeVersion() throws Exception {
    Stat stat = cluster.getZkClient().exists("/configs/" + CONF_NAME + "/configoverlay.json", null);
    return stat == null ? -1 : stat.getVersion();
  }

  /** Waits until the config update is persisted in ZooKeeper, which starts the replica wait. */
  private void waitForOverlayVersion(int versionBefore, Outcome outcome) throws Exception {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(60);
    while (System.nanoTime() < deadline) {
      if (outcome.error != null) {
        fail("config request failed before the update was persisted: " + outcome.error);
      }
      if (overlayZnodeVersion() > versionBefore) {
        return;
      }
      Thread.sleep(100);
    }
    fail("timed out waiting for the config update to be persisted");
  }

  /**
   * Waits until the given replica's real core reports the given overlay version from its {@code
   * /config/znodeVersion} endpoint, which means the core has processed the config change (including
   * any reload the change triggers).
   */
  private void waitForCoreOverlayVersion(Replica replica, int expectedVersion) throws Exception {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(20);
    try (HttpJettySolrClient client =
        new HttpJettySolrClient.Builder(replica.getBaseUrl() + "/" + replica.getCoreName())
            .build()) {
      while (System.nanoTime() < deadline) {
        try {
          GenericSolrRequest request =
              new GenericSolrRequest(
                  SolrRequest.METHOD.GET,
                  "/config/znodeVersion",
                  new ModifiableSolrParams().set(CommonParams.WT, CommonParams.JAVABIN));
          NamedList<Object> rsp = client.request(request);
          Object versions = rsp.get("znodeVersion");
          Object overlay = null;
          if (versions instanceof Map<?, ?> map) {
            overlay = map.get("overlay");
          } else if (versions instanceof NamedList<?> list) {
            overlay = list.get("overlay");
          }
          if (overlay instanceof Number number && number.intValue() >= expectedVersion) {
            return;
          }
        } catch (Exception ignored) {
          // the core may be mid reload; keep polling
        }
        Thread.sleep(100);
      }
    }
    fail(
        "timed out waiting for core "
            + replica.getCoreName()
            + " to report overlay version "
            + expectedVersion);
  }

  /** Waits until the node serving the config request sees the given collection state. */
  private void waitForHandlerState(String coll, Predicate<DocCollection> condition, String what)
      throws Exception {
    ZkStateReader reader =
        cluster.getJettySolrRunner(0).getCoreContainer().getZkController().getZkStateReader();
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(60);
    while (System.nanoTime() < deadline) {
      DocCollection dc = reader.getClusterState().getCollectionOrNull(coll);
      if (dc != null && condition.test(dc)) {
        return;
      }
      Thread.sleep(100);
    }
    fail("timed out waiting for the handler node state: " + what);
  }

  /** The replica on the first node (which serves the config request) and the other replica. */
  private Replica[] handlerAndOther(String coll) {
    DocCollection dc = getCollectionState(coll);
    String firstNodeBase =
        stripTrailingSlash(cluster.getJettySolrRunner(0).getBaseUrl().toString());
    Replica handler = null;
    Replica other = null;
    for (Replica replica : allReplicas(dc)) {
      if (stripTrailingSlash(replica.getBaseUrl()).equals(firstNodeBase)) {
        handler = replica;
      } else {
        other = replica;
      }
    }
    assertNotNull("no replica on the first node", handler);
    assertNotNull("no replica on the second node", other);
    return new Replica[] {handler, other};
  }

  private static Replica findReplica(DocCollection dc, String replicaName) {
    for (Replica replica : allReplicas(dc)) {
      if (replica.getName().equals(replicaName)) {
        return replica;
      }
    }
    return null;
  }

  private static List<Replica> allReplicas(DocCollection dc) {
    List<Replica> replicas = new ArrayList<>();
    dc.getSlices().forEach(slice -> replicas.addAll(slice.getReplicas()));
    return replicas;
  }

  private static String stripTrailingSlash(String url) {
    return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
  }

  private static String propertyName(String test) {
    return "solr17363." + test;
  }

  private String createCollection(String name) throws Exception {
    CollectionAdminRequest.createCollection(name, CONF_NAME, 1, 2).process(cluster.getSolrClient());
    cluster.waitForActiveCollection(name, 1, 2);
    return name;
  }

  private void deleteCollectionQuietly(String name) {
    try {
      CollectionAdminRequest.deleteCollection(name).process(cluster.getSolrClient());
    } catch (Exception ignored) {
      // the cluster is shut down at the end of the class either way
    }
  }

  private void editCollectionState(String coll, Consumer<Map<String, Object>> editor)
      throws Exception {
    String path = "/collections/" + coll + "/state.json";
    byte[] data = cluster.getZkClient().getData(path, null, null);
    @SuppressWarnings("unchecked")
    Map<String, Object> root = (Map<String, Object>) Utils.fromJSON(data);
    @SuppressWarnings("unchecked")
    Map<String, Object> collState = (Map<String, Object>) root.get(coll);
    editor.accept(collState);
    cluster.getZkClient().setData(path, Utils.toJSON(root));
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Object> replicasMap(Map<String, Object> collState) {
    Map<String, Object> shards = (Map<String, Object>) collState.get("shards");
    assertEquals(1, shards.size());
    Map<String, Object> shard = (Map<String, Object>) shards.values().iterator().next();
    return (Map<String, Object>) shard.get("replicas");
  }

  private static Map<String, Object> replicaProps(
      Map<String, Object> collState, String replicaName) {
    @SuppressWarnings("unchecked")
    Map<String, Object> props = (Map<String, Object>) replicasMap(collState).get(replicaName);
    assertNotNull("replica not in collection state: " + replicaName, props);
    return props;
  }
}
