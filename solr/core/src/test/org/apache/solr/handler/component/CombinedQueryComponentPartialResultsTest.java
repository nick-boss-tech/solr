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
package org.apache.solr.handler.component;

import java.lang.reflect.Field;
import java.util.List;
import org.apache.lucene.search.SortField;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CombinerParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.component.combine.ReciprocalRankFusion;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.search.SortSpec;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/** Lightweight regression coverage for CombinedQueryComponent partial-result propagation. */
public class CombinedQueryComponentPartialResultsTest extends SolrTestCaseJ4 {
  private static final String SORT_FIELD_NAME = "category";
  private static final int shard1Size = 2;
  private static final int shard2Size = 3;

  private static int id = 0;
  private static ShardRequest shardRequestWithPartialResults;

  @BeforeClass
  public static void setup() {
    assumeWorkingMockito();
    id = 0;
    shardRequestWithPartialResults = createShardRequestWithPartialResults();
  }

  @Test
  public void includesPartialShardResultWhenUsingExplicitScoreSort() {
    SortSpec sortSpec =
        MockSortSpecBuilder.create()
            .withSortFields(new SortField[] {SortField.FIELD_SCORE})
            .withIncludesNonScoreOrDocSortField(false)
            .build();

    MockResponseBuilder responseBuilder = MockResponseBuilder.create().withSortSpec(sortSpec);
    Mockito.when(responseBuilder.req.getParams().getParams(CombinerParams.COMBINER_QUERY))
        .thenReturn(new String[] {"lexical1"});
    Mockito.when(
            responseBuilder
                .req
                .getParams()
                .get(CombinerParams.COMBINER_ALGORITHM, CombinerParams.DEFAULT_COMBINER))
        .thenReturn(CombinerParams.RECIPROCAL_RANK_FUSION);

    CombinedQueryComponent combinedQueryComponent = createCombinedQueryComponent();
    combinedQueryComponent.mergeIds(responseBuilder, shardRequestWithPartialResults);

    assertEquals(
        Boolean.TRUE,
        responseBuilder
            .rsp
            .getResponseHeader()
            .getBooleanArg(SolrQueryResponse.RESPONSE_HEADER_PARTIAL_RESULTS_KEY));
  }

  private static CombinedQueryComponent createCombinedQueryComponent() {
    CombinedQueryComponent combinedQueryComponent = new CombinedQueryComponent();
    combinedQueryComponent.init(new NamedList<>());
    ReciprocalRankFusion reciprocalRankFusion = new ReciprocalRankFusion();
    reciprocalRankFusion.init(new NamedList<>());
    try {
      Field combinersField = CombinedQueryComponent.class.getDeclaredField("combiners");
      combinersField.setAccessible(true);
      @SuppressWarnings("unchecked")
      java.util.Map<String, org.apache.solr.handler.component.combine.QueryAndResponseCombiner>
          combiners =
              (java.util.Map<
                      String, org.apache.solr.handler.component.combine.QueryAndResponseCombiner>)
                  combinersField.get(combinedQueryComponent);
      combiners.put(CombinerParams.RECIPROCAL_RANK_FUSION, reciprocalRankFusion);
      return combinedQueryComponent;
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException("Unable to initialize combined query test harness", e);
    }
  }

  private static ShardRequest createShardRequestWithPartialResults() {
    final NamedList<Object> shard1ResponseHeader = new NamedList<>();
    final NamedList<Object> shard2ResponseHeader = new NamedList<>();
    SolrDocumentList shard1Docs = createSolrDocumentList(shard1Size);
    SolrDocumentList shard2Docs = createSolrDocumentList(shard2Size);

    shard1ResponseHeader.add(SolrQueryResponse.RESPONSE_HEADER_PARTIAL_RESULTS_KEY, Boolean.TRUE);

    return MockShardRequest.create()
        .withShardResponse(shard1ResponseHeader, shard1Docs, List.of(shard1Docs))
        .withShardResponse(shard2ResponseHeader, shard2Docs, List.of(shard2Docs));
  }

  private static SolrDocumentList createSolrDocumentList(int size) {
    SolrDocumentList solrDocuments = new SolrDocumentList();
    for (int i = 0; i < size; i++) {
      SolrDocument solrDocument = new SolrDocument();
      solrDocument.addField("id", id++);
      solrDocument.addField("score", (float) id);
      solrDocument.addField(SORT_FIELD_NAME, id);
      solrDocuments.add(solrDocument);
    }
    return solrDocuments;
  }
}
