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

import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.common.SolrDocument;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * SOLR-14931: macros in a request handler's {@code appends} must be expanded on the shard that
 * serves a distributed request, using the request parameters as the macro source. The {@code
 * cloud-macro-appends} configset appends {@code appended:'${my_term}'} to {@code fl} on {@code
 * /select}; the value visible in the final response is the one computed by the shard.
 */
public class TestShardMacroExpansion extends SolrCloudTestCase {

  private static final String COLLECTION = "macrotest";

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2)
        .addConfig(
            "conf", TEST_PATH().resolve("configsets").resolve("cloud-macro-appends").resolve("conf"))
        .configure();

    CollectionAdminRequest.createCollection(COLLECTION, "conf", 2, 1)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection(COLLECTION, 2, 2);
  }

  @Test
  public void testAppendsMacroIsExpandedOnShardRequests() throws Exception {
    cluster.getSolrClient().add(COLLECTION, sdoc("id", "1", "title_s", "hello"));
    cluster.getSolrClient().commit(COLLECTION);

    SolrQuery query = new SolrQuery("id:1");
    query.set("my_term", "foobar");
    query.setFields("id");
    QueryResponse rsp = cluster.getSolrClient().query(COLLECTION, query);

    assertEquals(1, rsp.getResults().getNumFound());
    SolrDocument doc = rsp.getResults().get(0);
    assertEquals(
        "the shard must expand the appends macro instead of returning it literally",
        "foobar",
        doc.getFieldValue("appended"));
  }
}
