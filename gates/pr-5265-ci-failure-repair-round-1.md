# Gate: PR 5265 CI failure repair, round 1 (exact head)

Status: CLAIMED by phone agent (claim: `claims/pr-5265-ci-failure-repair-round-1-phone-agent.md`); Selenium capability verified on the claiming host (Chromium 155.0.8059.80, ChromeDriver 155.0.8059.84 via Selenium Manager); running.

- Assignment: `assignments/pr-5265-ci-failure-repair-round-1.md`.
- Report: `reports/pr-5265-ci-failure-repair-round-1.md`.
- Target: PR 5265, SOLR-16398, `nick-boss-tech/solr:solr-16398-split-shard`.
- Candidate SHA: `16b0d50218bf82dc0d28158a63602c2d3da2ef3a`.
- Parent: `009f2c60513636b4aba2c93f4a4137aa39ccea64`.
- Exact scope: `solr/webapp/web/js/angular/app.js` and `solr/core/src/test/org/apache/solr/handler/admin/TestApiFramework.java`.
- Capability requirements: Linux, JDK 21, repository-pinned Gradle and dependencies, plus a working Selenium browser/driver for the webapp test. The registered Linux host records do not currently declare Selenium/browser capability. A host must confirm the browser and driver versions before starting; if unavailable, leave this job open and report the blocker without skipping the required test.
- Host must acquire its serialized Gradle lock and follow the Linux pool resource guards. Use a fresh isolated worktree at the full candidate SHA, not cached test output. Record host, JDK, browser/driver versions, UTC start/end, exact commands, exit codes, test counts, and fresh log/XML paths.

## Required proof

1. Verify the fetched fork branch is exactly `16b0d50218bf82dc0d28158a63602c2d3da2ef3a`; run `./gradlew tidy`; confirm tidy leaves the candidate tree clean. If tidy changes tracked files, stop, record the diff, and request a new pushed head and superseding gate before continuing.
2. Run the original Admin UI reproducer with Selenium and fresh XML:

   ```text
   ./gradlew :solr:webapp:test --tests "org.apache.solr.webapp.AdminUiCollectionScreensTest.testAnalysisScreenAnalyzesText" "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m" -Ptests.seed=83E3932EA6612D4A -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1 -Ptests.selenium=true
   ```

   Require the named test to execute and pass; record the actual browser/driver and any security-manager or Selenium setup details. A skipped test is not proof.
3. Reproduce the core API-framework test with fresh XML:

   ```text
   ./gradlew :solr:core:test --tests "org.apache.solr.handler.admin.TestApiFramework.testFramework" -Ptests.seed=4E30896B42ECC98D
   ```

4. Run the split-shard focused and neighboring suites from the previous gate with fresh XML and seed `16398C0DE`:

   ```text
   ./gradlew :solr:core:test --tests "org.apache.solr.handler.admin.api.SplitShardMessageMappingTest" --tests "org.apache.solr.handler.admin.api.SplitShardAPITest" --tests "org.apache.solr.handler.admin.TestCollectionAPIs" --tests "org.apache.solr.handler.admin.api.CreateShardAPITest" --tests "org.apache.solr.handler.admin.api.DeleteShardAPITest" --tests "org.apache.solr.handler.V2ApiIntegrationTest" -Dtests.seed=16398C0DE
   ```

   Record per-suite counts, skips, failures, and fresh JUnit XML locations.
5. Run the required Error Prone compile and OpenAPI client generation:

   ```text
   ./gradlew :solr:api:compileJava :solr:solrj:compileJava :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true
   ```

6. Run the module check at the same head:

   ```text
   ./gradlew :solr:core:check -x test
   ```

7. Record `git diff --check`, final clean-tree state, and a separate read-back confirming the fork branch and live PR head still equal the tested candidate SHA.

PASS requires every step above at the exact candidate. Any source, test, or formatting change invalidates this gate and requires a normal push plus a superseding exact-head job. No PR comment/review/body edit or JIRA write is authorized by this gate.
