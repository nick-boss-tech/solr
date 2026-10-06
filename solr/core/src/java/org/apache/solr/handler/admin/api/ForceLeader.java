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

import static org.apache.solr.common.cloud.ZkStateReader.COLLECTION_PROP;
import static org.apache.solr.common.cloud.ZkStateReader.SHARD_ID_PROP;
import static org.apache.solr.security.PermissionNameProvider.Name.COLL_EDIT_PERM;

import jakarta.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.solr.client.api.endpoint.ForceLeaderApi;
import org.apache.solr.client.api.model.SolrJerseyResponse;
import org.apache.solr.cloud.ZkController;
import org.apache.solr.cloud.ZkShardTerms;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.ClusterState;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.handler.api.V2ApiUtils;
import org.apache.solr.jersey.PermissionName;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * V2 API implementation for triggering a leader election on a particular collection and shard.
 *
 * <p>This API (POST /v2/collections/collectionName/shards/shardName/force-leader) is analogous to
 * the v1 /admin/collections?action=FORCELEADER command.
 */
public class ForceLeader extends AdminAPIBase implements ForceLeaderApi {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  public ForceLeader(
      CoreContainer coreContainer,
      SolrQueryRequest solrQueryRequest,
      SolrQueryResponse solrQueryResponse) {
    super(coreContainer, solrQueryRequest, solrQueryResponse);
  }

  @Override
  @PermissionName(COLL_EDIT_PERM)
  public SolrJerseyResponse forceShardLeader(String collectionName, String shardName) {
    final SolrJerseyResponse response = instantiateJerseyResponse(SolrJerseyResponse.class);
    ensureRequiredParameterProvided(COLLECTION_PROP, collectionName);
    ensureRequiredParameterProvided(SHARD_ID_PROP, shardName);
    fetchAndValidateZooKeeperAwareCoreContainer();
    recordCollectionForLogAndTracing(collectionName, solrQueryRequest);

    doForceLeaderElection(collectionName, shardName);
    return response;
  }

  public static void invokeFromV1Params(
      CoreContainer coreContainer, SolrQueryRequest request, SolrQueryResponse response) {
    final var api = new ForceLeader(coreContainer, request, response);
    final var params = request.getParams();
    params.required().check(COLLECTION_PROP, SHARD_ID_PROP);

    V2ApiUtils.squashIntoSolrResponseWithoutHeader(
        response, api.forceShardLeader(params.get(COLLECTION_PROP), params.get(SHARD_ID_PROP)));
  }

  private static final int WAIT_ATTEMPTS = 9;
  private static final long WAIT_PAUSE_MS = 5000;

  /**
   * Polls until the shard has an active leader.
   *
   * @param currentSlice supplies the latest state of the shard, null if it no longer exists
   * @throws SolrException if the shard disappears or no active leader shows up in time
   */
  static void waitForActiveLeader(
      Supplier<Slice> currentSlice,
      int attempts,
      long pauseMs,
      String collectionName,
      String shardName)
      throws InterruptedException {
    Slice slice = null;
    for (int i = 0; i < attempts; i++) {
      Thread.sleep(pauseMs);
      slice = currentSlice.get();
      if (slice == null) {
        throw new SolrException(
            SolrException.ErrorCode.SERVER_ERROR,
            "Shard "
                + shardName
                + " of collection "
                + collectionName
                + " was removed while waiting for a forced leader");
      }
      Replica leader = slice.getLeader();
      if (leader != null && leader.getState() == Replica.State.ACTIVE) {
        return;
      }
      log.warn(
          "Force leader attempt {}. Waiting {} ms for an active leader. State of the slice: {}",
          (i + 1),
          pauseMs,
          slice); // nowarn
    }
    throw new SolrException(
        SolrException.ErrorCode.SERVER_ERROR,
        "Couldn't force an active leader for collection: "
            + collectionName
            + " shard: "
            + shardName
            + " within "
            + (attempts * pauseMs)
            + " ms. State of the slice: "
            + slice);
  }

  private void doForceLeaderElection(String extCollectionName, String shardName) {
    ZkController zkController = coreContainer.getZkController();
    ClusterState clusterState = zkController.getClusterState();
    String collectionName =
        zkController.zkStateReader.getAliases().resolveSimpleAlias(extCollectionName);

    log.info("Force leader invoked, state: {}", clusterState);
    DocCollection collection = clusterState.getCollection(collectionName);
    Slice slice = collection.getSlice(shardName);
    if (slice == null) {
      throw new SolrException(
          SolrException.ErrorCode.BAD_REQUEST,
          "No shard with name " + shardName + " exists for collection " + collectionName);
    }

    try (ZkShardTerms zkShardTerms =
        new ZkShardTerms(collectionName, slice.getName(), zkController.getZkClient())) {
      // if an active replica is the leader, then all is fine already
      Replica leader = slice.getLeader();
      if (leader != null && leader.getState() == Replica.State.ACTIVE) {
        throw new SolrException(
            SolrException.ErrorCode.SERVER_ERROR,
            "The shard already has an active leader. Force leader is not applicable. State: "
                + slice);
      }

      final Set<String> liveNodes = clusterState.getLiveNodes();
      List<Replica> liveReplicas =
          slice.getReplicas().stream()
              .filter(rep -> liveNodes.contains(rep.getNodeName()))
              .collect(Collectors.toList());
      boolean shouldIncreaseReplicaTerms =
          liveReplicas.stream()
              .noneMatch(
                  rep ->
                      zkShardTerms.registered(rep.getName())
                          && zkShardTerms.canBecomeLeader(rep.getName()));
      // we won't increase replica's terms if exist a live replica with term equals to leader
      if (shouldIncreaseReplicaTerms) {
        // TODO only increase terms of replicas less out-of-sync
        liveReplicas.stream()
            .filter(rep -> zkShardTerms.registered(rep.getName()))
            // TODO should this all be done at once instead of increasing each replica individually?
            .forEach(rep -> zkShardTerms.setTermEqualsToLeader(rep.getName()));
      }

      waitForActiveLeader(
          () -> {
            DocCollection current =
                coreContainer.getZkController().getClusterState().getCollectionOrNull(collectionName);
            return current == null ? null : current.getSlice(shardName);
          },
          WAIT_ATTEMPTS,
          WAIT_PAUSE_MS,
          collectionName,
          shardName);
      log.info(
          "Successfully issued FORCELEADER command for collection: {}, shard: {}",
          collectionName,
          shardName);
    } catch (SolrException e) {
      throw e;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new SolrException(
          SolrException.ErrorCode.SERVER_ERROR,
          "Interrupted while waiting for a leader of collection: "
              + collectionName
              + " shard: "
              + shardName,
          e);
    } catch (Exception e) {
      throw new SolrException(
          SolrException.ErrorCode.SERVER_ERROR,
          "Error executing FORCELEADER operation for collection: "
              + collectionName
              + " shard: "
              + shardName,
          e);
    }
  }
}
