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
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrException;
import org.apache.solr.core.XmlConfigFile;
import org.apache.solr.util.LogListener;
import org.junit.BeforeClass;

/**
 * Tests the warn/fail rule for {@link CollationField} when a schema explicitly sets
 * useDocValuesAsStored="true": a warning below Lucene 9.0, a refusal from 9.0 on, in the style of
 * TestICUCollationFieldUDVAS. Two differences from that test: the core field takes a language
 * attribute (not locale), and the schema is written by this test because the change adds no test
 * resource files.
 */
public class TestCollationFieldUDVAS extends SolrTestCaseJ4 {

  private static final String FIELD_UDVAS_PROPNAME = "tests.collation_field.udvas";
  private static final String TYPE_UDVAS_PROPNAME = "tests.collation_fieldType.udvas";
  private static final String MATCH_VERSION_PROPNAME = "tests.luceneMatchVersion";
  private static final String FAIL_VERSION = "9.0.0";
  private static final String WARN_VERSION = "8.12.0";

  private static final String SCHEMA_XML =
      """
      <?xml version="1.0" ?>
      <schema name="test" version="1.7">
        <fieldType name="string" class="solr.StrField" omitNorms="true" positionIncrementGap="0"/>
        <fieldType name="sort_de_t" class="solr.CollationField" language="de" strength="primary"
                   useDocValuesAsStored="${tests.collation_fieldType.udvas:false}"/>
        <field name="id" type="string" indexed="true" stored="true" required="true"/>
        <field name="sort_de" type="sort_de_t" indexed="false" stored="false" multiValued="false"
               useDocValuesAsStored="${tests.collation_field.udvas:false}"/>
        <uniqueKey>id</uniqueKey>
      </schema>
      """;

  private static String home;

  @BeforeClass
  public static void beforeClass() throws Exception {
    home = setupSolrHome();
  }

  public static String setupSolrHome() throws Exception {
    Path tmpFile = createTempDir("collation-udvas");
    Files.createDirectories(tmpFile.resolve("data"));
    Path confDir = tmpFile.resolve("collection1").resolve("conf");
    Files.createDirectories(confDir);
    Files.copy(
        getFile("solr/collection1/conf/solrconfig-basic.xml"), confDir.resolve("solrconfig.xml"));
    Files.copy(
        getFile("solr/collection1/conf/solrconfig.snippet.randomindexconfig.xml"),
        confDir.resolve("solrconfig.snippet.randomindexconfig.xml"));
    Files.writeString(confDir.resolve("schema.xml"), SCHEMA_XML, StandardCharsets.UTF_8);
    return tmpFile.toAbsolutePath().toString();
  }

  public void testNoUdvasNoWarning() throws Exception {
    try (LogListener warnLog = udvasWarnLog()) {
      initWith(FAIL_VERSION, false, false);
      assertEquals("unexpected warning message was logged", 0, warnLog.getCount());
    } finally {
      cleanup();
    }
  }

  public void testFieldUdvasFailsFrom90() throws Exception {
    try {
      initWith(FAIL_VERSION, true, false);
      fail("expected schema load to fail for a field level flag at " + FAIL_VERSION);
    } catch (SolrException ex) {
      assertUdvasRefusal(ex);
    } finally {
      cleanup();
    }
  }

  public void testTypeUdvasFailsFrom90() throws Exception {
    try {
      initWith(FAIL_VERSION, false, true);
      fail("expected schema load to fail for a field type level flag at " + FAIL_VERSION);
    } catch (SolrException ex) {
      assertUdvasRefusal(ex);
    } finally {
      cleanup();
    }
  }

  public void testFieldUdvasWarnsBelow90() throws Exception {
    try (LogListener warnLog = udvasWarnLog()) {
      initWith(WARN_VERSION, true, false);
      assertTrue("expected warning message was not logged", warnLog.getCount() > 0);
      while (warnLog.pollMessage() != null) {}
    } finally {
      cleanup();
    }
  }

  public void testTypeUdvasWarnsBelow90() throws Exception {
    try (LogListener warnLog = udvasWarnLog()) {
      initWith(WARN_VERSION, false, true);
      assertTrue("expected warning message was not logged", warnLog.getCount() > 0);
      while (warnLog.pollMessage() != null) {}
    } finally {
      cleanup();
    }
  }

  private static LogListener udvasWarnLog() {
    return LogListener.warn(XmlConfigFile.class).substring(CollationField.UDVAS_MESSAGE);
  }

  private static void assertUdvasRefusal(SolrException ex) {
    assertTrue(
        "unexpected failure message: " + ex,
        getRootCause(ex).getMessage().contains(CollationField.UDVAS_MESSAGE));
  }

  private static void initWith(String matchVersion, boolean fieldUdvas, boolean typeUdvas)
      throws Exception {
    System.setProperty(MATCH_VERSION_PROPNAME, matchVersion);
    if (fieldUdvas) {
      System.setProperty(FIELD_UDVAS_PROPNAME, "true");
    } else {
      System.clearProperty(FIELD_UDVAS_PROPNAME);
    }
    if (typeUdvas) {
      System.setProperty(TYPE_UDVAS_PROPNAME, "true");
    } else {
      System.clearProperty(TYPE_UDVAS_PROPNAME);
    }
    initCore("solrconfig.xml", "schema.xml", Path.of(home));
  }

  private static void cleanup() {
    deleteCore();
    System.clearProperty(MATCH_VERSION_PROPNAME);
    System.clearProperty(FIELD_UDVAS_PROPNAME);
    System.clearProperty(TYPE_UDVAS_PROPNAME);
  }
}
