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
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;
import org.apache.solr.SolrTestCase;
import org.apache.solr.cloud.ZkTestServer;
import org.apache.solr.common.cloud.ClusterProperties;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.util.EnvUtils;
import org.junit.Test;
import picocli.CommandLine;

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

  private SSLTool.SSLParams encryptedParams(Path dir, String suppliedPassword) throws Exception {
    Path key = dir.resolve("encrypted-key.pem");
    Path log = dir.resolve("encrypt-key.log");
    ProcessBuilder builder =
        new ProcessBuilder(
                "openssl",
                "pkey",
                "-in",
                dir.resolve("key.pem").toString(),
                "-aes-256-cbc",
                "-passout",
                "env:TEST_PEM_PASSWORD",
                "-out",
                key.toString())
            .redirectErrorStream(true)
            .redirectOutput(log.toFile());
    builder.environment().put("TEST_PEM_PASSWORD", "tiny");
    Process process = builder.start();
    process.getOutputStream().close();
    if (!process.waitFor(30, TimeUnit.SECONDS)) {
      process.destroyForcibly().waitFor();
      fail("Test key encryption timed out");
    }
    assertEquals(Files.readString(log), 0, process.exitValue());
    Path pemPassword = dir.resolve("pem-password.txt");
    Files.writeString(pemPassword, suppliedPassword + "\n");
    return new SSLTool.SSLParams(
        dir.resolve("cert.pem"),
        key,
        dir.resolve("password.txt"),
        pemPassword,
        dir.resolve("store"),
        "JKS",
        dir.resolve("solr.in.sh"),
        null);
  }

  @Test
  public void testClientAuthenticationSettingsArePreserved() throws Exception {
    for (boolean required : new boolean[] {true, false}) {
      Path dir = prepare();
      String policy =
          "export SOLR_SSL_NEED_CLIENT_AUTH="
              + required
              + "\nSOLR_SSL_WANT_CLIENT_AUTH='"
              + !required
              + "'\n";
      Files.writeString(dir.resolve("solr.in.sh"), policy);
      new SSLTool().configure(params(dir, "JKS", null));
      assertTrue(Files.readString(dir.resolve("solr.in.sh")).startsWith(policy));
    }
  }

  @Test
  public void testShortExistingPemPassword() throws Exception {
    Path dir = prepare();
    new SSLTool().configure(encryptedParams(dir, "tiny"));
    KeyStore store = KeyStore.getInstance("JKS");
    char[] password = Files.readString(dir.resolve("password.txt")).stripTrailing().toCharArray();
    try (var input = Files.newInputStream(dir.resolve("store"))) {
      store.load(input, password);
    }
    assertEquals("RSA", store.getKey("solr-ssl", password).getAlgorithm());
  }

  @Test
  public void testWrongPemPasswordLeavesConfigurationUntouched() throws Exception {
    Path dir = prepare();
    String original = Files.readString(dir.resolve("solr.in.sh"));
    var encrypted = encryptedParams(dir, "wrong-password");
    var error =
        expectThrows(IllegalArgumentException.class, () -> new SSLTool().configure(encrypted));
    assertTrue(error.getMessage().contains("OpenSSL could not convert"));
    assertEquals(original, Files.readString(dir.resolve("solr.in.sh")));
    assertFalse(Files.exists(dir.resolve("store")));
    assertFalse(Files.exists(dir.resolve("store.truststore")));
  }

  @Test
  public void testShortKeystorePasswordLeavesConfigurationUntouched() throws Exception {
    Path dir = prepare();
    String original = Files.readString(dir.resolve("solr.in.sh"));
    Files.writeString(dir.resolve("password.txt"), "tiny\n");
    var error =
        expectThrows(
            IllegalArgumentException.class,
            () -> new SSLTool().configure(params(dir, "JKS", null)));
    assertTrue(error.getMessage().contains("six characters"));
    assertEquals(original, Files.readString(dir.resolve("solr.in.sh")));
    assertFalse(Files.exists(dir.resolve("store")));
    assertFalse(Files.exists(dir.resolve("store.truststore")));
  }

  @Test
  public void testBothFormats() throws Exception {
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
      assertEquals(0, new CommandLine(tool).execute(args));
      assertTrue(runtime.getOutput().contains("Restart Solr to enable HTTPS."));
      assertTrue(runtime.getOutput().contains("No cluster property changed."));
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
  public void testSecureExampleImportsCertificate() throws Exception {
    Path dir = prepare();
    var runtime = new CLITestHelper.TestingRuntime(true);
    SecureExampleSSL ssl = new SecureExampleSSL(dir.resolve("example"), runtime);
    assertFalse(ssl.isConfigured());
    ssl.install(dir.resolve("cert.pem"), dir.resolve("key.pem"), dir.resolve("password.txt"), null);
    assertTrue(ssl.isConfigured());
    assertFalse(runtime.getOutput().contains("Restart Solr to enable HTTPS."));
    assertFalse(runtime.getOutput().contains("No cluster property changed."));
    assertTrue(
        runtime
            .getOutput()
            .contains("Embedded ZooKeeper will configure urlScheme=https when Solr starts."));
    assertArrayEquals(
        Files.readAllBytes(dir.resolve("cert.pem")), Files.readAllBytes(ssl.certificate()));
    ssl.configureClientTrust();
    assertEquals(ssl.keystore() + ".truststore", EnvUtils.getProperty("javax.net.ssl.trustStore"));
    assertEquals("Test'$(false)Password", EnvUtils.getProperty("javax.net.ssl.trustStorePassword"));
    ssl.printVerificationInstructions("https://localhost:8983/solr");
    assertTrue(runtime.getOutput().contains("https://localhost:8983/api/node/system"));
    assertTrue(ssl.isSelfSigned());
    assertEquals(
        "# Keep this comment\nSOLR_HEAP=512m\nexport SOLR_SSL_ENABLED=false\nSOLR_SSL_KEY_STORE=old\n",
        Files.readString(dir.resolve("solr.in.sh")));
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
      byte[] originalStore = Files.readAllBytes(dir.resolve("store"));
      String originalInclude = Files.readString(dir.resolve("solr.in.sh"));
      expectThrows(
          IllegalArgumentException.class,
          () -> new SSLTool().configure(params(dir, "JKS", server.getZkAddress() + "/missing")));
      assertArrayEquals(originalStore, Files.readAllBytes(dir.resolve("store")));
      assertEquals(originalInclude, Files.readString(dir.resolve("solr.in.sh")));
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
