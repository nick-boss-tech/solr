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
package org.apache.solr.search.function;

import org.apache.solr.SolrTestCaseJ4;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestStringMinMaxFunction extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig-functionquery.xml", "schema11.xml");
    assertU(adoc("id", "1", "a_s", "b", "b_s", "zz"));
    assertU(adoc("id", "2", "a_s", "a", "b_s", "c"));
    assertU(adoc("id", "3", "b_s", "m"));
    assertU(adoc("id", "4"));
    assertU(commit());
  }

  @Test
  public void testMaxOfStringFieldsIsLexicographic() throws Exception {
    assertJQ(req("q", "id:1", "fl", "id,v:max(a_s,b_s)"), "/response/docs/[0]/v=='zz'");
  }

  @Test
  public void testMinOfStringFieldsIsLexicographic() throws Exception {
    assertJQ(req("q", "id:1", "fl", "id,v:min(a_s,b_s)"), "/response/docs/[0]/v=='b'");
  }

  @Test
  public void testMissingValuesAreIgnored() throws Exception {
    assertJQ(req("q", "id:3", "fl", "id,v:min(a_s,b_s)"), "/response/docs/[0]/v=='m'");
    assertJQ(req("q", "id:3", "fl", "id,v:max(a_s,b_s)"), "/response/docs/[0]/v=='m'");
  }

  @Test
  public void testNoValueYieldsNoResult() throws Exception {
    assertJQ(req("q", "id:4", "fl", "id,v:max(a_s,b_s)"), "/response/docs/[0]/v==null");
  }

  @Test
  public void testMultiValuedStringFieldIsRejected() throws Exception {
    expectThrows(Exception.class, () -> h.query(req("q", "*:*", "fl", "id,v:max(a_ss)")));
  }
}
