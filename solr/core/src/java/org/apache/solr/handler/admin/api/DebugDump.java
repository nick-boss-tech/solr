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
package org.apache.solr.handler.admin.api;

import static org.apache.solr.security.PermissionNameProvider.Name.CORE_READ_PERM;

import jakarta.inject.Inject;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.solr.client.api.endpoint.DebugDumpApi;
import org.apache.solr.client.api.model.DebugDumpResponse;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.DumpRequestHandler;
import org.apache.solr.api.JerseyResource;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * V2 API for dumping a request's parameters and context.
 *
 * <p>This API (GET /v2/cores/{coreName}/debug/dump) is analogous to the v1 /debug/dump API; the
 * content is produced by {@link DumpRequestHandler}, so both APIs serve the same dump.
 */
public class DebugDump extends JerseyResource implements DebugDumpApi {

  private final SolrCore solrCore;
  private final SolrQueryRequest solrQueryRequest;
  private final SolrQueryResponse solrQueryResponse;

  @Inject
  public DebugDump(
      SolrCore solrCore, SolrQueryRequest solrQueryRequest, SolrQueryResponse solrQueryResponse) {
    this.solrCore = solrCore;
    this.solrQueryRequest = solrQueryRequest;
    this.solrQueryResponse = solrQueryResponse;
  }

  @Override
  @PermissionName(CORE_READ_PERM)
  public DebugDumpResponse dumpRequest() throws Exception {
    final var dumpHandler = (DumpRequestHandler) solrCore.getRequestHandler("/debug/dump");
    dumpHandler.handleRequestBody(solrQueryRequest, solrQueryResponse);
    if (solrQueryResponse.getException() != null) {
      throw solrQueryResponse.getException();
    }

    final var response = instantiateJerseyResponse(DebugDumpResponse.class);
    solrQueryResponse
        .getValues()
        .forEach(
            (name, value) -> {
              if (!"responseHeader".equals(name)) {
                response.values.put(name, convertValue(value));
              }
            });
    return response;
  }

  private static Object convertValue(Object value) {
    if (value instanceof NamedList<?> namedList) {
      final Map<String, Object> map = new LinkedHashMap<>();
      namedList.forEach((name, v) -> map.put(name, convertValue(v)));
      return map;
    }
    return value;
  }
}
