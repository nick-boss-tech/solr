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

import static org.apache.solr.SolrTestCaseJ4.assumeWorkingMockito;

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.SortSpec;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for the totals that {@link QueryComponent#mergeIds} reports when a partial shard is
 * skipped: the skipped shard contributes nothing to numFound or approximateTotalHits and clears
 * numFoundExact, and the approximateTotalHits fallback adds each shard's own count.
 */
public class QueryComponentMergeTotalsTest extends SolrTestCase {

  private static QueryComponent qc;

  private SolrQueryRequest currentRequest;

  @BeforeClass
  public static void setup() throws Exception {
    assumeWorkingMockito();
    SolrTestCaseJ4.newRandomConfig();
    SolrTestCaseJ4.initCore("solrconfig.xml", "schema-vector-catchall.xml");
    qc = new QueryComponent();
    qc.init(new NamedList<>());
  }

  @AfterClass
  public static void teardown() {
    SolrTestCaseJ4.deleteCore();
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

  private static SolrDocumentList docList(long numFound, SolrDocument... docs) {
    SolrDocumentList list = new SolrDocumentList();
    for (SolrDocument doc : docs) {
      list.add(doc);
    }
    list.setNumFound(numFound);
    list.setNumFoundExact(true);
    return list;
  }

  private static ShardResponse shardResponse(
      String shard,
      NamedList<Object> header,
      SolrDocumentList docs,
      NamedList<List<Object>> sortValues) {
    NamedList<Object> response = new NamedList<>();
    response.add("responseHeader", header);
    response.add("response", docs);
    if (sortValues != null) {
      response.add("sort_values", sortValues);
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

  private static ShardRequest shardRequest(ShardResponse... responses) {
    ShardRequest sreq = new ShardRequest();
    sreq.purpose = ShardRequest.PURPOSE_GET_TOP_IDS;
    sreq.responses = new ArrayList<>(List.of(responses));
    return sreq;
  }

  private ResponseBuilder newResponseBuilder(SortSpec sortSpec) {
    currentRequest = SolrTestCaseJ4.req();
    SolrQueryResponse rsp = new SolrQueryResponse();
    rsp.addResponseHeader(new SimpleOrderedMap<>());
    ResponseBuilder rb = new ResponseBuilder(currentRequest, rsp, List.of(qc));
    rb.setSortSpec(sortSpec);
    return rb;
  }

  private SortSpec intSortSpec() {
    SchemaField intField;
    SolrQueryRequest req = SolrTestCaseJ4.req();
    try {
      intField = req.getSchema().getField("intDefault");
    } finally {
      req.close();
    }
    return new SortSpec(
        new Sort(new SortField("intDefault", SortField.Type.INT)), List.of(intField), 10, 0);
  }

  private static NamedList<List<Object>> intSortValues(String... values) {
    NamedList<List<Object>> sortValues = new NamedList<>();
    sortValues.add("intDefault", List.<Object>of((Object[]) values));
    return sortValues;
  }

  @Test
  public void partialShardWithoutSortValuesIsSkipped() {
    SolrDocument a1 = doc("a1", 2.0f), a2 = doc("a2", 1.0f);
    SolrDocument b1 = doc("b1", 3.0f), b2 = doc("b2", 2.0f), b3 = doc("b3", 1.0f);
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
            docList(4, a1, a2),
            null);
    ShardResponse shard2 =
        shardResponse(
            "shard2",
            header(
                SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY,
                Boolean.TRUE,
                SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY,
                5L),
            docList(3, b1, b2, b3),
            intSortValues("1", "2", "3"));
    ResponseBuilder rb = newResponseBuilder(intSortSpec());
    qc.mergeIds(rb, shardRequest(shard1, shard2));
    assertEquals("numFound", 3, rb.getResponseDocs().getNumFound());
    assertEquals("numFoundExact", Boolean.FALSE, rb.getResponseDocs().getNumFoundExact());
    assertEquals("response size", 3, rb.getResponseDocs().size());
    assertEquals(
        "approximateTotalHits",
        5L,
        rb.rsp
            .getResponseHeader()
            .get(SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY));
    assertEquals(
        "partialResults",
        Boolean.TRUE,
        rb.rsp.getResponseHeader().get(SolrQueryResponse.RESPONSE_HEADER_PARTIAL_RESULTS_KEY));
  }

  @Test
  public void fallbackUsesEachCompleteShardsOwnCount() {
    SolrDocument a1 = doc("a1", 2.0f), a2 = doc("a2", 1.0f);
    SolrDocument b1 = doc("b1", 3.0f), b2 = doc("b2", 2.0f), b3 = doc("b3", 1.0f);
    ShardResponse shard1 =
        shardResponse(
            "shard1",
            header(SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY, Boolean.TRUE),
            docList(2, a1, a2),
            intSortValues("1", "2"));
    ShardResponse shard2 =
        shardResponse(
            "shard2",
            header(SolrQueryResponse.RESPONSE_HEADER_MAX_HITS_TERMINATED_EARLY_KEY, Boolean.TRUE),
            docList(3, b1, b2, b3),
            intSortValues("1", "2", "3"));
    ResponseBuilder rb = newResponseBuilder(intSortSpec());
    qc.mergeIds(rb, shardRequest(shard1, shard2));
    assertEquals("numFound", 5, rb.getResponseDocs().getNumFound());
    assertEquals(
        "approximateTotalHits",
        5L,
        rb.rsp
            .getResponseHeader()
            .get(SolrQueryResponse.RESPONSE_HEADER_APPROXIMATE_TOTAL_HITS_KEY));
  }
}
