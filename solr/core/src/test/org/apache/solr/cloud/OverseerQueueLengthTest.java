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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.cloud.SolrCloudManager;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.handler.component.HttpShardHandler;
import org.apache.solr.handler.component.HttpShardHandlerFactory;
import org.apache.solr.metrics.SolrMetricsContext;
import org.junit.Test;
import org.mockito.Mockito;

public class OverseerQueueLengthTest extends SolrTestCase {

  @Test
  public void testStateUpdateAndCollectionQueuesReportTheirOwnLength() throws Exception {
    SolrZkClient zkClient = mock(SolrZkClient.class);
    when(zkClient.getChildren(eq("/overseer/queue"), any())).thenReturn(List.of("qn-0000000001"));
    when(zkClient.getChildren(eq("/overseer/collection-queue-work"), any()))
        .thenReturn(List.of("qn-0000000001", "qn-0000000002", "qn-0000000003"));

    ZkStateReader zkStateReader = mock(ZkStateReader.class);
    when(zkStateReader.getZkClient()).thenReturn(zkClient);

    Stats overseerStats = new Stats();
    ZkDistributedQueue stateUpdateQueue = Overseer.getStateUpdateQueue(zkClient, overseerStats);

    AtomicReference<OverseerTaskQueue> collectionQueue = new AtomicReference<>();
    Overseer overseer = mock(Overseer.class);
    when(overseer.getCollectionQueue(any())).thenCallRealMethod();
    when(overseer.getCollectionQueue(any(), any()))
        .thenAnswer(
            invocation -> {
              OverseerTaskQueue queue = (OverseerTaskQueue) invocation.callRealMethod();
              collectionQueue.set(queue);
              return queue;
            });

    when(overseer.getSolrCloudManager()).thenReturn(mock(SolrCloudManager.class));

    HttpShardHandlerFactory shardHandlerFactory = mock(HttpShardHandlerFactory.class);
    HttpShardHandler shardHandler = mock(HttpShardHandler.class);
    when(shardHandler.getShardHandlerFactory()).thenReturn(shardHandlerFactory);

    new OverseerCollectionConfigSetProcessor(
        zkStateReader,
        "1234",
        shardHandler,
        "/admin/cores",
        overseerStats,
        overseer,
        mock(OverseerNodePrioritizer.class),
        mock(SolrMetricsContext.class, Mockito.RETURNS_DEEP_STUBS));

    stateUpdateQueue.fetchZkChildren(null);
    collectionQueue.get().fetchZkChildren(null);

    assertEquals(1, stateUpdateQueue.getZkStats().getQueueLength());
    assertEquals(3, collectionQueue.get().getZkStats().getQueueLength());
  }
}
