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
package org.apache.solr.client.solrj.impl;

import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.request.JavaBinRequestWriter;
import org.apache.solr.client.solrj.request.XMLRequestWriter;
import org.apache.solr.client.solrj.response.JavaBinResponseParser;
import org.apache.solr.client.solrj.response.XMLResponseParser;
import org.junit.Test;

public class SetUseXmlFormatTest extends SolrTestCase {

  @Test
  public void testSetUseXmlFormatSwitchesWriterAndParser() throws Exception {
    try (var client = new HttpJdkSolrClient.Builder("http://127.0.0.1:1/solr").build()) {
      assertTrue(client.getRequestWriter() instanceof JavaBinRequestWriter);
      assertTrue(client.getParser() instanceof JavaBinResponseParser);

      client.setUseXmlFormat(true);
      assertTrue(client.getRequestWriter() instanceof XMLRequestWriter);
      assertTrue(client.getParser() instanceof XMLResponseParser);

      client.setUseXmlFormat(false);
      assertTrue(client.getRequestWriter() instanceof JavaBinRequestWriter);
      assertTrue(client.getParser() instanceof JavaBinResponseParser);
    }
  }
}
