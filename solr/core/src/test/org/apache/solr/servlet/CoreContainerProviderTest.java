/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.servlet;

import static org.apache.solr.SolrTestCaseJ4.assumeWorkingMockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.UnavailableException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.apache.solr.SolrTestCase;
import org.junit.BeforeClass;
import org.junit.Test;

public class CoreContainerProviderTest extends SolrTestCase {

  @BeforeClass
  public static void ensureAssumptions() {
    assumeWorkingMockito();
  }

  @Test
  public void testStartupFailureFailsContextInitialization() throws Exception {
    Path solrHome = createTempDir("solrhome");
    Files.writeString(solrHome.resolve("solr.xml"), "<solr><this is not valid xml");

    ServletContext servletContext = mock(ServletContext.class);
    when(servletContext.getAttribute(CoreContainerProvider.SOLR_PROPERTIES))
        .thenReturn(new Properties());
    when(servletContext.getAttribute(CoreContainerProvider.SOLR_SOLR_HOME))
        .thenReturn(solrHome.toString());

    CoreContainerProvider provider = new CoreContainerProvider();
    expectThrows(
        RuntimeException.class,
        () -> provider.contextInitialized(new ServletContextEvent(servletContext)));

    verify(servletContext, never()).setAttribute(eq(CoreContainerProvider.class.getName()), any());
    expectThrows(UnavailableException.class, provider::getCoreContainer);
  }
}
