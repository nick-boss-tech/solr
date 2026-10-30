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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.solr.client.solrj.io.Tuple;
import org.apache.solr.client.solrj.io.stream.TupleStream;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.cloud.ConfigRequest;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.ExecutorUtil;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SolrNamedThreadFactory;
import org.apache.solr.common.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/** Verifies that overlapping requests keep their UBI identities and recorded results together. */
public class UBIRecordingContextTest extends SolrCloudTestCase {
  private static final String COLLECTION = "ubi_context";
  private static final int REQUEST_TIMEOUT_SECONDS = DEFAULT_TIMEOUT;
  private static final int PAUSE_TIMEOUT_SECONDS = 3 * REQUEST_TIMEOUT_SECONDS;
  private static volatile RecordingPause recordingPause;

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1).addConfig("config", configset("cloud-minimal")).configure();
    CollectionAdminRequest.createCollection(COLLECTION, "config", 1, 1)
        .processAndWait(cluster.getSolrClient(), DEFAULT_TIMEOUT);
    cluster.waitForActiveCollection(COLLECTION, 1, 1);
    Map<String, Object> component =
        Map.of(
            "add-searchcomponent",
            Map.of("name", "ubi", "class", PausingUBIComponent.class.getName()));
    cluster.getSolrClient().request(new ConfigRequest(Utils.toJSONString(component)), COLLECTION);
    Map<String, Object> handler =
        Map.of(
            "add-requesthandler",
            Map.of(
                "name", "/ubi",
                "class", "solr.SearchHandler",
                "last-components", List.of("ubi")));
    cluster.getSolrClient().request(new ConfigRequest(Utils.toJSONString(handler)), COLLECTION);
    new UpdateRequest().add("id", "1").add("id", "2").commit(cluster.getSolrClient(), COLLECTION);
  }

  @Before
  public void resetRecording() throws IOException {
    recordingPause = null;
    Files.deleteIfExists(recordingFile());
  }

  @After
  public void releaseRecording() {
    RecordingPause pause = recordingPause;
    if (pause != null) {
      pause.release.countDown();
    }
    recordingPause = null;
  }

  @Test
  public void testConcurrentLocalRequestsKeepTheirOwnPayloads() throws Exception {
    assertConcurrentRecording(false);
  }

  @Test
  public void testConcurrentDistributedRequestsKeepTheirOwnPayloads() throws Exception {
    assertConcurrentRecording(true);
  }

  private static void assertConcurrentRecording(boolean distributed) throws Exception {
    RecordingPause pause = new RecordingPause();
    recordingPause = pause;
    ExecutorService executor =
        ExecutorUtil.newMDCAwareFixedThreadPool(2, new SolrNamedThreadFactory("ubi-context-test"));
    try {
      Future<QueryResponse> first =
          executor.submit(() -> query(distributed, params("1", "query-A", "first query")));
      assertTrue(
          "The first request must reach the recording boundary",
          pause.entered.await(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS));
      Future<QueryResponse> second =
          executor.submit(() -> query(distributed, params("2", "query-B", "second query")));
      assertResponse(second.get(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS), "query-B", "2");
      pause.release.countDown();
      assertResponse(first.get(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS), "query-A", "1");

      List<Map<?, ?>> records = readRecords();
      assertEquals(
          "Recorded query ids must match both successful responses in recording order",
          List.of("query-B", "query-A"),
          records.stream().map(record -> record.get("query_id")).toList());
      assertRecord(records.get(0), "query-B", "second query", "2");
      assertRecord(records.get(1), "query-A", "first query", "1");
    } finally {
      pause.release.countDown();
      executor.shutdownNow();
      assertTrue(
          "Query workers must stop before cluster teardown",
          executor.awaitTermination(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS));
    }
  }

  @Test
  public void testSequentialRequestsPreserveIdentityAndDocumentIds() throws Exception {
    assertResponse(query(false, params("1", "query-A", "first query")), "query-A", "1");
    assertResponse(query(true, params("2", "query-B", "second query")), "query-B", "2");
    List<Map<?, ?>> records = readRecords();
    assertEquals(2, records.size());
    assertRecord(records.get(0), "query-A", "first query", "1");
    assertRecord(records.get(1), "query-B", "second query", "2");
  }

  @Test
  public void testGeneratedQueryIdMatchesRecordedIdentity() throws Exception {
    ModifiableSolrParams params = params("1", "unused", "generated identity");
    params.remove("query_id");
    QueryResponse response = query(false, params);
    String queryId = (String) ((NamedList<?>) response.getResponse().get("ubi")).get("query_id");
    assertNotNull(queryId);
    assertFalse(queryId.isBlank());
    assertResponse(response, queryId, "1");
    List<Map<?, ?>> records = readRecords();
    assertEquals(1, records.size());
    assertRecord(records.get(0), queryId, "generated identity", "1");
  }

  @Test
  public void testDisabledRecordingLeavesNoRecord() throws Exception {
    QueryResponse response =
        query(false, params("1", "query-A", "first query").set("ubi", false));
    assertEquals(0, response.getStatus());
    assertEquals(1, response.getResults().getNumFound());
    assertNull(response.getResponse().get("ubi"));
    assertFalse(Files.exists(recordingFile()));
  }

  private static ModifiableSolrParams params(String documentId, String queryId, String userQuery) {
    return new ModifiableSolrParams()
        .set("q", "id:" + documentId)
        .set("fl", "id")
        .set("ubi", true)
        .set("query_id", queryId)
        .set("user_query", userQuery);
  }

  private static QueryResponse query(boolean distributed, ModifiableSolrParams params)
      throws Exception {
    // Exercise coordinator stages even though the single shard is on this node.
    params.set("distrib", distributed).set("shortCircuit", false).set("shards.info", distributed);
    QueryResponse response =
        new QueryRequest("/ubi", params)
            .process(cluster.getJettySolrRunner(0).getSolrClient(), COLLECTION);
    if (distributed) {
      assertNotNull(
          "The request must use distributed search", response.getResponse().get("shards.info"));
    }
    return response;
  }

  private static void assertResponse(QueryResponse response, String queryId, String documentId) {
    assertEquals(0, response.getStatus());
    assertEquals(1, response.getResults().getNumFound());
    assertEquals(documentId, response.getResults().get(0).getFieldValue("id"));
    assertEquals(queryId, ((NamedList<?>) response.getResponse().get("ubi")).get("query_id"));
  }

  private static Path recordingFile() {
    return cluster
        .getJettySolrRunner(0)
        .getCoreContainer()
        .getUserFilesPath()
        .resolve("ubi_queries.jsonl");
  }

  private static List<Map<?, ?>> readRecords() throws IOException {
    List<Map<?, ?>> records = new ArrayList<>();
    for (String line : Files.readAllLines(recordingFile(), StandardCharsets.UTF_8)) {
      records.add((Map<?, ?>) Utils.fromJSONString(line));
    }
    return records;
  }

  private static void assertRecord(
      Map<?, ?> record, String queryId, String userQuery, String documentId) {
    assertEquals(queryId, record.get("query_id"));
    assertEquals(userQuery, record.get("user_query"));
    assertEquals(documentId, record.get("doc_ids"));
  }

  private static class RecordingPause {
    private final AtomicBoolean first = new AtomicBoolean(true);
    private final CountDownLatch entered = new CountDownLatch(1);
    private final CountDownLatch release = new CountDownLatch(1);
  }

  /** Pauses after context assignment, before the real stream opens and consumes its payload. */
  public static class PausingUBIComponent extends UBIComponent {
    @Override
    protected Tuple getTuple(TupleStream stream) throws IOException {
      RecordingPause pause = recordingPause;
      if (pause != null && pause.first.compareAndSet(true, false)) {
        pause.entered.countDown();
        try {
          if (!pause.release.await(PAUSE_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            throw new IOException("Timed out waiting to resume the first UBI recording");
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new IOException("Interrupted while pausing UBI recording", e);
        }
      }
      return super.getTuple(stream);
    }
  }
}
