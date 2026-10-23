# Gate job: SOLR-16155 (value-bearing causes in update errors)

Status: CLAIMED (claims/pool-phone-agent-2-gate-SOLR-16155-phase-cause-redaction.md)

- Branch: `solr-16155-submit`. Gated head: `df0abc65cfe0a6b8ee0695ee48f50023e7f2814a` (tidy repair head, fork tip read back on 2026-10-23). Original assigned head: `323bafea9e097d05e46f2f088e5034f6fce171ec`. If the tip moves before another repair push, stop and record the new tip here.
- Prior gated tree: `ab394c8d916b6452a35bfcb6ff7002c41ab19ebd`, gated at `53196640988ae04b247dee19abfc074b024335a7` then squashed unchanged to prior PR head `9d1d0e5df9d91d5b18bc62c277c6baff5faee69c`. Those results do not cover this new head.
- Merge base with upstream/main: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.
- Gate ID: `SOLR-16155-phase-cause-redaction`. Claim file: `claims/pool-phone-agent-2-gate-SOLR-16155-phase-cause-redaction.md`.
- Intended host: any capable Linux host. Capability tags: gate.
- Module: `:solr:core`; test helper change in `:solr:test-framework`.
- Focused classes: `org.apache.solr.update.DocumentBuilderTest`, `org.apache.solr.handler.loader.JavabinLoaderTest`, `org.apache.solr.schema.DenseVectorFieldTest`, `org.apache.solr.schema.RankFieldTest`, `org.apache.solr.update.processor.TolerantUpdateProcessorTest`, `org.apache.solr.handler.RequestHandlerBaseTest`, `org.apache.solr.update.SolrCmdDistributorTest`, `org.apache.solr.schema.TestLateInteractionVectorFieldInit`.
- Seed: `16155C0FFEE23`.
- Why: Nick directed a patch and re-gate after the October 23 review. The old head scrubbed outer messages but attached original value-bearing causes, which RequestHandlerBase logs at ERROR and v1 500 response traces serialize. This commit removes causes from the changed document/field-construction and javabin wrappers and logs javabin IOException details only at TRACE. A real update ERROR log test and v1/v2 response-and-log assertions cover the output boundaries; a TRACE test covers debugging access. No Gradle, formatting tool or test ran on the Windows implementation host. Status: Implemented, Proof Pending.

Links: Ticket or parent: SOLR-16155. Category: 1, Update processing and atomic updates. Siblings: upstream PR #1151 covers the same ticket; SOLR-16155-review-pass-1 through -3 and receipts/SOLR-16155.md are historical evidence. Supersedes: the prior head's verification for the current public PR; does not overwrite the completed historical gate jobs.

## Diff scope

Against base `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb` (additions/deletions):

| File | Added | Deleted |
| --- | --- | --- |
| changelog/unreleased/SOLR-16155-no-field-values-in-update-errors.yml | 7 | 0 |
| solr/core/src/java/org/apache/solr/handler/loader/JavabinLoader.java | 20 | 1 |
| solr/core/src/java/org/apache/solr/schema/DenseVectorField.java | 4 | 3 |
| solr/core/src/java/org/apache/solr/schema/FieldType.java | 4 | 3 |
| solr/core/src/java/org/apache/solr/schema/RankField.java | 9 | 2 |
| solr/core/src/java/org/apache/solr/schema/StrFloatLateInteractionVectorField.java | 4 | 3 |
| solr/core/src/java/org/apache/solr/update/DocumentBuilder.java | 26 | 36 |
| solr/core/src/test/org/apache/solr/handler/loader/JavabinLoaderTest.java | 73 | 0 |
| solr/core/src/test/org/apache/solr/schema/DenseVectorFieldTest.java | 60 | 26 |
| solr/core/src/test/org/apache/solr/schema/RankFieldTest.java | 16 | 3 |
| solr/core/src/test/org/apache/solr/update/DocumentBuilderTest.java | 78 | 13 |
| solr/core/src/test/org/apache/solr/update/processor/TolerantUpdateProcessorTest.java | 4 | 8 |
| solr/test-framework/src/java/org/apache/solr/util/LogListener.java | 5 | 0 |

The new commit against 9d1d0e5df9d changes 12 files (+115/-46); TolerantUpdateProcessorTest is unchanged in this commit but remains part of the PR. No other source file is in repair scope.

## What to run

Use the host's standard gate runner under its normal lock. Do not run the build tool directly. Check out the head first. The single-class request-gate.py template cannot express this multi-class gate; this job uses the full gate-job template and the documented one-file coordination Contents API route.

- Step 0: parse `changelog/unreleased/SOLR-16155-no-field-values-in-update-errors.yml`; retain author Nick Shanin and no nick line.
- Step 1: tidy. Apply any tidy output under WORKFLOW.md, Gate repairs, commit and fast-forward push any repair, record the original and new head/tree, then run the full gate on the resulting head. A green result must cover the committed and pushed formatted tree.
- Step 2: force Error Prone main and test compilation for `:solr:core`, including the changed test-framework dependency. Named tasks: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`. Record whether compilation tasks actually executed.
- Step 3, proof leg: in a separate proof worktree keep the new tests and the new LogListener.trace helper, but restore only the six changed production files (DocumentBuilder, JavabinLoader, FieldType, DenseVectorField, RankField, StrFloatLateInteractionVectorField) from the previous PR head `9d1d0e5df9d91d5b18bc62c277c6baff5faee69c`. Run exactly `org.apache.solr.update.DocumentBuilderTest.testFailedUpdateDoesNotLogFieldValue` and `org.apache.solr.handler.loader.JavabinLoaderTest.testIOExceptionDoesNotLeakDocumentContents`, seed `16155C0FFEE23`. Expect two test failures: the rendered RequestHandlerBase ERROR Throwable still contains the synthetic marker `not-a-number-secret`, and v1 ResponseUtils serialized error info still contains `secret customer value` in the IOException cause. Confirm those are assertion failures in the two named methods, with the value-bearing cause/output in the log; a compile failure or harness failure is not proof. The loader's failing listener may also report unconsumed baseline log events during close; record this secondary failure, and require the primary message-content assertion. Run with response traces enabled (`solr.hideStackTrace=false` in the test JVM); the tests assert traces are present, so hiding them is not an acceptable pass.
- Step 4, focused head run: restore a clean head worktree, run `:solr:core:cleanTest`, then the eight focused classes listed above at the seed. Count fresh JUnit XML immediately after the run. Require all executable tests to pass; record skips with their reasons. Specifically confirm both boundary tests pass, DocumentBuilderTest.testFieldErrorDetailsAreAvailableAtTrace sees the value and original NumberFormatException only in the TRACE event, status-code assertions stay 400/500 as appropriate, and the handler/distributor compatibility tests pass. The late-interaction init suite checks the neighboring field type; this is not a wider audit of every update error path.
- Step 5: `:solr:core:check -x test` and `:solr:test-framework:check -x test`. Record each exit status separately. The helper is a new TRACE factory matching existing LogListener factories; validate the changed helper as well as core.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Cause handling | decided: original value-bearing causes stay only in TRACE events | The changed wrappers intentionally omit raw cause chains from public exceptions. Do not reattach them to satisfy old message expectations. The current tests were updated to this policy. | Windows implementation under Nick's October 23 patch direction |
| Diagnostic compatibility | decided: record reduced cause metadata/detail | HTTP codes remain as before; root-error-class and nested diagnostics for these wrappers can change. Core tragedy is checked from writer state, and client/distributor transport retry classification is unchanged. Require the named compatibility suites. No global exception/logging redesign in this gate. | Windows implementation |
| Tidy or mechanical compile/lint repair | default: WORKFLOW.md Gate repairs | Push each authorized repair, record old/new head and files, then one full retry from step 0 on the new tree. Never count proof for an uncommitted tidy tree. | Standing pool rule |
| Test failure outside repair scope, or failed retry | decided: hand back | Record exact task, test, seed, failure text, fresh XML and attempts. Do not alter expected values, delete tests, reattach raw causes, or rerun an unchanged tree. | Standing pool rule |
| Proof differs from the two stated failures | decided: record and stop | No test edits to manufacture the expected result. A trace-disabled test JVM must be corrected before the proof run; absence of the positive trace assertion is not proof. | Windows implementation |
| Tip moves before claim or before a repair push | decided: stop | Record live tip and hand back; do not reset or force-push. | Standing pool rule |
| External communication | decided: no public writes | No PR body edit, comment, Jira action, email, PR state change or merge. The existing PR description still cites the old proof. Report the new receipt so Nick can authorize its update after proof. | Workspace AGENTS.md |

## Execution record

- Claim and `CLAIMED` status were pushed atomically at coordination commit `6c9a60492fc9347ef447b23dd98955c7dea1c11e` and read back from the live branch.
- Assigned head `323bafea9e097d05e46f2f088e5034f6fce171ec` and the 13-file diff scope against `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb` were confirmed. Fork tip remained unchanged before the tidy repair push.
- Preflight attempt 1 exited 2 before clone or tidy because the host lacked `/workspace/.env` and `/workspace/tmp`; no build or tests ran. Attempt 2 used the authorized process-scoped credential and a scratch directory, exited 3, and produced the tidy patch at `/workspace/gates/logs/phone-agent-2/preflight/SOLR-16155-phase-cause-redaction-attempt-2/tidy.patch`.
- Tidy repair commit: `df0abc65cfe0a6b8ee0695ee48f50023e7f2814a`, parent `323bafea9e097d05e46f2f088e5034f6fce171ec`, tree `ae47de910e065fe76d6d24763215224a11a11353`. It changes only `JavabinLoaderTest.java`, `DenseVectorFieldTest.java`, and `RankFieldTest.java` formatting. The pushed fork tip reads back as the repair commit.
- Phase 1 completed on the repaired head under the standard runner. Step 0 parsed the changelog; author Nick Shanin is retained and no `nick` key exists. Step 1 tidy exited 0 with no diff. Step 2 Error Prone compilation exited 0; both `:solr:core:compileJava` and `:solr:core:compileTestJava` executed, with 14 tasks executed and 4 up to date.
- Step 3 used a separate proof worktree at seed `16155C0FFEE23`, restoring only the six production files from prior head `9d1d0e5df9d91d5b18bc62c277c6baff5faee69c`; tests and `LogListener.trace` remained at the gated head. `DocumentBuilderTest.testFailedUpdateDoesNotLogFieldValue` had one expected assertion failure at line 164; its ERROR Throwable log includes `not-a-number-secret` and `NumberFormatException`. `JavabinLoaderTest.testIOExceptionDoesNotLeakDocumentContents` had one expected assertion failure at line 173 in the v1 serialized error-info check; `-Dsolr.hideStackTrace=false` was picked up, and the raw log includes `IOException: simulated failure for secret customer value`. Its JUnit XML records the suppressed `LogListener.close` assertion for one unconsumed event; the primary line-173 assertion is present. See `phase1-proof-verdict.json` and the two proof logs/XML under `/workspace/phone-agent-2-logs/SOLR-16155-phase-cause-redaction/attempt-1/`.
- Phase 1's wrapper pause after step 3 created an empty `4-head.log` placeholder; it was documented in `4-head-placeholder-note.json` and removed before phase 2, with no test output discarded.
- Phase 2 attempted step 4 on `/workspace/solr-16155-repair` at the gated head. The coordinator sent SIGINT after inode availability fell below the 15% safety threshold. `4-head.log` shows `:solr:core:cleanTest UP-TO-DATE`, compilation completed, and `:solr:core:test` began. `4-head-strict-verdict.json` records the Gradle child return code 130 and no fresh XML (`xml_files: []`); its 0-test parser summary means no XML was available to count, not a test pass. The standard `state.json` has no completed `4-head` entry. `wrapper-error-head.txt` records the missing-class/XML error; `phase2-process.rc` records wrapper exit 1. No step 5 task ran. Logs remain under `/workspace/phone-agent-2-logs/SOLR-16155-phase-cause-redaction/attempt-1/`.
- The coordinator's stop measurement was 118103/837408 free inodes (14.1%); the latest read at 2026-10-23T18:58:53Z was 118032/837408 (14.0949%). No Gradle process remains. `/workspace/solr-16155-proof` (detached) and `/workspace/solr-16155-repair` (`solr-16155-submit`) are clean at `df0abc65cfe0a6b8ee0695ee48f50023e7f2814a`. No files were deleted during or after the interruption.
- This is an environment safety pause, not a verified code-level test failure. The VM2 lane coordinator is the next actor: record the capacity investigation, then resume from step 4 only after safe inode headroom is restored. Reuse verified steps 0-3 and preserve the interrupted logs; no retry or cleanup was done by this host.

## Retry resumption and capacity investigation
- Resumed: 2026-10-23T19:25:36Z by phone-agent-2 under Nick's direction to reclaim the job if unclaimed. Live coordination tip `7cda32f5781b6c60141ffff11a70dc06524a60db` had no VM2 or other actor claim for this gate. This reactivates the existing phone-agent-2 claim; only step 4 remains to be run, and the interrupted attempt-1 evidence stays preserved.
- Capacity checked at 2026-10-23T19:25:36Z: root, `/workspace`, and `/home/ubuntu/.gradle` all share `/dev/root` with 837,408 total inodes; 227,668 are available (27.1872%). The 15% guard floor is 125,612, leaving 102,056 inodes above the floor. Available disk space is 31 GiB. The six completed worktrees previously removed with Nick's approval account for the documented recovery; this resumed run has deleted no worktrees, logs, build outputs, shared caches, or other files.
- No Gradle process was active at preflight. The proof and head worktrees are clean at `df0abc65cfe0a6b8ee0695ee48f50023e7f2814a`, tree `ae47de910e065fe76d6d24763215224a11a11353`. The normal gate lock and 15% inode guard will govern resumed tasks.
- The preserved attempt-1 directory contains an interrupted `4-head.log` and strict verdict, so continuation evidence will use a separate `attempt-2` directory. Only the verified step 0-3 state and proof evidence will be copied; attempt-1 artifacts will not be overwritten.

## On green

1. Add a new top section to `receipts/SOLR-16155.md` for this gate ID, with exact gated head, tree, date/host, logs, step results, proof test names and failure evidence, and fresh focused counts. Preserve the old gate history.
2. Record all repairs, including original and final pushed SHA. Verify the fork ref and public PR head match the gated SHA.
3. Mark this job DONE and the claiming host's claim DONE in the same push as the receipt. No public text changes in this job.

If any step cannot be verified, record the limitation and stop under the appropriate failure decision.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog parse
- [x] step 1 tidy clean, or committed and pushed repair recorded
- [x] step 2 Error Prone compilation actually executed
- [x] step 3 both boundary regressions fail on the prior PR production code
- [ ] step 4 not verified: SIGINT interrupted `:solr:core:test` before fresh XML; no focused counts exist
- [ ] step 5 core and test-framework module checks
- [ ] fork and PR heads match the gated head
- [ ] receipt written
- [ ] job DONE and claim DONE

## Deliverable and limits

- One fast-forward coordination push containing the receipt and DONE marks, plus any authorized ticket repair commits.
- Windows static review: diff --check passed; outbound file list and diff reviewed. Linux evidence is partial: steps 0-3 are recorded; step 4 was interrupted before fresh XML, step 5 was not run, and no receipt or green gate exists.
- No force push, no upstream source write, no PR/comment/JIRA/email action. Commit as Nick Shanin without attribution trailers. No em dashes.
