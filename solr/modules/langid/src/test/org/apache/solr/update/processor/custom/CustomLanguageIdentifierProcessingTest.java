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
package org.apache.solr.update.processor.custom;

import java.nio.file.Path;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.util.EmbeddedSolrServerTestRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class CustomLanguageIdentifierProcessingTest extends SolrTestCase {

  @ClassRule
  public static final EmbeddedSolrServerTestRule solrTestRule = new EmbeddedSolrServerTestRule();

  @BeforeClass
  public static void startSolr() throws Exception {
    solrTestRule.startSolr(createTempDir());
    solrTestRule
        .newCollection()
        .withConfigSet(configSetDir())
        .withConfigFile("solrconfig-custom.xml")
        .withSchemaFile("schema-custom.xml")
        .create();
  }

  private static Path configSetDir() throws Exception {
    return Path.of(
        CustomLanguageIdentifierProcessingTest.class
            .getClassLoader()
            .getResource("langid/solr/custom/conf")
            .toURI());
  }

  @Test
  public void testCustomIdentifierSetsLanguageOnProcessedDocument() throws Exception {
    SolrClient client = solrTestRule.getSolrClient();
    client.deleteByQuery("*:*");
    add(client, "1", "hej och valkommen", "custom_langid");
    add(client, "2", "the quick brown fox", "custom_langid");
    add(client, "3", "hej utan kedja", null);
    client.commit();

    assertEquals("sv", languageOf(client, "1"));
    assertEquals("en", languageOf(client, "2"));
    assertNull("no chain, so no detection", languageOf(client, "3"));
  }

  private static void add(SolrClient client, String id, String text, String chain)
      throws Exception {
    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", id);
    doc.addField("text", text);
    UpdateRequest request = new UpdateRequest();
    if (chain != null) {
      request.setParam("update.chain", chain);
    }
    request.add(doc);
    request.process(client);
  }

  private static String languageOf(SolrClient client, String id) throws Exception {
    SolrDocument doc = client.getById(id);
    return (String) doc.getFieldValue("language_s");
  }
}
