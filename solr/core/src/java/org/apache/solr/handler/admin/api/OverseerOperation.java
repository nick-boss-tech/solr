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
import static org.apache.solr.security.PermissionNameProvider.Name.CORE_EDIT_PERM;

import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.solr.client.api.endpoint.OverseerOperationApi;
import org.apache.solr.client.api.model.OverseerOperationRequestBody;
import org.apache.solr.client.api.model.SolrJerseyResponse;
import org.apache.solr.common.params.CoreAdminParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.handler.admin.CoreAdminHandler;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * V2 API for rejoining the overseer leader election.
 *
 * <p>This API (POST /v2/node/overseer-op) is equivalent to the v1 /admin/cores?action=overseerop
 * command.
 */
public class OverseerOperation extends CoreAdminAPIBase implements OverseerOperationApi {

  @Inject
  public OverseerOperation(
      SolrQueryRequest solrQueryRequest,
      SolrQueryResponse solrQueryResponse,
      CoreContainer coreContainer,
      CoreAdminHandler.CoreAdminAsyncTracker coreAdminAsyncTracker) {
    super(coreContainer, coreAdminAsyncTracker, solrQueryRequest, solrQueryResponse);
  }

  @Override
  @PermissionName(CORE_EDIT_PERM)
  public SolrJerseyResponse joinOverseerLeaderElection(OverseerOperationRequestBody requestBody)
      throws Exception {
    ensureRequiredRequestBodyProvided(requestBody);
    ensureRequiredParameterProvided("op", requestBody.op);

    final Map<String, Object> v1Params = new HashMap<>();
    v1Params.put(
        ACTION, CoreAdminParams.CoreAdminAction.OVERSEEROP.name().toLowerCase(Locale.ROOT));
    v1Params.put("op", requestBody.op);
    if (requestBody.electionNode != null) {
      v1Params.put("electionNode", requestBody.electionNode);
    }

    return invokeCoreAdminAction(v1Params);
  }
}
