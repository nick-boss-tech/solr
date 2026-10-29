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

import static org.apache.solr.common.params.CommonParams.ZK_PATH;
import static org.apache.solr.security.PermissionNameProvider.Name.ZK_READ_PERM;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.api.endpoint.ZookeeperFilesApi;
import org.apache.solr.client.api.model.ZookeeperFilesResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.handler.admin.ZookeeperInfoHandler;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * V2 API for browsing the contents of ZooKeeper.
 *
 * <p>This API (GET /v2/cluster/zookeeper/files) is analogous to the v1 /admin/zookeeper API. The
 * response content is built by {@link ZookeeperInfoHandler}, the v1 handler, so both APIs serve the
 * same tree and znode detail. Unlike the v1 API, which requires the security-read permission for
 * the security.json path, this API runs entirely under the zookeeper-read permission and refuses
 * the security.json path; use the v1 API for that path.
 */
public class ZookeeperFiles extends AdminAPIBase implements ZookeeperFilesApi {

  @Inject
  public ZookeeperFiles(
      CoreContainer coreContainer,
      SolrQueryRequest solrQueryRequest,
      SolrQueryResponse solrQueryResponse) {
    super(coreContainer, solrQueryRequest, solrQueryResponse);
  }

  @Override
  @PermissionName(ZK_READ_PERM)
  public ZookeeperFilesResponse getZooKeeperFiles(String path, Boolean detail) throws Exception {
    return fetchFiles(path == null ? "/" : path, detail);
  }

  @Override
  @PermissionName(ZK_READ_PERM)
  public ZookeeperFilesResponse getZooKeeperFilesAtPath(String zkPath, Boolean detail)
      throws Exception {
    return fetchFiles("/" + zkPath, detail);
  }

  @SuppressWarnings("unchecked")
  private ZookeeperFilesResponse fetchFiles(String rawPath, Boolean detail) throws Exception {
    final var infoHandler = (ZookeeperInfoHandler) coreContainer.getRequestHandler(ZK_PATH);
    if (infoHandler == null) {
      throw new SolrException(
          SolrException.ErrorCode.SERVER_ERROR, "ZooKeeper info handler is not available");
    }
    final Map<String, Object> data =
        infoHandler.fetchPathView(rawPath, Boolean.TRUE.equals(detail));

    final var response = instantiateJerseyResponse(ZookeeperFilesResponse.class);
    response.znode = (Map<String, Object>) data.get("znode");
    response.tree = (List<Object>) data.get("tree");
    return response;
  }
}
