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
package org.apache.solr.handler.component;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.testing.exporter.InMemoryMetricExporter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.util.ExecutorUtil;
import org.apache.solr.common.util.ObjectReleaseTracker;
import org.apache.solr.common.util.SolrNamedThreadFactory;
import org.apache.solr.metrics.SolrMetricManager;
import org.apache.solr.metrics.SolrMetricsContext;
import org.apache.solr.util.stats.InstrumentedHttpListenerFactory;
import org.junit.Test;

/** Exercises the factory's metrics release independently of client shutdown. */
public class HttpShardHandlerFactoryReleaseTest extends SolrTestCase {
  private static final int CLEANUP_TIMEOUT_SECONDS = 30;

  @Test
  public void testExecutorTimeoutStillClosesMetrics() throws Exception {
    SolrMetricManager manager = new SolrMetricManager(InMemoryMetricExporter.create());
    SolrMetricsContext parent = new SolrMetricsContext(manager, "solr.node.release-test");
    ImmediateTimeoutExecutor executor = new ImmediateTimeoutExecutor();
    HttpShardHandlerFactory factory = new HttpShardHandlerFactory();
    factory.commExecutor = executor;
    factory.httpListenerFactory =
        new InstrumentedHttpListenerFactory(
            InstrumentedHttpListenerFactory.getNameStrategy("methodOnly"));
    CountDownLatch started = new CountDownLatch(1);
    CountDownLatch release = new CountDownLatch(1);
    try {
      factory.initializeMetrics(parent, Attributes.empty());
      SolrMetricsContext context = factory.getSolrMetricsContext();
      assertTrue(ObjectReleaseTracker.OBJECTS.containsKey(context));
      executor.execute(
          () -> {
            started.countDown();
            boolean done = false;
            while (!done) {
              try {
                release.await();
                done = true;
              } catch (InterruptedException ignored) {
                // Only the test's latch releases this deliberately stuck worker.
              }
            }
          });
      assertTrue(started.await(CLEANUP_TIMEOUT_SECONDS, TimeUnit.SECONDS));

      RuntimeException failure = expectThrows(RuntimeException.class, factory::close);
      assertTrue(failure.getMessage().contains("Timeout waiting for pool to shutdown"));
      assertEquals(2, executor.waits.get());
      parent.close();
      System.out.println("factory timeout tracker: " + ObjectReleaseTracker.checkEmpty());
      assertFalse(
          "executor failure must not bypass the factory context close",
          ObjectReleaseTracker.OBJECTS.containsKey(context));
    } finally {
      release.countDown();
      executor.shutdownNow();
      assertTrue(executor.awaitRealTermination());
      if (factory.getSolrMetricsContext() != null) {
        factory.getSolrMetricsContext().close();
      }
      parent.close();
      manager.closeAllRegistries();
    }
  }

  /** Uses the production timeout branch without spending two minutes waiting for the latch. */
  private static final class ImmediateTimeoutExecutor
      extends ExecutorUtil.MDCAwareThreadPoolExecutor {
    final AtomicInteger waits = new AtomicInteger();

    ImmediateTimeoutExecutor() {
      super(
          1,
          1,
          0,
          TimeUnit.SECONDS,
          new LinkedBlockingQueue<>(),
          new SolrNamedThreadFactory("release-test-blocked-worker"));
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      waits.incrementAndGet();
      return false;
    }

    boolean awaitRealTermination() throws InterruptedException {
      return super.awaitTermination(CLEANUP_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }
  }
}
