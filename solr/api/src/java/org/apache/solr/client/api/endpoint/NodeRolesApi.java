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
package org.apache.solr.client.api.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.apache.solr.client.api.model.NodeRolesForNodeResponse;
import org.apache.solr.client.api.model.NodeRolesResponse;
import org.apache.solr.client.api.model.SupportedRolesResponse;

@Path("/cluster/node-roles")
public interface NodeRolesApi {
  @GET
  @Operation(
      summary = "List all node roles and the nodes holding them.",
      tags = {"cluster"})
  NodeRolesResponse getAllNodeRoles() throws Exception;

  @GET
  @Path("/supported")
  @Operation(
      summary = "List the roles this version of Solr supports, with their modes.",
      tags = {"cluster"})
  SupportedRolesResponse getSupportedRoles() throws Exception;

  @GET
  @Path("/role/{role}")
  @Operation(
      summary = "List the nodes holding the given role, by mode.",
      tags = {"cluster"})
  NodeRolesResponse getNodesWithRole(@PathParam("role") String role) throws Exception;

  @GET
  @Path("/role/{role}/{mode}")
  @Operation(
      summary = "List the nodes holding the given role in the given mode.",
      tags = {"cluster"})
  NodeRolesResponse getNodesWithRoleMode(
      @PathParam("role") String role, @PathParam("mode") String mode) throws Exception;

  @GET
  @Path("/node/{node}")
  @Operation(
      summary = "List the roles held by the given node, with their modes.",
      tags = {"cluster"})
  NodeRolesForNodeResponse getRolesForNode(@PathParam("node") String node) throws Exception;
}
