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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShardLeaderElectionRejoinTest extends SolrCloudTestCase {

  private static final int FORCED_FAILURES = 3;
  private static final int MAX_FRAME_GROWTH = 6;
  private static final long REJOIN_WAIT_SECONDS = 120;

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1).addConfig("conf", configset("cloud-minimal")).configure();
  }

  @After
  public void clearHook() {
    ShardLeaderElectionContext.testForceSyncFailure = null;
  }

  @Test
  public void testRepeatedRejoinDoesNotGrowStack() throws Exception {
    AtomicInteger calls = new AtomicInteger();
    Queue<Integer> depths = new ConcurrentLinkedQueue<>();
    ShardLeaderElectionContext.testForceSyncFailure =
        core -> {
          core.readOnly = true;
          int n = calls.incrementAndGet();
          depths.add(Thread.currentThread().getStackTrace().length);
          return n <= FORCED_FAILURES;
        };

    CollectionAdminRequest.createCollection("rejoin", "conf", 1, 1)
        .processAsync("rejoin-create", cluster.getSolrClient());
    cluster.waitForActiveCollection("rejoin", REJOIN_WAIT_SECONDS, TimeUnit.SECONDS, 1, 1);

    assertEquals(FORCED_FAILURES + 1, calls.get());
    int min = depths.stream().skip(1).mapToInt(Integer::intValue).min().orElseThrow();
    int max = depths.stream().skip(1).mapToInt(Integer::intValue).max().orElseThrow();
    assertTrue(
        "stack grew by " + (max - min) + " frames over " + calls.get() + " rejoins",
        max - min < MAX_FRAME_GROWTH);
  }
}
