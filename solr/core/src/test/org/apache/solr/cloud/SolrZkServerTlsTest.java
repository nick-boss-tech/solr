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
import org.apache.solr.common.SolrException;

public class SolrZkServerTlsTest extends SolrTestCaseJ4 {
  public void testAutomaticTlsSuppressesExistingPlaintextPort() throws Exception {
    Path home = createTempDir();
    Files.writeString(
        home.resolve("zoo.cfg"),
        "clientPort=2181\nsecureClientPort=2182\nclient.portUnification=true\n");
    String property = "solr.zookeeper.embedded.tlsOnly";
    String previous = System.getProperty(property);
    try {
      System.setProperty(property, "true");
      SolrZkServer server = new SolrZkServer(true, null, home, home.toString(), 8983);
      server.parseConfig();
      assertNull(server.zkProps.getClientPortAddress());
      assertEquals(2182, server.zkProps.getSecureClientPortAddress().getPort());
      assertTrue(server.getClientString().endsWith(":2182"));
      assertEquals("false", System.getProperty("zookeeper.client.portUnification"));
    } finally {
      if (previous == null) {
        System.clearProperty(property);
      } else {
        System.setProperty(property, previous);
      }
    }
  }

  public void testPlaintextPortRemainsWhenAutomaticTlsIsDisabled() throws Exception {
    Path home = createTempDir();
    Files.writeString(home.resolve("zoo.cfg"), "clientPort=2181\n");
    System.setProperty("solr.zookeeper.embedded.tlsOnly", "false");
    SolrZkServer server = new SolrZkServer(true, null, home, home.toString(), 8983);
    server.parseConfig();
    assertEquals(2181, server.zkProps.getClientPortAddress().getPort());
    assertNull(server.zkProps.getSecureClientPortAddress());
  }

  public void testAutomaticTlsRequiresSecurePort() throws Exception {
    Path home = createTempDir();
    Files.writeString(home.resolve("zoo.cfg"), "clientPort=2181\n");
    System.setProperty("solr.zookeeper.embedded.tlsOnly", "true");
    SolrZkServer server = new SolrZkServer(true, null, home, home.toString(), 8983);
    SolrException error = expectThrows(SolrException.class, server::parseConfig);
    assertTrue(error.getMessage().contains("requires secureClientPort"));
  }

  public void testAutomaticTlsRejectsEnsemblePlaintextClientAddress() throws Exception {
    Path home = createTempDir();
    Files.writeString(home.resolve("myid"), "1\n");
    Files.writeString(
        home.resolve("zoo.cfg"),
        "secureClientPort=2182\n"
            + "initLimit=10\nsyncLimit=5\n"
            + "server.1=127.0.0.1:2888:3888;127.0.0.1:2181\n"
            + "server.2=127.0.0.1:2889:3889\n"
            + "server.3=127.0.0.1:2890:3890\n");
    System.setProperty("solr.zookeeper.embedded.tlsOnly", "true");
    SolrZkServer server = new SolrZkServer(true, null, home, home.toString(), 8983);
    SolrException error = expectThrows(SolrException.class, server::parseConfig);
    assertTrue(error.getMessage().contains("plaintext client address"));
  }

  public void testAutomaticTlsAcceptsEnsembleWithoutPlaintextClientAddress() throws Exception {
    Path home = createTempDir();
    Files.writeString(home.resolve("myid"), "1\n");
    Files.writeString(
        home.resolve("zoo.cfg"),
        "secureClientPort=2182\n"
            + "initLimit=10\nsyncLimit=5\n"
            + "server.1=127.0.0.1:2888:3888\n"
            + "server.2=127.0.0.1:2889:3889\n"
            + "server.3=127.0.0.1:2890:3890\n");
    System.setProperty("solr.zookeeper.embedded.tlsOnly", "true");
    SolrZkServer server = new SolrZkServer(true, null, home, home.toString(), 8983);
    server.parseConfig();
    assertNull(server.zkProps.getClientPortAddress());
    assertEquals(2182, server.zkProps.getSecureClientPortAddress().getPort());
  }
}
