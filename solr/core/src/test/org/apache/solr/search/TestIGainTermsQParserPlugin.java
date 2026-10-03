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

/** Tests the term scores and document frequencies reported by {@link IGainTermsQParserPlugin}. */
public class TestIGainTermsQParserPlugin extends SolrTestCaseJ4 {

  @ClassRule
  public static final SolrClientTestRule solrTestRule =
      new EmbeddedSolrServerTestRule() {
        @Override
        protected void before() throws Throwable {
          startSolr();
        }
      };

  private static final String FIELDS =
      "<fieldType name=\"igain_long\" class=\"solr.LongPointField\" docValues=\"true\"/>\n"
          + "<field name=\"feature_s\" type=\"string\" indexed=\"true\" stored=\"false\"/>\n"
          + "<field name=\"outcome_l\" type=\"igain_long\" indexed=\"false\" stored=\"false\""
          + " docValues=\"true\"/>\n";

  public void testTermsOutsideTheResultSetAreNotReported() throws Exception {
    Path configSet = LuceneTestCase.createTempDir();
    SolrTestCaseJ4.copyMinConf(configSet);
    Path schemaXml = configSet.resolve("conf/schema.xml");
    Files.writeString(
        schemaXml, Files.readString(schemaXml).replace("</schema>", FIELDS + "</schema>"));
    solrTestRule.newCollection().withConfigSet(configSet).create();
    SolrClient client = solrTestRule.getSolrClient();

    addDoc(client, "1", "a", 1);
    addDoc(client, "2", "b", 0);
    addDoc(client, "3", "a", 1);
    addDoc(client, "4", "b", 0);
    // the term "z" only occurs in documents that the query below does not match
    addDoc(client, "5", "z", 1);
    addDoc(client, "6", "z", 0);
    client.commit();

    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("q", "feature_s:(a OR b)");
    params.add("fq", "{!igain field=feature_s outcome=outcome_l positiveLabel=1 numTerms=5}");
    QueryResponse response = client.query(params);

    NamedList<?> featuredTerms = (NamedList<?>) response.getResponse().get("featuredTerms");
    NamedList<?> docFreq = (NamedList<?>) response.getResponse().get("docFreq");

    assertEquals(2, featuredTerms.size());
    for (int i = 0; i < featuredTerms.size(); i++) {
      double score = ((Number) featuredTerms.getVal(i)).doubleValue();
      assertFalse("score of " + featuredTerms.getName(i) + " is NaN", Double.isNaN(score));
    }

    assertEquals("docFreq must only list the featured terms", 2, docFreq.size());
    assertEquals(2, ((Number) docFreq.get("a")).intValue());
    assertEquals(2, ((Number) docFreq.get("b")).intValue());
    assertNull(docFreq.get("z"));
  }

  private static void addDoc(SolrClient client, String id, String feature, long outcome)
      throws Exception {
    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", id);
    doc.addField("feature_s", feature);
    doc.addField("outcome_l", outcome);
    client.add(doc);
  }
}
