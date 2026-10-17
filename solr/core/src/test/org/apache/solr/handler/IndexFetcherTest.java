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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FilterDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.MMapDirectory;
import org.apache.solr.SolrTestCase;
import org.junit.Assume;
import org.junit.Test;

/** Tests the file-existence check used before IndexFetcher moves replicated files. */
public class IndexFetcherTest extends SolrTestCase {

  private static final MethodHandle SLOW_FILE_EXISTS = createSlowFileExistsHandle();

  @Test
  public void testExistingFileIsCheckedWithFileLengthOnly() throws Exception {
    for (Directory base : directories()) {
      try (TrackingDirectory dir = new TrackingDirectory(base)) {
        writeFile(dir, "present", 1);
        dir.resetCalls();

        assertTrue(invokeSlowFileExists(dir, "present"));
        assertEquals(List.of("fileLength:present"), dir.calls);
      }
    }
  }

  @Test
  public void testEmptyFileIsCheckedWithFileLengthOnly() throws Exception {
    for (Directory base : directories()) {
      try (TrackingDirectory dir = new TrackingDirectory(base)) {
        writeFile(dir, "empty", 0);
        dir.resetCalls();

        assertTrue(invokeSlowFileExists(dir, "empty"));
        assertEquals(List.of("fileLength:empty"), dir.calls);
      }
    }
  }

  @Test
  public void testMissingFileIsCheckedWithFileLengthOnly() throws Exception {
    for (Directory base : directories()) {
      try (TrackingDirectory dir = new TrackingDirectory(base)) {
        assertFalse(invokeSlowFileExists(dir, "missing"));
        assertEquals(List.of("fileLength:missing"), dir.calls);
      }
    }
  }

  @Test
  public void testExistenceCheckLeavesNoMappingWhileReaderOpen() throws Exception {
    Assume.assumeTrue("needs /proc/self/maps", Files.isReadable(Path.of("/proc/self/maps")));
    Path indexPath = createTempDir();
    try (MMapDirectory dir = new MMapDirectory(indexPath)) {
      writeIndex(dir);
      String segmentInfo = segmentInfoFile(dir);
      Path file = indexPath.resolve(segmentInfo).toRealPath();

      try (DirectoryReader reader = DirectoryReader.open(dir)) {
        assertEquals(1, reader.leaves().size());
        long before = mappedBytes(file);
        for (int i = 0; i < 50; i++) {
          assertTrue(invokeSlowFileExists(dir, segmentInfo));
        }
        assertEquals(before, mappedBytes(file));
      }
      assertEquals(0, mappedBytes(file));
    }
  }

  private static void writeIndex(Directory dir) throws IOException {
    try (IndexWriter writer =
        new IndexWriter(dir, new IndexWriterConfig().setUseCompoundFile(false))) {
      Document doc = new Document();
      doc.add(new StringField("id", "1", Field.Store.NO));
      writer.addDocument(doc);
      writer.commit();
    }
  }

  private static String segmentInfoFile(Directory dir) throws IOException {
    for (String name : dir.listAll()) {
      if (name.endsWith(".si")) {
        return name;
      }
    }
    throw new AssertionError("no segment info file in index");
  }

  private static long mappedBytes(Path file) throws IOException {
    String path = file.toString();
    long total = 0;
    for (String line : Files.readAllLines(Path.of("/proc/self/maps"))) {
      if (!line.endsWith(path)) {
        continue;
      }
      String[] range = line.split(" ")[0].split("-");
      total += Long.parseUnsignedLong(range[1], 16) - Long.parseUnsignedLong(range[0], 16);
    }
    return total;
  }

  private static List<Directory> directories() throws IOException {
    List<Directory> directories = new ArrayList<>();
    directories.add(newDirectory());
    directories.add(new MMapDirectory(createTempDir()));
    return directories;
  }

  private static void writeFile(Directory dir, String name, int length) throws IOException {
    try (IndexOutput output = dir.createOutput(name, IOContext.DEFAULT)) {
      for (int i = 0; i < length; i++) {
        output.writeByte((byte) 1);
      }
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

  private static boolean invokeSlowFileExists(Directory dir, String fileName) throws Exception {
    try {
      return (boolean) SLOW_FILE_EXISTS.invoke(dir, fileName);
    } catch (Throwable t) {
      if (t instanceof Exception e) throw e;
      if (t instanceof Error e) throw e;
      throw new RuntimeException(t);
    }
  }

  private static class TrackingDirectory extends FilterDirectory {
    private final List<String> calls = new ArrayList<>();

    TrackingDirectory(Directory in) {
      super(in);
    }

    void resetCalls() {
      calls.clear();
    }

    @Override
    public IndexInput openInput(String name, IOContext context) throws IOException {
      calls.add("openInput:" + name);
      return super.openInput(name, context);
    }

    @Override
    public long fileLength(String name) throws IOException {
      calls.add("fileLength:" + name);
      return super.fileLength(name);
    }
  }
}
