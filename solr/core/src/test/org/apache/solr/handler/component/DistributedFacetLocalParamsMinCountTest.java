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

/** SOLR-11129: facet.mincount given as a local param must be honored by the coordinator. */
public class DistributedFacetLocalParamsMinCountTest extends BaseDistributedSearchTestCase {

  private static final String FLD = "t_s";

  @Before
  public void prepareIndex() throws Exception {
    del("*:*");
    int docId = 0;
    index(id, docId++, FLD, "A");
    index(id, docId++, FLD, "B");
    index(id, docId++, FLD, "B");
    index(id, docId++, FLD, "C");
    index(id, docId++, FLD, "C");
    index(id, docId++, FLD, "C");
    commit();

    handle.clear();
    handle.put("QTime", SKIPVAL);
    handle.put("timestamp", SKIPVAL);
    handle.put("maxScore", SKIPVAL);
    handle.put("_version_", SKIPVAL);
  }

  @ShardsFixed(num = 3)
  public void test() throws Exception {
    // the filter excludes "A" and "B" entirely, so with mincount=0 they would be listed with 0
    final ModifiableSolrParams withLocalParam = new ModifiableSolrParams();
    withLocalParam.set("q", "*:*");
    withLocalParam.set("fq", FLD + ":C");
    withLocalParam.set("rows", "0");
    withLocalParam.set("facet", "true");
    withLocalParam.set("facet.field", "{!key=k facet.mincount=1}" + FLD);
    // compares the distributed response with the single-node control response
    QueryResponse rsp = query(withLocalParam);

    FacetField ff = rsp.getFacetField("k");
    assertEquals("only terms with a count of at least 1 must be returned", 1, ff.getValueCount());
    assertEquals("C", ff.getValues().get(0).getName());
    assertEquals(3, ff.getValues().get(0).getCount());

    // a larger mincount given as a local param
    final ModifiableSolrParams higherMinCount = new ModifiableSolrParams();
    higherMinCount.set("q", "*:*");
    higherMinCount.set("rows", "0");
    higherMinCount.set("facet", "true");
    higherMinCount.set("facet.field", "{!key=k facet.mincount=2}" + FLD);
    rsp = query(higherMinCount);
    ff = rsp.getFacetField("k");
    assertEquals(2, ff.getValueCount());
  }
}
