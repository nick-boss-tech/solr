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

import java.util.List;
import org.apache.lucene.search.SortField;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

public class ShardFieldSortedHitQueueCycleTest extends SolrTestCase {

  @Test
  public void testDocIdLeadingSortHasNoCycle() {
    ShardFieldSortedHitQueue queue =
        new ShardFieldSortedHitQueue(
            new SortField[] {SortField.FIELD_DOC, new SortField("price", SortField.Type.LONG)},
            10,
            null);

    NamedList<List<Object>> shard1Values = new NamedList<>();
    shard1Values.add("price", List.of(5L, 1L));
    NamedList<List<Object>> shard2Values = new NamedList<>();
    shard2Values.add("price", List.of(3L));

    ShardDoc a1 = shardDoc("shard1", 0, shard1Values);
    ShardDoc a2 = shardDoc("shard1", 1, shard1Values);
    ShardDoc b1 = shardDoc("shard2", 0, shard2Values);

    assertFalse(isCycle(queue, a1, a2, b1) || isCycle(queue, a1, b1, a2));
  }

  @Test
  public void testCrossShardOrderIgnoresReplicaAddress() {
    ShardFieldSortedHitQueue queue =
        new ShardFieldSortedHitQueue(new SortField[] {SortField.FIELD_DOC}, 10, null);
    NamedList<List<Object>> values = new NamedList<>();
    ShardDoc fromShard1Replica1 =
        sliceDoc("10.0.0.1:8983/solr/c_shard1_replica_n1", "shard1", values);
    ShardDoc fromShard1Replica2 =
        sliceDoc("10.0.0.9:8983/solr/c_shard1_replica_n2", "shard1", values);
    ShardDoc fromShard2 = sliceDoc("10.0.0.2:8983/solr/c_shard2_replica_n3", "shard2", values);
    assertEquals(
        queue.lessThan(fromShard1Replica1, fromShard2),
        queue.lessThan(fromShard1Replica2, fromShard2));
    assertEquals(
        queue.lessThan(fromShard2, fromShard1Replica1),
        queue.lessThan(fromShard2, fromShard1Replica2));
  }

  private static ShardDoc sliceDoc(String shard, String sliceName, NamedList<List<Object>> values) {
    ShardDoc doc = new ShardDoc();
    doc.shard = shard;
    doc.sliceName = sliceName;
    doc.sortFieldValues = values;
    return doc;
  }

  private static boolean isCycle(
      ShardFieldSortedHitQueue queue, ShardDoc x, ShardDoc y, ShardDoc z) {
    return queue.lessThan(x, y) && queue.lessThan(y, z) && queue.lessThan(z, x);
  }

  private static ShardDoc shardDoc(String shard, int orderInShard, NamedList<List<Object>> values) {
    ShardDoc doc = new ShardDoc();
    doc.shard = shard;
    doc.sliceName = shard;
    doc.orderInShard = orderInShard;
    doc.sortFieldValues = values;
    return doc;
  }
}
