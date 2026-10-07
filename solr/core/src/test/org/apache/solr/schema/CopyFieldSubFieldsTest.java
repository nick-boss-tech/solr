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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.file.PathUtils;
import org.apache.lucene.tests.mockfile.FilterPath;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.SolrCore;
import org.apache.solr.util.EmbeddedSolrServerTestRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/** Sub-fields of copyField destinations are copy targets as well. */
public class CopyFieldSubFieldsTest extends SolrTestCase {

  @ClassRule
  public static final EmbeddedSolrServerTestRule solrTestRule = new EmbeddedSolrServerTestRule();

  @BeforeClass
  public static void beforeClass() throws Exception {
    Path configHome = createTempDir();
    Path confDir = FilterPath.unwrap(configHome.resolve("collection1/conf"));
    Path testConfDir = SolrTestCaseJ4.TEST_HOME().resolve("collection1/conf");
    Files.createDirectories(confDir);
    for (String file :
        List.of(
            "solrconfig-managed-schema.xml",
            "solrconfig-basic.xml",
            "solrconfig.snippet.randomindexconfig.xml",
            "schema-copyfield-subfields.xml",
            "currency.xml")) {
      PathUtils.copyFileToDirectory(testConfDir.resolve(file), confDir);
    }
    System.setProperty("managed.schema.mutable", "true");
    System.setProperty("solr.index.updatelog.enabled", "false");
    SolrTestCaseJ4.newRandomConfig();
    solrTestRule.startSolr(createTempDir());
    solrTestRule
        .newCollection("collection1")
        .withConfigSet(confDir)
        .withConfigFile("solrconfig-managed-schema.xml")
        .withSchemaFile("schema-copyfield-subfields.xml")
        .create();
  }

  private static IndexSchema latestSchema() {
    try (SolrCore core = solrTestRule.getCoreContainer().getCore("collection1")) {
      return core.getLatestSchema();
    }
  }

  private static List<SchemaField> subFields(IndexSchema schema, String fieldName) {
    SchemaField field = schema.getField(fieldName);
    List<SchemaField> subFields = field.getType().getSubFields(field, schema);
    assertFalse(fieldName + " has no sub-fields", subFields.isEmpty());
    return subFields;
  }

  @Test
  public void testSubFieldsOfTargetsAreTargets() {
    IndexSchema schema = latestSchema();
    for (String name : List.of("loc", "amount", "box", "price_c")) {
      assertTrue(name, schema.isCopyFieldTarget(schema.getField(name)));
      for (SchemaField subField : subFields(schema, name)) {
        assertTrue(subField.getName(), schema.isCopyFieldTarget(subField));
      }
    }
    for (SchemaField subField : subFields(schema, "unused_amount")) {
      assertFalse(subField.getName(), schema.isCopyFieldTarget(subField));
    }
  }

  @Test
  public void testSubFieldsStayTargetsUntilTheLastCopyFieldIsDeleted() {
    ManagedIndexSchema schema = (ManagedIndexSchema) latestSchema();
    SchemaField box = schema.getField("box");

    schema = schema.deleteCopyFields(Map.of("src1", List.of("box")));
    assertSubFieldTargets(schema, box, true);
    schema = schema.deleteCopyFields(Map.of("src2", List.of("box")));
    assertSubFieldTargets(schema, box, true);
    schema = schema.deleteCopyFields(Map.of("src3", List.of("box")));
    assertSubFieldTargets(schema, box, false);
    assertFalse(schema.isCopyFieldTarget(box));
  }

  @Test
  public void testRealTimeGetOmitsDerivedCurrencySubFields() throws Exception {
    EmbeddedSolrServer client = solrTestRule.getSolrClient("collection1");
    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", "rtg-currency-doc");
    doc.addField("price", "10.50,USD");
    client.add(doc);
    client.commit();

    SolrDocument fetched =
        realTimeGet(client, "rtg-currency-doc", "id,price,price_c,price_c_l_pl,price_c_s_c");
    assertEquals("10.50,USD", fetched.getFieldValue("price"));
    // price_c is a copyField target, so it is filtered from the materialized document; the
    // currency sub-fields derived from it are targets as well and must not leak either.
    assertNull(fetched.getFieldValue("price_c"));
    assertNull("derived amount sub-field leaked", fetched.getFieldValue("price_c_l_pl"));
    assertNull("derived currency code sub-field leaked", fetched.getFieldValue("price_c_s_c"));
  }

  @Test
  public void testAtomicUpdateMaterializationOmitsDerivedCurrencySubFields() throws Exception {
    EmbeddedSolrServer client = solrTestRule.getSolrClient("collection1");
    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", "atomic-currency-doc");
    doc.addField("price", "10.50,USD");
    client.add(doc);
    client.commit();

    // A partial (atomic) update materializes the stored document through
    // RealTimeGetComponent.getInputDocument before applying the modifier and
    // re-indexing it. This collection has no update log, so a client-side atomic
    // update is rejected by design; the materialization itself is exercised
    // through the /get entry point the update path uses. The materialized
    // document must not carry the derived currency sub-fields as if they were
    // user-supplied fields.
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/get");
    query.set("getInputDocument", "atomic-currency-doc");
    QueryResponse rsp = client.query(query);
    Object materialized = rsp.getResponse().get("inputDocument");
    assertNotNull("no inputDocument returned by /get", materialized);
    SolrInputDocument inputDoc = (SolrInputDocument) materialized;
    assertEquals("10.50,USD", inputDoc.getFieldValue("price"));
    assertNull(inputDoc.getFieldValue("price_c"));
    assertNull("derived amount sub-field leaked", inputDoc.getFieldValue("price_c_l_pl"));
    assertNull("derived currency code sub-field leaked", inputDoc.getFieldValue("price_c_s_c"));
  }

  private static SolrDocument realTimeGet(EmbeddedSolrServer client, String id, String fl)
      throws Exception {
    SolrQuery query = new SolrQuery();
    query.setRequestHandler("/get");
    query.set("id", id);
    query.set("fl", fl);
    QueryResponse rsp = client.query(query);
    Object doc = rsp.getResponse().get("doc");
    assertNotNull("no doc returned by /get for id " + id, doc);
    return (SolrDocument) doc;
  }

  private static void assertSubFieldTargets(IndexSchema schema, SchemaField box, boolean expected) {
    List<SchemaField> subFields = box.getType().getSubFields(box, schema);
    assertFalse(subFields.isEmpty());
    for (SchemaField subField : subFields) {
      assertEquals(subField.getName(), expected, schema.isCopyFieldTarget(subField));
    }
  }
}
