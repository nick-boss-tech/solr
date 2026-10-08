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
 */ package org.apache.solr.handler.admin.api;

import static org.apache.solr.security.PermissionNameProvider.Name.COLL_READ_PERM;

import jakarta.inject.Inject;
import java.util.List;
import org.apache.solr.client.api.endpoint.GetAliasByNameApi;
import org.apache.solr.client.api.model.GetAliasByNameResponse;
import org.apache.solr.common.cloud.Aliases;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

/** V2 API implementation for inspecting a single collection alias */
public class GetAliasByName extends AdminAPIBase implements GetAliasByNameApi {

  @Inject
  public GetAliasByName(
      CoreContainer coreContainer,
      SolrQueryRequest solrQueryRequest,
      SolrQueryResponse solrQueryResponse) {
    super(coreContainer, solrQueryRequest, solrQueryResponse);
  }

  @Override
  @PermissionName(COLL_READ_PERM)
  public GetAliasByNameResponse getAliasByName(String aliasName) throws Exception {
    recordCollectionForLogAndTracing(null, solrQueryRequest);

    final GetAliasByNameResponse response = instantiateJerseyResponse(GetAliasByNameResponse.class);
    response.alias = aliasName;

    final CoreContainer coreContainer = fetchAndValidateZooKeeperAwareCoreContainer();
    final ZkStateReader zkStateReader = coreContainer.getZkController().getZkStateReader();
    // Make sure we have the latest alias info, since a user has explicitly invoked an alias API
    zkStateReader.getAliasesManager().update();

    final Aliases aliases = zkStateReader.getAliases();
    if (aliases != null) {
      response.collections = aliases.getCollectionAliasListMap().getOrDefault(aliasName, List.of());
      response.properties = aliases.getCollectionAliasProperties(aliasName);
    }

    return response;
  }
}
