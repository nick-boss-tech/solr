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
import java.util.Collections;
import java.util.List;
import org.apache.lucene.search.SortField;
import org.apache.solr.SolrTestCase;
import org.junit.Test;

/** Tie-breaking of equal-scoring documents when merging distributed results. */
public class TestShardTieBreak extends SolrTestCase {

  private static ShardDoc doc(String id, String shardUrl, String shardName) {
    ShardDoc shardDoc = new ShardDoc(1.0f, null, id, shardUrl);
    shardDoc.shardName = shardName;
    shardDoc.orderInShard = 0;
    return shardDoc;
  }

  /** The ids in the order the merged response would list them, best first. */
  private static List<String> rankedIds(ShardDoc... docs) {
    ShardFieldSortedHitQueue queue =
        new ShardFieldSortedHitQueue(new SortField[] {SortField.FIELD_SCORE}, docs.length, null);
    for (ShardDoc shardDoc : docs) {
      queue.add(shardDoc);
    }
    List<String> ids = new ArrayList<>();
    while (queue.size() > 0) {
      ids.add((String) queue.pop().id);
    }
    Collections.reverse(ids);
    return ids;
  }

  @Test
  public void testTieBreakIgnoresReplicaChoice() {
    // same query, same shards, served by different replicas
    List<String> first =
        rankedIds(
            doc("a", "http://node1:8983/solr/coll_shard1_replica_n1", "shard1"),
            doc("b", "http://node2:8983/solr/coll_shard2_replica_n2", "shard2"));
    List<String> second =
        rankedIds(
            doc("a", "http://node2:8983/solr/coll_shard1_replica_n2", "shard1"),
            doc("b", "http://node1:8983/solr/coll_shard2_replica_n1", "shard2"));

    assertEquals(List.of("a", "b"), first);
    assertEquals(first, second);
  }

  @Test
  public void testTieBreakFallsBackToShardAddress() {
    List<String> ranked =
        rankedIds(
            doc("b", "http://host2/solr/core", null), doc("a", "http://host1/solr/core", null));

    assertEquals(List.of("a", "b"), ranked);
  }
}
