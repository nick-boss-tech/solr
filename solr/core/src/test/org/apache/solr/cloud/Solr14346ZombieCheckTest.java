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
package org.apache.solr.cloud;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.impl.LBSolrClient;
import org.apache.solr.client.solrj.jetty.LBJettySolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.TimeSource;
import org.apache.solr.embedded.JettySolrRunner;
import org.apache.solr.util.TimeOut;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * An LB client over bare node URLs (no core on the endpoints, no default collection) must revive a
 * restarted node through its alive check. Before the fix the check threw NullPointerException
 * before sending any request (Objects.requireNonNullElse with a null core and a null default
 * collection), so the node stayed a zombie forever (SOLR-14346).
 */
public class Solr14346ZombieCheckTest extends SolrCloudTestCase {

  private static final String COLLECTION = "test14346";

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2).addConfig("conf", configset("cloud-minimal")).configure();
    CollectionAdminRequest.createCollection(COLLECTION, "conf", 1, 2)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection(COLLECTION, 1, 2);
  }

  @Test
  public void testCollectionlessZombieRevival() throws Exception {
    JettySolrRunner node1 = cluster.getJettySolrRunner(0);
    JettySolrRunner node2 = cluster.getJettySolrRunner(1);
    String url1 = node1.getBaseUrl().toString();
    String url2 = node2.getBaseUrl().toString();

    LBJettySolrClient.Builder lbBuilder =
        new LBJettySolrClient.Builder(
            node1.getSolrClient(),
            new LBSolrClient.Endpoint(url1),
            new LBSolrClient.Endpoint(url2));
    lbBuilder.setAliveCheckInterval(1, TimeUnit.SECONDS);
    try (LBJettySolrClient lb = lbBuilder.build()) {
      // Baseline: the LB client serves collection queries over bare node URLs.
      assertEquals(0, lb.query(COLLECTION, SolrParams.of("q", "*:*")).getStatus());

      // Kill node 2 and query until the LB client marks it a zombie.
      JettySolrRunner stopped = cluster.stopJettySolrRunner(1);
      TimeOut zombieTimeout = new TimeOut(30, TimeUnit.SECONDS, TimeSource.NANO_TIME);
      zombieTimeout.waitFor(
          "node 2 was never marked a zombie",
          () -> {
            try {
              lb.query(COLLECTION, SolrParams.of("q", "*:*"));
            } catch (Exception e) {
              // Queries still succeed via node 1; any failure here is unexpected but the
              // zombie check below is the assertion that matters.
            }
            try {
              return zombieBaseUrls(lb).contains(url2);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          });
      assertTrue(zombieBaseUrls(lb).contains(url2));

      // Restart node 2 on the same URL and confirm it answers direct queries again.
      cluster.startJettySolrRunner(stopped, true);
      TimeOut upTimeout = new TimeOut(30, TimeUnit.SECONDS, TimeSource.NANO_TIME);
      upTimeout.waitFor(
          "restarted node 2 never answered direct queries",
          () -> {
            try {
              QueryResponse rsp =
                  stopped.getSolrClient().query(COLLECTION, SolrParams.of("q", "*:*"));
              return rsp.getStatus() == 0;
            } catch (Exception e) {
              return false;
            }
          });

      // The alive check must now revive the node: it leaves the zombie map.
      TimeOut reviveTimeout = new TimeOut(30, TimeUnit.SECONDS, TimeSource.NANO_TIME);
      reviveTimeout.waitFor(
          "restarted node 2 stayed a zombie",
          () -> {
            try {
              return !zombieBaseUrls(lb).contains(url2);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          });
      assertFalse(zombieBaseUrls(lb).contains(url2));

      // And LB queries still succeed.
      assertEquals(0, lb.query(COLLECTION, SolrParams.of("q", "*:*")).getStatus());
    }
  }

  private static Set<String> zombieBaseUrls(LBSolrClient lb) throws Exception {
    Field field = LBSolrClient.class.getDeclaredField("zombieServers");
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, ?> zombies = (Map<String, ?>) field.get(lb);
    return new HashSet<>(zombies.keySet());
  }
}
