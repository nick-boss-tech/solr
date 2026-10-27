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
package org.apache.solr.client.solrj.response;

import java.util.Date;
import java.util.Map;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

/** Pins the public core-status conversion behavior across Jackson versions. */
public class CoreAdminJacksonCompatibilityTest extends SolrTestCase {

  @Test
  public void testCoreStatusDateAndNumericTypes() {
    final var response = new CoreAdminResponse();
    final var values = new NamedList<Object>();
    values.add("status", Map.of("core", Map.of("startTime", new Date(1234), "uptime", 42L)));
    response.setResponse(values);

    assertEquals(1234L, response.getStartTime("core").getTime());
    assertEquals(Long.valueOf(42), response.getUptime("core"));
    assertNull(response.getCoreStatus("missing"));
  }

  @Test
  public void testInvalidCoreStatusRemainsArgumentError() {
    final var response = new CoreAdminResponse();
    final var values = new NamedList<Object>();
    values.add("status", Map.of("core", Map.of("uptime", "not a number")));
    response.setResponse(values);

    expectThrows(IllegalArgumentException.class, response::getCoreStatus);
  }
}
