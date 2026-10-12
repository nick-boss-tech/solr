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

import org.apache.solr.BaseDistributedSearchTestCase;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Before;

/**
 * SOLR-11129: a facet.offset given as a local param is applied by the shards, so the coordinator
 * must not apply it a second time when merging.
 */
public class DistributedFacetLocalParamsOffsetTest extends BaseDistributedSearchTestCase {

  private static final String FLD = "t_s";

  @Before
  public void prepareIndex() throws Exception {
    del("*:*");
    int docId = 0;
    // one doc per term per shard, so every shard lists all four terms in index order
    for (String term : new String[] {"A", "B", "C", "D"}) {
      for (int i = 0; i < 3; i++) {
        index(id, docId++, FLD, term);
      }
    }
    commit();

    handle.clear();
    handle.put("QTime", SKIPVAL);
    handle.put("timestamp", SKIPVAL);
    handle.put("maxScore", SKIPVAL);
    handle.put("_version_", SKIPVAL);
  }

  @ShardsFixed(num = 3)
  public void test() throws Exception {
    final ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("q", "*:*");
    params.set("rows", "0");
    params.set("facet", "true");
    params.set("facet.field", "{!key=k facet.offset=1 facet.limit=2 facet.sort=index}" + FLD);
    // compares the distributed response with the single-node control response
    QueryResponse rsp = query(params);

    // the offset is applied once, so the page starts at B, as on a single node
    FacetField ff = rsp.getFacetField("k");
    assertEquals(2, ff.getValueCount());
    assertEquals("B", ff.getValues().get(0).getName());
    assertEquals(3, ff.getValues().get(0).getCount());
    assertEquals("C", ff.getValues().get(1).getName());
    assertEquals(3, ff.getValues().get(1).getCount());
  }
}
