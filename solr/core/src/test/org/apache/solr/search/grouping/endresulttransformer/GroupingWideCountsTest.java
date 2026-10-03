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
package org.apache.solr.search.grouping.endresulttransformer;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.Grouping;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.search.SortSpec;
import org.apache.solr.search.grouping.GroupingSpecification;
import org.apache.solr.search.grouping.distributed.command.QueryCommandResult;
import org.apache.solr.util.EmbeddedSolrServerTestRule;
import org.apache.solr.util.RefCounted;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * SOLR-14381: grouping counts that aggregate past {@link Integer#MAX_VALUE} in a distributed search
 * must stay wide (long) in the final response. Lucene's {@link TopGroups} still carries an int
 * totalHitCount, which overflows; Solr's own accumulators on the {@link ResponseBuilder} are the
 * wide source the end result transformers must read.
 */
public class GroupingWideCountsTest extends SolrTestCase {

  private static final long WIDE_MATCHES = 2_500_000_000L;
  private static final long WIDE_NGROUPS = 2_200_000_000L;
  private static final String FIELD = "group_s";
  private static final String QUERY = "myquery";

  @ClassRule
  public static final EmbeddedSolrServerTestRule solrTestRule = new EmbeddedSolrServerTestRule();

  @BeforeClass
  public static void beforeClass() throws Exception {
    solrTestRule.startSolr(SolrTestCaseJ4.TEST_HOME());
    // Sets the randomized solr.tests.* properties the collection1 solrconfig.xml requires.
    SolrTestCaseJ4.newRandomConfig();
    solrTestRule
        .newCollection()
        .withConfigSet(SolrTestCaseJ4.TEST_COLL1_CONF())
        .withSchemaFile("schema.xml")
        .create();
  }

  private ResponseBuilder newResponseBuilder(Grouping.Format format) {
    ResponseBuilder rb = new ResponseBuilder(null, new SolrQueryResponse(), List.of());
    GroupingSpecification spec = new GroupingSpecification();
    spec.setFields(new String[] {FIELD});
    spec.setQueries(new String[] {QUERY});
    spec.setResponseFormat(format);
    // SortSpec fields must parallel the sort fields; the score field has no SchemaField.
    spec.setGroupSortSpec(new SortSpec(Sort.RELEVANCE, Arrays.asList((SchemaField) null), 10, 0));
    spec.setWithinGroupSortSpec(
        new SortSpec(Sort.RELEVANCE, Arrays.asList((SchemaField) null), 10, 0));
    rb.setGroupingSpec(spec);
    // The state the distributed merge leaves behind: wide totals as longs.
    rb.totalHitCount = WIDE_MATCHES;
    rb.mergedGroupCounts.put(FIELD, WIDE_NGROUPS);
    return rb;
  }

  private TopGroups<BytesRef> wideTopGroups() {
    // The merged TopGroups still carries the int totalHitCount, here the overflowed
    // (negative) cast of the wide total, exactly what a Lucene merge produces.
    @SuppressWarnings("unchecked")
    GroupDocs<BytesRef>[] noGroups = (GroupDocs<BytesRef>[]) Array.newInstance(GroupDocs.class, 0);
    TopGroups<BytesRef> topGroups =
        new TopGroups<>(
            Sort.RELEVANCE.getSort(),
            Sort.RELEVANCE.getSort(),
            (int) WIDE_MATCHES,
            0,
            noGroups,
            Float.NaN);
    return new TopGroups<>(topGroups, 2_000_000_000);
  }

  @Test
  public void testGroupedFormatWideCounts() throws Exception {
    ResponseBuilder rb = newResponseBuilder(Grouping.Format.grouped);
    Map<String, Object> result = Map.of(FIELD, wideTopGroups());
    try (SolrCore core =
        solrTestRule.getCoreContainer().getCore(SolrTestCaseJ4.DEFAULT_TEST_CORENAME)) {
      RefCounted<SolrIndexSearcher> ref = core.getSearcher();
      try {
        new GroupedEndResultTransformer(ref.get()).transform(result, rb, doc -> null);
      } finally {
        ref.decref();
      }
    }
    NamedList<Object> command = groupedCommand(rb, FIELD);
    assertEquals(WIDE_MATCHES, ((Number) command.get("matches")).longValue());
    assertEquals(WIDE_NGROUPS, ((Number) command.get("ngroups")).longValue());
  }

  @Test
  public void testSimpleFormatWideCounts() {
    ResponseBuilder rb = newResponseBuilder(Grouping.Format.simple);
    Map<String, Object> result = new LinkedHashMap<>();
    result.put(FIELD, wideTopGroups());
    TopDocs topDocs =
        new TopDocs(new TotalHits(WIDE_MATCHES, TotalHits.Relation.EQUAL_TO), new ScoreDoc[0]);
    result.put(QUERY, new QueryCommandResult(topDocs, WIDE_MATCHES, Float.NaN));
    new SimpleEndResultTransformer().transform(result, rb, doc -> null);

    NamedList<Object> fieldCommand = groupedCommand(rb, FIELD);
    assertEquals(WIDE_MATCHES, ((Number) fieldCommand.get("matches")).longValue());
    assertEquals(2_000_000_000L, ((Number) fieldCommand.get("ngroups")).longValue());
    SolrDocumentList docList = (SolrDocumentList) fieldCommand.get("doclist");
    // Before the fix this read the overflowed int totalHitCount off TopGroups.
    assertEquals(WIDE_MATCHES, docList.getNumFound());

    NamedList<Object> queryCommand = groupedCommand(rb, QUERY);
    assertEquals(WIDE_MATCHES, ((Number) queryCommand.get("matches")).longValue());
    assertEquals(WIDE_MATCHES, ((SolrDocumentList) queryCommand.get("doclist")).getNumFound());
  }

  @SuppressWarnings("unchecked")
  private NamedList<Object> groupedCommand(ResponseBuilder rb, String key) {
    NamedList<Object> grouped = (NamedList<Object>) rb.rsp.getValues().get("grouped");
    assertNotNull("grouped section missing", grouped);
    NamedList<Object> command = (NamedList<Object>) grouped.get(key);
    assertNotNull("command missing for " + key, command);
    return command;
  }
}
