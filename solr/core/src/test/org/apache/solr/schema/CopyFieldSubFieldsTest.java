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
import org.apache.solr.SolrTestCaseJ4;
import org.junit.BeforeClass;
import org.junit.Test;

/** Sub-fields of copyField destinations are copy targets as well. */
public class CopyFieldSubFieldsTest extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeClass() throws Exception {
    Path solrHome = createTempDir();
    Path confDir = FilterPath.unwrap(solrHome.resolve("collection1/conf"));
    Path testConfDir = TEST_HOME().resolve("collection1/conf");
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
    initCore("solrconfig-managed-schema.xml", "schema-copyfield-subfields.xml", solrHome);
  }

  private static List<SchemaField> subFields(IndexSchema schema, String fieldName) {
    SchemaField field = schema.getField(fieldName);
    List<SchemaField> subFields = field.getType().getSubFields(field, schema);
    assertFalse(fieldName + " has no sub-fields", subFields.isEmpty());
    return subFields;
  }

  @Test
  public void testSubFieldsOfTargetsAreTargets() {
    IndexSchema schema = h.getCore().getLatestSchema();
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
    ManagedIndexSchema schema = (ManagedIndexSchema) h.getCore().getLatestSchema();
    SchemaField box = schema.getField("box");

    schema = schema.deleteCopyFields(Map.of("src1", List.of("box")));
    assertSubFieldTargets(schema, box, true);
    schema = schema.deleteCopyFields(Map.of("src2", List.of("box")));
    assertSubFieldTargets(schema, box, true);
    schema = schema.deleteCopyFields(Map.of("src3", List.of("box")));
    assertSubFieldTargets(schema, box, false);
    assertFalse(schema.isCopyFieldTarget(box));
  }

  private static void assertSubFieldTargets(IndexSchema schema, SchemaField box, boolean expected) {
    List<SchemaField> subFields = box.getType().getSubFields(box, schema);
    assertFalse(subFields.isEmpty());
    for (SchemaField subField : subFields) {
      assertEquals(subField.getName(), expected, schema.isCopyFieldTarget(subField));
    }
  }
}
