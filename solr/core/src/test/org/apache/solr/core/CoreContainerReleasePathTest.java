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
package org.apache.solr.core;

import io.opentelemetry.api.common.Attributes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.util.ExecutorUtil;
import org.apache.solr.common.util.ObjectReleaseTracker;
import org.apache.solr.common.util.SolrNamedThreadFactory;
import org.apache.solr.common.util.TimeSource;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.handler.component.HttpShardHandlerFactory;
import org.apache.solr.metrics.SolrMetricsContext;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.security.AuthorizationContext;
import org.apache.solr.security.AuthorizationPlugin;
import org.apache.solr.security.AuthorizationResponse;
import org.apache.solr.security.PermissionNameProvider;
import org.apache.solr.util.TimeOut;
import org.junit.BeforeClass;
import org.junit.Test;

/** Distinguishes a thrown close-pool timeout from an interrupted wait that returns early. */
public class CoreContainerReleasePathTest extends SolrTestCase {
  private static final int CLEANUP_TIMEOUT_SECONDS = 30;
  private static final String SOLR_XML =
      "<solr><str name=\"configSetBaseDir\">${configsets:configsets}</str></solr>";

  @BeforeClass
  public static void configureConfigsets() {
    System.setProperty(
        "configsets", SolrTestCaseJ4.getFile("solr/configsets").toAbsolutePath().toString());
  }

  @Test
  public void testTimeoutStillClosesContainerTail() throws Exception {
    CoreContainer container = createContainer();
    CloseTrackingAuthorizationPlugin plugin =
        (CloseTrackingAuthorizationPlugin) container.getAuthorizationPlugin();
    CloseTrackingHandler handler = addHandler(container);
    BlockingFactory factory = replaceFactory(container);
    RuntimeException shutdownFailure = null;
    try {
      try {
        container.shutdown();
      } catch (RuntimeException e) {
        shutdownFailure = e;
      }
      if (shutdownFailure != null) {
        assertTrue(shutdownFailure.getMessage().contains("Timeout waiting for pool to shutdown"));
      }
      System.out.println("container timeout tracker: " + ObjectReleaseTracker.checkEmpty());
      assertTrue("authorization plugin must close", plugin.closed.get());
      assertTrue("container handlers must close", handler.closed.get());
      assertFalse(ObjectReleaseTracker.OBJECTS.containsKey(handler.getSolrMetricsContext()));
      assertFalse(ObjectReleaseTracker.OBJECTS.containsKey(container.solrMetricsContext));
      assertTrue(
          "a still-blocked factory remains its own owner's responsibility",
          ObjectReleaseTracker.OBJECTS.containsKey(factory.getSolrMetricsContext()));
    } finally {
      factory.release.countDown();
      assertTrue(factory.finished.await(CLEANUP_TIMEOUT_SECONDS, TimeUnit.SECONDS));
      assertNull(factory.failure.get());
      if (shutdownFailure != null) {
        plugin.close();
        container.getRequestHandlers().close();
        container.getResourceLoader().close();
        container.solrMetricsContext.close();
        container.getMetricManager().closeAllRegistries();
      }
    }
  }

  @Test
  public void testInterruptedContainerWaitReturnsBeforeFactoryClose() throws Exception {
    Set<Object> previouslyTracked = new HashSet<>(ObjectReleaseTracker.OBJECTS.keySet());
    CoreContainer container = createContainer();
    List<Object> siblingResources = siblingCloseResources(container, previouslyTracked);
    CloseTrackingAuthorizationPlugin plugin =
        (CloseTrackingAuthorizationPlugin) container.getAuthorizationPlugin();
    CloseTrackingHandler handler = addHandler(container);
    BlockingFactory factory = replaceFactory(container);
    ExecutorService runner =
        ExecutorUtil.newMDCAwareSingleThreadExecutor(
            new SolrNamedThreadFactory("release-test-shutdown"));
    AtomicReference<Thread> closer = new AtomicReference<>();
    AtomicReference<Throwable> failure = new AtomicReference<>();
    AtomicBoolean interruptPreserved = new AtomicBoolean();
    CountDownLatch returned = new CountDownLatch(1);
    try {
      runner.execute(
          () -> {
            closer.set(Thread.currentThread());
            try {
              container.shutdown();
              interruptPreserved.set(Thread.currentThread().isInterrupted());
            } catch (Throwable t) {
              failure.set(t);
            } finally {
              returned.countDown();
            }
          });
      assertTrue(factory.entered.await(CLEANUP_TIMEOUT_SECONDS, TimeUnit.SECONDS));
      new TimeOut(CLEANUP_TIMEOUT_SECONDS, TimeUnit.SECONDS, TimeSource.NANO_TIME)
          .waitFor(
              "update handler and default client provider must finish before factory interruption",
              () -> siblingResources.stream().noneMatch(ObjectReleaseTracker.OBJECTS::containsKey));
      System.out.println("sibling owners released before interruption");
      closer.get().interrupt();
      assertTrue(returned.await(CLEANUP_TIMEOUT_SECONDS, TimeUnit.SECONDS));
      assertNull(failure.get());
      assertTrue(interruptPreserved.get());
      assertTrue(plugin.closed.get());
      assertTrue(handler.closed.get());
      assertFalse(ObjectReleaseTracker.OBJECTS.containsKey(container.solrMetricsContext));
      assertTrue(ObjectReleaseTracker.OBJECTS.containsKey(factory.getSolrMetricsContext()));
      assertEquals(1L, factory.finished.getCount());
      System.out.println("interrupted container tracker: " + ObjectReleaseTracker.checkEmpty());

      factory.release.countDown();
      assertTrue(factory.finished.await(CLEANUP_TIMEOUT_SECONDS, TimeUnit.SECONDS));
      assertNull(factory.failure.get());
      assertFalse(ObjectReleaseTracker.OBJECTS.containsKey(factory.getSolrMetricsContext()));
      String remaining = ObjectReleaseTracker.checkEmpty();
      System.out.println("completed factory tracker: " + remaining);
      assertNull(remaining, remaining);
    } finally {
      factory.release.countDown();
      assertTrue(factory.finished.await(CLEANUP_TIMEOUT_SECONDS, TimeUnit.SECONDS));
      ExecutorUtil.shutdownAndAwaitTermination(runner);
    }
  }

  private static List<Object> siblingCloseResources(
      CoreContainer container, Set<Object> previouslyTracked) {
    List<Object> resources = new ArrayList<>();
    resources.add(container.getDefaultHttpSolrClient());
    resources.add(container.getUpdateShardHandler().getUpdateOnlyHttpClient());
    resources.add(container.getUpdateShardHandler().getRecoveryOnlyHttpClient());
    resources.add(container.getUpdateShardHandler().getSolrMetricsContext());
    List<SolrMetricsContext> providerContexts =
        ObjectReleaseTracker.getTrackedObjectsOfType(SolrMetricsContext.class).stream()
            .filter(context -> !previouslyTracked.contains(context))
            .filter(
                context ->
                    ObjectReleaseTracker.OBJECTS
                        .get(context)
                        .getMessage()
                        .startsWith(HttpSolrClientProvider.class.getName() + "@"))
            .toList();
    assertEquals(
        "one default client provider belongs to this container", 1, providerContexts.size());
    resources.addAll(providerContexts);
    for (Object resource : resources) {
      assertTrue(ObjectReleaseTracker.OBJECTS.containsKey(resource));
      System.out.println(
          "sibling resource: " + ObjectReleaseTracker.OBJECTS.get(resource).getMessage());
    }
    return resources;
  }

  private CoreContainer createContainer() throws Exception {
    Path home = createTempDir();
    Files.writeString(
        home.resolve("security.json"),
        "{\"authorization\":{\"class\":\""
            + CloseTrackingAuthorizationPlugin.class.getName()
            + "\"}}");
    CoreContainer container = new CoreContainer(SolrXmlConfig.fromString(home, SOLR_XML));
    container.load();
    return container;
  }

  private static CloseTrackingHandler addHandler(CoreContainer container) {
    CloseTrackingHandler handler = new CloseTrackingHandler();
    handler.initializeMetrics(container.solrMetricsContext, Attributes.empty());
    container.getRequestHandlers().put("/release-test", handler);
    return handler;
  }

  private static BlockingFactory replaceFactory(CoreContainer container) {
    BlockingFactory factory =
        new BlockingFactory((HttpShardHandlerFactory) container.shardHandlerFactory);
    container.shardHandlerFactory = factory;
    return factory;
  }

  /** Marker loaded through security.json so plugin cleanup uses the production container path. */
  public static class CloseTrackingAuthorizationPlugin implements AuthorizationPlugin {
    final AtomicBoolean closed = new AtomicBoolean();

    @Override
    public AuthorizationResponse authorize(AuthorizationContext context) {
      return AuthorizationResponse.OK;
    }

    @Override
    public void init(Map<String, Object> configuration) {}

    @Override
    public void close() {
      closed.set(true);
    }
  }

  private static final class CloseTrackingHandler extends RequestHandlerBase {
    final AtomicBoolean closed = new AtomicBoolean();

    @Override
    public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) {}

    @Override
    public String getDescription() {
      return "release path marker";
    }

    @Override
    public PermissionNameProvider.Name getPermissionName(AuthorizationContext request) {
      return PermissionNameProvider.Name.ALL;
    }

    @Override
    public void close() throws IOException {
      closed.set(true);
      super.close();
    }
  }

  private static final class BlockingFactory extends HttpShardHandlerFactory {
    final HttpShardHandlerFactory delegate;
    final CountDownLatch entered = new CountDownLatch(1);
    final CountDownLatch release = new CountDownLatch(1);
    final CountDownLatch finished = new CountDownLatch(1);
    final AtomicReference<Throwable> failure = new AtomicReference<>();

    BlockingFactory(HttpShardHandlerFactory delegate) {
      this.delegate = delegate;
    }

    @Override
    public SolrMetricsContext getSolrMetricsContext() {
      return delegate.getSolrMetricsContext();
    }

    @Override
    public void close() {
      entered.countDown();
      try {
        boolean ready = false;
        while (!ready) {
          try {
            release.await();
            ready = true;
          } catch (InterruptedException ignored) {
            // Model a close that survives shutdownNow; completion is controlled by the test.
          }
        }
        delegate.close();
      } catch (Throwable t) {
        failure.set(t);
        throw t;
      } finally {
        finished.countDown();
      }
    }
  }
}
