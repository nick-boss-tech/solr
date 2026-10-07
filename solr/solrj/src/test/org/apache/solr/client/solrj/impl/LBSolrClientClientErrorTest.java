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
package org.apache.solr.client.solrj.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

/** A 403/404 answer is retried on another server but must not mark the first server a zombie. */
public class LBSolrClientClientErrorTest extends SolrTestCase {

  private static final LBSolrClient.Endpoint HOST_1 =
      new LBSolrClient.Endpoint("http://127.0.0.1:1/solr");
  private static final LBSolrClient.Endpoint HOST_2 =
      new LBSolrClient.Endpoint("http://127.0.0.1:2/solr");

  /** The first endpoint tried answers with {@code code}; any later endpoint succeeds. */
  private static class FailFirstWithCode extends LBSolrClient {
    final List<String> attempted = new ArrayList<>();
    private final int code;

    FailFirstWithCode(int code) {
      super(List.of(HOST_1, HOST_2));
      this.code = code;
    }

    @Override
    protected SolrClient getClient(Endpoint endpoint) {
      return new SolrClient() {
        @Override
        public NamedList<Object> request(SolrRequest<?> request, String collection)
            throws SolrServerException, IOException {
          attempted.add(endpoint.getBaseUrl());
          if (attempted.size() > 1) {
            return new NamedList<>();
          }
          throw new SolrException(SolrException.ErrorCode.getErrorCode(code), "status " + code);
        }

        @Override
        public void close() {}
      };
    }
  }

  private static FailFirstWithCode queryAfterFailing(int code) throws Exception {
    try (FailFirstWithCode client = new FailFirstWithCode(code)) {
      client.request(new LBSolrClient.Req(new QueryRequest(), List.of(HOST_1, HOST_2)));
      assertEquals(2, client.attempted.size());
      return client;
    }
  }

  @Test
  public void testNotFoundIsRetriedWithoutZombie() throws Exception {
    assertTrue(queryAfterFailing(404).zombieServers.isEmpty());
  }

  @Test
  public void testForbiddenIsRetriedWithoutZombie() throws Exception {
    assertTrue(queryAfterFailing(403).zombieServers.isEmpty());
  }

  @Test
  public void testServiceUnavailableStillMarksZombie() throws Exception {
    assertEquals(1, queryAfterFailing(503).zombieServers.size());
  }
}
