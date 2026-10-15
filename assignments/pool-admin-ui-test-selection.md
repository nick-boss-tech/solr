# Pool assignment: Admin UI Selenium test selection (the SOLR-9831 premise run selects no tests)

Status: DONE

Capability tags: `review` for the investigation (any review host); `selenium` for the confirming runs (a Linux host with Chrome and Gradle, or Nick typing the commands himself, as the premise runs were set up). Staffing: 1. Claim path: claims/pool-admin-ui-test-selection.md.

Background: the Admin UI premise runs (claims/pool-admin-ui-premise-runs.md) need `AdminUiLoggingScreenTest#testEventsViewerShowsWarnings` to execute on the SOLR-9831 head and on the merge-base with the head's tests overlaid. The first run on the Windows host did not execute it. Two attempts with the same command, after the wrapper was fixed, produced the same result. The command was:

`gradlew.bat :solr:webapp:cleanTest :solr:webapp:test --tests "org.apache.solr.webapp.AdminUiLoggingScreenTest.testEventsViewerShowsWarnings" -Ptests.selenium=true -Ptests.seed=9831B0A5E0000001`

Evidence (the logs are in the reviewing host's scratchpad, not on the branch; the lines are quoted here):

- Compilation succeeds for `:solr:webapp:compileTestJava`. The compiled class exists at `solr/webapp/build/classes/java/test/org/apache/solr/webapp/AdminUiLoggingScreenTest.class`. Nineteen compiled `*Test.class` files are under `solr/webapp/build`.
- `:solr:webapp:test` runs and reports `Test folders for :solr:webapp` and then `Finished generating test XML results (0.0 secs)`. No test class is listed.
- No JUnit XML for `AdminUiLoggingScreenTest` is written under `solr/webapp/build/test-results/test/`. The directory holds only a `binary` folder.
- The build then fails at `gradle/testing/fail-on-no-tests.gradle` line 56: `No tests found for the given filters?`. That guard counts `result.testCount` in `afterSuite`, so zero means no test was executed in the webapp test task.
- `solr/webapp/build.gradle` has no include or exclude rules. Line 83 sets `testClassesDirs = sourceSets.test.output.classesDirs`. Lines 94 to 108 gate the Selenium setup on `tests.selenium` and `gradle.ext.withJsClient`.
- The target method is at `AdminUiLoggingScreenTest.java` line 44 (annotated at line 43). It calls `ensureCloudCluster()` first, then an `Assume.assumeTrue` on the log watcher (around line 63). A failed assumption is a skip, not a silent non-run, so the skip is not the whole explanation: the class produced no XML at all.
- Nothing in the `--info` log selects or filters the class: the `--tests` filter is not echoed, and the test task's configuration is not printed.

What is not known: whether Gradle's test detection reads the class at all (a test-discovery problem), whether the `--tests` pattern is parsed as intended under this wrapper, whether a base-class `@BeforeClass` failure is swallowed before JUnit records it, or whether this happens on the merge-base, on other Admin UI test classes, or only on this head.

Work, in order:

1. Read `solr/webapp/src/test/org/apache/solr/webapp/AdminUiLoggingScreenTest.java`, `AdminUiTestBase.java` and `AdminUiStandaloneTestBase.java` at the SOLR-9831 head (`f269a70e84f`, branch `solr-9831-submit`). Check the imports (which `Test` annotation is used), the class and method visibility, and the `@BeforeClass` and `@AfterClass` hooks. Check whether any class in the chain is annotated to be ignored, or to run only with a property.
2. Read `gradle/testing/` at the same head for any test-selection rule that could drop a class: `fail-on-no-tests.gradle`, the randomization and beast configuration, and any `filter` or `exclude` that applies to `Test` tasks.
3. Check whether the same filter selects a different Admin UI class that exists on main, for example `AdminUiSmokeTest`, which is on the merge-base. Whether it executes or not, tells whether the problem is the class or the setup.
4. Rank the mechanisms with code evidence at file and line, at the SHA you read. Rule each one in or out.
5. Write the confirming run spec, to be run on a Linux host with Chrome, or by Nick: (a) `--tests "org.apache.solr.webapp.AdminUiSmokeTest"` on the merge-base, expected to execute; (b) the same method filter on the head; (c) the method filter with `--info` and `--debug` to show the test-detection output. For each, state the outcome that would confirm or rule out each mechanism. Do not run anything on the Windows host unless Nick types the commands himself.
6. If a root cause is found, sketch the fix as text. Do not apply it to any branch.

Deliverable: `reports/admin-ui-test-selection.md` with the ranked mechanisms, the code evidence for each, the confirming run spec, and any fix sketch. Rules: WORKFLOW.md binds (claim before work, mark the claim DONE in the same push as the deliverable). No edit to any branch. No builds or tests on the Windows host. No PR, comment or Jira write. No em dashes.
