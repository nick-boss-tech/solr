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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DiscoveryFieldsRequestHandlerTest extends SolrTestCaseJ4 {
  private static final String HANDLER_PATH = "/discovery/fields";
  private static final Map<String, String> ORIGINAL_PROPERTIES = new HashMap<>();

  @BeforeClass
  public static void beforeClass() throws Exception {
    setPropertyForTest("solr.discovery.fields.maxFields", "16");
    setPropertyForTest("solr.discovery.fields.maxTimeAllowedMs", "7000");
    setPropertyForTest("solr.discovery.fields.maxConcurrentRequests", "2");
    setPropertyForTest("solr.discovery.fields.useFilterCache", "false");
    try {
      initCore("solrconfig-discovery-fields.xml", "schema.xml");
      h.getCore().getRequestHandler(HANDLER_PATH);
    } finally {
      restoreTestProperties();
    }
  }

  @Before
  public void clearDocuments() {
    assertU(delQ("*:*"));
    assertU(commit());
  }

  @Test
  public void testConfiguredDefaultsAndMatchAllDomain() throws Exception {
    assertU(adoc("id", "1", "config_s", "value"));
    assertU(commit());

    assertQ(
        req("qt", HANDLER_PATH, "fieldGlob", "config_*"),
        "//str[@name='countMode'][.='presence']",
        "//int[@name='numFound'][.='1']",
        "//lst[@name='fields']/lst[@name='config_s']/str[@name='presence'][.='present']",
        "count(//lst[@name='fields']/lst[@name='config_s']/int[@name='docCount'])=0",
        "//bool[@name='domainFilterCacheEnabled'][.='false']");
  }

  @Test
  public void testFieldGlobSelection() throws Exception {
    assertU(adoc("id", "1", "glob_alpha_s", "a", "glob_beta_s", "b", "glob_gamma_i", "3"));
    assertU(commit());

    assertQ(
        req("qt", HANDLER_PATH, "fieldGlob", "?lob_beta_s"),
        "//lst[@name='fields']/lst[@name='glob_beta_s']/str[@name='presence'][.='present']",
        "count(//lst[@name='fields']/lst)=1");
    assertQ(
        req("qt", HANDLER_PATH, "fieldGlob", "*alpha_?"),
        "//lst[@name='fields']/lst[@name='glob_alpha_s']",
        "count(//lst[@name='fields']/lst)=1");
    assertQ(req("qt", HANDLER_PATH, "fieldGlob", "missing*"), "count(//lst[@name='fields']/lst)=0");
    expectThrows(
        SolrException.class, () -> h.query(req("qt", HANDLER_PATH, "fieldGlob", "x".repeat(257))));
  }

  @Test
  public void testPresenceAndExactCountsRespectFiltersAndDeletedDocuments() throws Exception {
    assertU(adoc("id", "1", "kind_s", "keep", "review_alpha_s", "a"));
    assertU(adoc("id", "2", "kind_s", "drop", "review_beta_s", "b"));
    assertU(adoc("id", "3", "kind_s", "keep", "review_deleted_only_s", "x"));
    assertU(commit());
    assertU(delI("3"));
    assertU(commit());

    assertQ(
        req("qt", HANDLER_PATH, "fq", "kind_s:keep", "fieldGlob", "review_*"),
        "//lst[@name='fields']/lst[@name='review_alpha_s']/str[@name='presence'][.='present']",
        "//lst[@name='fields']/lst[@name='review_beta_s']/str[@name='presence'][.='absent']",
        "//lst[@name='fields']/lst[@name='review_deleted_only_s']/str[@name='presence'][.='absent']",
        "count(//lst[@name='fields']/lst/int[@name='docCount'])=0");

    assertQ(
        req(
            "qt", HANDLER_PATH,
            "fq", "kind_s:keep",
            "fieldGlob", "review_*",
            "countMode", "exact"),
        "//str[@name='countMode'][.='exact']",
        "//lst[@name='fields']/lst[@name='review_alpha_s']/int[@name='docCount'][.='1']",
        "count(//lst[@name='fields']/lst[@name='review_beta_s'])=0",
        "count(//lst[@name='fields']/lst[@name='review_deleted_only_s'])=0");
  }

  @Test
  public void testUnknownPresenceForStoredOnlyField() throws Exception {
    assertU(adoc("id", "1", "kind_s", "keep", "shouldbeunindexed", "stored value"));
    assertU(commit());

    assertQ(
        req("qt", HANDLER_PATH, "fq", "kind_s:keep", "fieldGlob", "shouldbeunindexed"),
        "//lst[@name='fields']/lst[@name='shouldbeunindexed']/str[@name='presence'][.='unknown']",
        "//lst[@name='fields']/lst[@name='shouldbeunindexed']/str[@name='presenceSource'][.='no-supported-index-presence-data']");
  }

  @Test
  public void testConfiguredFieldAndTimeLimits() throws Exception {
    String[] doc = new String[2 + 17 * 2];
    doc[0] = "id";
    doc[1] = "1000";
    for (int i = 0; i < 17; i++) {
      doc[2 + i * 2] = "limit" + i + "_s";
      doc[3 + i * 2] = "value";
    }
    assertU(adoc(doc));
    assertU(commit());

    expectThrows(
        SolrException.class, () -> h.query(req("qt", HANDLER_PATH, "fieldGlob", "limit*")));
    expectThrows(
        SolrException.class,
        () -> h.query(req("qt", HANDLER_PATH, "timeAllowed", "7001", "fieldGlob", "limit0_s")));

    assertQ(
        req("qt", HANDLER_PATH, "fieldGlob", "limit0_s"),
        "//str[@name='countMode'][.='presence']",
        "//lst[@name='fields']/lst[@name='limit0_s']/str[@name='presence'][.='present']",
        "count(//lst[@name='fields']/lst)=1");
  }

  @Test
  public void testConfiguredConcurrencyLimit() throws Exception {
    Field permitsField = DiscoveryFieldsRequestHandler.class.getDeclaredField("DISCOVERY_PERMITS");
    permitsField.setAccessible(true);
    Semaphore permits = (Semaphore) permitsField.get(null);
    assertTrue(permits.tryAcquire());
    assertTrue(permits.tryAcquire());
    try {
      expectThrows(
          SolrException.class, () -> h.query(req("qt", HANDLER_PATH, "fieldGlob", "missing*")));
    } finally {
      permits.release(2);
    }
  }

  private static void setPropertyForTest(String name, String value) {
    ORIGINAL_PROPERTIES.put(name, System.getProperty(name));
    System.setProperty(name, value);
  }

  private static void restoreTestProperties() {
    for (Map.Entry<String, String> entry : ORIGINAL_PROPERTIES.entrySet()) {
      if (entry.getValue() == null) {
        System.clearProperty(entry.getKey());
      } else {
        System.setProperty(entry.getKey(), entry.getValue());
      }
    }
    ORIGINAL_PROPERTIES.clear();
  }
}
