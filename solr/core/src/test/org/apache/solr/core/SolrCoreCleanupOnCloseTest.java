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

  private static CoreContainer coreContainer;

  /** Records which thread asked the factory to clean up old index directories. */
  public static class RecordingDirectoryFactory extends MockDirectoryFactory {
    @Override
    public void cleanupOldIndexDirectories(
        String dataDirPath, String currentIndexDirPath, boolean afterCoreReload) {
      cleanupThreadNames.add(Thread.currentThread().getName());
      super.cleanupOldIndexDirectories(dataDirPath, currentIndexDirPath, afterCoreReload);
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

    coreContainer.unload("collection1"); // closes the core

    // closing is asynchronous; wait for the close-time cleanup to be recorded
    long deadlineNanos = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
    while (cleanupThreadNames.isEmpty() && System.nanoTime() < deadlineNanos) {
      Thread.sleep(100);
    }

    assertFalse("cleanup should run when the core is closed", cleanupThreadNames.isEmpty());
    for (String threadName : cleanupThreadNames) {
      assertFalse(
          "cleanup at close must not race the DirectoryFactory close in a background thread: "
              + threadName,
          threadName.startsWith(CLEANUP_THREAD_PREFIX));
    }
  }
}
