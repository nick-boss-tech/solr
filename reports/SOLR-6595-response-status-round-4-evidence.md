# SOLR-6595 response-status round 4 — gate evidence

**Verdict: GREEN at the exact pinned candidate F.** The detached tidy preflight, Error Prone compilation, all five focused positive classes, root check, and five required negative proof legs completed. The negative legs compiled and failed at their intended response-status assertions; none was an environment or compilation failure.

## Candidate and host

The candidate is `nick-boss-tech/solr:solr-6595-submit` at `58dae6adb336b70d7b909e853e3dfa36de4cf091` (F), parent `fb93b62a252fb09d90084abd80b904db512e2e1b` (F0), with upstream base `ad26fdf2c6bee8168eae043b94833f05643d1995`. The live fork ref was re-read after the proof and remained exactly F. The clean detached tidy preflight at F returned exit 0: `tidy clean at 58dae6adb336b70d7b909e853e3dfa36de4cf091`.

The gate ran on the reused `phone-agent-2` Linux x86_64 alias with JDK 21 and Gradle 9.7.0. Gradle ran through the shared `sweep-run.sh` lock; `NODE_ENV` was unset and each prescribed build used `--no-daemon --no-build-cache`. A live inode monitor sampled `/workspace` once per second and stopped the process group at or below 15% free. The lowest observed headroom over all legs was 73.95% free; the stop threshold was never approached.

## Candidate proof at F

| Leg | Command | Result | Evidence |
| --- | --- | --- | --- |
| Tidy preflight | `./gradlew tidy` in a detached clean checkout at F | Clean, exit 0 | `/workspace/gates/logs/phone-agent-2/solr6595-response-status-round-4-preflight/preflight.log` |
| Error Prone compile | `./gradlew --no-daemon --no-build-cache :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` | Exit 0; `BUILD SUCCESSFUL`, 18 tasks | `solr6595-round4-compile.log` |
| Focused positive tests | `:solr:core:cleanTest :solr:core:test` for `SolrCoreResponseStatusTest`, `TestCollectionAPIs`, `DeleteNodeAPITest`, `ReloadCollectionAPITest`, and `PostRequestDecorationFilterTest`, seed `6595C0FFEE0001` | 16 tests, 0 failures, 0 errors, 0 skipped | Fresh XML: 2 + 5 + 4 + 4 + 1 tests in `solr6595-round4-positive.xml/`; log `solr6595-round4-positive.log` |
| Root check | `./gradlew --no-daemon --no-build-cache check -x test` at pristine F in a standalone clone | Exit 0 | `solr6595-round4-root-check.log`; clone had a `.git` directory so Antora could run |

The positive XML includes the legacy failure, empty-failure, and exception-precedence cases; both DeleteNode HTTP-wire assertions and its async case; both `SolrCoreResponseStatusTest` status cases; and the nonzero Jersey header-to-HTTP mapping case. All ran rather than being skipped. The tests are in `TestCollectionAPIs.java:106-143`, `ReloadCollectionAPITest.java:76-93`, `SolrCoreResponseStatusTest.java:32-53`, `DeleteNodeAPITest.java:126-192`, and `PostRequestDecorationFilterTest.java:46`.

## Five required negative proof legs

Each leg used a separate worktree, the pinned seed, `cleanTest`, a fresh XML directory, the pool runner, and a live inode monitor. Every mutation used `git diff --check`; the mutation worktree changed only the named source path, while the selected test file’s F blob was unchanged. “Expected nonzero” means Gradle returned 1 because the targeted assertion failed, not because compilation or setup failed.

| Leg | Isolated change and preserved test blob | Observed intended failure | Evidence |
| --- | --- | --- | --- |
| Parent P, legacy regression | Started at P `2b5a1d174f0471edb8280207ba877a4d7eb6e619`; overlaid only F’s `TestCollectionAPIs.java`. Test blob `5954461d79bc518c0808ce12084187d8b4b4007a`. | `testLegacyMoveReplicaFailureSetsResponseStatus`: expected header status 500, got 0. Test source compiled; 1 test, 1 failure, 0 errors/skips. | `solr6595-round4-parent.{log,driver.status,inode-monitor.log,code.diff,manifest.txt}` and `solr6595-round4-parent.xml/` |
| Shared failure predicate at F | Changed only `CollectionsHandler.hasFailedSubResponses` to return false. Source blob `f4e743ea5b013be618dbda47b96754add1efea54` → `38ac25ff73b51976afa06087dec8933f40370901`; unchanged test blob `0db091bd5c75790d181b10505bc4653a8f35a147`. | `ReloadCollectionAPITest.testFailedSubRequestReturnsServerErrorStatus`: expected 500, got 0. `compileJava` and `compileTestJava` ran; 1 test, 1 intended failure, no errors/skips. | `solr6595-round4-detection.{log,driver.status,inode-monitor.log,code.diff,manifest.txt}` and `solr6595-round4-detection.xml/` |
| V1 response-header copy at F | Removed only the `SolrCore.postDecorateResponse` override-to-header branch. Source blob `d8d8fc142b5d68b2faaf137aca7785b93fcf188f` → `16153a3b15ec43ed0049ebfd9228fd3945351b55`; unchanged test blob `dec0090ae1c2a13ee0a5cf0369d7cad56940490b`. | `SolrCoreResponseStatusTest.testHttpStatusOverrideIsAddedToResponseHeader`: expected 500, got 0. Both compile tasks ran; 1 test, 1 intended failure, no errors/skips. | `solr6595-round4-header.{log,driver.status,inode-monitor.log,code.diff,manifest.txt}` and `solr6595-round4-header.xml/` |
| V1 HTTP status at F | Removed only `HttpSolrCall.writeResponse`’s override-to-HTTP branch. Source blob `475594aac4aa59bbe53663da2a570350f1bd2e46` → `1832f9c60289faef1af89e7a60afe15efd551437`; unchanged test blob `e7e67e74eaf8530cbe92fef947c8c2a4431e3f1e`. | `DeleteNodeAPITest.testDeleteNodeFailureReturnsHttp500`: expected HTTP 500, got 200; the assertion message showed body `responseHeader.status: 500` and the failure detail remained. Both compile tasks ran; 1 test, 1 intended failure, no errors/skips. | `solr6595-round4-v1-http.{log,driver.status,inode-monitor.log,code.diff,manifest.txt}` and `solr6595-round4-v1-http.xml/` |
| Native V2 HTTP status at F | Removed only `PostRequestDecorationFilter`’s nonzero-header-status mapping. Source blob `d288d611b3ecb84ead18e63faa4ef2c972747165` → `5fb87de04af2adc9016227773064c85eecc59044`; unchanged test blob `e7e67e74eaf8530cbe92fef947c8c2a4431e3f1e`. | The test sends V1 before native V2. The V1 request passed; the subsequent native V2 assertion expected HTTP 500 but got 200, with body `responseHeader.status: 500` and failure detail preserved. Both compile tasks ran; 1 test, 1 intended failure, no errors/skips. | `solr6595-round4-v2-http.{log,driver.status,inode-monitor.log,code.diff,manifest.txt}` and `solr6595-round4-v2-http.xml/` |

The shared failure predicate and its Admin API caller are at `CollectionsHandler.java:328-345` and `AdminAPIBase.java:221-224`; the V1 body header branch is `SolrCore.java:2962-2977`; the V1 wire branch is `HttpSolrCall.java:809-835`; the native V2 mapping is `PostRequestDecorationFilter.java:55-72`. The `DeleteNodeAPITest` request order and assertions are in `DeleteNodeAPITest.java:161-177`.

## Limits and retained evidence

Success, empty-container behavior, payload preservation, and exception precedence passed as positive coverage pins; they were not each separately mutation-proven. SolrJ response-parser exception-payload storage is outside this patch; the wire tests use the raw response parser and assert the HTTP status and JSON body. Proof is Linux x86_64 only; no Windows run was part of this gate. No PR, Jira, public comment, merge, or upstream write was made.

All logs, fresh XML, status files, inode-monitor logs, mutation diffs, and per-leg manifests are retained locally under `/workspace/gates/logs/phone-agent-2/solr6595-response-status-round-4/`. The positive checkout and standalone root-check clone remain clean at F. Each negative worktree remains isolated at F (or P for the parent leg) with only its recorded local mutation/overlay.
