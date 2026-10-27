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
package org.apache.solr.webapp;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Properties;
import org.apache.solr.embedded.JettyConfig;
import org.apache.solr.embedded.JettySolrRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

/** Tests replication-specific HTTP failures on the core overview with a real generated client. */
public class AdminUiCoreOverviewErrorsTest extends AdminUiStandaloneTestBase {

  private static final String CORE = "overviewerrors";
  private static final String REPLICATION_PATH = "/api/cores/" + CORE + "/replication";
  private static final String ERROR_MESSAGE = "Replication status unavailable";
  private static volatile int errorStatus;
  private static volatile int lastFailureStatus;

  @BeforeClass
  public static void startStandaloneNode() throws Exception {
    Path home = buildStandaloneHome(CORE);
    JettyConfig.Builder config =
        JettyConfig.builder().withFilter(ReplicationErrorFilter.class, "/*");
    configureJettyForUi(config);
    standaloneJetty = new JettySolrRunner(home.toString(), new Properties(), config.build());
    standaloneJetty.start();
    baseUrl = standaloneJetty.getBaseUrl().toString();
  }

  @AfterClass
  public static void stopStandaloneNode() throws Exception {
    if (standaloneJetty != null) {
      standaloneJetty.stop();
      standaloneJetty = null;
    }
  }

  @After
  public void resetFailure() {
    errorStatus = 0;
    lastFailureStatus = 0;
    javascript().executeScript("sessionStorage.clear();");
  }

  @Test
  public void testReplicationForbiddenShowsPermissionFailure() throws Exception {
    openHealthyOverview();
    failReplicationRefresh(HttpServletResponse.SC_FORBIDDEN);

    waitForTextContains(By.id("authz-failures"), "Permission failure");
    assertNoConfigurationMessage();
    assertNoSevereConsoleErrors(REPLICATION_PATH);
  }

  @Test
  public void testReplicationServerErrorShowsMessage() throws Exception {
    openHealthyOverview();
    failReplicationRefresh(HttpServletResponse.SC_SERVICE_UNAVAILABLE);

    waitForTextContains(By.cssSelector("#http-exception .exception"), ERROR_MESSAGE);
    assertNoConfigurationMessage();
    assertNoSevereConsoleErrors(REPLICATION_PATH);
  }

  @Test
  public void testReplicationUnauthorizedRedirectsToLogin() throws Exception {
    openHealthyOverview();
    javascript().executeScript("sessionStorage.setItem('auth.header', 'Basic c3RhbGU6c3RhbGU=');");
    failReplicationRefresh(HttpServletResponse.SC_UNAUTHORIZED);

    waitFor(By.cssSelector("#content #login #username"));
    assertTrue(driver.getCurrentUrl(), driver.getCurrentUrl().endsWith("#/login"));
    assertNull(javascript().executeScript("return sessionStorage.getItem('auth.header');"));
    assertEquals(
        "/" + CORE + "/core-overview",
        javascript().executeScript("return sessionStorage.getItem('auth.location');"));
    assertNoSevereConsoleErrors(REPLICATION_PATH);
  }

  private static void openHealthyOverview() throws Exception {
    openPage(CORE + "/core-overview", By.id("dashboard"));
    // Finish the initial reads before failing just refreshReplication: a successful $http
    // response clears the shared permission banner and could otherwise race the assertion.
    waitUntil(
        "the core overview's initial reads should finish successfully",
        () ->
            Boolean.TRUE.equals(
                javascript()
                    .executeScript(
                        "var dashboard = angular.element(document.getElementById('dashboard'));"
                            + " var scope = dashboard.scope();"
                            + " return !!(scope.index && scope.core && scope.replication"
                            + " && scope.healthcheckMessage"
                            + " && dashboard.injector().get('$http').pendingRequests.length === 0);")));
    assertNoConfigurationMessage();
    assertNoSevereConsoleErrors();
  }

  private static void failReplicationRefresh(int status) throws InterruptedException {
    errorStatus = status;
    // Invoke the real controller method without replacing its service or callback. The filter
    // fails its HTTP request while the generated client and shared error UI run normally.
    javascript()
        .executeScript(
            "angular.element(document.getElementById('dashboard')).scope().refreshReplication();");
    waitUntil(
        "the replication request should receive HTTP " + status, () -> lastFailureStatus == status);
  }

  private static void assertNoConfigurationMessage() {
    assertFalse(
        "An HTTP failure must not be reported as missing replication configuration",
        driver
            .findElement(By.cssSelector("#dashboard #replication .message-container"))
            .isDisplayed());
  }

  private static JavascriptExecutor javascript() {
    return (JavascriptExecutor) driver;
  }

  /** Fails only this core's v2 replication request; all other requests reach Solr normally. */
  public static class ReplicationErrorFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
      int status = errorStatus;
      // JettySolrRunner rewrites /api to /solr/____v2 before filters run.
      String path = ((HttpServletRequest) request).getRequestURI();
      if (status == 0 || !path.endsWith("/cores/" + CORE + "/replication")) {
        chain.doFilter(request, response);
        return;
      }
      HttpServletResponse httpResponse = (HttpServletResponse) response;
      httpResponse.setStatus(status);
      httpResponse.setContentType("application/json");
      httpResponse.setHeader("Cache-Control", "no-store");
      if (status == HttpServletResponse.SC_UNAUTHORIZED) {
        httpResponse.setHeader("WWW-Authenticate", "xBasic realm=\"overview-replication\"");
      }
      httpResponse
          .getOutputStream()
          .write(
              ("{\"error\":{\"msg\":\"" + ERROR_MESSAGE + "\"}}").getBytes(StandardCharsets.UTF_8));
      lastFailureStatus = status;
    }
  }
}
