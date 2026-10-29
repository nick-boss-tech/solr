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

import static org.apache.solr.common.params.CommonParams.ZK_STATUS_PATH;
import static org.apache.solr.security.PermissionNameProvider.Name.ZK_READ_PERM;

import jakarta.inject.Inject;
import org.apache.solr.client.api.endpoint.ZookeeperStatusApi;
import org.apache.solr.client.api.model.ZookeeperStatusResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.handler.admin.ZookeeperStatusHandler;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * V2 API for the status of the ZooKeeper ensemble.
 *
 * <p>This API (GET /v2/cluster/zookeeper/status) is analogous to the v1 /admin/zookeeper/status
 * API; the status content is built by {@link ZookeeperStatusHandler}, the v1 handler.
 */
public class ZookeeperStatus extends AdminAPIBase implements ZookeeperStatusApi {

  @Inject
  public ZookeeperStatus(
      CoreContainer coreContainer,
      SolrQueryRequest solrQueryRequest,
      SolrQueryResponse solrQueryResponse) {
    super(coreContainer, solrQueryRequest, solrQueryResponse);
  }

  @Override
  @PermissionName(ZK_READ_PERM)
  public ZookeeperStatusResponse getZooKeeperStatus() throws Exception {
    final var statusHandler =
        (ZookeeperStatusHandler) coreContainer.getRequestHandler(ZK_STATUS_PATH);
    if (statusHandler == null) {
      throw new SolrException(
          SolrException.ErrorCode.SERVER_ERROR, "ZooKeeper status handler is not available");
    }

    final var response = instantiateJerseyResponse(ZookeeperStatusResponse.class);
    response.zkStatus = statusHandler.fetchZkStatus();
    return response;
  }
}
