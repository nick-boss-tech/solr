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
package org.apache.solr.response;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.IndexSchema;
import org.junit.BeforeClass;

/** SOLR-11153: a schema without a "name" attribute must not make wt=schema.xml fail with an NPE. */
public class SchemaXmlWriterMissingNameTest extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml", "schema.xml");
  }

  public void testSchemaWithoutNameOrVersion() throws Exception {
    // what IndexSchema.getNamedPropertyValues() yields for a schema lacking both attributes
    Map<String, Object> schemaProperties = new LinkedHashMap<>();
    schemaProperties.put(IndexSchema.UNIQUE_KEY, "id");

    try (SolrQueryRequest req = req()) {
      SolrQueryResponse rsp = new SolrQueryResponse();
      rsp.add(IndexSchema.SCHEMA, schemaProperties);

      StringWriter out = new StringWriter();
      SchemaXmlWriter.writeResponse(out, req, rsp);

      String xml = out.toString();
      assertTrue(xml, xml.contains("<schema"));
      assertFalse(xml, xml.contains("name="));
      assertTrue(xml, xml.contains("<uniqueKey>id</uniqueKey>"));
    }
  }
}