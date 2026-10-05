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
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.params.ShardParams;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * End-to-end check that the order of equal-scoring documents from different shards does not depend
 * on which replicas served the request. Each shard has an NRT replica on one node and a PULL
 * replica on the other, so the two {@code shards.preference} values below produce opposite replica
 * URL orders in the shard address the merge used to tie-break on.
 */
public class TestShardTieBreakCluster extends SolrCloudTestCase {

  private static final String COLLECTION = "tiebreakCollection";
  private static final String CONFIG = "conf1";

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2)
        .addConfig(
            CONFIG, TEST_PATH().resolve("configsets").resolve("cloud-minimal").resolve("conf"))
        .configure();

    CloudSolrClient client = cluster.getSolrClient();
    CollectionAdminRequest.createCollection(COLLECTION, CONFIG, 2, 1).process(client);
    cluster.waitForActiveCollection(COLLECTION, 2, 2);
    ensureShardLeadersOnDifferentNodes(client);

    DocCollection coll = client.getClusterState().getCollection(COLLECTION);
    String nodeShard1 = onlyReplica(coll.getSlice("shard1"), Replica.Type.NRT).getNodeName();
    String nodeShard2 = onlyReplica(coll.getSlice("shard2"), Replica.Type.NRT).getNodeName();

    // index before adding the PULL replicas, so each PULL replica bootstraps the
    // documents from its shard leader when it becomes active
    String idShard1 = idForSlice(coll, "shard1");
    String idShard2 = idForSlice(coll, "shard2");
    client.add(COLLECTION, new SolrInputDocument("id", idShard1, "tiebreak_s", "same"));
    client.add(COLLECTION, new SolrInputDocument("id", idShard2, "tiebreak_s", "same"));
    client.commit(COLLECTION);

    // the counts of the other types must be zeroed explicitly: an ADDREPLICA whose
    // type is left at the default would otherwise also add a replica of that type
    CollectionAdminRequest.addReplicaToShard(COLLECTION, "shard1")
        .setNrtReplicas(0)
        .setTlogReplicas(0)
        .setPullReplicas(1)
        .setCreateNodeSet(nodeShard2)
        .process(client);
    CollectionAdminRequest.addReplicaToShard(COLLECTION, "shard2")
        .setNrtReplicas(0)
        .setTlogReplicas(0)
        .setPullReplicas(1)
        .setCreateNodeSet(nodeShard1)
        .process(client);
    cluster.waitForActiveCollection(COLLECTION, 2, 4);
  }

  private static Replica onlyReplica(Slice slice, Replica.Type type) {
    return slice.getReplicas().stream()
        .filter(r -> r.getType() == type)
        .findFirst()
        .orElseThrow(() -> new AssertionError("no " + type + " replica in " + slice.getName()));
  }

  private static void ensureShardLeadersOnDifferentNodes(CloudSolrClient client) throws Exception {
    DocCollection coll = client.getClusterState().getCollection(COLLECTION);
    Replica replica1 = onlyReplica(coll.getSlice("shard1"), Replica.Type.NRT);
    Replica replica2 = onlyReplica(coll.getSlice("shard2"), Replica.Type.NRT);
    if (!replica1.getNodeName().equals(replica2.getNodeName())) {
      return;
    }
    String otherNode =
        client.getClusterState().getLiveNodes().stream()
            .filter(node -> !node.equals(replica2.getNodeName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("no second live node"));
    CollectionAdminRequest.deleteReplica(COLLECTION, "shard2", replica2.getName()).process(client);
    CollectionAdminRequest.addReplicaToShard(COLLECTION, "shard2")
        .setNrtReplicas(1)
        .setTlogReplicas(0)
        .setPullReplicas(0)
        .setCreateNodeSet(otherNode)
        .process(client);
    cluster.waitForActiveCollection(COLLECTION, 2, 2);
  }

  /** An id whose route key hashes to the named slice of the test collection. */
  private static String idForSlice(DocCollection coll, String sliceName) {
    for (int i = 0; ; i++) {
      String id = "key" + i + "!doc";
      SolrInputDocument doc = new SolrInputDocument("id", id);
      if (coll.getRouter().getTargetSlice(id, doc, null, null, coll).getName().equals(sliceName)) {
        return id;
      }
    }
  }

  private static List<String> queryIdsInOrder(CloudSolrClient client, String preference)
      throws Exception {
    SolrQuery query = new SolrQuery("tiebreak_s:same");
    query.setRows(10);
    query.setFields("id", "score");
    query.set(ShardParams.SHARDS_PREFERENCE, preference);
    QueryResponse response = client.query(COLLECTION, query);
    assertEquals(2, response.getResults().getNumFound());
    List<String> ids = new ArrayList<>();
    Float score = null;
    for (SolrDocument doc : response.getResults()) {
      ids.add((String) doc.getFieldValue("id"));
      Float docScore = (Float) doc.getFieldValue("score");
      if (score == null) {
        score = docScore;
      } else {
        // without an exact tie this test would not be exercising the tie-break at all
        assertEquals("documents must score exactly the same", score, docScore);
      }
    }
    return ids;
  }

  @Test
  public void testTieBreakDoesNotDependOnReplicaPreference() throws Exception {
    CloudSolrClient client = cluster.getSolrClient();
    DocCollection coll = client.getClusterState().getCollection(COLLECTION);
    List<String> expected = List.of(idForSlice(coll, "shard1"), idForSlice(coll, "shard2"));

    List<String> nrtPreferred = queryIdsInOrder(client, "replica.type:NRT");
    List<String> pullPreferred = queryIdsInOrder(client, "replica.type:PULL");

    assertEquals(expected, nrtPreferred);
    assertEquals(nrtPreferred, pullPreferred);
  }
}
