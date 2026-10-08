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

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.util.NamedList;

public class GenericSolrRequestTest extends SolrTestCase {

  private static final String ROOT_URL = "http://localhost:8983/solr";

  /** A client that records the URL a root-URL based client would build for the request. */
  private static class UrlRecordingClient extends SolrClient {
    final AtomicReference<String> url = new AtomicReference<>();

    @Override
    public NamedList<Object> request(SolrRequest<?> request, String collection) throws IOException {
      url.set(ClientUtils.buildRequestUrl(request, ROOT_URL, collection));
      return new NamedList<>();
    }

    @Override
    public void close() {}
  }

  public void testProcessWithCollectionTargetsThatCollection() throws Exception {
    final UrlRecordingClient client = new UrlRecordingClient();
    new GenericSolrRequest(SolrRequest.METHOD.GET, "/select").process(client, "coll1");
    assertEquals(ROOT_URL + "/coll1/select", client.url.get());
  }

  public void testProcessWithoutCollectionStaysAtRoot() throws Exception {
    final UrlRecordingClient client = new UrlRecordingClient();
    new GenericSolrRequest(SolrRequest.METHOD.GET, "/admin/info/system").process(client);
    assertEquals(ROOT_URL + "/admin/info/system", client.url.get());
  }

  public void testExplicitRequiresCollectionFalseIsHonored() throws Exception {
    final UrlRecordingClient client = new UrlRecordingClient();
    new GenericSolrRequest(SolrRequest.METHOD.GET, "/admin/info/system")
        .setRequiresCollection(false)
        .process(client, "coll1");
    assertEquals(ROOT_URL + "/admin/info/system", client.url.get());
  }

  public void testDirectFieldWriteFalseDoesNotOptOut() throws Exception {
    final UrlRecordingClient client = new UrlRecordingClient();
    GenericSolrRequest req = new GenericSolrRequest(SolrRequest.METHOD.GET, "/select");
    req.requiresCollection = false; // direct write to the public field; not tracked
    req.process(client, "coll1");
    assertEquals(ROOT_URL + "/coll1/select", client.url.get());
  }
}
