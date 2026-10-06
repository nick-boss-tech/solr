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
package org.apache.solr.spelling.suggest;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrException;
import org.junit.BeforeClass;

/** A suggester that was never built on this node must say so, not fail with a bare 500. */
public class TestFreeTextSuggesterNotBuilt extends SolrTestCaseJ4 {
  static final String URI = "/free_text_suggest";

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig-phrasesuggest.xml", "schema-phrasesuggest.xml");
  }

  public void testQueryBeforeAndAfterBuild() {
    assertQEx(
        "not built suggester should explain itself",
        "is not built on this node",
        reqWithPath(
            URI,
            "q",
            "foo b",
            SuggesterParams.SUGGEST_COUNT,
            "1",
            SuggesterParams.SUGGEST_DICT,
            "free_text_suggest"),
        SolrException.ErrorCode.SERVICE_UNAVAILABLE);

    assertQ(reqWithPath(URI, "q", "", SuggesterParams.SUGGEST_BUILD_ALL, "true"));
    assertQ(
        reqWithPath(
            URI,
            "q",
            "foo b",
            SuggesterParams.SUGGEST_COUNT,
            "1",
            SuggesterParams.SUGGEST_DICT,
            "free_text_suggest"),
        "//lst[@name='suggest']/lst[@name='free_text_suggest']/lst[@name='foo b']/int[@name='numFound'][.='1']");
  }
}
