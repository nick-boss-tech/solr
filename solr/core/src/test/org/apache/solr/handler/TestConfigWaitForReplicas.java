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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.util.URLUtil;
import org.junit.Test;

/** Which replicas that missed the new config version still fail a config request. */
public class TestConfigWaitForReplicas extends SolrTestCase {

  private static Replica replica(String nodeName, String coreName) {
    Map<String, Object> props = new HashMap<>();
    props.put(ZkStateReader.NODE_NAME_PROP, nodeName);
    props.put(ZkStateReader.BASE_URL_PROP, URLUtil.getBaseUrlForNodeName(nodeName, "http"));
    props.put(ZkStateReader.CORE_NAME_PROP, coreName);
    return new Replica(coreName, props, "coll", "shard1");
  }

  @Test
  public void testReplicaGoneFromActiveSetDoesNotFailRequest() {
    Replica deleted = replica("127.0.0.1:10001_solr", "coll_shard1_replica_n1");
    Replica live = replica("127.0.0.1:10002_solr", "coll_shard1_replica_n2");

    List<String> stillActive =
        SolrConfigHandler.failedCoresStillActive(
            List.of(deleted.getCoreUrl(), live.getCoreUrl()), List.of(live));

    assertEquals(List.of(live.getCoreUrl()), stillActive);
  }

  @Test
  public void testActiveReplicaThatMissedTheVersionStillFails() {
    Replica first = replica("127.0.0.1:10001_solr", "coll_shard1_replica_n1");
    Replica second = replica("127.0.0.1:10002_solr", "coll_shard1_replica_n2");
    List<String> failed = List.of(second.getCoreUrl(), first.getCoreUrl());

    assertEquals(failed, SolrConfigHandler.failedCoresStillActive(failed, List.of(first, second)));
  }

  @Test
  public void testNoActiveReplicasFailsNothing() {
    Replica replica = replica("127.0.0.1:10001_solr", "coll_shard1_replica_n1");

    assertTrue(
        SolrConfigHandler.failedCoresStillActive(List.of(replica.getCoreUrl()), List.of())
            .isEmpty());
  }

  @Test
  public void testNothingFailedFailsNothing() {
    Replica replica = replica("127.0.0.1:10001_solr", "coll_shard1_replica_n1");

    assertTrue(SolrConfigHandler.failedCoresStillActive(List.of(), List.of(replica)).isEmpty());
  }
}
