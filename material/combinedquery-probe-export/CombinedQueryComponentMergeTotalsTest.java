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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CombinerParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.SortSpec;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Premise probe for the CombinedQueryComponent merge totals: fabricated shard responses driven
 * through {@link CombinedQueryComponent#mergeIds}. Assertions state the correct totals; on
 * unmodified main the defect cases fail with the wrong values recorded in the premise report.
 */
public class CombinedQueryComponentMergeTotalsTest extends SolrTestCaseJ4 {

  private static CombinedQueryComponent cqc;

  private SolrQueryRequest currentRequest;

  @BeforeClass
  public static void setup() throws Exception {
    assumeWorkingMockito();
    initCore("solrconfig.xml", "schema-vector-catchall.xml");
    cqc = new CombinedQueryComponent();
    cqc.init(new NamedList<>());
    cqc.inform(h.getCore());
  }

  @After
  public void closeRequest() {
    if (currentRequest != null) {
      currentRequest.close();
      currentRequest = null;
    }
  }

  private static SolrDocument doc(String id, float score) {
    SolrDocument doc = new SolrDocument();
    doc.addField("id", id);
    doc.addField("score", score);
    return doc;
  }

  private static SolrDocumentList docList(long numFound, Boolean exact, SolrDocument... docs) {
    SolrDocumentList list = new SolrDocumentList();
    for (SolrDocument doc : docs) {
      list.add(doc);
    }
    list.setNumFound(numFound);
    if (exact != null) {
      list.setNumFoundExact(exact);
    }
    return list;
  }

  private static ShardResponse shardResponse(
      String shard,
      NamedList<Object> header,
      SolrDocumentList combinedResponse,
      List<SolrDocumentList> perQuery,
      NamedList<List<Object>> sortValues0) {
    NamedList<Object> response = new NamedList<>();
    response.add("responseHeader", header);
    response.add("response", combinedResponse);
    response.add("response_per_query", perQuery);
    if (sortValues0 != null) {
      response.add("sort_values_0", sortValues0);
    }
    SolrResponse solrResponse = Mockito.mock(SolrResponse.class);
    Mockito.when(solrResponse.getResponse()).thenReturn(response);
    ShardResponse srsp = new ShardResponse();
    srsp.setShard(shard);
    srsp.setSolrResponse(solrResponse);
    return srsp;
  }

  private static NamedList<Object> header(Object... nameValues) {
    NamedList<Object> header = new NamedList<>();
    for (int i = 0; i < nameValues.length; i += 2) {
      header.add((String) nameValues[i], nameValues[i + 1]);
    }
    return header;
  }

  private ResponseBuilder newResponseBuilder(SortSpec sortSpec, String... combinerQueries) {
    List<String> params = new ArrayList<>();
    for (String q : combinerQueries) {
      params.add(CombinerParams.COMBINER_QUERY);
      params.add(q);
    }
    currentRequest = req(params.toArray(new String[0]));
    SolrQueryResponse rsp = new SolrQueryResponse();
    rsp.addResponseHeader(new SimpleOrderedMap<>());
    ResponseBuilder rb = new ResponseBuilder(currentRequest, rsp, List.of(cqc));
    rb.setSortSpec(sortSpec);
    return rb;
  }

  private static ShardRequest shardRequest(ShardResponse... responses) {
    ShardRequest sreq = new ShardRequest();
    sreq.purpose = ShardRequest.PURPOSE_GET_TOP_IDS;
    sreq.responses = new ArrayList<>(List.of(responses));
    return sreq;
  }

  private static SortSpec scoreSortSpec() {
    // one schema-field slot per sort field; the score slot is null, as in production
    return new SortSpec(
        new Sort(SortField.FIELD_SCORE), Arrays.asList((SchemaField) null), 10, 0);
  }

  /** Case F: fallback adds the running total (2 then 7) instead of each shard's own count (5). */
  @Test
  public void fallbackUsesEachShardsOwnCount() {
    SolrDocument a1 = doc("a1", 2.0f), a2 = doc("a2", 1.0f);
    SolrDocument b1 = doc("b1", 3.0f), b2 = doc("b2", 2.0f), b3 = doc("b3", 1.0f);
    ShardResponse shard1 =
        shardResponse(
            "shard1",
            header(SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY, Boolean.TRUE),
            docList(2, true, a1, a2),
            List.of(docList(2, true, a1, a2)),
            null);
    ShardResponse shard2 =
        shardResponse(
            "shard2",
            header(),
            docList(3, true, b1, b2, b3),
            List.of(docList(3, true, b1, b2, b3)),
            null);
    ResponseBuilder rb = newResponseBuilder(scoreSortSpec(), "q0");
    cqc.mergeIds(rb, shardRequest(shard1, shard2));
    assertEquals("F numFound", 5, rb.getResponseDocs().getNumFound());
    assertEquals(
        "F approximateTotalHits",
        5L,
        rb.rsp
            .getResponseHeader()
            .get(SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY));
  }

  /** Case P: a skipped partial shard has already folded its totals in before the continue. */
  @Test
  public void skippedPartialShardContributesNothing() {
    SolrDocument a1 = doc("a1", 2.0f), a2 = doc("a2", 1.0f);
    SolrDocument b1 = doc("b1", 3.0f), b2 = doc("b2", 2.0f), b3 = doc("b3", 1.0f);
    // sort_values_0 is keyed by field name; the value list is per doc, in doc order
    NamedList<List<Object>> sortValues = new NamedList<>();
    sortValues.add("intDefault", List.<Object>of("1", "2", "3"));
    ShardResponse shard1 =
        shardResponse(
            "shard1",
            header(
                SolrQueryResponse.RESPONSE_HEADER_PARTIAL_RESULTS_KEY,
                Boolean.TRUE,
                SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY,
                Boolean.TRUE,
                SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY,
                10L),
            docList(4, true, a1, a2),
            List.of(docList(4, true, a1, a2)),
            null);
    ShardResponse shard2 =
        shardResponse(
            "shard2",
            header(SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY, 5L),
            docList(3, true, b1, b2, b3),
            List.of(docList(3, true, b1, b2, b3)),
            sortValues);
    SchemaField intField = h.getCore().getLatestSchema().getField("intDefault");
    SortSpec sortSpec =
        new SortSpec(
            new Sort(new SortField("intDefault", SortField.Type.INT)), List.of(intField), 10, 0);
    ResponseBuilder rb = newResponseBuilder(sortSpec, "q0");
    cqc.mergeIds(rb, shardRequest(shard1, shard2));
    assertEquals("P numFound", 3, rb.getResponseDocs().getNumFound());
    assertEquals(
        "P approximateTotalHits",
        5L,
        rb.rsp
            .getResponseHeader()
            .get(SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY));
    assertEquals("P response size", 3, rb.getResponseDocs().size());
  }

  /** Case P2: same setup as P, asserting only the approximate total (10 + 5 = 15 on main). */
  @Test
  public void skippedPartialShardApproximateTotalCounted() {
    SolrDocument a1 = doc("a1", 2.0f), a2 = doc("a2", 1.0f);
    SolrDocument b1 = doc("b1", 3.0f), b2 = doc("b2", 2.0f), b3 = doc("b3", 1.0f);
    NamedList<List<Object>> sortValues = new NamedList<>();
    sortValues.add("intDefault", List.<Object>of("1", "2", "3"));
    ShardResponse shard1 =
        shardResponse(
            "shard1",
            header(
                SolrQueryResponse.RESPONSE_HEADER_PARTIAL_RESULTS_KEY,
                Boolean.TRUE,
                SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY,
                Boolean.TRUE,
                SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY,
                10L),
            docList(4, true, a1, a2),
            List.of(docList(4, true, a1, a2)),
            null);
    ShardResponse shard2 =
        shardResponse(
            "shard2",
            header(SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY, 5L),
            docList(3, true, b1, b2, b3),
            List.of(docList(3, true, b1, b2, b3)),
            sortValues);
    SchemaField intField = h.getCore().getLatestSchema().getField("intDefault");
    SortSpec sortSpec =
        new SortSpec(
            new Sort(new SortField("intDefault", SortField.Type.INT)), List.of(intField), 10, 0);
    ResponseBuilder rb = newResponseBuilder(sortSpec, "q0");
    cqc.mergeIds(rb, shardRequest(shard1, shard2));
    assertEquals(
        "P2 approximateTotalHits",
        5L,
        rb.rsp
            .getResponseHeader()
            .get(SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY));
    assertEquals("P2 response size", 3, rb.getResponseDocs().size());
  }

  /** Case X1: both shards report exact counts; the merged flag must stay exact. */
  @Test
  public void exactnessAllExactStaysExact() {
    SolrDocument a1 = doc("a1", 2.0f), a2 = doc("a2", 1.0f);
    SolrDocument b1 = doc("b1", 3.0f), b2 = doc("b2", 2.0f), b3 = doc("b3", 1.0f);
    ShardResponse shard1 =
        shardResponse(
            "shard1", header(), docList(2, true, a1, a2), List.of(docList(2, true, a1, a2)), null);
    ShardResponse shard2 =
        shardResponse(
            "shard2",
            header(),
            docList(3, true, b1, b2, b3),
            List.of(docList(3, true, b1, b2, b3)),
            null);
    ResponseBuilder rb = newResponseBuilder(scoreSortSpec(), "q0");
    cqc.mergeIds(rb, shardRequest(shard1, shard2));
    assertEquals("X1 numFoundExact", Boolean.TRUE, rb.getResponseDocs().getNumFoundExact());
  }

  /** Case X2: both shards report inexact counts; the merged flag must be inexact. */
  @Test
  public void exactnessAllInexactStaysInexact() {
    SolrDocument a1 = doc("a1", 2.0f), a2 = doc("a2", 1.0f);
    SolrDocument b1 = doc("b1", 3.0f), b2 = doc("b2", 2.0f), b3 = doc("b3", 1.0f);
    ShardResponse shard1 =
        shardResponse(
            "shard1",
            header(),
            docList(2, false, a1, a2),
            List.of(docList(2, false, a1, a2)),
            null);
    ShardResponse shard2 =
        shardResponse(
            "shard2",
            header(),
            docList(3, false, b1, b2, b3),
            List.of(docList(3, false, b1, b2, b3)),
            null);
    ResponseBuilder rb = newResponseBuilder(scoreSortSpec(), "q0");
    cqc.mergeIds(rb, shardRequest(shard1, shard2));
    assertEquals("X2 numFoundExact", Boolean.FALSE, rb.getResponseDocs().getNumFoundExact());
  }

  /**
   * Case M1 (maximum evidence): two queries. q0 fallback total is 7 on main (correct 5); q1
   * fallback total is 21 on main (correct 11). The merged value on main is q1's own inflated 21:
   * the winning query's error passes through, q0's error is masked, nothing is amplified.
   */
  @Test
  public void maximumFoldWinningQueryErrorPassesThrough() {
    SolrDocument a1 = doc("a1", 5.0f), a2 = doc("a2", 4.0f), a3 = doc("a3", 3.0f);
    SolrDocument b1 = doc("b1", 5.0f), b2 = doc("b2", 4.0f), b3 = doc("b3", 3.0f), b4 = doc("b4", 2.0f);
    ShardResponse shard1 =
        shardResponse(
            "shard1",
            header(SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY, Boolean.TRUE),
            docList(3, true, a1, a2, a3),
            List.of(docList(2, true, a1, a2), docList(10, true, a3)),
            null);
    ShardResponse shard2 =
        shardResponse(
            "shard2",
            header(),
            docList(4, true, b1, b2, b3, b4),
            List.of(docList(3, true, b1, b2, b3), docList(1, true, b4)),
            null);
    ResponseBuilder rb = newResponseBuilder(scoreSortSpec(), "q0", "q1");
    cqc.mergeIds(rb, shardRequest(shard1, shard2));
    assertEquals("M1 numFound", 11, rb.getResponseDocs().getNumFound());
    assertEquals(
        "M1 approximateTotalHits",
        11L,
        rb.rsp
            .getResponseHeader()
            .get(SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY));
  }

  /**
   * Case M2 (maximum evidence): same q0 as M1 (7 on main, correct 5); q1 is small (3 on main,
   * correct 2). The merged value on main is q0's inflated 7: again the winner's error passes
   * through and the loser's is masked.
   */
  @Test
  public void maximumFoldOtherQueryWinning() {
    SolrDocument a1 = doc("a1", 5.0f), a2 = doc("a2", 4.0f), a3 = doc("a3", 3.0f);
    SolrDocument b1 = doc("b1", 5.0f), b2 = doc("b2", 4.0f), b3 = doc("b3", 3.0f), b4 = doc("b4", 2.0f);
    ShardResponse shard1 =
        shardResponse(
            "shard1",
            header(SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY, Boolean.TRUE),
            docList(3, true, a1, a2, a3),
            List.of(docList(2, true, a1, a2), docList(1, true, a3)),
            null);
    ShardResponse shard2 =
        shardResponse(
            "shard2",
            header(),
            docList(4, true, b1, b2, b3, b4),
            List.of(docList(3, true, b1, b2, b3), docList(1, true, b4)),
            null);
    ResponseBuilder rb = newResponseBuilder(scoreSortSpec(), "q0", "q1");
    cqc.mergeIds(rb, shardRequest(shard1, shard2));
    assertEquals("M2 numFound", 5, rb.getResponseDocs().getNumFound());
    assertEquals(
        "M2 approximateTotalHits",
        5L,
        rb.rsp
            .getResponseHeader()
            .get(SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY));
  }
}
