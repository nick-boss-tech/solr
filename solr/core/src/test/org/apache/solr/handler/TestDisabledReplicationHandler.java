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
package org.apache.solr.handler;

import org.apache.solr.SolrTestCaseJ4;
import org.junit.BeforeClass;
import org.junit.Test;

/** A replication handler whose leader and follower sections are all disabled stays disabled. */
public class TestDisabledReplicationHandler extends SolrTestCaseJ4 {

  private static final String REPLICATION_ENABLED =
      "//lst[@name='details']/lst[@name='leader']/str[@name='replicationEnabled']";

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig-disabled-replication.xml", "schema-minimal.xml");
  }

  @Test
  public void testDisabledLeaderStaysDisabled() throws Exception {
    assertQ(
        req("qt", "/replication-disabled-leader", "command", "details"),
        REPLICATION_ENABLED + "[.='false']");
  }

  @Test
  public void testDisabledLeaderAndFollowerStayDisabled() throws Exception {
    assertQ(
        req("qt", "/replication-disabled-both", "command", "details"),
        REPLICATION_ENABLED + "[.='false']");
  }

  @Test
  public void testUnconfiguredHandlerIsEnabled() throws Exception {
    assertQ(
        req("qt", "/replication-default", "command", "details"),
        REPLICATION_ENABLED + "[.='true']");
  }

  @Test
  public void testEnabledLeaderIsEnabled() throws Exception {
    assertQ(
        req("qt", "/replication-enabled-leader", "command", "details"),
        REPLICATION_ENABLED + "[.='true']");
  }
}
