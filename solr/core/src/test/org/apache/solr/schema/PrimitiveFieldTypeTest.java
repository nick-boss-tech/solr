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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.core.SolrConfig;
import org.junit.Test;

/** Tests that defaults are set for Primitive (non-analyzed) fields */
public class PrimitiveFieldTypeTest extends SolrTestCaseJ4 {
  private final Path testConfHome = TEST_HOME().resolve("collection1").resolve("conf");
  protected SolrConfig config;
  protected IndexSchema schema;
  protected HashMap<String, String> initMap;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    // set some system properties for use by tests
    System.setProperty(
        "solr.index.updatelog.enabled", "false"); // schema12 doesn't support _version_

    initMap = new HashMap<>();
    config =
        new SolrConfig(
            TEST_PATH().resolve("collection1"), testConfHome.resolve("solrconfig.xml").toString());
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

  public void testIgnoredStrField() {
    assertIgnoredField(new StrField(), Map.of());
  }

  public void testIgnoredBoolField() {
    assertIgnoredField(new BoolField(), Map.of());
  }

  public void testIgnoredBinaryField() {
    assertIgnoredField(new BinaryField(), Map.of());
  }

  public void testIgnoredSortableBinaryField() {
    assertIgnoredField(new SortableBinaryField(), Map.of());
  }

  public void testIgnoredSortableTextField() {
    assertIgnoredField(new SortableTextField(), Map.of());
  }

  public void testIgnoredCollationField() {
    assertIgnoredField(new CollationField(), Map.of("language", "en"));
  }

  @SuppressWarnings("deprecation")
  public void testIgnoredTrieFields() {
    for (FieldType fieldType :
        List.of(
            new TrieIntField(),
            new TrieLongField(),
            new TrieFloatField(),
            new TrieDoubleField(),
            new TrieDateField())) {
      assertIgnoredField(fieldType, Map.of());
    }
  }

  private void assertIgnoredField(FieldType fieldType, Map<String, String> args) {
    schema =
        IndexSchemaFactory.buildIndexSchema(
            testConfHome.resolve("schema15.xml").toString(), config);
    fieldType.setArgs(schema, new HashMap<>(args));
    for (boolean multiValued : new boolean[] {false, true}) {
      SchemaField field =
          SchemaField.create(
              "ignored",
              fieldType,
              Map.of(
                  "indexed", false,
                  "stored", false,
                  "docValues", false,
                  "multiValued", multiValued));
      assertFalse(field.indexed());
      assertFalse(field.stored());
      assertFalse(field.hasDocValues());
      assertTrue(fieldType.getClass().getName(), field.createFields("ignored").isEmpty());
    }
  }

  public void testDocValuesOnlyFields() {
    schema =
        IndexSchemaFactory.buildIndexSchema(
            testConfHome.resolve("schema15.xml").toString(), config);
    for (FieldType fieldType :
        List.of(
            new StrField(),
            new BoolField(),
            new BinaryField(),
            new SortableTextField(),
            new CollationField())) {
      fieldType.setArgs(
          schema,
          new HashMap<>(fieldType instanceof CollationField ? Map.of("language", "en") : Map.of()));
      for (boolean multiValued : new boolean[] {false, true}) {
        if (fieldType instanceof BinaryField && multiValued) {
          continue;
        }
        SchemaField field =
            SchemaField.create(
                "dv_only",
                fieldType,
                Map.of(
                    "indexed", false,
                    "stored", false,
                    "docValues", true,
                    "multiValued", multiValued));
        Object value =
            fieldType instanceof BinaryField
                ? new byte[] {1, 2, 3}
                : fieldType instanceof BoolField ? "true" : "value";
        List<IndexableField> docValues =
            field.createFields(value).stream()
                .filter(f -> f != null && f.fieldType().docValuesType() != DocValuesType.NONE)
                .toList();
        String message = fieldType.getClass().getName();
        assertEquals(message, 1, docValues.size());
        IndexableField docValue = docValues.get(0);
        DocValuesType expectedType =
            fieldType instanceof BinaryField
                ? DocValuesType.BINARY
                : multiValued ? DocValuesType.SORTED_SET : DocValuesType.SORTED;
        assertEquals(message, expectedType, docValue.fieldType().docValuesType());
        assertEquals(message, field.getName(), docValue.name());
        assertTrue(message, docValue.binaryValue().length > 0);
        if (fieldType instanceof BinaryField) {
          assertEquals(message, new BytesRef(new byte[] {1, 2, 3}), docValue.binaryValue());
        } else if (fieldType instanceof BoolField) {
          assertEquals(message, new BytesRef("T"), docValue.binaryValue());
        } else if (!(fieldType instanceof CollationField)) {
          assertEquals(message, new BytesRef("value"), docValue.binaryValue());
        }
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testDefaultOmitNorms() throws Exception {

    final List<Class<? extends FieldType>> types =
        Arrays.asList(
            TrieDateField.class, DatePointField.class,
            TrieIntField.class, IntPointField.class,
            TrieLongField.class, IntPointField.class,
            TrieFloatField.class, FloatPointField.class,
            TrieDoubleField.class, DoublePointField.class,
            StrField.class, BoolField.class,
            // Non-prims, omitNorms always defaults to false regardless of schema version...
            TextField.class, BinaryField.class);

    // ***********************
    // With schema version 1.4:
    // ***********************
    schema =
        IndexSchemaFactory.buildIndexSchema(
            testConfHome.resolve("schema12.xml").toString(), config);

    for (Class<? extends FieldType> clazz : types) {
      FieldType ft = clazz.getConstructor().newInstance();
      ft.init(schema, initMap);
      assertFalse(ft.getClass().getName(), ft.hasProperty(FieldType.OMIT_NORMS));
    }

    // ***********************
    // With schema version 1.5
    // ***********************
    schema =
        IndexSchemaFactory.buildIndexSchema(
            testConfHome.resolve("schema15.xml").toString(), config);

    for (Class<? extends FieldType> clazz : types) {
      FieldType ft = clazz.getConstructor().newInstance();
      ft.init(schema, initMap);
      assertEquals(
          ft.getClass().getName(),
          ft instanceof PrimitiveFieldType,
          ft.hasProperty(FieldType.OMIT_NORMS));
    }
  }

  public void testDateField() {
    schema =
        IndexSchemaFactory.buildIndexSchema(
            testConfHome.resolve("schema15.xml").toString(), config);

    final TrieDateField tdt = new TrieDateField();
    {
      final Map<String, String> args = new HashMap<>();
      args.put("sortMissingLast", "true");
      args.put("indexed", "true");
      args.put("stored", "false");
      args.put("docValues", "true");
      args.put("precisionStep", "16");
      tdt.setArgs(schema, args);
      assertEquals(16, tdt.getPrecisionStep());
    }
    final DatePointField pdt = new DatePointField();
    {
      final Map<String, String> args = new HashMap<>();
      args.put("sortMissingLast", "true");
      args.put("indexed", "true");
      args.put("stored", "false");
      args.put("docValues", "true");
      pdt.setArgs(schema, args);
    }

    for (FieldType ft : Arrays.asList(tdt, pdt)) {
      assertTrue(ft.getClass().getName(), ft.hasProperty(FieldType.OMIT_NORMS));
      assertTrue(ft.getClass().getName(), ft.hasProperty(FieldType.SORT_MISSING_LAST));
      assertTrue(ft.getClass().getName(), ft.hasProperty(FieldType.INDEXED));
      assertFalse(ft.getClass().getName(), ft.hasProperty(FieldType.STORED));
      assertTrue(ft.getClass().getName(), ft.hasProperty(FieldType.DOC_VALUES));
    }
  }
}
