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
package org.apache.solr.security;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.solr.client.solrj.request.V2Request;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.SolrException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class JaxRsSingleAuthorizationTest extends SolrCloudTestCase {

  private static final String SECURITY_JSON =
      "{\"authorization\":{\"class\":\"org.apache.solr.security.MockAuthorizationPlugin\"}}";

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1)
        .addConfig("conf", configset("cloud-minimal"))
        .withSecurityJson(SECURITY_JSON)
        .configure();
  }

  @After
  public void clearPredicate() {
    MockAuthorizationPlugin.predicate = null;
  }

  @Test
  public void testJaxRsApiIsAuthorizedOnce() throws Exception {
    final AtomicInteger authorizations = new AtomicInteger();
    MockAuthorizationPlugin.predicate =
        context -> {
          if (context.getResource().endsWith("/cluster/nodes")) {
            authorizations.incrementAndGet();
          }
        };

    new V2Request.Builder("/cluster/nodes").GET().build().process(cluster.getSolrClient());

    assertEquals(
        "a JAX-RS v2 API should be authorized exactly once per request", 1, authorizations.get());
  }

  @Test
  public void testJaxRsApiDenialStillHolds() throws Exception {
    final AtomicInteger authorizations = new AtomicInteger();
    MockAuthorizationPlugin.predicate =
        context -> {
          if (context.getResource().endsWith("/cluster/nodes")) {
            authorizations.incrementAndGet();
            throw new SolrException(SolrException.ErrorCode.FORBIDDEN, "denied by test plugin");
          }
        };

    var jetty = cluster.getJettySolrRunner(0);
    HttpURLConnection conn =
        (HttpURLConnection)
            URI.create(jetty.getBaseURLV2() + "/cluster/nodes").toURL().openConnection();

    assertEquals(
        "a denied JAX-RS v2 API request must still be rejected", 403, conn.getResponseCode());
    assertEquals(
        "a denied JAX-RS v2 API should be authorized exactly once per request",
        1,
        authorizations.get());
  }
}
