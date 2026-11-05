# Receipt section: SOLR-12613 (`solr-12613-pr-head-lifecycle-round-2`)

## Gate `solr-12613-pr-head-lifecycle-round-2` (phone-agent-2, 2026-11-05): **NOT GREEN** — one required F2 neighboring-selection test failed; gate remains OPEN / Implemented, Proof Pending

- **Ticket / parent PR:** SOLR-12613 / Apache Solr PR #5110. **Category:** gate-only. This receipt completes the exact-head round-2 execution and supersedes the round-1 actual-PR-head lifecycle execution only; it does not satisfy synthetic or combined jobs. The [round-1 lifecycle receipt](SOLR-12613-pr-head-lifecycle-round-1.md) and [prior Windows browser receipt](SOLR-12613-browser-verification-round-1.md) remain unchanged.
- **Claim and job:** [`claims/pool-phone-agent-2-gate-SOLR-12613-pr-head-lifecycle-round-2.md`](../claims/pool-phone-agent-2-gate-SOLR-12613-pr-head-lifecycle-round-2.md); [`gates/solr-12613-pr-head-lifecycle-round-2.md`](../gates/solr-12613-pr-head-lifecycle-round-2.md). The required new failure verdict is [`claims/pool-phone-agent-2-gate-SOLR-12613-pr-head-lifecycle-round-2-verdict-failed.md`](../claims/pool-phone-agent-2-gate-SOLR-12613-pr-head-lifecycle-round-2-verdict-failed.md).
- **Evidence:** [`round-2 evidence index`](SOLR-12613-pr-head-lifecycle-round-2-evidence/evidence-index.md), with fresh logs, JUnit XML, commands, headroom traces, failure diagnostics, and integrity hashes in [`SHA256SUMS`](SOLR-12613-pr-head-lifecycle-round-2-evidence/SHA256SUMS).

## Exact source and readbacks

- Candidate **F2** is `b0e096ae19ad637107227e1bc04e951b9023a680`; its only parent **F1** is `f02bb7bb06decce976c3ed2fdfbf5907764c2183`; F1's only parent is original base **P** `6b5d830132b941faa85856347099ade8b190be0e`. The live Apache PR #5110 is still OPEN at P, base `82abb86bb5b7326fe48c117e4de69981a43e4251`; the fork branch `nick-boss-tech/solr:pr-5110-core-menu-lifecycle` is still at F2.
- P tree: `cdd291ceed4c8414adf458cfa7aee2ad50bcfb64`; F2 tree: `be294a23520476ec198eaee7f56195513cbf96ac`. `P..F2` contains only `solr/webapp/web/js/angular/app.js` and `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java` (262 additions, 14 deletions total). `F1..F2` changes only the picker test (+1/-3); production `app.js` is unchanged between F1 and F2.
- The pre-claim readback and required F2/P pins are recorded in the immutable claim. Final live GitHub readback at `2026-11-05T21:37:20Z` still matched those source pins. Final tracked status was clean in the F2, P, and isolated fail-before worktrees; the fail-before checkout was restored to F2 `app.js` blob `d23e89b33a13eac833872a57f1b9d8ce10e82dea`, with the candidate test still exact F2 blob `d1ec6d38d0b5eb0e4fff712b17b9e1c8ebd54ee0`. See [final source verification](SOLR-12613-pr-head-lifecycle-round-2-evidence/support/final-source-verification.md).
- The round-2 claim remained ACTIVE before this finishing post; no competing round-2 execution claim was present. The board claim, receipt and verdict are separate add-only records. No source-branch commit/push, PR update, PR comment, or Jira action was made.

## Host and material execution conditions

Ubuntu 24.04.4 on registered Selenium-capable `phone-agent-2`; OpenJDK 21.0.12.1; Gradle wrapper 9.7.0; Chrome 155.0.8059.39 with cached ChromeDriver 155.0.8059.84; Node 24.22.1 and npm 11.19.1. Every Gradle leg used the serialized host lock and continuous live 15%-free-inode guard. The lowest recorded free-inode sample was 34.2235% during root check; no floor was approached. The lock was free after completion. `NODE_ENV` was unset by the guarded runner. No CA/truststore override variables were set.

The verified wasm archive used for compile was SHA-256 `654494303e4af3b8bfb26a83d250603f19033820295591d88db0597991ce8ad2`, matching the source pin. The earlier mismatched archive (`a1b20bc4aaf06563444ea38938dee4c2972cca1d265c125966a47e2fb6bffa29`) was preserved separately and not copied into the F2/P/fail-before caches. The generated JS client was built from F2's native API sources; the wasm artifact is not claimed as OpenAPI provenance. Details: [wasm provenance](SOLR-12613-pr-head-lifecycle-round-2-evidence/support/wasm-artifact-provenance.txt).

## Required proof results

### Changelog / tidy

- **Step 0:** Changelog YAML parse is not applicable: P..F2 changes only the two JS/test files above; no YAML changelog file changed. The existing feature entry was checked through the root build.
- **Detached pre-claim preflight:** exact F2 `tidy clean`, rc 0, 62 actionable tasks; finished `2026-11-05T21:01:43Z`. Logs: [preflight result](SOLR-12613-pr-head-lifecycle-round-2-evidence/preflight/preflight.log), [tidy log](SOLR-12613-pr-head-lifecycle-round-2-evidence/preflight/preflight-tidy.log).
- **Fresh F2 root tidy:** `./gradlew tidy`, rc 0, 62 actionable tasks, `2026-11-05T21:06:24Z`–`21:07:30Z`; tracked tree remained clean. [Log and headroom](SOLR-12613-pr-head-lifecycle-round-2-evidence/tidy/).

### Error Prone compile and JS client generation

- `./gradlew :solr:webapp:compileTestJava -Pvalidation.errorprone=true`: rc 0, `21:08:41Z`–`21:10:55Z` (26 executed, 4 up-to-date). The pinned `verifyUiWasm` input and Error Prone compilation passed. [Compile log and headroom](SOLR-12613-pr-head-lifecycle-round-2-evidence/compile/).
- `./gradlew :solr:webapp:js-client:generateJsClientBundle`: rc 0, `21:11:15Z`–`21:11:51Z` (9 executed, 5 up-to-date), generated from F2's native API source tree. [Generation log and headroom](SOLR-12613-pr-head-lifecycle-round-2-evidence/generation/).

### Picker runs

- Original P picker at seed `5110C0AE`: 4 tests, 0 failures/errors/skips, rc 0, `21:16:47Z`–`21:18:05Z`. [Fresh XML and log](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/p-picker/p-picker-seed-5110C0AE/).
- Full F2 `AdminUiNodePickerTest` at seed `5110C0AE`: **8/8 pass**, 0 failures/errors/skips, rc 0, `21:14:51Z`–`21:15:45Z`; no shared-setup timeout. Fresh XML order:
  1. `testCloudLandingShowsNodesAndEmptyNodeSelector`
  2. `testSystemInfoFailureStillRefreshesLocalCoreMenu`
  3. `testNodeSelectionSurvivesInAppNavigation`
  4. `testRestoredSelectionTargetsInitialCoreMenuRequest`
  5. `testSelectRemoteNodeUpdatesDashboardAndPersistsAcrossReload`
  6. `testCoreMenuKeepsNewestRefreshForSameNode`
  7. `testLoggingLevelsHidesNodeBadge`
  8. `testCoreMenuIgnoresLateResponseFromPreviousNode`

  The P order and complete F2 order are also preserved in [`picker-method-order.txt`](SOLR-12613-pr-head-lifecycle-round-2-evidence/support/picker-method-order.txt); F2 [XML, log and monitor](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/f2-picker/f2-picker-seed-5110C0AE/).

### Paired 17-method neighboring selections

The same selection comprised `AdminUiNodeScreensTest`, `AdminUiCoreAdminStandaloneTest`, the two logging-level methods, `AdminUiLoggingStandaloneTest`, and `AdminUiSecurityAuthTest`; each run has five fresh class XML files.

| Seed | P (original PR head) | F2 candidate | UTC windows |
|---|---:|---:|---|
| `5110C0AE` | 17/17, rc 0 | **16/17**, rc 1; one failure | P `21:18:17Z`–`21:18:47Z`; F2 `21:18:57Z`–`21:20:03Z` |
| `12613F00D` | 17/17, rc 0 | 17/17, rc 0 | P `21:22:23Z`–`21:22:54Z`; F2 `21:23:01Z`–`21:23:31Z` |

The sole current F2 failure is `AdminUiCoreAdminStandaloneTest.testAddCoreViaUi` at seed `5110C0AE`: `Timed out waiting for typing 'addedcore' at By.id: add_name`. It has 0 errors and 0 skips. The same seed's P selection passed, and F2 passes the full selection at `12613F00D`. The [F2 failure XML/log and captured page](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-5110C0AE/) are preserved, including [screenshot](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-5110C0AE/failure-debug/ui-failure-testAddCoreViaUi-001/screenshot.png) and [page HTML](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-5110C0AE/failure-debug/ui-failure-testAddCoreViaUi-001/page.html). The other three complete fresh run bundles are [P/5110](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/p-neighbors-5110C0AE/), [P/12613](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/p-neighbors-12613F00D/), and [F2/12613](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-12613F00D/).

### Combined fail-before at seed `5110C0AE`

In the isolated F2 fail-before checkout, only `app.js` was restored from P; the test stayed byte-identical to F2. The exact four-method combined selection ran `2026-11-05T21:25:18Z`–`21:26:36Z`: 4 tests, 4 assertion failures, 0 errors, 0 skips, rc 1. All four reached their intended explicit old-behavior assertions in the same run; there were no setup timeouts or browser/script errors:

- `testCoreMenuIgnoresLateResponseFromPreviousNode`: `A late response must not replace the current core menu expected:<[new]_core> but was:<[old]_core>`.
- `testCoreMenuKeepsNewestRefreshForSameNode`: `A late response must not replace the current core menu expected:<[new]_core> but was:<[old]_core>`.
- `testRestoredSelectionTargetsInitialCoreMenuRequest`: `Every initial core-menu request must target the restored node: [{target=null, url=/api/cores?indexInfo=false&_=1793913991503}, {target=127.0.0.1:45477_solr, url=/api/cores?indexInfo=false&_=1793913991516}]`.
- `testSystemInfoFailureStillRefreshesLocalCoreMenu`: `Failure to discover system info must not prevent a local core refresh`.

The combined [log and fresh XML](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/fail-before/f2-combined-5110C0AE/f2-combined-5110C0AE/), [P-to-F2 app.js overlay diff](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/fail-before/f2-combined-5110C0AE/appjs-restoration-from-P.diff), [overlay preflight](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/fail-before/f2-combined-5110C0AE/overlay-preflight.txt), and [restoration verification](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/fail-before/f2-combined-5110C0AE/restoration.txt) are preserved. The isolated checkout was restored to clean exact F2; no source repair was made.

### Root check

The normal F2 clone ran `./gradlew check -x test` at `21:27:21Z`–`21:31:09Z`, rc 0, **555 actionable tasks** (524 executed, 31 up-to-date), with clean tracked status after. The Antora local site and link check completed: 858 parsed files, 286 local links, 3,577 local anchor links, 0 errors, 0 warnings. `npmSetup` was skipped; Antora components downloaded successfully. npm reported deprecation notices and 7 audit vulnerabilities (6 moderate, 1 high), which did not fail the build. The root check explicitly excludes tests; no backend test is claimed. Its default Error Prone task was skipped outside CI, while the dedicated compile above enabled Error Prone explicitly. [Root-check log, environment and monitor](SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/root-check/f2-root-check/).

## Historical AddCore reconciliation and verdict

The older Windows synthetic-head selection was 16/17 at seed `12613F00D`, with the same `testAddCoreViaUi` timeout. That historical failure did not reproduce in the current P or F2 complete neighbor selections at `12613F00D`; the current P/`5110C0AE` selection also passed. F2 now has one exact-head AddCore timeout at `5110C0AE`. The cause remains unproven; this evidence does not establish whether the new occurrence is intermittent or candidate-related. The historical [receipt](SOLR-12613-browser-verification-round-1.md), [log](SOLR-12613-browser-verification-round-1-evidence/logs/positive-neighbors.log), [focused XML](SOLR-12613-browser-verification-round-1-evidence/xml/neighbors/TEST-org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.xml), and [screenshot](SOLR-12613-browser-verification-round-1-evidence/screenshots/neighbor-add-core-failure.png) remain unchanged and are not relabeled green.

All required round-2 proof was run. The candidate picker isolation repair is confirmed by the 8/8 F2 picker run and by the combined fail-before run reaching all four intended assertions. The gate nevertheless remains **NOT GREEN** because one required F2 neighboring selection failed. No blind retry was made. No source repair, fork branch update, PR update/comment, or Jira action is within this finishing post; an additional source repair would move F2 and requires owner direction and a superseding exact-head gate. A strict scan found no credential patterns; no explicit thread-leak message was found. See [evidence scan](SOLR-12613-pr-head-lifecycle-round-2-evidence/support/evidence-scan.md) and [thread-leak scan](SOLR-12613-pr-head-lifecycle-round-2-evidence/support/thread-leak-scan.md).

### Factual comment wording (not posted)

At F2 `b0e096ae19ad637107227e1bc04e951b9023a680`, the full 8-method picker suite passed and the combined fail-before run reached all four intended old-behavior assertions. The paired 17-method neighbor selection had one failure: `testAddCoreViaUi` timed out waiting for `#add_name` at seed `5110C0AE`; P at that seed and P/F2 at `12613F00D` passed. Therefore the exact-head local gate is **NOT GREEN / Proof Pending**. No source-branch or PR change and no public comment were made.
