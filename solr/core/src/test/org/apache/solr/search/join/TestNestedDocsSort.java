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
package org.apache.solr.search.join;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.SolrCache;
import org.apache.solr.search.SortSpec;
import org.apache.solr.search.SortSpecParsing;
import org.apache.solr.util.RandomNoReverseMergePolicyFactory;
import org.apache.solr.util.SolrMetricTestUtils;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class TestNestedDocsSort extends SolrTestCaseJ4 {

  @ClassRule
  public static final TestRule noReverseMerge = RandomNoReverseMergePolicyFactory.createRule();

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml", "schema.xml");
  }

  public void testNumericChildMissingValues() {
    for (String type : new String[] {"i", "l", "f", "d"}) {
      String field = "child_sort_" + type + "_p";
      indexMissingSortDocuments(field);
      assertChildOrder(field, "asc", "3", "4", "5", "1", "2");
      assertChildOrder(field, "desc", "2", "1", "3", "4", "5");

      field = "child_sort_first_" + type;
      indexMissingSortDocuments(field);
      assertChildOrder(field, "asc", "2", "3", "4", "5", "1");
      assertChildOrder(field, "desc", "2", "1", "3", "4", "5");

      field = "child_sort_last_" + type;
      indexMissingSortDocuments(field);
      assertChildOrder(field, "asc", "3", "4", "5", "1", "2");
      assertChildOrder(field, "desc", "2", "1", "3", "4", "5");
    }
  }

  public void testStringChildMissingFirst() {
    indexMissingSortDocuments("nullfirst");
    assertChildOrder("nullfirst", "asc", "2", "3", "4", "5", "1");
    assertChildOrder("nullfirst", "desc", "1", "2", "3", "4", "5");
  }

  public void testStringChildMissingLast() {
    indexMissingSortDocuments("child_sort_s1");
    assertChildOrder("child_sort_s1", "asc", "3", "4", "5", "1", "2");
    assertChildOrder("child_sort_s1", "desc", "2", "1", "3", "4", "5");
  }

  public void testFilteredChildrenDoNotSupplyMissingValues() {
    indexMissingSortDocuments("child_sort_first_i", true);
    assertChildOrder("child_sort_first_i", "asc", "3", "4", "5", "1", "2");
    assertChildOrder("child_sort_first_i", "desc", "2", "1", "3", "4", "5");
  }

  private void indexMissingSortDocuments(String field) {
    indexMissingSortDocuments(field, false);
  }

  private void indexMissingSortDocuments(String field, boolean excludeMissingChild) {
    assertU(delQ("*:*"));
    SolrInputDocument valued = sdoc("id", "1", "type_s1", "parent");
    valued.addChildDocument(sdoc("id", "11", "type_s1", "child", field, "1"));
    SolrInputDocument mixed = sdoc("id", "2", "type_s1", "parent");
    mixed.addChildDocument(sdoc("id", "21", "type_s1", "child", field, "2"));
    mixed.addChildDocument(
        sdoc("id", "22", "type_s1", excludeMissingChild ? "excluded" : "child"));
    SolrInputDocument missing = sdoc("id", "3", "type_s1", "parent");
    missing.addChildDocument(sdoc("id", "31", "type_s1", "child"));
    SolrInputDocument empty = sdoc("id", "4", "type_s1", "parent");
    SolrInputDocument excluded = sdoc("id", "5", "type_s1", "parent");
    excluded.addChildDocument(sdoc("id", "51", "type_s1", "excluded", field, "9"));
    for (SolrInputDocument parent :
        new SolrInputDocument[] {valued, mixed, missing, empty, excluded}) {
      assertU(adoc(parent));
    }
    assertU(commit());
  }

  private void assertChildOrder(String field, String direction, String... ids) {
    String[] assertions = new String[ids.length + 1];
    assertions[0] = "//result[@name='response'][@numFound='" + ids.length + "']";
    for (int i = 0; i < ids.length; i++) {
      assertions[i + 1] = "//result/doc[" + (i + 1) + "]/str[@name='id'][.='" + ids[i] + "']";
    }
    assertQ(
        field + " " + direction,
        req(
            "q",
            "type_s1:parent",
            "parents",
            "{!parent which=type_s1:parent}type_s1:child",
            "sort",
            "childfield(" + field + ",$parents) " + direction + ",id asc",
            "fl",
            "id",
            "rows",
            "10"),
        assertions);
  }

  public void testEquality() {
    parseAssertEq("childfield(name_s1,$q) asc", "childfield(name_s1,$q) asc");
    parseAssertEq("childfield(name_s1,$q) asc", "childfield(name_s1) asc");
    parseAssertEq("childfield(name_s1,$q) asc", "childfield(name_s1,) asc");

    parseAssertNe("childfield(name_s1,$q) asc", "childfield(name_s1,$q) desc");
    parseAssertNe("childfield(name_s1,$q) asc", "childfield(surname_s1,$q) asc");
    parseAssertNe("childfield(name_s1,$q) asc", "childfield(surname_s1,$q2) desc");
  }

  public void testEqualityUpToBlockJoin() {
    parseAssertNe("childfield(name_s1,$q) asc", "childfield(name_s1,$q2) asc");
  }

  @Test(expected = SolrException.class)
  public void testNotBjqReference() {
    parse("childfield(name_s1,$notbjq) asc");
  }

  // root cause is swallowed, but it's logged there.
  @Test(expected = SolrException.class)
  public void testOmitFieldWithComma() {
    parse("childfield(,$q)  asc");
  }

  @Test(expected = SolrException.class)
  public void testOmitField() {
    parse("childfield($q)  asc");
  }

  @Test(expected = SolrException.class)
  public void testForgetEverything() {
    parse("childfield() asc");
  }

  @Test(expected = SolrException.class)
  public void testEvenBraces() {
    parse("childfield asc");
  }

  @Test(expected = SolrException.class)
  public void testAbsentField() {
    parse("childfield(NEVER_SEEN_IT,$q) asc");
  }

  @Test(expected = SolrException.class)
  public void testOmitOrder() {
    parse("childfield(name_s1,$q)");
  }

  @Test
  public void testOmitSpaceInFrontOfOrd() {
    parseAssertEq("childfield(name_s1,$q)asc", "childfield(name_s1,$q) asc");
  }

  private void parseAssertEq(String sortField, String sortField2) {
    assertEq(parse(sortField), parse(sortField2));
  }

  private void assertEq(SortField sortField, SortField sortField2) {
    assertEquals(sortField, sortField2);
    assertEquals(sortField.hashCode(), sortField2.hashCode());
  }

  private void parseAssertNe(String sortField, String sortField2) {
    assertNotEquals(parse(sortField), parse(sortField2));
  }

  private SortField parse(String a) {
    final SolrQueryRequest req =
        req(
            "q",
            "{!parent which=type_s1:parent}whatever_s1:foo",
            "q2",
            "{!parent which=type_s1:parent}nomater_s1:what",
            "notbjq",
            "foo_s1:bar");
    try {
      final SortSpec spec = SortSpecParsing.parseSortSpec(a, req);
      assertNull(spec.getSchemaFields().get(0));
      final Sort sort = spec.getSort();
      final SortField field = sort.getSort()[0];
      assertNotNull(field);
      return field;
    } finally {
      req.close();
    }
  }

  public void testCacheHits() {
    final SolrQueryRequest req = req();
    try {
      @SuppressWarnings({"rawtypes"})
      final SolrCache cache = req.getSearcher().getCache("perSegFilter");
      assertNotNull(cache);
      var core = req.getSearcher().getCore();
      double before =
          SolrMetricTestUtils.getCacheSearcherTotalLookups(
              core, SolrMetricTestUtils.PER_SEG_FILTER_CACHE);

      parse("childfield(name_s1,$q) asc");
      double after =
          SolrMetricTestUtils.getCacheSearcherTotalLookups(
              core, SolrMetricTestUtils.PER_SEG_FILTER_CACHE);
      assertEquals(
          "parsing bjq lookups parent filter,"
              + "parsing sort spec lookups parent and child filters, "
              + "hopefully for the purpose",
          3,
          (int) (after - before));
    } finally {
      req.close();
    }
  }
}
