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

import java.nio.file.Path;
import java.util.Map;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Tests the Core Admin screen's write actions - add, rename, swap and unload - which only apply to
 * a standalone (user-managed) Solr node; in cloud mode these operations belong to the Collections
 * API. Also asserts the standalone-mode differences of the UI menus.
 */
public class AdminUiCoreAdminStandaloneTest extends AdminUiStandaloneTestBase {

  private static Path home;

  @BeforeClass
  public static void startStandaloneNode() throws Exception {
    home = buildStandaloneHome("renamecore", "swapa", "swapb", "unloadcore");
    // instance dir for the add-core test; the core is created through the UI
    createStandaloneCoreDir(home, "addedcore");
    standaloneJetty = startStandaloneJetty(home);
    baseUrl = standaloneJetty.getBaseUrl().toString();
  }

  @AfterClass
  public static void stopStandaloneNode() throws Exception {
    if (standaloneJetty != null) {
      standaloneJetty.stop();
      standaloneJetty = null;
    }
  }

  @Test
  public void testStandaloneMenus() {
    openPage("", By.id("index"));
    // cloud-only menu entries are absent in standalone mode
    assertTrue(driver.findElements(By.cssSelector("#menu .cloud")).isEmpty());
    assertTrue(driver.findElements(By.cssSelector("#menu .collections")).isEmpty());
    assertTrue(
        "node picker is SolrCloud-only",
        driver.findElements(By.id("node-selector-item")).isEmpty());
    // the per-core menu (shown when a core page is open) offers the core-level
    // screens directly - query and replication are standalone-only entries
    openPage("swapa/core-overview", By.id("dashboard"));
    waitFor(By.cssSelector("#core-menu .query"));
    waitFor(By.cssSelector("#core-menu .replication"));
    waitForPageContains("Ping request handler is not configured with a healthcheck file.");
    assertNoSevereConsoleErrors();
  }

  @Test
  public void testAddCoreViaUi() throws Exception {
    openPage("~cores", By.id("cores"));
    click(By.cssSelector("#cores #add"));
    setText(By.id("add_name"), "addedcore");
    setText(By.id("add_instanceDir"), "addedcore");
    click(By.xpath("//button[@ng-click='addCore()']"));

    waitUntil("core addedcore should exist", () -> coreExists("addedcore"));
    waitForPageContains("addedcore");
    assertNoSevereConsoleErrors();
  }

  @Test
  public void testInitialCoreStatusKeepsAddCoreFormOpen() throws Exception {
    assertInitialCoreStatusNavigation(true);
  }

  @Test
  public void testInitialCoreStatusStillSelectsDefaultCore() throws Exception {
    assertInitialCoreStatusNavigation(false);
  }

  @Test
  public void testRenameCoreViaUi() throws Exception {
    openPage("~cores/renamecore", By.id("cores"));
    click(By.cssSelector("#cores #rename"));
    setText(By.id("rename_other"), "renamedcore");
    click(By.xpath("//button[@ng-click='renameCore()']"));

    waitUntil(
        "core should be renamed", () -> coreExists("renamedcore") && !coreExists("renamecore"));
    assertNoSevereConsoleErrors();
  }

  @Test
  public void testSwapCoresViaUi() throws Exception {
    // make the cores distinguishable: swapa gets one document, swapb stays empty
    var client = standaloneJetty.getSolrClient();
    var doc = new SolrInputDocument();
    doc.addField("id", "swap-doc");
    client.add("swapa", doc);
    client.commit("swapa");
    assertEquals(1, numDocs("swapa"));
    assertEquals(0, numDocs("swapb"));

    openPage("~cores/swapa", By.id("cores"));
    click(By.cssSelector("#cores #swap"));
    // pick the other core in the swap-with dropdown (plain select)
    waitFor(By.id("swap_other")).sendKeys("swapb");
    click(By.xpath("//button[@ng-click='swapCores()']"));

    waitUntil("swap should exchange the cores' indexes", () -> numDocs("swapb") == 1);
    assertEquals(0, numDocs("swapa"));
    assertNoSevereConsoleErrors();
  }

  @Test
  public void testUnloadCoreViaUi() throws Exception {
    openPage("~cores/unloadcore", By.id("cores"));
    click(By.cssSelector("#cores #unload"));
    // unload asks for confirmation via a native browser dialog
    driver.switchTo().alert().accept();

    waitUntil("core unloadcore should be gone", () -> !coreExists("unloadcore"));
    assertNoSevereConsoleErrors();
  }

  private void assertInitialCoreStatusNavigation(boolean openAddForm) throws Exception {
    openPage("", By.id("index"));
    JavascriptExecutor js = (JavascriptExecutor) driver;
    try {
      js.executeScript(
          """
          var injector = angular.element(document.body).injector();
          var api = injector.get('CoresV2');
          var state = {
            api: api, original: api.getAllCoreStatus, captured: false, ready: false
          };
          window.initialCoreStatus = state;
          api.getAllCoreStatus = function(opts, callback) {
            var isCoreAdminRead = !Object.prototype.hasOwnProperty.call(opts || {}, 'indexInfo');
            if (state.captured || !isCoreAdminRead) {
              return state.original.apply(api, arguments);
            }
            state.captured = true;
            state.callback = callback;
            return state.original.call(api, opts, function(error, data) {
              state.failed = !!error;
              state.data = data;
              state.ready = true;
            });
          };
          """);
      driver.get(baseUrl + "/index.html#/~cores");
      waitFor(By.cssSelector("#content #cores"));
      waitUntil(
          "the initial Core Admin status response is held",
          () ->
              Boolean.TRUE.equals(
                  js.executeScript("return window.initialCoreStatus.ready === true;")));
      assertEquals(
          "The real initial status request must succeed",
          Boolean.FALSE,
          js.executeScript("return window.initialCoreStatus.failed;"));
      String firstCore =
          (String) js.executeScript("return Object.keys(window.initialCoreStatus.data.status)[0];");
      assertNotNull("The standalone fixture must have a default core", firstCore);
      if (openAddForm) {
        click(By.cssSelector("#content #cores #add"));
        setText(By.id("add_name"), "pending-core");
        setText(By.id("add_instanceDir"), "pending-instance");
      }
      js.executeScript(
          """
          var state = window.initialCoreStatus;
          state.api.getAllCoreStatus = state.original;
          state.callback(null, state.data, {body: state.data});
          angular.element(document.body).injector().get('$timeout')(function() {
            state.delivered = true;
          });
          """);
      waitUntil(
          "the initial Core Admin status callback has settled",
          () ->
              Boolean.TRUE.equals(
                  js.executeScript("return window.initialCoreStatus.delivered === true;")));
      String path =
          (String)
              js.executeScript(
                  "return angular.element(document.body).injector().get('$location').path();");
      if (openAddForm) {
        assertEquals(
            "Loading the default core must not navigate away from an open Add Core form",
            "/~cores",
            path);
        assertTrue(
            "The Add Core form must stay visible",
            driver.findElement(By.id("add_name")).isDisplayed());
        assertEquals("pending-core", driver.findElement(By.id("add_name")).getAttribute("value"));
        assertEquals(
            "pending-instance", driver.findElement(By.id("add_instanceDir")).getAttribute("value"));
      } else {
        assertEquals(
            "Default-core navigation must remain when no Add Core form is open",
            "/~cores/" + firstCore,
            path);
      }
      assertNoSevereConsoleErrors();
    } finally {
      js.executeScript(
          """
          if (window.initialCoreStatus) {
            var state = window.initialCoreStatus;
            state.api.getAllCoreStatus = state.original;
            delete window.initialCoreStatus;
          }
          """);
    }
  }

  private boolean coreExists(String coreName) {
    try {
      NamedList<Object> response = adminApi("/admin/cores", params());
      return ((Map<?, ?>) response.get("status")).containsKey(coreName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private long numDocs(String coreName) {
    try {
      return standaloneJetty
          .getSolrClient()
          .query(coreName, new SolrQuery("*:*"))
          .getResults()
          .getNumFound();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
