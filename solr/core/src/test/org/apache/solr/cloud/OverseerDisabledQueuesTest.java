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

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.ConfigSetAdminRequest;
import org.apache.solr.core.NodeRoles;
import org.apache.solr.embedded.JettySolrRunner;
import org.junit.Test;

/** Checks the legacy task state separately from the election and QUIT control queue. */
public class OverseerDisabledQueuesTest extends SolrCloudTestCase {
  private static final List<String> TASK_PATHS =
      List.of(
          "/overseer/collection-queue-work",
          "/overseer/collection-map-running",
          "/overseer/collection-map-completed",
          "/overseer/collection-map-failure",
          "/overseer/async_ids");

  @Test
  public void testTaskPathsFollowOverseerMode() throws Exception {
    for (boolean enabled : List.of(false, true)) {
      configureCluster(1)
          .withOverseer(enabled)
          .addConfig("conf", configset("cloud-minimal"))
          .configure();
      try {
        assertTaskPaths(enabled);
        CollectionAdminRequest.createCollection("probe", "conf", 1, 1)
            .process(cluster.getSolrClient());
        cluster.waitForActiveCollection("probe", 1, 1);
        new ConfigSetAdminRequest.Create()
            .setConfigSetName("copy")
            .setBaseConfigSetName("conf")
            .process(cluster.getSolrClient());
        new ConfigSetAdminRequest.Delete()
            .setConfigSetName("copy")
            .process(cluster.getSolrClient());
        CollectionAdminRequest.deleteCollection("probe").process(cluster.getSolrClient());
        assertTaskPaths(enabled);

        if (!enabled) {
          ZkController controller = cluster.getJettySolrRunner(0).getCoreContainer().getZkController();
          controller.setPreferredOverseer();
          String oldId = OverseerTaskProcessor.getLeaderId(cluster.getZkClient());
          assertNotNull(oldId);
          controller.getOverseer().sendQuitToOverseer(oldId);
          assertBusy(
              () -> {
                String newId = OverseerTaskProcessor.getLeaderId(cluster.getZkClient());
                assertNotNull(newId);
                assertNotEquals("QUIT must trigger a new election tenure", oldId, newId);
              },
              30,
              TimeUnit.SECONDS);
          assertTaskPaths(false);
        }

        JettySolrRunner jetty = cluster.stopJettySolrRunner(0);
        cluster.startJettySolrRunner(jetty);
        cluster.waitForAllNodes(30);
        assertTaskPaths(enabled);
      } finally {
        cluster.shutdown();
        cluster = null;
      }
    }
  }

  private void assertTaskPaths(boolean enabled) throws Exception {
    assertBusy(
        () -> {
          assertNotNull(OverseerTaskProcessor.getLeaderId(cluster.getZkClient()));
          assertTrue(
              "QUIT control queue is still required",
              cluster.getZkClient().exists("/overseer/queue"));
        },
        30,
        TimeUnit.SECONDS);
    for (String path : TASK_PATHS) {
      assertEquals(
          "Unexpected task path in mode overseerEnabled=" + enabled + ": " + path,
          enabled,
          cluster.getZkClient().exists(path));
    }
  }

  @Test
  public void testPreferredOverseerWithoutTaskQueue() throws Exception {
    configureCluster(1).withOverseer(false).configure();
    try {
      assertTaskPaths(false);
      System.setProperty(NodeRoles.NODE_ROLES_PROP, "data:on,overseer:preferred");
      JettySolrRunner preferred = cluster.startJettySolrRunner();
      assertEquals(
          NodeRoles.MODE_PREFERRED,
          preferred.getCoreContainer().nodeRoles.getRoleMode(NodeRoles.Role.OVERSEER));
      assertBusy(
          () ->
              assertEquals(
                  preferred.getNodeName(),
                  OverseerTaskProcessor.getLeaderNode(cluster.getZkClient())),
          30,
          TimeUnit.SECONDS);
      assertTaskPaths(false);
    } finally {
      cluster.shutdown();
      cluster = null;
    }
  }
}
