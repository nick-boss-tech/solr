# PR 5241 round-2 evidence closeout (vm4, records only)

Answers `assignments/pr-5241-round-2-evidence-closeout.md`. No test was run for this report. Source of every excerpt: the existing logs under `/workspace/gates/logs/vm4/pr-5241-overview-round-2/`. Head `3b36f32e93d513c58ff14c48db303a296218f84a`, seed `5241C0DE`, browser setting `-Dwebdriver.chrome.driver=/workspace/tools/chromedriver-156.0.8078.4/chromedriver-linux-arm64/chromedriver` (passed with `JAVA_TOOL_OPTIONS` on each launch, `--no-daemon`).

## 1. Property in the forked test JVM

The Java launcher prints `Picked up JAVA_TOOL_OPTIONS` once for each JVM it starts. The line right after `> Task :solr:webapp:test` is the forked test worker's launcher output. Line 1 of each log is the Gradle client, so it does not count.

| Log | Test-worker launcher line | Line 1 (Gradle client) |
| --- | --- | --- |
| `f1-focused-r2.log` | line 170, after `> Task :solr:webapp:test` at line 169 | line 1 |
| `f2-neighbors-r2.log` | lines 90 and 91, after `> Task :solr:webapp:test` at line 89 | line 1 |
| `f4-failbefore-r2.log` | line 191, after `> Task :solr:webapp:test` at line 190 | lines 1, 3, 5 |

Exact text, `f1-focused-r2.log` line 170:
`Picked up JAVA_TOOL_OPTIONS: -Dwebdriver.chrome.driver=/workspace/tools/chromedriver-156.0.8078.4/chromedriver-linux-arm64/chromedriver`

The XML does not record system properties, so this log line is the only observation of the property in the test JVM.

## 2. Test totals (fresh JUnit XML, seed 5241C0DE, SHA 3b36f32e93)

| Run | Gradle result | XML totals |
| --- | --- | --- |
| Focused `AdminUiCoreOverviewErrorsTest` (`f1-focused-r2`) | `:solr:webapp:test (SUCCESS): 3 test(s)` at line 172; `BUILD SUCCESSFUL in 6m 29s` at line 180 | 3 tests, 0 failures, 0 errors, 0 skipped |
| Neighbors (`f2-neighbors-r2`) | `:solr:webapp:test (SUCCESS): 10 test(s)` at line 94; `BUILD SUCCESSFUL in 2m 22s` at line 115 | AdminUiCoreAdminScreenTest 2, AdminUiCoreAdminStandaloneTest 5, AdminUiReplicationScreenTest 1, AdminUiReplicationStandaloneTest 1, AdminUiSecurityAuthTest 1; all 0 failures, 0 errors, 0 skipped |

## 3. Fail-before (`f4-failbefore-r2`, base `aa8272ef08`, only the 3b36 test file overlaid)

`:solr:webapp:test (FAILURE): 3 test(s), 3 failure(s)` at line 915; `BUILD FAILED in 8m 51s` at line 944. Assertion messages from `f4-failbefore-r2.xml/` (fresh XML):

- `testReplicationForbiddenShowsPermissionFailure`: `Timed out waiting for text containing 'Permission failure' at By.id: authz-failures` (test line 79).
- `testReplicationServerErrorShowsMessage`: `Timed out waiting for text containing 'Replication status unavailable' at By.cssSelector: #http-exception .exception` (test line 89).
- `testReplicationUnauthorizedRedirectsToLogin`: `Timed out waiting for visible element at By.cssSelector: #content #login #username` (test line 100).

Each failing line comes after `openHealthyOverview()` and after `failReplicationRefresh()` has seen the injected status. Screenshots, page source, and console logs are in `f4-failbefore-r2-ui-artifacts/`. Console `SEVERE` lines name only the exact replication endpoint, with 403, 503, and 401 respectively.

## 4. Root check and clean standalone clone

- Clone: `/workspace/worktrees/gate-pr5241-r2-clone`, shallow clone of `nick-boss-tech/solr:solr-16398-submit`. `git rev-parse HEAD` returns `3b36f32e93d513c58ff14c48db303a296218f84a`; `git status --porcelain` is empty.
- Check: `env -u NODE_ENV ./gradlew check -x test` in the clone. `BUILD SUCCESSFUL in 18m 48s` at line 4508 of `check-r2-clone.log`; `errorProneSkipped` at line 29, which is expected because Error Prone is not enabled. Driver status: `check-r2-clone rc=0 2026-10-27T05:55:55Z`, `tree-after-check-changes: 0`.
- Earlier in the worktree, `:solr:webapp:forbiddenApisTest` passed (`forbidden-r2.log`), and `git diff --check` from `80ca9374f4` to `3b36f32e93` passed (`diffcheck-r2-final.txt`).

## 5. Limits

- The worktree check run (`check-attempt-worktree-layout-failure/`) failed because Antora rejects a git worktree as its content source. It is kept as history and is not the proof for the check leg.
- Logs are retained, not deleted. Clone and worktree directories are scratch and are not evidence.
- Receipt history is unchanged: `receipts/PR-5241-overview-errors.md` keeps round 1.
