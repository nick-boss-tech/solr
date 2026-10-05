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
package org.apache.solr.ltr.store.rest;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.file.PathUtils;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.util.Utils;
import org.apache.solr.embedded.JettyConfig;
import org.apache.solr.ltr.feature.ValueFeature;
import org.apache.solr.util.RestTestHarness;
import org.apache.solr.util.SolrJettyTestRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * Exercises the feature-store REST endpoint through {@link RestTestHarness}, so requests go through
 * RestManager exactly as a client's would. A feature sent to /schema/feature-store/&lt;name&gt;
 * without a "store" attribute must land in the named store, not in the default store.
 */
public class TestManagedFeatureStoreRest extends SolrTestCase {

  private static final String COLLECTION = "collection1";

  @ClassRule public static SolrJettyTestRule solrTestRule = new SolrJettyTestRule();

  private static RestTestHarness restTestHarness;

  private static Path testHome() throws Exception {
    final URL url =
        TestManagedFeatureStoreRest.class.getClassLoader().getResource("solr/collection1");
    if (url == null) {
      throw new IllegalStateException("Cannot find solr/collection1 test resource");
    }
    return Path.of(url.toURI()).getParent();
  }

  @BeforeClass
  public static void beforeClass() throws Exception {
    // the ltr schema references randomized solr.tests.* properties
    SolrTestCaseJ4.newRandomConfig();
    final Path tmpSolrHome = createTempDir();
    PathUtils.copyDirectory(testHome(), tmpSolrHome);
    final Path confDir = tmpSolrHome.resolve(COLLECTION).resolve("conf");
    // the copied home must not carry managed data from a previous run
    Files.deleteIfExists(confDir.resolve("_schema_feature-store.json"));
    Files.deleteIfExists(confDir.resolve("_schema_model-store.json"));
    System.setProperty("managed.schema.mutable", "true");

    final Properties nodeProps = new Properties();
    nodeProps.setProperty("coreRootDirectory", createTempDir().toString());
    nodeProps.setProperty("configSetBaseDir", tmpSolrHome.toString());

    solrTestRule.startSolr(tmpSolrHome, nodeProps, JettyConfig.builder().build());
    solrTestRule
        .newCollection()
        .withConfigSet(COLLECTION)
        .withConfigFile("solrconfig-ltr.xml")
        .withSchemaFile("schema.xml")
        .create();

    restTestHarness = solrTestRule.getJetty().getRestClient(COLLECTION);
  }

  @AfterClass
  public static void afterClass() {
    restTestHarness = null;
  }

  private static String featureJson(String name) {
    return "{\"name\": \""
        + name
        + "\", \"class\": \""
        + ValueFeature.class.getName()
        + "\", \"params\": {\"value\": 1} }";
  }

  @SuppressWarnings("unchecked")
  private static List<Map<String, Object>> getFeatures(String store) throws Exception {
    final String response = restTestHarness.query(ManagedFeatureStore.REST_END_POINT + "/" + store);
    final Map<String, Object> map = (Map<String, Object>) Utils.fromJSONString(response);
    if (map.containsKey("error")) {
      return null; // no such store
    }
    return (List<Map<String, Object>>) map.get("features");
  }

  private static boolean hasFeature(List<Map<String, Object>> features, String name) {
    return features != null
        && features.stream().anyMatch(f -> name.equals(f.get(ManagedFeatureStore.NAME_KEY)));
  }

  private static void assertStatusOk(String response) {
    @SuppressWarnings("unchecked")
    final Map<String, Object> map = (Map<String, Object>) Utils.fromJSONString(response);
    @SuppressWarnings("unchecked")
    final Map<String, Object> header = (Map<String, Object>) map.get("responseHeader");
    assertNotNull("expected a responseHeader in: " + response, header);
    assertEquals(
        "expected status 0 in: " + response, 0, ((Number) header.get("status")).intValue());
  }

  @Test
  public void testPutFeatureWithoutStoreToNamedStore() throws Exception {
    final String store = "restPutStore";
    assertStatusOk(
        restTestHarness.put(
            ManagedFeatureStore.REST_END_POINT + "/" + store, featureJson("restPutFeature")));

    final List<Map<String, Object>> named = getFeatures(store);
    assertTrue(
        "feature sent to /" + store + " without a store attribute must land in that store",
        hasFeature(named, "restPutFeature"));
    assertEquals(store, named.get(0).get("store"));

    assertFalse(
        "feature must not land in the default store",
        hasFeature(getFeatures("_DEFAULT_"), "restPutFeature"));
  }

  @Test
  public void testPostFeatureWithoutStoreToNamedStore() throws Exception {
    final String store = "restPostStore";
    assertStatusOk(
        restTestHarness.post(
            ManagedFeatureStore.REST_END_POINT + "/" + store, featureJson("restPostFeature")));

    final List<Map<String, Object>> named = getFeatures(store);
    assertTrue(
        "feature sent to /" + store + " without a store attribute must land in that store",
        hasFeature(named, "restPostFeature"));
    assertEquals(store, named.get(0).get("store"));

    assertFalse(
        "feature must not land in the default store",
        hasFeature(getFeatures("_DEFAULT_"), "restPostFeature"));
  }
}
