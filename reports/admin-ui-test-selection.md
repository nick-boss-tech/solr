# Report: Admin UI Selenium test selection (the SOLR-9831 premise run selects no tests)

Verdict: **the most likely cause is a runner-level drop of the whole `@SeleniumTest` class family before JUnit reports any event.** Confidence is low: about 30% for the inherited Selenium group gate, and about 60% for some family-wide runner-level drop that this repository cannot show. No test was run on this host. The run spec below decides it in one step on a Linux host.

Refs read: the SOLR-9831 head `f269a70e84f` (the object exists locally; no fetch was needed) and the merge-base `cabedd1d968059215188f4e7563fb303241899ed`.

## Ruled out, with evidence

- **The test body.** The head adds ten lines to `AdminUiLoggingScreenTest.java` (an `assertEquals` block after the `waitUntil`, around line 75) and `logging.html`. Both refs have the same test-class set. The imports are JUnit 4. The class is public. The `@Test` methods are at lines 35 and 43. There is no `@Ignore` and no class-level `Assume`. `AdminUiTestBase.java` and `AdminUiStandaloneTestBase.java` are unchanged between the two refs.
- **A class-level skip in the chain.** `SolrTestCase.java` lines 190 to 194 and 210 to 213 assume only when `tests.force.assumption.failure.*` is set, which is off by default. `SolrCloudTestCase` has only an `@AfterClass`. `SolrTestCaseJ4.setupTestCases` (line 236) has no assume.
- **Include and exclude rules.** None applies to `Test` tasks in `gradle/testing`, `gradle/solr`, `build.gradle` or `settings.gradle`. The only setting is `failOnNoMatchingTests=false` (`fail-on-no-tests.gradle` lines 23 to 27). The guard throw at line 56 fires only when the test count is zero.
- **The method-level assume** at `AdminUiLoggingScreenTest.java` lines 63 to 65. A skip would produce a skipped XML entry and a non-zero test count. Neither exists. `dev-docs/admin-ui-tests.md` documents this skip as intended for shared JVMs, so the premise run must check that `skipped=0`.
- **A `@BeforeClass` failure** (`AdminUiTestBase.java` lines 182 to 184). It would appear as a class-method failure in the XML. No XML exists.
- **Classes dropped at compile time.** There are 19 `*Test.java` files at the head, matching the 19 compiled classes.
- **Another Admin UI class on the merge-base.** `AdminUiSmokeTest.java` exists on both refs, extends `AdminUiTestBase` directly, and has five `@Test` methods. All 19 concrete classes inherit the same gate.

## Ranked mechanisms

1. **The runner-level Selenium gate, the specific suspect (about 30%).** `@SeleniumTest` is `@Inherited` and carries `@TestGroup(enabled=false, sysProperty="tests.selenium")` (`SeleniumTest.java` lines 31 to 36), applied at `AdminUiTestBase.java` line 99. The repository configuration looks correct: `randomization.gradle` line 135 defaults to false, lines 145 and 146 resolve the property (`globals.gradle` line 44), and line 185 forwards it to the forked JVM. This stays alive only if the forked JVM gets a value other than `true` (from `TEST_JVM_ARGS` or `tests.jvmargs` on the host, which were not inspected), or if randomizedtesting 2.9.1 treats inherited groups differently. Only the binary jar is cached, so its source could not be read.
2. **A runner or Gradle-internal drop not visible in this repository (about 35%).** It would show as zero events for every Admin UI class in run (a).
3. **Method selection mismatch between `--tests` and the randomized runner's test description (about 10%).** Unlikely: `failed-tests-at-end.gradle` line 33 and `dev-docs/gradle-help/tests.txt` both use the `Class.method` form. Checked by runs (b2) and (b3).
4. **A class-specific detection or filter (ruled out in practice).** The class differs from `AdminUiSmokeTest` only in its method bodies, and both share the base class.

## Confirming run spec

For a Linux host with JDK 21 and Chrome. Set `JAVA_HOME` and `CHROME_BIN` if needed, change nothing else, and read the XML under `solr/webapp/build/test-results/test/`. Nothing is run on the Windows host unless Nick types the commands himself.

- **(a) Merge-base control, must execute.** Check out `cabedd1d968` and run `./gradlew :solr:webapp:cleanTest :solr:webapp:test --tests "org.apache.solr.webapp.AdminUiSmokeTest" -Ptests.selenium=true -Ptests.seed=9831B0A5E0000001`. Expect `TEST-org.apache.solr.webapp.AdminUiSmokeTest.xml` with `tests=5` and `failures=0`. Its `<properties>` should show `tests.selenium=true`, which rules out mechanism 1 at the JVM. If there is no XML and the guard fires, the cause is family-wide: go to (c) on the head and read the result there.
- **(b) Head, the premise command.** Check out `f269a70e84f` and run the same command with `--tests "org.apache.solr.webapp.AdminUiLoggingScreenTest.testEventsViewerShowsWarnings"`. An XML with the testcase and no `skipped` or `failed` means the premise is resolved, and the earlier host was at fault. An XML with `skipped="1"` means the assume at line 63 fired, so SOLR-9831 is not proven. Zero XML plus the guard reproduces the failure; if (a) also gave zero, the cause is family-wide.
- **(b2) Head, class-level filter.** `--tests "org.apache.solr.webapp.AdminUiLoggingScreenTest"`. Two XML test entries where (b) gave zero confirms mechanism 3.
- **(b3) Control on the merge-base, method filter.** `--tests "org.apache.solr.webapp.AdminUiSmokeTest.testNodeLevelScreens"`. Zero results here, while (a) gives five, confirms that method selection is broken (mechanism 3).
- **(c) Head with `--info --debug`.** Run (b) again into `run-c.log`, then grep for `Will use test opts` (`randomization.gradle` line 242, which shows the resolved `tests.selenium` at debug), `Test folders for :solr:webapp`, `Gradle Test Executor`, `NoClassDefFoundError`, `ClassNotFoundException`, `initializationError`, `classMethod`, `No tests found` and `skipped`. A resolved `tests.selenium=true` rules out mechanism 1 on the Gradle side. Executor class-loading errors point to mechanism 2. Nothing plus zero XML points inside the runner, which needs the randomizedtesting source to settle.
- **(d) Read-only, for Nick.** The GitHub Actions workflow `admin-ui-test.yml` runs `./gradlew :solr:webapp:test -Ptests.selenium=true` with no `--tests` filter (line 39) on `ubuntu-latest`. If its run on head `f269a70e84f` executed `LoggingScreenTest`, the difference is the `--tests` filter or the Windows host. If it also shows zero, the cause is the family gate.
- **(e) No tests run.** `./gradlew :solr:webapp:testOpts -Ptests.selenium=true` prints the resolved `tests.selenium` (`randomization.gradle` line 254).

## Fix sketch (text only, nothing applied)

- **If (b) shows a skip:** the assume at `AdminUiLoggingScreenTest.java` lines 63 to 65 can hide the SOLR-9831 assertion. For the premise, turn the watcher precondition into a failing assertion, or move the check into a test that fails on a skip, so a skip cannot count as proof. The trade-off is that `dev-docs/admin-ui-tests.md` documents this skip for shared-JVM runs.
- **If the family gate is the cause and (c) shows `tests.selenium` missing from the JVM:** add an explicit `systemProperty 'tests.selenium'` forward in `solr/webapp/build.gradle` (`tasks.withType(Test).configureEach`, next to the `seleniumEnabled` logic around line 94). That is build configuration outside the test class, so it is flagged for review rather than proposed as a branch change.
- **If (b2) passes and (b) gives zero:** no code change. The premise run should use the class-level filter and check the method's XML entry.
- The SOLR-9831 assertion itself needs no change until a run actually executes it.

## Not verified

- The randomizedtesting 2.9.1 `TestGroup` and `Description` internals (binary jar only).
- Gradle semantics: whether `testCount` counts skipped tests, and whether a skipped-only class always gets an XML. Both are assumed standard; run (b) settles them.
- The host environment: `TEST_JVM_ARGS`, `tests.jvmargs` and `~/.gradle/gradle.properties` were not inspected.
- The reviewing host's logs were not available to the investigator, and nothing was built or run.
- The head's current CI status was not queried.
- The LuceneTestCase base hooks (in the Lucene jar, not in this repository).
