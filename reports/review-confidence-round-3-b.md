# Review confidence round 3, slice B: VM2 gate job file audit

Scope: the 29 jobs in `assignments/pool-vm2-gate-backlog-round-1.md` (13 jobs) and `assignments/pool-vm2-gate-backlog-round-2.md` (16 jobs), each with its `gates/` job file. The 9865 and 17287 recount is one job file covering two heads, so it is one row in the table.

Method: read-only. `git ls-remote origin refs/heads/<branch>` on the fork, git object reads at each head (`git show`, `git grep`, `git ls-tree`, `git diff` against `git merge-base <head> upstream/main`), and the receipts, claims, and answers files on `origin/pr-prepare`. No builds, no Gradle, no tests, no BATS, no gate or queue runs.

## Verdict counts

- CLEAR: 12
- FIX: 17

## Heads

- All 29 job heads are on the fork. Every gate file head equals its `ls-remote` tip, its backlog prefix, and its receipt head.
- Jobs whose head is not on the fork: none.
- Head mismatches: none. The defects found are in the module, class, premise, proof-shape, and status text, not in the heads.

## Environment

No job file states a JDK, `GRADLE_USER_HOME`, or a work directory. The host note in both backlog assignments (`GRADLE_USER_HOME` under `/workspace`) governs all 29 jobs and conflicts with none of them.

## Verdict table

| # | Job | Gate file | Head | Class (package, module) | Verdict |
|---|-----|-----------|------|-------------------------|---------|
| 1 | R1-1 SOLR-12998 | gates/SOLR-12998.md | 62a17a116b5 | SyncStrategyTest (org.apache.solr.cloud), CoreAdminOperationTest (org.apache.solr.handler.admin), :solr:core | FIX (backlog status) |
| 2 | R1-2 SOLR-18391 | gates/SOLR-18391.md | adcda10b501 | CreateCollectionCleanupTest, DeleteCoreRemnantsOnCreateTest, OverseerCollectionConfigSetProcessorTest (org.apache.solr.cloud); CreateCollectionCmdRetryTest (org.apache.solr.cloud.api.collections); PlacementPluginIntegrationTest (org.apache.solr.cluster.placement.impl); :solr:core | FIX (backlog status, stale expectation) |
| 3 | R1-3 SOLR-4502 | gates/SOLR-4502.md | 4491f5162c1 | TestCoreContainer (org.apache.solr.core), :solr:core | CLEAR |
| 4 | R1-4 SOLR-5011 | gates/SOLR-5011.md | f20ffe48078 | CoreCloseResourceLoaderTest (org.apache.solr.core), :solr:core | FIX (proof shape) |
| 5 | R1-5 SOLR-12916 | gates/SOLR-12916.md | ebe5db37433 | QuerySenderListenerTest (org.apache.solr.core), :solr:core | FIX (premise vehicle) |
| 6 | R1-6 SOLR-16499 | gates/SOLR-16499.md | 6a2ff7618d9 | ReplaceNodeAPITest (org.apache.solr.handler.admin.api), :solr:core | FIX (proof shape) |
| 7 | R1-7 SOLR-5262 premise | gates/SOLR-5262-premise.md | ade8b80264a | TestCoreDescriptorImplicitProperties (org.apache.solr.core), :solr:core | FIX (showing (a) has no vehicle) |
| 8 | R1-8 SOLR-9091 | gates/SOLR-9091.md | e31bdaa4d27 | TestRestoreCore (org.apache.solr.handler), :solr:core | CLEAR |
| 9 | R1-9 SOLR-9382 | gates/SOLR-9382.md | c0b5fec1be2 | TestReplicationConfFileGlob (org.apache.solr.handler), :solr:core | CLEAR |
| 10 | R1-10 TestRestoreCore recount | gates/SOLR-9865-17287-recount.md | 4937608bb18, 6957daf8261 | TestRestoreCore (org.apache.solr.handler), :solr:core | CLEAR (expected count note) |
| 11 | R1-11 SOLR-11650 base run | gates/SOLR-11650-baserun.md | e4f5e941cd8 | testFollowerDetailsRedactLeaderUrlPassword in TestUserManagedReplicationWithAuth (org.apache.solr.handler), :solr:core | FIX (production files at base) |
| 12 | R1-12 SOLR-10390 premise | gates/SOLR-10390-premise.md | 4af4a6834e2 | BATS test in solr/packaging/test/test_start_solr.bats | CLEAR |
| 13 | R1-13 SOLR-13705 premise | gates/SOLR-13705-premise.md | 5f141fb2af3 | SSLConfigurationsFactoryTest (org.apache.solr.util.configuration), :solr:core | CLEAR |
| 14 | R2-1 SOLR-9852 | gates/SOLR-9852.md | 31f58dbe8e6 | JdbcTest (org.apache.solr.client.solrj.io.sql), :solr:solrj-streaming | CLEAR |
| 15 | R2-2 SOLR-10882 | gates/SOLR-10882.md | 83fc3dfeb24 | ArrayEvaluatorTest (org.apache.solr.client.solrj.io.stream.eval), :solr:solrj-streaming | CLEAR |
| 16 | R2-3 SOLR-3498 | gates/SOLR-3498.md | 812598302de | TestContentWriterUpdateRequest (org.apache.solr.client.solrj.request), :solr:solrj | CLEAR |
| 17 | R2-4 SOLR-10364 | gates/SOLR-10364.md | 502bdbf033f | TestDocumentObjectBinder (org.apache.solr.client.solrj.beans), :solr:solrj | CLEAR |
| 18 | R2-5 SOLR-11356 | gates/SOLR-11356.md | 8474e5a3a26 | ConcurrentUpdateJettySolrClientTest (org.apache.solr.client.solrj.jetty), :solr:solrj-jetty (abstract base in :solr:solrj) | FIX (run class not named) |
| 19 | R2-6 SOLR-14187 | gates/SOLR-14187.md | 45b0f7ce34f | CollectionAdminRequestAsyncAuthTest (org.apache.solr.client.solrj.request), :solr:solrj | FIX (base cannot compile) |
| 20 | R2-7 SOLR-10667 | gates/SOLR-10667.md | 32b594f280c | BATS test_modules.bats (no Java class); gradle/solr/packaging.gradle | FIX (no task or base named) |
| 21 | R2-8 SOLR-12347 | gates/SOLR-12347.md | b77acba2ad6 | BATS test_start_solr.bats (no Java class) | CLEAR (base SHA note) |
| 22 | R2-9 SOLR-11678 | gates/SOLR-11678.md | 55d8cd189d1 | SSLConfigurationsTest (org.apache.solr.util.configuration); EnvSSLCredentialProviderTest, SysPropSSLCredentialProviderTest (org.apache.solr.util.configuration.providers); :solr:core | FIX (module text, proof compile) |
| 23 | R2-10 SOLR-12161 | gates/SOLR-12161.md | 1725cbd8489 | BasicAuthIntegrationTest (org.apache.solr.security), :solr:core | CLEAR |
| 24 | R2-11 SOLR-6430 | gates/SOLR-6430.md | 22f83870c4a | docs only (indexing-guide field-type-definitions-and-properties.adoc) | FIX (docs task not named) |
| 25 | R2-12 SOLR-7119 | gates/SOLR-7119.md | 9593f4bd0d6 | docs only (query-guide faceting.adoc) | FIX (docs task not named) |
| 26 | R2-13 SOLR-11700 | gates/SOLR-11700.md | c513388be05 | docs only (indexing-guide filters.adoc) | FIX (docs task not named) |
| 27 | R2-14 SOLR-17356 | gates/SOLR-17356.md | ea7fc15cade | docs only (indexing-guide language-analysis.adoc) | FIX (expected outcome, docs task) |
| 28 | R2-15 SOLR-16322 | gates/SOLR-16322.md | 65e0b8d7c79 | Gradle script gradle/testing/failed-tests-at-end.gradle (no test class) | FIX (no failing vehicle) |
| 29 | R2-16 SOLR-17722 | gates/SOLR-17722.md | fee3a26beb3 | SolrMessageProcessorTest (org.apache.solr.crossdc.manager.messageprocessor), :solr:cross-dc-manager | FIX (premise wording and vehicle) |

## Findings for FIX jobs

Each finding gives the exact line, the fact that conflicts with it, and the fix.

### 1. R1-1 SOLR-12998 (backlog status)

- Line: `assignments/pool-vm2-gate-backlog-round-1.md`, Tranche 1 item 1: "SOLR-12998 live-tip gate ... Gate at 62a17a116b5", listed under "claimable now".
- Conflict: `gates/SOLR-12998.md` Status reads "BLOCKED ON PROOF LEG by vm2 (run finished 2026-10-10T19:17:39Z)". `claims/pool-vm2-gate-backlog-12998.md` is DONE with outcome "BLOCKED ON PROOF LEG". The receipt records green gate steps and a proof leg that cannot compile at merge-base c3e18f1e4550, so a relaunch repeats a known blocked result.
- Fix: in the backlog, mark item 1 "run finished 2026-10-10T19:17Z; gate steps green; proof leg blocked; main agent decision pending; not claimable" and remove it from the claimable tranche. In `gates/SOLR-12998.md`, tick the last checklist box so the file matches the DONE claim.

### 2. R1-2 SOLR-18391 (backlog status and stale expectation)

- Line A: `assignments/pool-vm2-gate-backlog-round-1.md`, Tranche 1 item 2: "SOLR-18391 graceful-create gate ... Gate at adcda10b501", listed under "claimable now".
- Conflict A: `gates/SOLR-18391.md` Status reads "DONE on vm2". `claims/pool-vm2-gate-backlog-18391.md` is DONE: "Proof leg has a mismatch (second base failure in CreateCollectionCleanupTest, testCreateDoesNotDeleteExistingCollectionOnStaleView)".
- Fix A: backlog item 2 becomes "DONE 2026-10-10T19:45Z; gate green at adcda10b501; proof leg mismatch; main agent decision". Do not re-run.
- Line B: `gates/SOLR-18391.md`, "Proof shape (from the receipt): ... CreateCollectionCleanupTest runs with exactly 1 failure, testCleanupAfterUnexpectedPlacementFailure".
- Conflict B: the run produced a second base failure. The method is at head line 218 of CreateCollectionCleanupTest.java.
- Fix B: append "Observed on vm2: a second base failure, testCreateDoesNotDeleteExistingCollectionOnStaleView (head line 218); the exactly-one expectation is superseded." Leave the checklist "proof leg" box open or marked as mismatch.

### 3. R1-4 SOLR-5011 (proof shape)

- Line: `gates/SOLR-5011.md`, "- Proof shape: production reverted to the branch's merge-base with the branch tests kept; CoreCloseResourceLoaderTest must fail there at its first assertTrue, checked by failure content."
- Conflict: `CoreCloseResourceLoaderTest.java` calls `oldLoader.isClosed()` at lines 39 (assertFalse) and 45 (assertTrue). `isClosed()` and the `closed` flag are new in `SolrResourceLoader.java` on the branch (head line 917); neither exists at merge-base cabedd1d9680. Reverting `SolrResourceLoader.java` breaks test compilation, so the base run cannot fail at an assertion.
- Also: no branch test covers the shared-schema scenario (no `shareSchema` in the test file), so the job's record-only path applies.
- Fix: "- Proof shape: revert only `solr/core/src/java/org/apache/solr/core/SolrCore.java` (the doClose resourceLoader close) to merge-base; keep `SolrResourceLoader.java` so the test compiles. CoreCloseResourceLoaderTest must then fail at line 45, `assertTrue(oldLoader.isClosed())`, checked by failure content; line 39 passes on base."

### 4. R1-5 SOLR-12916 (premise vehicle)

- Line: `gates/SOLR-12916.md`, "- Premise run (before the gate steps): exercise the Config API round trip the answers require (the nested form that base drops), on base production and at the head, and record the outcome here."
- Conflict: the branch has no Config API round trip test. `SOLR-12916-TESTING.md` says a real add-listener round trip (TestConfigOverlay or TestSolrConfigHandler) "was not run". The only new test is `QuerySenderListenerTest.testFlatNameValueQueryFromConfigApi` (head line 51), a unit test of the static `convertQueriesToList`. `material/core-admin-round-1-answers.md` line 104 requires "a Config API round trip or a stated limit", not a round trip alone.
- Fix: "- Premise run: run `QuerySenderListenerTest.testFlatNameValueQueryFromConfigApi` on base production and at the head (the branch's vehicle; TESTING.md expects it to fail on base with size 0 and a missing q). Then either run a Config API round trip as a scratch test outside the branch, or record the stated limit that no round trip was run, per the Core admin round 1 answers, SOLR-12916 entry."

### 5. R1-6 SOLR-16499 (proof shape)

- Line: `gates/SOLR-16499.md`, "- Proof shape: production reverted to the branch's merge-base with the branch tests kept; the branch's new tests must fail there for the premise reason ...".
- Conflict: `ReplaceNodeAPITest.java` sets `requestBody.parallel` and `requestBody.timeout` (head lines 61 and 62). Those fields are declared in `solr/api/src/java/org/apache/solr/client/api/model/ReplaceNodeRequestBody.java` (head lines 61 and 68), which does not declare them at merge-base e2cdb2d7e8ae. Reverting it breaks compilation. `SOLR-16499-TESTING.md` fail-before says "revert ReplaceNode.java only" and expects message size 2 against 4.
- Fix: "- Proof shape: revert only `solr/core/src/java/org/apache/solr/handler/admin/api/ReplaceNode.java` to merge-base (per SOLR-16499-TESTING.md); keep `ReplaceNodeRequestBody.java` so the test compiles. `testParallelAndTimeoutAreForwardedToTheOverseerMessage` must fail on the message size (2 against 4), checked by failure content."

### 6. R1-7 SOLR-5262 premise (showing (a) has no vehicle)

- Line: `gates/SOLR-5262-premise.md`, "- Spec (the three showings the answers require): (a) a config using ${solr.core.ulogDir} with no ulogDir in core.properties fails on base; ...".
- Conflict: the branch's only test, `TestCoreDescriptorImplicitProperties` (testUlogDirDefaultsToDataDir at head line 33), covers showings (b) and (c). No file on the branch or in the job names a vehicle for (a). `material/core-admin-round-1-answers.md` (SOLR-5262 entry) uses the same bare wording.
- Fix: name the vehicle: "(a) a scratch core outside the branch whose solrconfig.xml uses ${solr.core.ulogDir} and whose core.properties omits ulogDir, loaded on base and at the head; record the error text. Not committed; no branch edits under this backlog."

### 7. R1-11 SOLR-11650 base run (production files at base)

- Line: `gates/SOLR-11650-baserun.md`, "- Run shape: ... Run that single method with the branch's tests in place and the production code at base (the pre-fix IndexFetcher path)."
- Conflict: the follower-details `leaderUrl` is redacted in `solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java` line 1041 (`URLUtil.redactUserInfo(fetcher.getLeaderCoreUrl())`) as well as in `IndexFetcher.java`. Reverting `IndexFetcher.java` alone leaves the details redaction in place, so the case passes on a partial base. The method lives in `org.apache.solr.handler.TestUserManagedReplicationWithAuth` (head line 189), not in `IndexFetcherLeaderUrlRedactionTest`, and the job names no class.
- Fix: "- Run shape: run `org.apache.solr.handler.TestUserManagedReplicationWithAuth#testFollowerDetailsRedactLeaderUrlPassword` (class confirmed by grep at e4f5e941cd8) with these three branch production files reverted to merge-base: `solr/core/src/java/org/apache/solr/handler/IndexFetcher.java`, `solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java`, `solr/solrj/src/java/org/apache/solr/common/util/URLUtil.java`. Keep the tests."

### 8. R2-5 SOLR-11356 (run class not named)

- Line: `gates/SOLR-11356.md`, "- Focused classes: claimant determines from the branch diff per the FQCN grep rule (copy the exact package and module from the test files on the branch; the receipt names the module as solrj-jetty ...".
- Conflict: the branch's only test file is `solr/solrj/src/test/org/apache/solr/client/solrj/impl/ConcurrentUpdateSolrClientTestBase.java`, an abstract base in module `solrj`. Copying the module from that file gives `:solr:solrj`, which is wrong for the Jetty run. The new test runs through `solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/ConcurrentUpdateJettySolrClientTest.java`.
- Fix: "- Focused classes: run `org.apache.solr.client.solrj.jetty.ConcurrentUpdateJettySolrClientTest` in `:solr:solrj-jetty` (the inherited `testRequestsWithDifferentCredentialsAreNotSentOnOneStream`). The base class in `solr/solrj` is not run on its own."
- Note, not a verdict item: `SOLR-11356-TESTING.md` warns the test may pass on main (NOT_PROVEN). The job's premise wording already handles a pass.

### 9. R2-6 SOLR-14187 (base cannot compile)

- Line: `gates/SOLR-14187.md`, "- Premise run: run the branch's tests against the branch's merge-base production and at the head. The premise holds only if the base run shows the wait dropping the per-request credentials and the head run shows them carried."
- Conflict: `CollectionAdminRequestAsyncAuthTest.java` line 51 calls the five-argument `waitForAsyncRequest`. Merge-base `CollectionAdminRequest.java` has only the three-argument static (line 1829), so the base run is a compile failure, which cannot show the premise by failure content.
- Fix: add after the premise line: "Expected base outcome: test compile failure at merge-base (five-argument overload absent; test line 51). Record it as INCONCLUSIVE by compile. The premise is then not shown by base content. Any base-compilable vehicle is a scratch file named in this job before the run."

### 10. R2-7 SOLR-10667 (no task or base named)

- Line: `gates/SOLR-10667.md`, "- Premise run (shape recorded in the receipt): assemble a distribution without the fix and show the LTR example directory missing, then assemble with the fix and show it present. Run the branch's test_modules.bats check as part of the same leg."
- Conflict: no Gradle task and no BATS invocation is named, and the base is not stated. Merge-base cabedd1d9680 has no LTR BATS check, because the branch adds it (`test_modules.bats` is in the branch diff). The distribution task is `:solr:packaging:assembleDist` (`solr/packaging/build.gradle` line 163). `SOLR_TIP` is set from `distDir` by the BATS integration (line 329).
- Fix: "- Premise run: (1) at merge-base cabedd1d9680 with `gradle/solr/packaging.gradle` reverted, run `./gradlew :solr:packaging:assembleDist` and confirm `modules/ltr/example` is absent in the distribution; the BATS file is copied from the head. (2) At the head, run the same task, then `bats solr/packaging/test/test_modules.bats` with `SOLR_TIP` set to the distribution directory; the test is 'ltr module ships its example directory'. Record both."

### 11. R2-9 SOLR-11678 (module text and proof compile)

- Line A: `gates/SOLR-11678.md`, "- Branch against its base cabedd1d968: 13 files: ... and the SSL configuration family in solrj with three test files."
- Conflict A: the SSL configuration files are in `solr/core` (`solr/core/src/java/org/apache/solr/util/configuration/...` and the three tests under `solr/core/src/test/...`). None is in solrj. The 13-file count matches the diff.
- Fix A: "... and the SSL configuration family in `solr/core` with three test files."
- Line B: `gates/SOLR-11678.md`, "- Proof shape (gate): production reverted to the branch's merge-base with the branch tests kept; ...".
- Conflict B: the head tests use `SSLConfigurations.SysProps.SSL_KEY_MANAGER_PASSWORD`, `getKeyManagerPassword()` and `EnvSSLCredentialProvider.EnvVars.SOLR_SSL_KEY_MANAGER_PASSWORD` (`SSLConfigurationsTest.java` lines 56 and 186 to 193, and the two provider tests). None exists at merge-base cabedd1d9680. Reverting production breaks compilation.
- Fix B: add: "The merge-base proof leg is a compile failure (new credential type and `getKeyManagerPassword` absent at base), recorded as INCONCLUSIVE by compile. The premise run (keystore) does not use these tests. Any base-compilable vehicle is named here before the run."

### 12. R2-11, R2-12, R2-13 SOLR-6430, SOLR-7119, SOLR-11700 (docs task not named)

- Line (each file): the checklist item "- [ ] documentation build of the changed page" and the "On completion" line that refers to "the documentation build result". No Gradle task appears in any of the three files.
- Conflict: the step is not runnable as written. The task exists at all three heads: `task buildLocalSite` in `solr/solr-ref-guide/build.gradle` (also present at the SOLR-17356 head).
- Fix (each file): name it: "documentation build: `./gradlew :solr:solr-ref-guide:buildLocalSite`, then confirm the changed page renders in the local site."

### 13. R2-14 SOLR-17356 (wording and expected outcome)

- Line A: `gates/SOLR-17356.md`, "- Job type: ... The branch is docs-only: solr/solr-ref-guide/modules/indexing-guide/pages/language-analysis.adoc (the Ukrainian language analysis page ...)" and "... checked against the current analysis factories" in the premise line, which calls it "the rewritten Ukrainian language analysis page".
- Conflict A: the branch makes four targeted edits in the Ukrainian section (module jar name, dictionary paragraph, two example attributes; 35 insertions and 4 deletions in the page). `reports/build-docs-misc-round-1-g4.md` (SOLR-17356 section) says "'rewritten' overstates it".
- Fix A: replace "the rewritten Ukrainian language analysis page" with "the Ukrainian section of the language analysis page, which the branch edits in four places".
- Line B: `gates/SOLR-17356.md`, "- Premise verification: ... If the rewrite's claims do not hold against the current factories, stop: record the outcome in receipts/SOLR-17356.md as NO GATE by finding ...". The job cites no expected outcome.
- Conflict B: `reports/build-docs-misc-round-1-g4.md` (SOLR-17356 section) already finds the premise wrong in two places: the default Ukrainian dictionary is already on the analysis-extras classpath at `ua/net/nlp/ukrainian.dict`, and the new `org/languagetool/resource/uk/ukrainian.dict` path is not in `morfologik-ukrainian-search`, so the branch's new dictionary attributes resolve only if an unlisted jar is added.
- Fix B: add: "Expected outcome from the audit (`reports/build-docs-misc-round-1-g4.md`, SOLR-17356): premise wrong in part; the new dictionary path does not resolve on current main. Confirm, then record NO GATE by finding unless the verification shows otherwise." Also apply the docs task fix in finding 12.

### 14. R2-15 SOLR-16322 (no failing vehicle)

- Line: `gates/SOLR-16322.md`, "- Premise exercise: run a deliberately failing test task twice, once with the base script and once with the branch's script, and compare the seed the failure summary prints."
- Conflict: the job names no failing task. Nothing on the branch or at head is deliberately failing. `SOLR-16322-TESTING.md` suggests `./gradlew :solr:core:beast -Ptests.dups=16 --tests "org.apache.solr.uninverting.TestUninvertingReader.testSortedSetIntegerManyValues"`. That method exists (head line 231 of TestUninvertingReader.java) but is not shown to fail. The failure summary the premise compares prints only on a failing run.
- Fix: name the vehicle: "a scratch failing test in a scratch copy (not committed), run with `./gradlew :solr:core:beast -Ptests.dups=N --tests <scratch FQCN>.<method>` under the base `gradle/testing/failed-tests-at-end.gradle` and then under the branch script. The beast task is in `gradle/testing/beasting.gradle`."

### 15. R2-16 SOLR-17722 (premise wording and vehicle)

- Line A: `gates/SOLR-17722.md`, "The ticket says the CrossDC module mirrors the update content type param." and "- Premise run: ... The premise holds only if the base run shows the content type param being mirrored as the ticket describes and the head run shows it corrected."
- Conflict A: the branch changes the mirrored `wt` (response writer) param: `SolrMessageProcessor.java` sets `CommonParams.WT` to `JAVABIN` in `prepareIfUpdateRequest`. `SOLR-17722-TESTING.md` and `reports/build-docs-misc-round-1-g5.md` (SOLR-17722 section, "CrossDC mirrors the update `wt` param") say the same.
- Fix A: replace "content type param" with "`wt` (response writer) param" in both lines.
- Line B: `gates/SOLR-17722.md`, "- Premise run: run the branch's tests against the branch's merge-base production and at the head ...".
- Conflict B: the only branch vehicle, `SolrMessageProcessorTest#handleItemForcesJavabinResponseWriter` (head line 149), is a mock-level test. `SOLR-17722-TESTING.md` says it "does not prove the ticket". The wire-level check named in `reports/build-docs-misc-round-1.md` (SOLR-17722 item, about line 111) is the vehicle the premise needs. The audit's own reading (`reports/build-docs-misc-round-1-g5.md`, SOLR-17722 section) is "premise fails by reading": SolrJ's `HttpSolrClient.initializeSolrParams` overwrites `wt` on the default path.
- Fix B: "Premise vehicle: the wire-level check in `reports/build-docs-misc-round-1.md` (a SolrJ test that sends `wt=json` and asserts the wire carries `javabin`). The mock test is not a premise vehicle. Expected outcome per `reports/build-docs-misc-round-1-g5.md`: premise fails by reading; NO GATE if the wire check confirms."

## Notes on CLEAR jobs (no change required)

- R1-3 SOLR-4502, R1-8 SOLR-9091, R1-9 SOLR-9382, R1-12 SOLR-10390, R1-13 SOLR-13705, R2-1 through R2-4 (SOLR-9852, 10882, 3498, 10364), R2-10 SOLR-12161: heads, classes, and packages match the branch tree; the named test methods exist at head; the TESTING.md or answers expectation matches the job. Checked specifically: 9091 line 295 comment and its replacement wording (`reports/replication-backup-round-1-g1.md`, finding 3); 9382 changelog title and the two over-length lines (`reports/replication-backup-round-1-g3.md`, findings 15 and 16); the TESTING.md packaging targets (`SOLR-4502-TESTING.md`, `SOLR-9091-TESTING.md`, `SOLR-9382-TESTING.md`, `SOLR-10390-TESTING.md`) all exist at head.
- R1-10 recount (gates/SOLR-9865-17287-recount.md): CLEAR as a runnable job. Expected-count note: at both heads the file has three `@Test` annotations (9865: lines 90, 182, 245; 17287: lines 90, 173, 269). The fourth `public void test...` method (`testBackupFailsMissingAllowPaths`, 9865 line 168, 17287 line 255) has no `@Test`, so a JUnit 4 run should report 3 testcases, not 4. Expect 3; if the XML shows 3, the 4 in both receipts and the 11 total in the SOLR-9865 receipt (which would be 10) are not reproduced.
- R2-8 SOLR-12347 (base SHA note): the job says "base bin/solr" without a SHA. The merge-base with `upstream/main` for this branch is `b6b2b8f10e98`, not `cabedd1d9680` used by most branches. Name the base in the job before the run. At `b6b2b8f10e98`, `solr/bin/solr` sets `SOLR_STOP_WAIT:=180` (line 154), consistent with the BATS expectation that the first assertion fails on base.
- R1-12 SOLR-10390: the BATS test prepends its own fake `lsof` (test line 122 to 126), so the "PATH without lsof" setup is an extra condition; it does not conflict with the job.
- R1-13 SOLR-13705: the only test file on the branch is `SSLConfigurationsFactoryTest.java`; base symbols `setCurrent` and `current` exist at 9b3a84b1c460, so a test-only copy compiles.
- R2-10 SOLR-12161: the branch's new block (BasicAuthIntegrationTest, around the `SOLR-12161` comment) expects `SolrException` with code 401, which matches the tip commit and the security audit's reading. Upstream main has a later edit to the same test file (SOLR-18234), outside the base run; no conflict for the merge-base run.

## Status observations outside the verdicts

- Backlog items 1 and 2 (R1-1, R1-2) are the only backlog lines whose jobs already ran. The round 1 claims directory holds only `claims/pool-vm2-gate-backlog-12998.md` and `claims/pool-vm2-gate-backlog-18391.md`, so the other 27 jobs are unclaimed as their files say.
- Round 2 jobs each carry the "Round 2: claimable once ..." line, as the backlog requires.
- `gates/SOLR-16630.md`, `gates/SOLR-18530.md`, `gates/SOLR-18531.md` and `gates/SOLR-18532.md` are outside both backlogs and were not audited here.
