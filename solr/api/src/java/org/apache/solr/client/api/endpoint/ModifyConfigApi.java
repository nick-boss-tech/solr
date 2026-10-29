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

import static org.apache.solr.client.api.util.Constants.INDEX_PATH_PREFIX;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.Map;
import org.apache.solr.client.api.model.SolrJerseyResponse;
import org.apache.solr.client.api.util.StoreApiParameters;

/**
 * The write side of the collection (or core) "config" API: creating, updating, and deleting
 * individual solrconfig components, config properties, and paramsets.
 *
 * <p>Component definitions are free-form maps, mirroring the JSON bodies of the v1 config API
 * commands they replace (e.g. 'add-searchcomponent' or 'update-requesthandler'); the component name
 * is taken from the URL path rather than the body. A "PUT" creates the component if it does not
 * exist and replaces it if it does, mirroring the upsert semantics used elsewhere in the v2 APIs.
 */
@Path(INDEX_PATH_PREFIX + "/config")
public interface ModifyConfigApi {

  @PUT
  @Path("/{componentType}/{componentName}")
  @StoreApiParameters
  @Operation(
      summary =
          "Create or replace a named solrconfig component (a search component, query response"
              + " writer, initParams, listener, query parser, value source parser, transformer,"
              + " update processor, query converter, or expressible).",
      tags = {"collection-config"})
  SolrJerseyResponse upsertConfigComponent(
      @PathParam("componentType") String componentType,
      @PathParam("componentName") String componentName,
      @RequestBody Map<String, Object> requestBody)
      throws Exception;

  @DELETE
  @Path("/{componentType}/{componentName}")
  @StoreApiParameters
  @Operation(
      summary = "Delete a named solrconfig component.",
      tags = {"collection-config"})
  SolrJerseyResponse deleteConfigComponent(
      @PathParam("componentType") String componentType,
      @PathParam("componentName") String componentName)
      throws Exception;

  // Request handler names are paths (e.g. "/select"), which do not fit the single-segment
  // {componentName} pattern above; the handler path is given here without its leading slash.
  @PUT
  @Path("/requesthandlers/{handlerName}")
  @StoreApiParameters
  @Operation(
      summary = "Create or replace the request handler at the specified path.",
      tags = {"collection-config"})
  SolrJerseyResponse upsertRequestHandler(
      @PathParam("handlerName") String handlerName, @RequestBody Map<String, Object> requestBody)
      throws Exception;

  @DELETE
  @Path("/requesthandlers/{handlerName}")
  @StoreApiParameters
  @Operation(
      summary = "Delete the request handler at the specified path.",
      tags = {"collection-config"})
  SolrJerseyResponse deleteRequestHandler(@PathParam("handlerName") String handlerName)
      throws Exception;

  @PUT
  @Path("/properties/{propertyName}")
  @StoreApiParameters
  @Operation(
      summary = "Set an editable config property; the request body is {\"value\": ...}.",
      tags = {"collection-config"})
  SolrJerseyResponse setProperty(
      @PathParam("propertyName") String propertyName, @RequestBody Map<String, Object> requestBody)
      throws Exception;

  @DELETE
  @Path("/properties/{propertyName}")
  @StoreApiParameters
  @Operation(
      summary = "Unset a config property.",
      tags = {"collection-config"})
  SolrJerseyResponse unsetProperty(@PathParam("propertyName") String propertyName) throws Exception;

  @PUT
  @Path("/userproperties/{propertyName}")
  @StoreApiParameters
  @Operation(
      summary = "Set a user property; the request body is {\"value\": ...}.",
      tags = {"collection-config"})
  SolrJerseyResponse setUserProperty(
      @PathParam("propertyName") String propertyName, @RequestBody Map<String, Object> requestBody)
      throws Exception;

  @DELETE
  @Path("/userproperties/{propertyName}")
  @StoreApiParameters
  @Operation(
      summary = "Unset a user property.",
      tags = {"collection-config"})
  SolrJerseyResponse unsetUserProperty(@PathParam("propertyName") String propertyName)
      throws Exception;

  @PUT
  @Path("/paramsets/{paramsetName}")
  @StoreApiParameters
  @Operation(
      summary = "Create or replace a paramset with the provided definition.",
      tags = {"collection-config"})
  SolrJerseyResponse setParamSet(
      @PathParam("paramsetName") String paramsetName, @RequestBody Map<String, Object> requestBody)
      throws Exception;

  @DELETE
  @Path("/paramsets/{paramsetName}")
  @StoreApiParameters
  @Operation(
      summary = "Delete a paramset.",
      tags = {"collection-config"})
  SolrJerseyResponse deleteParamSet(@PathParam("paramsetName") String paramsetName)
      throws Exception;
}
