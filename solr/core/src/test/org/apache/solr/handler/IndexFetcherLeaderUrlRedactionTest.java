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

import static org.apache.solr.SolrTestCaseJ4.assumeWorkingMockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.apache.solr.SolrTestCase;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.security.AllowListUrlChecker;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The leader URL a follower is configured with can carry basic-auth credentials. When {@link
 * IndexFetcher} reports a problem with that URL, the error must not print the password; the
 * connection itself keeps using the original URL.
 */
public class IndexFetcherLeaderUrlRedactionTest extends SolrTestCase {

  @BeforeClass
  public static void ensureWorkingMockito() {
    assumeWorkingMockito();
  }

  private static SolrCore coreWithRealAllowListChecker() throws Exception {
    SolrCore core = mock(SolrCore.class);
    CoreContainer container = mock(CoreContainer.class);
    when(core.getCoreContainer()).thenReturn(container);
    when(container.getZkController()).thenReturn(null);
    when(container.getAllowListUrlChecker()).thenReturn(new AllowListUrlChecker(List.of()));
    return core;
  }

  private static NamedList<Object> initArgsWithLeaderUrl(String leaderUrl) {
    NamedList<Object> initArgs = new NamedList<>();
    initArgs.add("leaderUrl", leaderUrl);
    return initArgs;
  }

  @Test
  public void testMalformedLeaderUrlErrorRedactsPassword() throws Exception {
    // no port: the allow-list checker rejects the URL as malformed before any fetch
    SolrException e =
        assertThrows(
            SolrException.class,
            () ->
                new IndexFetcher(
                    initArgsWithLeaderUrl("http://solr:secretpw@localhost/solr/collection1"),
                    null,
                    coreWithRealAllowListChecker()));
    assertEquals(SolrException.ErrorCode.SERVER_ERROR.code, e.code());
    assertFalse(e.getMessage(), e.getMessage().contains("secretpw"));
    assertTrue(e.getMessage(), e.getMessage().contains("solr:********@localhost"));
    assertNotNull(e.getCause());
    assertFalse(e.getCause().getMessage(), e.getCause().getMessage().contains("secretpw"));
  }

  @Test
  public void testForbiddenLeaderUrlErrorRedactsPassword() throws Exception {
    // well-formed, but the empty allow-list admits nothing
    SolrException e =
        assertThrows(
            SolrException.class,
            () ->
                new IndexFetcher(
                    initArgsWithLeaderUrl("http://solr:secretpw@127.0.0.1:8983/solr/collection1"),
                    null,
                    coreWithRealAllowListChecker()));
    assertEquals(SolrException.ErrorCode.FORBIDDEN.code, e.code());
    assertFalse(e.getMessage(), e.getMessage().contains("secretpw"));
    assertTrue(e.getMessage(), e.getMessage().contains("solr:********@127.0.0.1"));
  }
}
