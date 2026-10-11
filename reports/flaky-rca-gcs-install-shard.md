# Flaky RCA: GCSInstallShardTest classMethod failure on the SOLR-18506 branch

Assignment: `assignments/pool-flaky-rca-gcs-install-shard.md`. Reading only. No builds, no test runs, no gate runs, no test-queue commands, no GitHub or Jira writes, no commits. The two Crave job logs were read through the read-only `gh` wrapper and saved to the session scratchpad, outside this worktree.

Trees read:
- Branch head: `77c019e1ff0e922e3c2350379dece841651b1d44` (`origin/solr-18506-submit`, fetched; matches the run's head SHA).
- Merge base with main, which is the CI base: `22a8cfebbbdb026437d10b0aa47bb66ffdb23a9a`.
- Current main: `upstream/main` = `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` (local ref in the source checkout; 46 commits past the merge base).

## Bottom line

1. Top-ranked hypothesis (H1): a pre-existing, intermittent hang in the shared MiniSolrCloudCluster shutdown path. The HTTP client stop inside `HttpShardHandlerFactory.close` blocks, the close pool waits 60 s, is interrupted, waits again, and the cluster check reports the class as failed. The branch is not involved.
2. The branch diff is one test file in `:solr:core` (`TestThinCache.java`, +9 lines). The failing JVM is `:solr:modules:gcs-repository`, which does not load that file, and no failing stack frame comes from the diff.
3. The seed is recovered from the run's own log: `B15D4D92C6F2B656`.
4. The RCA's restore route (`IndexNotFoundException`) is not what the check collected. Those lines are logged WARNs from expected restore failures. The collected cause is "Timeout waiting for pool to shutdown".
5. The same chain appears in run 37987579785 (SOLR-3657 branch, base `8e62c2686882`, an ancestor of current main, seed `B94347D2600CC75D`). That is on-disk evidence that the flake predates this branch.

## 1. What is on disk and what is not

Confirmed by `gh run view` (read only):
- Run `37384974639`: workflow "Solr Tests via Crave", event `pull_request`, head branch `solr-18506-submit`, head SHA `77c019e1ff0e922e3c2350379dece841651b1d44`, created 2026-10-05T22:50:22Z, conclusion `failure`.
- Its only job, `112037885655` ("Run Solr Tests using Crave.io resources"), failed at step "Initialize, build, test".
- The failed-step log is saved as `crave-37384974639-failed.log` in the scratchpad. Line references below are to that file.
  - Line 57 and 75: CI checked out `refs/pull/5029/merge`; `HEAD is now at 9b7eb492981 Merge 77c019e1... into 22a8cfebbb`. So the tested tree is the branch head on the merge base, which is the same tree as the head itself, because the diff is one file.
  - Line 132: `Running tests with randomization seed: tests.seed=B15D4D92C6F2B656`.
  - Lines 5921-5926: `GCSInstallShardTest > classMethod FAILED`, `java.lang.Exception: Error shutting down MiniSolrCloudCluster`.
  - Lines 13333-13335: `:solr:modules:gcs-repository:test (FAILURE): 35 test(s), 1 failure(s), 6 skipped`.
  - Line 13358: the CI's reproduce command, used in section 6.
- Other on-disk records of this run: `reports/review-confidence-round-3-a2.md:100` and `:184` (run and classMethod, no seed); `research/branch-reviews/round-31/SOLR-18506-review.md:5-8` (seed `B15D4D92C6F2B656`, "reads as unrelated noise", rerun advised; the cause was not read); `research/pr-rereview-tracker-2026-10-06.md:28` and `:88`.

Not on disk:
- The takeover log entry (2026-10-10 about 15:50 MDT). Searched: this worktree, the workspace `research/`, `reports/`, `claims/`, `gates/`, `env/`, `.recovery/`, and the top-level notes. Not found.
- A "goal CI failure record" under that name. Nearest records are the review files above.
- The assignment's "pre-existing status unverified" is not on disk as a finding. This report supplies the reading, not the run result.
- The CI merge commit `9b7eb492981` is not in the local object store. Its tree is reasoned from the merge base, not read.

Second record, read for the cross-check: job `114192758248` of run `37987579785` (the t1 assignment's source run).
- `crave-37987579785-failed.log`, line 75: `HEAD is now at 174a25d1c86 Merge 14edaca577c0... into 8e62c2686882`.
- Line 589: seed `B94347D2600CC75D`. Lines 6385-6386: the same classMethod failure.
- Lines 13590-13630 and 13662: the same suppressed cause as run 37384974639 (section 4).
- This answers t1's "Run 1" for that job. The restore-frame hypothesis (t1 H2) does not hold for it.

## 2. What classMethod does before any test method, and what it touches

Paths read at `upstream/main`. The source files in the chain are unchanged between `22a8cfebbb` and `upstream/main`, and between `8e62c2686882` and `upstream/main` (checked with `git diff --stat`). The only change in the GCS module's directory over that range is its `gradle.lockfile` (section 3).

Class-level order for `GCSInstallShardTest extends AbstractInstallShardTest extends SolrCloudTestCase`:
1. `@BeforeClass AbstractInstallShardTest.seedDocGenerator` (`AbstractInstallShardTest.java:80-84`). Sets `solr.directoryFactory` for the JVM at class level (line 83). Not restored per class.
2. `@BeforeClass GCSInstallShardTest.setupClass` (`GCSInstallShardTest.java:58-67`). `configureCluster(2)` with a solr.xml that has `trackingBackupRepository`, `errorBackupRepository` (`ErrorThrowingTrackingBackupRepository`, `hostPort` at line 48) and `localfs` = `LocalStorageGCSBackupRepository` (lines 39-54). Then `bootstrapBackupRepositoryData("backup1")` (line 66) builds a 1-shard and a 4-shard collection and indexes docs.
3. The test methods (35 run, 6 skipped).
4. `@AfterClass GCSInstallShardTest.tearDownClass` (`GCSInstallShardTest.java:69-72`) calls `LocalStorageGCSBackupRepository.clearStashedStorage()`. JUnit 4 runs subclass `@AfterClass` before the superclass one, so the stash is cleared while the nodes are still up.
5. `@AfterClass SolrCloudTestCase.shutdownCluster` (`SolrCloudTestCase.java:157-166`) calls `MiniSolrCloudCluster.shutdown()` (`MiniSolrCloudCluster.java:615-645`).

Shared state and external dependencies:
- GCS: no network, no credentials. `initStorage` is overridden (`LocalStorageGCSBackupRepository.java:37-48`) to use an in-memory `LocalStorageHelper` behind a static `stashedStorage` (line 34; `clearStashedStorage` at 50-54; `getSingletonStorage` at 78-90).
- Ports: Jetty on 127.0.0.1 ephemeral ports; embedded ZooKeeper on a random port (log line 5971). The per-node `hostPort` is set in `nodeProperties` (`JettySolrRunner.java:320-323`). No fixed-port binding on this path.
- Temp dirs under the module's `build/tmp/tests-tmp`.
- Static test state: `ErrorThrowingTrackingBackupRepository.portsToFailOn` (`AbstractIncrementalBackupTest.java:581-583`), set at `AbstractInstallShardTest.java:255-256` and never reset. It gates only the copy methods (lines 595 and 605), not shutdown.
- Timing: `ExecutorUtil.awaitTermination` waits 60 s, calls `shutdownNow`, waits 60 s more, then throws (`solr/solrj/.../ExecutorUtil.java:134-149`).

Teardown path that fails (line numbers at `upstream/main`):
- `CoreContainerProvider.contextDestroyed` (line 90) calls `close()`, which calls `cc.shutdown()` (lines 131-133). Jetty runs this inside `server.stop()` (`JettySolrRunner.java:627`), called from the parallel jetty-closer tasks (`MiniSolrCloudCluster.java:627-634`).
- `CoreContainer.shutdown` (`CoreContainer.java:1220-1320`): creates `closeThreadPool` (1233-1234), submits `shardHandlerFactory.close()` (1300), and ends with `shutdownAndAwaitTermination(customThreadPool)` (1315).
- `HttpShardHandlerFactory.close` (`:345-358`): `IOUtils.closeQuietly(defaultClient)` (353), then `shutdownAndAwaitTermination(commExecutor)` (356). The default client is built with `.withExecutor(commExecutor)` (308-312). `commExecutor` is the `httpShardExecutor` pool (279-289).
- `HttpJettySolrClient.close` (`solrj-jetty/.../HttpJettySolrClient.java:341-361`): `asyncTracker.waitForComplete()` (343), `httpClient.stop()` (345-346), `RuntimeException("Exception on closing client")` (357-358). It shuts down the executor only if it owns it (360-361); `createHttpClient` sets `shutdownExecutor = false` when an executor is passed (213-220).
- Jetty `ManagedSelector.doStop` (external library; Jetty 12.1.12 per log line 13037): waits on a `CountDownLatch` (log lines 13072-13073).
- `ExecutorUtil.awaitTermination` throws `RuntimeException("Timeout waiting for pool to shutdown")` (`ExecutorUtil.java:146-149`).
- `MiniSolrCloudCluster.checkForExceptions` (`:718-736`) adds each `ExecutionException` cause as suppressed (722-727). `shutdown` throws "Error shutting down MiniSolrCloudCluster" (636-640).

## 3. Does the branch touch any of it?

- `git diff 22a8cfebbb..77c019e1ff0` is one file: `solr/core/src/test/org/apache/solr/search/TestThinCache.java`, +9 lines in `testSimple` (a `backing.setMaxSize(200)` call and comments). Nothing else.
- None of the chain files is in the diff. None changed between the merge base and `upstream/main`.
- Changes that did land between base and main, and that are not in the failing stack: ZooKeeper 3.9.5 to 3.9.6 (`e66837f05b4`; `gradle/libs.versions.toml` and lockfiles) and the per-test system-property rule moved into `SolrTestCase` (`9d7cc2884e8`). Jetty is not changed (the toml diff touches only the ZooKeeper line among the Jetty, ZooKeeper, Curator and randomizedtesting keys).
- The failing JVM cannot load `TestThinCache`. `solr/modules/gcs-repository/build.gradle` has `api project(':solr:core')` (main code, line 31) and `testImplementation project(':solr:test-framework')` (line 55); it does not depend on core test sources. The log says `All tests run in this JVM: [GCSInstallShardTest]` (line 13274).
- No frame in either failing stack comes from `TestThinCache` or from the branch.

## 4. What the failing run shows

Timeline from `crave-37384974639-failed.log` (JVM clock; the JVM started at 00:06:56, line 5955):
- About 42.0 s: both nodes' jetty-closer threads start `CoreContainer.shutdown` (lines 13041-13042). Connectors are already stopped (13039-13040).
- About 42.2 s: the close pool (`closeThreadPool-532`) is shut down and the first 60 s wait starts. The client-close task is in `HttpJettySolrClient.close` -> Jetty `ManagedSelector.doStop` (13070-13094).
- 102184 ms: the first wait ends and `shutdownNow` interrupts the thread. `IOUtils` logs "Error while closing" from `HttpShardHandlerFactory.close` (`:353`), caused by `RuntimeException: Exception on closing client` caused by `InterruptedException` (13060-13070).
- After the interrupt, the same thread is still in the close pool while it waits on the shard executor (`HttpShardHandlerFactory.java:356`). That wait is inferred from the code order, not logged.
- 162184 ms: the second wait ends with the pool still busy: "Threads from pool did not forcefully stop ... active threads = 1" (13095).
- 162188 ms: `MiniSolrCloudCluster` logs "Error shutting down MiniSolrCloudCluster" with `ExecutionException` caused by `RuntimeException: Timeout waiting for pool to shutdown` (13100-13101; cause at 13133-13137). The Gradle header shows the same exception (5921-5926).
- The class is reported as failed: 35 tests, 1 failure, 6 skipped (13333-13335).

The restore lines are not the collected cause:
- The 12 `IndexNotFoundException: no segments* file found` traces (lines 11260-12025) are WARNs from `RestoreCore.java:218` ("Could not switch to restored index. Rolling back to the current index") during the expected restore failures in the test methods. They are logged, not collected.
- `checkForExceptions` reads only `ExecutionException` values from the stop callables (`MiniSolrCloudCluster.java:722-728`). The injected `UnsupportedOperationException` (lines 12505, 12564) comes from `AbstractIncrementalBackupTest.java:594-609`.

Shutdown is concurrent in this suite: all jetty stops are submitted together (`MiniSolrCloudCluster.java:627-634`), so one node's shard client can be closing while the peer node is also stopping. Log line 13097 (after the timeout) shows an `httpShardExecutor` thread logging a `ClosedSelectorException` from Jetty's selector. That is consistent with in-flight shard I/O at close, but it is one warning after the fact.

## 5. Ranked hypotheses

**H1 (rank 1; pre-existing; shared shutdown path, not the branch).** The Jetty client stop started from `HttpShardHandlerFactory.close` blocks until interrupted, and the close pool then times out. The class teardown fails.
- For: the same chain in two runs on two branches with two seeds, both on Crave, both in the GCS class. Run 37987579785 used a base that is an ancestor of current main; per the t1 assignment text, its branch changes only `DocumentBuilder.java` in `:solr:core`, not GCS or shutdown code. The chain lies in production shutdown code and Jetty, not in GCS code or in either branch diff.
- Against or unknown: why the stop blocks is not shown. The log has no thread dump. Two inferences, neither verified:
  - (a) the Jetty selector may run on the client's executor, which is `commExecutor` (`HttpShardHandlerFactory.java:312`), and `commExecutor` is shut down only after the client (353 before 356);
  - (b) in-flight shard requests from a node that is closing may hold the executor. The log's single post-timeout `ClosedSelectorException` (line 13097) fits this but does not prove it.
- Confidence: high that the mechanism is in shutdown and not in the test; medium on the trigger.
- Classification: pre-existing flake in shared teardown code. The defect sits in production shutdown (the close order in `HttpShardHandlerFactory`, or Jetty), not in the test. Do not change `MiniSolrCloudCluster.checkForExceptions`; it is what makes the hang visible.

**H2 (rank 2; infrastructure or timing contributor).** The Crave runner's timing makes the stop race likely.
- For: the flake is intermittent. Many Crave runs on disk passed, for example PR 5015 (`reports/configsets-round-1-c.md:9`) and `reports/review-confidence-round-3-a2.md:86`. Those runs also ran this module (inference from `./gradlew test` in `.github/workflows/tests-via-crave.yml:41`). The 60 s waits make any stall fatal.
- Against as primary: two Crave runs on two bases show the same chain, and the trigger is not Crave-specific in the code. The default test JVM args include `-XX:ActiveProcessorCount=1` (`gradle/testing/defaults-tests.gradle:53`), so the single-CPU setting is the project default, not a Crave quirk.
- No host metrics on disk. Settle with the lane's rate versus Crave's (section 6).

**H3 (rank 3; branch-caused).** Ruled out on the code.
- The diff's only file is not on this JVM's classpath. No frame comes from it. The diff adds no thread, static, port, or shutdown code.
- Reopening requires a frame from `TestThinCache` in a failing stack. Neither log has one.

**H4 (rejected; the RCA's restore route, t1 H2).** A restore exception collected at shutdown.
- Refuted by the logs. The collected cause is the close-pool timeout (13101, 13133). The `IndexNotFoundException` traces are WARN logs (11259-11260). The same holds for job `114192758248` (13630, 13662).

**H5 (low; harness drift between base and main).** ZooKeeper 3.9.6 and the `SolrTestCase` rule move are not in either stack. Relevant only if run 2 (current main) shows the signature and run 1 (base) does not; then check the ZooKeeper bump first.

## 6. Run spec for the Linux lane (not executed here)

Common to all three runs:
- Module `:solr:modules:gcs-repository`, class `org.apache.solr.gcs.GCSInstallShardTest`, seed `B15D4D92C6F2B656` (recovered from the run's log; no fixed-seed substitute is needed). JDK 21 (the CI used Eclipse Adoptium 21.0.10).
- Command, once per repetition, with a clean test task so Gradle does not skip it:

  ```
  ./gradlew :solr:modules:gcs-repository:cleanTest :solr:modules:gcs-repository:test --tests "org.apache.solr.gcs.GCSInstallShardTest" "-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m" -Ptests.seed=B15D4D92C6F2B656 -Ptests.locale=en-TT -Ptests.timezone=America/Rosario -Ptests.timeoutSuite='600000!' -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1
  ```

  The `-P` set is the CI reproduce line (log line 13358). Locale and timezone are the values the CI printed for this seed (log lines 13272 and 13275), pinned here. Check the test-params NOTE in the lane's log shows `locale=en-TT, timezone=America/Rosario`; if Gradle rejects the `-P` form for these two, drop them and confirm the NOTE matches.
- Repetitions: 5 per tree, same seed each time. The seed fixes test order and data; the hang is timing.
- Save each repetition's console log and `solr/modules/gcs-repository/build/test-results/test/TEST-org.apache.solr.gcs.GCSInstallShardTest.xml`.

Signatures:
- SIG-SHUTDOWN: output has `GCSInstallShardTest > classMethod FAILED`, `Error shutting down MiniSolrCloudCluster`, and `Timeout waiting for pool to shutdown`.
- SIG-OTHER: any other failure in `GCSInstallShardTest` (setupClass, a test method, or a different cause).
- CLEAN: exit 0, the module summary line shows 0 failures, and there is no `classMethod FAILED` line.
- A repetition that fails before the test task (compile or Gradle error) is not counted. Re-run it.

Run 1, pre-merge main: `22a8cfebbbdb026437d10b0aa47bb66ffdb23a9a` (the CI base of run 37384974639).
- Confirms H1 (pre-existing): SIG-SHUTDOWN in at least 1 of 5 repetitions. That reproduces the flake without the branch.
- 0 of 5: no conclusion about frequency. Continue to runs 2 and 3 and record the count.

Run 2, current main: `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c`. Record the SHA the lane actually reads, because the source checkout's `upstream/main` ref may lag GitHub.
- SIG-SHUTDOWN at least once: the flake is present on current main.

Run 3, PR head: `77c019e1ff0e922e3c2350379dece841651b1d44`. Same tree as the CI merge `9b7eb492981`, since the merge base is `22a8cfebbb` and the diff is one test file.
- Pre-existing is confirmed by SIG-SHUTDOWN in run 1 or run 2, regardless of run 3.
- Branch-caused is supported only by SIG-OTHER with a frame from `TestThinCache`, which the classpath analysis says cannot happen. Or, if run 3 shows SIG-SHUTDOWN and runs 1 and 2 show 0 of 5 each, extend runs 1 and 2 to 10 repetitions each before drawing any conclusion.
- Infrastructure contribution is supported only if the Linux lane shows 0 of 15 SIG-SHUTDOWN across the three runs while Crave shows the signature in comparable runs. Fifteen repetitions is a weak comparison; treat it as supported only with a Crave comparison set from the lead.
- All CLEAN means no reproduction in 15 repetitions. It is not clearance. Do not reclassify H1 on a clean result.

Diagnostic, only if SIG-SHUTDOWN reproduces: save a thread dump (`jcmd <pid> Thread.print`) about 60 s after the shutdown starts. It shows where the `closeThreadPool` thread and the `httpShardExecutor` threads are blocked, which would settle the trigger in H1. Save the repetition's log; do not relaunch it on a different seed, because a different seed changes the test set, not the timing.

## 7. Proposed direction (not applied)

- Do not change the shutdown check or this test for this flake. The t1 "GCS stash clear" hardening is not on any failing frame; leave it.
- If the thread dump or inference (a) holds, the candidate fix is in `HttpShardHandlerFactory.close` (`solr/core`): the order of shard-executor drain and client stop. This is a production change and an owner decision, not something for this branch.
- A separate investigation of the Jetty stop is an owner call.

## 8. Not done

- No builds, tests, gate runs, test-queue commands, GitHub or Jira writes, claim edits, or commits.
- Not read: Jetty's source (external library); Develocity history; other Crave logs for this suite on main (only the two runs above were read); the CI merge commit `9b7eb492981` (not local).
- The takeover log entry and the goal CI failure record are not on disk (section 1).
- Claim `claims/pool-flaky-rca-gcs-install-shard.md` still reads ACTIVE. Per `WORKFLOW.md` rule 5, the parent marks it DONE in the same push as this report; this subagent wrote only this file.
- Logs are in the session scratchpad, not in the worktree.
