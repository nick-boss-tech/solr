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
package org.apache.solr.handler.admin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.security.RuleBasedAuthorizationPlugin;

/** Tests for the temporary-file write path, which uses package-private hooks to force each step. */
public class SecurityConfHandlerLocalSeamTest extends SolrTestCaseJ4 {

  /** A reader sees the old document until the temporary file has been synced and is moved. */
  public void testReaderSeesOldDocumentUntilTemporaryFileIsMoved() throws Exception {
    assumeWorkingMockito();
    assumeAtomicReplaceSupported();
    Path solrHome = createTempDir("securityConfHandlerLocalAtomic");
    Path securityJson = solrHome.resolve("security.json");
    byte[] oldBytes = seedSecurityJson().getBytes(StandardCharsets.UTF_8);
    Files.write(securityJson, oldBytes);
    HookedSecurityConfHandler handler = new HookedSecurityConfHandler(mockCoreContainer(solrHome));
    CountDownLatch entered = new CountDownLatch(1);
    CountDownLatch release = new CountDownLatch(1);
    handler.entered = entered;
    handler.release = release;
    AtomicReference<Throwable> editErr = new AtomicReference<>();
    Thread edit = new Thread(() -> runEdit(handler, editErr));
    edit.start();
    assertTrue("the edit did not reach the move", entered.await(30, TimeUnit.SECONDS));
    assertArrayEquals(
        "a reader must see the old document until the move",
        oldBytes,
        Files.readAllBytes(securityJson));
    release.countDown();
    edit.join(TimeUnit.SECONDS.toMillis(60));
    assertFalse("edit still running", edit.isAlive());
    assertNull("edit failed: " + editErr.get(), editErr.get());
    assertTrue(Files.readString(securityJson, StandardCharsets.UTF_8).contains("alice"));
    assertNoTemporaryFiles(solrHome);
  }

  /** The temporary file is synced before it is moved over security.json. */
  public void testTemporaryFileIsSyncedBeforeItIsMoved() throws Exception {
    assumeWorkingMockito();
    assumeAtomicReplaceSupported();
    Path solrHome = createTempDir("securityConfHandlerLocalSyncOrder");
    Files.write(
        solrHome.resolve("security.json"), seedSecurityJson().getBytes(StandardCharsets.UTF_8));
    HookedSecurityConfHandler handler = new HookedSecurityConfHandler(mockCoreContainer(solrHome));
    postAuthorizationEdit(handler, "{'set-user-role': {'alice': 'admin'}}");
    assertEquals(List.of("sync", "move-attempt", "move", "dirSync"), handler.events);
  }

  /** A move that the file system refuses falls back to the in-place write. */
  public void testRefusedMoveFallsBackToInPlaceWrite() throws Exception {
    assumeWorkingMockito();
    assumeAtomicReplaceSupported();
    Path solrHome = createTempDir("securityConfHandlerLocalRefused");
    Path securityJson = solrHome.resolve("security.json");
    Files.write(securityJson, seedSecurityJson().getBytes(StandardCharsets.UTF_8));
    HookedSecurityConfHandler handler = new HookedSecurityConfHandler(mockCoreContainer(solrHome));
    handler.failMove.set(true);
    postAuthorizationEdit(handler, "{'set-user-role': {'alice': 'admin'}}");
    assertTrue("the move was not attempted", handler.events.contains("move-attempt"));
    assertTrue(Files.readString(securityJson, StandardCharsets.UTF_8).contains("alice"));
    assertNoTemporaryFiles(solrHome);
  }

  /**
   * A failed fsync of the temporary file falls back to the in-place write and removes the temporary
   * file.
   */
  public void testFailedSyncFallsBackToInPlaceWrite() throws Exception {
    assumeWorkingMockito();
    assumeAtomicReplaceSupported();
    Path solrHome = createTempDir("securityConfHandlerLocalSyncFail");
    Path securityJson = solrHome.resolve("security.json");
    Files.write(securityJson, seedSecurityJson().getBytes(StandardCharsets.UTF_8));
    HookedSecurityConfHandler handler = new HookedSecurityConfHandler(mockCoreContainer(solrHome));
    handler.failSync.set(true);
    postAuthorizationEdit(handler, "{'set-user-role': {'alice': 'admin'}}");
    assertFalse("the file was moved after a failed sync", handler.events.contains("move-attempt"));
    assertTrue(Files.readString(securityJson, StandardCharsets.UTF_8).contains("alice"));
    assertNoTemporaryFiles(solrHome);
  }

  /** A permission copy that fails falls back to the in-place write, which keeps the file mode. */
  public void testFailedPermissionCopyFallsBackAndKeepsMode() throws Exception {
    assumeWorkingMockito();
    Path solrHome = createTempDir("securityConfHandlerLocalPermCopy");
    assumeTrue(
        "POSIX file permissions are not supported on this file system",
        solrHome.getFileSystem().supportedFileAttributeViews().contains("posix"));
    Path securityJson = solrHome.resolve("security.json");
    Files.write(securityJson, seedSecurityJson().getBytes(StandardCharsets.UTF_8));
    EnumSet<PosixFilePermission> expected =
        EnumSet.of(
            PosixFilePermission.OWNER_READ,
            PosixFilePermission.OWNER_WRITE,
            PosixFilePermission.GROUP_READ);
    Files.setPosixFilePermissions(securityJson, expected);
    HookedSecurityConfHandler handler = new HookedSecurityConfHandler(mockCoreContainer(solrHome));
    handler.failPermissionCopy.set(true);
    postAuthorizationEdit(handler, "{'set-user-role': {'alice': 'admin'}}");
    assertTrue(Files.readString(securityJson, StandardCharsets.UTF_8).contains("alice"));
    assertEquals(expected, Files.getPosixFilePermissions(securityJson));
    assertNoTemporaryFiles(solrHome);
  }

  /** An owner mismatch falls back to the in-place write and does not fail the edit. */
  public void testOwnerMismatchFallsBackToInPlaceWrite() throws Exception {
    assumeWorkingMockito();
    assumeAtomicReplaceSupported();
    Path solrHome = createTempDir("securityConfHandlerLocalOwner");
    Path securityJson = solrHome.resolve("security.json");
    Files.write(securityJson, seedSecurityJson().getBytes(StandardCharsets.UTF_8));
    HookedSecurityConfHandler handler = new HookedSecurityConfHandler(mockCoreContainer(solrHome));
    handler.failOwnerCheck.set(true);
    postAuthorizationEdit(handler, "{'set-user-role': {'alice': 'admin'}}");
    assertFalse(
        "the file was moved after an owner mismatch", handler.events.contains("move-attempt"));
    assertTrue(Files.readString(securityJson, StandardCharsets.UTF_8).contains("alice"));
    assertNoTemporaryFiles(solrHome);
  }

  /** A failed directory sync after a successful move does not fail the edit. */
  public void testDirectorySyncFailureAfterMoveDoesNotFailEdit() throws Exception {
    assumeWorkingMockito();
    assumeAtomicReplaceSupported();
    Path solrHome = createTempDir("securityConfHandlerLocalDirSync");
    Path securityJson = solrHome.resolve("security.json");
    Files.write(securityJson, seedSecurityJson().getBytes(StandardCharsets.UTF_8));
    HookedSecurityConfHandler handler = new HookedSecurityConfHandler(mockCoreContainer(solrHome));
    handler.failDirectorySync.set(true);
    postAuthorizationEdit(handler, "{'set-user-role': {'alice': 'admin'}}");
    assertTrue(handler.events.contains("dirSync"));
    assertTrue(Files.readString(securityJson, StandardCharsets.UTF_8).contains("alice"));
    assertNoTemporaryFiles(solrHome);
  }

  /** Where the file system cannot replace a file atomically, the edit is written in place. */
  public void testNonPosixFileSystemWritesInPlace() throws Exception {
    assumeWorkingMockito();
    Path solrHome = createTempDir("securityConfHandlerLocalNonPosix");
    Path securityJson = solrHome.resolve("security.json");
    Files.write(securityJson, seedSecurityJson().getBytes(StandardCharsets.UTF_8));
    HookedSecurityConfHandler handler = new HookedSecurityConfHandler(mockCoreContainer(solrHome));
    handler.atomicReplaceSupported.set(false);
    postAuthorizationEdit(handler, "{'set-user-role': {'alice': 'admin'}}");
    assertFalse(
        "the temporary-file path ran on a file system without POSIX views",
        handler.events.contains("sync"));
    assertTrue(Files.readString(securityJson, StandardCharsets.UTF_8).contains("alice"));
    assertNoTemporaryFiles(solrHome);
  }

  /** Atomic replacement needs a POSIX view and a file that is not a symbolic link. */
  public void testAtomicReplaceNeedsPosixViewAndNoSymbolicLink() {
    assertTrue(
        SecurityConfHandlerLocal.supportsAtomicReplace(
            Set.of("basic", "posix", "unix", "owner"), false));
    assertFalse(
        SecurityConfHandlerLocal.supportsAtomicReplace(
            Set.of("basic", "owner", "dos", "acl", "user"), false));
    assertFalse(
        SecurityConfHandlerLocal.supportsAtomicReplace(Set.of("basic", "posix", "owner"), true));
  }

  /** Test seam: each step of the temporary-file path can be made to fail or be held. */
  private static class HookedSecurityConfHandler extends SecurityConfHandlerLocal {
    final AtomicBoolean failSync = new AtomicBoolean();
    final AtomicBoolean failPermissionCopy = new AtomicBoolean();
    final AtomicBoolean failOwnerCheck = new AtomicBoolean();
    final AtomicBoolean failMove = new AtomicBoolean();
    final AtomicBoolean failDirectorySync = new AtomicBoolean();
    final AtomicBoolean atomicReplaceSupported = new AtomicBoolean(true);
    final List<String> events = new CopyOnWriteArrayList<>();
    volatile CountDownLatch entered;
    volatile CountDownLatch release;

    HookedSecurityConfHandler(CoreContainer cc) {
      super(cc);
    }

    @Override
    boolean atomicReplaceSupported() {
      return atomicReplaceSupported.get() && super.atomicReplaceSupported();
    }

    @Override
    void syncFile(Path path) throws IOException {
      events.add("sync");
      if (failSync.get()) {
        throw new IOException("simulated fsync failure");
      }
      super.syncFile(path);
    }

    @Override
    void copyPermissions(Path tmpPath) throws IOException {
      if (failPermissionCopy.get()) {
        throw new IOException("simulated permission copy failure");
      }
      super.copyPermissions(tmpPath);
    }

    @Override
    void checkOwner(Path tmpPath) throws IOException {
      if (failOwnerCheck.get()) {
        throw new IOException("simulated owner mismatch");
      }
      super.checkOwner(tmpPath);
    }

    @Override
    void beforeMove(Path tmpPath) throws IOException {
      CountDownLatch e = entered;
      if (e == null) {
        return;
      }
      e.countDown();
      try {
        if (!release.await(60, TimeUnit.SECONDS)) {
          throw new IOException("release was not signalled");
        }
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        throw new IOException(ie);
      }
    }

    @Override
    void moveTemporaryFile(Path tmpPath) throws IOException {
      events.add("move-attempt");
      if (failMove.get()) {
        throw new IOException("simulated refused move");
      }
      super.moveTemporaryFile(tmpPath);
      events.add("move");
    }

    @Override
    void syncDirectory(Path dir) throws IOException {
      events.add("dirSync");
      if (failDirectorySync.get()) {
        throw new IOException("simulated directory sync failure");
      }
      super.syncDirectory(dir);
    }
  }

  private static void runEdit(SecurityConfHandler handler, AtomicReference<Throwable> err) {
    try {
      postAuthorizationEdit(handler, "{'set-user-role': {'alice': 'admin'}}");
    } catch (Throwable t) {
      err.set(t);
    }
  }

  private static void assertNoTemporaryFiles(Path solrHome) throws IOException {
    try (Stream<Path> files = Files.list(solrHome)) {
      assertEquals(
          "temporary files left behind",
          List.of(),
          files.filter(f -> f.getFileName().toString().endsWith(".tmp")).toList());
    }
  }

  private static CoreContainer mockCoreContainer(Path solrHome) {
    CoreContainer cc = mock(CoreContainer.class);
    when(cc.getSolrHome()).thenReturn(solrHome);
    when(cc.getAuthorizationPlugin()).thenReturn(new RuleBasedAuthorizationPlugin());
    return cc;
  }

  private static String seedSecurityJson() {
    return "{\"authentication\":{\"class\":\"solr.BasicAuthPlugin\","
        + "\"credentials\":{\"solr\":\"solr\"}},"
        + "\"authorization\":{\"class\":\"solr.RuleBasedAuthorizationPlugin\",\"user-role\":{}}}";
  }

  private static void assumeAtomicReplaceSupported() {
    assumeTrue(
        "atomic replacement needs POSIX file attributes",
        FileSystems.getDefault().supportedFileAttributeViews().contains("posix"));
  }

  private static void postAuthorizationEdit(SecurityConfHandler handler, String command)
      throws Exception {
    SolrQueryRequestBase req = new SolrQueryRequestBase(null, new ModifiableSolrParams());
    req.getContext().put("httpMethod", SolrRequest.METHOD.POST);
    req.getContext().put("path", "/admin/authorization");
    req.setContentStreams(
        List.of(
            new ContentStreamBase.ByteArrayStream(command.getBytes(StandardCharsets.UTF_8), "")));
    handler.handleRequestBody(req, new SolrQueryResponse());
  }
}
