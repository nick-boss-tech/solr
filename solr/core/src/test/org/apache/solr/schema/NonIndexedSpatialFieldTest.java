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
package org.apache.solr.schema;

import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.util.EmbeddedSolrServerTestRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/** Spatial prefix-tree fields that are stored but not indexed must load and round-trip values. */
public class NonIndexedSpatialFieldTest extends SolrTestCase {

  @ClassRule
  public static final EmbeddedSolrServerTestRule solrTestRule = new EmbeddedSolrServerTestRule();

  @BeforeClass
  public static void beforeClass() throws Exception {
    SolrTestCaseJ4.newRandomConfig();
    solrTestRule.startSolr(SolrTestCaseJ4.TEST_HOME());
    solrTestRule
        .newCollection()
        .withConfigSet(SolrTestCaseJ4.TEST_COLL1_CONF())
        .withConfigFile("solrconfig-minimal.xml")
        .withSchemaFile("schema-nonindexed-spatial.xml")
        .create();
  }

  @Test
  public void testStoredOnlyFieldsRoundTrip() throws Exception {
    SolrClient client = solrTestRule.getSolrClient();
    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", "1");
    doc.addField("daterange_stored", "[2000 TO 2014-05-21]");
    doc.addField("srpt_stored", "25,82");
    client.add(doc);
    client.commit();

    SolrQuery query = new SolrQuery("id:1");
    query.setFields("daterange_stored", "srpt_stored");
    SolrDocumentList results = client.query(query).getResults();
    assertEquals(1, results.getNumFound());
    assertNotNull(results.get(0).getFieldValue("daterange_stored"));
    assertNotNull(results.get(0).getFieldValue("srpt_stored"));
  }
}
