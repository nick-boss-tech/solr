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
import java.util.Map;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.ScoreMode;
import org.apache.lucene.search.SimpleCollector;
import org.apache.lucene.util.Bits;
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
 * Regression test for SOLR-13903: a selected training document that contains none of the feature
 * terms used to have no feature vector and was silently dropped from the logistic regression
 * evaluation and weight updates, so the confusion matrix summed to less than the training set size.
 * Such documents must now be scored with an intercept-only vector so they count in the evaluation
 * and participate in training.
 */
public class TextLogisticRegressionQParserPluginTest extends SolrCloudTestCase {

  private static final String COLLECTION = "tlogit13903";

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1).addConfig("conf", configset("analytics-minimal")).configure();
    CollectionAdminRequest.createCollection(COLLECTION, "conf", 1, 1)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection(COLLECTION, 1, 1);
    List<SolrInputDocument> docs = new ArrayList<>();
    docs.add(doc("1", "apple banana", 1));
    docs.add(doc("2", "apple cherry", 0));
    // Selected for training below, but contains none of the feature terms ("apple")
    docs.add(doc("3", "zebra", 1));
    cluster.getSolrClient().add(COLLECTION, docs);
    cluster.getSolrClient().commit(COLLECTION);
  }

  @Test
  public void testTrainingDocWithoutFeatureTermsIsCounted() throws Exception {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("feature", "text");
    params.set("terms", "apple");
    params.set("idfs", "1.0");
    params.set("outcome", "outcome");
    params.set("positiveLabel", "1");
    params.set("threshold", "0.5");
    params.set("alpha", "0.01");
    QParserPlugin plugin =
        QParserPlugin.standardPlugins.get(TextLogisticRegressionQParserPlugin.NAME);
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
        // The training query selected all three documents
        for (LeafReaderContext ctx : searcher.getIndexReader().leaves()) {
          LeafCollector leafCollector = collector.getLeafCollector(ctx);
          LeafReader reader = ctx.reader();
          Bits liveDocs = reader.getLiveDocs();
          for (int docId = 0; docId < reader.maxDoc(); docId++) {
            if (liveDocs == null || liveDocs.get(docId)) {
              leafCollector.collect(docId);
            }
          }
        }
        collector.complete();
      } finally {
        ref.decref();
      }
    }

    @SuppressWarnings("unchecked")
    NamedList<Object> logit = (NamedList<Object>) rsp.getValues().get("logit");
    assertNotNull(logit);

    // With all weights initially 1.0: docs 1 and 2 score sigmoid(2), doc 3 scores sigmoid(1)
    // through the intercept alone; all three are predicted positive, so the confusion matrix
    // is TP=2, FP=1 and sums to the full training set size of 3. Before the fix doc 3 was
    // dropped and the matrix summed to 2.
    @SuppressWarnings("unchecked")
    Map<String, Long> evaluation = (Map<String, Long>) logit.get("evaluation");
    assertNotNull(evaluation);
    assertEquals(2L, evaluation.get("truePositive_i").longValue());
    assertEquals(1L, evaluation.get("falsePositive_i").longValue());
    assertEquals(0L, evaluation.get("trueNegative_i").longValue());
    assertEquals(0L, evaluation.get("falseNegative_i").longValue());
    long total =
        evaluation.get("truePositive_i")
            + evaluation.get("trueNegative_i")
            + evaluation.get("falsePositive_i")
            + evaluation.get("falseNegative_i");
    assertEquals("confusion matrix must cover the whole training set", 3L, total);

    // Total error against the initial weights also includes doc 3: |sigmoid(2)-1| +
    // |sigmoid(2)-0| + |sigmoid(1)-1| = 1.268941 (it was exactly 1.0 when doc 3 was dropped).
    assertEquals(1.268941, (Double) logit.get("error"), 0.0001);

    // Doc 3 has no term component, so its training update moves only the intercept weight:
    // the two weights, equal after docs 1 and 2, diverge to these exact values.
    @SuppressWarnings("unchecked")
    List<Double> weights = (List<Double>) logit.get("weights");
    assertNotNull(weights);
    assertEquals(2, weights.size());
    assertEquals(0.995086, weights.get(0), 0.0001);
    assertEquals(0.992382, weights.get(1), 0.0001);
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
