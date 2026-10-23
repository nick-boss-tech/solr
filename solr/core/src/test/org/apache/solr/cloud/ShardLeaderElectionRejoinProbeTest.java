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

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.common.util.TimeSource;
import org.apache.solr.util.TimeOut;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShardLeaderElectionRejoinProbeTest extends SolrCloudTestCase {

  private static final int FORCED_FAILURES = 30;

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1).addConfig("conf", configset("cloud-minimal")).configure();
  }

  @After
  public void clearHook() {
    ShardLeaderElectionContext.testForceSyncFailure = null;
  }

  @Test
  public void testForcedSyncFailuresRejoinWithoutLimit() throws Exception {
    AtomicInteger calls = new AtomicInteger();
    Queue<Long> times = new ConcurrentLinkedQueue<>();
    Queue<Integer> depths = new ConcurrentLinkedQueue<>();
    ShardLeaderElectionContext.testForceSyncFailure =
        core -> {
          core.readOnly = true;
          int n = calls.incrementAndGet();
          times.add(System.nanoTime());
          depths.add(Thread.currentThread().getStackTrace().length);
          return n <= FORCED_FAILURES;
        };

    CollectionAdminRequest.createCollection("rejoin", "conf", 1, 1)
        .processAsync("rejoin-create", cluster.getSolrClient());

    TimeOut reachedTen = new TimeOut(240, TimeUnit.SECONDS, TimeSource.NANO_TIME);
    reachedTen.waitFor("ten cycles not reached", () -> calls.get() >= 10);
    for (int dump = 0; dump < 3; dump++) {
      Thread.sleep(700);
      for (Map.Entry<Thread, StackTraceElement[]> e : Thread.getAllStackTraces().entrySet()) {
        if (!e.getKey().getName().contains("parallelCoreAdminAPIBaseExecutor")) continue;
        StringBuilder sb = new StringBuilder("THREADDUMP " + dump + " " + e.getKey().getName());
        for (int i = 0; i < Math.min(14, e.getValue().length); i++) {
          sb.append("\n   at ").append(e.getValue()[i]);
        }
        System.err.println(sb);
      }
    }

    TimeOut timeout = new TimeOut(240, TimeUnit.SECONDS, TimeSource.NANO_TIME);
    timeout.waitFor(
        "forced sync failures did not reach " + FORCED_FAILURES,
        () -> calls.get() > FORCED_FAILURES);

    Long[] t = times.toArray(new Long[0]);
    long sumMs = 0;
    long maxMs = 0;
    for (int i = 1; i < t.length; i++) {
      long d = (t[i] - t[i - 1]) / 1_000_000;
      sumMs += d;
      maxMs = Math.max(maxMs, d);
    }
    System.err.println(
        "REJOIN-PROBE calls="
            + calls.get()
            + " avgCycleMs="
            + (t.length > 1 ? sumMs / (t.length - 1) : -1)
            + " maxCycleMs="
            + maxMs
            + " depths="
            + depths);
  }
}
