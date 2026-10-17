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
package org.apache.solr.handler;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FilterDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.solr.SolrTestCase;
import org.junit.Test;

/** Tests the file-existence check used before IndexFetcher moves replicated files. */
public class IndexFetcherTest extends SolrTestCase {

  private static final MethodHandle SLOW_FILE_EXISTS = createSlowFileExistsHandle();

  @Test
  public void testSlowFileExistsUsesLengthWithoutOpeningExistingFile() throws Exception {
    try (TrackingDirectory dir = new TrackingDirectory(newDirectory())) {
      try (IndexOutput output = dir.createOutput("present", IOContext.DEFAULT)) {
        output.writeByte((byte) 1);
      }
      dir.resetCalls();

      assertTrue(slowFileExists(dir, "present"));
      assertEquals(0, dir.openInputCalls);
      assertEquals(1, dir.fileLengthCalls);
    }
  }

  @Test
  public void testSlowFileExistsTreatsEmptyFileAsPresent() throws Exception {
    try (TrackingDirectory dir = new TrackingDirectory(newDirectory())) {
      try (IndexOutput output = dir.createOutput("empty", IOContext.DEFAULT)) {
        assertEquals(0, output.getFilePointer());
      }
      dir.resetCalls();

      assertTrue(slowFileExists(dir, "empty"));
      assertEquals(0, dir.openInputCalls);
      assertEquals(1, dir.fileLengthCalls);
    }
  }

  @Test
  public void testSlowFileExistsReturnsFalseForMissingFile() throws Exception {
    try (TrackingDirectory dir = new TrackingDirectory(newDirectory())) {
      assertFalse(slowFileExists(dir, "missing"));
      assertEquals(0, dir.openInputCalls);
      assertEquals(1, dir.fileLengthCalls);
    }
  }

  private static MethodHandle createSlowFileExistsHandle() {
    try {
      MethodHandles.Lookup lookup =
          MethodHandles.privateLookupIn(IndexFetcher.class, MethodHandles.lookup());
      return lookup.findStatic(
          IndexFetcher.class,
          "slowFileExists",
          MethodType.methodType(boolean.class, Directory.class, String.class));
    } catch (ReflectiveOperationException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private static boolean slowFileExists(Directory dir, String fileName) throws Exception {
    try {
      return (boolean) SLOW_FILE_EXISTS.invoke(dir, fileName);
    } catch (Throwable t) {
      if (t instanceof Exception e) throw e;
      if (t instanceof Error e) throw e;
      throw new RuntimeException(t);
    }
  }

  private static class TrackingDirectory extends FilterDirectory {
    private int openInputCalls;
    private int fileLengthCalls;

    TrackingDirectory(Directory in) {
      super(in);
    }

    void resetCalls() {
      openInputCalls = 0;
      fileLengthCalls = 0;
    }

    @Override
    public IndexInput openInput(String name, IOContext context) throws IOException {
      openInputCalls++;
      return super.openInput(name, context);
    }

    @Override
    public long fileLength(String name) throws IOException {
      fileLengthCalls++;
      return super.fileLength(name);
    }
  }
}
