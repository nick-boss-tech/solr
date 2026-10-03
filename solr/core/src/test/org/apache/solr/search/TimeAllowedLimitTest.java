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
package org.apache.solr.search;

import org.apache.solr.SolrTestCase;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.handler.component.ShardRequest;
import org.apache.solr.request.SolrQueryRequestBase;
import org.junit.Test;

public class TimeAllowedLimitTest extends SolrTestCase {

  /** A limit whose time was already used up by an earlier phase of the request. */
  private static TimeAllowedLimit exhaustedLimit() {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set(CommonParams.TIME_ALLOWED, "100");
    params.set(TimeAllowedLimit.USED_PARAM, "500");
    return new TimeAllowedLimit(new SolrQueryRequestBase(null, params) {});
  }

  private static ModifiableSolrParams shardParams() {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set(CommonParams.TIME_ALLOWED, "100");
    return params;
  }

  @Test
  public void testDocumentFetchIsNotSkippedWhenTimeIsUsedUp() {
    ShardRequest sreq = new ShardRequest();
    sreq.purpose = ShardRequest.PURPOSE_GET_FIELDS;
    ModifiableSolrParams params = shardParams();

    assertFalse(exhaustedLimit().adjustShardRequestLimit(sreq, "shard1", params));
    assertNull("the fetch must not be time limited", params.get(CommonParams.TIME_ALLOWED));
  }

  @Test
  public void testOtherPhasesAreStillSkippedWhenTimeIsUsedUp() {
    ShardRequest sreq = new ShardRequest();
    sreq.purpose = ShardRequest.PURPOSE_GET_TOP_IDS;
    ModifiableSolrParams params = shardParams();

    assertTrue(exhaustedLimit().adjustShardRequestLimit(sreq, "shard1", params));
    assertEquals("100", params.get(CommonParams.TIME_ALLOWED));
  }
}
