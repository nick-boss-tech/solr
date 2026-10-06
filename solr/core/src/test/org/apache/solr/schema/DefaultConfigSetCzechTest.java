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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.solr.SolrTestCase;
import org.apache.solr.util.ExternalPaths;
import org.w3c.dom.Document;

/** The {@code _default} configset offers Czech under its language code "cs" as well as "cz". */
public class DefaultConfigSetCzechTest extends SolrTestCase {

  public void testCzechLanguageCodeAlias() throws Exception {
    Path conf = ExternalPaths.DEFAULT_CONFIGSET.resolve("conf");
    Document schema =
        DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(conf.resolve("managed-schema.xml").toFile());
    XPath xpath = XPathFactory.newInstance().newXPath();

    assertEquals(
        "text_cs",
        xpath.evaluate("/schema/dynamicField[@name='*_txt_cs']/@type", schema));
    assertEquals(
        "lang/stopwords_cs.txt",
        xpath.evaluate("/schema/fieldType[@name='text_cs']//filter[@name='stop']/@words", schema));
    assertTrue(
        "text_cz must stay for existing indexes",
        (Boolean)
            xpath.evaluate("boolean(/schema/fieldType[@name='text_cz'])", schema, XPathConstants.BOOLEAN));

    assertEquals(
        "the cs stopword list is the cz one",
        Files.readString(conf.resolve("lang/stopwords_cz.txt"), StandardCharsets.UTF_8)
            .replace("\r\n", "\n"),
        Files.readString(conf.resolve("lang/stopwords_cs.txt"), StandardCharsets.UTF_8)
            .replace("\r\n", "\n"));
  }
}
