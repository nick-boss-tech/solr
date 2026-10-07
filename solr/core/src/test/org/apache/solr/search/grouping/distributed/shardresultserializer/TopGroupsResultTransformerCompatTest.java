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
package org.apache.solr.search.grouping.distributed.shardresultserializer;

import java.util.List;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.grouping.distributed.command.QueryCommandResult;
import org.apache.solr.util.EmbeddedSolrServerTestRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * SOLR-14381 rolling-upgrade compatibility: a shard running this version must serialize grouping
 * counts as {@link Integer} whenever they fit, so a coordinator running the previous version (whose
 * {@code TopGroupsResultTransformer} reads {@code matches} and the per-group {@code totalHits} with
 * {@code (Integer)} casts) keeps working in a mixed cluster. Only counts that overflow the int
 * range may travel as {@link Long}; the previous version could not represent those anyway.
 */
public class TopGroupsResultTransformerCompatTest extends SolrTestCase {

  private static final long WIDE = 2_500_000_000L;

  @ClassRule
  public static final EmbeddedSolrServerTestRule solrTestRule = new EmbeddedSolrServerTestRule();

  @BeforeClass
  public static void beforeClass() throws Exception {
    solrTestRule.startSolr(SolrTestCaseJ4.TEST_HOME());
    SolrTestCaseJ4.newRandomConfig();
    solrTestRule
        .newCollection()
        .withConfigSet(SolrTestCaseJ4.TEST_COLL1_CONF())
        .withSchemaFile("schema.xml")
        .create();
  }

  private TopGroupsResultTransformer newTransformer(SolrQueryRequestBase req) {
    ResponseBuilder rb = new ResponseBuilder(req, new SolrQueryResponse(), List.of());
    return new TopGroupsResultTransformer(rb);
  }

  @Test
  public void testSerializeTopDocsCountTypes() throws Exception {
    try (SolrCore core =
            solrTestRule.getCoreContainer().getCore(SolrTestCaseJ4.DEFAULT_TEST_CORENAME);
        SolrQueryRequestBase req = new SolrQueryRequestBase(core, new ModifiableSolrParams())) {
      TopGroupsResultTransformer transformer = newTransformer(req);

      NamedList<Object> small =
          transformer.serializeTopDocs(
              new QueryCommandResult(
                  new TopDocs(new TotalHits(7, TotalHits.Relation.EQUAL_TO), new ScoreDoc[0]),
                  7,
                  Float.NaN));
      // The previous version's coordinator casts these to Integer; that must not throw.
      assertEquals(Integer.valueOf(7), (Integer) small.get("matches"));
      assertEquals(Integer.valueOf(7), (Integer) small.get("totalHits"));

      NamedList<Object> wide =
          transformer.serializeTopDocs(
              new QueryCommandResult(
                  new TopDocs(new TotalHits(WIDE, TotalHits.Relation.EQUAL_TO), new ScoreDoc[0]),
                  WIDE,
                  Float.NaN));
      assertEquals(Long.valueOf(WIDE), wide.get("matches"));
      assertEquals(Long.valueOf(WIDE), wide.get("totalHits"));
      // And this version's coordinator reads either type via Number.
      assertEquals(WIDE, ((Number) wide.get("matches")).longValue());
    }
  }

  @Test
  public void testSerializeTopGroupsPerGroupTotalHitsType() throws Exception {
    try (SolrCore core =
            solrTestRule.getCoreContainer().getCore(SolrTestCaseJ4.DEFAULT_TEST_CORENAME);
        SolrQueryRequestBase req = new SolrQueryRequestBase(core, new ModifiableSolrParams())) {
      TopGroupsResultTransformer transformer = newTransformer(req);
      SchemaField groupField = core.getLatestSchema().getField("id");

      NamedList<Object> small = transformer.serializeTopGroups(topGroups(7), groupField);
      // The previous version's coordinator casts the per-group totalHits to Integer.
      assertEquals(Integer.valueOf(7), (Integer) groupResult(small).get("totalHits"));

      NamedList<Object> wide = transformer.serializeTopGroups(topGroups(WIDE), groupField);
      assertEquals(Long.valueOf(WIDE), groupResult(wide).get("totalHits"));
    }
  }

  private TopGroups<BytesRef> topGroups(long groupTotalHits) {
    GroupDocs<BytesRef> group =
        new GroupDocs<>(
            Float.NaN,
            Float.NaN,
            new TotalHits(groupTotalHits, TotalHits.Relation.EQUAL_TO),
            new ScoreDoc[0],
            new BytesRef("grp"),
            null);
    @SuppressWarnings({"unchecked", "rawtypes"})
    GroupDocs<BytesRef>[] groups = new GroupDocs[] {group};
    return new TopGroups<>(
        Sort.RELEVANCE.getSort(), Sort.RELEVANCE.getSort(), 7, 7, groups, Float.NaN);
  }

  @SuppressWarnings("unchecked")
  private NamedList<Object> groupResult(NamedList<Object> serialized) {
    // Entries 0 and 1 are totalGroupedHitCount and totalHitCount; the group follows.
    return (NamedList<Object>) serialized.get("grp");
  }
}
