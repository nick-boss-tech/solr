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

import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.core.SolrCore;
import org.apache.solr.embedded.JettySolrRunner;
import org.junit.BeforeClass;
import org.junit.Test;

/** Tests the error handling of {@link ZkController#getLeader}. */
public class ZkControllerGetLeaderTest extends SolrCloudTestCase {

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1)
        .addConfig(
            "config", TEST_PATH().resolve("configsets").resolve("cloud-minimal").resolve("conf"))
        .configure();
  }

  @Test
  public void testInterruptIsNotSwallowed() throws Exception {
    String collectionName = "getLeaderInterrupted";
    CollectionAdminRequest.createCollection(collectionName, "config", 1, 1)
        .process(cluster.getSolrClient());
    waitForState("Expected a single replica collection", collectionName, clusterShape(1, 1));

    Replica leader = cluster.getZkStateReader().getLeaderRetry(collectionName, "shard1");
    JettySolrRunner jetty = cluster.getReplicaJetty(leader);
    ZkController zkController = jetty.getCoreContainer().getZkController();

    try (SolrCore core = jetty.getCoreContainer().getCore(leader.getCoreName())) {
      CloudDescriptor cloudDescriptor = core.getCoreDescriptor().getCloudDescriptor();

      Thread.currentThread().interrupt();
      try {
        SolrException e =
            expectThrows(SolrException.class, () -> zkController.getLeader(cloudDescriptor, 10000));
        assertTrue(e.getMessage(), e.getMessage().contains("Interrupted"));
        assertTrue(
            "the interrupt status must be restored for the caller",
            Thread.currentThread().isInterrupted());
      } finally {
        Thread.interrupted(); // clear, so that the test framework is not disturbed
      }
    }
  }
}
