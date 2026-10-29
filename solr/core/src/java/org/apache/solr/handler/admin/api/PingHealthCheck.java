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

import static org.apache.solr.security.PermissionNameProvider.Name.CORE_EDIT_PERM;
import static org.apache.solr.security.PermissionNameProvider.Name.CORE_READ_PERM;

import jakarta.inject.Inject;
import org.apache.solr.client.api.endpoint.PingHealthCheckApi;
import org.apache.solr.client.api.model.HealthCheckFileResponse;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.PingRequestHandler;
import org.apache.solr.api.JerseyResource;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * V2 APIs for the ping handler's healthcheck file.
 *
 * <p>These APIs (under /v2/cores/{coreName}/ping/healthcheck-file) are analogous to the v1 ping
 * API's action=enable, action=disable, and action=status requests. The file is created, deleted,
 * and reported by {@link PingRequestHandler}, so both APIs share one implementation.
 */
public class PingHealthCheck extends JerseyResource implements PingHealthCheckApi {

  private final SolrCore solrCore;

  @Inject
  public PingHealthCheck(
      SolrCore solrCore, SolrQueryRequest solrQueryRequest, SolrQueryResponse solrQueryResponse) {
    this.solrCore = solrCore;
  }

  @Override
  @PermissionName(CORE_EDIT_PERM)
  public HealthCheckFileResponse enableHealthCheckFile(String coreName) throws Exception {
    return setEnabled(true);
  }

  @Override
  @PermissionName(CORE_EDIT_PERM)
  public HealthCheckFileResponse disableHealthCheckFile(String coreName) throws Exception {
    return setEnabled(false);
  }

  @Override
  @PermissionName(CORE_READ_PERM)
  public HealthCheckFileResponse getHealthCheckFileStatus(String coreName) throws Exception {
    final var response = instantiateJerseyResponse(HealthCheckFileResponse.class);
    response.status = fetchPingHandler().healthCheckFileStatus();
    return response;
  }

  private HealthCheckFileResponse setEnabled(boolean enable) throws Exception {
    final var pingHandler = fetchPingHandler();
    pingHandler.setHealthCheckFileEnabled(enable);
    final var response = instantiateJerseyResponse(HealthCheckFileResponse.class);
    response.status = pingHandler.healthCheckFileStatus();
    return response;
  }

  private PingRequestHandler fetchPingHandler() {
    return (PingRequestHandler) solrCore.getRequestHandler("/admin/ping");
  }
}
