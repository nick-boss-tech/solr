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

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.CloudHttp2SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.LBSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.DocRouter;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.ExecutorUtil;
import org.apache.solr.common.util.TimeSource;
import org.apache.solr.embedded.JettyConfig;
import org.apache.solr.embedded.JettySolrRunner;
import org.apache.solr.util.TimeOut;
import org.eclipse.jetty.server.handler.GracefulHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Checks update retries and exhaustion across real shards during graceful shutdown. */
public class ProbeGracefulMultiShardTest extends SolrTestCase {

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String SUCCESS_COLLECTION = "probe5315success";
  private static final String EXHAUSTION_COLLECTION = "probe5315exhaust";
  private static final String MARKER_PROPERTY = "solr.probe.gracefulMarker";

  private static final class ProbeCloudSolrClient extends CloudHttp2SolrClient {
    private String preferredCollection;
    private String preferredSlice;
    private String preferredUrl;
    private String orderedRouteCollection;
    private List<String> preferredRouteOrder;

    ProbeCloudSolrClient(CloudSolrClient.Builder builder) {
      super(builder);
    }

    void preferReplica(String collection, String slice, String url) {
      preferredCollection = collection;
      preferredSlice = slice;
      preferredUrl = url;
    }

    void preferRouteOrder(String collection, List<String> sliceOrder) {
      orderedRouteCollection = collection;
      preferredRouteOrder = List.copyOf(sliceOrder);
    }

    @Override
    protected Map<String, ? extends LBSolrClient.Req> createRoutes(
        UpdateRequest updateRequest,
        ModifiableSolrParams routableParams,
        DocCollection collection,
        DocRouter router,
        Map<String, List<String>> urlMap,
        String routeField) {
      if (urlMap != null && collection.getName().equals(preferredCollection)) {
        List<String> endpoints = urlMap.get(preferredSlice);
        if (endpoints != null) {
          List<String> ordered = new ArrayList<>(endpoints);
          if (!ordered.remove(preferredUrl)) {
            throw new IllegalStateException("Preferred probe replica is absent from route list");
          }
          ordered.add(0, preferredUrl);
          urlMap.put(preferredSlice, ordered);
        }
      }
      Map<String, ? extends LBSolrClient.Req> routes =
          super.createRoutes(updateRequest, routableParams, collection, router, urlMap, routeField);
      if (routes == null
          || urlMap == null
          || !collection.getName().equals(orderedRouteCollection)) {
        return routes;
      }
      Map<String, LBSolrClient.Req> orderedRoutes = new LinkedHashMap<>();
      for (String slice : preferredRouteOrder) {
        List<String> endpoints = urlMap.get(slice);
        if (endpoints != null && !endpoints.isEmpty()) {
          String endpoint = endpoints.get(0);
          LBSolrClient.Req route = routes.get(endpoint);
          if (route != null) {
            orderedRoutes.put(endpoint, route);
          }
        }
      }
      routes.forEach((endpoint, route) -> orderedRoutes.putIfAbsent(endpoint, route));
      return orderedRoutes;
    }
  }

  public void testMultiShardMarkedRejectionAndExhaustion() throws Exception {
    String previousMarkerProperty = System.getProperty(MARKER_PROPERTY);
    System.setProperty(MARKER_PROPERTY, "true");
    MiniSolrCloudCluster cluster = null;
    ExecutorService executor = ExecutorUtil.newMDCAwareCachedThreadPool("probe-5315-multishard");
    Map<String, JettySolrRunner.ProbeMarkingGracefulHandler> handlers = new HashMap<>();
    List<JettySolrRunner.ProbeMarkingGracefulHandler> gates = new ArrayList<>();
    try {
      // Four nodes let the RF=2 success collection place one replica of each shard on distinct
      // nodes with the default maxShardsPerNode, while RF=1 still has no alternate per shard.
      cluster =
          new MiniSolrCloudCluster(
              4, createTempDir(), JettyConfig.builder().enableGracefulShutdown(true).build());
      for (JettySolrRunner runner : cluster.getJettySolrRunners()) {
        JettySolrRunner.ProbeMarkingGracefulHandler handler =
            runner.getProbeMarkingGracefulHandler();
        assertNotNull("probe marker handler must be installed on " + runner.getNodeName(), handler);
        handlers.put(runner.getNodeName(), handler);
      }

      cluster.createCollection(
          CollectionAdminRequest.createCollection(SUCCESS_COLLECTION, "_default", 2, 2));
      cluster.createCollection(
          CollectionAdminRequest.createCollection(EXHAUSTION_COLLECTION, "_default", 2, 1));

      try (ProbeCloudSolrClient parallelClient = newCloudClient(cluster, true);
          ProbeCloudSolrClient sequentialClient = newCloudClient(cluster, false)) {
        DocCollection successCollection = collection(cluster, SUCCESS_COLLECTION);
        DocCollection exhaustionCollection = collection(cluster, EXHAUSTION_COLLECTION);
        Map<String, String> successIds = findOneIdPerShard(successCollection, "success");
        Map<String, String> parallelIds = findOneIdPerShard(exhaustionCollection, "parallel");
        Map<String, String> sequentialIds = findOneIdPerShard(exhaustionCollection, "sequential");
        String deleteByQueryId = "r4-trash-5315";

        seedCollection(parallelClient, SUCCESS_COLLECTION, successIds, deleteByQueryId);
        seedCollection(parallelClient, EXHAUSTION_COLLECTION, parallelIds, null);
        seedCollection(parallelClient, EXHAUSTION_COLLECTION, sequentialIds, null);

        UpdateRequest sequentialRequest = incrementRequest(sequentialIds);
        List<String> exhaustionSliceOrder =
            exhaustionCollection.getActiveSlices().stream().map(Slice::getName).sorted().toList();
        assertEquals(
            "RF=1 collection must route two shard subrequests", 2, exhaustionSliceOrder.size());
        String sequentialFirstSlice = exhaustionSliceOrder.get(0);
        String sequentialSecondSlice = exhaustionSliceOrder.get(1);
        String sequentialHealthyNode =
            exhaustionCollection.getSlice(sequentialFirstSlice).getLeader().getNodeName();
        String exhaustionTargetNode =
            exhaustionCollection.getSlice(sequentialSecondSlice).getLeader().getNodeName();
        assertNotEquals(
            "the two RF=1 shard endpoints must differ",
            sequentialHealthyNode,
            exhaustionTargetNode);
        parallelClient.preferRouteOrder(EXHAUSTION_COLLECTION, exhaustionSliceOrder);
        sequentialClient.preferRouteOrder(EXHAUSTION_COLLECTION, exhaustionSliceOrder);
        log.info(
            "R4_PROBE exhaustion routeOrder={} healthyNode={} targetNode={}",
            exhaustionSliceOrder,
            sequentialHealthyNode,
            exhaustionTargetNode);

        // For the success leg, reject a real non-leader replica before it reaches Solr's update
        // handler, then retry to the shard leader. The test-only route order keeps that second
        // endpoint deterministic and avoids routing an alternate replica back through the
        // draining leader as a forwarded update.
        Set<String> liveNodes = cluster.getZkStateReader().getClusterState().getLiveNodes();
        Slice successRejectSlice = null;
        Slice successCompletedSlice = null;
        Replica successRejectReplica = null;
        for (Slice candidate : successCollection.getActiveSlices()) {
          Replica nonLeader =
              candidate.getReplicas().stream()
                  .filter(
                      replica -> !replica.getNodeName().equals(candidate.getLeader().getNodeName()))
                  .filter(
                      replica ->
                          liveNodes.contains(replica.getNodeName())
                              && replica.getState() == Replica.State.ACTIVE
                              && !replica.getNodeName().equals(sequentialHealthyNode))
                  .findFirst()
                  .orElse(null);
          if (nonLeader != null) {
            successRejectSlice = candidate;
            successRejectReplica = nonLeader;
            for (Slice other : successCollection.getActiveSlices()) {
              if (!other.getName().equals(candidate.getName())) {
                successCompletedSlice = other;
                break;
              }
            }
            break;
          }
        }
        assertNotNull(
            "must find a live non-leader replica for the success rejection", successRejectSlice);
        assertNotNull(successCompletedSlice);
        assertNotNull(successRejectReplica);
        String successRejectNode = successRejectReplica.getNodeName();
        assertNotEquals(sequentialHealthyNode, successRejectNode);
        parallelClient.preferReplica(
            SUCCESS_COLLECTION, successRejectSlice.getName(), successRejectReplica.getCoreUrl());
        boolean hasLiveAlternate =
            successRejectSlice.getReplicas().stream()
                .anyMatch(
                    replica ->
                        !replica.getNodeName().equals(successRejectNode)
                            && liveNodes.contains(replica.getNodeName())
                            && replica.getState() == Replica.State.ACTIVE);
        assertTrue("success shard must have a live alternate endpoint", hasLiveAlternate);

        String successCompletedId = successIds.get(successCompletedSlice.getName());
        String successRejectedId = successIds.get(successRejectSlice.getName());
        UpdateRequest successRequest =
            incrementRequestForSlices(
                successIds, successCompletedSlice.getName(), successRejectSlice.getName());
        JettySolrRunner.ProbeMarkingGracefulHandler successRejectHandler =
            handlers.get(successRejectNode);
        resetCounters(handlers);
        successRejectHandler.armUpdateRejectionGate();
        gates.add(successRejectHandler);
        ((GracefulHandler) successRejectHandler).shutdown();
        Future<?> successFuture =
            executor.submit(() -> parallelClient.request(successRequest, SUCCESS_COLLECTION));
        boolean successBarrierEntered =
            successRejectHandler.awaitUpdateRejection(60, TimeUnit.SECONDS);
        int successCompletedBeforeRelease =
            awaitCount(cluster, successCollection, successCompletedId, 1, 30, TimeUnit.SECONDS);
        successRejectHandler.releaseUpdateRejectionGate();
        Throwable successFailure = awaitOutcome(successFuture);
        int successCompletedValue = readCount(cluster, successCollection, successCompletedId);
        int successRejectedValue = readCount(cluster, successCollection, successRejectedId);
        String successCounts = describeCounts(handlers);
        int successTotalRequests = totalRequests(handlers);
        int successTotalRejections = totalRejections(handlers);
        int successTotalPassed = totalPassed(handlers);
        Map<String, CoreCounts> successCoreCounts =
            snapshotCoreCounts(handlers, successCompletedSlice, successRejectSlice);
        log.info(
            "R4_PROBE success barrierEntered={} completedBeforeRelease={} nodeCounts={} coreCounts={} total={}/{} passed={} persisted completed={} retried={} failure={}",
            successBarrierEntered,
            successCompletedBeforeRelease,
            successCounts,
            describeCoreCounts(successCoreCounts),
            successTotalRequests,
            successTotalRejections,
            successTotalPassed,
            successCompletedValue,
            successRejectedValue,
            describeFailure(successFailure));

        // Exercise the post-route non-routable update phase while one leader is draining. This
        // records actual delete-by-query/commit behavior separately from the atomic-update counts.
        resetCounters(handlers);
        Throwable deleteFailure = null;
        Throwable commitFailure = null;
        try {
          UpdateRequest deleteRequest = new UpdateRequest();
          deleteRequest.deleteByQuery("id:" + deleteByQueryId);
          parallelClient.request(deleteRequest, SUCCESS_COLLECTION);
        } catch (Throwable t) {
          deleteFailure = t;
        }
        try {
          parallelClient.commit(SUCCESS_COLLECTION);
        } catch (Throwable t) {
          commitFailure = t;
        }
        SolrDocument deletedDocument = readDocument(cluster, successCollection, deleteByQueryId);
        String deleteCommitCounts = describeCounts(handlers);
        String deleteCommitCoreCounts =
            describeCoreCounts(
                snapshotCoreCounts(handlers, successCompletedSlice, successRejectSlice));
        log.info(
            "R4_PROBE deleteByQuery/commit nodeCounts={} coreCounts={} deletedDocPresent={} deleteFailure={} commitFailure={}",
            deleteCommitCounts,
            deleteCommitCoreCounts,
            deletedDocument != null,
            describeFailure(deleteFailure),
            describeFailure(commitFailure));

        // The RF=1 collection has no alternate. The target endpoint's first genuine rejection is
        // held at the Jetty boundary until RTG on the other shard proves its atomic increment is
        // already visible. The r4 correction must propagate the rejection without replaying the
        // completed route in either the parallel or sequential client.
        UpdateRequest parallelRequest = incrementRequest(parallelIds);
        String parallelHealthySlice = exhaustionSliceOrder.get(0);
        String parallelRejectedSlice = exhaustionSliceOrder.get(1);
        String parallelHealthyId = parallelIds.get(parallelHealthySlice);
        String parallelRejectedId = parallelIds.get(parallelRejectedSlice);
        JettySolrRunner.ProbeMarkingGracefulHandler exhaustionTargetHandler =
            handlers.get(exhaustionTargetNode);
        if (!successRejectNode.equals(exhaustionTargetNode)) {
          ((GracefulHandler) exhaustionTargetHandler).shutdown();
        }

        resetCounters(handlers);
        exhaustionTargetHandler.armUpdateRejectionGate();
        gates.add(exhaustionTargetHandler);
        Future<?> parallelFuture =
            executor.submit(() -> parallelClient.request(parallelRequest, EXHAUSTION_COLLECTION));
        boolean parallelBarrierEntered =
            exhaustionTargetHandler.awaitUpdateRejection(60, TimeUnit.SECONDS);
        int parallelCompletedBeforeRelease =
            awaitCount(cluster, exhaustionCollection, parallelHealthyId, 1, 30, TimeUnit.SECONDS);
        exhaustionTargetHandler.releaseUpdateRejectionGate();
        Throwable parallelFailure = awaitOutcome(parallelFuture);
        int parallelHealthyValue = readCount(cluster, exhaustionCollection, parallelHealthyId);
        int parallelRejectedValue = readCount(cluster, exhaustionCollection, parallelRejectedId);
        String parallelCounts = describeCounts(handlers);
        int parallelTotalRequests = totalRequests(handlers);
        int parallelTotalRejections = totalRejections(handlers);
        Map<String, CoreCounts> parallelCoreCounts =
            snapshotCoreCounts(
                handlers,
                exhaustionCollection.getSlice(parallelHealthySlice),
                exhaustionCollection.getSlice(parallelRejectedSlice));
        log.info(
            "R4_PROBE parallel exhaustion barrierEntered={} completedBeforeRelease={} nodeCounts={} coreCounts={} total={}/{} persisted completed={} rejected={} failure={}",
            parallelBarrierEntered,
            parallelCompletedBeforeRelease,
            parallelCounts,
            describeCoreCounts(parallelCoreCounts),
            parallelTotalRequests,
            parallelTotalRejections,
            parallelHealthyValue,
            parallelRejectedValue,
            describeFailure(parallelFailure));

        // The same RF=1 topology and ordering test the sequential path independently of the
        // parallel failure above by using a fresh document pair and re-arming the same rejection.
        resetCounters(handlers);
        exhaustionTargetHandler.armUpdateRejectionGate();
        Future<?> sequentialFuture =
            executor.submit(
                () -> sequentialClient.request(sequentialRequest, EXHAUSTION_COLLECTION));
        boolean sequentialBarrierEntered =
            exhaustionTargetHandler.awaitUpdateRejection(60, TimeUnit.SECONDS);
        int sequentialCompletedBeforeRelease =
            awaitCount(
                cluster,
                exhaustionCollection,
                sequentialIds.get(sequentialFirstSlice),
                1,
                30,
                TimeUnit.SECONDS);
        exhaustionTargetHandler.releaseUpdateRejectionGate();
        Throwable sequentialFailure = awaitOutcome(sequentialFuture);
        int sequentialHealthyValue =
            readCount(cluster, exhaustionCollection, sequentialIds.get(sequentialFirstSlice));
        int sequentialRejectedValue =
            readCount(cluster, exhaustionCollection, sequentialIds.get(sequentialSecondSlice));
        String sequentialCounts = describeCounts(handlers);
        int sequentialTotalRequests = totalRequests(handlers);
        int sequentialTotalRejections = totalRejections(handlers);
        Map<String, CoreCounts> sequentialCoreCounts =
            snapshotCoreCounts(
                handlers,
                exhaustionCollection.getSlice(sequentialFirstSlice),
                exhaustionCollection.getSlice(sequentialSecondSlice));
        log.info(
            "R4_PROBE sequential exhaustion barrierEntered={} completedBeforeRelease={} nodeCounts={} coreCounts={} total={}/{} persisted completed={} rejected={} failure={}",
            sequentialBarrierEntered,
            sequentialCompletedBeforeRelease,
            sequentialCounts,
            describeCoreCounts(sequentialCoreCounts),
            sequentialTotalRequests,
            sequentialTotalRejections,
            sequentialHealthyValue,
            sequentialRejectedValue,
            describeFailure(sequentialFailure));

        assertTrue("success path must reach the genuine rejection barrier", successBarrierEntered);
        assertNull("success path should use the alternate and complete", successFailure);
        assertEquals(1, successCompletedBeforeRelease);
        assertEquals(1, successCompletedValue);
        assertEquals(1, successRejectedValue);
        assertEquals(
            "all node-level update POSTs must be classified as passed or rejected",
            successTotalRequests,
            successTotalRejections + successTotalPassed);
        for (Replica replica : successCompletedSlice.getReplicas()) {
          assertCoreCounts(successCoreCounts, replica, 1, 0);
        }
        assertCoreCounts(successCoreCounts, successRejectSlice.getLeader(), 1, 0);
        assertCoreAllRejected(successCoreCounts, successRejectReplica);
        assertNull("delete-by-query should complete", deleteFailure);
        assertNull("commit should complete", commitFailure);
        assertNull("delete-by-query plus commit should remove the test document", deletedDocument);

        assertTrue(
            "parallel path must reach the genuine rejection barrier", parallelBarrierEntered);
        assertNotNull("parallel no-alternate leg should fail", parallelFailure);
        assertEquals(1, parallelCompletedBeforeRelease);
        assertEquals(
            "parallel exhaustion must not replay the completed shard", 1, parallelHealthyValue);
        assertEquals("rejected shard must remain unchanged", 0, parallelRejectedValue);
        assertCoreCounts(
            parallelCoreCounts,
            exhaustionCollection.getSlice(parallelHealthySlice).getLeader(),
            1,
            0);
        assertCoreAllRejected(
            parallelCoreCounts, exhaustionCollection.getSlice(parallelRejectedSlice).getLeader());

        assertTrue(
            "sequential path must reach the genuine rejection barrier", sequentialBarrierEntered);
        assertNotNull("sequential no-alternate leg should fail", sequentialFailure);
        assertEquals(1, sequentialCompletedBeforeRelease);
        assertEquals(1, sequentialHealthyValue);
        assertEquals(0, sequentialRejectedValue);
        assertCoreCounts(
            sequentialCoreCounts,
            exhaustionCollection.getSlice(sequentialFirstSlice).getLeader(),
            1,
            0);
        assertCoreAllRejected(
            sequentialCoreCounts, exhaustionCollection.getSlice(sequentialSecondSlice).getLeader());
      }
    } finally {
      for (JettySolrRunner.ProbeMarkingGracefulHandler handler : gates) {
        handler.releaseUpdateRejectionGate();
      }
      ExecutorUtil.shutdownAndAwaitTermination(executor);
      if (cluster != null) {
        cluster.shutdown();
      }
      if (previousMarkerProperty == null) {
        System.clearProperty(MARKER_PROPERTY);
      } else {
        System.setProperty(MARKER_PROPERTY, previousMarkerProperty);
      }
    }
  }

  private static ProbeCloudSolrClient newCloudClient(
      MiniSolrCloudCluster cluster, boolean parallel) {
    CloudSolrClient.Builder builder =
        new CloudHttp2SolrClient.Builder(cluster.getZkServer().getZkHost() + "/solr")
            .withHttpClientBuilder(
                HttpSolrClient.builder(null).withTrustedGracefulShutdownRejections(true))
            .withParallelUpdates(parallel)
            .sendUpdatesOnlyToShardLeaders()
            .sendDirectUpdatesToAnyShardReplica();
    return new ProbeCloudSolrClient(builder);
  }

  private static DocCollection collection(MiniSolrCloudCluster cluster, String name) {
    return cluster.getZkStateReader().getClusterState().getCollection(name);
  }

  private static LinkedHashMap<String, String> findOneIdPerShard(
      DocCollection collection, String prefix) {
    LinkedHashMap<String, String> ids = new LinkedHashMap<>();
    int candidate = 0;
    while (ids.size() < collection.getActiveSlices().size() && candidate < 10000) {
      String id = "r4-" + prefix + "-" + candidate++;
      Slice slice = collection.getRouter().getTargetSlice(id, null, null, null, collection);
      if (slice != null) {
        ids.putIfAbsent(slice.getName(), id);
      }
    }
    assertEquals(
        "must find an ID for each active shard", collection.getActiveSlices().size(), ids.size());
    return ids;
  }

  private static void seedCollection(
      CloudSolrClient client, String collection, Map<String, String> ids, String extraId)
      throws Exception {
    UpdateRequest seed = new UpdateRequest();
    for (String id : ids.values()) {
      SolrInputDocument document = new SolrInputDocument();
      document.setField("id", id);
      document.setField("count_i", 0);
      seed.add(document);
    }
    if (extraId != null) {
      SolrInputDocument document = new SolrInputDocument();
      document.setField("id", extraId);
      document.setField("count_i", 0);
      seed.add(document);
    }
    client.request(seed, collection);
    client.commit(collection);
  }

  private static UpdateRequest incrementRequest(Map<String, String> ids) {
    UpdateRequest request = new UpdateRequest();
    for (String id : ids.values()) {
      request.add(incrementDocument(id));
    }
    return request;
  }

  private static UpdateRequest incrementRequestForSlices(
      Map<String, String> ids, String firstSlice, String secondSlice) {
    UpdateRequest request = new UpdateRequest();
    request.add(incrementDocument(ids.get(firstSlice)));
    request.add(incrementDocument(ids.get(secondSlice)));
    return request;
  }

  private static SolrInputDocument incrementDocument(String id) {
    SolrInputDocument document = new SolrInputDocument("id", id);
    document.setField("count_i", Map.of("inc", 1));
    return document;
  }

  private static int awaitCount(
      MiniSolrCloudCluster cluster,
      DocCollection collection,
      String id,
      int expected,
      long timeout,
      TimeUnit unit)
      throws Exception {
    int[] value = {readCount(cluster, collection, id)};
    TimeOut timeOut = new TimeOut(timeout, unit, TimeSource.NANO_TIME);
    timeOut.waitFor(
        "timed out waiting for " + id + " to reach count " + expected,
        () -> {
          try {
            value[0] = readCount(cluster, collection, id);
            return value[0] == expected;
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
    return value[0];
  }

  private static int readCount(MiniSolrCloudCluster cluster, DocCollection collection, String id)
      throws Exception {
    SolrDocument document = readDocument(cluster, collection, id);
    if (document == null) {
      return -1;
    }
    Object value = document.getFieldValue("count_i");
    return value == null ? 0 : ((Number) value).intValue();
  }

  private static SolrDocument readDocument(
      MiniSolrCloudCluster cluster, DocCollection collection, String id) throws Exception {
    Slice slice = collection.getRouter().getTargetSlice(id, null, null, null, collection);
    Replica leader = slice.getLeader();
    JettySolrRunner runner = runnerForNode(cluster, leader.getNodeName());
    try (EmbeddedSolrServer embedded =
        new EmbeddedSolrServer(runner.getCoreContainer(), leader.getCoreName())) {
      ModifiableSolrParams localParams = new ModifiableSolrParams();
      localParams.set(CommonParams.DISTRIB, false);
      return embedded.getById(id, localParams);
    }
  }

  private static JettySolrRunner runnerForNode(MiniSolrCloudCluster cluster, String nodeName) {
    return cluster.getJettySolrRunners().stream()
        .filter(runner -> nodeName.equals(runner.getNodeName()))
        .findFirst()
        .orElseThrow(() -> new AssertionError("No JettySolrRunner for node " + nodeName));
  }

  private static Throwable awaitOutcome(Future<?> future) throws Exception {
    try {
      future.get(90, TimeUnit.SECONDS);
      return null;
    } catch (ExecutionException e) {
      return e.getCause();
    }
  }

  private static void resetCounters(
      Map<String, JettySolrRunner.ProbeMarkingGracefulHandler> handlers) {
    handlers.values().forEach(JettySolrRunner.ProbeMarkingGracefulHandler::resetUpdateCounters);
  }

  private static int totalRequests(
      Map<String, JettySolrRunner.ProbeMarkingGracefulHandler> handlers) {
    return handlers.values().stream()
        .mapToInt(JettySolrRunner.ProbeMarkingGracefulHandler::getUpdateRequestCount)
        .sum();
  }

  private static int totalRejections(
      Map<String, JettySolrRunner.ProbeMarkingGracefulHandler> handlers) {
    return handlers.values().stream()
        .mapToInt(JettySolrRunner.ProbeMarkingGracefulHandler::getRejectedUpdateRequestCount)
        .sum();
  }

  private static int totalPassed(
      Map<String, JettySolrRunner.ProbeMarkingGracefulHandler> handlers) {
    return handlers.values().stream()
        .mapToInt(JettySolrRunner.ProbeMarkingGracefulHandler::getPassedUpdateRequestCount)
        .sum();
  }

  private static String describeCounts(
      Map<String, JettySolrRunner.ProbeMarkingGracefulHandler> handlers) {
    return handlers.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(
            entry ->
                entry.getKey()
                    + "="
                    + entry.getValue().getUpdateRequestCount()
                    + "/reject="
                    + entry.getValue().getRejectedUpdateRequestCount()
                    + "/passed="
                    + entry.getValue().getPassedUpdateRequestCount())
        .toList()
        .toString();
  }

  private static Map<String, CoreCounts> snapshotCoreCounts(
      Map<String, JettySolrRunner.ProbeMarkingGracefulHandler> handlers, Slice... slices) {
    Map<String, CoreCounts> coreCounts = new LinkedHashMap<>();
    for (Slice slice : slices) {
      for (Replica replica : slice.getReplicas()) {
        JettySolrRunner.ProbeMarkingGracefulHandler handler = handlers.get(replica.getNodeName());
        coreCounts.put(
            coreKey(replica),
            new CoreCounts(
                handler.getUpdateRequestCount(replica.getCoreName()),
                handler.getRejectedUpdateRequestCount(replica.getCoreName())));
      }
    }
    return coreCounts;
  }

  private static String describeCoreCounts(Map<String, CoreCounts> coreCounts) {
    return coreCounts.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(
            entry ->
                entry.getKey()
                    + "="
                    + entry.getValue().requests()
                    + "/reject="
                    + entry.getValue().rejections())
        .toList()
        .toString();
  }

  private static void assertCoreCounts(
      Map<String, CoreCounts> coreCounts,
      Replica replica,
      int expectedRequests,
      int expectedRejects) {
    CoreCounts counts = coreCounts.get(coreKey(replica));
    assertNotNull("missing per-core counts for " + coreKey(replica), counts);
    assertEquals(
        "unexpected request count for " + coreKey(replica), expectedRequests, counts.requests());
    assertEquals(
        "unexpected rejection count for " + coreKey(replica), expectedRejects, counts.rejections());
  }

  private static void assertCoreAllRejected(Map<String, CoreCounts> coreCounts, Replica replica) {
    CoreCounts counts = coreCounts.get(coreKey(replica));
    assertNotNull("missing per-core counts for " + coreKey(replica), counts);
    assertTrue(
        "expected at least one marked rejection at " + coreKey(replica), counts.requests() > 0);
    assertEquals(
        "every POST to the draining core must be rejected", counts.requests(), counts.rejections());
  }

  private static String coreKey(Replica replica) {
    return replica.getNodeName() + "/" + replica.getCoreName();
  }

  private static String describeFailure(Throwable failure) {
    return failure == null ? "none" : failure.getClass().getName() + ": " + failure.getMessage();
  }

  private record CoreCounts(int requests, int rejections) {}
}
