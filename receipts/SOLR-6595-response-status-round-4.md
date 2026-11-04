# SOLR-6595 response-status round 4 gate receipt (GREEN)

Host: `phone-agent-2` alias. Candidate: `nick-boss-tech/solr:solr-6595-submit` at `58dae6adb336b70d7b909e853e3dfa36de4cf091` (F), parent `fb93b62a252fb09d90084abd80b904db512e2e1b` (F0), upstream base `ad26fdf2c6bee8168eae043b94833f05643d1995`. The live fork ref remained exactly F after proof.

Job: `gates/SOLR-6595-response-status-round-4.md`. Claim: `claims/pool-phone-agent-2-gate-SOLR-6595-response-status-round-4.md`. Evidence report: `reports/SOLR-6595-response-status-round-4-evidence.md`.

## Commands and results

| Leg | Result |
| --- | --- |
| Detached tidy preflight | `tidy clean at 58dae6adb336b70d7b909e853e3dfa36de4cf091`, exit 0. |
| Error Prone | `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` passed, exit 0; `BUILD SUCCESSFUL`, 18 tasks. |
| Focused positive tests | Five pinned classes, seed `6595C0FFEE0001`: 16 tests, 0 failures, 0 errors, 0 skipped. The required legacy failure/empty/exception, DeleteNode V1/V2 and async, response-header, and Jersey status-map cases executed. |
| Root check | `./gradlew --no-daemon --no-build-cache check -x test` passed at pristine F in a standalone clone with a `.git` directory. |
| Negative proofs | All five required independent legs compiled and reached the intended assertion. Parent-P legacy regression: expected 500, got 0. Shared-predicate mutation: expected 500, got 0. V1 header-copy mutation: expected 500, got 0. V1 HTTP branch mutation: expected HTTP 500, got 200 while the JSON body retained status 500 and failure detail. Native V2 mapping mutation: the V1 request passed, then native V2 expected HTTP 500 but got 200 while its JSON body retained status 500 and failure detail. Each fresh XML reports 1 test, 1 intended failure, 0 errors, 0 skipped; each Gradle result was rc 1 for that expected assertion failure. |

All Gradle invocations used the shared runner and lock with `NODE_ENV` unset and `--no-daemon --no-build-cache`. The inode monitor sampled once per second and would stop the process group at or below 15% free. Minimum recorded free-inode headroom was 73.95%; no stop occurred.

Logs, fresh XML, inode monitors, mutation/overlay patches, and source/test blob manifests are retained under `/workspace/gates/logs/phone-agent-2/solr6595-response-status-round-4/`. The exact-head preflight log is `/workspace/gates/logs/phone-agent-2/solr6595-response-status-round-4-preflight/preflight.log`. Full results and code-path references are in the evidence report named above.

## Limits and actions

The positive proof is Linux x86_64/JDK 21 only; no Windows run was part of this gate. Success, empty-failure, payload-preservation, and exception-precedence cases are positive coverage pins, not separate mutation witnesses. SolrJ exception-payload storage is outside this patch. No PR, Jira, public comment, merge, or upstream write was made. The following PR text is a draft only and has not been posted.

## PR description draft (not posted)

**Solr now reports collection-operation failures consistently across V1 and V2.** When a distributed collection command returns a non-empty per-node failure list, the response carries HTTP 500 while preserving its success and failure details. Empty failure containers remain successful, and real exceptions retain precedence. The focused tests verify the response-header status, the actual V1 and native V2 HTTP codes for DeleteNode, and async behavior. SolrJ’s normal parsing differences remain outside this change; the wire tests inspect the raw response status and body.
