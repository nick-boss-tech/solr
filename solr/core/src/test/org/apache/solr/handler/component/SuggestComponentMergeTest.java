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

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.search.suggest.Lookup.LookupResult;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CharsRef;
import org.apache.solr.SolrTestCase;
import org.apache.solr.spelling.suggest.SuggesterResult;

/** Tests how {@link SuggestComponent} merges the suggestions returned by individual shards. */
public class SuggestComponentMergeTest extends SolrTestCase {

  private static final String DICT = "mySuggester";
  private static final String TOKEN = "test";

  private static SuggesterResult shardResult(Object... termsAndWeights) {
    List<LookupResult> results = new ArrayList<>();
    for (int i = 0; i < termsAndWeights.length; i += 2) {
      results.add(
          new LookupResult(
              new CharsRef((String) termsAndWeights[i]),
              (Long) termsAndWeights[i + 1],
              new BytesRef("")));
    }
    SuggesterResult result = new SuggesterResult();
    result.add(DICT, TOKEN, results);
    return result;
  }

  private static List<String> mergedTerms(List<SuggesterResult> shards, int count) {
    List<String> terms = new ArrayList<>();
    for (LookupResult res : SuggestComponent.merge(shards, count).getLookupResult(DICT, TOKEN)) {
      terms.add(res.key.toString());
    }
    return terms;
  }

  public void testHigherWeightComesFirst() {
    SuggesterResult shard1 = shardResult("test123", 3L, "testing", 1L);
    SuggesterResult shard2 = shardResult("test", 7L, "test1234", 2L);

    assertEquals(List.of("test", "test123", "test1234"), mergedTerms(List.of(shard1, shard2), 3));
  }

  public void testEqualWeightsDoNotDependOnShardResponseOrder() {
    // no weightField configured: every suggestion has the same weight on every shard
    SuggesterResult shard1 = shardResult("test123", 1L, "testing", 1L);
    SuggesterResult shard2 = shardResult("test", 1L, "test1234", 1L);
    SuggesterResult shard3 = shardResult("test_123", 1L);

    final List<String> expected = List.of("test", "test123", "test1234");
    assertEquals(expected, mergedTerms(List.of(shard1, shard2, shard3), 3));
    assertEquals(expected, mergedTerms(List.of(shard3, shard2, shard1), 3));
    assertEquals(expected, mergedTerms(List.of(shard2, shard3, shard1), 3));
  }
}
