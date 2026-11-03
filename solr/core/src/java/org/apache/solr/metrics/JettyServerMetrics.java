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
package org.apache.solr.metrics;

import static org.apache.solr.metrics.SolrMetricProducer.STATE_KEY_ATTR;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import java.io.Closeable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.solr.common.util.IOUtils;
import org.apache.solr.core.SolrInfoBean;
import org.apache.solr.metrics.otel.OtelUnit;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exposes HTTP layer metrics of the Jetty {@link Server} hosting Solr in the {@code solr.jetty}
 * registry.
 *
 * <p>Jetty starts before Solr's {@link SolrMetricManager} exists, so nothing in the Jetty layer
 * registers metrics itself. Instead, Jetty's plain {@link StatisticsHandler} (configured in {@code
 * jetty.xml}) and {@link QueuedThreadPool} keep their own counters, and this class reads them via
 * observable instruments at collection time.
 */
public class JettyServerMetrics implements Closeable {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static final String JETTY_REGISTRY =
      SolrMetricManager.REGISTRY_NAME_PREFIX + SolrInfoBean.Group.jetty;

  public static final AttributeKey<String> STATUS_CLASS_ATTR =
      AttributeKey.stringKey("status_class");

  private final List<AutoCloseable> instruments = new ArrayList<>();

  public JettyServerMetrics(SolrMetricManager metricManager, Server server) {
    StatisticsHandler stats = server.getDescendant(StatisticsHandler.class);
    if (stats != null) {
      registerRequestMetrics(metricManager, stats);
    } else {
      log.info("No Jetty StatisticsHandler configured; Jetty request metrics are unavailable");
    }
    registerThreadPoolMetrics(metricManager, server.getThreadPool());
  }

  private void registerRequestMetrics(SolrMetricManager mm, StatisticsHandler stats) {
    instruments.add(
        mm.observableLongCounter(
            JETTY_REGISTRY,
            "jetty.requests",
            "Total number of HTTP requests received by Jetty, including those not served by Solr",
            m -> m.record(stats.getRequestTotal()),
            null));
    instruments.add(
        mm.observableLongGauge(
            JETTY_REGISTRY,
            "jetty.requests.active",
            "Number of HTTP requests currently in progress",
            m -> m.record(stats.getRequestsActive()),
            null));
    instruments.add(
        mm.observableLongCounter(
            JETTY_REGISTRY,
            "jetty.responses",
            "Total number of HTTP responses by status class (1xx, 2xx, 3xx, 4xx, 5xx)",
            m -> {
              m.record(stats.getResponses1xx(), Attributes.of(STATUS_CLASS_ATTR, "1xx"));
              m.record(stats.getResponses2xx(), Attributes.of(STATUS_CLASS_ATTR, "2xx"));
              m.record(stats.getResponses3xx(), Attributes.of(STATUS_CLASS_ATTR, "3xx"));
              m.record(stats.getResponses4xx(), Attributes.of(STATUS_CLASS_ATTR, "4xx"));
              m.record(stats.getResponses5xx(), Attributes.of(STATUS_CLASS_ATTR, "5xx"));
            },
            null));
    instruments.add(
        mm.observableLongCounter(
            JETTY_REGISTRY,
            "jetty.request.failures",
            "Total number of HTTP requests that failed without a response, e.g. due to an exception",
            m -> m.record(stats.getFailures()),
            null));
    instruments.add(
        mm.observableLongCounter(
            JETTY_REGISTRY,
            "jetty.request.time",
            "Total time spent processing HTTP requests",
            m -> m.record(TimeUnit.NANOSECONDS.toMillis(stats.getRequestTimeTotal())),
            OtelUnit.MILLISECONDS));
    instruments.add(
        mm.observableLongCounter(
            JETTY_REGISTRY,
            "jetty.request.read",
            "Total number of request body bytes read by the application",
            m -> m.record(stats.getBytesRead()),
            OtelUnit.BYTES));
    instruments.add(
        mm.observableLongCounter(
            JETTY_REGISTRY,
            "jetty.response.written",
            "Total number of response body bytes written",
            m -> m.record(stats.getBytesWritten()),
            OtelUnit.BYTES));
  }

  private void registerThreadPoolMetrics(SolrMetricManager mm, ThreadPool pool) {
    if (!(pool instanceof QueuedThreadPool qtp)) {
      log.info("Jetty thread pool is not a QueuedThreadPool; thread pool metrics are unavailable");
      return;
    }
    instruments.add(
        mm.observableLongGauge(
            JETTY_REGISTRY,
            "jetty.thread_pool.threads",
            "Number of Jetty request threads by state (busy, idle)",
            m -> {
              m.record(qtp.getBusyThreads(), Attributes.of(STATE_KEY_ATTR, "busy"));
              m.record(qtp.getIdleThreads(), Attributes.of(STATE_KEY_ATTR, "idle"));
            },
            null));
    instruments.add(
        mm.observableLongGauge(
            JETTY_REGISTRY,
            "jetty.thread_pool.threads.limit",
            "Configured minimum and maximum number of Jetty request threads",
            m -> {
              m.record(qtp.getMinThreads(), Attributes.of(STATE_KEY_ATTR, "min"));
              m.record(qtp.getMaxThreads(), Attributes.of(STATE_KEY_ATTR, "max"));
            },
            null));
    instruments.add(
        mm.observableDoubleGauge(
            JETTY_REGISTRY,
            "jetty.thread_pool.utilization",
            "Fraction of the maximum Jetty threads that are in use (0.0 to 1.0)",
            m -> m.record(qtp.getUtilizationRate()),
            null));
    instruments.add(
        mm.observableLongGauge(
            JETTY_REGISTRY,
            "jetty.thread_pool.queue.size",
            "Number of jobs waiting for a free Jetty thread",
            m -> m.record(qtp.getQueueSize()),
            null));
  }

  @Override
  public void close() {
    instruments.forEach(IOUtils::closeQuietly);
    instruments.clear();
  }
}
