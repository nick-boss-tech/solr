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

package org.apache.solr.client.solrj.impl;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.request.RequestWriter;
import org.apache.solr.client.solrj.response.ResponseParser;
import org.apache.solr.client.solrj.response.XMLResponseParser;
import org.apache.solr.common.util.ExecutorUtil;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

/**
 * Guards the binary-compatibility restoration of SOLR-18600: {@link HttpSolrClientBase} and {@link
 * HttpSolrClientBuilderBase} exist again as top-level classes carrying the members that code
 * compiled against SolrJ 10.0.0 links against, and the ordinary builder path works when used
 * entirely through those base types.
 */
public class HttpSolrClientBinaryCompatTest extends SolrTestCase {

  /** A subclass written the way pre-10.1 user code was: against the base types only. */
  private static class BaseFixture extends HttpSolrClientBase {
    BaseFixture(String url, HttpSolrClientBuilderBase<?, ?> builder) {
      super(url, builder);
    }

    @Override
    public HttpSolrClientBuilderBase<?, ?> builder() {
      return new HttpJdkSolrClient.Builder(serverBaseUrl);
    }

    @Override
    protected LBSolrClient createLBSolrClient() {
      throw new UnsupportedOperationException();
    }

    @Override
    protected boolean isFollowRedirects() {
      return true;
    }

    @Override
    public NamedList<Object> requestWithBaseUrl(
        String baseUrl, SolrRequest<?> solrRequest, String collection) {
      return new NamedList<>();
    }

    @Override
    public CompletableFuture<NamedList<Object>> requestAsync(
        SolrRequest<?> request, String collection) {
      return CompletableFuture.completedFuture(new NamedList<>());
    }

    @Override
    public NamedList<Object> request(SolrRequest<?> request, String collection) {
      return new NamedList<>();
    }

    @Override
    public void close() {}

    void replaceParser(ResponseParser newParser) {
      setParser(newParser);
    }
  }

  private static Set<String> normalizedMimeTypes(ResponseParser parser) {
    return parser.getContentTypes().stream()
        .map(ct -> ct.split(";", 2)[0].toLowerCase(Locale.ROOT).trim())
        .collect(Collectors.toSet());
  }

  @Test
  public void testRestoredClassesCarryReleasedMembers() throws Exception {
    assertTrue(HttpSolrClientBase.class.isAssignableFrom(HttpSolrClient.class));
    assertTrue(HttpSolrClientBase.class.isAssignableFrom(HttpJdkSolrClient.class));

    // The nine released protected fields of the client base, with their released types.
    assertField(HttpSolrClientBase.class, "DEFAULT_PATH", String.class);
    assertField(HttpSolrClientBase.class, "FALLBACK_CHARSET", Charset.class);
    assertField(HttpSolrClientBase.class, "serverBaseUrl", String.class);
    assertField(HttpSolrClientBase.class, "requestTimeoutMillis", long.class);
    assertField(HttpSolrClientBase.class, "urlParamNames", Set.class);
    assertField(HttpSolrClientBase.class, "requestWriter", RequestWriter.class);
    assertField(HttpSolrClientBase.class, "parser", ResponseParser.class);
    assertField(HttpSolrClientBase.class, "defaultParserMimeTypes", Set.class);
    assertField(HttpSolrClientBase.class, "basicAuthAuthorizationStr", String.class);

    // The fluent builder methods are declared on the restored builder base and return it, which
    // is the descriptor shape pre-compiled consumers link against.
    for (String name :
        new String[] {"withConnectionTimeout", "withRequestTimeout", "withIdleTimeout"}) {
      assertEquals(
          HttpSolrClientBuilderBase.class,
          HttpSolrClientBuilderBase.class
              .getMethod(name, long.class, TimeUnit.class)
              .getReturnType());
      assertEquals(
          HttpSolrClientBuilderBase.class,
          HttpJdkSolrClient.Builder.class
              .getMethod(name, long.class, TimeUnit.class)
              .getReturnType());
    }

    // The JDK builder carries the bridges for the released build() and withHttpClient descriptors.
    assertTrue(
        Arrays.stream(HttpJdkSolrClient.Builder.class.getDeclaredMethods())
            .anyMatch(
                m -> m.getName().equals("build") && m.getReturnType() == HttpSolrClientBase.class));
    assertTrue(
        Arrays.stream(HttpJdkSolrClient.Builder.class.getDeclaredMethods())
            .anyMatch(
                m ->
                    m.getName().equals("withHttpClient")
                        && m.getParameterTypes().length == 1
                        && m.getParameterTypes()[0] == HttpSolrClientBase.class));

    // The released SolrRequest overload and the restored declared types on neighboring classes.
    assertNotNull(
        SolrRequest.class.getMethod(
            "processWithBaseUrl", HttpSolrClientBase.class, String.class, String.class));
    assertEquals(
        HttpSolrClientBase.class, CloudSolrClient.class.getMethod("getHttpClient").getReturnType());
    assertEquals(
        HttpSolrClientBase.class, LBSolrClient.Builder.class.getTypeParameters()[0].getBounds()[0]);
    assertEquals(
        HttpSolrClientBase.class,
        HttpClusterStateProvider.class.getTypeParameters()[0].getBounds()[0]);
  }

  private static void assertField(Class<?> clazz, String name, Class<?> type) throws Exception {
    Field f = clazz.getDeclaredField(name);
    assertEquals("field " + name, type, f.getType());
  }

  @Test
  public void testOrdinaryBuilderPathThroughBaseTypes() throws Exception {
    HttpSolrClientBuilderBase<?, ?> builder =
        new HttpJdkSolrClient.Builder("http://127.0.0.1:8983/solr")
            .withConnectionTimeout(10, TimeUnit.SECONDS)
            .withRequestTimeout(60, TimeUnit.SECONDS);
    HttpSolrClientBase client = builder.build();
    try {
      assertEquals(HttpJdkSolrClient.class, client.getClass());
      assertEquals("http://127.0.0.1:8983/solr", client.getBaseURL());
    } finally {
      client.close();
    }
  }

  @Test
  public void testSubclassFixtureProtectedState() {
    HttpJdkSolrClient.Builder builder = new HttpJdkSolrClient.Builder();
    builder.withRequestTimeout(1234, TimeUnit.MILLISECONDS);
    BaseFixture fixture = new BaseFixture("http://127.0.0.1:8983/solr/", builder);

    assertEquals("http://127.0.0.1:8983/solr", fixture.serverBaseUrl);
    assertEquals(1234, fixture.requestTimeoutMillis);
    assertEquals(HttpSolrClient.DEFAULT_URL_PARAM_NAMES, fixture.urlParamNames);
    assertNotNull(fixture.requestWriter);
    assertNotNull(fixture.parser);
    assertEquals(normalizedMimeTypes(fixture.parser), fixture.defaultParserMimeTypes);

    fixture.replaceParser(new XMLResponseParser());
    assertEquals(normalizedMimeTypes(fixture.getParser()), fixture.defaultParserMimeTypes);
  }

  @Test
  public void testDonorClientSeedingAndExecutorOwnership() throws Exception {
    ExecutorService executor = ExecutorUtil.newMDCAwareCachedThreadPool("binaryCompatTest");
    try {
      HttpJdkSolrClient donor =
          new HttpJdkSolrClient.Builder("http://127.0.0.1:8983/solr")
              .withExecutor(executor)
              .withRequestTimeout(4321, TimeUnit.MILLISECONDS)
              .withBasicAuthCredentials("user", "pass")
              .build();
      // Seed a derived client by invoking the base-typed withHttpClient descriptor, the call
      // shape pre-compiled code was built against.
      HttpJdkSolrClient.Builder seedingBuilder =
          new HttpJdkSolrClient.Builder("http://127.0.0.1:8983/solr");
      HttpJdkSolrClient.Builder.class
          .getMethod("withHttpClient", HttpSolrClientBase.class)
          .invoke(seedingBuilder, donor);
      HttpSolrClientBase derived = seedingBuilder.build();
      try {
        assertEquals(4321, derived.requestTimeoutMillis);
        assertEquals(donor.basicAuthAuthorizationStr, derived.basicAuthAuthorizationStr);
      } finally {
        derived.close();
      }
      // The executor was caller-provided to the donor and only adopted by the derived client, so
      // closing either client must leave it running.
      assertFalse(executor.isShutdown());
      donor.close();
      assertFalse(executor.isShutdown());
    } finally {
      executor.shutdownNow();
    }
  }
}
