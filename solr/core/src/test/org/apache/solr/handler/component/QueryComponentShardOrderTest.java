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
import org.apache.lucene.search.SortField;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.search.SortSpec;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class QueryComponentShardOrderTest extends SolrTestCaseJ4 {

  @BeforeClass
  public static void setup() {
    assumeWorkingMockito();
  }

  @Test
  public void groupOrderAcrossShardsDoesNotDependOnReplicaListOrder() {
    SortSpec sortSpec =
        MockSortSpecBuilder.create()
            .withSortFields(new SortField[] {SortField.FIELD_DOC})
            .withIncludesNonScoreOrDocSortField(false)
            .build();
    String[] slices = {"shard1", "shard2"};
    String shard1Replica1 = "10.0.0.1:8983/solr/c_shard1_replica_n1";
    String shard1Replica2 = "10.0.0.3:8983/solr/c_shard1_replica_n2";
    String shard2Replica = "10.0.0.2:8983/solr/c_shard2_replica_n3";

    List<String> replicaListOrderOne =
        mergedIds(
            sortSpec,
            slices,
            new String[] {shard1Replica1 + "|" + shard1Replica2, shard2Replica},
            new String[] {shard1Replica1, shard2Replica});
    List<String> replicaListOrderTwo =
        mergedIds(
            sortSpec,
            slices,
            new String[] {shard1Replica2 + "|" + shard1Replica1, shard2Replica},
            new String[] {shard1Replica2, shard2Replica});

    assertEquals(replicaListOrderOne, replicaListOrderTwo);
    assertEquals(4, replicaListOrderOne.size());
  }

  private static List<String> mergedIds(
      SortSpec sortSpec, String[] slices, String[] replicaLists, String[] answeringReplicas) {
    MockResponseBuilder rb = MockResponseBuilder.create().withSortSpec(sortSpec);
    rb.slices = slices;
    rb.shards = replicaLists;

    ShardRequest sreq = new ShardRequest();
    sreq.actualShards = replicaLists;
    sreq.responses = new ArrayList<>();
    sreq.responses.add(shardResponse(replicaLists[0], answeringReplicas[0], 1, 2));
    sreq.responses.add(shardResponse(replicaLists[1], answeringReplicas[1], 3, 4));

    new QueryComponent().mergeIds(rb, sreq);

    ShardDoc[] ordered = new ShardDoc[rb.resultIds.size()];
    for (ShardDoc shardDoc : rb.resultIds.values()) {
      ordered[shardDoc.positionInResponse] = shardDoc;
    }
    List<String> ids = new ArrayList<>();
    for (ShardDoc shardDoc : ordered) {
      ids.add(String.valueOf(shardDoc.id));
    }
    return ids;
  }

  private static ShardResponse shardResponse(
      String replicaList, String answeringReplica, int firstId, int lastId) {
    SolrDocumentList docs = new SolrDocumentList();
    for (int id = firstId; id <= lastId; id++) {
      SolrDocument doc = new SolrDocument();
      doc.addField("id", id);
      doc.addField("score", (float) id);
      docs.add(doc);
    }
    NamedList<Object> response = new NamedList<>();
    response.add("response", docs);
    response.add("responseHeader", new NamedList<>());
    SolrResponse solrResponse = Mockito.mock(SolrResponse.class);
    Mockito.when(solrResponse.getResponse()).thenReturn(response);

    ShardResponse shardResponse = new ShardResponse();
    shardResponse.setSolrResponse(solrResponse);
    shardResponse.setShard(replicaList);
    shardResponse.setShardAddress(answeringReplica);
    return shardResponse;
  }
}
