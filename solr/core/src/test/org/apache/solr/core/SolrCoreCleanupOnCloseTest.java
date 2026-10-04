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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.solr.SolrTestCaseJ4;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/** SOLR-12007: old index directories are cleaned up before the DirectoryFactory is closed. */
public class SolrCoreCleanupOnCloseTest extends SolrTestCaseJ4 {

  private static final String CLEANUP_THREAD_PREFIX = "OldIndexDirectoryCleanupThreadForCore-";

  private static final List<String> cleanupThreadNames = new CopyOnWriteArrayList<>();

  /** Records which thread asked the factory to clean up old index directories. */
  public static class RecordingDirectoryFactory extends MockDirectoryFactory {
    @Override
    public void cleanupOldIndexDirectories(
        String dataDirPath, String currentIndexDirPath, boolean afterCoreReload)
        throws IOException {
      cleanupThreadNames.add(Thread.currentThread().getName());
      super.cleanupOldIndexDirectories(dataDirPath, currentIndexDirPath, afterCoreReload);
    }
  }

  @BeforeClass
  public static void beforeClass() throws Exception {
    System.setProperty("solr.directoryFactory", RecordingDirectoryFactory.class.getName());
    initCore("solrconfig.xml", "schema.xml");
  }

  @AfterClass
  public static void afterClass() {
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

    deleteCore(); // closes the core

    assertFalse("cleanup should run when the core is closed", cleanupThreadNames.isEmpty());
    for (String threadName : cleanupThreadNames) {
      assertFalse(
          "cleanup at close must not race the DirectoryFactory close in a background thread: "
              + threadName,
          threadName.startsWith(CLEANUP_THREAD_PREFIX));
    }
  }
}
