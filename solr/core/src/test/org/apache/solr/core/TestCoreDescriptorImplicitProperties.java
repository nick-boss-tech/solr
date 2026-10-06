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
package org.apache.solr.core;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import org.apache.solr.SolrTestCase;

/** SOLR-5262: implicit {@code solr.core.*} properties are available even when not configured. */
public class TestCoreDescriptorImplicitProperties extends SolrTestCase {

  private static CoreDescriptor descriptor(Map<String, String> coreProps) {
    Path instanceDir = createTempDir("instance").toAbsolutePath();
    return new CoreDescriptor("c1", instanceDir, coreProps, new Properties(), null);
  }

  public void testUlogDirDefaultsToDataDir() {
    Properties props = descriptor(Map.of()).getSubstitutableProperties();
    assertEquals("c1", props.getProperty("solr.core.name"));
    assertEquals("data" + File.separator, props.getProperty("solr.core.dataDir"));
    assertEquals(props.getProperty("solr.core.dataDir"), props.getProperty("solr.core.ulogDir"));
  }

  public void testExplicitUlogDirWins() {
    Properties props =
        descriptor(Map.of(CoreDescriptor.CORE_ULOGDIR, "/var/tlogs")).getSubstitutableProperties();
    assertEquals("/var/tlogs", props.getProperty("solr.core.ulogDir"));
  }

  public void testUnsetUlogDirStaysNullInStoredProperties() {
    assertNull(descriptor(Map.of()).getUlogDir());
  }
}
