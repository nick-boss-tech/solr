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

import java.util.List;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.index.IndexableField;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.SolrCore;
import org.apache.solr.util.EmbeddedSolrServerTestRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/** The docValues setting of the amount and currency code sub-fields is honored. */
public class CurrencyFieldTypeDocValuesTest extends SolrTestCase {

  @ClassRule
  public static final EmbeddedSolrServerTestRule solrTestRule = new EmbeddedSolrServerTestRule();

  private static final String AMOUNT = "money" + FieldType.POLY_FIELD_SEPARATOR + "_l_dv";
  private static final String CODE = "money" + FieldType.POLY_FIELD_SEPARATOR + "_s_dv";

  @BeforeClass
  public static void beforeClass() throws Exception {
    CurrencyFieldTypeTest.assumeCurrencySupport("USD", "EUR");
    SolrTestCaseJ4.newRandomConfig();
    solrTestRule.startSolr(SolrTestCaseJ4.TEST_HOME());
    solrTestRule
        .newCollection()
        .withConfigSet(SolrTestCaseJ4.TEST_COLL1_CONF())
        .withConfigFile("solrconfig-minimal.xml")
        .withSchemaFile("schema-currency-docvalues.xml")
        .create();
  }

  @Test
  public void testSubFieldsWriteDocValues() {
    SchemaField money;
    try (SolrCore core = solrTestRule.getCoreContainer().getCore("collection1")) {
      money = core.getLatestSchema().getField("money");
    }
    List<IndexableField> fields = money.createFields("1.50,EUR");

    assertTrue(
        "amount docValues missing: " + fields,
        fields.stream()
            .anyMatch(f -> f instanceof NumericDocValuesField && f.name().equals(AMOUNT)));
    assertTrue(
        "currency code docValues missing: " + fields,
        fields.stream().anyMatch(f -> f instanceof SortedDocValuesField && f.name().equals(CODE)));
  }

  @Test
  public void testSubFieldsCanBeSortedOnThroughDocValues() throws Exception {
    SolrClient client = solrTestRule.getSolrClient();
    client.add(doc("1", "2.00,USD"));
    client.add(doc("2", "1.00,USD"));
    client.add(doc("3", "3.00,EUR"));
    client.commit();

    assertEquals(List.of("2", "1", "3"), idsSortedBy(client, AMOUNT + " asc"));
    assertEquals(List.of("3", "2", "1"), idsSortedBy(client, CODE + " asc, " + AMOUNT + " asc"));
  }

  private static SolrInputDocument doc(String id, String money) {
    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", id);
    doc.addField("money", money);
    return doc;
  }

  private static List<String> idsSortedBy(SolrClient client, String sort) throws Exception {
    SolrQuery query = new SolrQuery("*:*");
    query.setFields("id");
    query.setParam("sort", sort);
    return client.query(query).getResults().stream().map(d -> (String) d.get("id")).toList();
  }
}
