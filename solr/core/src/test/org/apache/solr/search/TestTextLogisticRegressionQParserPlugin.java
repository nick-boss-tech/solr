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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.util.EmbeddedSolrServerTestRule;
import org.apache.solr.util.SolrClientTestRule;
import org.junit.ClassRule;

/** Tests the training evaluation of {@link TextLogisticRegressionQParserPlugin}. */
public class TestTextLogisticRegressionQParserPlugin extends SolrTestCaseJ4 {

  @ClassRule
  public static final SolrClientTestRule solrTestRule =
      new EmbeddedSolrServerTestRule() {
        @Override
        protected void before() throws Throwable {
          startSolr();
        }
      };

  private static final String FIELDS =
      "<fieldType name=\"logit_long\" class=\"solr.LongPointField\" docValues=\"true\"/>\n"
          + "<field name=\"feature_s\" type=\"string\" indexed=\"true\" stored=\"false\"/>\n"
          + "<field name=\"outcome_l\" type=\"logit_long\" indexed=\"false\" stored=\"false\""
          + " docValues=\"true\"/>\n";

  public void testEvaluationCoversDocsWithoutSelectedTerms() throws Exception {
    Path configSet = LuceneTestCase.createTempDir();
    SolrTestCaseJ4.copyMinConf(configSet);
    Path schemaXml = configSet.resolve("conf/schema.xml");
    Files.writeString(
        schemaXml, Files.readString(schemaXml).replace("</schema>", FIELDS + "</schema>"));
    solrTestRule.newCollection().withConfigSet(configSet).create();
    SolrClient client = solrTestRule.getSolrClient();

    // docs 5 and 6 contain none of the selected terms "a" and "b"
    addDoc(client, "1", "a", 1);
    addDoc(client, "2", "b", 0);
    addDoc(client, "3", "a", 1);
    addDoc(client, "4", "b", 0);
    addDoc(client, "5", "z", 1);
    addDoc(client, "6", "z", 0);
    client.commit();

    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("q", "*:*");
    params.add("fq", "{!tlogit}");
    params.set("feature", "feature_s");
    params.set("terms", "a,b");
    params.set("idfs", "1,1");
    params.set("outcome", "outcome_l");
    params.set("iteration", "0");
    QueryResponse response = client.query(params);

    NamedList<?> logit = (NamedList<?>) response.getResponse().get("logit");
    Object evaluation = logit.get("evaluation");
    long total =
        count(evaluation, "truePositive_i")
            + count(evaluation, "trueNegative_i")
            + count(evaluation, "falsePositive_i")
            + count(evaluation, "falseNegative_i");
    assertEquals("every training doc must be counted in the evaluation", 6, total);
  }

  private static void addDoc(SolrClient client, String id, String feature, long outcome)
      throws Exception {
    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", id);
    doc.addField("feature_s", feature);
    doc.addField("outcome_l", outcome);
    client.add(doc);
  }

  private static long count(Object evaluation, String key) {
    Object value =
        evaluation instanceof Map<?, ?> map ? map.get(key) : ((NamedList<?>) evaluation).get(key);
    return ((Number) value).longValue();
  }
}
