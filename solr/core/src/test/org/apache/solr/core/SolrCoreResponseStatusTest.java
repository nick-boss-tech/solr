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

import org.apache.solr.SolrTestCase;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.request.SolrQueryRequestBase;
import org.apache.solr.response.SolrQueryResponse;
import org.junit.Test;

/** Unit tests for status overrides in the V1 response header. */
public class SolrCoreResponseStatusTest extends SolrTestCase {

  @Test
  public void testHttpStatusOverrideIsAddedToResponseHeader() {
    try (var request = new SolrQueryRequestBase(null, new ModifiableSolrParams()) {}) {
      final var response = new SolrQueryResponse();
      response.addResponseHeader(new SimpleOrderedMap<>());
      response.setHttpStatusCodeOverride(500);

      SolrCore.postDecorateResponse(null, request, response);

      assertEquals(500, response.getResponseHeader().get("status"));
    }
  }

  @Test
  public void testExceptionTakesPrecedenceOverStatusOverride() {
    try (var request = new SolrQueryRequestBase(null, new ModifiableSolrParams()) {}) {
      final var response = new SolrQueryResponse();
      response.addResponseHeader(new SimpleOrderedMap<>());
      response.setHttpStatusCodeOverride(500);
      response.setException(
          new SolrException(SolrException.ErrorCode.BAD_REQUEST, "invalid request"));

      SolrCore.postDecorateResponse(null, request, response);

      assertEquals(400, response.getResponseHeader().get("status"));
    }
  }
}
