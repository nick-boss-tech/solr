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

package org.apache.solr.client.solrj.io.stream;

import java.io.IOException;
import java.util.List;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.ClusterStateProvider;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.LBSolrClient;
import org.apache.solr.client.solrj.io.SolrClientCache;
import org.apache.solr.client.solrj.io.Tuple;
import org.apache.solr.client.solrj.io.comp.StreamComparator;
import org.apache.solr.client.solrj.io.stream.expr.Explanation;
import org.apache.solr.client.solrj.io.stream.expr.StreamFactory;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.params.TermsParams;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

/**
 * Regression tests for SOLR-14231: with a single input node, {@link ScoreNodesStream} used to build
 * its /terms request with a null field and a null collection, because those values were only
 * assigned once a second tuple had been read. The /terms request must also be sent as POST so that
 * large term lists travel in the request body rather than the URL.
 */
public class ScoreNodesStreamTest extends SolrTestCase {

  @Test
  public void testSingleNodeTermsRequest() throws Exception {
    Tuple node = new Tuple();
    node.put("node", "product1");
    node.put("field", "product_s");
    node.put("collection", "collection1");
    node.put("termFreq", 3L);

    CapturingClient client = new CapturingClient(termsResponse());
    SolrClientCache cache =
        new SolrClientCache() {
          @Override
          public CloudSolrClient getCloudSolrClient(
              CloudSolrClient.CloudSolrClientConnection connection) {
            return client;
          }
        };

    ScoreNodesStream stream = new ScoreNodesStream(new ListTupleStream(List.of(node)), "termFreq");
    StreamContext context = new StreamContext();
    context.setSolrClientCache(cache);
    stream.setStreamContext(context);
    try {
      stream.open();
      Tuple scored = stream.read();
      assertEquals("product1", scored.getString("node"));
      assertEquals(4L, scored.getLong("docFreq").longValue());
      assertEquals(10L, scored.getLong("numDocs").longValue());
      assertNotNull(scored.getDouble("nodeScore"));
      assertTrue(stream.read().EOF);
    } finally {
      stream.close();
      client.close();
    }

    SolrRequest<?> request = client.capturedRequest;
    assertNotNull(request);
    assertEquals("collection1", client.capturedCollection);
    assertEquals("/terms", request.getPath());
    assertEquals(SolrRequest.METHOD.POST, request.getMethod());
    SolrParams params = request.getParams();
    assertEquals("product_s", params.get(TermsParams.TERMS_FIELD));
    assertEquals("product1", params.get(TermsParams.TERMS_LIST));
  }

  private static NamedList<Object> termsResponse() {
    NamedList<Object> response = new NamedList<>();
    NamedList<Number> indexStats = new NamedList<>();
    indexStats.add("numDocs", 10);
    response.add("indexstats", indexStats);
    NamedList<Number> fieldTerms = new NamedList<>();
    fieldTerms.add("product1", 4);
    NamedList<Object> terms = new NamedList<>();
    terms.add("product_s", fieldTerms);
    response.add("terms", terms);
    return response;
  }

  /** A {@link CloudSolrClient} that records the request instead of sending it. */
  private static class CapturingClient extends CloudSolrClient {
    private final NamedList<Object> cannedResponse;
    private SolrRequest<?> capturedRequest;
    private String capturedCollection;

    CapturingClient(NamedList<Object> cannedResponse) {
      super(true, true, false);
      this.cannedResponse = cannedResponse;
    }

    @Override
    public NamedList<Object> request(SolrRequest<?> request, String collection) {
      this.capturedRequest = request;
      this.capturedCollection = collection;
      return cannedResponse;
    }

    @Override
    public HttpSolrClient getHttpClient() {
      return null; // request() above never delegates to HTTP
    }

    @Override
    public ClusterStateProvider getClusterStateProvider() {
      return null; // request() above never consults cluster state
    }

    @Override
    protected LBSolrClient getLbClient() {
      return null; // request() above never load-balances
    }
  }

  /** A {@link TupleStream} that replays a fixed list of tuples. */
  private static class ListTupleStream extends TupleStream {
    private final List<Tuple> tuples;
    private int index;

    ListTupleStream(List<Tuple> tuples) {
      this.tuples = tuples;
    }

    @Override
    public void setStreamContext(StreamContext context) {}

    @Override
    public List<TupleStream> children() {
      return List.of();
    }

    @Override
    public void open() throws IOException {}

    @Override
    public void close() throws IOException {}

    @Override
    public Tuple read() throws IOException {
      if (index < tuples.size()) {
        return tuples.get(index++);
      }
      Tuple eof = new Tuple();
      eof.EOF = true;
      return eof;
    }

    @Override
    public StreamComparator getStreamSort() {
      return null;
    }

    @Override
    public Explanation toExplanation(StreamFactory factory) throws IOException {
      return null;
    }
  }
}
