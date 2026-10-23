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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.Utils;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.security.RuleBasedAuthorizationPlugin;

public class SecurityConfHandlerRequestEditTest extends SolrTestCaseJ4 {

  private static final String SEED =
      "{\"authentication\":{\"class\":\"solr.BasicAuthPlugin\","
          + "\"credentials\":{\"solr\":\"solr\"}},"
          + "\"authorization\":{\"class\":\"solr.RuleBasedAuthorizationPlugin\",\"user-role\":{}}}";

  /**
   * Two edit requests on one handler both survive. The first request is held right after it reads
   * security.json, and the second request must not read until the first has written.
   */
  public void testOverlappingEditRequestsKeepBothChanges() throws Exception {
    assumeWorkingMockito();
    Path solrHome = createTempDir("securityConfHandlerRequestEdits");
    Path securityJson = solrHome.resolve("security.json");
    Files.write(securityJson, SEED.getBytes(StandardCharsets.UTF_8));
    CoreContainer cc = mock(CoreContainer.class);
    when(cc.getSolrHome()).thenReturn(solrHome);
    when(cc.getAuthorizationPlugin()).thenReturn(new RuleBasedAuthorizationPlugin());

    CountDownLatch firstRead = new CountDownLatch(1);
    CountDownLatch secondRead = new CountDownLatch(1);
    AtomicBoolean holdFirst = new AtomicBoolean(true);
    AtomicReference<Thread> firstThread = new AtomicReference<>();
    SecurityConfHandlerLocal handler =
        new SecurityConfHandlerLocal(cc) {
          @Override
          public SecurityConfHandler.SecurityConfig getSecurityConfig(boolean getFresh) {
            SecurityConfHandler.SecurityConfig config = super.getSecurityConfig(getFresh);
            if (Thread.currentThread() == firstThread.get()) {
              if (holdFirst.compareAndSet(true, false)) {
                firstRead.countDown();
                try {
                  secondRead.await(3, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
              }
            } else {
              secondRead.countDown();
            }
            return config;
          }
        };

    AtomicReference<Throwable> errors = new AtomicReference<>();
    Thread first =
        new Thread(() -> postEdit(handler, "{'set-user-role': {'alice': ['admin']}}", errors));
    firstThread.set(first);
    first.start();
    if (!firstRead.await(60, TimeUnit.SECONDS)) {
      fail("first request never read security.json: " + errors.get());
    }
    Thread second =
        new Thread(() -> postEdit(handler, "{'set-user-role': {'bob': ['admin']}}", errors));
    second.start();
    first.join(TimeUnit.SECONDS.toMillis(60));
    second.join(TimeUnit.SECONDS.toMillis(60));
    assertFalse("first request still running", first.isAlive());
    assertFalse("second request still running", second.isAlive());
    assertNull("an edit failed: " + errors.get(), errors.get());

    @SuppressWarnings("unchecked")
    Map<String, Object> data = (Map<String, Object>) Utils.fromJSON(Files.readAllBytes(securityJson));
    @SuppressWarnings("unchecked")
    Map<String, Object> authorization = (Map<String, Object>) data.get("authorization");
    @SuppressWarnings("unchecked")
    Map<String, Object> userRole = (Map<String, Object>) authorization.get("user-role");
    assertEquals("both edits are kept", Set.of("alice", "bob"), userRole.keySet());
  }

  private static void postEdit(
      SecurityConfHandler handler, String command, AtomicReference<Throwable> errors) {
    try {
      SolrQueryRequestBase req = new SolrQueryRequestBase(null, new ModifiableSolrParams());
      req.getContext().put("httpMethod", SolrRequest.METHOD.POST);
      req.getContext().put("path", "/admin/authorization");
      req.setContentStreams(
          List.of(
              new ContentStreamBase.ByteArrayStream(
                  command.getBytes(StandardCharsets.UTF_8), "")));
      handler.handleRequestBody(req, new SolrQueryResponse());
    } catch (Throwable t) {
      errors.set(t);
    }
  }
}
