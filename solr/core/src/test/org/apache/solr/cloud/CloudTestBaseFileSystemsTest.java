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
package org.apache.solr.cloud;

import java.util.Arrays;
import java.util.List;
import org.apache.lucene.tests.util.LuceneTestCase.SuppressFileSystems;
import org.apache.solr.BaseDistributedSearchTestCase;
import org.apache.solr.SolrTestCase;
import org.apache.solr.SolrTestCaseJ4;

/**
 * The mock {@code HandleLimitFS} allows a fixed number of open files for the whole JVM, which a
 * multi-node test exceeds regardless of how many nodes it starts. The base classes of such tests
 * must keep suppressing it, and must not drop the {@code ExtrasFS} suppression of {@link
 * SolrTestCaseJ4} when they declare their own annotation.
 */
public class CloudTestBaseFileSystemsTest extends SolrTestCase {

  public void testMultiNodeBaseClassesSuppressHandleLimitFs() {
    for (Class<?> base :
        List.of(
            SolrCloudTestCase.class,
            BaseDistributedSearchTestCase.class,
            AbstractFullDistribZkTestBase.class)) {
      SuppressFileSystems suppressed = base.getAnnotation(SuppressFileSystems.class);
      assertNotNull(base.getSimpleName(), suppressed);
      List<String> names = Arrays.asList(suppressed.value());
      assertTrue(base.getSimpleName() + " " + names, names.contains("HandleLimitFS"));
      assertTrue(base.getSimpleName() + " " + names, names.contains("ExtrasFS"));
    }
  }
}
