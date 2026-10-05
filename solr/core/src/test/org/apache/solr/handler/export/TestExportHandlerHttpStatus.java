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
package org.apache.solr.handler.export;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.RemoteSolrException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.response.InputStreamResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.json.CanonicalJsonResponseParser;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.util.ExternalPaths;
import org.apache.solr.util.SolrJettyTestRule;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * SOLR-12543: over real HTTP, an /export request without a sort or without fl comes back with an
 * HTTP 400 status, not an HTTP 200 whose body carries an EXCEPTION document. A sort supplied as a
 * local param on q is a valid sort and must still export.
 */
public class TestExportHandlerHttpStatus extends SolrTestCase {

  @ClassRule public static final SolrJettyTestRule solrTestRule = new SolrJettyTestRule();

  @BeforeClass
  public static void beforeClass() throws Exception {
    // The sortingresponse solrconfig pulls in the randomized indexConfig snippet, whose
    // placeholders come from the system properties this sets.
    SolrTestCaseJ4.newRandomConfig();
    System.setProperty(
        CoreContainer.ALLOW_PATHS_SYSPROP,
        ExternalPaths.SOURCE_HOME
            .resolve("core/src/test-files/solr/collection1")
            .toAbsolutePath()
            .toString());
    solrTestRule.startSolr();
    solrTestRule
        .newCollection()
        .withConfigSet(
            ExternalPaths.SOURCE_HOME.resolve("core/src/test-files/solr/collection1/conf"))
        .withConfigFile("solrconfig-sortingresponse.xml")
        .withSchemaFile("schema-sortingresponse.xml")
        .create();
  }

  @Before
  public void clearIndex() throws Exception {
    SolrClient client = solrTestRule.getSolrClient();
    client.deleteByQuery("*:*");
    client.commit();
  }

  @Test
  public void testMissingSortReturns400() {
    SolrQuery q = exportQuery("*:*");
    q.setFields("id");
    RemoteSolrException e = expectThrows(RemoteSolrException.class, () -> query(q));
    assertEquals(400, e.code());
    assertTrue(e.getMessage().contains("No sort criteria was provided."));
  }

  @Test
  public void testMissingFlReturns400() {
    SolrQuery q = exportQuery("*:*");
    q.setSort("id", SolrQuery.ORDER.asc);
    RemoteSolrException e = expectThrows(RemoteSolrException.class, () -> query(q));
    assertEquals(400, e.code());
    assertTrue(e.getMessage().contains("export field list (fl) must be specified."));
  }

  @Test
  public void testLocalParamSortExports() throws Exception {
    SolrClient client = solrTestRule.getSolrClient();
    client.add(doc("1", 2));
    client.add(doc("2", 1));
    client.commit();

    SolrQuery q = exportQuery("{!lucene sort='intdv asc'}*:*");
    q.setFields("id", "intdv");
    // Read the raw body: a successful /export response does not carry an application/json
    // content type, which the JSON response parsers reject on the client side.
    QueryRequest request = new QueryRequest(q);
    request.setResponseParser(new InputStreamResponseParser("json"));
    NamedList<Object> rsp = client.request(request);
    String body;
    try (InputStream in = (InputStream) rsp.get("stream")) {
      body = new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
    assertTrue(body.contains("\"numFound\":2"));
    int first = body.indexOf("\"id\":\"2\"");
    int second = body.indexOf("\"id\":\"1\"");
    assertTrue("docs should be sorted by intdv asc", first >= 0 && second > first);
  }

  private static SolrQuery exportQuery(String queryString) {
    SolrQuery query = new SolrQuery(queryString);
    query.setRequestHandler("/export");
    return query;
  }

  private static QueryResponse query(SolrQuery q) throws Exception {
    QueryRequest request = new QueryRequest(q);
    request.setResponseParser(new CanonicalJsonResponseParser());
    return request.process(solrTestRule.getSolrClient());
  }

  private static SolrInputDocument doc(String id, int intdv) {
    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", id);
    doc.addField("intdv", intdv);
    return doc;
  }
}
