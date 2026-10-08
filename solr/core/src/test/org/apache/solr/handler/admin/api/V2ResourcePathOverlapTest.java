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
 */ package org.apache.solr.handler.admin.api;

import java.util.List;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.V2Request;
import org.apache.solr.client.solrj.response.V2Response;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Exercises v2 endpoints over HTTP whose JAX-RS resource classes have overlapping class-level
 * paths. Calling the handler classes directly (as the mock-based API tests do) can't catch a
 * resource that is unreachable because a sibling resource class claimed the same path.
 */
public class V2ResourcePathOverlapTest extends SolrCloudTestCase {

  private static final String COLLECTION = "overlapColl";
  private static final String ALIAS = "overlapAlias";

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1).addConfig("conf", configset("cloud-minimal")).configure();
    CollectionAdminRequest.createCollection(COLLECTION, "conf", 1, 1)
        .process(cluster.getSolrClient());
    CollectionAdminRequest.createAlias(ALIAS, COLLECTION).process(cluster.getSolrClient());
  }

  @Test
  public void testGetAliasByNameIsReachable() throws Exception {
    // GET /aliases/{name} shares its path with the DELETE /aliases/{name} resource; it must not be
    // answered with a 405 by that resource.
    final V2Response response =
        new V2Request.Builder("/aliases/" + ALIAS)
            .withMethod(SolrRequest.METHOD.GET)
            .build()
            .process(cluster.getSolrClient());
    assertEquals(ALIAS, response.getResponse().get("alias"));
    assertEquals(List.of(COLLECTION), response.getResponse().get("collections"));
  }

  @Test
  public void testCreateCollectionSnapshotIsReachable() throws Exception {
    // POST /collections/{coll}/snapshots/{name} shares its path with the DELETE resource.
    final String snapshotName = "overlapSnapshot";
    new V2Request.Builder("/collections/" + COLLECTION + "/snapshots/" + snapshotName)
        .withMethod(SolrRequest.METHOD.POST)
        .withPayload("{}")
        .build()
        .process(cluster.getSolrClient());

    final V2Response listResponse =
        new V2Request.Builder("/collections/" + COLLECTION + "/snapshots")
            .withMethod(SolrRequest.METHOD.GET)
            .build()
            .process(cluster.getSolrClient());
    assertTrue(
        listResponse.getResponse().toString(),
        listResponse.getResponse().toString().contains(snapshotName));

    new V2Request.Builder("/collections/" + COLLECTION + "/snapshots/" + snapshotName)
        .withMethod(SolrRequest.METHOD.DELETE)
        .build()
        .process(cluster.getSolrClient());
  }
}
