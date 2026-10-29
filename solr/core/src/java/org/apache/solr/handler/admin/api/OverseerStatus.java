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

import static org.apache.solr.common.params.CommonParams.ACTION;
import static org.apache.solr.common.params.CommonParams.COLLECTIONS_HANDLER_PATH;
import static org.apache.solr.handler.ClusterAPI.wrapParams;
import static org.apache.solr.security.PermissionNameProvider.Name.COLL_READ_PERM;

import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.solr.client.api.endpoint.OverseerStatusApi;
import org.apache.solr.client.api.model.OverseerStatusResponse;
import org.apache.solr.common.params.CollectionParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.handler.admin.CollectionsHandler;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * V2 API for the overseer's status.
 *
 * <p>This API (GET /v2/cluster/overseer/status) is analogous to the v1
 * /admin/collections?action=OVERSEERSTATUS command; the status is produced by {@link
 * CollectionsHandler}, so both APIs serve the same content.
 */
public class OverseerStatus extends AdminAPIBase implements OverseerStatusApi {

  @Inject
  public OverseerStatus(
      CoreContainer coreContainer,
      SolrQueryRequest solrQueryRequest,
      SolrQueryResponse solrQueryResponse) {
    super(coreContainer, solrQueryRequest, solrQueryResponse);
  }

  @Override
  @PermissionName(COLL_READ_PERM)
  public OverseerStatusResponse getOverseerStatus() throws Exception {
    fetchAndValidateZooKeeperAwareCoreContainer();

    final Map<String, Object> v1Params = new HashMap<>();
    v1Params.put(ACTION, CollectionParams.CollectionAction.OVERSEERSTATUS.lowerName);

    final var collectionsHandler =
        (CollectionsHandler) coreContainer.getRequestHandler(COLLECTIONS_HANDLER_PATH);
    collectionsHandler.handleRequestBody(
        wrapParams(solrQueryRequest, v1Params), solrQueryResponse);
    if (solrQueryResponse.getException() != null) {
      throw solrQueryResponse.getException();
    }

    final var response = instantiateJerseyResponse(OverseerStatusResponse.class);
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
