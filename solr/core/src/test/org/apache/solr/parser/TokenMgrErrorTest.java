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
package org.apache.solr.parser;

import org.apache.solr.SolrTestCaseJ4;
import org.junit.Test;

public class TokenMgrErrorTest extends SolrTestCaseJ4 {

  @Test
  public void testMessageConstructorKeepsMessageAndReason() {
    TokenMgrError error = new TokenMgrError("boom", TokenMgrError.LOOP_DETECTED);
    assertEquals("boom", error.getMessage());
    assertEquals(TokenMgrError.LOOP_DETECTED, error.errorCode);
  }

  @Test
  public void testLexicalErrorMessageWithCharacterAndPrefix() {
    TokenMgrError error = new TokenMgrError(false, 0, 1, 7, "ab", 'x', TokenMgrError.LEXICAL_ERROR);
    assertEquals(
        "Lexical error at line 1, column 7.  Encountered: '120' (120), after prefix \"ab\"",
        error.getMessage());
    assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
  }

  @Test
  public void testLexicalErrorAtEofReportsLexicalState() {
    TokenMgrError error = new TokenMgrError(true, 2, 3, 4, "", 0, TokenMgrError.LEXICAL_ERROR);
    assertEquals(
        "Lexical error at line 3, column 4.  Encountered: <EOF> (in lexical state 2)",
        error.getMessage());
  }

  @Test
  public void testAddEscapesUsesUnicodeEscapeForUnprintableCharacters() {
    assertEquals("\\t\\\"\\\\", TokenMgrError.addEscapes("\t\"\\"));
    assertEquals("\\u0001", TokenMgrError.addEscapes("\u0001"));
  }

  @Test
  public void testReasonConstantsAreDistinct() {
    assertEquals(0, TokenMgrError.LEXICAL_ERROR);
    assertEquals(1, TokenMgrError.STATIC_LEXER_ERROR);
    assertEquals(2, TokenMgrError.INVALID_LEXICAL_STATE);
    assertEquals(3, TokenMgrError.LOOP_DETECTED);
  }
}
