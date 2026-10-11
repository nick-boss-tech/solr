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
package org.apache.solr.handler;

import java.util.HashMap;
import java.util.List;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.api.model.FileMetaData;
import org.apache.solr.common.util.NamedList;
import org.junit.BeforeClass;

/** Wildcards in the leader's {@code confFiles}, such as managed resource variants. */
public class TestReplicationConfFileGlob extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml", "schema.xml");
  }

  private List<FileMetaData> confFiles(String name, String alias) {
    ReplicationHandler handler = (ReplicationHandler) h.getCore().getRequestHandler("/replication");
    assertNotNull(handler);
    NamedList<String> nameAndAlias = new NamedList<>();
    nameAndAlias.add(name, alias);
    return handler.getConfFileInfoFromCache(nameAndAlias, new HashMap<>());
  }

  public void testPlainNameIsUnchanged() {
    List<FileMetaData> files = confFiles("schema.xml", null);
    assertEquals(1, files.size());
    assertEquals("schema.xml", files.get(0).name);
  }

  public void testGlobExpandsToMatchingFiles() {
    List<FileMetaData> files = confFiles("stopwords*.txt", null);
    assertTrue("expected several stopword files, got " + files, files.size() > 1);
    for (FileMetaData file : files) {
      assertTrue(file.name, file.name.startsWith("stopwords") && file.name.endsWith(".txt"));
    }
  }

  public void testGlobMatchingNothingIsEmpty() {
    assertTrue(confFiles("no-such-prefix-*.txt", null).isEmpty());
  }

  public void testGlobCannotEscapeConfigDir() {
    assertTrue(confFiles("../*", null).isEmpty());
    assertTrue(confFiles("../../*.xml", null).isEmpty());
  }
}
