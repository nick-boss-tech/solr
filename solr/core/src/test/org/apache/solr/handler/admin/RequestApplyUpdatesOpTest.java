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
package org.apache.solr.handler.admin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import org.apache.solr.SolrTestCase;
import org.apache.solr.cloud.ZkController;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.CoreDescriptor;
import org.apache.solr.core.SolrCore;
import org.apache.solr.update.UpdateLog;
import org.junit.Before;
import org.junit.Test;

/** Unit tests for the replica state published by {@link RequestApplyUpdatesOp}. */
public class RequestApplyUpdatesOpTest extends SolrTestCase {

  private CoreContainer coreContainer;
  private ZkController zkController;
  private SolrCore core;
  private CoreDescriptor coreDescriptor;
  private UpdateLog updateLog;

  @Before
  public void setUpMocks() {
    zkController = mock(ZkController.class);
    coreDescriptor = mock(CoreDescriptor.class);
    core = mock(SolrCore.class);
    when(core.getCoreDescriptor()).thenReturn(coreDescriptor);
    coreContainer = mock(CoreContainer.class);
    when(coreContainer.isZooKeeperAware()).thenReturn(true);
    when(coreContainer.getZkController()).thenReturn(zkController);
    updateLog = mock(UpdateLog.class);
  }

  @Test
  public void testEmptyBufferPublishesActiveWhenLogIsActive() throws Exception {
    when(updateLog.applyBufferedUpdates()).thenReturn(null);
    when(updateLog.getState()).thenReturn(UpdateLog.State.ACTIVE);

    assertEquals(
        "EMPTY_BUFFER", RequestApplyUpdatesOp.applyBufferedUpdates(coreContainer, core, updateLog));
    verify(zkController).publish(coreDescriptor, Replica.State.ACTIVE);
  }

  @Test
  public void testNullFutureWhileLogIsNotActiveDoesNotPublish() throws Exception {
    when(updateLog.applyBufferedUpdates()).thenReturn(null);
    when(updateLog.getState()).thenReturn(UpdateLog.State.APPLYING_BUFFERED);

    assertEquals(
        "EMPTY_BUFFER", RequestApplyUpdatesOp.applyBufferedUpdates(coreContainer, core, updateLog));
    verifyNoInteractions(zkController);
  }

  @Test
  public void testAppliedBufferPublishesActive() throws Exception {
    when(updateLog.applyBufferedUpdates())
        .thenReturn(CompletableFuture.completedFuture(new UpdateLog.RecoveryInfo()));

    assertEquals(
        "BUFFER_APPLIED",
        RequestApplyUpdatesOp.applyBufferedUpdates(coreContainer, core, updateLog));
    verify(zkController).publish(coreDescriptor, Replica.State.ACTIVE);
  }

  @Test
  public void testFailedReplayDoesNotPublish() throws Exception {
    UpdateLog.RecoveryInfo failed = new UpdateLog.RecoveryInfo();
    failed.failed = true;
    when(updateLog.applyBufferedUpdates()).thenReturn(CompletableFuture.completedFuture(failed));

    expectThrows(
        SolrException.class,
        () -> RequestApplyUpdatesOp.applyBufferedUpdates(coreContainer, core, updateLog));
    verifyNoInteractions(zkController);
  }

  @Test
  public void testStandaloneCoreIsNotPublished() throws Exception {
    when(coreContainer.isZooKeeperAware()).thenReturn(false);
    when(updateLog.applyBufferedUpdates()).thenReturn(null);
    when(updateLog.getState()).thenReturn(UpdateLog.State.ACTIVE);

    assertEquals(
        "EMPTY_BUFFER", RequestApplyUpdatesOp.applyBufferedUpdates(coreContainer, core, updateLog));
    verify(coreContainer, never()).getZkController();
  }
}
