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

import static org.apache.solr.security.PermissionNameProvider.Name.COLL_READ_PERM;

import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.solr.client.api.endpoint.NodeRolesApi;
import org.apache.solr.client.api.model.NodeRolesForNodeResponse;
import org.apache.solr.client.api.model.NodeRolesResponse;
import org.apache.solr.client.api.model.SupportedRolesResponse;
import org.apache.solr.client.solrj.cloud.DistribStateManager;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.NodeRoles;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/**
 * V2 APIs for listing node roles.
 *
 * <p>These APIs (under /v2/cluster/node-roles) are analogous to the v1 ClusterAPI node-role
 * endpoints; both read the same ZooKeeper node-roles tree.
 */
public class ListNodeRoles extends AdminAPIBase implements NodeRolesApi {

  @Inject
  public ListNodeRoles(
      CoreContainer coreContainer,
      SolrQueryRequest solrQueryRequest,
      SolrQueryResponse solrQueryResponse) {
    super(coreContainer, solrQueryRequest, solrQueryResponse);
  }

  @Override
  @PermissionName(COLL_READ_PERM)
  public NodeRolesResponse getAllNodeRoles() throws Exception {
    final var response = instantiateJerseyResponse(NodeRolesResponse.class);
    @SuppressWarnings("unchecked")
    final Map<String, Object> roles =
        (Map<String, Object>) readRecursive(ZkStateReader.NODE_ROLES, stateManager(), 3);
    response.nodeRoles = roles;
    return response;
  }

  @Override
  @PermissionName(COLL_READ_PERM)
  public SupportedRolesResponse getSupportedRoles() throws Exception {
    final Map<String, Object> roleModesSupportedMap = new HashMap<>();
    for (NodeRoles.Role role : NodeRoles.Role.values()) {
      roleModesSupportedMap.put(role.toString(), Map.of("modes", role.supportedModes()));
    }
    final var response = instantiateJerseyResponse(SupportedRolesResponse.class);
    response.supportedRoles = roleModesSupportedMap;
    return response;
  }

  @Override
  @PermissionName(COLL_READ_PERM)
  public NodeRolesResponse getNodesWithRole(String role) throws Exception {
    final var response = instantiateJerseyResponse(NodeRolesResponse.class);
    response.nodeRoles =
        Map.of(role, readRecursive(ZkStateReader.NODE_ROLES + "/" + role, stateManager(), 2));
    return response;
  }

  @Override
  @PermissionName(COLL_READ_PERM)
  public NodeRolesResponse getNodesWithRoleMode(String role, String mode) throws Exception {
    final List<String> nodes =
        stateManager().listData(ZkStateReader.NODE_ROLES + "/" + role + "/" + mode);
    final var response = instantiateJerseyResponse(NodeRolesResponse.class);
    response.nodeRoles = Map.of(role, Map.of(mode, nodes));
    return response;
  }

  @Override
  @PermissionName(COLL_READ_PERM)
  @SuppressWarnings("unchecked")
  public NodeRolesForNodeResponse getRolesForNode(String node) throws Exception {
    final Map<String, Map<String, Set<String>>> roles =
        (Map<String, Map<String, Set<String>>>)
            readRecursive(ZkStateReader.NODE_ROLES, stateManager(), 3);
    final var response = instantiateJerseyResponse(NodeRolesForNodeResponse.class);
    for (String role : roles.keySet()) {
      for (String mode : roles.get(role).keySet()) {
        final Set<String> nodes = roles.get(role).get(mode);
        if (!nodes.isEmpty() && nodes.contains(node)) {
          response.roles.put(role, mode);
        }
      }
    }
    return response;
  }

  private DistribStateManager stateManager() {
    fetchAndValidateZooKeeperAwareCoreContainer();
    return coreContainer.getZkController().getSolrCloudManager().getDistribStateManager();
  }

  private static Object readRecursive(String path, DistribStateManager zk, int depth) {
    if (depth == 0) return null;
    Map<String, Object> result;
    try {
      List<String> children = zk.listData(path);
      if (children != null && !children.isEmpty()) {
        result = new HashMap<>();
      } else {
        return Set.of();
      }
      for (String child : children) {
        Object c = readRecursive(path + "/" + child, zk, depth - 1);
        result.put(child, c);
      }
    } catch (Exception e) {
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, e);
    }
    if (depth == 1) {
      return result.keySet();
    } else {
      return result;
    }
  }
}
