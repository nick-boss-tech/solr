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

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.cloud.SolrCloudManager;
import org.apache.solr.common.cloud.ClusterState;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.DocRouter;
import org.apache.solr.common.cloud.RoutingRule;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.cloud.ZkNodeProps;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.util.Utils;
import org.junit.BeforeClass;
import org.junit.Test;

/** Routing rollback preserves earlier ranges and refuses a stale expected rule. */
public class SliceMutatorRoutingRuleTest extends SolrTestCase {
  @BeforeClass
  public static void beforeClass() {
    assumeWorkingMockito();
  }

  @Test
  public void testRestoreEarlierRangesAndExpiry() {
    RoutingRule previous = rule("0-7fff", "1000");
    RoutingRule installed = rule("0-7fff,8000-ffff", "2000");
    ClusterState state = state(installed);
    ZkWriteCommand restored = mutate(state, installed, previous);
    assertEquals(previous.getProperties(), routingRule(restored).getProperties());
    assertEquals(
        installed.getProperties(),
        state.getCollection("source")
            .getSlice("shard1")
            .getRoutingRules()
            .get("a!")
            .getProperties());
  }

  @Test
  public void testRemoveNewRule() {
    RoutingRule installed = rule("0-ffff", "2000");
    assertNull(routingRule(mutate(state(installed), installed, null)));
  }

  @Test
  public void testStaleCleanupLeavesNewerRule() {
    RoutingRule installed = rule("0-7fff", "2000");
    RoutingRule newer = rule("0-7fff,8000-ffff", "3000");
    assertSame(ZkStateWriter.NO_OP, mutate(state(newer), installed, null));
  }

  @Test
  public void testDuplicateRangeRestoresPreviousRule() {
    RoutingRule previous = rule("0-ffff", "1000");
    RoutingRule installed = rule("0-ffff,0-ffff", "2000");
    assertEquals(
        previous.getProperties(),
        routingRule(mutate(state(installed), installed, previous)).getProperties());
  }

  @Test
  public void testUnconditionalExpiryRemovalStillRemovesWholeRule() {
    RoutingRule installed = rule("0-7fff,8000-ffff", "2000");
    ZkWriteCommand removed =
        mutator().removeRoutingRule(state(installed), new ZkNodeProps(baseMessage()));
    assertNull(routingRule(removed));
  }

  private static RoutingRule rule(String ranges, String expiry) {
    return new RoutingRule(
        Map.of("routeRanges", ranges, "targetCollection", "target", "expireAt", expiry));
  }

  private static RoutingRule routingRule(ZkWriteCommand command) {
    return command.collection.getSlice("shard1").getRoutingRules().get("a!");
  }

  private static Map<String, Object> baseMessage() {
    return new HashMap<>(
        Map.of(
            ZkStateReader.COLLECTION_PROP, "source",
            ZkStateReader.SHARD_ID_PROP, "shard1",
            "routeKey", "a!"));
  }

  private static ZkWriteCommand mutate(
      ClusterState state, RoutingRule installed, RoutingRule previous) {
    Map<String, Object> props = baseMessage();
    props.put("expectedRule", installed.getProperties());
    if (previous != null) props.put("restoreRule", previous.getProperties());
    @SuppressWarnings("unchecked")
    Map<String, Object> serialized = (Map<String, Object>) Utils.fromJSON(Utils.toJSON(props));
    return mutator().removeRoutingRule(state, new ZkNodeProps(serialized));
  }

  private static SliceMutator mutator() {
    return new SliceMutator(mock(SolrCloudManager.class));
  }

  private static ClusterState state(RoutingRule rule) {
    Slice slice =
        new Slice("shard1", Map.of(), Map.of("routingRules", Map.of("a!", rule)), "source");
    DocCollection collection =
        DocCollection.create(
            "source", Map.of("shard1", slice), Map.of(), DocRouter.DEFAULT, 1, Instant.EPOCH, null);
    return new ClusterState(Set.of(), Map.of("source", collection));
  }
}
