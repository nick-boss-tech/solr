# Gate: SOLR-18628 idle h2c streamed update fail-before, round 1

Status: OPEN

- Category: reproduction phase for the claimed idle h2c streamed-update defect. Tags: gate, implementation, review, records.
- Intended host: a Linux CPU gate host with Gradle/JDK 21 and a JDK 25 test runtime. Use the host's standard gate runner and lock; verify tool paths and the actual forked test JVM version before execution.
- Fork: `nick-boss-tech/solr:solr-18628-submit`.
- Exact test-only head H1: `aebe1d8e9b0f226948e092f2610682c9c2596f27`. Stop if the live fork ref differs.
- Production baseline B: `c0c219e374611e0edc475c0f793c9bd688b13292`. H1 adds the regression test only; it contains no production or changelog changes.
- Earlier test-only head: `ac85e718803994d258c766e66d18831c5de24d3a` (same test with a 35-second fixed idle; H1 reads the keepalive property so the gate can shorten it).
- Only changed path: `solr/solrj/src/test/org/apache/solr/client/solrj/impl/HttpJdkSolrClientTest.java`.
- Claim: `claims/solr-18628-idle-h2c-update-windows-round-1.md`.
- Assignment: `assignments/solr-18628-httpjdk-h2c-idle-round-1.md`.
- Receipt: `receipts/SOLR-18628-h2c-fail-before-round-1.md`, owned by the Linux executor.

## Purpose

Establish the fail-before before any production change. The test first performs a streamed update over cleartext HTTP/2 and asserts that the successful HEAD probe was recorded. It then sends an HTTP/1.1 control update and waits past the JDK HTTP/2 keepalive timeout before sending another HTTP/1.1 update followed by the second h2c update. The JDK keepalive property is read by the test to choose its wait; with the one-second setting, it waits three seconds. Without an override, it waits 32 seconds, past the JDK 21/25 documented 30-second default.

The intended fail-before is a single test failure at the second h2c update, with the HTTP/1.1 control having completed. A pass on B/H1 means this attempt did not reproduce the report; do not make the production change on that result. Compilation, setup, a skipped test, timeout, or an earlier failure is not fail-before proof.

## Required execution

1. Refresh the live fork ref and require H1. Record host, JDK 21 Gradle path/version, JDK 25 test runtime path/version, runner, UTC times, and clean checkout status. If this host cannot run the test fork on JDK 25 while using JDK 21 for Gradle, record the capability block without substituting JDK 21.
2. Run the standard tidy preflight and `git diff --check`; require no formatting changes. Compile `:solr:solrj:compileJava` and `:solr:solrj:compileTestJava` before the expected runtime failure.
3. Run the focused test at H1 with the keepalive timeout passed at JVM startup so it reaches both the Gradle process and the forked JDK 25 test process. Confirm the worker reports Java 25 and sees `jdk.httpclient.keepalive.timeout=1` (or the HTTP/2 override). Use a fresh Gradle process.

   `JAVA_TOOL_OPTIONS=-Djdk.httpclient.keepalive.timeout=1 ./gradlew :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.client.solrj.impl.HttpJdkSolrClientTest.testStreamedUpdatesAfterIdleH2cConnectionExpires -Ptests.seed=18628C0DE --rerun-tasks`

4. Preserve the exact command, exit code, complete Gradle log, fresh test XML, expected exception/stack at the second h2c update, and proof that the HTTP/1.1 control completed. If it passes, record non-reproduction and do not continue with the production hypothesis. If it fails elsewhere, diagnose once and follow the failure route in `WORKFLOW.md`; do not silently rerun.

This phase does not mark the ticket handled. If fail-before reproduces, the implementation host will prepare a scoped candidate fix and push a new head. A separate exact-head gate will cover the fail-before commit, pass-after head, tidy/format, the focused test and HTTP/1.1 control, and root `check -x test`. Keep the ticket Implemented, Proof Pending until that final receipt is green.
