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
package org.apache.solr.search.stats;

import org.apache.solr.SolrTestCase;
import org.apache.solr.common.params.ShardParams;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

/**
 * Pins the per-shard key derivation of {@link ExactStatsCache} for shard responses that carry no
 * collection name. A shard running an older version (during a rolling upgrade) reports only its
 * shard name; when two collections have same-named shards, those responses must still key apart, or
 * the coordinator merges the statistics of different collections into one entry.
 */
public class TestExactStatsCacheLegacyResponse extends SolrTestCase {

  private static final String URL_COLLECTION1 =
      "http://host1:8983/solr/collection1_shard1_replica_n1";
  private static final String URL_COLLECTION2 =
      "http://host2:8983/solr/collection2_shard1_replica_n1";

  private static NamedList<Object> shardResponse(String shardName, String collection) {
    NamedList<Object> nl = new NamedList<>();
    if (shardName != null) {
      nl.add(ShardParams.SHARD_NAME, shardName);
    }
    if (collection != null) {
      nl.add(ExactStatsCache.SHARD_COLLECTION_KEY, collection);
    }
    return nl;
  }

  @Test
  public void testLegacyResponsesWithSameShardNameKeyApart() {
    String key1 = ExactStatsCache.perShardKey(shardResponse("shard1", null), URL_COLLECTION1);
    String key2 = ExactStatsCache.perShardKey(shardResponse("shard1", null), URL_COLLECTION2);
    assertNotEquals(
        "legacy shards of different collections must not share a stats key", key1, key2);
  }

  @Test
  public void testCurrentResponsesKeyByCollectionAndShard() {
    assertEquals(
        "collection1!shard1",
        ExactStatsCache.perShardKey(shardResponse("shard1", "collection1"), URL_COLLECTION1));
    assertEquals(
        "collection2!shard1",
        ExactStatsCache.perShardKey(shardResponse("shard1", "collection2"), URL_COLLECTION2));
  }

  @Test
  public void testResponseWithoutShardNameKeysByUrl() {
    assertEquals(
        URL_COLLECTION1, ExactStatsCache.perShardKey(shardResponse(null, null), URL_COLLECTION1));
  }
}
