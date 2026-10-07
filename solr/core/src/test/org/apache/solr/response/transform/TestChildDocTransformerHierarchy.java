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
package org.apache.solr.response.transform;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.lucene.index.IndexableField;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.BasicResultContext;
import org.apache.solr.util.RandomNoReverseMergePolicyFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class TestChildDocTransformerHierarchy extends SolrTestCaseJ4 {

  private static AtomicInteger idCounter = new AtomicInteger();
  private static final String[] types = {"donut", "cake"};
  private static final String[] ingredients = {"flour", "cocoa", "vanilla"};
  private static final Iterator<String> ingredientsCycler =
      Stream.generate(() -> List.of(ingredients)).flatMap(Collection::stream).iterator();
  private static final String[] names = {"Yaz", "Jazz", "Costa"};
  private static final String[] fieldsToRemove = {"_nest_parent_", "_nest_path_", "_root_"};
  private static final int sumOfDocsPerNestedDocument = 8;
  private static final int numberOfDocsPerNestedTest = 10;
  // filter documents that were created for random segments to ensure the transformer works with
  // multiple segments.
  private static final String fqToExcludeNonTestedDocs = "{!frange l=0}id_i";

  @ClassRule
  public static final TestRule noReverseMerge = RandomNoReverseMergePolicyFactory.createRule();

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig-minimal.xml", "schema-nest.xml"); // use "nest" schema

    if (random().nextBoolean()) {
      // start docIDs at -100 for these random docs we don't care about (all less than 0)
      idCounter.set(-100);
      // create random segments
      final int numOfDocs = 10;
      for (int i = 0; i < numOfDocs; ++i) {
        updateJ(generateDocHierarchy(i), null);
        if (random().nextBoolean()) {
          assertU(commit());
        }
      }
      assertU(commit());
    }
  }

  @Before
  public void before() {
    idCounter.set(0); // reset idCounter
  }

  @After
  public void after() {
    assertU(delQ(fqToExcludeNonTestedDocs));
    assertU(commit());
  }

  @Test
  public void testNonTrivialChildFilter() {
    // just check we don't throw an exception. This used to throw before SOLR-15152
    assertQ(
        req(
            "q",
            "*:*",
            "sort",
            "id asc",
            "fl",
            "*, _nest_path_, [child childFilter='type_s:Regular OR type_s:Chocolate']",
            "fq",
            fqToExcludeNonTestedDocs));
  }

  @Test
  public void testParentFilterJSON() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);
    String[] tests =
        new String[] {
          "/response/docs/[0]/type_s==donut",
          "/response/docs/[0]/toppings/[0]/type_s==Regular",
          "/response/docs/[0]/toppings/[1]/type_s==Chocolate",
          "/response/docs/[0]/toppings/[0]/ingredients/[0]/name_s==cocoa",
          "/response/docs/[0]/toppings/[1]/ingredients/[1]/name_s==cocoa",
          "/response/docs/[0]/lonely/test_s==testing",
          "/response/docs/[0]/lonely/lonelyGrandChild/test2_s==secondTest",
        };

    try (SolrQueryRequest req =
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "*, _nest_path_, [child]",
            "fq",
            fqToExcludeNonTestedDocs)) {
      BasicResultContext res =
          (BasicResultContext) h.queryAndResponse("/select", req).getResponse();
      Iterator<SolrDocument> docsStreamer = res.getProcessedDocuments();
      while (docsStreamer.hasNext()) {
        SolrDocument doc = docsStreamer.next();
        cleanSolrDocumentFields(doc);
        int currDocId = Integer.parseInt((doc.getFirstValue("id")).toString());
        assertEquals(
            "queried docs are not equal to expected output for id: " + currDocId,
            fullNestedDocTemplate(currDocId),
            doc.toString());
      }
    }

    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "*, _nest_path_, [child]",
            "fq",
            fqToExcludeNonTestedDocs),
        tests);
  }

  @Test
  public void testNestedChildrenRetainedWhenFlOmitsNestPathsJSON() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);
    // SOLR-14678: fl names only id and [child]. The writers must still keep the
    // nested named children (ingredients under toppings, lonelyGrandChild under
    // lonely), not just the paths attached directly to the root doc. The child
    // docs carry only the fields fl permits (here id), as the XML variant of
    // this test shows in the raw response.
    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "id,[child]",
            "fq",
            fqToExcludeNonTestedDocs),
        "/response/docs/[0]/toppings/[0]/id=='4'",
        "/response/docs/[0]/toppings/[1]/id=='6'",
        "/response/docs/[0]/toppings/[0]/ingredients/[0]/id=='5'",
        "/response/docs/[0]/toppings/[1]/ingredients/[1]/id=='8'",
        "/response/docs/[0]/lonely/id=='2'",
        "/response/docs/[0]/lonely/lonelyGrandChild/id=='3'");

    // When fl also names the child docs' own fields, those fields are present
    // at every level of the retained structure.
    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "id,type_s,name_s,test_s,test2_s,[child]",
            "fq",
            fqToExcludeNonTestedDocs),
        "/response/docs/[0]/toppings/[0]/type_s==Regular",
        "/response/docs/[0]/toppings/[1]/type_s==Chocolate",
        "/response/docs/[0]/toppings/[0]/ingredients/[0]/name_s==cocoa",
        "/response/docs/[0]/toppings/[1]/ingredients/[1]/name_s==cocoa",
        "/response/docs/[0]/lonely/test_s==testing",
        "/response/docs/[0]/lonely/lonelyGrandChild/test2_s==secondTest");
  }

  @Test
  public void testNestedChildrenRetainedWhenFlOmitsNestPathsXML() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);
    // SOLR-14678: same repro as the JSON variant, through the XML writer.
    assertQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "id,[child]",
            "fq",
            fqToExcludeNonTestedDocs),
        "/response/result/doc[1]/arr[@name='toppings']/doc[1]/str[@name='id']='4'",
        "/response/result/doc[1]/arr[@name='toppings']/doc[1]/arr[@name='ingredients']/doc[1]/str[@name='id']='5'",
        "/response/result/doc[1]/doc[@name='lonely']/str[@name='id']='2'",
        "/response/result/doc[1]/doc[@name='lonely']/doc[@name='lonelyGrandChild']/str[@name='id']='3'");
  }

  @Test
  public void testNestedChildrenRetainedWithChildFilterAndFlOmissionJSON() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);
    // SOLR-14678: with a child filter, matched grandchildren and their ancestors
    // are kept, and the nested path fields survive an fl that omits them.
    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "id,[child childFilter='name_s:cocoa']",
            "fq",
            fqToExcludeNonTestedDocs),
        "/response/docs/[0]/toppings/[0]/ingredients/[0]/id=='5'",
        "/response/docs/[0]/toppings/[1]/ingredients/[0]/id=='7'",
        "/response/docs/[0]/toppings/[1]/ingredients/[1]/id=='8'",
        "!/response/docs/[0]/lonely/id=='2'");
  }

  @Test
  public void testNestedChildrenRetainedWithAliasAndFlOmissionJSON() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);
    // SOLR-14678: the alias form fake:[child] must retain nested named children too.
    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "id,fake:[child]",
            "fq",
            fqToExcludeNonTestedDocs),
        "/response/docs/[0]/toppings/[0]/ingredients/[0]/id=='5'",
        "/response/docs/[0]/toppings/[1]/ingredients/[1]/id=='8'",
        "/response/docs/[0]/lonely/lonelyGrandChild/id=='3'");
  }

  @Test
  public void testExtraResponseFieldsDoNotLeakAcrossDocuments() throws Exception {
    // SOLR-14678: the nest-path names a transformer reports via getExtraResponseFields
    // must stay scoped to the document being transformed. One doc nests a child
    // under the "name_s" path; another doc has an ordinary name_s value and no
    // children. While that second doc is written, wantsField("name_s") must be false:
    // the name was never requested in fl, so the ordinary field must not pass
    // the projection just because another document's children used the same name.
    final int parentId = id();
    final int childId = id();
    final int plainId = id();
    updateJ(
        "{\"add\":{\"doc\":{\"id\": "
            + parentId
            + ", \"type_s\": \"donut\", \"name_s\": {\"id\": "
            + childId
            + ", \"name_s\": \"inner\"}}}}",
        null);
    updateJ(
        "{\"add\":{\"doc\":{\"id\": "
            + plainId
            + ", \"type_s\": \"cake\", \"name_s\": \"plain\"}}}",
        null);
    assertU(commit());

    // Roots only: the nested child carries no type_s, so it cannot stream as a
    // root result. Sorting on id_i (a copyField of the sequential ids) streams
    // the parent before the plain doc on every seed.
    try (SolrQueryRequest req =
        req(
            "q",
            "type_s:(donut OR cake)",
            "sort",
            "id_i asc",
            "fl",
            "id,[child]",
            "fq",
            fqToExcludeNonTestedDocs)) {
      BasicResultContext res =
          (BasicResultContext) h.queryAndResponse("/select", req).getResponse();
      Iterator<SolrDocument> docsStreamer = res.getProcessedDocuments();

      // Sample the return-fields state while each document is the one being
      // written; that state is exactly what the response writers consult.
      Map<String, Boolean> nameWantedByDoc = new HashMap<>();
      Map<String, Object> nameValueByDoc = new HashMap<>();
      while (docsStreamer.hasNext()) {
        SolrDocument doc = docsStreamer.next();
        String docId = doc.getFirstValue("id").toString();
        nameWantedByDoc.put(docId, res.getReturnFields().wantsField("name_s"));
        nameValueByDoc.put(docId, doc.getFieldValue("name_s"));
      }
      assertEquals(2, nameWantedByDoc.size());

      assertTrue(
          "parent doc should carry its nested child under name_s",
          nameValueByDoc.get(String.valueOf(parentId)) instanceof SolrDocument);
      assertEquals(
          "while the parent is written, its nest-path field must pass wantsField",
          Boolean.TRUE,
          nameWantedByDoc.get(String.valueOf(parentId)));
      assertEquals(
          "nest-path names from the previous document must not widen this document's projection",
          Boolean.FALSE,
          nameWantedByDoc.get(String.valueOf(plainId)));
    }
  }

  @Test
  public void testParentFilterLimitJSON() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);

    try (SolrQueryRequest req =
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "id, type_s, toppings, _nest_path_, [child childFilter='{!field f=_nest_path_}/toppings' limit=1]",
            "fq",
            fqToExcludeNonTestedDocs)) {
      BasicResultContext res =
          (BasicResultContext) h.queryAndResponse("/select", req).getResponse();
      Iterator<SolrDocument> docsStreamer = res.getProcessedDocuments();
      while (docsStreamer.hasNext()) {
        SolrDocument doc = docsStreamer.next();
        cleanSolrDocumentFields(doc);
        assertFalse("root doc should not have anonymous child docs", doc.hasChildDocuments());
        assertEquals("should only have 1 child doc", 1, doc.getFieldValues("toppings").size());
      }
    }

    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "*, [child limit=1]",
            "fq",
            fqToExcludeNonTestedDocs),
        "/response/docs/[0]/type_s==donut",
        "/response/docs/[0]/lonely/test_s==testing",
        "/response/docs/[0]/lonely/lonelyGrandChild/test2_s==secondTest",
        // "!" (negate): don't find toppings.  The "limit" kept us from reaching these, which follow
        // lonely.
        "!/response/docs/[0]/toppings/[0]/type_s==Regular");
  }

  @Test
  public void testWithDeletedChildren() throws Exception {
    // add a doc to create another segment
    final String addNonTestedDoc =
        "{\n"
            + "\"add\": {\n"
            + "\"doc\": {\n"
            + "\"id\": "
            + -1000
            + ", \n"
            + "\"type_s\": \"cake\", \n"
            + "}\n"
            + "}\n"
            + "}";

    if (random().nextBoolean()) {
      updateJ(addNonTestedDoc, null);
      assertU(commit());
    }

    indexSampleData(numberOfDocsPerNestedTest);
    // delete toppings path
    assertU(delQ("_nest_path_:\\/toppings"));
    assertU(commit());

    try (SolrQueryRequest req =
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "id, type_s, toppings, _nest_path_, [child childFilter='_nest_path_:\\\\/toppings' limit=1]",
            "fq",
            fqToExcludeNonTestedDocs)) {
      BasicResultContext res =
          (BasicResultContext) h.queryAndResponse("/select", req).getResponse();
      Iterator<SolrDocument> docsStreamer = res.getProcessedDocuments();
      while (docsStreamer.hasNext()) {
        SolrDocument doc = docsStreamer.next();
        cleanSolrDocumentFields(doc);
        assertFalse("root doc should not have anonymous child docs", doc.hasChildDocuments());
        assertNull("should not include deleted docs", doc.getFieldValues("toppings"));
      }
    }

    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "*, [child limit=1]",
            "fq",
            fqToExcludeNonTestedDocs),
        "/response/docs/[0]/type_s==donut",
        "/response/docs/[0]/lonely/test_s==testing",
        "/response/docs/[0]/lonely/lonelyGrandChild/test2_s==secondTest",
        // "!" (negate): don't find toppings.  The "limit" kept us from reaching these, which follow
        // lonely.
        "!/response/docs/[0]/toppings/[0]/type_s==Regular");
  }

  @Test
  public void testChildFilterLimitJSON() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);

    try (SolrQueryRequest req =
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "*, _nest_path_, " + "[child limit='1' childFilter='toppings/type_s:Regular']",
            "fq",
            fqToExcludeNonTestedDocs)) {
      BasicResultContext res =
          (BasicResultContext) h.queryAndResponse("/select", req).getResponse();
      Iterator<SolrDocument> docsStreamer = res.getProcessedDocuments();
      while (docsStreamer.hasNext()) {
        SolrDocument doc = docsStreamer.next();
        cleanSolrDocumentFields(doc);
        assertFalse("root doc should not have anonymous child docs", doc.hasChildDocuments());
        assertEquals("should only have 1 child doc", 1, doc.getFieldValues("toppings").size());
        assertEquals(
            "should be of type_s:Regular",
            "Regular",
            ((SolrDocument) doc.getFirstValue("toppings")).getFieldValue("type_s"));
      }
    }

    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "id, type_s, toppings, _nest_path_, [child limit='10' childFilter='toppings/type_s:Regular']",
            "fq",
            fqToExcludeNonTestedDocs),
        "/response/docs/[0]/type_s==donut",
        "/response/docs/[0]/toppings/[0]/type_s==Regular");
  }

  @Test
  public void testExactPath() throws Exception {
    indexSampleData(2);
    String[] tests = {
      "/response/numFound==4",
      "/response/docs/[0]/_nest_path_=='/toppings#0'",
      "/response/docs/[1]/_nest_path_=='/toppings#0'",
      "/response/docs/[2]/_nest_path_=='/toppings#1'",
      "/response/docs/[3]/_nest_path_=='/toppings#1'",
    };

    assertJQ(
        req(
            "q",
            "_nest_path_:*toppings",
            "sort",
            "_nest_path_ asc",
            "fl",
            "*, id_i, _nest_path_",
            "fq",
            fqToExcludeNonTestedDocs),
        tests);

    assertJQ(
        req(
            "q",
            "+_nest_path_:\"/toppings\"",
            "sort",
            "_nest_path_ asc",
            "fl",
            "*, _nest_path_",
            "fq",
            fqToExcludeNonTestedDocs),
        tests);
  }

  @Test
  public void testChildFilterJSON() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);
    String[] tests =
        new String[] {
          "/response/docs/[0]/type_s==donut", "/response/docs/[0]/toppings/[0]/type_s==Regular",
        };

    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "*,[child childFilter='toppings/type_s:Regular']",
            "fq",
            fqToExcludeNonTestedDocs),
        tests);
  }

  @Test
  public void testGrandChildFilterJSON() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);
    String[] tests =
        new String[] {
          "/response/docs/[0]/type_s==donut",
          "/response/docs/[0]/toppings/[0]/ingredients/[0]/name_s==cocoa"
        };

    try (SolrQueryRequest req =
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "*,[child childFilter='toppings/ingredients/name_s:cocoa'],",
            "fq",
            fqToExcludeNonTestedDocs)) {
      BasicResultContext res =
          (BasicResultContext) h.queryAndResponse("/select", req).getResponse();
      Iterator<SolrDocument> docsStreamer = res.getProcessedDocuments();
      while (docsStreamer.hasNext()) {
        SolrDocument doc = docsStreamer.next();
        cleanSolrDocumentFields(doc);
        int currDocId = Integer.parseInt((doc.getFirstValue("id")).toString());
        assertEquals(
            "queried docs are not equal to expected output for id: " + currDocId,
            grandChildDocTemplate(currDocId),
            doc.toString());
      }
    }

    // test full path
    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "*,[child childFilter='toppings/ingredients/name_s:cocoa']",
            "fq",
            fqToExcludeNonTestedDocs),
        tests);

    // test partial path
    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "*,[child childFilter='ingredients/name_s:cocoa']",
            "fq",
            fqToExcludeNonTestedDocs),
        tests);

    // test absolute path
    assertJQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "*,[child childFilter='/toppings/ingredients/name_s:cocoa']",
            "fq",
            fqToExcludeNonTestedDocs),
        tests);
  }

  @Test
  public void testNestPathTransformerMatches() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);

    // test partial path
    // should not match any child docs
    assertQ(
        req(
            "q",
            "type_s:donut",
            "sort",
            "id asc",
            "fl",
            "*,[child childFilter='redients/name_s:cocoa']",
            "fq",
            fqToExcludeNonTestedDocs),
        "//result/doc/str[@name='type_s'][.='donut']",
        "not(//result/doc/arr[@name='toppings'])");
  }

  @Test
  public void testSingularChildFilterJSON() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);
    String[] tests =
        new String[] {
          "/response/docs/[0]/type_s==cake",
          "/response/docs/[0]/lonely/test_s==testing",
          "/response/docs/[0]/lonely/lonelyGrandChild/test2_s==secondTest"
        };

    assertJQ(
        req(
            "q",
            "type_s:cake",
            "sort",
            "id asc",
            "fl",
            "*,[child childFilter='lonely/lonelyGrandChild/test2_s:secondTest']",
            "fq",
            fqToExcludeNonTestedDocs),
        tests);
  }

  @Test
  public void testNonRootChildren() throws Exception {
    indexSampleData(numberOfDocsPerNestedTest);
    assertJQ(
        req(
            "q",
            "test_s:testing",
            "sort",
            "id asc",
            "fl",
            "*,[child childFilter='lonely/lonelyGrandChild/test2_s:secondTest']",
            "fq",
            fqToExcludeNonTestedDocs),
        "/response/docs/[0]/test_s==testing",
        "/response/docs/[0]/lonelyGrandChild/test2_s==secondTest");

    assertJQ(
        req(
            "q",
            "type_s:Chocolate",
            "sort",
            "id asc",
            "fl",
            "*,[child]",
            "fq",
            fqToExcludeNonTestedDocs),
        "/response/docs/[0]/type_s==Chocolate",
        "/response/docs/[0]/ingredients/[0]/name_s==cocoa",
        "/response/docs/[0]/ingredients/[1]/name_s==cocoa");
  }

  @Test
  public void testExceptionThrownWParentFilter() {
    expectThrows(
        SolrException.class,
        "Exception was not thrown when parentFilter param was passed to ChildDocTransformer using a nested schema",
        () ->
            assertJQ(
                req(
                    "q",
                    "test_s:testing",
                    "sort",
                    "id asc",
                    "fl",
                    "*,[child childFilter='lonely/lonelyGrandChild/test2_s:secondTest' parentFilter='_nest_path_:\"lonely/\"']",
                    "fq",
                    fqToExcludeNonTestedDocs),
                "/response/docs/[0]/test_s==testing",
                "/response/docs/[0]/lonelyGrandChild/test2_s==secondTest"));
  }

  @Test
  public void testNoChildren() throws Exception {
    final String addDocWoChildren =
        "{\n"
            + "\"add\": {\n"
            + "\"doc\": {\n"
            + "\"id\": "
            + id()
            + ", \n"
            + "\"type_s\": \"cake\", \n"
            + "}\n"
            + "}\n"
            + "}";
    updateJ(addDocWoChildren, null);
    assertU(commit());

    assertJQ(
        req(
            "q",
            "type_s:cake",
            "sort",
            "id asc",
            "fl",
            "*,[child childFilter='lonely/lonelyGrandChild/test2_s:secondTest']",
            "fq",
            fqToExcludeNonTestedDocs),
        "/response/docs/[0]/type_s==cake");
  }

  private void indexSampleData(int numDocs) throws Exception {
    for (int i = 0; i < numDocs; ++i) {
      updateJ(generateDocHierarchy(i), null);
    }
    assertU(commit());
  }

  private static int id() {
    return idCounter.incrementAndGet();
  }

  @SuppressWarnings({"unchecked"})
  private static void cleanSolrDocumentFields(SolrDocument input) {
    for (String fieldName : fieldsToRemove) {
      input.removeFields(fieldName);
    }
    for (Map.Entry<String, Object> field : input) {
      Object val = field.getValue();
      if (val instanceof Collection) {
        Object newVals =
            ((Collection) val)
                .stream().map((item) -> (cleanIndexableField(item))).collect(Collectors.toList());
        input.setField(field.getKey(), newVals);
        continue;
      }
      input.setField(field.getKey(), cleanIndexableField(field.getValue()));
    }
  }

  private static Object cleanIndexableField(Object field) {
    if (field instanceof IndexableField) {
      return ((IndexableField) field).stringValue();
    } else if (field instanceof SolrDocument) {
      cleanSolrDocumentFields((SolrDocument) field);
    }
    return field;
  }

  private static String grandChildDocTemplate(int id) {
    final int docNum =
        id / sumOfDocsPerNestedDocument; // the index of docs sent to solr in the AddUpdateCommand.
    // e.g. first doc is 0
    return "SolrDocument{id="
        + id
        + ", type_s="
        + types[docNum % types.length]
        + ", name_s="
        + names[docNum % names.length]
        + ", "
        + "toppings=["
        + "SolrDocument{id="
        + (id + 3)
        + ", type_s=Regular, "
        + "ingredients=[SolrDocument{id="
        + (id + 4)
        + ", name_s=cocoa}]}, "
        + "SolrDocument{id="
        + (id + 5)
        + ", type_s=Chocolate, "
        + "ingredients=[SolrDocument{id="
        + (id + 6)
        + ", name_s=cocoa}, SolrDocument{id="
        + (id + 7)
        + ", name_s=cocoa}]}]}";
  }

  private static String fullNestedDocTemplate(int id) {
    final int docNum =
        id / sumOfDocsPerNestedDocument; // the index of docs sent to solr in the AddUpdateCommand.
    // e.g. first doc is 0
    boolean doubleIngredient = docNum % 2 == 0;
    String currIngredient = doubleIngredient ? ingredients[1] : ingredientsCycler.next();
    return "SolrDocument{id="
        + id
        + ", type_s="
        + types[docNum % types.length]
        + ", name_s="
        + names[docNum % names.length]
        + ", "
        + "lonely=SolrDocument{id="
        + (id + 1)
        + ", test_s=testing, "
        + "lonelyGrandChild=SolrDocument{id="
        + (id + 2)
        + ", test2_s=secondTest}}, "
        + "toppings=["
        + "SolrDocument{id="
        + (id + 3)
        + ", type_s=Regular, "
        + "ingredients=[SolrDocument{id="
        + (id + 4)
        + ", name_s="
        + currIngredient
        + "}]}, "
        + "SolrDocument{id="
        + (id + 5)
        + ", type_s=Chocolate, "
        + "ingredients=[SolrDocument{id="
        + (id + 6)
        + ", name_s=cocoa}, SolrDocument{id="
        + (id + 7)
        + ", name_s=cocoa}]}]}";
  }

  private static String generateDocHierarchy(int sequence) {
    boolean doubleIngredient = sequence % 2 == 0;
    String currIngredient = doubleIngredient ? ingredients[1] : ingredientsCycler.next();
    return "{\n"
        + "\"add\": {\n"
        + "\"doc\": {\n"
        + "\"id\": "
        + id()
        + ", \n"
        + "\"type_s\": \""
        + types[sequence % types.length]
        + "\", \n"
        + "\"lonely\": {\"id\": "
        + id()
        + ", \"test_s\": \"testing\", \"lonelyGrandChild\": {\"id\": "
        + id()
        + ", \"test2_s\": \"secondTest\"}}, \n"
        + "\"name_s\": "
        + names[sequence % names.length]
        + "\"toppings\": [ \n"
        + "{\"id\": "
        + id()
        + ", \"type_s\":\"Regular\","
        + "\"ingredients\": [{\"id\": "
        + id()
        + ","
        + "\"name_s\": \""
        + currIngredient
        + "\"}]"
        + "},\n"
        + "{\"id\": "
        + id()
        + ", \"type_s\":\"Chocolate\","
        + "\"ingredients\": [{\"id\": "
        + id()
        + ","
        + "\"name_s\": \""
        + ingredients[1]
        + "\"},"
        + "{\"id\": "
        + id()
        + ",\n"
        + "\"name_s\": \""
        + ingredients[1]
        + "\""
        + "}]"
        + "}]\n"
        + "}\n"
        + "}\n"
        + "}";
  }
}
