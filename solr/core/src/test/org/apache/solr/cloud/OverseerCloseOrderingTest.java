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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.solr.embedded.JettySolrRunner;
import org.apache.zookeeper.Watcher;
import org.junit.BeforeClass;
import org.junit.Test;

/** Stopping the overseer node must stop its overseer threads before its leader node is released. */
public class OverseerCloseOrderingTest extends SolrCloudTestCase {

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2).addConfig("conf", configset("cloud-minimal")).configure();
  }

  @Test
  public void testOverseerStoppedBeforeLeaderNodeReleased() throws Exception {
    JettySolrRunner overseerNode = null;
    Thread updaterThread = null;
    for (JettySolrRunner runner : cluster.getJettySolrRunners()) {
      Thread thread = runner.getCoreContainer().getZkController().getOverseer().getUpdaterThread();
      if (thread != null) {
        overseerNode = runner;
        updaterThread = thread;
      }
    }
    assertNotNull("no node is running the overseer", overseerNode);

    CountDownLatch released = new CountDownLatch(1);
    AtomicBoolean updaterAliveWhenReleased = new AtomicBoolean(true);
    Thread overseerUpdater = updaterThread;
    cluster
        .getZkClient()
        .exists(
            Overseer.OVERSEER_ELECT + "/leader",
            event -> {
              if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                updaterAliveWhenReleased.set(overseerUpdater.isAlive());
                released.countDown();
              }
            });

    cluster.stopJettySolrRunner(overseerNode);

    assertTrue("overseer leader node was not released", released.await(30, TimeUnit.SECONDS));
    assertFalse(
        "overseer was still running when its leader node was released",
        updaterAliveWhenReleased.get());
  }
}
