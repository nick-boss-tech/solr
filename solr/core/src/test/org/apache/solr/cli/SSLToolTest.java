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

package org.apache.solr.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.KeyStore;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import org.apache.solr.SolrTestCase;
import org.apache.solr.cloud.ZkTestServer;
import org.apache.solr.common.cloud.ClusterProperties;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.util.EnvUtils;
import org.junit.Test;

/** Tests certificate conversion and local HTTPS configuration. */
public class SSLToolTest extends SolrTestCase {
  private Path prepare() throws Exception {
    assumeTrue(
        "External programs require -Ptests.useSecurityManager=false",
        EnvUtils.getProperty("java.security.manager") == null);
    assumeFalse(
        "solr.in.sh requires POSIX file permissions",
        EnvUtils.getProperty("os.name").startsWith("Windows"));
    Path dir = createTempDir();
    Files.writeString(
        dir.resolve("openssl.cnf"),
        "[req]\ndistinguished_name=dn\n[dn]\n[v3]\nsubjectAltName=DNS:localhost,IP:127.0.0.1\n");
    try {
      Process process =
          new ProcessBuilder(
                  "openssl",
                  "req",
                  "-x509",
                  "-newkey",
                  "rsa:2048",
                  "-nodes",
                  "-keyout",
                  dir.resolve("key.pem").toString(),
                  "-out",
                  dir.resolve("cert.pem").toString(),
                  "-days",
                  "2",
                  "-subj",
                  "/CN=localhost",
                  "-config",
                  dir.resolve("openssl.cnf").toString(),
                  "-extensions",
                  "v3")
              .redirectErrorStream(true)
              .redirectOutput(dir.resolve("openssl.log").toFile())
              .start();
      if (!process.waitFor(30, TimeUnit.SECONDS)) {
        process.destroyForcibly().waitFor();
        fail("Test certificate generation timed out");
      }
      assertEquals(Files.readString(dir.resolve("openssl.log")), 0, process.exitValue());
    } catch (IOException missingOpenSSL) {
      assumeNoException("OpenSSL must be installed", missingOpenSSL);
    }
    Files.writeString(dir.resolve("password.txt"), "Test'$(false)Password\n");
    Files.writeString(
        dir.resolve("solr.in.sh"),
        "# Keep this comment\nSOLR_HEAP=512m\nexport SOLR_SSL_ENABLED=false\nSOLR_SSL_KEY_STORE=old\n");
    return dir;
  }

  private SSLTool.SSLParams params(Path dir, String type, String zkHost) {
    return new SSLTool.SSLParams(
        dir.resolve("cert.pem"),
        dir.resolve("key.pem"),
        dir.resolve("password.txt"),
        null,
        dir.resolve("store"),
        type,
        dir.resolve("solr.in.sh"),
        zkHost);
  }

  @Test
  public void testBothParsersAndFormats() throws Exception {
    for (String type : new String[] {"JKS", "PKCS12"}) {
      Path dir = prepare();
      var runtime = new CLITestHelper.TestingRuntime(true);
      String[] args = {
        "--cert",
        dir.resolve("cert.pem").toString(),
        "--key",
        dir.resolve("key.pem").toString(),
        "--password-file",
        dir.resolve("password.txt").toString(),
        "--keystore",
        dir.resolve("store").toString(),
        "--solr-include-file",
        dir.resolve("solr.in.sh").toString(),
        "--store-type",
        type
      };
      SSLTool tool = new SSLTool(runtime);
      if (type.equals("JKS")) {
        assertEquals(0, tool.runTool(SolrCLI.processCommandLineArgs(tool, args)));
      } else {
        assertEquals(0, new picocli.CommandLine(tool).execute(args));
      }
      char[] password = "Test'$(false)Password".toCharArray();
      KeyStore store = KeyStore.getInstance(type);
      try (var input = Files.newInputStream(dir.resolve("store"))) {
        store.load(input, password);
      }
      assertNotNull(store.getKey("solr-ssl", password));
      assertEquals("RSA", store.getKey("solr-ssl", password).getAlgorithm());
      KeyStore trust = KeyStore.getInstance(type);
      try (var input = Files.newInputStream(dir.resolve("store.truststore"))) {
        trust.load(input, password);
      }
      assertTrue(trust.size() > 1);
      assertEquals(store.getCertificate("solr-ssl"), trust.getCertificate("solr-0"));
      assertFalse(runtime.getOutput().contains(new String(password)));
      String include = Files.readString(dir.resolve("solr.in.sh"));
      assertTrue(include.contains("SOLR_SSL_ENABLED='true'"));
      assertTrue(include.contains("SOLR_SSL_KEY_STORE_TYPE='" + type + "'"));
    }
  }

  @Test
  public void testInvalidKeyLeavesConfigurationUntouched() throws Exception {
    Path dir = prepare();
    String original = Files.readString(dir.resolve("solr.in.sh"));
    Files.writeString(dir.resolve("key.pem"), "not a private key");
    expectThrows(
        IllegalArgumentException.class, () -> new SSLTool().configure(params(dir, "JKS", null)));
    assertEquals(original, Files.readString(dir.resolve("solr.in.sh")));
    assertFalse(Files.exists(dir.resolve("store")));
  }

  @Test
  public void testShortKeystorePasswordLeavesConfigurationUntouched() throws Exception {
    Path dir = prepare();
    String original = Files.readString(dir.resolve("solr.in.sh"));
    Files.writeString(dir.resolve("password.txt"), "tiny\n");
    expectThrows(
        IllegalArgumentException.class, () -> new SSLTool().configure(params(dir, "JKS", null)));
    assertEquals(original, Files.readString(dir.resolve("solr.in.sh")));
    assertFalse(Files.exists(dir.resolve("store")));
    assertFalse(Files.exists(dir.resolve("store.truststore")));
  }

  @Test
  public void testPrivateServiceGroupForNewStores() throws Exception {
    Path dir = prepare();
    Path include = dir.resolve("solr.in.sh");
    Files.setPosixFilePermissions(include, PosixFilePermissions.fromString("rw-r-----"));
    var access = Files.readAttributes(include, PosixFileAttributes.class);
    new SSLTool().configure(params(dir, "JKS", null));
    for (Path file : new Path[] {include, dir.resolve("store"), dir.resolve("store.truststore")}) {
      var actual = Files.readAttributes(file, PosixFileAttributes.class);
      assertEquals(access.owner(), actual.owner());
      assertEquals(access.group(), actual.group());
      assertEquals(access.permissions(), actual.permissions());
    }
  }

  @Test
  public void testShortExistingPemPassword() throws Exception {
    Path dir = prepare();
    ProcessBuilder builder =
        new ProcessBuilder(
            "openssl",
            "pkey",
            "-in",
            dir.resolve("key.pem").toString(),
            "-aes-256-cbc",
            "-passout",
            "env:SOLR_SSL_TEST_PEM_PASSWORD",
            "-out",
            dir.resolve("encrypted-key.pem").toString());
    builder.environment().put("SOLR_SSL_TEST_PEM_PASSWORD", "tiny");
    Process process =
        builder
            .redirectErrorStream(true)
            .redirectOutput(dir.resolve("encrypt.log").toFile())
            .start();
    if (!process.waitFor(30, TimeUnit.SECONDS)) {
      process.destroyForcibly().waitFor();
      fail("Test key encryption timed out");
    }
    assertEquals(Files.readString(dir.resolve("encrypt.log")), 0, process.exitValue());
    Files.writeString(dir.resolve("key-password.txt"), "tiny\n");
    new SSLTool()
        .configure(
            new SSLTool.SSLParams(
                dir.resolve("cert.pem"),
                dir.resolve("encrypted-key.pem"),
                dir.resolve("password.txt"),
                dir.resolve("key-password.txt"),
                dir.resolve("store"),
                "JKS",
                dir.resolve("solr.in.sh"),
                null));
    char[] password = "Test'$(false)Password".toCharArray();
    KeyStore store = KeyStore.getInstance("JKS");
    try (var input = Files.newInputStream(dir.resolve("store"))) {
      store.load(input, password);
    }
    assertNotNull(store.getKey("solr-ssl", password));
  }

  @Test
  public void testClusterPropertyAndRollback() throws Exception {
    Path dir = prepare();
    ZkTestServer server = new ZkTestServer(createTempDir());
    server.run();
    try {
      new SSLTool().configure(params(dir, "JKS", server.getZkAddress()));
      try (SolrZkClient client =
          new SolrZkClient.Builder().withUrl(server.getZkAddress()).build()) {
        var properties = new ClusterProperties(client).getClusterProperties();
        assertEquals("https", properties.get("urlScheme"));
      }
      Files.setPosixFilePermissions(
          dir.resolve("solr.in.sh"), PosixFilePermissions.fromString("rw-r--r--"));
      Files.setPosixFilePermissions(
          dir.resolve("store"), PosixFilePermissions.fromString("rw-r-----"));
      var previous = new LinkedHashMap<Path, IncludeFileEditor.Snapshot>();
      var access = new LinkedHashMap<Path, PosixFileAttributes>();
      for (String name : new String[] {"store", "store.truststore", "solr.in.sh"}) {
        Path file = dir.resolve(name);
        previous.put(file, IncludeFileEditor.snapshot(file));
        access.put(file, Files.readAttributes(file, PosixFileAttributes.class));
      }
      String originalInclude = Files.readString(dir.resolve("solr.in.sh"));
      expectThrows(
          IllegalArgumentException.class,
          () -> new SSLTool().configure(params(dir, "JKS", server.getZkAddress() + "/missing")));
      for (var file : previous.entrySet()) {
        assertArrayEquals(file.getValue().data(), Files.readAllBytes(file.getKey()));
        var actual = Files.readAttributes(file.getKey(), PosixFileAttributes.class);
        assertEquals(access.get(file.getKey()).owner(), actual.owner());
        assertEquals(access.get(file.getKey()).group(), actual.group());
        assertEquals(access.get(file.getKey()).permissions(), actual.permissions());
      }
      assertEquals(originalInclude, Files.readString(dir.resolve("solr.in.sh")));
      Path fresh = prepare();
      String originalFresh = Files.readString(fresh.resolve("solr.in.sh"));
      expectThrows(
          IllegalArgumentException.class,
          () -> new SSLTool().configure(params(fresh, "PKCS12", server.getZkAddress() + "/missing")));
      assertFalse(Files.exists(fresh.resolve("store")));
      assertFalse(Files.exists(fresh.resolve("store.truststore")));
      assertEquals(originalFresh, Files.readString(fresh.resolve("solr.in.sh")));
    } finally {
      server.shutdown();
    }
  }

  @Test
  public void testWindowsUnsupportedMessage() {
    System.setProperty("os.name", "Windows 11");
    var error =
        expectThrows(UnsupportedOperationException.class, () -> new SSLTool().configure(null));
    assertTrue(error.getMessage().contains("not supported on Windows"));
    assertTrue(error.getMessage().contains("Configure HTTPS manually"));
  }
}
