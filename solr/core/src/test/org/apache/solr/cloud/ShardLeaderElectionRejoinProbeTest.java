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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.common.util.TimeSource;
import org.apache.solr.util.TimeOut;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShardLeaderElectionRejoinProbeTest extends SolrCloudTestCase {

  private static final int FORCED_FAILURES = 5000;

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
    AtomicInteger firstDepth = new AtomicInteger(-1);
    AtomicInteger lastDepth = new AtomicInteger(-1);
    ShardLeaderElectionContext.testForceSyncFailure =
        core -> {
          core.readOnly = true;
          int n = calls.incrementAndGet();
          int depth = Thread.currentThread().getStackTrace().length;
          if (n == 1) {
            firstDepth.set(depth);
          }
          lastDepth.set(depth);
          return n <= FORCED_FAILURES;
        };

    CollectionAdminRequest.createCollection("rejoin", "conf", 1, 1)
        .processAsync("rejoin-create", cluster.getSolrClient());

    TimeOut timeout = new TimeOut(240, TimeUnit.SECONDS, TimeSource.NANO_TIME);
    timeout.waitFor(
        "forced sync failures did not reach " + FORCED_FAILURES + ", calls=" + calls.get(),
        () -> calls.get() > FORCED_FAILURES);

    System.err.println(
        "REJOIN-PROBE calls="
            + calls.get()
            + " firstStackFrames="
            + firstDepth.get()
            + " lastStackFrames="
            + lastDepth.get());
  }
}
