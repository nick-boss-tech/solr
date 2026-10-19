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
package org.apache.solr.client.solrj.jetty;

import org.apache.solr.SolrTestCase;
import org.junit.Test;

public class HttpJettySolrClientSSLConfigTest extends SolrTestCase {

  @Test
  public void testNonSslConfigWithHttpsUrlFailsWithClearMessage() {
    SSLConfig nonSsl = new SSLConfig(false, false, null, null, null, null);
    IllegalArgumentException e =
        expectThrows(
            IllegalArgumentException.class,
            () ->
                new HttpJettySolrClient.Builder("https://127.0.0.1:1/solr")
                    .withSSLConfig(nonSsl)
                    .build());
    assertTrue(e.getMessage(), e.getMessage().contains("not in SSL mode"));
  }

  @Test
  public void testNonSslConfigWithHttpUrlBuilds() throws Exception {
    SSLConfig nonSsl = new SSLConfig(false, false, null, null, null, null);
    try (var client =
        new HttpJettySolrClient.Builder("http://127.0.0.1:1/solr").withSSLConfig(nonSsl).build()) {
      assertNotNull(client);
    }
  }
}
