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

package org.apache.solr.client.solrj.io.stream.metrics;

import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.io.Tuple;
import org.junit.Test;

/** Tests the string comparison used by {@link MinMetric} and {@link MaxMetric}. */
public class MinMaxMetricTest extends SolrTestCase {

  @Test
  public void testMinMaxOnInstantsWithMixedFractionDigits() {
    // The whole-second value is the earlier time, but as text it sorts after the .250 value
    // ('Z' > '.'), and the .250 value sorts before the .5 value as text as well. Only an
    // instant comparison orders all three by time.
    String wholeSecond = "2018-03-01T10:00:00Z";
    String quarterSecond = "2018-03-01T10:00:00.250Z";
    String halfSecond = "2018-03-01T10:00:00.5Z";
    assertEquals(wholeSecond, minOf(quarterSecond, wholeSecond));
    assertEquals(wholeSecond, minOf(wholeSecond, quarterSecond));
    assertEquals(wholeSecond, minOf(halfSecond, quarterSecond, wholeSecond));
    assertEquals(halfSecond, maxOf(wholeSecond, halfSecond));
    assertEquals(halfSecond, maxOf(halfSecond, wholeSecond));
    assertEquals(halfSecond, maxOf(wholeSecond, quarterSecond, halfSecond));
  }

  @Test
  public void testMinMaxOnPlainStringsUsesTextOrder() {
    assertEquals("apple", minOf("banana", "apple"));
    assertEquals("banana", maxOf("apple", "banana"));
  }

  @Test
  public void testMinMaxOnMixedInstantAndTextUsesTextOrder() {
    // Only one of the two values parses as an instant, so the pair falls back to text order:
    // "2017-not-a-date" sorts before "2018-03-01T10:00:00Z" as text.
    String instant = "2018-03-01T10:00:00Z";
    String text = "2017-not-a-date";
    assertEquals(text, minOf(instant, text));
    assertEquals(instant, maxOf(instant, text));
  }

  private Object minOf(String... values) {
    MinMetric min = new MinMetric("field");
    for (String value : values) {
      Tuple tuple = new Tuple();
      tuple.put("field", value);
      min.update(tuple);
    }
    return min.getValue();
  }

  private Object maxOf(String... values) {
    MaxMetric max = new MaxMetric("field");
    for (String value : values) {
      Tuple tuple = new Tuple();
      tuple.put("field", value);
      max.update(tuple);
    }
    return max.getValue();
  }
}
