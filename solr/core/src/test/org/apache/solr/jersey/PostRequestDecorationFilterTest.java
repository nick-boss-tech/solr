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

package org.apache.solr.jersey;

import static org.apache.solr.jersey.RequestContextKeys.SOLR_QUERY_REQUEST;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import java.util.Set;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.api.model.SubResponseAccumulatingJerseyResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrQueryRequestBase;
import org.junit.Test;

public class PostRequestDecorationFilterTest extends SolrTestCaseJ4 {

  @Test
  public void testNonZeroResponseHeaderStatusIsUsedForHttpStatus() throws Exception {
    final ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
    final ContainerResponseContext responseContext = mock(ContainerResponseContext.class);
    final SolrQueryRequest solrQueryRequest =
        new SolrQueryRequestBase(null, new ModifiableSolrParams()) {};
    final SubResponseAccumulatingJerseyResponse response =
        new SubResponseAccumulatingJerseyResponse();
    response.responseHeader.status = 500;

    when(requestContext.getPropertyNames()).thenReturn(Set.of(SOLR_QUERY_REQUEST));
    when(requestContext.getProperty(SOLR_QUERY_REQUEST)).thenReturn(solrQueryRequest);
    when(responseContext.hasEntity()).thenReturn(true);
    when(responseContext.getEntity()).thenReturn(response);

    new PostRequestDecorationFilter().filter(requestContext, responseContext);

    verify(responseContext).setStatus(500);
  }
}
