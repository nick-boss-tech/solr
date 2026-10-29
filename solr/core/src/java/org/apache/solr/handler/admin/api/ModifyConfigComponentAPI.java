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

import static org.apache.solr.handler.SolrConfigHandler.SET;
import static org.apache.solr.handler.SolrConfigHandler.SET_PROPERTY;
import static org.apache.solr.handler.SolrConfigHandler.SET_USER_PROPERTY;
import static org.apache.solr.handler.SolrConfigHandler.UNSET_PROPERTY;
import static org.apache.solr.handler.SolrConfigHandler.UNSET_USER_PROPERTY;

import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.solr.api.JerseyResource;
import org.apache.solr.client.api.endpoint.ModifyConfigApi;
import org.apache.solr.client.api.model.SolrJerseyResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.CommandOperation;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.SolrConfigHandler;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.security.PermissionNameProvider;

/**
 * V2 APIs for creating, updating, or deleting individual components in a collection configuration,
 * the editable config properties, and paramsets.
 *
 * <p>Each operation is translated to the {@link CommandOperation} that the equivalent v1 config
 * command would have produced, and the operations are run through {@link
 * SolrConfigHandler#processConfigCommands}, so validation, persistence, and error reporting match
 * the v1 API. Component "PUT" operations use the 'upsert' command form: a component is created if
 * it does not exist and replaced if it does, instead of the v1 API's separate 'add' and 'update'
 * commands.
 */
public class ModifyConfigComponentAPI extends JerseyResource implements ModifyConfigApi {

  // Maps the URL path segment for a component type to the tag used by the config commands (the
  // suffix of the 'add-...', 'update-...', and 'delete-...' command names). Request handlers are
  // handled separately, as their names are paths and do not fit a single path segment.
  private static final Map<String, String> COMPONENT_TYPE_TAGS =
      Map.ofEntries(
          Map.entry("searchcomponents", "searchcomponent"),
          Map.entry("queryresponsewriters", "queryresponsewriter"),
          Map.entry("initparams", "initparams"),
          Map.entry("listeners", "listener"),
          Map.entry("queryparsers", "queryparser"),
          Map.entry("valuesourceparsers", "valuesourceparser"),
          Map.entry("transformers", "transformer"),
          Map.entry("updateprocessors", "updateprocessor"),
          Map.entry("queryconverters", "queryconverter"),
          Map.entry("expressibles", "expressible"));

  private final SolrCore solrCore;
  private final SolrQueryRequest solrQueryRequest;

  @Inject
  public ModifyConfigComponentAPI(SolrCore solrCore, SolrQueryRequest solrQueryRequest) {
    this.solrCore = solrCore;
    this.solrQueryRequest = solrQueryRequest;
  }

  @Override
  @PermissionName(PermissionNameProvider.Name.CONFIG_EDIT_PERM)
  public SolrJerseyResponse upsertConfigComponent(
      String componentType, String componentName, Map<String, Object> requestBody)
      throws Exception {
    final var response = instantiateJerseyResponse(SolrJerseyResponse.class);
    ensureRequiredParameterProvided("componentName", componentName);
    ensureRequiredRequestBodyProvided(requestBody);
    final var tag = lookupComponentTag(componentType);

    final var componentDef = new HashMap<>(requestBody);
    componentDef.put("name", componentName);
    runConfigOps(List.of(new CommandOperation("upsert-" + tag, componentDef)), false);

    return response;
  }

  @Override
  @PermissionName(PermissionNameProvider.Name.CONFIG_EDIT_PERM)
  public SolrJerseyResponse deleteConfigComponent(String componentType, String componentName)
      throws Exception {
    final var response = instantiateJerseyResponse(SolrJerseyResponse.class);
    ensureRequiredParameterProvided("componentName", componentName);
    final var tag = lookupComponentTag(componentType);

    runConfigOps(List.of(new CommandOperation("delete-" + tag, componentName)), false);

    return response;
  }

  @Override
  @PermissionName(PermissionNameProvider.Name.CONFIG_EDIT_PERM)
  public SolrJerseyResponse upsertRequestHandler(
      String handlerName, Map<String, Object> requestBody) throws Exception {
    final var response = instantiateJerseyResponse(SolrJerseyResponse.class);
    ensureRequiredParameterProvided("handlerName", handlerName);
    ensureRequiredRequestBodyProvided(requestBody);

    final var handlerDef = new HashMap<>(requestBody);
    handlerDef.put("name", "/" + handlerName);
    runConfigOps(List.of(new CommandOperation("upsert-requesthandler", handlerDef)), false);

    return response;
  }

  @Override
  @PermissionName(PermissionNameProvider.Name.CONFIG_EDIT_PERM)
  public SolrJerseyResponse deleteRequestHandler(String handlerName) throws Exception {
    final var response = instantiateJerseyResponse(SolrJerseyResponse.class);
    ensureRequiredParameterProvided("handlerName", handlerName);

    runConfigOps(List.of(new CommandOperation("delete-requesthandler", "/" + handlerName)), false);

    return response;
  }

  @Override
  @PermissionName(PermissionNameProvider.Name.CONFIG_EDIT_PERM)
  public SolrJerseyResponse setProperty(String propertyName, Map<String, Object> requestBody)
      throws Exception {
    final var response = instantiateJerseyResponse(SolrJerseyResponse.class);
    ensureRequiredParameterProvided("propertyName", propertyName);
    final var value = extractPropertyValue(requestBody);

    runConfigOps(List.of(new CommandOperation(SET_PROPERTY, Map.of(propertyName, value))), false);

    return response;
  }

  @Override
  @PermissionName(PermissionNameProvider.Name.CONFIG_EDIT_PERM)
  public SolrJerseyResponse unsetProperty(String propertyName) throws Exception {
    final var response = instantiateJerseyResponse(SolrJerseyResponse.class);
    ensureRequiredParameterProvided("propertyName", propertyName);

    runConfigOps(List.of(new CommandOperation(UNSET_PROPERTY, propertyName)), false);

    return response;
  }

  @Override
  @PermissionName(PermissionNameProvider.Name.CONFIG_EDIT_PERM)
  public SolrJerseyResponse setUserProperty(String propertyName, Map<String, Object> requestBody)
      throws Exception {
    final var response = instantiateJerseyResponse(SolrJerseyResponse.class);
    ensureRequiredParameterProvided("propertyName", propertyName);
    final var value = extractPropertyValue(requestBody);

    runConfigOps(
        List.of(new CommandOperation(SET_USER_PROPERTY, Map.of(propertyName, value))), false);

    return response;
  }

  @Override
  @PermissionName(PermissionNameProvider.Name.CONFIG_EDIT_PERM)
  public SolrJerseyResponse unsetUserProperty(String propertyName) throws Exception {
    final var response = instantiateJerseyResponse(SolrJerseyResponse.class);
    ensureRequiredParameterProvided("propertyName", propertyName);

    runConfigOps(List.of(new CommandOperation(UNSET_USER_PROPERTY, propertyName)), false);

    return response;
  }

  @Override
  @PermissionName(PermissionNameProvider.Name.CONFIG_EDIT_PERM)
  public SolrJerseyResponse setParamSet(String paramsetName, Map<String, Object> requestBody)
      throws Exception {
    final var response = instantiateJerseyResponse(SolrJerseyResponse.class);
    ensureRequiredParameterProvided("paramsetName", paramsetName);
    ensureRequiredRequestBodyProvided(requestBody);

    runConfigOps(List.of(new CommandOperation(SET, Map.of(paramsetName, requestBody))), true);

    return response;
  }

  @Override
  @PermissionName(PermissionNameProvider.Name.CONFIG_EDIT_PERM)
  public SolrJerseyResponse deleteParamSet(String paramsetName) throws Exception {
    final var response = instantiateJerseyResponse(SolrJerseyResponse.class);
    ensureRequiredParameterProvided("paramsetName", paramsetName);

    runConfigOps(List.of(new CommandOperation("delete", List.of(paramsetName))), true);

    return response;
  }

  private String lookupComponentTag(String componentType) {
    ensureRequiredParameterProvided("componentType", componentType);
    final var tag = COMPONENT_TYPE_TAGS.get(componentType.toLowerCase(Locale.ROOT));
    if (tag == null) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST,
          "Unknown config component type '"
              + componentType
              + "'; valid types are: "
              + String.join(", ", COMPONENT_TYPE_TAGS.keySet()));
    }
    return tag;
  }

  private static Object extractPropertyValue(Map<String, Object> requestBody) {
    if (requestBody == null || !requestBody.containsKey("value")) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST, "Missing required request body field: 'value'");
    }
    return requestBody.get("value");
  }

  private void runConfigOps(List<CommandOperation> ops, boolean forParams) throws Exception {
    final var configHandler = (SolrConfigHandler) solrCore.getRequestHandler("/config");
    if (configHandler == null) {
      throw new SolrException(
          SolrException.ErrorCode.SERVER_ERROR, "No config handler found for core");
    }
    configHandler.processConfigCommands(ops, solrQueryRequest, forParams);
  }
}
