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

package org.apache.solr.handler;

import static org.apache.solr.client.solrj.SolrRequest.METHOD.GET;
import static org.apache.solr.client.solrj.SolrRequest.METHOD.POST;
import static org.apache.solr.common.params.CollectionParams.CollectionAction.OVERSEERSTATUS;
import static org.apache.solr.core.RateLimiterConfig.RL_CONFIG_KEY;
import static org.apache.solr.security.PermissionNameProvider.Name.COLL_EDIT_PERM;
import static org.apache.solr.security.PermissionNameProvider.Name.COLL_READ_PERM;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.solr.api.Command;
import org.apache.solr.api.EndPoint;
import org.apache.solr.api.PayloadObj;
import org.apache.solr.client.solrj.cloud.DistribStateManager;
import org.apache.solr.client.solrj.request.beans.RateLimiterPayload;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.ClusterProperties;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.params.DefaultSolrParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.Utils;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.NodeRoles;
import org.apache.solr.handler.admin.CollectionsHandler;
import org.apache.solr.handler.admin.ConfigSetsHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.zookeeper.KeeperException;

/** All V2 APIs that have a prefix of /api/cluster/ */
public class ClusterAPI {
  private final CollectionsHandler collectionsHandler;
  private final ConfigSetsHandler configSetsHandler;

  public final Commands commands = new Commands();

  public ClusterAPI(CollectionsHandler ch, ConfigSetsHandler configSetsHandler) {
    this.collectionsHandler = ch;
    this.configSetsHandler = configSetsHandler;
  }

  public static List<String> getNodesByRole(
      NodeRoles.Role role, String mode, DistribStateManager zk)
      throws InterruptedException, IOException, KeeperException {
    try {
      return zk.listData(ZkStateReader.NODE_ROLES + "/" + role + "/" + mode);
    } catch (NoSuchElementException e) {
      return List.of();
    }
  }

  @EndPoint(method = GET, path = "/cluster/overseer", permission = COLL_READ_PERM)
  public void getOverseerStatus(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
    collectionsHandler.handleRequestBody(wrapParams(req, "action", OVERSEERSTATUS.lowerName), rsp);
  }

  public static SolrQueryRequest wrapParams(SolrQueryRequest req, Object... def) {
    Map<String, Object> m = Utils.makeMap(def);
    return wrapParams(req, m);
  }

  public static SolrQueryRequest wrapParams(SolrQueryRequest req, Map<String, Object> m) {
    ModifiableSolrParams solrParams = new ModifiableSolrParams();
    m.forEach(
        (k, v) -> {
          if (v == null) return;
          if (v instanceof String[]) {
            solrParams.add(k, (String[]) v);
          } else {
            solrParams.add(k, String.valueOf(v));
          }
        });
    DefaultSolrParams dsp = new DefaultSolrParams(req.getParams(), solrParams);
    req.setParams(dsp);
    return req;
  }

  private CoreContainer getCoreContainer() {
    return collectionsHandler.getCoreContainer();
  }

  @EndPoint(method = POST, path = "/cluster", permission = COLL_EDIT_PERM)
  public class Commands {
    @Command(name = "set-ratelimiter")
    public void setRateLimiters(PayloadObj<RateLimiterPayload> payLoad) {
      RateLimiterPayload rateLimiterConfig = payLoad.get();
      ClusterProperties clusterProperties =
          new ClusterProperties(getCoreContainer().getZkController().getZkClient());

      try {
        clusterProperties.update(rateLimiterConfig, RL_CONFIG_KEY);
      } catch (Exception e) {
        throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Error in API", e);
      }
    }
  }
}
