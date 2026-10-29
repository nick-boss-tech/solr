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
import static org.apache.solr.common.params.CommonParams.NAME;
import static org.apache.solr.handler.ClusterAPI.wrapParams;
import static org.apache.solr.handler.api.V2ApiUtils.flattenMapWithPrefix;
import static org.apache.solr.security.PermissionNameProvider.Name.COLL_EDIT_PERM;

import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.apache.solr.client.api.endpoint.ReindexCollectionApi;
import org.apache.solr.client.api.model.ReindexCollectionRequestBody;
import org.apache.solr.client.api.model.ReindexCollectionResponse;
import org.apache.solr.cloud.api.collections.CollectionHandlingUtils;
import org.apache.solr.cloud.api.collections.ReindexCollectionCmd;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.params.CollectionAdminParams;
import org.apache.solr.common.params.CollectionParams;
import org.apache.solr.common.params.CommonAdminParams;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.handler.admin.CollectionsHandler;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * V2 API for reindexing a collection.
 *
 * <p>This API (POST /v2/collections/{collectionName}/reindex) is analogous to the v1
 * /admin/collections?action=REINDEXCOLLECTION command. The request is translated to the same
 * parameters the v1 API produced and run through {@link CollectionsHandler}, so both APIs share
 * one implementation.
 */
public class ReindexCollection extends AdminAPIBase implements ReindexCollectionApi {

  @Inject
  public ReindexCollection(
      CoreContainer coreContainer,
      SolrQueryRequest solrQueryRequest,
      SolrQueryResponse solrQueryResponse) {
    super(coreContainer, solrQueryRequest, solrQueryResponse);
  }

  @Override
  @PermissionName(COLL_EDIT_PERM)
  @SuppressWarnings("unchecked")
  public ReindexCollectionResponse reindexCollection(
      String collectionName, ReindexCollectionRequestBody requestBody) throws Exception {
    ensureRequiredParameterProvided(NAME, collectionName);
    fetchAndValidateZooKeeperAwareCoreContainer();
    recordCollectionForLogAndTracing(collectionName, solrQueryRequest);
    if (requestBody == null) {
      requestBody = new ReindexCollectionRequestBody();
    }

    final Map<String, Object> v1Params = new HashMap<>();
    v1Params.put(ACTION, CollectionParams.CollectionAction.REINDEXCOLLECTION.lowerName);
    v1Params.put(NAME, collectionName);
    if (requestBody.cmd != null) {
      v1Params.put(ReindexCollectionCmd.COMMAND, requestBody.cmd);
    }
    if (requestBody.removeSource != null) {
      v1Params.put(ReindexCollectionCmd.REMOVE_SOURCE, requestBody.removeSource);
    }
    if (requestBody.target != null) {
      v1Params.put(ReindexCollectionCmd.TARGET, requestBody.target);
    }
    if (requestBody.configName != null) {
      v1Params.put(ZkStateReader.CONFIGNAME_PROP, requestBody.configName);
    }
    if (requestBody.numShards != null) {
      v1Params.put(CollectionHandlingUtils.NUM_SLICES, requestBody.numShards);
    }
    if (requestBody.replicationFactor != null) {
      v1Params.put(ZkStateReader.REPLICATION_FACTOR, requestBody.replicationFactor);
    }
    if (requestBody.shards != null) {
      v1Params.put("shards", requestBody.shards);
    }
    if (requestBody.rows != null) {
      v1Params.put(CommonParams.ROWS, requestBody.rows);
    }
    if (requestBody.query != null) {
      v1Params.put(CommonParams.Q, requestBody.query);
    }
    if (requestBody.fields != null) {
      v1Params.put(CommonParams.FL, requestBody.fields);
    }
    if (requestBody.followAliases != null) {
      v1Params.put(CollectionAdminParams.FOLLOW_ALIASES, requestBody.followAliases);
    }
    if (requestBody.async != null) {
      v1Params.put(CommonAdminParams.ASYNC, requestBody.async);
    }
    if (requestBody.routerProperties != null && !requestBody.routerProperties.isEmpty()) {
      flattenMapWithPrefix(requestBody.routerProperties, v1Params, "router.");
    }

    final var collectionsHandler =
        (CollectionsHandler) coreContainer.getRequestHandler(COLLECTIONS_HANDLER_PATH);
    collectionsHandler.handleRequestBody(
        wrapParams(solrQueryRequest, v1Params), solrQueryResponse);
    if (solrQueryResponse.getException() != null) {
      throw solrQueryResponse.getException();
    }

    final var response = instantiateJerseyResponse(ReindexCollectionResponse.class);
    response.success = (Map<String, Object>) solrQueryResponse.getValues().get("success");
    return response;
  }
}
