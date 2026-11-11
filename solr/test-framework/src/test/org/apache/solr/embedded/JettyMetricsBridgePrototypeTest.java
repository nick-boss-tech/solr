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
package org.apache.solr.embedded;

import io.prometheus.metrics.model.snapshots.CounterSnapshot;
import io.prometheus.metrics.model.snapshots.GaugeSnapshot;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.util.TimeSource;
import org.apache.solr.metrics.SolrMetricsContext;
import org.apache.solr.util.TimeOut;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.EventsHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.junit.Test;

/** Experimental proof of a Jetty-owned source registered after Solr starts. */
public class JettyMetricsBridgePrototypeTest extends SolrTestCaseJ4 {
  @Test
  public void testServerMetricsBridge() throws Exception {
    Path solrHome = createTempDir();
    Files.writeString(solrHome.resolve("solr.xml"), "<solr/>", StandardCharsets.UTF_8);
    var jetty = new JettySolrRunner(solrHome.toString(), JettyConfig.builder().build());
    try {
      jetty.start();
      var connection =
          (HttpURLConnection)
              jetty.getBaseUrl().toURI().resolve("/missing-path").toURL().openConnection();
      connection.setRequestMethod("PUT");
      try {
        assertEquals(404, connection.getResponseCode());
      } finally {
        connection.disconnect();
      }
      var valid =
          (HttpURLConnection)
              jetty
                  .getBaseUrl()
                  .toURI()
                  .resolve("/solr/admin/info/system")
                  .toURL()
                  .openConnection();
      try {
        assertEquals(200, valid.getResponseCode());
      } finally {
        valid.disconnect();
      }
      var reader =
          jetty.getCoreContainer().getMetricManager().getPrometheusMetricReader("solr.jetty");
      assertNotNull(reader);
      new TimeOut(10, TimeUnit.SECONDS, TimeSource.NANO_TIME)
          .waitFor(
              "PUT and 4xx metrics",
              () -> {
                boolean put = false;
                boolean get = false;
                boolean response = false;
                boolean pool = false;
                for (var snapshot : reader.collect()) {
                  if (snapshot.getMetadata().getPrometheusName().equals("solr_jetty_requests")) {
                    put =
                        ((CounterSnapshot) snapshot)
                            .getDataPoints().stream()
                                .anyMatch(
                                    p ->
                                        "PUT".equals(p.getLabels().get("method"))
                                            && p.getValue() == 1);
                    get =
                        ((CounterSnapshot) snapshot)
                            .getDataPoints().stream()
                                .anyMatch(
                                    p ->
                                        "GET".equals(p.getLabels().get("method"))
                                            && p.getValue() >= 1);
                  } else if (snapshot
                      .getMetadata()
                      .getPrometheusName()
                      .equals("solr_jetty_responses")) {
                    response =
                        ((CounterSnapshot) snapshot)
                            .getDataPoints().stream()
                                .anyMatch(
                                    p ->
                                        "4xx".equals(p.getLabels().get("status"))
                                            && p.getValue() >= 1);
                  } else if (snapshot
                      .getMetadata()
                      .getPrometheusName()
                      .equals("solr_jetty_threads")) {
                    pool =
                        ((GaugeSnapshot) snapshot)
                            .getDataPoints().stream()
                                .anyMatch(
                                    p ->
                                        "threads.busy".equals(p.getLabels().get("state"))
                                            && p.getValue() >= 0);
                  }
                }
                return put && get && response && pool;
              });
    } finally {
      jetty.stop();
    }
  }

  @Test
  public void testLateBridgeIncludesUnroutableRequest() throws Exception {
    Path solrHome = createTempDir();
    Files.writeString(solrHome.resolve("solr.xml"), "<solr/>", StandardCharsets.UTF_8);
    var handler = new RequestCounts();
    var jetty =
        new JettySolrRunner(solrHome.toString(), new Properties(), JettyConfig.builder().build()) {
          @Override
          protected Handler.Wrapper injectJettyHandlers(Handler.Wrapper chain) {
            handler.setHandler(chain);
            return handler;
          }
        };
    try {
      jetty.start();
      var url = jetty.getBaseUrl().toURI().resolve("/missing-jetty-path").toURL();
      var connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("PUT");
      try {
        assertEquals(404, connection.getResponseCode());
      } finally {
        connection.disconnect();
      }
      new TimeOut(10, TimeUnit.SECONDS, TimeSource.NANO_TIME)
          .waitFor("request completion", () -> handler.puts.sum() == 1);

      var manager = jetty.getCoreContainer().getMetricManager();
      try (var context = new SolrMetricsContext(manager, "solr.jetty.prototype")) {
        context.observableLongCounter(
            "solr.jetty.prototype.put",
            "Completed PUT requests",
            m -> m.record(handler.puts.sum()));
        context.observableLongCounter(
            "solr.jetty.prototype.4xx",
            "Completed 4xx responses",
            m -> m.record(handler.errors.sum()));
        var pool = (QueuedThreadPool) handler.getServer().getThreadPool();
        context.observableLongGauge(
            "solr.jetty.prototype.threads.max",
            "Maximum pool threads",
            m -> m.record(pool.getMaxThreads()));

        var snapshots = manager.getPrometheusMetricReader("solr.jetty.prototype").collect();
        boolean putFound = false;
        boolean errorFound = false;
        boolean poolFound = false;
        for (var snapshot : snapshots) {
          switch (snapshot.getMetadata().getPrometheusName()) {
            case "solr_jetty_prototype_put" -> {
              putFound = true;
              assertEquals(
                  1.0, ((CounterSnapshot) snapshot).getDataPoints().get(0).getValue(), 0.0);
            }
            case "solr_jetty_prototype_4xx" -> {
              errorFound = true;
              assertEquals(
                  1.0, ((CounterSnapshot) snapshot).getDataPoints().get(0).getValue(), 0.0);
            }
            case "solr_jetty_prototype_threads_max" -> poolFound = true;
          }
        }
        assertTrue("PUT counter missing", putFound);
        assertTrue("4xx counter missing", errorFound);
        assertTrue("thread pool gauge missing", poolFound);
      }
    } finally {
      jetty.stop();
    }
  }

  private static class RequestCounts extends EventsHandler {
    private final LongAdder puts = new LongAdder();
    private final LongAdder errors = new LongAdder();

    @Override
    protected void onComplete(Request request, int status, HttpFields headers, Throwable failure) {
      if (status / 100 == 4) errors.increment();
      if ("PUT".equals(request.getMethod())) puts.increment();
    }
  }
}
