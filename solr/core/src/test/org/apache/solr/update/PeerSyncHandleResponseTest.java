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

package org.apache.solr.update;

import java.util.List;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrException;
import org.apache.solr.handler.component.ShardRequest;
import org.apache.solr.handler.component.ShardResponse;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for {@link PeerSync#handleResponse}. These live outside {@link PeerSyncTest} because
 * that suite calls initCore from its constructor, so every additional test method in it leaks a
 * harness core.
 */
public class PeerSyncHandleResponseTest extends SolrTestCase {

  @BeforeClass
  public static void beforeClass() throws Exception {
    SolrTestCaseJ4.newRandomConfig();
    initCore("solrconfig-tlog.xml", "schema.xml");
  }

  @Test
  public void testPeerSyncIgnores500FromVersionRequestWhenCantReachIsSuccess() throws Exception {
    try (PeerSync peerSync =
        new PeerSync(h.getCore(), List.of("http://example.com/solr/core"), 10, true)) {
      ShardResponse response =
          failedResponse(
              PeerSync.SHARD_REQUEST_PURPOSE_GET_VERSIONS,
              new SolrException(SolrException.ErrorCode.SERVER_ERROR, "boom"));

      assertTrue(peerSync.handleResponse(response));
    }
  }

  @Test
  public void testPeerSyncStillFails500FromUpdateRequest() throws Exception {
    try (PeerSync peerSync =
        new PeerSync(h.getCore(), List.of("http://example.com/solr/core"), 10, true)) {
      ShardResponse response =
          failedResponse(
              PeerSync.SHARD_REQUEST_PURPOSE_GET_UPDATES,
              new SolrException(SolrException.ErrorCode.SERVER_ERROR, "boom"));

      assertFalse(peerSync.handleResponse(response));
    }
  }

  private static ShardResponse failedResponse(int purpose, Throwable exception) {
    ShardRequest request = new ShardRequest();
    request.purpose = purpose;

    // ShardResponse is final, so it cannot be mocked; build a real one.
    ShardResponse response = new ShardResponse();
    response.setShardRequest(request);
    response.setException(exception);
    response.setShardAddress("http://example.com/solr/core");
    return response;
  }
}
