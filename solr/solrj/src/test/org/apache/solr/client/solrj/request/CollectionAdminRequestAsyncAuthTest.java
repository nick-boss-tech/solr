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
package org.apache.solr.client.solrj.request;

import java.util.ArrayList;
import java.util.List;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

/** SOLR-14187: the async helpers must be able to carry per-request basic auth credentials. */
public class CollectionAdminRequestAsyncAuthTest extends SolrTestCase {

  /** Answers every request as a completed async status and records what was sent. */
  private static class RecordingClient extends SolrClient {
    final List<SolrRequest<?>> requests = new ArrayList<>();

    @Override
    public NamedList<Object> request(SolrRequest<?> request, String collection) {
      requests.add(request);
      NamedList<Object> status = new NamedList<>();
      status.add("state", "completed");
      NamedList<Object> response = new NamedList<>();
      response.add("status", status);
      return response;
    }

    @Override
    public void close() {}
  }

  @Test
  public void testWaitForAsyncRequestSendsCredentials() throws Exception {
    RecordingClient client = new RecordingClient();
    CollectionAdminRequest.waitForAsyncRequest("req-1", client, 5, "alice", "secret");

    assertEquals("REQUESTSTATUS poll and DELETESTATUS", 2, client.requests.size());
    for (SolrRequest<?> request : client.requests) {
      assertEquals("alice", request.getBasicAuthUser());
      assertEquals("secret", request.getBasicAuthPassword());
    }
  }

  @Test
  public void testWaitForAsyncRequestWithoutCredentialsIsUnchanged() throws Exception {
    RecordingClient client = new RecordingClient();
    CollectionAdminRequest.waitForAsyncRequest("req-2", client, 5);

    assertEquals(2, client.requests.size());
    for (SolrRequest<?> request : client.requests) {
      assertNull(request.getBasicAuthUser());
    }
  }
}
