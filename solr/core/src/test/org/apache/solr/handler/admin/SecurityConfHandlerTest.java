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

import static org.apache.solr.handler.admin.SecurityConfHandler.SecurityConfig;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.common.SolrErrorWrappingException;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.CommandOperation;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.Utils;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.security.BasicAuthPlugin;
import org.apache.solr.security.RuleBasedAuthorizationPlugin;

public class SecurityConfHandlerTest extends SolrTestCaseJ4 {

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void testEdit() throws Exception {
    MockSecurityHandler handler = new MockSecurityHandler();
    String command =
        "{\n"
            + "'set-user': {'tom':'TomIsCool'},\n"
            + "'set-user':{ 'tom':'TomIsUberCool'}\n"
            + "}";
    SolrQueryRequestBase req = new SolrQueryRequestBase(null, new ModifiableSolrParams());
    req.getContext().put("httpMethod", SolrRequest.METHOD.POST);
    req.getContext().put("path", "/admin/authentication");
    ContentStreamBase.ByteArrayStream o =
        new ContentStreamBase.ByteArrayStream(command.getBytes(StandardCharsets.UTF_8), "");
    req.setContentStreams(List.of(o));
    handler.handleRequestBody(req, new SolrQueryResponse());

    try (BasicAuthPlugin basicAuth = new BasicAuthPlugin()) {
      SecurityConfig securityCfg = handler.m.get("/security.json");
      basicAuth.init((Map<String, Object>) securityCfg.getData().get("authentication"));
      assertTrue(basicAuth.authenticate("tom", "TomIsUberCool"));

      command = "{\n" + "'set-user': {'harry':'HarryIsCool'},\n" + "'delete-user': ['tom']\n" + "}";
      o = new ContentStreamBase.ByteArrayStream(command.getBytes(StandardCharsets.UTF_8), "");
      req.setContentStreams(List.of(o));
      handler.handleRequestBody(req, new SolrQueryResponse());
      securityCfg = handler.m.get("/security.json");
      assertEquals(3, securityCfg.getVersion());
      Map result = (Map) securityCfg.getData().get("authentication");
      result = (Map) result.get("credentials");
      assertEquals(1, result.size());
    }

    command =
        "{'set-permission':{ collection : acoll ,\n"
            + "                      path : '/nonexistentpath',\n"
            + "                      role :guest },\n"
            + "'set-user-role': { 'tom': ['admin','dev']},"
            + "'set-permission':{'name': 'security-edit',\n"
            + "                  'role': 'admin'}\n"
            + "}";

    req = new SolrQueryRequestBase(null, new ModifiableSolrParams());
    req.getContext().put("httpMethod", SolrRequest.METHOD.POST);
    req.getContext().put("path", "/admin/authorization");
    o = new ContentStreamBase.ByteArrayStream(command.getBytes(StandardCharsets.UTF_8), "");
    req.setContentStreams(List.of(o));
    SolrQueryResponse rsp = new SolrQueryResponse();
    handler.handleRequestBody(req, rsp);
    assertNull(rsp.getValues().get(CommandOperation.ERR_MSGS));
    Map authzconf = (Map) handler.m.get("/security.json").getData().get("authorization");
    Map userRoles = (Map) authzconf.get("user-role");
    List tomRoles = (List) userRoles.get("tom");
    assertTrue(tomRoles.contains("admin"));
    assertTrue(tomRoles.contains("dev"));
    List<Map> permissions = (List<Map>) authzconf.get("permissions");
    assertEquals(2, permissions.size());
    for (Map p : permissions) {
      assertEquals("acoll", p.get("collection"));
      break;
    }
    command =
        "{\n"
            + "'set-permission':{index : 2,  name : security-edit,\n"
            + "                  'role': ['admin','dev']\n"
            + "                  }}";
    req = new SolrQueryRequestBase(null, new ModifiableSolrParams());
    req.getContext().put("httpMethod", SolrRequest.METHOD.POST);
    req.getContext().put("path", "/admin/authorization");
    o = new ContentStreamBase.ByteArrayStream(command.getBytes(StandardCharsets.UTF_8), "");
    req.setContentStreams(List.of(o));
    rsp = new SolrQueryResponse();
    handler.handleRequestBody(req, rsp);
    authzconf = (Map) handler.m.get("/security.json").getData().get("authorization");
    permissions = (List<Map>) authzconf.get("permissions");

    Map p = permissions.get(1);
    assertEquals("security-edit", p.get("name"));
    List rol = (List) p.get("role");
    assertEquals("admin", rol.get(0));
    assertEquals("dev", rol.get(1));

    command =
        "{\n"
            + "'update-permission':{'index': 1,\n"
            + "                  'role': ['guest','admin']\n"
            + "                  }}";
    req = new SolrQueryRequestBase(null, new ModifiableSolrParams());
    req.getContext().put("httpMethod", SolrRequest.METHOD.POST);
    req.getContext().put("path", "/admin/authorization");
    o = new ContentStreamBase.ByteArrayStream(command.getBytes(StandardCharsets.UTF_8), "");
    req.setContentStreams(List.of(o));
    rsp = new SolrQueryResponse();
    handler.handleRequestBody(req, rsp);
    authzconf = (Map) handler.m.get("/security.json").getData().get("authorization");
    permissions = (List<Map>) authzconf.get("permissions");

    p = permissions.get(0);
    assertEquals("acoll", p.get("collection"));
    rol = (List) p.get("role");
    assertEquals("guest", rol.get(0));
    assertEquals("admin", rol.get(1));

    command = "{\n" + "delete-permission: 1,\n" + " set-user-role : { tom :null}\n" + "}";
    req = new SolrQueryRequestBase(null, new ModifiableSolrParams());
    req.getContext().put("httpMethod", SolrRequest.METHOD.POST);
    req.getContext().put("path", "/admin/authorization");
    o = new ContentStreamBase.ByteArrayStream(command.getBytes(StandardCharsets.UTF_8), "");
    req.setContentStreams(List.of(o));
    rsp = new SolrQueryResponse();
    handler.handleRequestBody(req, rsp);
    assertNull(rsp.getValues().get(CommandOperation.ERR_MSGS));
    authzconf = (Map) handler.m.get("/security.json").getData().get("authorization");
    userRoles = (Map) authzconf.get("user-role");
    assertEquals(0, userRoles.size());
    permissions = (List<Map>) authzconf.get("permissions");
    assertEquals(1, permissions.size());

    for (Map permission : permissions) {
      assertNotEquals("some-permission", permission.get("name"));
    }
    // -ve test security edit is a well-known permission, only role attribute should be provided
    command =
        "{\n"
            + "'set-permission':{index : 2,  'name': 'security-edit',\n"
            + "                  'method':'POST',"
            + "                  'role': 'admin'\n"
            + "                  }}";
    final SolrQueryRequestBase badReq = new SolrQueryRequestBase(null, new ModifiableSolrParams());
    badReq.getContext().put("httpMethod", SolrRequest.METHOD.POST);
    badReq.getContext().put("path", "/admin/authorization");
    o = new ContentStreamBase.ByteArrayStream(command.getBytes(StandardCharsets.UTF_8), "");
    badReq.setContentStreams(List.of(o));
    final SolrQueryResponse badRsp = new SolrQueryResponse();
    SolrErrorWrappingException ex =
        assertThrows(
            SolrErrorWrappingException.class, () -> handler.handleRequestBody(badReq, badRsp));
    assertEquals(SolrException.ErrorCode.BAD_REQUEST.code, ex.code());
    assertTrue(ex.getMessage().contains("method is not a valid key for the permission"));
    handler.close();
  }

  /**
   * Two edits to the standalone security.json that overlap in time must both survive: each edit
   * reads the file, applies its commands and rewrites the file, so an edit that starts from the
   * same content as another one overwrites it. The first edit is held open inside the plugin while
   * the second edit is issued, which is the interleave a caller produces by sending two edit
   * requests without waiting for the first response (SOLR-18010).
   */
  @SuppressWarnings({"unchecked"})
  /**
   * Two overlapping writes of security.json must never leave a torn file behind: the document on
   * disk afterwards is always exactly one of the written documents, never the beginning of one
   * followed by the tail of a longer one (the corruption shape in SOLR-18010). The first write is
   * held open inside serialization while the second write runs, which is the interleave that
   * produced that shape.
   */
  public void testConcurrentPersistConfLeavesOneWholeDocument() throws Exception {
    assumeWorkingMockito();
    Path solrHome = createTempDir("securityConfHandlerLocalPersist");
    Path securityJson = solrHome.resolve("security.json");
    CoreContainer cc = mock(CoreContainer.class);
    when(cc.getSolrHome()).thenReturn(solrHome);
    SecurityConfHandlerLocalForTesting handler = new SecurityConfHandlerLocalForTesting(cc);

    byte[] shortBytes = Utils.toJSON(shortSecurityData("x"));
    byte[] longBytes = Utils.toJSON(longSecurityData());
    Files.write(securityJson, Utils.toJSON(shortSecurityData("seed")));

    CountDownLatch entered = new CountDownLatch(1);
    CountDownLatch release = new CountDownLatch(1);
    AtomicReference<Throwable> firstErr = new AtomicReference<>();
    Thread first =
        new Thread(
            () -> {
              try {
                handler.persistConf(
                    new SecurityConfig()
                        .setData(shortSecurityData(new LatchingValue("x", entered, release))));
              } catch (Throwable t) {
                firstErr.set(t);
              }
            });
    first.start();
    assertTrue("first write reached serialization", entered.await(60, TimeUnit.SECONDS));
    handler.persistConf(new SecurityConfig().setData(longSecurityData()));
    release.countDown();
    first.join(TimeUnit.SECONDS.toMillis(60));
    assertFalse("first write still running", first.isAlive());
    assertNull("first write failed: " + firstErr.get(), firstErr.get());

    byte[] fileBytes = Files.readAllBytes(securityJson);
    assertSingleJsonDocument(fileBytes);
    assertTrue(
        "security.json holds neither written document ("
            + fileBytes.length
            + " bytes; the writes were "
            + shortBytes.length
            + " and "
            + longBytes.length
            + " bytes)",
        Arrays.equals(fileBytes, shortBytes) || Arrays.equals(fileBytes, longBytes));
  }

  /** A reader sees the old document until the temporary file has been synced and is moved. */
  public void testReaderSeesOldDocumentUntilTemporaryFileIsMoved() throws Exception {
    assumeWorkingMockito();
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

  /** An edit keeps the POSIX permissions of the existing file. */
  public void testEditKeepsExistingSecurityJsonPermissions() throws Exception {
    assumeWorkingMockito();
    Path solrHome = createTempDir("securityConfHandlerLocalPerms");
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
    SecurityConfHandlerLocal handler = new SecurityConfHandlerLocal(mockCoreContainer(solrHome));
    postAuthorizationEdit(handler, "{'set-user-role': {'alice': 'admin'}}");
    assertTrue(Files.readString(securityJson, StandardCharsets.UTF_8).contains("alice"));
    assertEquals(expected, Files.getPosixFilePermissions(securityJson));
    assertNoTemporaryFiles(solrHome);
  }

  /** A read-only SOLR_HOME still allows an edit when security.json itself is writable. */
  public void testEditInPlaceWhenSolrHomeIsReadOnly() throws Exception {
    assumeWorkingMockito();
    Path solrHome = createTempDir("securityConfHandlerLocalReadOnly");
    assumeTrue(
        "POSIX file permissions are not supported on this file system",
        solrHome.getFileSystem().supportedFileAttributeViews().contains("posix"));
    Path securityJson = solrHome.resolve("security.json");
    Files.write(securityJson, seedSecurityJson().getBytes(StandardCharsets.UTF_8));
    Files.setPosixFilePermissions(
        solrHome, EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_EXECUTE));
    try {
      assumeFalse("the directory is writable for this user", Files.isWritable(solrHome));
      SecurityConfHandlerLocal handler = new SecurityConfHandlerLocal(mockCoreContainer(solrHome));
      postAuthorizationEdit(handler, "{'set-user-role': {'alice': 'admin'}}");
      assertTrue(Files.readString(securityJson, StandardCharsets.UTF_8).contains("alice"));
    } finally {
      Files.setPosixFilePermissions(
          solrHome,
          EnumSet.of(
              PosixFilePermission.OWNER_READ,
              PosixFilePermission.OWNER_WRITE,
              PosixFilePermission.OWNER_EXECUTE));
    }
  }

  private static String seedSecurityJson() {
    return "{\"authentication\":{\"class\":\"solr.BasicAuthPlugin\","
        + "\"credentials\":{\"solr\":\"solr\"}},"
        + "\"authorization\":{\"class\":\"solr.RuleBasedAuthorizationPlugin\",\"user-role\":{}}}";
  }

  private static CoreContainer mockCoreContainer(Path solrHome) {
    CoreContainer cc = mock(CoreContainer.class);
    when(cc.getSolrHome()).thenReturn(solrHome);
    when(cc.getAuthorizationPlugin()).thenReturn(new RuleBasedAuthorizationPlugin());
    return cc;
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

  private static void assertSingleJsonDocument(byte[] fileBytes) {
    ObjectMapper mapper =
        JsonMapper.builder().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS).build();
    try {
      mapper.readValue(fileBytes, Map.class);
    } catch (Exception e) {
      fail("security.json is not a single JSON document: " + e);
    }
  }

  private static Map<String, Object> shortSecurityData(CharSequence credValue) {
    Map<String, Object> creds = new LinkedHashMap<>();
    creds.put("u", credValue);
    Map<String, Object> authc = new LinkedHashMap<>();
    authc.put("class", "solr.BasicAuthPlugin");
    authc.put("credentials", creds);
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("authentication", authc);
    return data;
  }

  private static Map<String, Object> longSecurityData() {
    StringBuilder pad = new StringBuilder();
    for (int i = 0; i < 6000; i++) {
      pad.append('L');
    }
    Map<String, Object> creds = new LinkedHashMap<>();
    creds.put("longuser", pad.toString());
    Map<String, Object> authc = new LinkedHashMap<>();
    authc.put("class", "solr.BasicAuthPlugin");
    authc.put("credentials", creds);
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("authentication", authc);
    return data;
  }

  /** An authorization plugin whose first {@code edit} call blocks until the test releases it. */
  /**
   * A {@link CharSequence} whose first read blocks until released. Serialization evaluates it, so a
   * write whose data carries one is held open at a known point inside the write.
   */
  private static final class LatchingValue implements CharSequence {
    private final String delegate;
    private final CountDownLatch entered;
    private final CountDownLatch release;
    private final AtomicBoolean armed = new AtomicBoolean(true);

    LatchingValue(String delegate, CountDownLatch entered, CountDownLatch release) {
      this.delegate = delegate;
      this.entered = entered;
      this.release = release;
    }

    private void gate() {
      if (armed.compareAndSet(true, false)) {
        entered.countDown();
        try {
          release.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    }

    @Override
    public int length() {
      gate();
      return delegate.length();
    }

    @Override
    public char charAt(int index) {
      gate();
      return delegate.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
      gate();
      return delegate.subSequence(start, end);
    }

    @Override
    public String toString() {
      gate();
      return delegate;
    }
  }

  public static class MockSecurityHandler extends SecurityConfHandler {
    private Map<String, SecurityConfig> m;
    final BasicAuthPlugin basicAuthPlugin = new BasicAuthPlugin();
    final RuleBasedAuthorizationPlugin rulesBasedAuthorizationPlugin =
        new RuleBasedAuthorizationPlugin();

    public MockSecurityHandler() {
      super(null);
      m = new HashMap<>();
      SecurityConfig sp = new SecurityConfig();
      Map<String, Object> securityData = new HashMap<>();
      securityData.put(
          "authentication", Map.of("class", "solr." + BasicAuthPlugin.class.getSimpleName()));
      securityData.put(
          "authorization",
          Map.of("class", "solr." + RuleBasedAuthorizationPlugin.class.getSimpleName()));
      sp.setVersion(1);
      sp.setData(securityData);
      m.put("/security.json", sp);

      basicAuthPlugin.init(Map.of("credentials", Map.of("ignore", "me")));

      rulesBasedAuthorizationPlugin.init(new HashMap<>());
    }

    @Override
    Object getPlugin(String key) {
      if (key.equals("authentication")) {
        return basicAuthPlugin;
      }
      if (key.equals("authorization")) {
        return rulesBasedAuthorizationPlugin;
      }
      return null;
    }

    @Override
    protected void getConf(SolrQueryResponse rsp, String key) {
      // NOP
    }

    @Override
    public SecurityConfig getSecurityConfig(boolean getFresh) {
      return m.get("/security.json");
    }

    @Override
    protected boolean persistConf(SecurityConfig props) {
      SecurityConfig fromMap = m.get("/security.json");
      if (fromMap.getVersion() == props.getVersion()) {
        props.setVersion(props.getVersion() + 1);
        m.put("/security.json", props);
        return true;
      } else {
        return false;
      }
    }

    public String getStandardJson() throws Exception {
      String command = "{\n" + "'set-user': {'solr':'SolrRocks'}\n" + "}";
      SolrQueryRequestBase req = new SolrQueryRequestBase(null, new ModifiableSolrParams());
      req.getContext().put("httpMethod", SolrRequest.METHOD.POST);
      req.getContext().put("path", "/admin/authentication");
      ContentStreamBase.ByteArrayStream o =
          new ContentStreamBase.ByteArrayStream(command.getBytes(StandardCharsets.UTF_8), "");
      req.setContentStreams(List.of(o));
      handleRequestBody(req, new SolrQueryResponse());

      command =
          "{'set-user-role': { 'solr': 'admin'},\n"
              + "'set-permission':{'name': 'security-edit', 'role': 'admin'}"
              + "}";
      req = new SolrQueryRequestBase(null, new ModifiableSolrParams());
      req.getContext().put("httpMethod", SolrRequest.METHOD.POST);
      req.getContext().put("path", "/admin/authorization");
      o = new ContentStreamBase.ByteArrayStream(command.getBytes(StandardCharsets.UTF_8), "");
      req.setContentStreams(List.of(o));
      SolrQueryResponse rsp = new SolrQueryResponse();
      handleRequestBody(req, rsp);
      Map<String, Object> data = m.get("/security.json").getData();
      ((Map) data.get("authentication")).remove("");
      ((Map) data.get("authorization")).remove("");
      return Utils.toJSONString(data);
    }
  }

  public static void main(String[] args) throws Exception {
    try (MockSecurityHandler msh = new MockSecurityHandler()) {
      System.out.println(msh.getStandardJson());
    }
  }
}
