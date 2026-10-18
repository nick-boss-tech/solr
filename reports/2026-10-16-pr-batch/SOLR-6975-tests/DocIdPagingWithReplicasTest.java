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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.jetty.HttpJettySolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.junit.BeforeClass;
import org.junit.Test;

public class DocIdPagingWithReplicasTest extends SolrCloudTestCase {

  private static final String COLLECTION = "docidpaging";
  private static final int DOCS = 40;
  private static final int PAGE = 7;
  private static final int ROUNDS = 20;

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(4).addConfig("conf", configset("cloud-minimal")).configure();

    CollectionAdminRequest.createCollection(COLLECTION, "conf", 2, 2)
        .processAndWait(cluster.getSolrClient(), DEFAULT_TIMEOUT);
    cluster
        .getZkStateReader()
        .waitForState(
            COLLECTION,
            DEFAULT_TIMEOUT,
            TimeUnit.SECONDS,
            (n, c) -> SolrCloudTestCase.replicasForCollectionAreFullyActive(n, c, 2, 2));

    UpdateRequest req = new UpdateRequest();
    for (int i = 0; i < DOCS; i++) {
      req.add(sdoc("id", String.valueOf(i)));
    }
    req.commit(cluster.getSolrClient(), COLLECTION);
  }

  @Test
  public void testDocIdPagingAcrossReplicas() throws Exception {
    DocCollection coll = cluster.getZkStateReader().getClusterState().getCollection(COLLECTION);
    for (Slice s : coll.getSlices()) {
      for (Replica r : s.getReplicas()) {
        System.out.println("REPLICA " + s.getName() + " " + r.getName() + " " + localOrder(r));
      }
    }

    System.out.println("BASELINE");
    pageAll();

    Replica divergent = nonLeaderOf("shard1");
    reorderLocalDocs(divergent);
    System.out.println("REORDERED " + divergent.getName() + " " + localOrder(divergent));

    System.out.println("DIVERGENT " + divergent.getName());
    int badRounds = 0;
    for (int round = 0; round < ROUNDS; round++) {
      List<String> seen = pageAll();
      Set<String> distinct = new HashSet<>(seen);
      if (seen.size() != DOCS || distinct.size() != DOCS) {
        badRounds++;
      }
    }
    assertEquals("rounds with a repeat or gap", 0, badRounds);
  }

  private static List<String> pageAll() throws Exception {
    List<String> ids = new ArrayList<>();
    for (int start = 0; start < DOCS; start += PAGE) {
      SolrQuery q = new SolrQuery("q", "*:*", "fl", "id", "sort", "_docid_ asc");
      q.setStart(start);
      q.setRows(PAGE);
      QueryResponse rsp = cluster.getSolrClient().query(COLLECTION, q);
      List<String> page = new ArrayList<>();
      for (SolrDocument d : rsp.getResults()) {
        page.add((String) d.getFieldValue("id"));
      }
      System.out.println(
          "page start=" + start + " numFound=" + rsp.getResults().getNumFound() + " " + page);
      ids.addAll(page);
    }
    return ids;
  }

  private static Replica nonLeaderOf(String shardName) {
    DocCollection coll = cluster.getZkStateReader().getClusterState().getCollection(COLLECTION);
    Slice slice = coll.getSlice(shardName);
    for (Replica r : slice.getReplicas()) {
      if (!r.equals(slice.getLeader())) {
        return r;
      }
    }
    throw new AssertionError("no non-leader replica in " + shardName);
  }

  private static List<String> localOrder(Replica replica) throws Exception {
    try (SolrClient c = new HttpJettySolrClient.Builder(replica.getBaseUrl()).build()) {
      QueryResponse local =
          c.query(
              replica.getCoreName(),
              new SolrQuery(
                  "q", "*:*", "fl", "id", "sort", "_docid_ asc", "rows", String.valueOf(DOCS),
                  "distrib", "false"));
      List<String> ids = new ArrayList<>();
      for (SolrDocument d : local.getResults()) {
        ids.add((String) d.getFieldValue("id"));
      }
      return ids;
    }
  }

  private static void reorderLocalDocs(Replica replica) throws Exception {
    List<String> ids = localOrder(replica);
    try (SolrClient c = new HttpJettySolrClient.Builder(replica.getBaseUrl()).build()) {
      for (int i = ids.size() - 1; i >= 0; i--) {
        UpdateRequest del = new UpdateRequest().deleteById(ids.get(i));
        del.setParam("distrib", "false");
        c.request(del, replica.getCoreName());

        UpdateRequest add = new UpdateRequest().add(sdoc("id", ids.get(i)));
        add.setParam("distrib", "false");
        c.request(add, replica.getCoreName());
      }
      c.commit(replica.getCoreName());
    }
  }
}
