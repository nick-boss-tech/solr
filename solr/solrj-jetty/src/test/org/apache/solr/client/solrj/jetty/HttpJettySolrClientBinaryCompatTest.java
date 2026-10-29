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

package org.apache.solr.client.solrj.jetty;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.impl.HttpSolrClientBase;
import org.apache.solr.client.solrj.impl.HttpSolrClientBuilderBase;
import org.junit.Test;

/**
 * Jetty-side counterpart of the SOLR-18600 binary-compatibility guard: the Jetty builder exposes
 * the same restored base-type descriptors as the JDK builder.
 */
public class HttpJettySolrClientBinaryCompatTest extends SolrTestCase {

  @Test
  public void testJettyBuilderBaseDescriptors() throws Exception {
    assertTrue(HttpSolrClientBase.class.isAssignableFrom(HttpJettySolrClient.class));

    for (String name :
        new String[] {"withConnectionTimeout", "withRequestTimeout", "withIdleTimeout"}) {
      assertTrue(
          returnTypes(HttpJettySolrClient.Builder.class, name, long.class, TimeUnit.class)
              .contains(HttpSolrClientBuilderBase.class));
    }

    assertTrue(
        Arrays.stream(HttpJettySolrClient.Builder.class.getDeclaredMethods())
            .anyMatch(
                m -> m.getName().equals("build") && m.getReturnType() == HttpSolrClientBase.class));
    assertTrue(
        Arrays.stream(HttpJettySolrClient.Builder.class.getDeclaredMethods())
            .anyMatch(
                m ->
                    m.getName().equals("withHttpClient")
                        && m.getParameterTypes().length == 1
                        && m.getParameterTypes()[0] == HttpSolrClientBase.class));
  }

  @Test
  public void testOrdinaryBuilderPathThroughBaseTypes() throws Exception {
    HttpSolrClientBuilderBase<?, ?> builder =
        new HttpJettySolrClient.Builder("http://127.0.0.1:8983/solr")
            .withConnectionTimeout(10, TimeUnit.SECONDS)
            .withRequestTimeout(60, TimeUnit.SECONDS);
    HttpSolrClientBase client = builder.build();
    try {
      assertEquals(HttpJettySolrClient.class, client.getClass());
      assertEquals("http://127.0.0.1:8983/solr", client.getBaseURL());
    } finally {
      client.close();
    }
  }

  private static Set<Class<?>> returnTypes(Class<?> c, String name, Class<?>... params) {
    return Arrays.stream(c.getMethods())
        .filter(m -> m.getName().equals(name) && Arrays.equals(m.getParameterTypes(), params))
        .map(Method::getReturnType)
        .collect(Collectors.toSet());
  }
}
