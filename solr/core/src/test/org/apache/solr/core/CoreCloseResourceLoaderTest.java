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

import org.apache.solr.SolrTestCaseJ4;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/** A core owns its {@link SolrResourceLoader}, so closing the core closes the loader. */
public class CoreCloseResourceLoaderTest extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml", "schema.xml");
  }

  @AfterClass
  public static void afterClass() {
    deleteCore();
  }

  public void testReloadClosesTheOldLoaderOnly() throws Exception {
    SolrCore oldCore = h.getCore();
    SolrResourceLoader oldLoader = oldCore.getResourceLoader();
    assertFalse(oldLoader.isClosed());

    h.reload();
    // the reference held here keeps the old core open
    assertFalse(oldLoader.isClosed());
    oldCore.close();
    assertTrue(oldLoader.isClosed());

    try (SolrCore newCore = h.getCore()) {
      SolrResourceLoader newLoader = newCore.getResourceLoader();
      assertNotSame(oldLoader, newLoader);
      assertFalse(newLoader.isClosed());
    }
  }
}
