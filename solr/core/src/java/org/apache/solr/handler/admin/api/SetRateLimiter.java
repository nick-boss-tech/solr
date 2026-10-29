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

import static org.apache.solr.core.RateLimiterConfig.RL_CONFIG_KEY;
import static org.apache.solr.security.PermissionNameProvider.Name.COLL_EDIT_PERM;

import jakarta.inject.Inject;
import org.apache.solr.client.api.endpoint.SetRateLimiterApi;
import org.apache.solr.client.api.model.SetRateLimiterRequestBody;
import org.apache.solr.client.api.model.SolrJerseyResponse;
import org.apache.solr.client.solrj.request.beans.RateLimiterPayload;
import org.apache.solr.common.cloud.ClusterProperties;
import org.apache.solr.common.SolrException;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.common.cloud.ClusterProperties;

/**
 * V2 API for setting the cluster request rate-limiter configuration.
 *
 * <p>This API (PUT /v2/cluster/rate-limiters) is analogous to the v1 POST /cluster
 * set-ratelimiter command; both write the configuration to the same cluster property.
 */
public class SetRateLimiter extends AdminAPIBase implements SetRateLimiterApi {

  @Inject
  public SetRateLimiter(
      CoreContainer coreContainer,
      SolrQueryRequest solrQueryRequest,
      SolrQueryResponse solrQueryResponse) {
    super(coreContainer, solrQueryRequest, solrQueryResponse);
  }

  @Override
  @PermissionName(COLL_EDIT_PERM)
  public SolrJerseyResponse setRateLimiter(SetRateLimiterRequestBody requestBody) {
    fetchAndValidateZooKeeperAwareCoreContainer();

    final var payload = new RateLimiterPayload();
    if (requestBody != null) {
      payload.enabled = requestBody.enabled;
      payload.guaranteedSlots = requestBody.guaranteedSlots;
      payload.allowedRequests = requestBody.allowedRequests;
      payload.slotBorrowingEnabled = requestBody.slotBorrowingEnabled;
      payload.slotAcquisitionTimeoutInMS = requestBody.slotAcquisitionTimeoutInMS;
    }

    final var clusterProperties =
        new ClusterProperties(coreContainer.getZkController().getZkClient());
    try {
      clusterProperties.update(payload, RL_CONFIG_KEY);
    } catch (Exception e) {
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Error in API", e);
    }
    return instantiateJerseyResponse(SolrJerseyResponse.class);
  }
}
