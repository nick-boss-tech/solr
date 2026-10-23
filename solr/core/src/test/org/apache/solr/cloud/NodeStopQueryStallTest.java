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

import java.lang.invoke.MethodHandles;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.jetty.HttpJettySolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.TimeSource;
import org.apache.solr.embedded.JettyConfig;
import org.apache.solr.embedded.JettySolrRunner;
import org.apache.solr.util.TimeOut;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Measures how long a query to a live node stalls while another node of the cluster is stopped. */
public class NodeStopQueryStallTest extends SolrTestCase {

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final int NODES = 4;
  private static final int SAMPLES_AROUND_STOP = 100;
  private static final long STALL_LIMIT_MS = 1000;
  private static final long WAIT_SECONDS = 120;

  @Test
  public void testStopWithGracefulShutdown() throws Exception {
    measureStall(true);
  }

  @Test
  public void testStopWithoutGracefulShutdown() throws Exception {
    measureStall(false);
  }

  private void measureStall(boolean graceful) throws Exception {
    MiniSolrCloudCluster cluster =
        new MiniSolrCloudCluster(
            NODES, createTempDir(), JettyConfig.builder().enableGracefulShutdown(graceful).build());
    try {
      String collection = "stall";
      CollectionAdminRequest.createCollection(collection, "_default", NODES, 2)
          .process(cluster.getSolrClient());
      cluster.waitForActiveCollection(collection, NODES, NODES * 2);
      for (int i = 0; i < 100; i++) {
        cluster.getSolrClient().add(collection, new SolrInputDocument("id", "doc" + i));
      }
      cluster.getSolrClient().commit(collection);

      JettySolrRunner survivor = cluster.getJettySolrRunner(0);
      JettySolrRunner victim = cluster.getJettySolrRunner(NODES - 1);
      AtomicBoolean running = new AtomicBoolean(true);
      Queue<long[]> samples = new ConcurrentLinkedQueue<>();
      Thread querier =
          new Thread(
              () -> {
                try (HttpJettySolrClient client =
                    new HttpJettySolrClient.Builder(survivor.getBaseUrl().toString()).build()) {
                  while (running.get()) {
                    long start = System.nanoTime();
                    boolean ok = true;
                    try {
                      client.query(collection, new SolrQuery("*:*"));
                    } catch (Exception e) {
                      ok = false;
                    }
                    samples.add(new long[] {start, System.nanoTime(), ok ? 1 : 0});
                  }
                }
              });
      querier.start();

      waitForSamples(samples, SAMPLES_AROUND_STOP);
      long stopStart = System.nanoTime();
      cluster.stopJettySolrRunner(victim);
      long stopEnd = System.nanoTime();
      int atStop = samples.size();
      waitForSamples(samples, atStop + SAMPLES_AROUND_STOP);
      running.set(false);
      querier.join(TimeUnit.SECONDS.toMillis(WAIT_SECONDS));

      long maxLatencyMs = 0;
      int failures = 0;
      for (long[] s : samples) {
        if (s[2] == 0) {
          failures++;
        }
        if (s[1] >= stopStart && s[0] <= stopEnd) {
          maxLatencyMs = Math.max(maxLatencyMs, (s[1] - s[0]) / 1_000_000);
        }
      }
      log.warn(
          "STOP-STALL graceful={} samples={} failures={} stopMs={} maxLatencyDuringStopMs={}",
          graceful,
          samples.size(),
          failures,
          (stopEnd - stopStart) / 1_000_000,
          maxLatencyMs);
      assertTrue(
          "a query to a live node stalled for " + maxLatencyMs + " ms while another node stopped",
          maxLatencyMs < STALL_LIMIT_MS);
    } finally {
      cluster.shutdown();
    }
  }

  private static void waitForSamples(Queue<long[]> samples, int count) throws Exception {
    TimeOut timeout = new TimeOut(WAIT_SECONDS, TimeUnit.SECONDS, TimeSource.NANO_TIME);
    timeout.waitFor("not enough query samples", () -> samples.size() >= count);
  }
}
