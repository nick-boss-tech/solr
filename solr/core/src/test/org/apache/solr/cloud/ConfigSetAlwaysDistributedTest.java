/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.cloud;

import java.util.Collection;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.ConfigSetAdminRequest.Create;
import org.apache.solr.client.solrj.request.ConfigSetAdminRequest.Delete;
import org.apache.solr.client.solrj.request.ConfigSetAdminRequest.List;
import org.apache.solr.client.solrj.response.ConfigSetAdminResponse;
import org.apache.solr.core.ConfigSetService;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ConfigSet commands always run through the distributed runner, even while the Overseer is enabled
 * for Collection commands. The Overseer ConfigSet message handler no longer exists, so the runner
 * must be constructed regardless of the Overseer switch, and ConfigSet create and delete through
 * the API must succeed on a cluster with the Overseer enabled.
 */
public class ConfigSetAlwaysDistributedTest extends SolrCloudTestCase {

  @BeforeClass
  public static void setUpClass() throws Exception {
    configureCluster(1).configure();
  }

  private static ConfigSetService getConfigSetService() {
    return cluster.getJettySolrRunners().get(0).getCoreContainer().getConfigSetService();
  }

  @Test
  public void testConfigSetCommandsRunDistributedWithOverseerEnabled() throws Exception {
    ZkController zkController =
        cluster.getJettySolrRunners().get(0).getCoreContainer().getZkController();
    assertTrue(
        "Overseer is expected to be enabled in this cluster", zkController.isOverseerEnabled());
    assertTrue(
        "Distributed runner must be present even with the Overseer enabled",
        zkController.getDistributedCommandRunner().isPresent());

    getConfigSetService().uploadConfig("base16542", configset("configset-2"));

    final SolrClient solrClient = cluster.getJettySolrRunners().get(0).getSolrClient();
    Create create = new Create();
    create.setConfigSetName("child16542").setBaseConfigSetName("base16542");
    ConfigSetAdminResponse createResponse = create.process(solrClient);
    assertEquals(0, createResponse.getStatus());

    List list = new List();
    Collection<String> configSets = list.process(solrClient).getConfigSets();
    assertTrue(configSets.contains("child16542"));

    Delete delete = new Delete();
    delete.setConfigSetName("child16542");
    ConfigSetAdminResponse deleteResponse = delete.process(solrClient);
    assertEquals(0, deleteResponse.getStatus());

    configSets = list.process(solrClient).getConfigSets();
    assertFalse(configSets.contains("child16542"));
  }
}
