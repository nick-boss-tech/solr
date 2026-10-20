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
import org.apache.solr.common.SolrException;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestStringMinMaxFunction extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml", "schema.xml");
    assertU(adoc("id", "1", "a_s1", "b", "b_s1", "c", "i_idv", "5"));
    assertU(adoc("id", "2", "a_s1", "zz"));
    assertU(adoc("id", "3", "b_s1", "a"));
    assertU(adoc("id", "4"));
    assertU(adoc("id", "5", "a_s1", "B", "b_s1", "a"));
    assertU(commit());
  }

  @Test
  public void testMinAndMaxIgnoreMissingValues() {
    assertQ(
        req("q", "*:*", "fl", "id,mn:min(a_s1,b_s1),mx:max(a_s1,b_s1)", "sort", "id asc"),
        "//result/doc[1]/str[@name='mn']='b'",
        "//result/doc[1]/str[@name='mx']='c'",
        "//result/doc[2]/str[@name='mn']='zz'",
        "//result/doc[2]/str[@name='mx']='zz'",
        "//result/doc[3]/str[@name='mn']='a'",
        "//result/doc[3]/str[@name='mx']='a'",
        "count(//result/doc[4]/str[@name='mn'])=0",
        "count(//result/doc[4]/str[@name='mx'])=0");
  }

  @Test
  public void testComparisonIsLexicographicByCodePoint() {
    assertQ(
        req("q", "id:5", "fl", "mn:min(a_s1,b_s1),mx:max(a_s1,b_s1)"),
        "//result/doc/str[@name='mn']='B'",
        "//result/doc/str[@name='mx']='a'");
  }

  @Test
  public void testMultiValuedFieldIsAnError() {
    assertQEx(
        "multi-valued string in min",
        req("q", "*:*", "fl", "mn:min(cat)"),
        SolrException.ErrorCode.BAD_REQUEST.code);
  }

  @Test
  public void testMixingStringAndNumericIsAnError() {
    assertQEx(
        "string mixed with numeric in max",
        req("q", "*:*", "fl", "mx:max(a_s1,i_idv)"),
        SolrException.ErrorCode.BAD_REQUEST.code);
  }

  @Test
  public void testNumericMinAndMaxUnchanged() {
    assertQ(
        req("q", "id:1", "fl", "mn:min(i_idv,4),mx:max(i_idv,4)"),
        "//result/doc/float[@name='mn']='4.0'",
        "//result/doc/float[@name='mx']='5.0'");
  }
}
