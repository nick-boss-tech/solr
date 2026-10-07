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

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.cloud.ClusterState;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.DocRouter;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.cloud.ZkNodeProps;
import org.apache.solr.common.cloud.ZkStateReader;

public class NodeMutatorTest extends SolrTestCase {

  private static final String NODE = "node1:8983_solr";

  private static ClusterState clusterState(Set<String> liveNodes) {
    Map<String, Object> replicaProps = new HashMap<>();
    replicaProps.put(ZkStateReader.NODE_NAME_PROP, NODE);
    replicaProps.put(ZkStateReader.CORE_NAME_PROP, "c1_shard1_replica_n1");
    replicaProps.put(ZkStateReader.REPLICA_TYPE, "NRT");
    replicaProps.put(ZkStateReader.BASE_URL_PROP, "http://node1:8983/solr");
    replicaProps.put(ZkStateReader.STATE_PROP, Replica.State.ACTIVE.toString());
    Map<String, Replica> replicas =
        Map.of("core_node1", new Replica("core_node1", replicaProps, "c1", "shard1"));
    Map<String, Slice> slices = Map.of("shard1", new Slice("shard1", replicas, Map.of(), "c1"));
    DocCollection c1 =
        DocCollection.create("c1", slices, Map.of(), DocRouter.DEFAULT, 0, Instant.now(), null);
    return new ClusterState(liveNodes, Map.of("c1", c1));
  }

  private static ZkNodeProps downNodeMessage(boolean onlyIfNodeNotLive) {
    Map<String, Object> props = new HashMap<>();
    props.put(ZkStateReader.NODE_NAME_PROP, NODE);
    if (onlyIfNodeNotLive) {
      props.put(NodeMutator.ONLY_IF_NODE_NOT_LIVE, true);
    }
    return new ZkNodeProps(props);
  }

  public void testDownNodeMarksReplicasDown() {
    NodeMutator mutator = new NodeMutator(null);
    List<ZkWriteCommand> cmds = mutator.downNode(clusterState(Set.of()), downNodeMessage(true));
    assertEquals(1, cmds.size());
    Replica replica = cmds.get(0).collection.getReplica("core_node1");
    assertEquals(Replica.State.DOWN, replica.getState());
  }

  public void testUnconditionalDownNodeIgnoresLiveNodes() {
    NodeMutator mutator = new NodeMutator(null);
    List<ZkWriteCommand> cmds =
        mutator.downNode(clusterState(Set.of(NODE)), downNodeMessage(false));
    assertEquals(1, cmds.size());
    assertEquals(Replica.State.DOWN, cmds.get(0).collection.getReplica("core_node1").getState());
  }

  /** A shutdown DOWNNODE processed after the node restarted must not mark its replicas down. */
  public void testConditionalDownNodeSkippedWhenNodeIsLiveAgain() {
    NodeMutator mutator = new NodeMutator(null);
    List<ZkWriteCommand> cmds = mutator.downNode(clusterState(Set.of(NODE)), downNodeMessage(true));
    assertTrue("restarted node must not be marked down: " + cmds, cmds.isEmpty());
  }
}
