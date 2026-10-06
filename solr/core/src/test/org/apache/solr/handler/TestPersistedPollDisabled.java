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
import org.apache.solr.response.SolrQueryResponse;
import org.junit.BeforeClass;
import org.junit.Test;

/** {@code disablepoll} with {@code persist=true} survives a core reload; plain disable does not. */
public class TestPersistedPollDisabled extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig-follower-nopoll.xml", "schema.xml");
  }

  private static ReplicationHandler handler() {
    return (ReplicationHandler) h.getCore().getRequestHandler("/replication");
  }

  private static void command(String... params) throws Exception {
    String[] all = new String[params.length + 2];
    all[0] = "qt";
    all[1] = "/replication";
    System.arraycopy(params, 0, all, 2, params.length);
    SolrQueryResponse rsp = new SolrQueryResponse();
    handler().handleRequestBody(req(all), rsp);
    assertNull(rsp.getException());
    assertEquals("OK", rsp.getValues().get("status"));
  }

  @Test
  public void testPersistedAcrossReload() throws Exception {
    assertFalse(handler().isPollingDisabled());

    command("command", "disablepoll");
    assertTrue(handler().isPollingDisabled());
    h.reload();
    assertFalse("a plain disablepoll is not persistent", handler().isPollingDisabled());

    command("command", "disablepoll", "persist", "true");
    assertTrue(handler().isPollingDisabled());
    h.reload();
    assertTrue("persist=true must survive a reload", handler().isPollingDisabled());

    command("command", "enablepoll");
    assertFalse(handler().isPollingDisabled());
    h.reload();
    assertFalse("enablepoll clears the persisted flag", handler().isPollingDisabled());
  }
}
