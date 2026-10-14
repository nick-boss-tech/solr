# Fix for the SOLR-15712 version gate: corrected TestCollationFieldUDVAS

Job: `gates/SOLR-15712-udvas-version-gate.md`. The gate FAILED at `2d65ed6329`
(vm2 gate worktree, not pushed) with 4 of 5 tests in the new
`TestCollationFieldUDVAS` dying at field type initialization. The main-side
investigation recorded in that job file classifies the failure as (b): the new
test's generated schema does not meet core `CollationField`'s init contract, so
the loads die in `setup(...)` before the version rule can act. The production
implementation is not implicated.

## What to do (patch loop, on vm2 where the implementation commit lives)

1. In the gate worktree (`/workspace/gates/solr-15712-gate`, head `2d65ed6329`),
   replace `solr/core/src/test/org/apache/solr/schema/TestCollationFieldUDVAS.java`
   in full with the class below. The production file and the changelog title from
   the failed run stay exactly as they are. Commit the replacement as one new
   commit on top of `2d65ed6329`.
2. Re-run the job's steps at the resulting head: changelog YAML parse, tidy
   (`:solr:core:tidy`, tree clean afterward), Error Prone compile, the focused
   run (all three classes named in the job), the pre-fix proof for the new test,
   and the module check.
3. On green, follow the job's On green steps: push plainly to
   `solr-15712-submit`, refresh `receipts/SOLR-15712.md` at the new head, mark the
   job DONE. On any failure, record it in the job file and stop; do not relaunch
   silently.

What changed against the failed test, and why:

- The schema is written by the test itself (the job allows one new test class and
  no new resource files), and its collation field type uses the core field's own
  attributes: `language="de"`. Core `CollationField.setup` requires `language` or
  `custom` and throws "Either custom or language is required." otherwise; the ICU
  field's `locale` attribute does not satisfy it. This is the defect that killed
  the four loads at field type init, including the no-flag control.
- The match version is driven through the system property
  `tests.luceneMatchVersion`, which the copied `solrconfig-basic.xml` resolves in
  its `luceneMatchVersion` element; that is the value
  `CollationField.init` reads through `schema.getDefaultLuceneMatchVersion()`.
- The flag is injected with schema placeholders in the ICU test's shape, one for
  the field type and one for the field, each defaulting to `false`.
- Both fail cases assert that the root cause message contains
  `CollationField.UDVAS_MESSAGE`, so a schema load that fails for any other
  reason can no longer pass a fail case the way `testTypeUdvasFailsFrom90` may
  have in the failed run.
- The five test names are unchanged, so the re-gate maps one to one onto the
  failed run.

## Replacement test class

```java
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
 * useDocValuesAsStored="true": a warning below Lucene 9.0, a refusal from 9.0 on, in the
 * style of TestICUCollationFieldUDVAS. Two differences from that test: the core field
 * takes a language attribute (not locale), and the schema is written by this test
 * because the change adds no test resource files.
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
    } finally {
      cleanup();
    }
  }

  public void testTypeUdvasWarnsBelow90() throws Exception {
    try (LogListener warnLog = udvasWarnLog()) {
      initWith(WARN_VERSION, false, true);
      assertTrue("expected warning message was not logged", warnLog.getCount() > 0);
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
```
