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

import io.opentelemetry.exporter.prometheus.PrometheusMetricReader;
import io.prometheus.metrics.model.snapshots.CounterSnapshot;
import io.prometheus.metrics.model.snapshots.GaugeSnapshot;
import io.prometheus.metrics.model.snapshots.Labels;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.jetty.HttpJettySolrClient;
import org.apache.solr.util.SolrJettyTestRule;
import org.apache.solr.util.SolrMetricTestUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.StringRequestContent;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class JettyServerMetricsTest extends SolrTestCase {

  private static final Labels SCOPE = Labels.of("otel_scope_name", "org.apache.solr");

  @ClassRule public static SolrJettyTestRule solrTestRule = new SolrJettyTestRule();

  @BeforeClass
  public static void beforeClass() throws Exception {
    solrTestRule.startSolr(createTempDir());
  }

  private static PrometheusMetricReader reader() {
    return SolrMetricTestUtils.getPrometheusMetricReader(
        solrTestRule.getJetty().getCoreContainer(), JettyServerMetrics.JETTY_REGISTRY);
  }

  private static double counter(String name, Labels labels) {
    CounterSnapshot.CounterDataPointSnapshot dp =
        SolrMetricTestUtils.getCounterDatapoint(reader(), name, labels);
    assertNotNull("missing counter " + name + " " + labels, dp);
    return dp.getValue();
  }

  private static double gauge(String name, Labels labels) {
    GaugeSnapshot.GaugeDataPointSnapshot dp =
        SolrMetricTestUtils.getGaugeDatapoint(reader(), name, labels);
    assertNotNull("missing gauge " + name + " " + labels, dp);
    return dp.getValue();
  }

  private static Labels with(String key, String value) {
    return SCOPE.merge(Labels.of(key, value));
  }

  private static int send(HttpClient client, String method, String url) throws Exception {
    return client
        .newRequest(url)
        .method(method)
        .body(new StringRequestContent("x"))
        .send()
        .getStatus();
  }

  @Test
  public void testRequestMetricsIncludeRequestsSolrDoesNotServe() throws Exception {
    double requestsBefore = counter("jetty_requests", SCOPE);
    double ok2xxBefore = counter("jetty_responses", with("status_class", "2xx"));
    double err4xxBefore = counter("jetty_responses", with("status_class", "4xx"));

    String solrUrl = solrTestRule.getBaseUrl();
    String rootUrl = solrUrl.substring(0, solrUrl.lastIndexOf("/solr"));
    HttpClient client = ((HttpJettySolrClient) solrTestRule.getAdminClient()).getHttpClient();
    assertEquals(200, send(client, "GET", solrUrl + "/admin/info/system"));
    // neither of these reach a Solr collection or core, so no other Solr metric sees them
    assertEquals(404, send(client, "PUT", solrUrl + "/no_such_collection/update/nowhere"));
    assertEquals(404, send(client, "PUT", rootUrl + "/not-solr-at-all"));

    assertTrue(counter("jetty_requests", SCOPE) >= requestsBefore + 3);
    assertTrue(counter("jetty_responses", with("status_class", "2xx")) >= ok2xxBefore + 1);
    assertTrue(counter("jetty_responses", with("status_class", "4xx")) >= err4xxBefore + 2);
    assertTrue(counter("jetty_request_time_milliseconds", SCOPE) >= 0);
    // only body bytes the application consumes are counted; these requests have theirs ignored
    assertTrue(counter("jetty_request_read_bytes", SCOPE) >= 0);
    assertTrue(counter("jetty_response_written_bytes", SCOPE) > 0);
    // the metrics request itself is not issued over HTTP here, so nothing is active
    assertEquals(0.0, gauge("jetty_requests_active", SCOPE), 0.0);
  }

  @Test
  public void testThreadPoolMetrics() {
    double max = gauge("jetty_thread_pool_threads_limit", with("state", "max"));
    double min = gauge("jetty_thread_pool_threads_limit", with("state", "min"));
    assertTrue("max=" + max, max > 0);
    assertTrue("min=" + min + " max=" + max, min <= max);

    double busy = gauge("jetty_thread_pool_threads", with("state", "busy"));
    double idle = gauge("jetty_thread_pool_threads", with("state", "idle"));
    assertTrue(busy >= 0 && idle >= 0 && busy + idle <= max);

    double utilization = gauge("jetty_thread_pool_utilization", SCOPE);
    assertTrue("utilization=" + utilization, utilization >= 0.0 && utilization <= 1.0);
    assertTrue(gauge("jetty_thread_pool_queue_size", SCOPE) >= 0);
  }
}
