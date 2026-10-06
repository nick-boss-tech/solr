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
package org.apache.solr.search;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.tests.util.TestUtil;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.util.TestInjection;
import org.apache.solr.util.ThreadCpuTimer;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestQueryLimits extends SolrCloudTestCase {

  private static final String COLLECTION = "test";

  @BeforeClass
  public static void setupCluster() throws Exception {
    System.setProperty(ThreadCpuTimer.ENABLE_CPU_TIME, "true");
    configureCluster(4).addConfig("conf", configset("exitable-directory")).configure();
    SolrClient solrClient = cluster.getSolrClient();
    CollectionAdminRequest.Create create =
        CollectionAdminRequest.createCollection(COLLECTION, "conf", 3, 2);
    create.process(solrClient);
    waitForState("active", COLLECTION, clusterShape(3, 6));
    for (int j = 0; j < 100; j++) {
      solrClient.add(
          COLLECTION,
          sdoc(
              "id",
              "id-" + j,
              "val_i",
              j % 5,
              "text",
              TestUtil.randomHtmlishString(random(), 100)));
    }
    solrClient.commit(COLLECTION);
  }

  @After
  public void teardown() {
    TestInjection.queryTimeout = null;
  }

  // TODO: add more tests and better assertions once SOLR-17151 / SOLR-17158 is done
  @Test
  public void testQueryLimits() throws Exception {
    SolrClient solrClient = cluster.getSolrClient();
    QueryResponse rsp = solrClient.query(COLLECTION, params("q", "*:*"));
    assertNull("should have full results", rsp.getHeader().get("partialResults"));

    String[] matchingExprTests =
        new String[] {
          "SearchHandler",
          "SearchHandler.handleRequestBody",
          "QueryComponent",
          "QueryComponent.process",
          "TimeLimitingBulkScorer.score",
          "FacetComponent.process:2"
        };
    for (String matchingExpr : matchingExprTests) {
      CallerSpecificQueryLimit limit = new CallerSpecificQueryLimit(Set.of(matchingExpr));
      TestInjection.queryTimeout = limit;
      rsp =
          solrClient.query(
              COLLECTION,
              params(
                  "q",
                  "id:*",
                  "sort",
                  "id asc",
                  "facet",
                  "true",
                  "facet.field",
                  "val_i",
                  "multiThreaded",
                  "false"));
      assertNotNull(
          "should have partial results for expr " + matchingExpr,
          rsp.getHeader().get("partialResults"));
      assertFalse("should have trippedBy info", limit.getCallerMatcher().getTrippedBy().isEmpty());
      assertTrue(
          "expected result to start with "
              + matchingExpr
              + " but was "
              + limit.getCallerMatcher().getTrippedBy(),
          limit.getCallerMatcher().getTrippedBy().iterator().next().startsWith(matchingExpr));
      Map<String, Integer> callCounts = limit.getCallerMatcher().getCallCounts();
      assertTrue("call count should be > 0", callCounts.get(matchingExpr) > 0);
    }
  }

  @Test
  public void testPivotFacetRespectsLimits() throws Exception {
    SolrClient solrClient = cluster.getSolrClient();
    CallerSpecificQueryLimit limit =
        new CallerSpecificQueryLimit(Set.of("PivotFacetProcessor.doPivots"));
    TestInjection.queryTimeout = limit;
    QueryResponse rsp =
        solrClient.query(
            COLLECTION,
            params(
                "q", "id:*", "facet", "true", "facet.pivot", "val_i,id", "multiThreaded", "false"));
    assertNotNull(
        "pivot faceting should stop with partial results: " + rsp.jsonStr(),
        rsp.getHeader().get("partialResults"));
    assertFalse("should have trippedBy info", limit.getCallerMatcher().getTrippedBy().isEmpty());
  }

  @Test
  public void testPivotFacetTruncatesWhenLimitsTrip() throws Exception {
    SolrClient solrClient = cluster.getSolrClient();
    // distrib=false keeps the request on one shard replica, so the pivot pass runs
    // locally and its result (or absence) is directly observable in the response
    ModifiableSolrParams pivotParams =
        params(
            "q", "id:*",
            "facet", "true",
            "facet.pivot", "val_i,id",
            "multiThreaded", "false",
            "distrib", "false");
    // control: with no limit installed, every value of val_i (5 values across the 100
    // indexed docs) appears in the top level pivot list
    QueryResponse rsp = solrClient.query(COLLECTION, pivotParams);
    assertNull("should have full results", rsp.getHeader().get("partialResults"));
    List<PivotField> controlPivots = rsp.getFacetPivot().get("val_i,id");
    assertNotNull("pivot results missing: " + rsp.jsonStr(), controlPivots);
    assertEquals("expected one top level pivot per val_i value", 5, controlPivots.size());

    CallerSpecificQueryLimit limit =
        new CallerSpecificQueryLimit(Set.of("PivotFacetProcessor.doPivots"));
    TestInjection.queryTimeout = limit;
    rsp = solrClient.query(COLLECTION, pivotParams);
    assertNotNull(
        "pivot faceting should stop with partial results: " + rsp.jsonStr(),
        rsp.getHeader().get("partialResults"));
    assertFalse("should have trippedBy info", limit.getCallerMatcher().getTrippedBy().isEmpty());
    // The header assertions above hold with or without the in-loop check in doPivots:
    // the matcher trips on any stack that has doPivots as an ancestor frame, which
    // includes the searches the pivot loop calls. The pivot data is what discriminates.
    // Without the check, the first subset search inside the loop trips the limit and
    // throws QueryLimitsExceededException out of doPivots, so the response is marked
    // partial by the handler but carries no pivot data at all. With the check, doPivots
    // stops at the top of the first iteration and returns the values built so far (none)
    // as a well formed partial pivot result, labeled with the limit detail.
    NamedList<List<PivotField>> facetPivot = rsp.getFacetPivot();
    List<PivotField> pivots = (facetPivot == null) ? null : facetPivot.get("val_i,id");
    assertNotNull(
        "expected a truncated pivot list in the partial response, but the response carried "
            + "no pivot data at all (the pivot pass died on the tripped limit): "
            + rsp.jsonStr(),
        pivots);
    assertEquals(
        "expected the pivot list to be truncated to no values once the limit trips, but got "
            + pivots.size()
            + " top level pivot values: "
            + rsp.jsonStr(),
        0,
        pivots.size());
    assertTrue(
        "expected the partial results detail to name the pivot limit check: " + rsp.jsonStr(),
        String.valueOf(rsp.getHeader().get("partialResultsDetails")).contains("Faceting pivots"));
  }

  @Test
  public void testAdjustShardRequestLimits() throws Exception {
    SolrClient solrClient = cluster.getSolrClient();
    String timeAllowed = "10000"; // ms - generous; must complete fully even on slow CI
    ModifiableSolrParams params =
        params(
            "q",
            "id:*",
            "cache",
            "false",
            "group",
            "true",
            "group.field",
            "val_i",
            "timeAllowed",
            timeAllowed,
            "sleep",
            "100");
    QueryResponse rsp = solrClient.query(COLLECTION, params);
    assertNull("should have full results: " + rsp.jsonStr(), rsp.getHeader().get("partialResults"));

    // reduce timeAllowed to force partial results
    params.set("timeAllowed", "100");
    // pretend this is a request with some time already used
    params.set(TimeAllowedLimit.USED_PARAM, "60");
    // set a high skew to trigger skipping shard requests
    params.set(TimeAllowedLimit.INFLIGHT_PARAM, "50");
    QueryResponse rsp1 = solrClient.query(COLLECTION, params);
    assertNotNull(
        "should have partial results: " + rsp1.jsonStr(), rsp1.getHeader().get("partialResults"));
    assertEquals(
        "partialResults should be true", "true", rsp1.getHeader().get("partialResults").toString());
    assertTrue(
        "partialResultsDetails should contain 'skipped':" + rsp1.jsonStr(),
        rsp1.getHeader().get("partialResultsDetails").toString().contains("skipped"));

    params.set(CommonParams.PARTIAL_RESULTS, false);
    QueryResponse rsp2 = solrClient.query(COLLECTION, params);
    assertNotNull(
        "should have partial results: " + rsp2.jsonStr(), rsp2.getHeader().get("partialResults"));
    assertEquals(
        "partialResults should be omitted: " + rsp2.jsonStr(),
        "omitted",
        rsp2.getHeader().get("partialResults").toString());
    assertTrue(
        "partialResultsDetails should contain 'skipped': " + rsp2.jsonStr(),
        rsp2.getHeader().get("partialResultsDetails").toString().contains("skipped"));
  }
}
