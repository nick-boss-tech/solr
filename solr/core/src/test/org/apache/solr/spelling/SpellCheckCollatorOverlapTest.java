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
package org.apache.solr.spelling;

import java.util.List;
import org.apache.solr.SolrTestCase;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.response.SolrQueryResponse;
import org.junit.Test;

/**
 * Collation must not fail when the corrections in one possibility are not in strictly increasing,
 * non-overlapping offset order. Synonym expansion can produce exactly that: several corrections
 * sharing one original span, or corrections listed out of offset order (SOLR-13360).
 */
public class SpellCheckCollatorOverlapTest extends SolrTestCase {

  private static String collate(String originalQuery, SpellingResult result) {
    SpellCheckCollator collator = new SpellCheckCollator();
    collator.setMaxCollations(1);
    collator.setMaxCollationTries(0); // no query verification; collation string only
    List<SpellCheckCollation> collations =
        collator.collate(
            result, originalQuery, new ResponseBuilder(null, new SolrQueryResponse(), null));
    assertEquals(1, collations.size());
    return collations.get(0).getCollationQuery();
  }

  @Test
  public void testInOrderCorrectionsUnchanged() {
    SpellingResult result = new SpellingResult();
    result.add(new SpellCheckToken("cats", 0, 4), "cat", 1);
    result.add(new SpellCheckToken("persians", 5, 13), "persian", 1);
    result.add(new SpellCheckToken("tabby", 14, 19), "tabbi", 1);
    assertEquals("cat persian tabbi", collate("cats persians tabby", result));
  }

  @Test
  public void testOutOfOrderCorrections() {
    // The later-offset correction is listed first; the collation must still apply both.
    SpellingResult result = new SpellingResult();
    result.add(new SpellCheckToken("efghijkl", 5, 13), "EFGH", 1);
    result.add(new SpellCheckToken("abcd", 0, 4), "wxyz", 1);
    assertEquals("wxyz EFGHm", collate("abcd efghijklm", result));
  }

  @Test
  public void testOverlappingCorrectionsFirstWins() {
    // A synonym expansion covering (0,12) plus a correction for the (9,12) span inside it.
    // The inner correction must be skipped, not applied against already-replaced text.
    SpellingResult result = new SpellingResult();
    result.add(new SpellCheckToken("abcdefghijkl", 0, 12), "XY", 1);
    result.add(new SpellCheckToken("jkl", 9, 12), "ZZZ", 1);
    assertEquals("XY", collate("abcdefghijkl", result));
  }

  @Test
  public void testRepeatedPositionCorrectionsFirstWins() {
    // Two distinct synonym-expanded tokens sharing the original (0,6) span: equal offsets but
    // different records, as synonym expansion produces. (Identical records would merge into one
    // map entry and never collide.)
    SpellingResult result = new SpellingResult();
    result.add(new SpellCheckToken("abcdef", 0, 6), "wxyz", 1);
    result.add(new SpellCheckToken("abcdef", 0, 6, "synonym", 1, 0, null), "pq", 1);
    assertEquals("wxyz", collate("abcdef", result));
  }
}
