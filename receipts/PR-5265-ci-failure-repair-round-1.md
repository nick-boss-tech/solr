# Receipt: PR 5265 CI failure repair, round 1 gate (phone agent)

Verdict: PARTIAL. Every leg except the Admin UI Selenium leg is proven at the exact candidate head. The Selenium leg was attempted twice and is blocked by this host's browser build, recorded below; it needs a host whose Chrome or Chromium a WebDriver session can launch directly.

- Job: `gates/pr-5265-ci-failure-repair-round-1.md`. Assignment: `assignments/pr-5265-ci-failure-repair-round-1.md` (Windows source patch, claim `claims/pr-5265-ci-failure-repair-round-1-windows.md`).
- Candidate: `16b0d50218bf82dc0d28158a63602c2d3da2ef3a` on `nick-boss-tech/solr:solr-16398-split-shard`, parent `009f2c60513636b4aba2c93f4a4137aa39ccea64`. Fork ref and live PR #5265 head both read at the candidate before the run and again after it.
- Scope at the candidate: `solr/webapp/web/js/angular/app.js` and `solr/core/src/test/org/apache/solr/handler/admin/TestApiFramework.java`.
- Host: phone agent VM (Linux, root). JDK 21.0.12.1+1 (vendored), Gradle 9.7.0 via the host's solr-gradle.sh, builds serialized through the host test-queue lock. Browser: Chromium 155.0.8059.80 at `/opt/meta-chromium/chrome`; ChromeDriver 155.0.8059.84, resolved and downloaded by Selenium Manager 4.47.0 before claiming. Fresh clone at the exact SHA: `~/workspace/solr-clones/5265-ci-gate`; logs in `~/workspace/tools/5265-ci-gate-logs/`.
- Window: 2026-11-02 21:18 to 21:25 UTC (main run), 21:38 to 21:41 UTC (Admin UI leg rerun).

## Legs

1. Tidy: rc=0, candidate tree clean afterwards. PASS.
2. Admin UI reproducer (`AdminUiCollectionScreensTest.testAnalysisScreenAnalyzesText`, seed 83E3932EA6612D4A, `-Ptests.selenium=true`, browser pointed at the host Chromium with `-Dtests.selenium.chrome.binary`): NOT PROVEN on this host.
   - Attempt 1 failed before any test at `:solr:webapp:verifyUiWasm`: the Gradle-downloaded `solr-ui-wasm.zip` arrived corrupted through this VM's egress relay (got SHA-256 `05079c46272d...`, expected `654494303e4a...`). Same host defect recorded on the PR 5107 gate (addendum at board 07bde60a9b).
   - Remedy, disclosed: the pinned zip was fetched with curl from nightlies.apache.org, verified byte-identical to the pinned SHA-256 (5,237,370 bytes), and seeded into the clone's `solr/webapp/build/ui-download/`, where the build's own local-copy check accepts it. Build directory only; the candidate tree was not touched.
   - Attempt 2: the build proceeded, the generated JS client bundle built, ChromeDriver started, and the test class failed in `@BeforeClass` with `SessionNotCreatedException: Chrome instance exited`. Cause, verified by direct launch outside the test: this host's only Chromium is the Hatch-managed build, which refuses to start without its harness-issued WebGL domain policy switches ("Hatch WebGL domain policy is required for browser startup"). A WebDriver launch cannot supply those switches, and `AdminUiTestBase` accepts no extra Chrome arguments. The named test's body never executed (JUnit XML: 2 tests, 2 failures, both the classMethod setup). A skipped or setup-blocked test is not proof, so this leg stays open for a host with a stock browser build.
3. `TestApiFramework.testFramework` at seed 4E30896B42ECC98D: 1 test, passed (step log `:solr:core:test (SUCCESS): 1 test(s)`; its JUnit XML was superseded by step 4's run of the same task, the log is the evidence). PASS. This proves the candidate's TestApiFramework repair at the CI seed.
4. Focused and neighboring suites at seed 16398C0DE, fresh JUnit XML: SplitShardMessageMappingTest 14/14, SplitShardAPITest 5/5, TestCollectionAPIs 2/2, CreateShardAPITest 6/6, DeleteShardAPITest 4/4, V2ApiIntegrationTest 13/13. Total 44 tests, 0 failures, 0 skipped. PASS.
5. Error Prone compile (`:solr:api`, `:solr:solrj`, `:solr:core` compileJava plus `:solr:core:compileTestJava`, `-Pvalidation.errorprone=true`): rc=0. PASS.
6. Module check `:solr:core:check -x test`: rc=0. PASS.
7. `git diff --check` clean; final tree at the candidate; fork ref and live PR head re-read at the candidate after the run. PASS.

## What remains

The Admin UI leg only: rerun leg 2 on a Linux host with a stock Chrome or Chromium build that ChromeDriver can launch directly (the rest of this receipt stands; the candidate tree needs no change). The `app.js` half of the repair is the part that rides on that leg; the `TestApiFramework` half is proven here.
