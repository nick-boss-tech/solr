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
package org.apache.solr.cloud;

import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.request.V2Request;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestCloneConfigSetWithoutBase extends SolrCloudTestCase {

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1).configure();
  }

  @Test
  public void testOmittedBaseCreatesConfigSetThatLoadsACore() throws Exception {
    new V2Request.Builder("/configsets")
        .withMethod(SolrRequest.METHOD.POST)
        .withPayload("{\"name\":\"no-base\"}")
        .build()
        .process(cluster.getSolrClient());

    assertTrue(
        cluster
            .getOpenOverseer()
            .getCoreContainer()
            .getConfigSetService()
            .checkConfigExists("no-base"));

    CollectionAdminRequest.createCollection("no-base-coll", "no-base", 1, 1)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection("no-base-coll", 1, 1);

    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", "1");
    new UpdateRequest().add(doc).commit(cluster.getSolrClient(), "no-base-coll");
    QueryResponse rsp =
        cluster.getSolrClient().query("no-base-coll", new ModifiableSolrParams().set("q", "*:*"));
    assertEquals(1, rsp.getResults().getNumFound());
  }
}
