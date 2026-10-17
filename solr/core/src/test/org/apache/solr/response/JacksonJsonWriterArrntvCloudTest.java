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
package org.apache.solr.response;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrRequest.SolrRequestType;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.GenericSolrRequest;
import org.apache.solr.client.solrj.response.InputStreamResponseParser;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.Utils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Runs json.nl=arrntv through a SolrCloud collection with two shards. The coordinator writes the
 * final response, so facet and terms output must be valid JSON on the default writer (no wt) and on
 * an explicit wt=json. Each shard holds part of the data, so the counts and statistics below are
 * the merged totals.
 */
public class JacksonJsonWriterArrntvCloudTest extends SolrCloudTestCase {

  private static final String COLLECTION = "arrntvCloud";

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2).addConfig("conf", configset("cloud-minimal")).configure();
    CollectionAdminRequest.createCollection(COLLECTION, "conf", 2, 1)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection(COLLECTION, 2, 2);
    for (int i = 0; i < 4; i++) {
      cluster.getSolrClient().add(COLLECTION, new SolrInputDocument("id", "a" + i, "cat_s", "a"));
    }
    for (int i = 0; i < 2; i++) {
      cluster.getSolrClient().add(COLLECTION, new SolrInputDocument("id", "b" + i, "cat_s", "b"));
    }
    cluster.getSolrClient().commit(COLLECTION);
  }

  private static String get(String path, String... params) throws Exception {
    ModifiableSolrParams p = new ModifiableSolrParams();
    for (int i = 0; i < params.length; i += 2) {
      p.set(params[i], params[i + 1]);
    }
    GenericSolrRequest req = new GenericSolrRequest(METHOD.GET, path, SolrRequestType.QUERY, p);
    req.setRequiresCollection(true);
    req.setResponseParser(new InputStreamResponseParser("json"));
    NamedList<Object> res = cluster.getSolrClient().request(req, COLLECTION);
    return InputStreamResponseParser.consumeResponseToString(res);
  }

  private static void assertFacetCounts(String body) {
    assertNotNull(Utils.fromJSONString(body));
    assertTrue(body, body.contains("{\"name\":\"a\",\"type\":\"int\",\"value\":4}"));
    assertTrue(body, body.contains("{\"name\":\"b\",\"type\":\"int\",\"value\":2}"));
  }

  private static void assertTermStats(String body) {
    assertNotNull(Utils.fromJSONString(body));
    assertTrue(
        body, body.contains("{\"name\":\"a\",\"type\":\"lst\",\"value\":{\"df\":4,\"ttf\":4}}"));
    assertTrue(
        body, body.contains("{\"name\":\"b\",\"type\":\"lst\",\"value\":{\"df\":2,\"ttf\":2}}"));
  }

  @Test
  public void testFacetsWithoutWtAcrossShards() throws Exception {
    assertFacetCounts(
        get(
            "/select",
            "q",
            "*:*",
            "rows",
            "0",
            "facet",
            "true",
            "facet.field",
            "cat_s",
            "json.nl",
            "arrntv"));
  }

  @Test
  public void testFacetsWithWtJsonAcrossShards() throws Exception {
    assertFacetCounts(
        get(
            "/select",
            "q",
            "*:*",
            "rows",
            "0",
            "facet",
            "true",
            "facet.field",
            "cat_s",
            "wt",
            "json",
            "json.nl",
            "arrntv"));
  }

  @Test
  public void testTermsTtfWithoutWtAcrossShards() throws Exception {
    assertTermStats(get("/terms", "terms.fl", "cat_s", "terms.ttf", "true", "json.nl", "arrntv"));
  }

  @Test
  public void testTermsTtfWithWtJsonAcrossShards() throws Exception {
    assertTermStats(
        get("/terms", "terms.fl", "cat_s", "terms.ttf", "true", "wt", "json", "json.nl", "arrntv"));
  }
}
