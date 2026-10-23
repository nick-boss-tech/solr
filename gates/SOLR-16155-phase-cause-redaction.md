# Gate job: SOLR-16155 (value-bearing causes in update errors)

Status: HANDED (to phone-agent-2 at owner direction 2026-10-23; it claims under claims/pool-phone-agent-2-gate-SOLR-16155-phase-cause-redaction.md and sets CLAIMED)

- Branch: `solr-16155-submit`. Head: `323bafea9e097d05e46f2f088e5034f6fce171ec` (fork ref and PR head read back on 2026-10-23). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated tree: `ab394c8d916b6452a35bfcb6ff7002c41ab19ebd`, gated at `53196640988ae04b247dee19abfc074b024335a7` then squashed unchanged to prior PR head `9d1d0e5df9d91d5b18bc62c277c6baff5faee69c`. Those results do not cover this new head.
- Merge base with upstream/main: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.
- Gate ID: `SOLR-16155-phase-cause-redaction`. Claim file: `claims/pool-HOST-gate-SOLR-16155-phase-cause-redaction.md`, replacing HOST with the claiming host.
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
| External communication | decided: no public writes | No PR body edit, comment, JIRA action, email, PR state change or merge. The existing PR description still cites the old proof. Report the new receipt so Nick can authorize its update after proof. | Workspace AGENTS.md |

## On green

1. Add a new top section to `receipts/SOLR-16155.md` for this gate ID, with exact gated head, tree, date/host, logs, step results, proof test names and failure evidence, and fresh focused counts. Preserve the old gate history.
2. Record all repairs, including original and final pushed SHA. Verify the fork ref and public PR head match the gated SHA.
3. Mark this job DONE and the claiming host's claim DONE in the same push as the receipt. No public text changes in this job.

If any step cannot be verified, record the limitation and stop under the appropriate failure decision.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean, or committed and pushed repair recorded
- [ ] step 2 Error Prone compilation actually executed
- [ ] step 3 both boundary regressions fail on the prior PR production code
- [ ] step 4 focused head tests and compatibility suites pass, counted from fresh XML
- [ ] step 5 core and test-framework module checks
- [ ] fork and PR heads match the gated head
- [ ] receipt written
- [ ] job DONE and claim DONE

## Deliverable and limits

- One fast-forward coordination push containing the receipt and DONE marks, plus any authorized ticket repair commits.
- Windows static review: diff --check passed; outbound file list and diff reviewed. No Linux proof for this commit exists yet.
- No force push, no upstream source write, no PR/comment/JIRA/email action. Commit as Nick Shanin without attribution trailers. No em dashes.

