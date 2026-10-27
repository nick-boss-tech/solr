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
package org.apache.solr.search.facet;

import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CoreAdminParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.junit.BeforeClass;
import org.junit.Test;

public class SortableTextNestedStatsCloudTest extends SolrCloudTestCase {

  private static final String COLLECTION = "sortable_text_nested_stats";
  private static final String CONF = COLLECTION + "_configSet";

  private static final Map<String, Double> EXPECTED_AVG =
      Map.of("Alpha grp", 15.0, "Beta grp", 40.0, "Gamma grp", 7.0);
  private static final Map<String, Double> EXPECTED_SUM =
      Map.of("Alpha grp", 30.0, "Beta grp", 80.0, "Gamma grp", 7.0);
  private static final Map<String, Double> EXPECTED_MIN =
      Map.of("Alpha grp", 10.0, "Beta grp", 30.0, "Gamma grp", 7.0);

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1)
        .addConfig(CONF, TEST_HOME().resolve("collection1").resolve("conf"))
        .configure();

    assertEquals(
        0,
        CollectionAdminRequest.createCollection(COLLECTION, CONF, 1, 1)
            .setProperties(Map.of(CoreAdminParams.CONFIG, "solrconfig-minimal.xml"))
            .process(cluster.getSolrClient())
            .getStatus());
    cluster.waitForActiveCollection(COLLECTION, 1, 1);

    UpdateRequest req = new UpdateRequest();
    req.add(doc("1", "Alpha grp", 10.0));
    req.add(doc("2", "Alpha grp", 20.0));
    req.add(doc("3", "Beta grp", 30.0));
    req.add(doc("4", "Beta grp", 50.0));
    req.add(doc("5", "Gamma grp", 7.0));
    req.commit(cluster.getSolrClient(), COLLECTION);
  }

  private static SolrInputDocument doc(String id, String grp, double price) {
    SolrInputDocument d = new SolrInputDocument();
    d.addField("id", id);
    d.addField("grp_sortable", grp);
    d.addField("grp_s", grp);
    d.addField("price", price);
    return d;
  }

  @Test
  public void testNestedStatsOverSortableTextMatchStringControl() throws Exception {
    assertBucketStats("grp_s");
    assertBucketStats("grp_sortable");
  }

  private static void assertBucketStats(String field) throws Exception {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("q", "*:*");
    params.set("rows", "0");
    params.set(
        "json.facet",
        "{\"cats\":{\"type\":\"terms\",\"field\":\""
            + field
            + "\",\"facet\":{\"avgp\":\"avg(price)\",\"sump\":\"sum(price)\",\"minp\":\"min(price)\"}}}");

    QueryResponse rsp = cluster.getSolrClient().query(COLLECTION, params);
    NamedList<?> facets = (NamedList<?>) rsp.getResponse().get("facets");
    NamedList<?> cats = (NamedList<?>) facets.get("cats");
    List<?> buckets = (List<?>) cats.get("buckets");
    assertEquals("field " + field, EXPECTED_AVG.size(), buckets.size());
    for (Object o : buckets) {
      NamedList<?> b = (NamedList<?>) o;
      String val = (String) b.get("val");
      assertEquals(
          "avg " + field + " " + val,
          EXPECTED_AVG.get(val),
          ((Number) b.get("avgp")).doubleValue(),
          0.0);
      assertEquals(
          "sum " + field + " " + val,
          EXPECTED_SUM.get(val),
          ((Number) b.get("sump")).doubleValue(),
          0.0);
      assertEquals(
          "min " + field + " " + val,
          EXPECTED_MIN.get(val),
          ((Number) b.get("minp")).doubleValue(),
          0.0);
    }
  }
}
