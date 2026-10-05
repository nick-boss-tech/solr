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
package org.apache.solr.search.grouping.distributed.shardresultserializer;

import static org.apache.solr.SolrTestCaseJ4.sdoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.params.GroupParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.util.EmbeddedSolrServerTestRule;
import org.apache.solr.util.SolrClientTestRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class TopGroupsResultTransformerTest extends SolrTestCase {

  private static final String STORED_ID =
      "<field name=\"id\" type=\"string\" indexed=\"true\" stored=\"true\" required=\"true\"/>";
  private static final String DOCVALUES_ONLY_ID =
      "<field name=\"id\" type=\"string\" indexed=\"true\" stored=\"false\" docValues=\"true\""
          + " required=\"true\"/>";

  @ClassRule public static final SolrClientTestRule solrTestRule = new EmbeddedSolrServerTestRule();

  @BeforeClass
  public static void beforeClass() {
    SolrTestCaseJ4.newRandomConfig();
  }

  @Test
  public void testSecondPhaseWithUniqueKeyNotStored() throws Exception {
    solrTestRule.startSolr();

    Path configSet = LuceneTestCase.createTempDir();
    SolrTestCaseJ4.copyMinConf(configSet);
    Path schemaXml = configSet.resolve("conf/schema.xml");
    String schema = Files.readString(schemaXml);
    String schemaWithUnstoredId = schema.replace(STORED_ID, DOCVALUES_ONLY_ID);
    assertNotEquals(
        "the id field definition was not found in the test schema", schema, schemaWithUnstoredId);
    Files.writeString(schemaXml, schemaWithUnstoredId);

    solrTestRule.newCollection().withConfigSet(configSet).create();

    SolrClient client = solrTestRule.getSolrClient();
    client.add(sdoc("id", "1", "grp_s", "a"));
    client.add(sdoc("id", "2", "grp_s", "a"));
    client.add(sdoc("id", "3", "grp_s", "b"));
    client.commit();

    // the request a shard receives in the second phase of a distributed grouping request
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("q", "*:*");
    params.set(GroupParams.GROUP, true);
    params.set(GroupParams.GROUP_FIELD, "grp_s");
    params.set(GroupParams.GROUP_LIMIT, 10);
    params.set(GroupParams.GROUP_DISTRIBUTED_SECOND, true);
    params.add(GroupParams.GROUP_DISTRIBUTED_TOPGROUPS_PREFIX + "grp_s", "a", "b");

    NamedList<Object> response = client.request(new QueryRequest(params));
    NamedList<?> secondPhase = (NamedList<?>) response.get("secondPhase");
    NamedList<?> groups = (NamedList<?>) secondPhase.get("grp_s");

    Set<String> ids = new TreeSet<>();
    for (String groupValue : List.of("a", "b")) {
      NamedList<?> group = (NamedList<?>) groups.get(groupValue);
      for (Object document : (Iterable<?>) group.get("documents")) {
        ids.add((String) ((NamedList<?>) document).get("id"));
      }
    }
    assertEquals(Set.of("1", "2", "3"), ids);
  }
}
