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

import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.jetty.HttpJettySolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ShardParams;
import org.apache.solr.common.cloud.Replica;
import org.junit.BeforeClass;
import org.junit.Test;

public class DuplicateUniqueKeyNullSlotTest extends SolrCloudTestCase {

  private static final String COLLECTION = "dupkey";

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2).addConfig("conf", configset("cloud-minimal")).configure();

    CollectionAdminRequest.createCollectionWithImplicitRouter(COLLECTION, "conf", "shard1,shard2", 1)
        .processAndWait(cluster.getSolrClient(), DEFAULT_TIMEOUT);
    cluster
        .getZkStateReader()
        .waitForState(
            COLLECTION,
            DEFAULT_TIMEOUT,
            TimeUnit.SECONDS,
            (n, c) -> SolrCloudTestCase.replicasForCollectionAreFullyActive(n, c, 2, 1));

    for (int i = 0; i < 5; i++) {
      addRouted(i % 2 == 0 ? "shard1" : "shard2", sdoc("id", "u" + i));
    }
    addRouted("shard1", sdoc("id", "dup"));
    addRouted("shard2", sdoc("id", "dup"));
    cluster.getSolrClient().commit(COLLECTION);
  }

  @Test
  public void testDuplicateUniqueKeyLeavesNoNullSlot() throws Exception {
    for (String shard : new String[] {"shard1", "shard2"}) {
      System.out.println("LOCAL " + shard + " " + localIds(shard));
    }
    QueryResponse rsp =
        cluster.getSolrClient().query(COLLECTION, new SolrQuery("q", "*:*", "fl", "id", "rows", "20"));
    StringBuilder seen = new StringBuilder();
    int nulls = 0;
    for (SolrDocument doc : rsp.getResults()) {
      if (doc == null) {
        nulls++;
      } else {
        seen.append(doc.getFieldValue("id")).append(' ');
      }
    }
    System.out.println(
        "CLOUD numFound="
            + rsp.getResults().getNumFound()
            + " size="
            + rsp.getResults().size()
            + " nulls="
            + nulls
            + " ids="
            + seen);
    assertEquals("null slots in response", 0, nulls);
  }

  private static java.util.List<String> localIds(String shard) throws Exception {
    Replica leader =
        cluster.getZkStateReader().getClusterState().getCollection(COLLECTION).getSlice(shard).getLeader();
    java.util.List<String> ids = new java.util.ArrayList<>();
    try (SolrClient c = new HttpJettySolrClient.Builder(leader.getBaseUrl()).build()) {
      QueryResponse rsp =
          c.query(
              leader.getCoreName(),
              new SolrQuery("q", "*:*", "fl", "id", "rows", "20", "distrib", "false"));
      for (SolrDocument d : rsp.getResults()) {
        ids.add((String) d.getFieldValue("id"));
      }
    }
    return ids;
  }

  private static void addRouted(String shard, SolrInputDocument doc) throws Exception {
    UpdateRequest req = new UpdateRequest().add(doc);
    req.setParam(ShardParams._ROUTE_, shard);
    req.process(cluster.getSolrClient(), COLLECTION);
  }
}
