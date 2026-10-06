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

import static org.apache.solr.SolrTestCaseJ4.assumeWorkingMockito;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.core.CoreContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OverseerElectionContextTest extends SolrTestCase {

  private ZkTestServer server;
  private SolrZkClient zkClient;

  @Before
  @Override
  public void setUp() throws Exception {
    assumeWorkingMockito();
    super.setUp();
    server = new ZkTestServer(createTempDir("zkData"));
    server.run();
    zkClient =
        new SolrZkClient.Builder()
            .withUrl(server.getZkAddress())
            .withTimeout(AbstractZkTestCase.TIMEOUT, TimeUnit.MILLISECONDS)
            .build();
  }

  @After
  @Override
  public void tearDown() throws Exception {
    if (zkClient != null) zkClient.close();
    if (server != null) server.shutdown();
    super.tearDown();
  }

  @Test
  public void testLeaderNodeRemovedWhenOverseerIsNotStarted() throws Exception {
    CoreContainer coreContainer = mock(CoreContainer.class);
    when(coreContainer.isShutDown()).thenReturn(true);
    ZkController zkController = mock(ZkController.class);
    when(zkController.getCoreContainer()).thenReturn(coreContainer);
    Overseer overseer = mock(Overseer.class);
    when(overseer.getZkController()).thenReturn(zkController);

    OverseerElectionContext context = new OverseerElectionContext(zkClient, overseer, "node1");
    context.leaderSeqPath = Overseer.OVERSEER_ELECT + "/election/1234-node1-n_0000000000";

    context.runLeaderProcess(false);

    verify(overseer, never()).start(anyString());
    assertFalse(
        "leader registration must not outlive a skipped Overseer start",
        zkClient.exists(Overseer.OVERSEER_ELECT + "/leader"));
  }
}
