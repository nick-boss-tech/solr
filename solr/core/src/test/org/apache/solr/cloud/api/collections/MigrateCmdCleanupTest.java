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
package org.apache.solr.cloud.api.collections;

import static org.apache.solr.SolrTestCaseJ4.assumeWorkingMockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import org.apache.solr.SolrTestCase;
import org.apache.solr.cloud.DistributedClusterStateUpdater;
import org.apache.solr.common.MapWriter;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.RoutingRule;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.params.CollectionParams.CollectionAction;
import org.apache.zookeeper.KeeperException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/** Failed routing cleanup must not resume target update processing. */
public class MigrateCmdCleanupTest extends SolrTestCase {
  private CollectionCommandContext context;
  private ZkStateReader reader;

  @BeforeClass
  public static void beforeClass() {
    assumeWorkingMockito();
  }

  @Before
  public void setupContext() {
    context = mock(CollectionCommandContext.class);
    reader = mock(ZkStateReader.class);
    when(context.getZkStateReader()).thenReturn(reader);
    when(context.getDistributedClusterStateUpdater())
        .thenReturn(mock(DistributedClusterStateUpdater.class));
  }

  @Test
  public void testRemovalFailureKeepsTargetBuffering() throws Exception {
    KeeperException failure = new KeeperException.ConnectionLossException();
    doThrow(failure).when(context).offerStateUpdate(any(MapWriter.class));
    assertCleanupStops(failure);
  }

  @Test
  public void testRemovalTimeoutKeepsTargetBuffering() throws Exception {
    TimeoutException failure = new TimeoutException("rule remains installed");
    when(reader.waitForState(eq("source"), eq(60L), eq(TimeUnit.SECONDS), anyStatePredicate()))
        .thenThrow(failure);
    assertCleanupStops(failure);
  }

  @Test
  public void testRemovalAssertionKeepsTargetBuffering() throws Exception {
    AssertionError failure = new AssertionError("routing cleanup assertion");
    doThrow(failure).when(context).offerStateUpdate(any(MapWriter.class));
    assertCleanupStops(failure);
  }

  @Test
  public void testReplayAssertionIsSuppressed() {
    AssertionError cleanupFailure = new AssertionError("replay request assertion");
    doThrow(cleanupFailure).when(context).newShardHandler();
    Throwable original = new AssertionError("original migration assertion");
    cleanup(original);
    assertArrayEquals(new Throwable[] {cleanupFailure}, original.getSuppressed());
    verify(context).newShardHandler();
  }

  @Test
  public void testInterruptedRemovalKeepsTargetBuffering() throws Exception {
    InterruptedException failure = new InterruptedException("cleanup interrupted");
    doThrow(failure).when(context).offerStateUpdate(any(MapWriter.class));
    try {
      assertCleanupStops(failure);
      assertTrue(Thread.currentThread().isInterrupted());
    } finally {
      Thread.interrupted();
    }
  }

  private void assertCleanupStops(Throwable cleanupFailure) {
    Exception original = new Exception("original migration failure");
    cleanup(original);
    verify(context, never()).newShardHandler();
    assertArrayEquals(new Throwable[] {cleanupFailure}, original.getSuppressed());
  }

  private void cleanup(Throwable original) {
    RoutingRule installed =
        new RoutingRule(
            Map.of("routeRanges", "0-ffff", "targetCollection", "target", "expireAt", "1000"));
    new MigrateCmd(context)
        .cleanupFailedMigrate(
            new AdminCmdContext(CollectionAction.MIGRATE),
            "source",
            "shard1",
            "a!",
            null,
            installed,
            mock(Replica.class),
            original);
  }

  private static Predicate<DocCollection> anyStatePredicate() {
    return any();
  }
}
