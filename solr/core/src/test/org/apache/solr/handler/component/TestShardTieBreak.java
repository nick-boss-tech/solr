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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.search.SortField;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.DocRouter;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.util.URLUtil;
import org.junit.Test;

/** Tie-breaking of equal-scoring documents when merging distributed results. */
public class TestShardTieBreak extends SolrTestCase {

  private static final String NODE_1 = "127.0.0.1:10001_solr";
  private static final String NODE_2 = "127.0.0.1:10002_solr";

  private static String coreUrl(String nodeName, String coreName) {
    return URLUtil.getBaseUrlForNodeName(nodeName, "http") + "/" + coreName;
  }

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

  private static DocCollection twoShardsTwoReplicas() {
    Map<String, Slice> slices = new HashMap<>();
    for (int shard = 1; shard <= 2; shard++) {
      String shardName = "shard" + shard;
      Map<String, Replica> replicas = new HashMap<>();
      for (int replica = 1; replica <= 2; replica++) {
        String nodeName = replica == 1 ? NODE_1 : NODE_2;
        String coreName = "coll_" + shardName + "_replica_n" + replica;
        Map<String, Object> props = new HashMap<>();
        props.put(ZkStateReader.NODE_NAME_PROP, nodeName);
        props.put(ZkStateReader.BASE_URL_PROP, URLUtil.getBaseUrlForNodeName(nodeName, "http"));
        props.put(ZkStateReader.CORE_NAME_PROP, coreName);
        String replicaName = "core_node" + shard + replica;
        replicas.put(replicaName, new Replica(replicaName, props, "coll", shardName));
      }
      slices.put(shardName, new Slice(shardName, replicas, null, "coll"));
    }
    return DocCollection.create(
        "coll", slices, new HashMap<>(), DocRouter.DEFAULT, Integer.MAX_VALUE, Instant.EPOCH, null);
  }

  @Test
  public void testTieBreakIgnoresReplicaChoice() {
    // same query, same shards, served by different replicas
    List<String> first =
        rankedIds(
            doc("a", coreUrl(NODE_1, "coll_shard1_replica_n1"), "shard1"),
            doc("b", coreUrl(NODE_2, "coll_shard2_replica_n2"), "shard2"));
    List<String> second =
        rankedIds(
            doc("a", coreUrl(NODE_2, "coll_shard1_replica_n2"), "shard1"),
            doc("b", coreUrl(NODE_1, "coll_shard2_replica_n1"), "shard2"));

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

  @Test
  public void testResolveShardNameFromReplicaUrl() {
    DocCollection collection = twoShardsTwoReplicas();
    Map<String, String> cache = new HashMap<>();

    assertEquals(
        "shard1",
        QueryComponent.resolveShardName(
            collection, cache, coreUrl(NODE_2, "coll_shard1_replica_n2")));
    assertEquals(
        "shard2",
        QueryComponent.resolveShardName(
            collection, cache, coreUrl(NODE_1, "coll_shard2_replica_n1")));
  }

  @Test
  public void testResolveShardNameFromJoinedReplicaUrls() {
    DocCollection collection = twoShardsTwoReplicas();
    Map<String, String> cache = new HashMap<>();
    String joined =
        coreUrl(NODE_1, "coll_shard2_replica_n1") + "|" + coreUrl(NODE_2, "coll_shard2_replica_n2");

    assertEquals("shard2", QueryComponent.resolveShardName(collection, cache, joined));
    assertEquals("shard2", QueryComponent.resolveShardName(collection, cache, joined));
  }

  @Test
  public void testResolveShardNameUnknownOrMissing() {
    DocCollection collection = twoShardsTwoReplicas();
    Map<String, String> cache = new HashMap<>();

    assertNull(
        QueryComponent.resolveShardName(
            collection, cache, coreUrl(NODE_1, "other_shard1_replica_n1")));
    assertNull(QueryComponent.resolveShardName(collection, cache, null));
    assertNull(
        QueryComponent.resolveShardName(
            null, cache, coreUrl(NODE_1, "coll_shard1_replica_n1")));
  }
}
