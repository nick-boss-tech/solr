# PR 5241 overview replication errors: Linux gate round 2 completion (vm4)

Result: GREEN at the pinned head `3b36f32e93d513c58ff14c48db303a296218f84a` on `nick-boss-tech/solr:solr-16398-submit` (remote tip verified by ls-remote after the run). Receipt: `receipts/PR-5241-overview-errors.md` (round-2 section at the top; round-1 history kept below it).

Gate: `gates/pr-5241-overview-errors-round-2.md`, with superseding job `gates/pr-5241-overview-errors-round-2-f3-vm4.md` (pinned to `3b36f32e93`). Claim: `claims/pr-5241-overview-errors-gate-vm4-round-2.md`. Heartbeats: `hosts/vm4.md`.

## Results at 3b36f32e93

| Leg | Result | Evidence |
| --- | --- | --- |
| Tidy, worktree | rc 0, no files changed | `tidy-r2.log` |
| `git diff --check` 80ca9374f4..3b36f32e93 | rc 0 | `diffcheck-r2-final.txt` |
| `:solr:webapp:forbiddenApisTest` | rc 0 (round-1 finding repaired) | `forbidden-r2.log` |
| Focused `AdminUiCoreOverviewErrorsTest`, seed 5241C0DE | 3 tests, 0 failures, 0 errors, 0 skipped | `f1-focused-r2.log`, `f1-focused-r2.xml/` |
| Neighbor classes, seed 5241C0DE | 10 tests in 5 classes, 0/0/0 each: AdminUiCoreAdminStandaloneTest 5, AdminUiCoreAdminScreenTest 2, AdminUiReplicationStandaloneTest 1, AdminUiReplicationScreenTest 1, AdminUiSecurityAuthTest 1 | `f2-neighbors-r2.log`, `f2-neighbors-r2.xml/` |
| `env -u NODE_ENV ./gradlew check -x test` | BUILD SUCCESSFUL 18m 48s, 557 tasks, rc 0, including `buildLocalAntoraSite` and `checkSiteLinks`; `errorProneSkipped` as expected | `check-r2-clone.log` (standalone clone, see Limits) |
| `node --check core-overview.js` | rc 0 | `node-check-r2.txt`, and rc 0 in the clone |
| Fail-before at `aa8272ef08` with only the 3b36 test file | 3 tests, 3 failures, each at its intended UI wait (see below) | `f4-failbefore-r2.log`, `f4-failbefore-r2.xml/`, `f4-failbefore-r2-ui-artifacts/` |

Fail-before details (fresh XML). Each failure is a UI wait timeout at the line that asserts the UI, after `openHealthyOverview()` and after `failReplicationRefresh()` saw the injected status:
- `testReplicationForbiddenShowsPermissionFailure`: timed out waiting for `By.id: authz-failures` to contain "Permission failure" (test line 79).
- `testReplicationServerErrorShowsMessage`: timed out waiting for `#http-exception .exception` to contain "Replication status unavailable" (test line 89).
- `testReplicationUnauthorizedRedirectsToLogin`: timed out waiting for `#content #login #username` to be visible (test line 100).

Console logs for the three runs contain only `SEVERE` lines for the exact replication endpoint (403, 503, 401 respectively), which the gate allows.

## Hashes

- Candidate test file at `3b36f32e93`, git blob: `0dfcbf0a66900fed318bf89cad484f4af944a668`.
- Overlay file in the fail-before worktree, sha256: `ca0aec5810755d940e6336727cd53b6cc019e1ed2fd1c1070f8fbe60dd5f51ae` (same as the clone's copy).
- Controller `core-overview.js` at `3b36f32e93`, git blob: `ec7ce5c103614d4906fc812c5f209fa3d1a8bf0e`.

## Repairs and interruptions

1. Formatting repair commit `3b36f32e93` (Nick Shanin, no trailers): tidy wrapped one line of `AdminUiCoreOverviewErrorsTest.java` (2 insertions, 1 deletion). Pushed fast-forward from `56365b43ee` and verified by ls-remote. A superseding gate pins this head. Runs at `56365b43ee` are not proof.
2. Driver interruption at `56365b43ee` during tidy: the driver stopped with no end line and no tests run. Archived in `logs/.../interrupted-attempt-1-tidy/`.
3. Driver interruption at `3b36f32e93` at the start of the focused leg: no output, no tests run. Archived in `logs/.../interrupted-attempt-2-f1/`. The relaunch used `setsid` to detach it, and it completed.
4. Worktree layout failure: `check -x test` in the gate worktree failed at `:solr:solr-ref-guide:buildLocalAntoraSite` with "Local content source must be a git repository: /workspace/worktrees/gate-pr5241-r2". A git worktree's `.git` is a file, and Antora requires a directory. Reproduced directly with the Antora CLI. Not a candidate defect. Archived in `logs/.../check-attempt-worktree-layout-failure/`. Rerun in a standalone shallow clone at the same SHA (`/workspace/worktrees/gate-pr5241-r2-clone`), which passed.
5. Round-1 history (three browser setup attempts, forbidden-API failure at `80ca9374f4`) is preserved unchanged in the round-1 logs and in the receipt.

## Limits

- The check leg ran in a standalone shallow clone at `3b36f32e93`, not in the gate worktree (repair 4). Its tree was clean before and after.
- `git diff --check` for the fork range ran in the worktree, which has the full history; the clone is shallow and could not check `80ca9374f4`.
- The browser driver setting was passed as `JAVA_TOOL_OPTIONS=-Dwebdriver.chrome.driver=...` on each browser launch with `--no-daemon`. The Gradle XML does not record system properties, so the proof that the property reached the forked test JVM is that the browser tests started with no Selenium Manager refusal and all tests passed.
- Browser: Chrome for Testing 156.0.8078.4 headless shell, chromedriver 156.0.8078.4 linux-arm64. JDK Temurin 21.0.12.1, Node v22.23.4, Gradle 9.7.0. `NODE_ENV` unset for every leg (host shell has `NODE_ENV=production`), disclosed.
- No Windows build or test was run. No PR, comment, JIRA, or contributor-branch action was taken. The PR body and public comments are unchanged.

## Upstream follow-up draft (not posted; Nick's decision)

PR 5241 follow-up at fork head `3b36f32e93`, on top of the reviewed head `80ca9374f4`. It changes the new `AdminUiCoreOverviewErrorsTest` fixture to write the JSON error body as UTF-8 bytes through `getOutputStream()`, which removes the forbidden `ServletResponse.getWriter()` call that the repo's forbidden-API check rejects, and wraps one line that tidy reformatted. No suppressions, assertion changes, or production changes. On Linux with JDK 21 and Chrome/chromedriver 156: the three replication tests pass, the five neighboring admin UI classes pass (10 tests), `check -x test` passes, and against the original base `aa8272ef08` the same three tests fail at their intended UI checks.
