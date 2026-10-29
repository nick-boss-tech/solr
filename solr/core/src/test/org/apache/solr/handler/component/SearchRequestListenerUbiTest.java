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
package org.apache.solr.handler.component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.cloud.ConfigRequest;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.ShardParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.common.util.Utils;
import org.apache.solr.core.SolrCore;
import org.apache.solr.embedded.JettySolrRunner;
import org.apache.solr.response.ResultContext;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.search.CallerSpecificQueryLimit;
import org.apache.solr.search.DocIterator;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.TestInjection;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/** Exercises the contract between a UBI-style preparation component and recording listener. */
public class SearchRequestListenerUbiTest extends SolrCloudTestCase {
  private static final String LOCAL_COLLECTION = "ubi_local";
  private static final String DISTRIBUTED_COLLECTION = "ubi_distributed";
  private static final List<String> IDS = List.of("1", "2", "3");
  private static final Queue<RecordedQuery> records = new ConcurrentLinkedQueue<>();
  private static final List<SearchRequestListener> listeners = new ArrayList<>();
  private static final AtomicBoolean failWrites = new AtomicBoolean();
  private static final AtomicBoolean sawPartialResults = new AtomicBoolean();
  private static final AtomicInteger callbacksAfterRecorder = new AtomicInteger();

  private record PreparedUbi(String queryId) {}

  private record RecordedQuery(
      String node, String queryId, String query, List<String> documentIds) {
    private RecordedQuery {
      documentIds = List.copyOf(documentIds);
    }
  }

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2)
        .addConfig("local", configset("cloud-minimal"))
        .addConfig("distributed", configset("cloud-minimal"))
        .configure();
    createCollection(LOCAL_COLLECTION, "local", 1, 2);
    createCollection(DISTRIBUTED_COLLECTION, "distributed", 2, 1);
  }

  private static void createCollection(String collection, String config, int shards, int replicas)
      throws Exception {
    CollectionAdminRequest.createCollection(collection, config, shards, replicas)
        .processAndWait(cluster.getSolrClient(), DEFAULT_TIMEOUT);
    cluster.waitForActiveCollection(collection, shards, shards * replicas);
    Map<String, Object> component =
        Map.of(
            "add-searchcomponent",
            Map.of("name", "ubi-prepare", "class", UbiPreparationComponent.class.getName()));
    cluster
        .getSolrClient()
        .request(new ConfigRequest(Utils.toJSONString(component)), collection);
    Map<String, Object> handlers =
        Map.of(
            "add-requesthandler",
            List.of(
                Map.of(
                    "name", "/ubi",
                    "class", "solr.SearchHandler",
                    "last-components", List.of("ubi-prepare")),
                Map.of("name", "/plain", "class", "solr.SearchHandler")));
    cluster
        .getSolrClient()
        .request(new ConfigRequest(Utils.toJSONString(handlers)), collection);
    new UpdateRequest()
        .add("id", "1")
        .add("id", "2")
        .add("id", "3")
        .commit(cluster.getSolrClient(), collection);
  }

  @Before
  public void registerListeners() {
    records.clear();
    failWrites.set(false);
    sawPartialResults.set(false);
    callbacksAfterRecorder.set(0);
    for (JettySolrRunner jetty : cluster.getJettySolrRunners()) {
      addListener(
          jetty,
          new UbiRecorder(
              jetty.getNodeName(),
              record -> {
                if (failWrites.get()) {
                  throw new IllegalStateException("recording destination unavailable");
                }
                records.add(record);
              }));
      addListener(
          jetty,
          info -> {
            callbacksAfterRecorder.incrementAndGet();
            sawPartialResults.set(info.partialResults());
          });
    }
  }

  private static void addListener(JettySolrRunner jetty, SearchRequestListener listener) {
    listeners.add(listener);
    jetty.getCoreContainer().addSearchRequestListener(listener);
  }

  @After
  public void removeListeners() {
    for (JettySolrRunner jetty : cluster.getJettySolrRunners()) {
      listeners.forEach(jetty.getCoreContainer()::removeSearchRequestListener);
    }
    listeners.clear();
  }

  private static ModifiableSolrParams params() {
    return new ModifiableSolrParams()
        .set("q", "*:*")
        .set("sort", "id asc")
        .set("fl", "id")
        .set("ubi", true);
  }

  private static QueryResponse query(
      String handler, boolean distributed, ModifiableSolrParams params) throws Exception {
    params.set("distrib", distributed);
    return new QueryRequest(handler, params)
        .process(
            cluster.getJettySolrRunner(0).getSolrClient(),
            distributed ? DISTRIBUTED_COLLECTION : LOCAL_COLLECTION);
  }

  private static RecordedQuery assertRecorded(QueryResponse response) {
    assertEquals(0, response.getStatus());
    assertEquals(3, response.getResults().getNumFound());
    assertEquals(1, records.size());
    RecordedQuery record = records.remove();
    assertEquals(IDS, record.documentIds());
    assertEquals(
        response.getResults().stream().map(doc -> doc.getFieldValue("id").toString()).toList(),
        record.documentIds());
    NamedList<?> ubi = (NamedList<?>) response.getResponse().get("ubi");
    assertNotNull(ubi);
    assertEquals(ubi.get("query_id"), record.queryId());
    assertFalse(record.queryId().isBlank());
    assertEquals("*:*", record.query());
    assertEquals(cluster.getJettySolrRunner(0).getNodeName(), record.node());
    assertEquals(1, callbacksAfterRecorder.get());
    return record;
  }

  @Test
  public void testLocalRecordingMatchesResponse() throws Exception {
    assertRecorded(query("/ubi", false, params()));
  }

  @Test
  public void testDistributedRecordingMatchesResponseOnce() throws Exception {
    assertRecorded(query("/ubi", true, params()));
  }

  @Test
  public void testSuppliedQueryIdIsPreserved() throws Exception {
    RecordedQuery record =
        assertRecorded(query("/ubi", true, params().set("query_id", "client-query-id")));
    assertEquals("client-query-id", record.queryId());
  }

  @Test
  public void testDisabledRecordingLeavesSearchUnchanged() throws Exception {
    QueryResponse response = query("/ubi", true, params().set("ubi", false));
    assertEquals(0, response.getStatus());
    assertEquals(3, response.getResults().getNumFound());
    assertNull(response.getResponse().get("ubi"));
    assertTrue(records.isEmpty());
    assertEquals(1, callbacksAfterRecorder.get());
  }

  @Test
  public void testParameterDoesNotEnableAnUnconfiguredHandler() throws Exception {
    QueryResponse response = query("/plain", true, params());
    assertEquals(0, response.getStatus());
    assertEquals(3, response.getResults().getNumFound());
    assertNull(response.getResponse().get("ubi"));
    assertTrue(records.isEmpty());
    assertEquals(1, callbacksAfterRecorder.get());
  }

  @Test
  public void testRecordingFailureDoesNotChangeResponseOrSkipNextListener() throws Exception {
    failWrites.set(true);
    QueryResponse response = query("/ubi", true, params());
    assertEquals(0, response.getStatus());
    assertEquals(IDS, response.getResults().stream().map(doc -> doc.getFieldValue("id")).toList());
    assertNotNull(response.getResponse().get("ubi"));
    assertTrue(records.isEmpty());
    assertEquals(1, callbacksAfterRecorder.get());
  }

  @Test
  public void testFailedSearchIsNotRecordedAsACompleteResult() {
    SolrException failure =
        expectThrows(SolrException.class, () -> query("/ubi", false, params().set("q", "id:(")));
    assertEquals(SolrException.ErrorCode.BAD_REQUEST.code, failure.code());
    assertTrue(records.isEmpty());
    assertEquals(1, callbacksAfterRecorder.get());
  }

  @Test
  public void testPartialSearchIsNotRecordedAsACompleteResult() throws Exception {
    TestInjection.queryTimeout = new CallerSpecificQueryLimit(Set.of("QueryComponent.process"));
    try {
      query("/ubi", false, params());
    } finally {
      TestInjection.queryTimeout = null;
    }
    assertTrue(sawPartialResults.get());
    assertTrue(records.isEmpty());
    assertEquals(1, callbacksAfterRecorder.get());
  }

  @Test
  public void testEmbeddedSnapshotRemainsUsableAfterRequestClose() throws Exception {
    QueryResponse response;
    JettySolrRunner jetty = cluster.getJettySolrRunner(0);
    String coreName =
        getCollectionState(LOCAL_COLLECTION)
            .replicaStream()
            .filter(replica -> replica.getNodeName().equals(jetty.getNodeName()))
            .map(Replica::getCoreName)
            .findFirst()
            .orElseThrow();
    try (SolrCore core = jetty.getCoreContainer().getCore(coreName);
        SolrClient client = new EmbeddedSolrServer(core)) {
      response = new QueryRequest("/ubi", params().set("distrib", false)).process(client);
    }
    RecordedQuery record = assertRecorded(response);
    assertEquals(IDS, record.documentIds());
    expectThrows(UnsupportedOperationException.class, () -> record.documentIds().add("late"));
  }

  /** Keeps query preparation and response mutation outside the read-only recording callback. */
  public static class UbiPreparationComponent extends SearchComponent {
    @Override
    public void prepare(ResponseBuilder rb) {
      if (!rb.req.getParams().getBool("ubi", false)
          || rb.req.getParams().getBool(ShardParams.IS_SHARD, false)) {
        return;
      }
      String queryId = rb.req.getParams().get("query_id");
      if (queryId == null) {
        queryId = UUID.randomUUID().toString();
      }
      rb.req.getContext().put(PreparedUbi.class, new PreparedUbi(queryId));
      rb.setNeedDocList(true);
    }

    @Override
    public void process(ResponseBuilder rb) {
      if (!rb.isDistributed()) {
        addResponseIdentity(rb);
      }
    }

    @Override
    public void finishStage(ResponseBuilder rb) {
      if (rb.getStage() == ResponseBuilder.STAGE_GET_FIELDS) {
        addResponseIdentity(rb);
      }
    }

    private static void addResponseIdentity(ResponseBuilder rb) {
      PreparedUbi prepared = (PreparedUbi) rb.req.getContext().get(PreparedUbi.class);
      if (prepared != null && rb.rsp.getValues().get("ubi") == null) {
        NamedList<Object> response = new SimpleOrderedMap<>();
        response.add("query_id", prepared.queryId());
        rb.rsp.add("ubi", response);
      }
    }

    @Override
    public String getDescription() {
      return "Prepares a UBI-style query identity for the listener contract tests";
    }
  }

  /** Copies the selected query and result data before passing it to a recording destination. */
  private static class UbiRecorder implements SearchRequestListener {
    private final String node;
    private final Consumer<RecordedQuery> destination;

    private UbiRecorder(String node, Consumer<RecordedQuery> destination) {
      this.node = node;
      this.destination = destination;
    }

    @Override
    public void onRequestEnd(SearchRequestInfo info) {
      PreparedUbi prepared = (PreparedUbi) info.req().getContext().get(PreparedUbi.class);
      if (prepared == null
          || info.failure() != null
          || !SolrQueryResponse.haveCompleteResults(info.rsp().getResponseHeader())) {
        return;
      }
      try {
        destination.accept(
            new RecordedQuery(
                node, prepared.queryId(), info.req().getParams().get("q"), documentIds(info)));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    private static List<String> documentIds(SearchRequestInfo info) throws IOException {
      Object response = info.rsp().getResponse();
      if (response instanceof SolrDocumentList docs) {
        return docs.stream().map(doc -> doc.getFieldValue("id").toString()).toList();
      }
      if (response instanceof ResultContext result) {
        List<String> ids = new ArrayList<>();
        IndexSchema schema = info.req().getSchema();
        SolrIndexSearcher searcher = info.req().getSearcher();
        String uniqueKey = schema.getUniqueKeyField().getName();
        for (DocIterator iterator = result.getDocList().iterator(); iterator.hasNext(); ) {
          ids.add(
              schema.printableUniqueKey(
                  searcher.getDocFetcher().doc(iterator.nextDoc(), Set.of(uniqueKey))));
        }
        return ids;
      }
      throw new IllegalStateException("No completed search results are available to the recorder");
    }
  }
}
