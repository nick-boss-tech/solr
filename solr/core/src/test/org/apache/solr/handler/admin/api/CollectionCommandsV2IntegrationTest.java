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

package org.apache.solr.handler.admin.api;

import java.util.Map;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.V2Request;
import org.apache.solr.client.solrj.response.V2Response;
import org.apache.solr.client.solrj.response.json.JsonMapResponseParser;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.RoutingRule;
import org.apache.solr.common.cloud.Slice;
import org.junit.BeforeClass;
import org.junit.Test;

/** Drives the JAX-RS collection command APIs over HTTP against a running SolrCloud cluster. */
public class CollectionCommandsV2IntegrationTest extends SolrCloudTestCase {

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2).addConfig("conf", configset("cloud-minimal")).configure();
  }

  @Test
  public void testSplitShard() throws Exception {
    final String collection = "v2SplitShard";
    CollectionAdminRequest.createCollection(collection, "conf", 1, 1)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection(collection, 1, 1);

    postV2("/collections/" + collection + "/shards/shard1/split", "{\"numSubShards\": 2}");

    waitForState(
        "Expected sub-shards shard1_0 and shard1_1 to be active",
        collection,
        c -> c != null && isActive(c.getSlice("shard1_0")) && isActive(c.getSlice("shard1_1")));
  }

  @Test
  public void testMoveReplica() throws Exception {
    final String collection = "v2MoveReplica";
    CollectionAdminRequest.createCollection(collection, "conf", 1, 1)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection(collection, 1, 1);

    final Replica replica = getCollectionState(collection).getSlice("shard1").iterator().next();
    final String sourceNode = replica.getNodeName();
    final String targetNode =
        cluster.getJettySolrRunners().stream()
            .map(jetty -> jetty.getNodeName())
            .filter(node -> !node.equals(sourceNode))
            .findFirst()
            .orElseThrow();

    postV2(
        "/collections/" + collection + "/shards/shard1/replicas/" + replica.getName() + "/move",
        "{\"targetNode\": \"" + targetNode + "\", \"inPlaceMove\": false}");

    waitForState(
        "Expected the only shard1 replica to be active on " + targetNode,
        collection,
        c ->
            c != null
                && c.getSlice("shard1").getReplicas().stream()
                    .allMatch(
                        r ->
                            r.getNodeName().equals(targetNode)
                                && r.getState() == Replica.State.ACTIVE));
  }

  @Test
  public void testRebalanceLeaders() throws Exception {
    final String collection = "v2RebalanceLeaders";
    CollectionAdminRequest.createCollection(collection, "conf", 2, 2)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection(collection, 2, 4);

    postV2(
        "/collections/" + collection + "/rebalance-leaders",
        "{\"maxAtOnce\": 2, \"maxWaitSeconds\": 30}");
  }

  @Test
  public void testMigrateDocs() throws Exception {
    final String source = "v2MigrateSource";
    final String target = "v2MigrateTarget";
    CollectionAdminRequest.createCollection(source, "conf", 1, 1).process(cluster.getSolrClient());
    CollectionAdminRequest.createCollection(target, "conf", 1, 1).process(cluster.getSolrClient());
    cluster.getSolrClient().add(source, new SolrInputDocument("id", "a!1"));
    cluster.getSolrClient().commit(source);

    postV2(
        "/collections/" + source + "/migrate",
        "{\"target\": \"" + target + "\", \"splitKey\": \"a!\", \"forwardTimeout\": 45}");

    waitForState(
        "Expected a routing rule for split key a!",
        source,
        c -> {
          if (c == null) {
            return false;
          }
          Map<String, RoutingRule> rules = c.getSlice("shard1").getRoutingRules();
          return rules != null && rules.containsKey("a!");
        });
  }

  private static void postV2(String path, String payload) throws Exception {
    V2Request request =
        new V2Request.Builder(path)
            .withMethod(SolrRequest.METHOD.POST)
            .withPayload(payload)
            .build();
    request.setResponseParser(new JsonMapResponseParser());
    V2Response response = request.process(cluster.getSolrClient());
    assertEquals(0, response.getStatus());
  }

  private static boolean isActive(Slice slice) {
    return slice != null && slice.getState() == Slice.State.ACTIVE;
  }
}
