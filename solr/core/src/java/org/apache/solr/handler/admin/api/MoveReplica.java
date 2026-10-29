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

import static org.apache.solr.common.cloud.ZkStateReader.COLLECTION_PROP;
import static org.apache.solr.common.cloud.ZkStateReader.REPLICA_PROP;
import static org.apache.solr.common.cloud.ZkStateReader.SHARD_ID_PROP;
import static org.apache.solr.security.PermissionNameProvider.Name.COLL_EDIT_PERM;

import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.apache.solr.client.api.endpoint.MoveReplicaApi;
import org.apache.solr.client.api.model.MoveReplicaRequestBody;
import org.apache.solr.client.api.model.SubResponseAccumulatingJerseyResponse;
import org.apache.solr.common.cloud.ZkNodeProps;
import org.apache.solr.common.params.CollectionParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * V2 API for moving a replica from one node to another.
 *
 * <p>This API (POST /v2/collections/collectionName/shards/shardName/replicas/replicaName/move) is
 * analogous to the v1 /admin/collections?action=MOVEREPLICA command.
 */
public class MoveReplica extends AdminAPIBase implements MoveReplicaApi {

  @Inject
  public MoveReplica(
      CoreContainer coreContainer,
      SolrQueryRequest solrQueryRequest,
      SolrQueryResponse solrQueryResponse) {
    super(coreContainer, solrQueryRequest, solrQueryResponse);
  }

  @Override
  @PermissionName(COLL_EDIT_PERM)
  public SubResponseAccumulatingJerseyResponse moveReplica(
      String collectionName,
      String shardName,
      String replicaName,
      MoveReplicaRequestBody requestBody)
      throws Exception {
    final var response = instantiateJerseyResponse(SubResponseAccumulatingJerseyResponse.class);
    fetchAndValidateZooKeeperAwareCoreContainer();
    ensureRequiredParameterProvided(COLLECTION_PROP, collectionName);
    ensureRequiredParameterProvided(SHARD_ID_PROP, shardName);
    ensureRequiredParameterProvided(REPLICA_PROP, replicaName);
    recordCollectionForLogAndTracing(collectionName, solrQueryRequest);
    if (requestBody == null) {
      requestBody = new MoveReplicaRequestBody();
    }

    final Map<String, Object> messageProps = new HashMap<>();
    messageProps.put(COLLECTION_PROP, collectionName);
    messageProps.put(SHARD_ID_PROP, shardName);
    messageProps.put(REPLICA_PROP, replicaName);
    insertIfNotNull(messageProps, "targetNode", requestBody.targetNode);
    insertIfNotNull(messageProps, "sourceNode", requestBody.sourceNode);
    insertIfNotNull(messageProps, "timeout", requestBody.timeout);
    insertIfNotNull(messageProps, "inPlaceMove", requestBody.inPlaceMove);
    insertIfNotNull(messageProps, "followAliases", requestBody.followAliases);

    submitRemoteMessageAndHandleResponse(
        response,
        CollectionParams.CollectionAction.MOVEREPLICA,
        new ZkNodeProps(messageProps),
        null);
    return response;
  }
}
