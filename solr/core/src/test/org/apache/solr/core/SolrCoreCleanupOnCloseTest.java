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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/** SOLR-12007: old index directories are cleaned up before the DirectoryFactory is closed. */
public class SolrCoreCleanupOnCloseTest extends SolrTestCase {

  private static final String CLEANUP_THREAD_PREFIX = "OldIndexDirectoryCleanupThreadForCore-";

  private static final List<String> cleanupThreadNames = new CopyOnWriteArrayList<>();

  private static final List<String> closeEvents = new CopyOnWriteArrayList<>();

  private static CoreContainer coreContainer;

  /** Records which thread asked the factory to clean up old index directories, and when. */
  public static class RecordingDirectoryFactory extends MockDirectoryFactory {
    @Override
    public void cleanupOldIndexDirectories(
        String dataDirPath, String currentIndexDirPath, boolean afterCoreReload) {
      cleanupThreadNames.add(Thread.currentThread().getName());
      closeEvents.add("cleanup-start");
      try {
        // Widen the window in which a backgrounded cleanup would still be running
        // when SolrCore.close() goes on to close this factory.
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      super.cleanupOldIndexDirectories(dataDirPath, currentIndexDirPath, afterCoreReload);
      closeEvents.add("cleanup-done");
    }

    @Override
    public void close() throws IOException {
      closeEvents.add("factory-close");
      super.close();
    }
  }

  @BeforeClass
  public static void beforeClass() throws Exception {
    SolrTestCaseJ4.newRandomConfig();
    Path solrHome = createTempDir();
    Path confSource = SolrTestCaseJ4.TEST_COLL1_CONF();
    Path confTarget = solrHome.resolve("collection1").resolve("conf");
    try (var stream = Files.walk(confSource)) {
      for (Path source : stream.toList()) {
        Path target = confTarget.resolve(confSource.relativize(source).toString());
        if (Files.isDirectory(source)) {
          Files.createDirectories(target);
        } else {
          Files.copy(source, target);
        }
      }
    }
    Files.writeString(
        solrHome.resolve("collection1").resolve("core.properties"), "name=collection1\n");
    System.setProperty("solr.directoryFactory", RecordingDirectoryFactory.class.getName());
    coreContainer =
        new CoreContainer(new NodeConfig.NodeConfigBuilder("testNode", solrHome).build());
    coreContainer.load();
    assertEquals(List.of("collection1"), coreContainer.getLoadedCoreNames());
  }

  @AfterClass
  public static void afterClass() throws Exception {
    if (coreContainer != null) {
      coreContainer.shutdown();
      coreContainer = null;
    }
    System.clearProperty("solr.directoryFactory");
    cleanupThreadNames.clear();
    closeEvents.clear();
  }

  @Test
  public void testCleanupOnCloseIsNotBackgrounded() throws Exception {
    // let the background cleanup started while the core was being created finish first
    for (Thread t : Thread.getAllStackTraces().keySet()) {
      if (t.getName().startsWith(CLEANUP_THREAD_PREFIX)) {
        t.join(10_000);
      }
    }
    cleanupThreadNames.clear();
    closeEvents.clear();

    coreContainer.unload("collection1"); // closes the core

    // closing is asynchronous; wait for the factory close and the cleanup to be recorded
    long deadlineNanos = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
    while (!closeEvents.contains("factory-close") && System.nanoTime() < deadlineNanos) {
      Thread.sleep(100);
    }
    // On a correct tree the cleanup completes before the factory closes, so its
    // completion is already recorded; a backgrounded cleanup gets a bounded chance
    // to finish so the recorded order shows the race instead of a missing event.
    long doneDeadlineNanos = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
    while (!closeEvents.contains("cleanup-done") && System.nanoTime() < doneDeadlineNanos) {
      Thread.sleep(100);
    }

    assertFalse("cleanup should run when the core is closed", cleanupThreadNames.isEmpty());

    int cleanupDone = closeEvents.indexOf("cleanup-done");
    int factoryClose = closeEvents.indexOf("factory-close");
    assertTrue(
        "the DirectoryFactory should be closed when the core is closed, events: " + closeEvents,
        factoryClose >= 0);
    assertTrue(
        "cleanup should complete when the core is closed, events: " + closeEvents,
        cleanupDone >= 0);
    assertTrue(
        "old index directory cleanup must complete before the DirectoryFactory is closed, events: "
            + closeEvents,
        cleanupDone < factoryClose);

    for (String threadName : cleanupThreadNames) {
      assertFalse(
          "cleanup at close must not race the DirectoryFactory close in a background thread: "
              + threadName,
          threadName.startsWith(CLEANUP_THREAD_PREFIX));
    }
  }
}
