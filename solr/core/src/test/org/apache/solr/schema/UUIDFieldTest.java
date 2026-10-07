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
package org.apache.solr.schema;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrException;

public class UUIDFieldTest extends SolrTestCaseJ4 {
  public void testToInternal() {
    boolean ok = false;
    UUIDField uuidfield = new UUIDField();

    try {
      uuidfield.toInternal((String) null);
      ok = true;
    } catch (SolrException se) {
      ok = false;
    }
    assertTrue("ID generation from null failed", ok);

    try {
      uuidfield.toInternal("");
      ok = true;
    } catch (SolrException se) {
      ok = false;
    }
    assertTrue("ID generation from empty string failed", ok);

    try {
      uuidfield.toInternal("NEW");
      ok = true;
    } catch (SolrException se) {
      ok = false;
    }
    assertTrue("ID generation from 'NEW' failed", ok);

    try {
      uuidfield.toInternal("d574fb6a-5f79-4974-b01a-fcd598a19ef5");
      ok = true;
    } catch (SolrException se) {
      ok = false;
    }
    assertTrue("ID generation from UUID failed", ok);

    try {
      uuidfield.toInternal("This is a test");
      ok = false;
    } catch (SolrException se) {
      ok = true;
    }
    assertTrue("Bad UUID check failed", ok);
  }

  public void testNonHexCharactersAreRejected() {
    UUIDField uuidfield = new UUIDField();
    for (String bad :
        new String[] {
          "zzzzzzzz-zzzz-zzzz-zzzz-zzzzzzzzzzzz",
          "d574fb6a-5f79-4974-b01a-fcd598a19eg5",
          "d574fb6a-5f79-4974-b01a-fcd598a19e f",
          "４574fb6a-5f79-4974-b01a-fcd598a19ef5"
        }) {
      SolrException e = expectThrows(SolrException.class, () -> uuidfield.toInternal(bad));
      assertEquals(bad, SolrException.ErrorCode.BAD_REQUEST.code, e.code());
    }
    assertEquals(
        "d574fb6a-5f79-4974-b01a-fcd598a19ef5",
        uuidfield.toInternal("D574FB6A-5F79-4974-B01A-FCD598A19EF5"));
  }

  public void testBadRequest() {
    try {
      new UUIDField()
          .createField(
              new SchemaField(
                  "test",
                  new StrField() {
                    {
                      properties = (STORED | INDEXED);
                    }
                  }),
              "bad request");
    } catch (SolrException s) {
      assertEquals(SolrException.ErrorCode.BAD_REQUEST.code, s.code());
    }
  }
}
