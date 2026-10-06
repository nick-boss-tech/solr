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
package org.apache.solr.handler.admin.api;

import org.apache.lucene.store.RateLimiter;
import org.apache.solr.SolrTestCase;

public class ReplicationRateLimiterTest extends SolrTestCase {

  public void testConcurrentStreamsShareOneLimiterPerRate() {
    RateLimiter first = ReplicationAPIBase.rateLimiterFor(0.25);
    RateLimiter second = ReplicationAPIBase.rateLimiterFor(0.25);
    assertSame("streams with the same configured rate must share one limiter", first, second);
    assertEquals(0.25, first.getMBPerSec(), 0.0);
  }

  public void testDifferentRatesGetDifferentLimiters() {
    RateLimiter slow = ReplicationAPIBase.rateLimiterFor(0.5);
    RateLimiter fast = ReplicationAPIBase.rateLimiterFor(5);
    assertNotSame(slow, fast);
    assertEquals(0.5, slow.getMBPerSec(), 0.0);
    assertEquals(5, fast.getMBPerSec(), 0.0);
  }

  public void testZeroMeansUnthrottled() {
    RateLimiter unlimited = ReplicationAPIBase.rateLimiterFor(0);
    assertSame(unlimited, ReplicationAPIBase.rateLimiterFor(0));
    assertEquals(Double.MAX_VALUE, unlimited.getMBPerSec(), 0.0);
  }
}
