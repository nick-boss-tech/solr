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

package org.apache.solr.update.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.util.EmbeddedSolrServerTestRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * SOLR-15018: an atomic update must not silently delete nested child documents when the schema has
 * a catch-all ignored dynamic field. The child field names match the ignored field, and the
 * document reconstruction behind the atomic update used to skip them.
 */
public class NestedAtomicUpdateIgnoredFieldTest extends SolrTestCase {

  @ClassRule
  public static final EmbeddedSolrServerTestRule solrTestRule = new EmbeddedSolrServerTestRule();

  @BeforeClass
  public static void beforeClass() throws Exception {
    solrTestRule.startSolr(SolrTestCaseJ4.TEST_HOME());
    SolrTestCaseJ4.newRandomConfig();
    solrTestRule
        .newCollection()
        .withConfigSet(SolrTestCaseJ4.TEST_COLL1_CONF())
        .withConfigFile("solrconfig-tlog.xml")
        .withSchemaFile("schema-nest-ignored.xml")
        .create();
  }

  @Test
  public void testAtomicUpdateKeepsChildDocsWithCatchAllIgnoredField() throws Exception {
    SolrClient client = solrTestRule.getSolrClient();
    client.deleteByQuery("*:*");
    client.commit();

    SolrInputDocument grandChild = new SolrInputDocument();
    grandChild.addField("id", "3");
    grandChild.addField("child_s", "grandchild");

    SolrInputDocument child = new SolrInputDocument();
    child.addField("id", "2");
    child.addField("child_s", "child");
    child.setField("grandChild", List.of(grandChild));

    SolrInputDocument parent = new SolrInputDocument();
    parent.addField("id", "1");
    parent.addField("title_s", "parent");
    parent.setField("child1", List.of(child));

    client.add(parent);
    client.commit();

    // Evict the original add from the transaction log with unrelated updates, so the
    // atomic update below cannot be served the original document from the log and must
    // reconstruct the stored document from the index instead. solrconfig-tlog.xml keeps
    // at most 100 records in at most 10 log files, so write 120 filler records over 12
    // commits: beyond both retention limits, the original add is neither in the update
    // log's recent records nor in any retained log file.
    for (int batch = 0; batch < 12; batch++) {
      for (int i = 0; i < 10; i++) {
        SolrInputDocument filler = new SolrInputDocument();
        filler.addField("id", String.valueOf(100 + batch * 10 + i));
        filler.addField("title_s", "filler");
        client.add(filler);
      }
      client.commit();
    }

    SolrInputDocument atomicUpdate = new SolrInputDocument();
    atomicUpdate.addField("id", "1");
    atomicUpdate.setField("title_s", Map.of("set", "parent-updated"));
    client.add(atomicUpdate);
    client.commit();

    SolrQuery query = new SolrQuery("id:1");
    query.setParam("fl", "*,[child]");
    SolrDocument result = client.query(query).getResults().get(0);
    assertEquals("parent-updated", result.getFirstValue("title_s"));

    List<String> nestedIds = new ArrayList<>();
    collectNestedIds(result, nestedIds);
    assertTrue("child doc must survive the atomic update: " + result, nestedIds.contains("2"));
    assertTrue("grandchild doc must survive the atomic update: " + result, nestedIds.contains("3"));

    // The child and grandchild are also still indexed as documents in their own right.
    assertEquals(1, client.query(new SolrQuery("id:2")).getResults().getNumFound());
    assertEquals(1, client.query(new SolrQuery("id:3")).getResults().getNumFound());
  }

  private static void collectNestedIds(SolrDocument doc, List<String> ids) {
    for (String fieldName : doc.getFieldNames()) {
      Object value = doc.getFieldValue(fieldName);
      if (value instanceof SolrDocument childDoc) {
        ids.add(String.valueOf(childDoc.getFirstValue("id")));
        collectNestedIds(childDoc, ids);
      } else if (value instanceof Collection<?> values) {
        for (Object element : values) {
          if (element instanceof SolrDocument childDoc) {
            ids.add(String.valueOf(childDoc.getFirstValue("id")));
            collectNestedIds(childDoc, ids);
          }
        }
      }
    }
    if (doc.getChildDocuments() != null) {
      for (SolrDocument childDoc : doc.getChildDocuments()) {
        ids.add(String.valueOf(childDoc.getFirstValue("id")));
        collectNestedIds(childDoc, ids);
      }
    }
  }
}
