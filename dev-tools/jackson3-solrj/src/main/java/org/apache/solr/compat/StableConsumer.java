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

package org.apache.solr.compat;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.impl.HttpJdkSolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.request.JavaBinRequestWriter;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;

/** Consumer fixture using ordinary SolrJ APIs and no Jackson runtime types. */
public class StableConsumer {
  public static void main(String[] args) throws Exception {
    var query = new SolrQuery("id:compatibility").setRows(1);
    require("id:compatibility".equals(new QueryRequest(query).getParams().get("q")), "query");

    var input = new SolrInputDocument();
    input.addField("id", "compatibility");
    var update = new UpdateRequest().add(input);
    var bytes = new ByteArrayOutputStream();
    new JavaBinRequestWriter().getContentWriter(update).write(bytes);
    require(bytes.size() > 0, "JavaBin update serialization");

    var document = new SolrDocument();
    document.setField("id", "compatibility");
    var documents = new SolrDocumentList();
    documents.setNumFound(1);
    documents.add(document);
    var responseData = new NamedList<Object>();
    responseData.add("response", documents);
    var response = new QueryResponse();
    response.setResponse(responseData);
    require(response.getResults().getNumFound() == 1, "query result count");
    require("compatibility".equals(response.getResults().get(0).getFieldValue("id")), "query result");

    var coreData = new NamedList<Object>();
    coreData.add("status", Map.of("compatibility", Map.of(
        "name", "compatibility", "startTime", "2026-01-02T03:04:05Z", "uptime", 1234L)));
    var coreResponse = new CoreAdminResponse();
    coreResponse.setResponse(coreData);
    require(coreResponse.getStartTime("compatibility").toInstant()
        .equals(Instant.parse("2026-01-02T03:04:05Z")), "CoreAdminResponse date");
    require(Long.valueOf(1234).equals(coreResponse.getUptime("compatibility")), "core uptime");
    require(coreResponse.getStartTime("absent") == null, "missing core");

    if (args.length != 0) {
      require(args.length == 2, "expected server URL and disposable collection");
      http(args[0], args[1]);
    }
    System.out.println("PASS ordinary SolrJ query/update/response/date consumer");
  }

  private static void http(String server, String collection) throws Exception {
    String id = "jackson3-compat-" + UUID.randomUUID();
    try (var client = new HttpJdkSolrClient.Builder(server)
        .withConnectionTimeout(10, TimeUnit.SECONDS)
        .withRequestTimeout(30, TimeUnit.SECONDS).build()) {
      var status = CoreAdminRequest.getStatus(collection, client);
      require(status.getStartTime(collection) != null, "HTTP core start time");
      require(status.getStartTime(collection).getTime() > 0, "HTTP core date conversion");
      require(status.getUptime(collection) != null, "HTTP core uptime conversion");
      require(status.getUptime(collection) >= 0, "HTTP core uptime");
      System.out.println("PASS HTTP CoreAdminResponse startTime Date and uptime Long conversion");
      try {
        var input = new SolrInputDocument();
        input.addField("id", id);
        require(client.add(collection, input).getStatus() == 0, "HTTP update status");
        require(client.commit(collection).getStatus() == 0, "HTTP commit status");
        var found = client.query(collection, new SolrQuery("id:" + id));
        require(found.getResults().getNumFound() == 1, "HTTP query count");
        require(id.equals(found.getResults().get(0).getFieldValue("id")), "HTTP query id");
      } finally {
        client.deleteById(collection, id);
        client.commit(collection);
      }
    }
    System.out.println("PASS HTTP add/commit/query/delete against " + server + "/" + collection);
  }

  private static void require(boolean condition, String description) {
    if (!condition) throw new AssertionError(description);
  }
}
