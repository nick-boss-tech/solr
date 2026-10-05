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

import static org.apache.solr.common.params.CursorMarkParams.CURSOR_MARK_NEXT;
import static org.apache.solr.common.params.CursorMarkParams.CURSOR_MARK_START;
import static org.apache.solr.common.util.Utils.fromJSONString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

  public void testCursorMarkPagingOverMissingChildValue() throws Exception {
    clearIndex();
    for (int p = 1; p <= 6; p++) {
      final SolrInputDocument parent = new SolrInputDocument();
      parent.addField("id", String.valueOf(p));
      parent.addField("type_s1", "parent");
      final SolrInputDocument child = new SolrInputDocument();
      child.addField("id", String.valueOf(100 + p));
      child.addField("kid_s1", "yes");
      if (p % 2 == 0) {
        // odd parents have no child value to sort by, so their sort value is null
        child.addField("name_s1", "name" + p);
      }
      parent.addChildDocument(child);
      assertU(adoc(parent));
    }
    assertU(commit());

    final String q = "{!parent which=type_s1:parent}kid_s1:yes";
    final String sort = "childfield(name_s1,$q) asc, id asc";

    final List<String> expected =
        parentIds(
            assertJQ(req("q", q, "sort", sort, "rows", "10", "fl", "id"), "/response/numFound==6"));

    final List<String> paged = new ArrayList<>();
    String cursorMark = CURSOR_MARK_START;
    for (int page = 0; page < 10; page++) {
      final String json =
          assertJQ(req("q", q, "sort", sort, "rows", "1", "fl", "id", "cursorMark", cursorMark));
      paged.addAll(parentIds(json));
      final String next = (String) ((Map<?, ?>) fromJSONString(json)).get(CURSOR_MARK_NEXT);
      if (next.equals(cursorMark)) {
        break;
      }
      cursorMark = next;
    }
    assertEquals(expected, paged);
  }

  private static List<String> parentIds(String json) {
    final Map<?, ?> response = (Map<?, ?>) ((Map<?, ?>) fromJSONString(json)).get("response");
    final List<String> ids = new ArrayList<>();
    for (Object doc : (List<?>) response.get("docs")) {
      ids.add((String) ((Map<?, ?>) doc).get("id"));
    }
    return ids;
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
