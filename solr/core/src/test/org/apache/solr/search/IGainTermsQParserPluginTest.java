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

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.ScoreMode;
import org.apache.lucene.search.SimpleCollector;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.embedded.JettySolrRunner;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.util.RefCounted;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Regression test for SOLR-13838: a term that occurs in the analyzed field but in none of the
 * documents selected for the igain computation used to produce a NaN score in the featuredTerms
 * response, because its document frequency within the selected sets is zero and the information
 * gain calculation divided by it. Such terms must now be skipped entirely.
 */
public class IGainTermsQParserPluginTest extends SolrCloudTestCase {

  private static final String COLLECTION = "igain13838";

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1).addConfig("conf", configset("analytics-minimal")).configure();
    CollectionAdminRequest.createCollection(COLLECTION, "conf", 1, 1)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection(COLLECTION, 1, 1);
    List<SolrInputDocument> docs = new ArrayList<>();
    docs.add(doc("1", "apple banana", 1));
    docs.add(doc("2", "apple cherry", 0));
    // "zebra" occurs in the field dictionary, but this document is never selected below
    docs.add(doc("3", "zebra", 1));
    cluster.getSolrClient().add(COLLECTION, docs);
    cluster.getSolrClient().commit(COLLECTION);
  }

  @Test
  public void testTermAbsentFromSelectedDocsIsSkipped() throws Exception {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("field", "text");
    params.set("outcome", "outcome");
    params.set("positiveLabel", "1");
    params.set("numTerms", "10");
    QParserPlugin plugin = QParserPlugin.standardPlugins.get(IGainTermsQParserPlugin.NAME);
    QParser parser = plugin.createParser("", SolrParams.of(), params, null);
    AnalyticsQuery query = (AnalyticsQuery) parser.parse();

    SolrQueryResponse rsp = new SolrQueryResponse();
    ResponseBuilder rb = new ResponseBuilder(null, rsp, List.of());

    JettySolrRunner jetty = cluster.getJettySolrRunner(0);
    try (SolrCore core = jetty.getCoreContainer().getCore(coreName(jetty))) {
      RefCounted<SolrIndexSearcher> ref = core.getSearcher();
      try {
        SolrIndexSearcher searcher = ref.get();
        DelegatingCollector collector = query.getAnalyticsCollector(rb, searcher);
        // DelegatingCollector dereferences its (otherwise unused) delegate when a leaf reader
        // is set, so install a no-op one.
        collector.setDelegate(
            new SimpleCollector() {
              @Override
              public void collect(int doc) {}

              @Override
              public ScoreMode scoreMode() {
                return ScoreMode.COMPLETE_NO_SCORES;
              }
            });
        // Select only the documents containing "apple"; the "zebra" document is not selected
        for (LeafReaderContext ctx : searcher.getIndexReader().leaves()) {
          LeafCollector leafCollector = collector.getLeafCollector(ctx);
          PostingsEnum postings = ctx.reader().postings(new Term("text", "apple"));
          if (postings == null) {
            continue;
          }
          int docId;
          while ((docId = postings.nextDoc()) != PostingsEnum.NO_MORE_DOCS) {
            leafCollector.collect(docId);
          }
        }
        collector.complete();
      } finally {
        ref.decref();
      }
    }

    assertEquals(2, rsp.getValues().get("numDocs"));

    @SuppressWarnings("unchecked")
    NamedList<Double> featuredTerms = (NamedList<Double>) rsp.getValues().get("featuredTerms");
    assertNotNull(featuredTerms);
    assertNotNull(featuredTerms.get("apple"));
    assertNull("term outside the selected documents must be skipped", featuredTerms.get("zebra"));
    for (int i = 0; i < featuredTerms.size(); i++) {
      assertTrue(
          "score for '" + featuredTerms.getName(i) + "' must be finite",
          Double.isFinite(featuredTerms.getVal(i)));
    }

    @SuppressWarnings("unchecked")
    NamedList<Integer> docFreq = (NamedList<Integer>) rsp.getValues().get("docFreq");
    assertNotNull(docFreq);
    assertNull("skipped term must not appear in docFreq", docFreq.get("zebra"));
  }

  private static String coreName(JettySolrRunner jetty) {
    List<String> names = new ArrayList<>(jetty.getCoreContainer().getAllCoreNames());
    names.removeIf(name -> !name.startsWith(COLLECTION));
    assertEquals(1, names.size());
    return names.get(0);
  }

  private static SolrInputDocument doc(String id, String text, int outcome) {
    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", id);
    doc.addField("text", text);
    doc.addField("outcome", outcome);
    return doc;
  }
}
