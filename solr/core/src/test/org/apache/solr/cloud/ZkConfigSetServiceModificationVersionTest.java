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
package org.apache.solr.cloud;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.solr.SolrTestCase;
import org.apache.zookeeper.data.Stat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ZkConfigSetServiceModificationVersionTest extends SolrTestCase {

  private static final String CONFIG = "conf";
  private static final String SCHEMA = "schema.xml";
  private static final String SCHEMA_PATH = "/configs/" + CONFIG + "/" + SCHEMA;

  private static ZkTestServer zkServer;

  @BeforeClass
  public static void startZkServer() throws Exception {
    zkServer = new ZkTestServer(createTempDir("zkData"));
    zkServer.run();
  }

  @AfterClass
  public static void shutdownZkServer() throws IOException, InterruptedException {
    if (null != zkServer) {
      zkServer.shutdown();
    }
    zkServer = null;
  }

  @Test
  public void testModificationVersionChangesWhenConfigSetIsRecreated() throws Exception {
    ZkConfigSetService service = new ZkConfigSetService(zkServer.getZkClient());
    Path configDir = createTempDir("conf");
    Files.writeString(configDir.resolve(SCHEMA), "<schema/>");

    service.uploadConfig(CONFIG, configDir);
    Long first = service.getCurrentSchemaModificationVersion(CONFIG, null, SCHEMA);
    int firstDataVersion = dataVersion();
    assertEquals(
        "unchanged file keeps its modification version",
        first,
        service.getCurrentSchemaModificationVersion(CONFIG, null, SCHEMA));

    service.deleteConfig(CONFIG);
    service.uploadConfig(CONFIG, configDir);
    Long recreated = service.getCurrentSchemaModificationVersion(CONFIG, null, SCHEMA);
    assertEquals("the znode data version starts over", firstDataVersion, dataVersion());
    assertNotEquals("recreated config set gets a new version", first, recreated);

    service.uploadFileToConfig(
        CONFIG, SCHEMA, "<schema name='changed'/>".getBytes(StandardCharsets.UTF_8), true);
    assertNotEquals(
        "changed file gets a new version",
        recreated,
        service.getCurrentSchemaModificationVersion(CONFIG, null, SCHEMA));

    assertNull(service.getCurrentSchemaModificationVersion("missing", null, SCHEMA));
  }

  private static int dataVersion() throws Exception {
    Stat stat = zkServer.getZkClient().exists(SCHEMA_PATH, null);
    assertNotNull(stat);
    return stat.getVersion();
  }
}
