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
package org.apache.solr.handler.component;

import java.util.ArrayList;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.params.ShardParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.junit.BeforeClass;
import org.junit.Test;

/** Recording a failed shard in {@code shards.info} during the field retrieval phase. */
public class TestShardsInfoErrorRecording extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml", "schema.xml");
  }

  private static ShardRequest failedFieldsRequest(String shard, Exception failure) {
    final ShardRequest shardRequest = new ShardRequest();
    shardRequest.purpose = ShardRequest.PURPOSE_GET_FIELDS;
    final ShardResponse shardResponse = new ShardResponse();
    shardResponse.setShardRequest(shardRequest);
    shardResponse.setShard(shard);
    shardResponse.setException(failure);
    shardRequest.responses.add(shardResponse);
    return shardRequest;
  }

  @Test
  public void testFailureWithoutInfoEntryOrCauseIsRecorded() {
    try (SolrQueryRequest req = req("q", "*:*", ShardParams.SHARDS_INFO, "true")) {
      final SolrQueryResponse rsp = new SolrQueryResponse();
      rsp.add(ShardParams.SHARDS_INFO, new SimpleOrderedMap<Object>());
      final ResponseBuilder rb = new ResponseBuilder(req, rsp, new ArrayList<>());

      new QueryComponent()
          .returnFields(rb, failedFieldsRequest("shard1", new SolrServerException("shard down")));

      final NamedList<?> shardInfo = (NamedList<?>) rsp.getValues().get(ShardParams.SHARDS_INFO);
      final NamedList<?> entry = (NamedList<?>) shardInfo.get("shard1");
      assertNotNull(entry);
      assertNotNull(entry.get("error"));
    }
  }

  @Test
  public void testFailureWithoutShardNameIsNotRecordedUnderNull() {
    try (SolrQueryRequest req = req("q", "*:*", ShardParams.SHARDS_INFO, "true")) {
      final SolrQueryResponse rsp = new SolrQueryResponse();
      rsp.add(ShardParams.SHARDS_INFO, new SimpleOrderedMap<Object>());
      final ResponseBuilder rb = new ResponseBuilder(req, rsp, new ArrayList<>());

      new QueryComponent()
          .returnFields(rb, failedFieldsRequest(null, new SolrServerException("shard down")));

      final NamedList<?> shardInfo = (NamedList<?>) rsp.getValues().get(ShardParams.SHARDS_INFO);
      assertEquals(0, shardInfo.size());
    }
  }

  @Test
  public void testFailureWithoutShardsInfoSectionDoesNotThrow() {
    try (SolrQueryRequest req = req("q", "*:*", ShardParams.SHARDS_INFO, "true")) {
      final ResponseBuilder rb =
          new ResponseBuilder(req, new SolrQueryResponse(), new ArrayList<>());

      new QueryComponent()
          .returnFields(rb, failedFieldsRequest("shard1", new SolrServerException("shard down")));
    }
  }

  @Test
  public void testExistingErrorEntryIsKept() {
    try (SolrQueryRequest req = req("q", "*:*", ShardParams.SHARDS_INFO, "true")) {
      final SolrQueryResponse rsp = new SolrQueryResponse();
      final SimpleOrderedMap<Object> shardInfo = new SimpleOrderedMap<>();
      final SimpleOrderedMap<Object> existing = new SimpleOrderedMap<>();
      existing.add("error", "first failure");
      shardInfo.add("shard1", existing);
      rsp.add(ShardParams.SHARDS_INFO, shardInfo);
      final ResponseBuilder rb = new ResponseBuilder(req, rsp, new ArrayList<>());

      new QueryComponent()
          .returnFields(rb, failedFieldsRequest("shard1", new SolrServerException("later")));

      assertEquals(1, shardInfo.size());
      assertEquals("first failure", existing.get("error"));
      assertEquals(1, existing.getAll("error").size());
    }
  }
}
