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

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.solr.common.util.IOUtils;
import org.apache.solr.embedded.JettySolrRunner;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * On shutdown the overseer must finish closing before the ZooKeeper session ends (that is, before
 * {@code ZkController} closes its state reader and client), so the old overseer never works against
 * a dead session while another node takes over the queues.
 */
public class OverseerCloseOrderingTest extends SolrCloudTestCase {

  @BeforeClass
  public static void setupCluster() throws Exception {
    assumeWorkingMockito();
    configureCluster(2).addConfig("conf", configset("cloud-minimal")).configure();
  }

  /** The node currently running the overseer; waits briefly for a failover to settle. */
  private JettySolrRunner awaitOverseerNode() throws Exception {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(60);
    while (System.nanoTime() < deadline) {
      for (JettySolrRunner runner : cluster.getJettySolrRunners()) {
        if (runner.getCoreContainer().getZkController().getOverseer().getUpdaterThread() != null) {
          return runner;
        }
      }
      Thread.sleep(100);
    }
    return null;
  }

  @Test
  public void testZkClientClosesOnlyAfterOverseerCloseCompletes() throws Exception {
    JettySolrRunner overseerNode = awaitOverseerNode();
    assertNotNull("no node is running the overseer", overseerNode);

    ZkController zkController = overseerNode.getCoreContainer().getZkController();
    Overseer realOverseer = zkController.getOverseer();

    // An overseer whose close() blocks until the test releases it. With the field swapped,
    // ZkController.close() cannot get past the overseer close quickly on any code path.
    CountDownLatch overseerCloseStarted = new CountDownLatch(1);
    CountDownLatch finishOverseerClose = new CountDownLatch(1);
    Overseer blockingOverseer = mock(Overseer.class);
    doAnswer(
            invocation -> {
              overseerCloseStarted.countDown();
              finishOverseerClose.await(120, TimeUnit.SECONDS);
              return null;
            })
        .when(blockingOverseer)
        .close();
    zkController.overseer = blockingOverseer;

    AtomicReference<Throwable> stopError = new AtomicReference<>();
    JettySolrRunner nodeToStop = overseerNode;
    Thread stopper =
        new Thread(
            () -> {
              try {
                cluster.stopJettySolrRunner(nodeToStop);
              } catch (Throwable t) {
                stopError.set(t);
              }
            },
            "overseer-node-stopper");

    boolean zkClientClosedDuringOverseerClose = false;
    try {
      stopper.start();
      assertTrue(
          "overseer close was never started", overseerCloseStarted.await(60, TimeUnit.SECONDS));
      // While the overseer close is blocked, the ZooKeeper client must stay open. (The poll
      // reads the Curator state because SolrZkClient.isClosed() also turns true as soon as the
      // CoreContainer starts shutting down, before ZkController.close() even runs.) On
      // unpatched code ZkController closes its state reader and client without waiting for
      // the overseer, so the client closes inside this window.
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
      while (System.nanoTime() < deadline) {
        if (zkController.getZkClient().getCuratorFramework().getState()
            == CuratorFrameworkState.STOPPED) {
          zkClientClosedDuringOverseerClose = true;
          break;
        }
        Thread.sleep(20);
      }
    } finally {
      finishOverseerClose.countDown();
      stopper.join(TimeUnit.SECONDS.toMillis(120));
    }

    assertNull("stopping the overseer node failed", stopError.get());
    assertFalse("stopper thread is still running", stopper.isAlive());
    assertTrue(
        "ZooKeeper client should be closed once shutdown completes",
        zkController.getZkClient().getCuratorFramework().getState()
            == CuratorFrameworkState.STOPPED);
    assertFalse(
        "ZooKeeper client closed before the overseer finished closing",
        zkClientClosedDuringOverseerClose);

    // The mock absorbed the shutdown closes; close the real overseer exactly once so its
    // threads do not linger past the test.
    if (!realOverseer.isClosed()) {
      IOUtils.closeQuietly(realOverseer);
    }
  }

  @Test
  public void testZkClientStaysOpenWhenShutdownThreadIsInterrupted() throws Exception {
    JettySolrRunner overseerNode = awaitOverseerNode();
    assertNotNull("no node is running the overseer", overseerNode);

    ZkController zkController = overseerNode.getCoreContainer().getZkController();
    Overseer realOverseer = zkController.getOverseer();

    CountDownLatch overseerCloseStarted = new CountDownLatch(1);
    CountDownLatch finishOverseerClose = new CountDownLatch(1);
    Overseer blockingOverseer = mock(Overseer.class);
    doAnswer(
            invocation -> {
              overseerCloseStarted.countDown();
              finishOverseerClose.await(120, TimeUnit.SECONDS);
              return null;
            })
        .when(blockingOverseer)
        .close();
    zkController.overseer = blockingOverseer;

    AtomicReference<Throwable> stopError = new AtomicReference<>();
    JettySolrRunner nodeToStop = overseerNode;
    Thread stopper =
        new Thread(
            () -> {
              try {
                cluster.stopJettySolrRunner(nodeToStop);
              } catch (Throwable t) {
                stopError.set(t);
              }
            },
            "overseer-node-stopper-interrupt");

    boolean zkClientClosedDuringOverseerClose = false;
    try {
      stopper.start();
      assertTrue(
          "overseer close was never started", overseerCloseStarted.await(60, TimeUnit.SECONDS));
      // Interrupt the shutdown while the overseer close is still blocked. The wait for the
      // overseer must survive the interrupt: if ZkController gave up waiting here, it would
      // close its state reader and client inside this window, ending the session while the
      // old overseer is still closing.
      stopper.interrupt();
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
      while (System.nanoTime() < deadline) {
        if (zkController.getZkClient().getCuratorFramework().getState()
            == CuratorFrameworkState.STOPPED) {
          zkClientClosedDuringOverseerClose = true;
          break;
        }
        Thread.sleep(20);
      }
    } finally {
      finishOverseerClose.countDown();
      stopper.join(TimeUnit.SECONDS.toMillis(120));
    }

    assertNull("stopping the overseer node failed", stopError.get());
    assertFalse("stopper thread is still running", stopper.isAlive());
    assertTrue(
        "ZooKeeper client should be closed once shutdown completes",
        zkController.getZkClient().getCuratorFramework().getState()
            == CuratorFrameworkState.STOPPED);
    assertFalse(
        "ZooKeeper client closed after the shutdown thread was interrupted, before the"
            + " overseer finished closing",
        zkClientClosedDuringOverseerClose);

    if (!realOverseer.isClosed()) {
      IOUtils.closeQuietly(realOverseer);
    }
  }
}
