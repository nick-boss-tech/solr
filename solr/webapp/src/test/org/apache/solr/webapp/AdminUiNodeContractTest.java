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

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

/** Exercises the picker controllers with the existing generated node API contracts. */
public class AdminUiNodeContractTest extends AdminUiTestBase {

  @Before
  public void selectRemoteNode() {
    openPage("", By.id("index"));
    String remoteNode = cluster.getJettySolrRunner(1).getNodeName();
    chosenSelect("node_selector_select", remoteNode);
    waitForTextContains(By.cssSelector("#content #index .node dd"), remoteNode);
  }

  @Test
  public void testPropertiesUseCallbackOnlyContract() throws Exception {
    observeCall("NodeV2", "getNodeProperties", "system.properties");
    click(By.cssSelector("#menu #java-properties a"));
    assertNativeCallback();
    waitFor(By.cssSelector("#java-properties li"));
    assertNoSevereConsoleErrors();
  }

  @Test
  public void testThreadsUseCallbackOnlyContract() throws Exception {
    observeCall("SystemV2", "getThreadDump", "system");
    click(By.cssSelector("#menu #threads a"));
    assertNativeCallback();
    waitFor(By.cssSelector("#thread-dump tbody tr"));
    assertNoSevereConsoleErrors();
  }

  @Test
  public void testLoggingLevelsUseCallbackOnlyContract() throws Exception {
    click(By.cssSelector("#menu #logging a"));
    waitFor(By.id("viewer"));
    observeCall("LoggingV2", "listAllLoggersAndLevels", "loggers");
    click(By.cssSelector("#menu #logging .level a"));
    assertNativeCallback();
    waitFor(By.cssSelector("#loggingtree .jstree-anchor"));
    assertNoSevereConsoleErrors();
  }

  private void observeCall(String service, String method, String field) {
    ((JavascriptExecutor) driver)
        .executeScript(
            """
            var api = angular.element(document.body).injector().get(arguments[0]);
            var method = arguments[1], field = arguments[2], original = api[method];
            var observed = window.nodeContract = {called: false, nativePayload: false};
            api[method] = function() {
              var args = Array.prototype.slice.call(arguments);
              observed.called = true;
              observed.argumentCount = args.length;
              observed.callbackOnly = typeof args[0] === 'function';
              var callbackIndex = args.length - 1, callback = args[callbackIndex];
              if (typeof callback === 'function') {
                args[callbackIndex] = function(error, data, response) {
                  observed.nativePayload = !error && !!data && data[field] !== undefined
                      && !!response && response.status === 200;
                  return callback.apply(this, arguments);
                };
              }
              return original.apply(this, args);
            };
            """,
            service,
            method,
            field);
  }

  private void assertNativeCallback() throws Exception {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    waitUntil(
        "controller invokes the generated node API",
        () -> Boolean.TRUE.equals(js.executeScript("return window.nodeContract.called;")));
    assertEquals(
        "the existing API accepts only its callback",
        1,
        ((Number) js.executeScript("return window.nodeContract.argumentCount;")).intValue());
    assertEquals(
        "the first argument must be the callback",
        Boolean.TRUE,
        js.executeScript("return window.nodeContract.callbackOnly;"));
    waitUntil(
        "generated callback receives the native response",
        () -> Boolean.TRUE.equals(js.executeScript("return window.nodeContract.nativePayload;")));
  }
}
