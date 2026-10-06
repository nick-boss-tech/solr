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
package org.apache.solr.handler.component;

import java.time.Instant;
import java.util.Date;
import org.apache.solr.SolrTestCase;

/** SOLR-8939: unique keys sent back to shards must keep their full precision. */
public class QueryComponentIdFormatTest extends SolrTestCase {

  public void testDateKeyKeepsMilliseconds() {
    Date id = Date.from(Instant.parse("2016-03-21T10:15:30.123Z"));
    assertEquals("2016-03-21T10:15:30.123Z", QueryComponent.idToString(id));
  }

  public void testOtherKeysUseToString() {
    assertEquals("abc", QueryComponent.idToString("abc"));
    assertEquals("42", QueryComponent.idToString(42));
    assertEquals("9876543210", QueryComponent.idToString(9876543210L));
  }
}
