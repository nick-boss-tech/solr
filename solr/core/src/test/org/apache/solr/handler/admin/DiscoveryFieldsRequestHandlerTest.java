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
package org.apache.solr.handler.admin;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DiscoveryFieldsRequestHandlerTest extends SolrTestCaseJ4 {
  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml", "schema.xml");
    h.getCore().registerRequestHandler("/discovery/fields", new DiscoveryFieldsRequestHandler());
  }

  @Before
  public void clearDocuments() {
    assertU(delQ("*:*"));
    assertU(commit());
  }

  @Test
  public void testFieldGlobAndFilteredCounts() throws Exception {
    assertU(adoc("id", "1", "kind_s", "keep", "alpha_s", "a", "beta_s", "b"));
    assertU(adoc("id", "2", "kind_s", "drop", "alpha_s", "a", "gamma_i", "3"));
    assertU(commit());

    assertQ(
        req("qt", "/discovery/fields", "timeAllowed", "5000", "fq", "kind_s:keep"),
        "//lst[@name='fields']/lst[@name='alpha_s']/int[@name='docCount'][.='1']",
        "//lst[@name='fields']/lst[@name='beta_s']",
        "count(//lst[@name='fields']/lst[@name='gamma_i'])=0");
    assertQ(
        req("qt", "/discovery/fields", "timeAllowed", "5000", "fieldGlob", "?eta_*"),
        "//lst[@name='fields']/lst[@name='beta_s']/int[@name='docCount'][.='1']",
        "count(//lst[@name='fields']/lst)=1");
    assertQ(
        req("qt", "/discovery/fields", "timeAllowed", "5000", "fieldGlob", "missing*"),
        "count(//lst[@name='fields']/lst)=0");
    assertQ(
        req("qt", "/discovery/fields", "timeAllowed", "5000", "fieldGlob", "*a*ha_?"),
        "//lst[@name='fields']/lst[@name='alpha_s']",
        "count(//lst[@name='fields']/lst)=1");
    expectThrows(
        SolrException.class,
        () ->
            h.query(
                req(
                    "qt", "/discovery/fields",
                    "timeAllowed", "5000",
                    "fieldGlob", "x".repeat(257))));
    String[] doc = new String[2 + 257 * 2];
    doc[0] = "id";
    doc[1] = "many";
    for (int i = 0; i < 257; i++) {
      doc[2 + i * 2] = "wide" + i + "_s";
      doc[3 + i * 2] = "value";
    }
    assertU(adoc(doc));
    assertU(commit());
    expectThrows(
        SolrException.class,
        () -> h.query(req("qt", "/discovery/fields", "timeAllowed", "5000")));
    assertQ(
        req("qt", "/discovery/fields", "timeAllowed", "5000", "fieldGlob", "wide1_s"),
        "//lst[@name='fields']/lst[@name='wide1_s']/int[@name='docCount'][.='1']",
        "count(//lst[@name='fields']/lst)=1");
  }
}
