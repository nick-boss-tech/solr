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

import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.solr.SolrTestCaseJ4;

public class SolrZkServerTlsTest extends SolrTestCaseJ4 {
  public void testAutomaticTlsSuppressesExistingPlaintextPort() throws Exception {
    Path home = createTempDir();
    Files.writeString(home.resolve("zoo.cfg"), "clientPort=2181\nsecureClientPort=2182\n");
    String property = "solr.zookeeper.embedded.tlsOnly";
    String previous = System.getProperty(property);
    try {
      System.setProperty(property, "true");
      SolrZkServer server = new SolrZkServer(true, null, home, home.toString(), 8983);
      server.parseConfig();
      assertNull(server.zkProps.getClientPortAddress());
      assertEquals(2182, server.zkProps.getSecureClientPortAddress().getPort());
      assertTrue(server.getClientString().endsWith(":2182"));
    } finally {
      if (previous == null) {
        System.clearProperty(property);
      } else {
        System.setProperty(property, previous);
      }
    }
  }
}
