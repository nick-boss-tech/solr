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

import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chromium.ChromiumDriver;

/**
 * Browser coverage for the Admin UI live-node picker: selecting another node, verifying node-scoped
 * screens follow that target, restoring the selection from sessionStorage across reload, and
 * confirming the picker stays hidden in standalone mode (covered separately).
 */
public class AdminUiNodePickerTest extends AdminUiTestBase {

  private static final By DASHBOARD_NODE = By.cssSelector("#content #index .node dd");

  @Before
  public void clearPersistedNodeSelection() {
    ensureCloudCluster();
    driver.get(baseUrl + "/index.html#/");
    waitFor(By.id("node-selector-item"));
    ((JavascriptExecutor) driver)
        .executeScript("sessionStorage.removeItem('adminui.selectedNode');");
  }

  private void selectNode(String nodeName) {
    chosenSelect("node_selector_select", nodeName);
  }

  @Test
  public void testCloudLandingShowsNodesAndEmptyNodeSelector() {
    driver.get(baseUrl + "/index.html#/");
    waitFor(By.id("node-selector-item"));
    ((JavascriptExecutor) driver)
        .executeScript("sessionStorage.removeItem('adminui.selectedNode');");
    driver.navigate().refresh();
    waitFor(By.id("nodes-table"));
    waitFor(By.id("node-selector-item"));
    String pickerText = waitFor(By.id("node_selector_select_chosen")).getText();
    assertTrue(
        "empty picker should show Node Selector placeholder, got: " + pickerText,
        pickerText.contains("Node Selector"));
    assertFalse(
        "node prompt stays off the cluster Nodes page",
        driver.findElement(By.id("select-node-prompt")).isDisplayed());

    click(By.cssSelector("#menu #index a"));
    waitFor(By.id("select-node-prompt"));
    assertTrue(driver.findElement(By.id("select-node-prompt")).isDisplayed());

    String remoteNode = cluster.getJettySolrRunner(1).getNodeName();
    selectNode(remoteNode);
    waitForTextContains(DASHBOARD_NODE, remoteNode);
    assertFalse(driver.findElement(By.id("select-node-prompt")).isDisplayed());
    assertNoSevereConsoleErrors();
  }

  @Test
  public void testSelectRemoteNodeUpdatesDashboardAndPersistsAcrossReload() throws Exception {
    String localNode = cluster.getJettySolrRunner(0).getNodeName();
    String remoteNode = cluster.getJettySolrRunner(1).getNodeName();

    openPage("", By.id("index"));
    waitFor(By.id("node-selector-item"));
    waitForTextContains(DASHBOARD_NODE, localNode);
    waitFor(By.id("selected-node-badge"));

    selectNode(remoteNode);

    waitForTextContains(DASHBOARD_NODE, remoteNode);
    waitForTextContains(By.cssSelector("#selected-node-badge .badge-value"), remoteNode);
    assertTrue(
        "badge should mark remote targeting",
        waitFor(By.id("selected-node-badge")).getAttribute("class").contains("remote"));

    driver.navigate().refresh();
    waitFor(By.cssSelector("#content #index"));
    waitForTextContains(DASHBOARD_NODE, remoteNode);
    waitForTextContains(By.cssSelector("#selected-node-badge .badge-value"), remoteNode);
    assertTrue(waitFor(By.id("selected-node-badge")).getAttribute("class").contains("remote"));

    assertNoSevereConsoleErrors();
  }

  @Test
  public void testNodeSelectionSurvivesInAppNavigation() {
    String remoteNode = cluster.getJettySolrRunner(1).getNodeName();

    openPage("", By.id("index"));
    waitFor(By.id("node-selector-item"));
    selectNode(remoteNode);
    waitForTextContains(DASHBOARD_NODE, remoteNode);

    click(By.cssSelector("#menu #java-properties a"));
    waitFor(By.id("java-properties"));
    waitFor(By.cssSelector("#java-properties li"));
    waitForTextContains(By.cssSelector("#selected-node-badge .badge-value"), remoteNode);
    assertTrue(waitFor(By.id("selected-node-badge")).getAttribute("class").contains("remote"));
    assertNoSevereConsoleErrors();
  }

  @Test
  public void testLoggingLevelsHidesNodeBadge() {
    String remoteNode = cluster.getJettySolrRunner(1).getNodeName();

    openPage("", By.id("index"));
    waitFor(By.id("node-selector-item"));
    selectNode(remoteNode);

    click(By.cssSelector("#menu #logging a"));
    waitFor(By.id("logging"));
    click(By.cssSelector("#menu #logging .level a"));
    waitFor(By.id("loggingtree"));

    assertFalse(
        "Node badge should stay hidden on logging levels",
        driver.findElement(By.id("selected-node-badge")).isDisplayed());
    assertNoSevereConsoleErrors();
  }

  @Test
  public void testCoreMenuIgnoresLateResponseFromPreviousNode() throws Exception {
    assertCoreMenuIgnoresOlderResponse(true);
  }

  @Test
  public void testCoreMenuKeepsNewestRefreshForSameNode() throws Exception {
    assertCoreMenuIgnoresOlderResponse(false);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testRestoredSelectionTargetsInitialCoreMenuRequest() throws Exception {
    String remoteNode = cluster.getJettySolrRunner(1).getNodeName();
    openPage("", By.id("index"));
    selectNode(remoteNode);
    waitForTextContains(DASHBOARD_NODE, remoteNode);
    ChromiumDriver chrome = (ChromiumDriver) driver;
    Map<String, Object> registration =
        chrome.executeCdpCommand(
            "Page.addScriptToEvaluateOnNewDocument",
            Map.of(
                "source",
                """
                var prototype = XMLHttpRequest.prototype;
                var open = prototype.open;
                var setHeader = prototype.setRequestHeader;
                var send = prototype.send;
                window.initialCoreRequests = [];
                prototype.open = function(method, url) {
                  this.coreRequest = {url: String(url), target: null};
                  return open.apply(this, arguments);
                };
                prototype.setRequestHeader = function(name, value) {
                  if (this.coreRequest && name.toLowerCase() === 'x-solr-targetnode') {
                    this.coreRequest.target = String(value);
                  }
                  return setHeader.apply(this, arguments);
                };
                prototype.send = function() {
                  if (this.coreRequest && /\\/api\\/cores(?:\\?|$)/.test(this.coreRequest.url)) {
                    window.initialCoreRequests.push(this.coreRequest);
                  }
                  return send.apply(this, arguments);
                };
                """));
    try {
      driver.navigate().refresh();
      waitForTextContains(DASHBOARD_NODE, remoteNode);
      waitUntil(
          "the restored core-menu request was sent",
          () ->
              Boolean.TRUE.equals(
                  chrome.executeScript("return window.initialCoreRequests.length > 0;")));
      List<Map<String, Object>> requests =
          (List<Map<String, Object>>) chrome.executeScript("return window.initialCoreRequests;");
      assertTrue(
          "Every initial core-menu request must target the restored node: " + requests,
          requests.stream().allMatch(r -> remoteNode.equals(r.get("target"))));
      assertNoSevereConsoleErrors();
    } finally {
      chrome.executeCdpCommand(
          "Page.removeScriptToEvaluateOnNewDocument",
          Map.of("identifier", registration.get("identifier")));
    }
  }

  @Test
  public void testSystemInfoFailureStillRefreshesLocalCoreMenu() throws Exception {
    openPage("", By.id("index"));
    waitForTextContains(DASHBOARD_NODE, cluster.getJettySolrRunner(0).getNodeName());
    JavascriptExecutor js = (JavascriptExecutor) driver;
    try {
      js.executeScript(
          """
          var injector = angular.element(document.body).injector();
          var system = injector.get('SystemV2');
          var cores = injector.get('CoresV2');
          var scope = angular.element(document.body).scope();
          var state = {
            system: system, systemOriginal: system.getNodeSystemInfo,
            cores: cores, coresOriginal: cores.getAllCoreStatus, calls: 0
          };
          window.metadataFallback = state;
          system.getNodeSystemInfo = function(opts, callback) {
            callback(new Error('metadata denied'), null, {status: 403});
          };
          cores.getAllCoreStatus = function(opts, callback) {
            state.calls++;
            return state.coresOriginal.call(cores, opts, function(error, data, response) {
              state.completed = !error;
              callback(error, data, response);
            });
          };
          scope.$apply(function() {
            scope.resetMenu('index', injector.get('Constants').IS_ROOT_PAGE);
          });
          injector.get('$timeout')(function() { state.settled = true; });
          """);
      waitUntil(
          "the metadata failure callback has settled",
          () ->
              Boolean.TRUE.equals(
                  js.executeScript("return window.metadataFallback.settled === true;")));
      assertTrue(
          "Failure to discover system info must not prevent a local core refresh",
          ((Number) js.executeScript("return window.metadataFallback.calls;")).intValue() > 0);
      waitUntil(
          "the permitted local core request completed",
          () ->
              Boolean.TRUE.equals(
                  js.executeScript("return window.metadataFallback.completed === true;")));
      assertNull(
          js.executeScript(
              "return angular.element(document.body).injector().get('NodeTarget').state.selectedNode;"));
      assertNoSevereConsoleErrors();
    } finally {
      js.executeScript(
          """
          if (window.metadataFallback) {
            var state = window.metadataFallback;
            state.system.getNodeSystemInfo = state.systemOriginal;
            state.cores.getAllCoreStatus = state.coresOriginal;
            delete window.metadataFallback;
          }
          """);
    }
  }

  private void assertCoreMenuIgnoresOlderResponse(boolean changeNode) throws Exception {
    String localNode = cluster.getJettySolrRunner(0).getNodeName();
    String remoteNode = cluster.getJettySolrRunner(1).getNodeName();
    openPage("", By.id("index"));
    selectNode(remoteNode);
    waitForTextContains(DASHBOARD_NODE, remoteNode);
    JavascriptExecutor js = (JavascriptExecutor) driver;
    try {
      js.executeScript(
          """
          var injector = angular.element(document.body).injector();
          var api = injector.get('CoresV2');
          var scope = angular.element(document.body).scope();
          window.coreStatusQueue = {
            api: api, original: api.getAllCoreStatus, scope: scope, callbacks: []
          };
          api.getAllCoreStatus = function(opts, callback) {
            var queue = window.coreStatusQueue;
            var entry = {callback: callback, ready: false};
            queue.callbacks.push(entry);
            return queue.original.call(api, opts, function(error) {
              entry.error = error;
              entry.ready = true;
            });
          };
          scope.$apply(function() {
            scope.resetMenu('index', injector.get('Constants').IS_ROOT_PAGE);
          });
          """);
      waitUntil(
          "the older core request is queued",
          () ->
              Boolean.TRUE.equals(
                  js.executeScript(
                      "var entries = window.coreStatusQueue.callbacks;"
                          + "return entries.length >= 1 && entries[0].ready && !entries[0].error;")));
      if (changeNode) {
        selectNode(localNode);
        waitForTextContains(DASHBOARD_NODE, localNode);
      } else {
        js.executeScript(
            """
            var injector = angular.element(document.body).injector();
            var scope = window.coreStatusQueue.scope;
            scope.$apply(function() {
              scope.resetMenu('index', injector.get('Constants').IS_ROOT_PAGE);
            });
            """);
      }
      waitUntil(
          "the newer core request is queued",
          () ->
              Boolean.TRUE.equals(
                  js.executeScript(
                      "var entries = window.coreStatusQueue.callbacks;"
                          + "return entries.length >= 2 && entries[entries.length - 1].ready"
                          + " && !entries[entries.length - 1].error;")));
      js.executeScript(
          """
          var callbacks = window.coreStatusQueue.callbacks;
          var payload = {status: {new_core: {name: 'new_core'}}, initFailures: {}};
          callbacks[callbacks.length - 1].callback(null, payload, {body: payload});
          """);
      waitUntil(
          "the newer core inventory is rendered",
          () ->
              Boolean.TRUE.equals(
                  js.executeScript(
                      """
                      return Array.from(document.querySelectorAll('#core-selector option'))
                        .some(function(option) { return option.textContent === 'new_core'; });
                      """)));
      js.executeScript(
          """
          var payload = {status: {old_core: {name: 'old_core'}}, initFailures: {}};
          window.coreStatusQueue.callbacks[0].callback(null, payload, {body: payload});
          angular.element(document.body).injector().get('$timeout')(function() {
            window.coreStatusQueue.released = true;
          });
          """);
      waitUntil(
          "the older callback and its digest have completed",
          () ->
              Boolean.TRUE.equals(
                  js.executeScript("return window.coreStatusQueue.released === true;")));
      assertEquals(
          "A late response must not replace the current core menu",
          "new_core",
          js.executeScript(
              "return window.coreStatusQueue.scope.cores.map(function(core) { return core.name; })"
                  + ".join(',');"));
      assertTrue(
          Boolean.TRUE.equals(
              js.executeScript(
                  """
                  var options = Array.from(document.querySelectorAll('#core-selector option'));
                  return options.some(function(option) { return option.textContent === 'new_core'; })
                    && !options.some(function(option) { return option.textContent === 'old_core'; });
                  """)));
      assertNoSevereConsoleErrors();
    } finally {
      js.executeScript(
          """
          if (window.coreStatusQueue) {
            window.coreStatusQueue.api.getAllCoreStatus = window.coreStatusQueue.original;
            delete window.coreStatusQueue;
          }
          """);
    }
  }
}
