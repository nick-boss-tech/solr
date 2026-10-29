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

import org.apache.solr.client.solrj.RemoteSolrException;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.request.V2Request;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Checks that the JAX-RS core command APIs are served over HTTP. Each request targets a core or
 * node that does not exist, so the handler fails inside the API; a route that is not registered
 * fails earlier with "Cannot find API for the path".
 */
public class CoreCommandsV2RoutesIntegrationTest extends SolrCloudTestCase {

  private static final String MISSING_ROUTE = "Cannot find API for the path";

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1).addConfig("conf", configset("cloud-minimal")).configure();
  }

  @Test
  public void testSplitCoreIsRouted() throws Exception {
    assertRouted("/cores/missingCore/split", "{\"path\": [\"/tmp/missing\"]}");
  }

  @Test
  public void testPrepareCoreRecoveryIsRouted() throws Exception {
    assertRouted("/cores/missingCore/prep-recovery", "{\"coreNodeName\": \"core_node1\"}");
  }

  @Test
  public void testRequestCoreRecoveryIsRouted() throws Exception {
    assertRouted("/cores/missingCore/request-recovery", "{}");
  }

  @Test
  public void testRequestSyncShardIsRouted() throws Exception {
    assertRouted("/cores/missingCore/request-sync-shard", "{}");
  }

  @Test
  public void testRequestBufferUpdatesIsRouted() throws Exception {
    assertRouted("/cores/missingCore/request-buffer-updates", "{}");
  }

  @Test
  public void testRequestApplyCoreUpdatesIsRouted() throws Exception {
    assertRouted("/cores/missingCore/request-apply-updates", "{}");
  }

  @Test
  public void testOverseerOperationIsRouted() throws Exception {
    assertRouted("/node/overseer-op", "{}");
  }

  @Test
  public void testRejoinLeaderElectionIsRouted() throws Exception {
    assertRouted("/node/rejoin-leader-election", "{}");
  }

  private static void assertRouted(String path, String payload) throws Exception {
    V2Request request =
        new V2Request.Builder(path)
            .withMethod(SolrRequest.METHOD.POST)
            .withPayload(payload)
            .build();
    String error = null;
    try {
      request.process(cluster.getSolrClient());
    } catch (RemoteSolrException e) {
      error = e.getMessage();
    }
    assertFalse(path + " is not routed: " + error, error != null && error.contains(MISSING_ROUTE));
  }
}
