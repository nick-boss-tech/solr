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

import java.util.Arrays;
import org.apache.solr.BaseDistributedSearchTestCase;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.BeforeClass;
import org.junit.Test;

/** A distributed {@code {!knn topK=N}} query returns N hits overall, not N per shard. */
public class DistributedKnnTopKTest extends BaseDistributedSearchTestCase {

  private static final int NUM_DOCS = 24;
  private static final int TOP_K = 3;

  public DistributedKnnTopKTest() {
    super();
    fixShardCount(3);
  }

  @BeforeClass
  public static void setUpClass() {
    schemaString = "schema-vector-catchall.xml";
  }

  @Test
  public void test() throws Exception {
    del("*:*");
    for (int i = 1; i <= NUM_DOCS; i++) {
      SolrInputDocument doc = new SolrInputDocument();
      doc.addField("id", Integer.toString(i));
      // vectors move away from [1,0,0,0] as i grows, so doc 1 is the closest
      doc.addField("vector", Arrays.asList(1f, i / 10f, 0.1f, 0.1f));
      indexDoc(doc);
    }
    commit();

    final String knn = "{!knn f=vector topK=" + TOP_K + "}[1.0, 0.0, 0.1, 0.1]";

    QueryResponse rsp = query("q", knn, "fl", "id,score", "rows", "20");
    SolrDocumentList results = rsp.getResults();
    assertEquals("numFound is topK overall, not per shard", TOP_K, results.getNumFound());
    assertEquals("rows larger than topK still yields topK docs", TOP_K, results.size());
    for (int i = 0; i < TOP_K; i++) {
      assertEquals("closest vectors first", Integer.toString(i + 1), results.get(i).get("id"));
    }

    rsp = query("q", knn, "fl", "id,score", "rows", "2", "start", "2");
    assertEquals(TOP_K, rsp.getResults().getNumFound());
    assertEquals("start+rows past topK leaves the last hit", 1, rsp.getResults().size());
  }
}
