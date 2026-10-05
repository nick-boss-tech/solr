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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.ltr.feature.Feature;
import org.apache.solr.ltr.feature.FeatureException;
import org.apache.solr.ltr.feature.OriginalScoreFeature;
import org.apache.solr.ltr.feature.ValueFeature;
import org.apache.solr.ltr.store.FeatureStore;
import org.apache.solr.rest.ManagedResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestManagedFeatureStore extends SolrTestCaseJ4 {

  private ManagedFeatureStore featureStore = null;

  @Before
  public void setup() throws Exception {
    initCore("solrconfig-ltr.xml", "schema.xml");
    featureStore = ManagedFeatureStore.getManagedFeatureStore(h.getCore());
  }

  @After
  public void cleanup() {
    featureStore = null;
    deleteCore();
  }

  private static Map<String, Object> createMap(
      String name, String className, Map<String, Object> params) {
    final Map<String, Object> map = new HashMap<>();
    map.put(ManagedFeatureStore.NAME_KEY, name);
    map.put(ManagedFeatureStore.CLASS_KEY, className);
    if (params != null) {
      map.put(ManagedFeatureStore.PARAMS_KEY, params);
    }
    return map;
  }

  @Test
  public void testDefaultFeatureStoreName() {
    assertEquals("_DEFAULT_", FeatureStore.DEFAULT_FEATURE_STORE_NAME);
    final FeatureStore expectedFeatureStore =
        featureStore.getFeatureStore(FeatureStore.DEFAULT_FEATURE_STORE_NAME);
    final FeatureStore actualFeatureStore = featureStore.getFeatureStore(null);
    assertEquals(
        "getFeatureStore(null) should return the default feature store",
        expectedFeatureStore,
        actualFeatureStore);
  }

  @Test
  public void testFeatureStoreAdd() throws FeatureException {
    final FeatureStore fs = featureStore.getFeatureStore("featureStore-testFeature");
    for (int i = 0; i < 5; i++) {
      final String name = "c" + i;

      featureStore.addFeature(
          createMap(name, OriginalScoreFeature.class.getName(), null), "featureStore-testFeature");

      final Feature f = fs.get(name);
      assertNotNull(f);
    }
    assertEquals(5, fs.getFeatures().size());
  }

  @Test
  public void testFeatureStoreGet() throws FeatureException {
    final FeatureStore fs = featureStore.getFeatureStore("featureStore-testFeature2");
    for (int i = 0; i < 5; i++) {
      Map<String, Object> params = new HashMap<>();
      params.put("value", i);
      final String name = "c" + i;

      featureStore.addFeature(
          createMap(name, ValueFeature.class.getName(), params), "featureStore-testFeature2");
    }

    for (int i = 0; i < 5; i++) {
      final Feature f = fs.get("c" + i);
      assertEquals("c" + i, f.getName());
      assertTrue(f instanceof ValueFeature);
      final ValueFeature vf = (ValueFeature) f;
      assertEquals(i, vf.getValue());
    }
  }

  private static List<Object> featureList(Map<String, Object> feature) {
    final List<Object> features = new ArrayList<>();
    features.add(feature);
    return features;
  }

  @Test
  public void testPutWithChildIdUsesItAsDefaultStore() {
    featureStore.doPut(
        null,
        featureList(createMap("f1", OriginalScoreFeature.class.getName(), null)),
        "childStore1");

    assertNotNull(featureStore.getFeatureStore("childStore1").get("f1"));
    final FeatureStore defaultStore =
        featureStore.getFeatureStore(FeatureStore.DEFAULT_FEATURE_STORE_NAME);
    assertTrue(defaultStore.getFeatures().isEmpty());
  }

  @Test
  public void testPutWithChildIdKeepsExplicitStore() {
    final Map<String, Object> feature = createMap("f2", OriginalScoreFeature.class.getName(), null);
    feature.put(ManagedFeatureStore.FEATURE_STORE_NAME_KEY, "explicitStore");

    featureStore.doPut(null, featureList(feature), "childStore2");

    assertNotNull(featureStore.getFeatureStore("explicitStore").get("f2"));
    assertNull(featureStore.getFeatureStore("childStore2").get("f2"));
  }

  @Test
  public void testPostAndEnvelopeWithChildId() {
    featureStore.doPost(
        null, createMap("f3", OriginalScoreFeature.class.getName(), null), "childStore3");
    assertNotNull(featureStore.getFeatureStore("childStore3").get("f3"));

    final Map<String, Object> envelope = new HashMap<>();
    envelope.put(ManagedResource.INIT_ARGS_JSON_FIELD, new HashMap<String, Object>());
    envelope.put(
        ManagedResource.MANAGED_JSON_LIST_FIELD,
        featureList(createMap("f4", OriginalScoreFeature.class.getName(), null)));
    featureStore.doPut(null, envelope, "childStore4");
    assertNotNull(featureStore.getFeatureStore("childStore4").get("f4"));
  }

  @Test
  public void testPutWithoutChildIdUsesDefaultStore() {
    featureStore.doPut(
        null, featureList(createMap("f5", OriginalScoreFeature.class.getName(), null)), null);

    assertNotNull(featureStore.getFeatureStore(FeatureStore.DEFAULT_FEATURE_STORE_NAME).get("f5"));
  }

  @Test
  public void testMissingFeatureReturnsNull() {
    final FeatureStore fs = featureStore.getFeatureStore("featureStore-testFeature3");
    for (int i = 0; i < 5; i++) {
      Map<String, Object> params = new HashMap<>();
      params.put("value", i);
      final String name = "testc" + (float) i;
      featureStore.addFeature(
          createMap(name, ValueFeature.class.getName(), params), "featureStore-testFeature3");
    }
    assertNull(fs.get("missing_feature_name"));
  }

  @Test
  public void getInstanceTest() throws FeatureException {
    featureStore.addFeature(
        createMap("test", OriginalScoreFeature.class.getName(), null), "testFeatureStore");
    final Feature feature = featureStore.getFeatureStore("testFeatureStore").get("test");
    assertNotNull(feature);
    assertEquals("test", feature.getName());
    assertEquals(OriginalScoreFeature.class.getName(), feature.getClass().getName());
  }

  @Test
  public void getInvalidInstanceTest() {
    final String nonExistingClassName = "org.apache.solr.ltr.feature.LOLFeature";
    final ClassNotFoundException expectedException =
        new ClassNotFoundException(nonExistingClassName);
    Exception ex =
        expectThrows(
            Exception.class,
            () -> {
              featureStore.addFeature(
                  createMap("test", nonExistingClassName, null), "testFeatureStore2");
            });
    Throwable rootError = getRootCause(ex);
    assertEquals(expectedException.toString(), rootError.toString());
  }
}
