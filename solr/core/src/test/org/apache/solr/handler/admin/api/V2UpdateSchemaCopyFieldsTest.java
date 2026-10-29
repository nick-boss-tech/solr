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

package org.apache.solr.handler.admin.api;

import java.nio.file.Path;
import org.apache.commons.io.file.PathUtils;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.util.EmbeddedSolrServerTestRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class V2UpdateSchemaCopyFieldsTest extends SolrTestCase {

  @ClassRule
  public static final EmbeddedSolrServerTestRule solrTestRule = new EmbeddedSolrServerTestRule();

  @BeforeClass
  public static void startMutableSchemaCore() throws Exception {
    System.setProperty("managed.schema.mutable", "true");
    System.setProperty("solr.index.updatelog.enabled", "false");
    Path solrHome = createTempDir("solrhome");
    PathUtils.copyDirectory(SolrTestCaseJ4.TEST_HOME(), solrHome);
    solrTestRule.startSolr(solrHome);
    SolrTestCaseJ4.newRandomConfig();
    solrTestRule
        .newCollection()
        .withConfigSet(solrHome.resolve("collection1").resolve("conf"))
        .withConfigFile("solrconfig-managed-schema.xml")
        .withSchemaFile("schema-rest.xml")
        .create();
  }

  @Test
  public void testDeleteCopyFieldsForSourceWithoutCopyFieldsIsNoOp() throws Exception {
    try (SolrCore core =
        solrTestRule.getCoreContainer().getCore(SolrTestCaseJ4.DEFAULT_TEST_CORENAME)) {
      new UpdateSchema(core, request(core)).deleteCopyFields("no_such_copy_source");

      assertTrue(core.getLatestSchema().getCopyFieldsList("no_such_copy_source").isEmpty());
    }
  }

  @Test
  public void testDeleteCopyFieldsRemovesEveryDestinationOfSource() throws Exception {
    try (SolrCore core =
        solrTestRule.getCoreContainer().getCore(SolrTestCaseJ4.DEFAULT_TEST_CORENAME)) {
      assertFalse(core.getLatestSchema().getCopyFieldsList("copyfield_source").isEmpty());

      new UpdateSchema(core, request(core)).deleteCopyFields("copyfield_source");

      assertTrue(core.getLatestSchema().getCopyFieldsList("copyfield_source").isEmpty());
    }
  }

  private static SolrQueryRequestBase request(SolrCore core) {
    return new SolrQueryRequestBase(core, new ModifiableSolrParams()) {};
  }
}
