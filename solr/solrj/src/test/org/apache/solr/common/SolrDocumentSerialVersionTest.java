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
package org.apache.solr.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.apache.lucene.util.SuppressForbidden;
import org.apache.solr.SolrTestCase;

/** Document classes pin their serialVersionUID so Java serialization survives recompiles. */
public class SolrDocumentSerialVersionTest extends SolrTestCase {

  public void testExplicitSerialVersionUid() throws Exception {
    for (Class<?> clazz :
        new Class<?>[] {
          SolrDocumentBase.class, SolrDocument.class, SolrInputDocument.class, SolrInputField.class
        }) {
      Field f = clazz.getDeclaredField("serialVersionUID");
      assertTrue(clazz.getName(), Modifier.isPrivate(f.getModifiers()));
      assertTrue(clazz.getName(), Modifier.isStatic(f.getModifiers()));
      assertTrue(clazz.getName(), Modifier.isFinal(f.getModifiers()));
      assertEquals(clazz.getName(), long.class, f.getType());
      // The UID the serialization runtime will use for this class must be the declared one.
      assertEquals(clazz.getName(), 1L, ObjectStreamClass.lookup(clazz).getSerialVersionUID());
    }
  }

  @SuppressForbidden(
      reason = "testing a same-version Java serialization round-trip on locally generated data")
  public void testSolrInputDocumentRoundTrip() throws Exception {
    SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", "1");
    doc.addField("tags", "a");
    doc.addField("tags", "b");

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
      out.writeObject(doc);
    }
    try (ObjectInputStream in =
        new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
      SolrInputDocument copy = (SolrInputDocument) in.readObject();
      assertEquals("1", copy.getFieldValue("id"));
      assertEquals(2, copy.getFieldValues("tags").size());
    }
  }
}
