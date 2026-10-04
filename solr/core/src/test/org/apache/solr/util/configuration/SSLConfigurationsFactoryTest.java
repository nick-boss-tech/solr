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
package org.apache.solr.util.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.util.ExecutorUtil;
import org.apache.solr.common.util.SolrNamedThreadFactory;
import org.junit.Test;

public class SSLConfigurationsFactoryTest extends SolrTestCase {

  @Test
  public void testLazilyInitializedSingletonIsVolatile() throws Exception {
    Field field = SSLConfigurationsFactory.class.getDeclaredField("currentConfigurations");
    assertTrue(
        "double-checked locking requires a volatile field",
        Modifier.isVolatile(field.getModifiers()));
  }

  @Test
  public void testConcurrentCurrentReturnsSingleInstance() throws Exception {
    SSLConfigurationsFactory.setCurrent(null);
    int threads = 8;
    ExecutorService executor =
        ExecutorUtil.newMDCAwareFixedThreadPool(
            threads, new SolrNamedThreadFactory("SSLConfigurationsFactoryTest"));
    try {
      CountDownLatch start = new CountDownLatch(1);
      List<Future<SSLConfigurations>> futures = new ArrayList<>();
      for (int i = 0; i < threads; i++) {
        futures.add(
            executor.submit(
                () -> {
                  start.await();
                  return SSLConfigurationsFactory.current();
                }));
      }
      start.countDown();
      SSLConfigurations first = futures.get(0).get();
      assertNotNull(first);
      for (Future<SSLConfigurations> future : futures) {
        assertSame(first, future.get());
      }
    } finally {
      ExecutorUtil.shutdownAndAwaitTermination(executor);
      SSLConfigurationsFactory.setCurrent(null);
    }
  }
}
