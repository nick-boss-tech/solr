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
package org.apache.solr.handler.component;

import java.util.List;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.request.SolrRequestInfo;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.util.ThreadCpuTimer;
import org.junit.Test;

/** CPU snapshots with nonzero values independent of the platform's CPU timer resolution. */
public class SearchRequestInfoTest extends SolrTestCase {

  @Test
  public void testDistributedFailureCountsLocalCpuOnce() {
    SolrQueryResponse rsp = responseWithLocalCpu();
    rsp.setException(new SolrException(SolrException.ErrorCode.BAD_REQUEST, "Invalid query"));
    assertCpuTime(7L, true, rsp);
  }

  @Test
  public void testDistributedCpuIncludesLatestShardTotal() {
    SolrQueryResponse rsp = responseWithLocalCpu();
    rsp.getResponseHeader().remove(ThreadCpuTimer.CPU_TIME);
    rsp.getResponseHeader().add(ThreadCpuTimer.CPU_TIME, 3L);
    rsp.addToLog(ThreadCpuTimer.CPU_TIME, 3L);
    rsp.getResponseHeader().add(ThreadCpuTimer.CPU_TIME, 11L);
    rsp.addToLog(ThreadCpuTimer.CPU_TIME, 11L);
    assertCpuTime(18L, true, rsp);
  }

  @Test
  public void testLocalCpuNotDoubled() {
    assertCpuTime(7L, false, responseWithLocalCpu());
  }

  @Test
  public void testCpuUnavailable() {
    SolrQueryResponse rsp = new SolrQueryResponse();
    rsp.addResponseHeader(new SimpleOrderedMap<>());
    assertCpuTime(null, true, rsp);
  }

  private static SolrQueryResponse responseWithLocalCpu() {
    SolrQueryResponse rsp = new SolrQueryResponse();
    rsp.addResponseHeader(new SimpleOrderedMap<>());
    rsp.getResponseHeader().add(ThreadCpuTimer.CPU_TIME, 7L);
    rsp.addToLog(ThreadCpuTimer.LOCAL_CPU_TIME, 7L);
    return rsp;
  }

  private static void assertCpuTime(Long expected, boolean distributed, SolrQueryResponse rsp) {
    try (SolrQueryRequest req = new SolrQueryRequestBase(null, new ModifiableSolrParams())) {
      SolrRequestInfo requestInfo = new SolrRequestInfo(req, rsp);
      SolrRequestInfo.setRequestInfo(requestInfo);
      try {
        ResponseBuilder rb = new ResponseBuilder(req, rsp, List.of());
        rb.isDistrib = distributed;
        rb.shards = distributed ? new String[] {"shard1", "shard2"} : null;
        requestInfo.setResponseBuilder(rb);
        assertEquals(expected, SearchRequestInfo.from(req, rsp).cpuTimeMs());
      } finally {
        SolrRequestInfo.clearRequestInfo();
      }
    }
  }
}
