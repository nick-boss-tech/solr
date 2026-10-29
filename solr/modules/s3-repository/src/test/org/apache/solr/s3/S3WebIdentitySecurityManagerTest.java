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
package org.apache.solr.s3;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.util.SuppressForbidden;
import org.apache.solr.util.ExternalPaths;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;

/** Exercises the AWS SDK's default STS transport with the policy shipped in Solr distributions. */
public class S3WebIdentitySecurityManagerTest extends SolrTestCase {
  private static final int PROCESS_TIMEOUT_SECONDS = 60;

  @SuppressForbidden(
      reason = "The local STS stub uses the JDK HTTP server; no shared fixture exists")
  public void testWebIdentityCredentialsWithSecurityManager() throws Exception {
    assumeTrue("Java 24+ cannot enable the Security Manager", Runtime.version().feature() < 24);
    assertNotNull(
        "The shipped policy must be tested from a Solr source checkout", ExternalPaths.SOURCE_HOME);
    Path policy = ExternalPaths.SOURCE_HOME.resolve("server/etc/security.policy");
    assertTrue("Missing shipped policy: " + policy, Files.isRegularFile(policy));
    Path temporaryDirectory = createTempDir("web-identity").toAbsolutePath();
    Path token = temporaryDirectory.resolve("token");
    Files.writeString(token, "dummy-web-identity-token", StandardCharsets.UTF_8);

    AtomicReference<String> request = new AtomicReference<>();
    HttpServer sts = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    sts.createContext(
        "/",
        exchange -> {
          try (exchange) {
            request.set(
                new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            byte[] response =
                """
                <AssumeRoleWithWebIdentityResponse xmlns="https://sts.amazonaws.com/doc/2011-06-15/">
                  <AssumeRoleWithWebIdentityResult>
                    <Credentials>
                      <AccessKeyId>stub-access-key</AccessKeyId>
                      <SecretAccessKey>stub-secret-key</SecretAccessKey>
                      <SessionToken>stub-session-token</SessionToken>
                      <Expiration>2099-01-01T00:00:00Z</Expiration>
                    </Credentials>
                  </AssumeRoleWithWebIdentityResult>
                  <ResponseMetadata><RequestId>stub-request</RequestId></ResponseMetadata>
                </AssumeRoleWithWebIdentityResponse>
                """
                    .getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/xml");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
          }
        });
    sts.start();
    try {
      runProbe(policy, temporaryDirectory, token, sts.getAddress().getPort());
      assertNotNull("The web identity provider must call STS", request.get());
      assertTrue(request.get(), request.get().contains("Action=AssumeRoleWithWebIdentity"));
      assertTrue(
          request.get(), request.get().contains("WebIdentityToken=dummy-web-identity-token"));
    } finally {
      sts.stop(0);
    }
  }

  /** The shipped policy plus read access to the child's classpath. */
  @SuppressWarnings("SystemGetProperty")
  private static Path childPolicy(Path shippedPolicy, Path temporaryDirectory) throws IOException {
    StringBuilder policy =
        new StringBuilder(Files.readString(shippedPolicy, StandardCharsets.UTF_8));
    policy.append("\ngrant {\n");
    for (String entry :
        System.getProperty("java.class.path").split(System.getProperty("path.separator"))) {
      Path path = Path.of(entry).toAbsolutePath();
      String target = path.toString().replace("\\", "\\\\").replace("\"", "\\\"");
      policy
          .append("  permission java.io.FilePermission \"")
          .append(target)
          .append(Files.isDirectory(path) ? "/-" : "")
          .append("\", \"read\";\n");
    }
    policy.append("};\n");
    Path file = temporaryDirectory.resolve("security.policy");
    Files.writeString(file, policy, StandardCharsets.UTF_8);
    return file;
  }

  @SuppressWarnings("SystemGetProperty")
  private void runProbe(Path policy, Path temporaryDirectory, Path token, int port)
      throws Exception {
    Path output = temporaryDirectory.resolve("probe.log");
    ProcessBuilder builder =
        new ProcessBuilder(
            Path.of(System.getProperty("java.home"), "bin", "java").toString(),
            "-Djava.security.manager",
            // Replace the default policy so a user's ~/.java.policy cannot hide missing grants.
            "-Djava.security.policy==" + childPolicy(policy, temporaryDirectory).toUri(),
            "-Djava.io.tmpdir=" + temporaryDirectory,
            "-Duser.home=" + temporaryDirectory,
            "-Daws.region=us-east-1",
            "-Daws.endpointUrl=http://127.0.0.1:" + port,
            "-Daws.disableEc2Metadata=true",
            "-cp",
            System.getProperty("java.class.path"),
            CredentialsProbe.class.getName(),
            token.toString());
    // Inherited SDK overrides, proxies or injected JVM options must not select another transport.
    builder
        .environment()
        .keySet()
        .removeIf(
            name -> {
              String upper = name.toUpperCase(Locale.ROOT);
              return upper.startsWith("AWS_")
                  || upper.endsWith("_PROXY")
                  || upper.equals("JAVA_TOOL_OPTIONS")
                  || upper.equals("JDK_JAVA_OPTIONS")
                  || upper.equals("_JAVA_OPTIONS");
            });
    Process process = builder.redirectErrorStream(true).redirectOutput(output.toFile()).start();
    try {
      boolean exited = process.waitFor(PROCESS_TIMEOUT_SECONDS, TimeUnit.SECONDS);
      assertTrue("STS probe timed out: " + Files.readString(output), exited);
      assertEquals("STS probe failed: " + Files.readString(output), 0, process.exitValue());
    } finally {
      process.destroyForcibly();
      assertTrue(
          "STS probe did not stop", process.waitFor(PROCESS_TIMEOUT_SECONDS, TimeUnit.SECONDS));
    }
  }

  /** Runs outside the Testcontainers suite's JVM, with the Security Manager enabled at startup. */
  @SuppressWarnings("NewClassNamingConvention")
  public static class CredentialsProbe {
    @SuppressForbidden(reason = "This subprocess specifically tests the Java 21 Security Manager")
    @SuppressWarnings("removal")
    public static void main(String[] args) {
      if (System.getSecurityManager() == null) {
        throw new AssertionError("The probe requires an active Security Manager");
      }
      try (WebIdentityTokenFileCredentialsProvider provider =
          WebIdentityTokenFileCredentialsProvider.builder()
              .webIdentityTokenFile(Path.of(args[0]))
              .roleArn("arn:aws:iam::123456789012:role/test-role")
              .roleSessionName("solr-policy-test")
              .build()) {
        var credentials = provider.resolveCredentials();
        if (!(credentials instanceof AwsSessionCredentials session)
            || !session.accessKeyId().equals("stub-access-key")
            || !session.secretAccessKey().equals("stub-secret-key")
            || !session.sessionToken().equals("stub-session-token")) {
          throw new AssertionError("Expected credentials returned by the local STS stub");
        }
      }
    }
  }
}
