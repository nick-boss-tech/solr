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
import org.apache.solr.SolrTestCaseJ4;
import org.junit.BeforeClass;
import org.junit.Test;

/** The docValues setting of the amount and currency code sub-fields is honored. */
public class CurrencyFieldTypeDocValuesTest extends SolrTestCaseJ4 {

  private static final String AMOUNT = "money" + FieldType.POLY_FIELD_SEPARATOR + "_l_dv";
  private static final String CODE = "money" + FieldType.POLY_FIELD_SEPARATOR + "_s_dv";

  @BeforeClass
  public static void beforeClass() throws Exception {
    CurrencyFieldTypeTest.assumeCurrencySupport("USD", "EUR");
    initCore("solrconfig-minimal.xml", "schema-currency-docvalues.xml");
  }

  @Test
  public void testSubFieldsWriteDocValues() {
    SchemaField money = h.getCore().getLatestSchema().getField("money");
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
  public void testSubFieldsCanBeSortedOnThroughDocValues() {
    assertU(adoc("id", "1", "money", "2.00,USD"));
    assertU(adoc("id", "2", "money", "1.00,USD"));
    assertU(adoc("id", "3", "money", "3.00,EUR"));
    assertU(commit());

    assertQ(
        req("q", "*:*", "fl", "id", "sort", AMOUNT + " asc"),
        "//result/doc[1]/str[@name='id'][.='2']",
        "//result/doc[2]/str[@name='id'][.='1']",
        "//result/doc[3]/str[@name='id'][.='3']");
    assertQ(
        req("q", "*:*", "fl", "id", "sort", CODE + " asc, " + AMOUNT + " asc"),
        "//result/doc[1]/str[@name='id'][.='3']",
        "//result/doc[2]/str[@name='id'][.='2']",
        "//result/doc[3]/str[@name='id'][.='1']");
  }
}
