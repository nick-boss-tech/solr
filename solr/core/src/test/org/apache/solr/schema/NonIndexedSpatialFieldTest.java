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
package org.apache.solr.schema;

import org.apache.solr.SolrTestCaseJ4;
import org.junit.BeforeClass;

/** Spatial prefix-tree fields that are stored but not indexed must load and round-trip values. */
public class NonIndexedSpatialFieldTest extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig-minimal.xml", "schema-nonindexed-spatial.xml");
  }

  public void testStoredOnlyFieldsRoundTrip() {
    assertU(adoc("id", "1", "daterange_stored", "[2000 TO 2014-05-21]", "srpt_stored", "25,82"));
    assertU(commit());

    assertQ(
        req("q", "id:1", "fl", "daterange_stored,srpt_stored"),
        "//result[@numFound='1']",
        "//result/doc/*[@name='daterange_stored']",
        "//result/doc/*[@name='srpt_stored']");
  }
}
