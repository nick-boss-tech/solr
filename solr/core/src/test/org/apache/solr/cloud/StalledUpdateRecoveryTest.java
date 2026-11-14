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

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.util.ExecutorUtil;
import org.apache.solr.common.util.SuppressForbidden;
import org.apache.solr.embedded.JettySolrRunner;
import org.apache.solr.update.SolrCmdDistributor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** Exercises replica recovery after a queued leader-to-replica update stream stalls. */
public class StalledUpdateRecoveryTest extends SolrCloudTestCase {
  private static final String COLLECTION = "stalled_update_recovery";
  private static final String SHARD = "shard1";
  private static final int DOC_COUNT = 64;
  @Before
  public void setupCluster() throws Exception {
    System.setProperty("solr.cloud.client.pollQueueTime", "100");
    System.setProperty("solr.cloud.client.stallTime", "1000");
    System.setProperty("solr.retries.to.followers", "0");
    configureCluster(2)
        .withJettyConfig(b -> b.withFilter(StallReplicaUpdateFilter.class, "/*"))
        .addConfig("conf", configset("cloud-minimal"))
        .configure();
  }

  @After
  public void tearDownCluster() throws Exception {
    StallControl control = StallReplicaUpdateFilter.CONTROL.getAndSet(null);
    if (control != null) control.release.complete(null);
    SolrCmdDistributor.testing_errorHook = null;
    shutdownCluster();
  }

  @Test
  public void testStalledReplicaRecoversAcknowledgedUpdates() throws Exception {
    CollectionAdminRequest.createCollection(COLLECTION, "conf", 1, 2)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection(COLLECTION, 1, 2);
    Slice shard = getCollectionState(COLLECTION).getSlice(SHARD);
    Replica leader = shard.getLeader();
    Replica replica = shard.getReplicas(r -> !r.isLeader()).getFirst();
    JettySolrRunner leaderJetty = cluster.getReplicaJetty(leader);
    ZkShardTerms terms =
        leaderJetty.getCoreContainer().getZkController().getShardTerms(COLLECTION, SHARD);
    assertTrue(terms.getShardTerms().canBecomeLeader(replica.getName()));

    StallControl control = new StallControl(replica.getCoreName());
    assertTrue(StallReplicaUpdateFilter.CONTROL.compareAndSet(null, control));
    CountDownLatch detected = new CountDownLatch(1);
    AtomicReference<String> stallMessage = new AtomicReference<>();
    SolrCmdDistributor.testing_errorHook =
        data -> {
          if (data[0] instanceof IOException error
              && error.getMessage() != null
              && error.getMessage().contains("Request processing has stalled")) {
            stallMessage.set(error.getMessage());
            detected.countDown();
          }
        };

    ExecutorService executor = ExecutorUtil.newMDCAwareCachedThreadPool("stalled-update-test");
    try {
      // The replica filter does not read the request. A large batch makes the streaming
      // writer block on backpressure while later updates remain in the client's queue.
      String payload = "x".repeat(8192);
      UpdateRequest batch = new UpdateRequest();
      for (int i = 0; i < DOC_COUNT; i++) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "doc-" + i);
        for (int field = 0; field < 16; field++) {
          doc.addField("payload" + field + "_s", payload);
        }
        batch.add(doc);
      }
      SolrClient leaderClient = cluster.getSolrClient(leader);
      Future<?> update = executor.submit(() -> batch.process(leaderClient));

      assertTrue(
          "replica update did not reach the filter", control.arrived.await(30, TimeUnit.SECONDS));
      assertTrue(
          "no queued-stream stall reached error processing", detected.await(45, TimeUnit.SECONDS));
      assertTrue(
          stallMessage.get(), stallMessage.get().contains("remaining elements in the queue"));
      update.get(45, TimeUnit.SECONDS);

      terms.refreshTerms();
      assertFalse(
          "stalled replica remains leader eligible",
          terms.getShardTerms().canBecomeLeader(replica.getName()));

      // Release only after the leader has processed the error and changed the terms.
      control.release.complete(null);
      StallReplicaUpdateFilter.CONTROL.compareAndSet(control, null);
      cluster.waitForActiveCollection(COLLECTION, 90, TimeUnit.SECONDS, 1, 2);
      terms.refreshTerms();
      assertTrue(
          "replica did not regain leader eligibility",
          terms.getShardTerms().canBecomeLeader(replica.getName()));
      SolrClient replicaClient = cluster.getSolrClient(replica);
      for (int i = 0; i < DOC_COUNT; i++) {
        String id = "doc-" + i;
        SolrDocument leaderDoc = leaderClient.getById(id);
        SolrDocument replicaDoc = replicaClient.getById(id);
        assertNotNull("leader missing " + id, leaderDoc);
        assertNotNull("replica missing " + id, replicaDoc);
        assertNotNull("leader version missing for " + id, leaderDoc.getFieldValue("_version_"));
        assertEquals(
            "version differs for " + id,
            leaderDoc.getFieldValue("_version_"),
            replicaDoc.getFieldValue("_version_"));
      }
    } finally {
      control.release.complete(null);
      StallReplicaUpdateFilter.CONTROL.compareAndSet(control, null);
      ExecutorUtil.shutdownAndAwaitTermination(executor);
    }
  }

  private static class StallControl {
    final String core;
    final CountDownLatch arrived = new CountDownLatch(1);
    final CompletableFuture<Void> release = new CompletableFuture<>();

    StallControl(String core) {
      this.core = core;
    }
  }

  public static class StallReplicaUpdateFilter implements Filter {
    static final AtomicReference<StallControl> CONTROL = new AtomicReference<>();

    @Override
    @SuppressForbidden(reason = "Test filter needs the distributed update phase")
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
      StallControl control = CONTROL.get();
      if (control != null
          && request instanceof HttpServletRequest http
          && http.getRequestURI().contains("/" + control.core + "/update")
          && "FROMLEADER".equals(http.getParameter("update.distrib"))) {
        control.arrived.countDown();
        try {
          control.release.get(90, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new InterruptedIOException(e.toString());
        } catch (ExecutionException | TimeoutException e) {
          throw new IOException(e);
        }
      }
      chain.doFilter(request, response);
    }
  }
}
