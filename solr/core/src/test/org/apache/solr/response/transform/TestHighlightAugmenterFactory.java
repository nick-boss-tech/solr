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

import java.util.List;
import java.util.Locale;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.Utils;
import org.apache.solr.util.EmbeddedSolrServerTestRule;
import org.apache.solr.util.ExternalPaths;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class TestHighlightAugmenterFactory extends SolrTestCase {

  private static final String BODY_1 =
      "The quick brown fox jumps over the lazy dog. Nothing else here. The fox ran away.";
  private static final String BODY_2 = "A story about cats. Cats sleep a lot.";

  @ClassRule
  public static final EmbeddedSolrServerTestRule solrTestRule = new EmbeddedSolrServerTestRule();

  @BeforeClass
  public static void beforeClass() throws Exception {
    solrTestRule.startSolr();
    solrTestRule.newCollection().withConfigSet(ExternalPaths.DEFAULT_CONFIGSET).create();

    SolrClient client = solrTestRule.getSolrClient();
    SolrInputDocument doc1 = new SolrInputDocument();
    doc1.addField("id", "1");
    doc1.addField("body_t", BODY_1);
    doc1.addField("title_t", "Fox story");
    doc1.addField("tags_txt", List.of("animals", "forest fox"));
    client.add(doc1);
    SolrInputDocument doc2 = new SolrInputDocument();
    doc2.addField("id", "2");
    doc2.addField("body_t", BODY_2);
    doc2.addField("title_t", "Fox cameo");
    doc2.addField("tags_txt", List.of("pets", "cats"));
    client.add(doc2);
    client.commit();
  }

  /** Queries for "fox" in body_t or title_t, sorted by id; doc 2 has no "fox" in its body. */
  private static List<SolrDocument> query(String fl, String... extraParams) throws Exception {
    SolrQuery query = new SolrQuery("body_t:fox title_t:fox");
    query.setFields(fl);
    query.set("sort", "id asc");
    for (int i = 0; i < extraParams.length; i += 2) {
      query.set(extraParams[i], extraParams[i + 1]);
    }
    List<SolrDocument> docs = solrTestRule.getSolrClient().query(query).getResults();
    assertEquals(2, docs.size());
    return docs;
  }

  @SuppressWarnings("unchecked")
  private static List<String> snippets(SolrDocument doc, String name) {
    return (List<String>) doc.getFieldValue(name);
  }

  private static void assertContains(String expected, Object actual) {
    assertTrue(String.valueOf(actual), actual instanceof String s && s.contains(expected));
  }

  @Test
  public void testExplicitField() throws Exception {
    // single-valued field and 1 snippet: a string, not a list
    List<SolrDocument> docs = query("id,body_hl:[hl f=body_t]");
    assertContains("<em>fox</em>", docs.getFirst().getFieldValue("body_hl"));
    assertNull("the highlighted field isn't returned", docs.getFirst().getFieldValue("body_t"));
    // no highlight, and the name isn't the field, so no pass-through
    assertFalse(docs.get(1).containsKey("body_hl"));

    // a list when multiple snippets may be returned
    docs = query("id,body_hl:[hl f=body_t snippets=2]");
    assertEquals(List.of(), docs.get(1).getFieldValue("body_hl"));
  }

  @Test
  public void testFieldFromAlias() throws Exception {
    List<SolrDocument> docs = query("id,body_t:[hl]");
    assertContains("<em>fox</em>", docs.getFirst().getFieldValue("body_t"));
    // no highlight: the original value passes through
    assertEquals(BODY_2, docs.get(1).getFieldValue("body_t"));

    // also when the name equals an explicitly given field
    docs = query("id,body_t:[hl f=body_t]");
    assertEquals(BODY_2, docs.get(1).getFieldValue("body_t"));
  }

  @Test
  public void testMultiValuedPassThrough() throws Exception {
    List<SolrDocument> docs = query("id,tags_txt:[hl]");
    List<String> snippets = snippets(docs.getFirst(), "tags_txt");
    assertEquals(List.of("forest <em>fox</em>"), snippets);
    assertEquals(List.of("pets", "cats"), docs.get(1).getFieldValue("tags_txt"));
  }

  @Test
  public void testLocalParams() throws Exception {
    List<SolrDocument> docs =
        query("id,body_hl:[hl f=body_t snippets=2 fragsize=1 tag.pre='[' tag.post=']']");
    List<String> snippets = snippets(docs.getFirst(), "body_hl");
    assertEquals(snippets.toString(), 2, snippets.size());
    for (String snippet : snippets) {
      assertTrue(snippet, snippet.contains("[fox]"));
    }

    // q overrides the main query
    docs = query("id,body_hl:[hl f=body_t q=body_t:cats]");
    assertFalse(docs.getFirst().containsKey("body_hl"));
    assertContains("<em>cats</em>", docs.get(1).getFieldValue("body_hl"));

    // defaultSummary returns leading text when nothing matches
    docs = query("id,body_hl:[hl f=body_t defaultSummary=true fragsize=0]");
    assertEquals(BODY_2, docs.get(1).getFieldValue("body_hl"));
  }

  @Test
  public void testRequestParamFallback() throws Exception {
    List<SolrDocument> docs =
        query("id,body_hl:[hl f=body_t]", "hl.tag.pre", "[", "hl.tag.post", "]");
    assertContains("[fox]", docs.getFirst().getFieldValue("body_hl"));

    // local params override the request's, even per-field ones
    docs = query("id,body_hl:[hl f=body_t tag.pre=< tag.post=>]", "hl.tag.pre", "[");
    assertContains("<fox>", docs.getFirst().getFieldValue("body_hl"));
    docs = query("id,body_hl:[hl f=body_t tag.pre=< tag.post=>]", "f.body_t.hl.tag.pre", "[");
    assertContains("<fox>", docs.getFirst().getFieldValue("body_hl"));

    // the request's snippets param determines if the output is a list
    docs = query("id,body_hl:[hl f=body_t]", "hl.snippets", "2");
    assertEquals(List.of(), docs.get(1).getFieldValue("body_hl"));
  }

  @Test
  public void testMultipleInstances() throws Exception {
    List<SolrDocument> docs = query("id,body_t:[hl],title_t:[hl tag.pre=* tag.post=*]");
    assertContains("<em>fox</em>", docs.getFirst().getFieldValue("body_t"));
    assertEquals("*Fox* story", docs.getFirst().getFieldValue("title_t"));
  }

  @Test
  public void testRealTimeGet() throws Exception {
    // no query, so the original value passes through
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("fl", "id,body_t:[hl]");
    SolrDocument doc = solrTestRule.getSolrClient().getById("1", params);
    assertEquals(BODY_1, doc.getFieldValue("body_t"));
  }

  @Test
  public void testErrors() {
    SolrException e = expectThrows(SolrException.class, () -> query("id,[hl]"));
    assertEquals(SolrException.ErrorCode.BAD_REQUEST.code, e.code());
    e = expectThrows(SolrException.class, () -> query("id,body_t:[hl bogus=1]"));
    assertEquals(SolrException.ErrorCode.BAD_REQUEST.code, e.code());
  }

  private static String review5126Doc2Highlight(boolean explicitQuery) throws Exception {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("q", "fox");
    params.set("defType", "edismax");
    params.set("qf", "body_t title_t");
    params.set("bq", "body_t:cats");
    params.set("sort", "id asc");
    params.set("rows", "10");
    params.set("fl", "id,body_hl:[hl f=body_t]");
    params.set("hl.method", "unified");
    params.set("hl.defaultSummary", false);
    params.set("hl.tag.pre", "<em>");
    params.set("hl.tag.post", "</em>");
    params.set("hl.weightMatches", false);
    if (explicitQuery) {
      params.set("hl.q", "fox");
    }
    var docs = solrTestRule.getSolrClient().query(params).getResults();
    assertEquals(2, docs.size());
    assertEquals("2", docs.get(1).getFieldValue("id"));
    return String.valueOf(docs.get(1).getFieldValue("body_hl"));
  }

  @Test
  public void testReview5126ImplicitBoostDiffersFromExplicit() throws Exception {
    String implicit = review5126Doc2Highlight(false);
    String explicit = review5126Doc2Highlight(true);
    System.out.println("PR5126_IMPLICIT_DOC2 " + implicit);
    System.out.println("PR5126_EXPLICIT_DOC2 " + explicit);
    assertContains("<em>cats</em>", implicit);
    assertFalse(explicit, explicit.contains("<em>cats</em>"));
  }

  @Test
  public void testReview5126QueryExamples() throws Exception {
    for (boolean weightMatches : List.of(true, false)) {
      for (boolean boost : List.of(false, true)) {
        for (boolean component : List.of(false, true)) {
          for (String variant : List.of("implicit", "explicit", "local")) {
            ModifiableSolrParams params = new ModifiableSolrParams();
            params.set("q", "fox");
            params.set("defType", "edismax");
            params.set("qf", "body_t title_t");
            params.set("sort", "id asc");
            params.set("rows", "10");
            params.set(
                "fl",
                variant.equals("local")
                    ? "id,body_hl:[hl f=body_t q=fox]"
                    : "id,body_hl:[hl f=body_t]");
            params.set("hl", component);
            params.set("hl.method", "unified");
            params.set("hl.fl", "body_t");
            params.set("hl.defaultSummary", false);
            params.set("hl.tag.pre", "<em>");
            params.set("hl.tag.post", "</em>");
            params.set("hl.weightMatches", weightMatches);
            if (boost) {
              params.set("bq", "body_t:cats");
            }
            if (variant.equals("explicit")) {
              params.set("hl.q", "fox");
            }
            var response = solrTestRule.getSolrClient().query(params);
            String label =
                "variant="
                    + variant
                    + " boost="
                    + boost
                    + " component="
                    + component
                    + " weightMatches="
                    + weightMatches;
            System.out.println("PR5126_QUERY " + label + " request=" + params);
            System.out.println(
                "PR5126_RESPONSE " + label + " " + Utils.toJSONString(response.getResponse()));
            var docs = response.getResults();
            assertEquals(label, 2, docs.size());
            assertEquals("1", docs.getFirst().getFieldValue("id"));
            assertEquals("2", docs.get(1).getFieldValue("id"));
            assertContains("<em>fox</em>", docs.getFirst().getFieldValue("body_hl"));
            String inline =
                String.valueOf(docs.get(1).getFieldValue("body_hl")).toLowerCase(Locale.ROOT);
            if (!boost || !variant.equals("implicit")) {
              assertFalse(label + " inline=" + inline, inline.contains("<em>cats</em>"));
            }
          }
        }
      }
    }
  }
}
