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
package org.apache.solr.request.macro;

import java.util.Map;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.json.RequestUtil;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestMacros extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeTests() throws Exception {
    initCore("solrconfig-tlog.xml", "schema_latest.xml");
  }

  @Test
  public void testMacros() throws Exception {
    assertU(add(doc("id", "1", "val_s", "aaa", "val_i", "123")));
    assertU(add(doc("id", "2", "val_s", "bbb", "val_i", "456")));

    assertU(commit());

    assertJQ(req("fl", "id", "q", "id:${id}", "id", "1"), "/response/docs==[{'id':'1'}]");

    assertJQ(req("fl", "id", "q", "${idquery}", "idquery", "id:1"), "/response/docs==[{'id':'1'}]");

    assertJQ(
        req("fl", "id", "q", "${fname}:${fval}", "fname", "id", "fval", "2"),
        "/response/docs==[{'id':'2'}]");

    // test macro expansion in keys...
    assertJQ(
        req(
            "fl",
            "id",
            "q",
            "{!term f=$fieldparam v=$valueparam}",
            "field${p}",
            "val_s",
            "value${p}",
            "aaa",
            "p",
            "param",
            "echoParams",
            "ALL"),
        "/response/docs==[{'id':'1'}]");

    // test disabling expansion
    assertJQ(
        req("fl", "id", "q", "id:\"${id}\"", "id", "1", "expandMacros", "false"),
        "/response/docs==[]");

    // test multiple levels in values
    assertJQ(
        req(
            "fl",
            "id",
            "q",
            "${idquery}",
            "idquery",
            "${a}${b}",
            "a",
            "val${fieldpostfix}:",
            "b",
            "${fieldval}",
            "fieldpostfix",
            "_s",
            "fieldval",
            "bbb"),
        "/response/docs==[{'id':'2'}]");

    // test defaults
    assertJQ(req("fl", "id", "q", "val_s:${val:aaa}"), "/response/docs==[{'id':'1'}]");

    // test defaults with value present
    assertJQ(
        req("fl", "id", "q", "val_s:${val:aaa}", "val", "bbb"), "/response/docs==[{'id':'2'}]");

    // test zero length default value
    assertJQ(req("fl", "id", "q", "val_s:${missing:}aaa"), "/response/docs==[{'id':'1'}]");

    // test missing value
    assertJQ(req("fl", "id", "q", "val_s:${missing}aaa"), "/response/docs==[{'id':'1'}]");
  }

  @Test
  public void testHandlerAppendsAndInvariantsMacrosAreExpandedOnShardRequests() throws Exception {
    SolrParams appends = new MapSolrParams(Map.of("fl", "appended:'${my_term}'"));
    SolrParams invariants = new MapSolrParams(Map.of("rows", "${my_rows}"));

    try (SolrQueryRequest shardReq =
        req(
            "q",
            "*:*",
            "isShard",
            "true",
            "my_term",
            "foobar",
            "my_rows",
            "7",
            "fl",
            "coordinator:'foobar'")) {
      RequestUtil.processParams(null, shardReq, null, appends, invariants);

      assertArrayEquals(
          new String[] {"coordinator:'foobar'", "appended:'foobar'"},
          shardReq.getParams().getParams("fl"));
      assertEquals("7", shardReq.getParams().get("rows"));
    }

    // values the client sent are never expanded on a shard request
    try (SolrQueryRequest shardReq =
        req("q", "*:*", "isShard", "true", "my_term", "foobar", "fl", "literal:'${my_term}'")) {
      RequestUtil.processParams(null, shardReq, null, appends, null);

      assertArrayEquals(
          new String[] {"literal:'${my_term}'", "appended:'foobar'"},
          shardReq.getParams().getParams("fl"));
    }
  }
}
