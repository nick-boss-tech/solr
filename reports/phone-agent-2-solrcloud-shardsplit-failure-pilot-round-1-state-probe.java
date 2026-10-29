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
package org.apache.solr.cloud.overseer;

import static org.apache.solr.SolrTestCaseJ4.assumeWorkingMockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.cloud.DistribStateManager;
import org.apache.solr.client.solrj.cloud.SolrCloudManager;
import org.apache.solr.client.solrj.impl.ClusterStateProvider;
import org.apache.solr.common.cloud.ClusterState;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.DocRouter;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.cloud.Slice.SliceStateProps;
import org.apache.solr.common.cloud.ZkNodeProps;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.util.TimeSource;
import org.apache.solr.handler.admin.ConfigSetsHandler;
import org.junit.BeforeClass;
import org.junit.Test;

/** State-machine probe for split completion with an incomplete fellow sub-shard. */
public class ReplicaMutatorShardSplitTest extends SolrTestCase {

  private static final String COLLECTION = "collection1";
  private static final String PARENT = "shard1";
  private static final String CHILD_0 = "shard1_0";
  private static final String CHILD_1 = "shard1_1";
  private static final String CORE_NODE = "core_node0";

  @BeforeClass
  public static void beforeClass() {
    assumeWorkingMockito();
  }

  @Test
  public void testCompletingChildDoesNotDeactivateParentWhileFellowIsConstructing() {
    SolrCloudManager cloudManager = mock(SolrCloudManager.class);
    when(cloudManager.getDistribStateManager()).thenReturn(mock(DistribStateManager.class));
    when(cloudManager.getTimeSource()).thenReturn(TimeSource.NANO_TIME);
    ClusterStateProvider clusterStateProvider = mock(ClusterStateProvider.class);
    when(cloudManager.getClusterStateProvider()).thenReturn(clusterStateProvider);
    when(clusterStateProvider.getUrlScheme()).thenReturn("http");

    Map<String, Object> replicaProps = new HashMap<>();
    replicaProps.put(ZkStateReader.CORE_NAME_PROP, "core0");
    replicaProps.put(ZkStateReader.REPLICA_TYPE, Replica.Type.NRT.toString());
    replicaProps.put(ZkStateReader.STATE_PROP, Replica.State.DOWN.toString());
    replicaProps.put(ZkStateReader.NODE_NAME_PROP, "127.0.0.1:8983_solr");
    replicaProps.put(ZkStateReader.BASE_URL_PROP, "http://127.0.0.1:8983/solr");
    Replica replica = new Replica(CORE_NODE, replicaProps, COLLECTION, CHILD_0);

    DocCollection collection =
        collection(
            slice(PARENT, Slice.State.ACTIVE, Map.of(), null),
            slice(CHILD_0, Slice.State.RECOVERY, Map.of(CORE_NODE, replica), PARENT),
            slice(CHILD_1, Slice.State.CONSTRUCTION, Map.of(), PARENT));
    ClusterState clusterState = new ClusterState(Set.of(), Map.of(COLLECTION, collection));

    Map<String, Object> replicaActive = new HashMap<>();
    replicaActive.put(ZkStateReader.COLLECTION_PROP, COLLECTION);
    replicaActive.put(ZkStateReader.SHARD_ID_PROP, CHILD_0);
    replicaActive.put(ZkStateReader.CORE_NODE_NAME_PROP, CORE_NODE);
    replicaActive.put(ZkStateReader.CORE_NAME_PROP, "core0");
    replicaActive.put(ZkStateReader.NODE_NAME_PROP, "127.0.0.1:8983_solr");
    replicaActive.put(ZkStateReader.STATE_PROP, Replica.State.ACTIVE.toString());

    ZkWriteCommand result =
        new ReplicaMutator(cloudManager).updateState(clusterState, new ZkNodeProps(replicaActive));

    assertSame(
        "the parent must remain routable until every split child is ready",
        Slice.State.ACTIVE,
        result.collection.getSlice(PARENT).getState());
    assertSame(Slice.State.CONSTRUCTION, result.collection.getSlice(CHILD_1).getState());
  }

  private static Slice slice(
      String name, Slice.State state, Map<String, Replica> replicas, String parent) {
    Map<String, Object> props = new HashMap<>();
    props.put(ZkStateReader.STATE_PROP, state.toString());
    if (parent != null) props.put(SliceStateProps.PARENT, parent);
    return new Slice(name, replicas, props, COLLECTION);
  }

  private static DocCollection collection(Slice... slices) {
    Map<String, Slice> map = new LinkedHashMap<>();
    for (Slice slice : slices) {
      map.put(slice.getName(), slice);
    }
    return DocCollection.create(
        COLLECTION,
        map,
        Map.of(ZkStateReader.CONFIGNAME_PROP, ConfigSetsHandler.DEFAULT_CONFIGSET_NAME),
        DocRouter.DEFAULT,
        1,
        Instant.EPOCH,
        null);
  }
}
