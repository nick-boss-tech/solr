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
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.apache.solr.client.api.model.HealthCheckFileResponse;

@Path("/cores/{coreName}/ping/healthcheck-file")
public interface PingHealthCheckApi {
  @POST
  @Path("/enable")
  @Operation(
      summary = "Create the ping handler's healthcheck file, enabling the ping.",
      tags = {"cores"})
  HealthCheckFileResponse enableHealthCheckFile(@Parameter(description = "The name of the core whose ping handler to configure.", required = true) @PathParam("coreName") String coreName) throws Exception;

  @POST
  @Path("/disable")
  @Operation(
      summary = "Delete the ping handler's healthcheck file, disabling the ping.",
      tags = {"cores"})
  HealthCheckFileResponse disableHealthCheckFile(@Parameter(description = "The name of the core whose ping handler to configure.", required = true) @PathParam("coreName") String coreName) throws Exception;

  @GET
  @Path("/status")
  @Operation(
      summary = "Report whether the ping handler's healthcheck file is present.",
      tags = {"cores"})
  HealthCheckFileResponse getHealthCheckFileStatus(@Parameter(description = "The name of the core whose ping handler to configure.", required = true) @PathParam("coreName") String coreName) throws Exception;
}
