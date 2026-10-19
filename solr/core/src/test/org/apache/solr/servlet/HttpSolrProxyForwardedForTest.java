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
package org.apache.solr.servlet;

import java.util.List;
import org.apache.solr.SolrTestCaseJ4;
import org.junit.Test;

public class HttpSolrProxyForwardedForTest extends SolrTestCaseJ4 {

  @Test
  public void testNoExistingHopsUsesRemoteAddress() {
    assertEquals("10.0.0.5", HttpSolrProxy.forwardedFor(List.of(), "10.0.0.5"));
  }

  @Test
  public void testExistingHopGetsRemoteAddressAppended() {
    assertEquals("1.1.1.1, 10.0.0.5", HttpSolrProxy.forwardedFor(List.of("1.1.1.1"), "10.0.0.5"));
  }

  @Test
  public void testSeparateHeaderValuesAreJoinedInOrder() {
    assertEquals(
        "1.1.1.1, 2.2.2.2, 10.0.0.5",
        HttpSolrProxy.forwardedFor(List.of("1.1.1.1", "2.2.2.2"), "10.0.0.5"));
  }

  @Test
  public void testMissingRemoteAddressKeepsExistingHops() {
    assertEquals("1.1.1.1", HttpSolrProxy.forwardedFor(List.of("1.1.1.1"), null));
  }

  @Test
  public void testNothingToForwardReturnsNull() {
    assertNull(HttpSolrProxy.forwardedFor(List.of(), null));
  }
}
