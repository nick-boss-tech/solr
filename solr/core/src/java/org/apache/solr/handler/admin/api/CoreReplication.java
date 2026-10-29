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

import static org.apache.solr.common.params.CommonParams.NAME;
import static org.apache.solr.security.PermissionNameProvider.Name.CORE_EDIT_PERM;
import static org.apache.solr.security.PermissionNameProvider.Name.CORE_READ_PERM;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.IOException;
import org.apache.solr.client.api.endpoint.ReplicationApis;
import org.apache.solr.client.api.model.CommitsResponse;
import org.apache.solr.client.api.model.FetchIndexRequestBody;
import org.apache.solr.client.api.model.FileListResponse;
import org.apache.solr.client.api.model.IndexVersionResponse;
import org.apache.solr.client.api.model.ReplicationCommandResponse;
import org.apache.solr.client.api.model.ReplicationDetailsResponse;
import org.apache.solr.client.api.model.ReplicationEnabledRequestBody;
import org.apache.solr.client.api.model.RestoreRequestBody;
import org.apache.solr.client.api.model.RestoreStatusResponse;
import org.apache.solr.client.api.model.SolrJerseyResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CoreAdminParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.ReplicationHandler;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * V2 API implementation of {@link ReplicationApis}
 *
 * <p>These APIs are analogous to the v1 /coreName/replication APIs.
 */
public class CoreReplication extends ReplicationAPIBase implements ReplicationApis {

  @Inject
  public CoreReplication(SolrCore solrCore, SolrQueryRequest req, SolrQueryResponse rsp) {
    super(solrCore, req, rsp);
  }

  @Override
  @PermissionName(CORE_READ_PERM)
  public ReplicationDetailsResponse fetchReplicationDetails(Boolean follower) {
    // Boxed (rather than the primitive boolean used elsewhere for similar flags) so this method
    // can also be called directly, bypassing the JAX-RS @DefaultValue, with no value supplied.
    return doFetchReplicationDetails(follower == null || follower);
  }

  @Override
  @PermissionName(CORE_READ_PERM)
  public RestoreStatusResponse fetchRestoreStatus() {
    return doFetchRestoreStatus();
  }

  @Override
  @PermissionName(CORE_READ_PERM)
  public CommitsResponse fetchCommits() {
    return doFetchCommits();
  }

  @Override
  @PermissionName(CORE_READ_PERM)
  public IndexVersionResponse fetchIndexVersion() throws IOException {
    return doFetchIndexVersion();
  }

  @Override
  @PermissionName(CORE_READ_PERM)
  public FileListResponse fetchFileList(long gen) {
    return doFetchFileList(gen);
  }

  @Override
  @PermissionName(CORE_READ_PERM)
  public StreamingOutput fetchFile(
      String filePath,
      String dirType,
      String offset,
      String len,
      Boolean compression,
      Boolean checksum,
      double maxWriteMBPerSec,
      Long gen) {
    if (dirType == null) {
      throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Must provide a dirType ");
    }
    return doFetchFile(
        filePath, dirType, offset, len, compression, checksum, maxWriteMBPerSec, gen);
  }

  @Override
  @PermissionName(CORE_EDIT_PERM)
  public SolrJerseyResponse setReplicationEnabled(ReplicationEnabledRequestBody requestBody) {
    return doSetReplicationEnabled(requestBody);
  }

  @Override
  @PermissionName(CORE_EDIT_PERM)
  public ReplicationCommandResponse fetchIndex(FetchIndexRequestBody requestBody) throws Exception {
    final var params = new ModifiableSolrParams();
    params.set(ReplicationHandler.COMMAND, ReplicationHandler.CMD_FETCH_INDEX);
    if (requestBody != null) {
      if (requestBody.leaderUrl != null) {
        params.set(ReplicationHandler.LEADER_URL, requestBody.leaderUrl);
      }
      if (requestBody.compression != null) {
        params.set("compression", requestBody.compression);
      }
    }
    return runReplicationCommand(params);
  }

  @Override
  @PermissionName(CORE_EDIT_PERM)
  public ReplicationCommandResponse abortFetch() throws Exception {
    final var params = new ModifiableSolrParams();
    params.set(ReplicationHandler.COMMAND, ReplicationHandler.CMD_ABORT_FETCH);
    return runReplicationCommand(params);
  }

  @Override
  @PermissionName(CORE_EDIT_PERM)
  public ReplicationCommandResponse enablePoll() throws Exception {
    final var params = new ModifiableSolrParams();
    params.set(ReplicationHandler.COMMAND, ReplicationHandler.CMD_ENABLE_POLL);
    return runReplicationCommand(params);
  }

  @Override
  @PermissionName(CORE_EDIT_PERM)
  public ReplicationCommandResponse disablePoll() throws Exception {
    final var params = new ModifiableSolrParams();
    params.set(ReplicationHandler.COMMAND, ReplicationHandler.CMD_DISABLE_POLL);
    return runReplicationCommand(params);
  }

  @Override
  @PermissionName(CORE_EDIT_PERM)
  public ReplicationCommandResponse restoreIndex(RestoreRequestBody requestBody) throws Exception {
    return runReplicationCommand(backupParams(ReplicationHandler.CMD_RESTORE, requestBody));
  }

  @Override
  @PermissionName(CORE_EDIT_PERM)
  public ReplicationCommandResponse deleteBackup(RestoreRequestBody requestBody) throws Exception {
    return runReplicationCommand(backupParams(ReplicationHandler.CMD_DELETE_BACKUP, requestBody));
  }

  private static ModifiableSolrParams backupParams(String command, RestoreRequestBody body) {
    final var params = new ModifiableSolrParams();
    params.set(ReplicationHandler.COMMAND, command);
    if (body != null) {
      if (body.name != null) {
        params.set(NAME, body.name);
      }
      if (body.location != null) {
        params.set(CoreAdminParams.BACKUP_LOCATION, body.location);
      }
      if (body.repository != null) {
        params.set(CoreAdminParams.BACKUP_REPOSITORY, body.repository);
      }
    }
    return params;
  }

  /**
   * Runs a v1 replication command through the core's {@link ReplicationHandler}, as the v1
   * /replication API does, and returns its status and response values.
   */
  private ReplicationCommandResponse runReplicationCommand(ModifiableSolrParams params)
      throws Exception {
    final var replicationHandler = (ReplicationHandler) solrCore.getRequestHandler("/replication");
    solrQueryRequest.setParams(params);
    replicationHandler.handleRequestBody(solrQueryRequest, solrQueryResponse);
    if (solrQueryResponse.getException() != null) {
      throw solrQueryResponse.getException();
    }

    final var response = instantiateJerseyResponse(ReplicationCommandResponse.class);
    final var details = new java.util.LinkedHashMap<String, Object>();
    solrQueryResponse
        .getValues()
        .forEach(
            (name, value) -> {
              if ("status".equals(name)) {
                response.status = String.valueOf(value);
              } else if (!"responseHeader".equals(name)) {
                details.put(name, convertValue(value));
              }
            });
    response.details = details;
    return response;
  }

  private static Object convertValue(Object value) {
    if (value instanceof NamedList<?> namedList) {
      final var map = new java.util.LinkedHashMap<String, Object>();
      namedList.forEach((name, v) -> map.put(name, convertValue(v)));
      return map;
    }
    return value;
  }
}
