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

import static org.apache.solr.SolrTestCaseJ4.assumeWorkingMockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.junit.BeforeClass;
import org.junit.Test;

/** The wait at the end of FORCELEADER reports failure instead of silently returning. */
public class ForceLeaderWaitTest extends SolrTestCase {

  @BeforeClass
  public static void ensureWorkingMockito() {
    assumeWorkingMockito();
  }

  private static Slice sliceWithLeader(Replica.State leaderState) {
    Replica leader = mock(Replica.class);
    when(leader.getState()).thenReturn(leaderState);
    Slice slice = mock(Slice.class);
    when(slice.getLeader()).thenReturn(leader);
    return slice;
  }

  @Test
  public void testReturnsOnceLeaderIsActive() throws Exception {
    final AtomicInteger polls = new AtomicInteger();
    final Slice down = sliceWithLeader(Replica.State.DOWN);
    final Slice active = sliceWithLeader(Replica.State.ACTIVE);
    ForceLeader.waitForActiveLeader(
        () -> polls.incrementAndGet() < 3 ? down : active, 5, 1, "coll", "shard1");
    assertEquals(3, polls.get());
  }

  @Test
  public void testFailsWhenNoActiveLeaderAppears() {
    final Slice down = sliceWithLeader(Replica.State.DOWN);
    SolrException e =
        expectThrows(
            SolrException.class,
            () -> ForceLeader.waitForActiveLeader(() -> down, 3, 1, "coll", "shard1"));
    assertEquals(SolrException.ErrorCode.SERVER_ERROR.code, e.code());
    assertTrue(e.getMessage(), e.getMessage().contains("Couldn't force an active leader"));
  }

  @Test
  public void testFailsWhenShardIsRemoved() {
    SolrException e =
        expectThrows(
            SolrException.class,
            () -> ForceLeader.waitForActiveLeader(() -> null, 3, 1, "coll", "shard1"));
    assertEquals(SolrException.ErrorCode.SERVER_ERROR.code, e.code());
    assertTrue(e.getMessage(), e.getMessage().contains("was removed"));
  }
}
