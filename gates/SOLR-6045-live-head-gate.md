# Gate job: SOLR-6045 gate at the live head (decided factory fix and post-gate commits)

- Branch: solr-6045-submit. Head pinned at filing: `04208ba7f0b45837738e2afe1c428292f9fecae3` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing; matches the head in `audits/update-processing/SOLR-6045.md`). Merge-base with upstream main: `97d973814336101e12475558d7419321c743de79`.
Status: DONE (GATE GREEN at 04208ba7f0b4, main-side verdict 2026-10-15). Run by vm2 2026-10-15T21:23:41Z to 21:47:15Z on head 04208ba7f0b45837738e2afe1c428292f9fecae3; changelog, tidy, compile-errorprone, focused head and `:solr:core:check -x test` all rc=0 (log: logs/vm2/queue-g6045-gate-20261015T212341Z.out). The proof leg returned rc=1 against base 97d973814336 and was escalated; the by-content verdict below is GATE GREEN. The gate produced no commit; no push.
- Capability tags: gate.
- Job type: full hardening gate at the pinned head. No implementation step. Pushes only if the gate itself produces a commit (see On green).

## Main-side verdict, 2026-10-15 (vm1): GATE GREEN

vm2's run left one item open: the step 5 proof leg returned rc=1 against merge-base 97d973814336 with no per-test detail recorded, so the verdict could not be read from the run record. The main side re-ran the proof leg on vm1 (branch test files in place against merge-base production, seed 6045C0FFEE6045, log g6045-proof-vm1.log) and counted from fresh JUnit XML:

- AtomicUpdatesTest: 32 tests, 6 failures, 0 errors, 1 skipped (the pre-existing testRemoveDateUsingDateType skip).
- AtomicUpdateProcessorFactoryTest: 6 tests, 1 failure, 0 errors, 0 skipped.

Every failure is the premise failing in the way step 5 records:

- testRepeatedAddFieldOfOperationsIsAtomicUpdate: assertion failure; on base, a repeated-operation field's getValue() is a collection, so isAtomicUpdate is false.
- testRepeatedAddFieldOfOperationsViaJavabin: the final query assertion fails; the update sent as javabin does not land as an atomic update on base.
- testMixedFieldPlainValueFirstIsRejected and testMixedFieldOperationMapFirstIsRejected: assertion failures; base has no mixed-field rejection in either order.
- testRepeatedOperationsWithPlainFieldStillMerge: expected [bbb, ddd] but was [{set=bbb}, {add=ddd}]; on base the operation maps are indexed as plain values.
- testAtomicUpdateWithChildDocuments: SolrException "Unknown operation for the an atomic update: id"; on base the merger reads the child document's own field names as operation names.
- testRepeatedOperationMapsPassThroughUnwrapped (factory test): expected the list of operation maps but was [{set=[{set=bbb}, {add=ddd}]}]; on base the factory wraps a collection of operation maps because it does not recognize it as already atomic. The control testPlainMultiValueFieldStillWrapped passed on base.

No failure came from setup, compilation, or an unrelated test; the branch test code compiles against merge-base production, so no proof test is inconclusive by compile. Focused head counts were also confirmed firsthand on vm1 at 04208ba7f0b4 (log g6045-head-vm1.log, same seed): AtomicUpdatesTest 32 tests, 0 failures, 1 skipped; AtomicUpdateProcessorFactoryTest 6 tests, 0 failures; rc=0. With vm2's green head legs, the gate is GATE GREEN at 04208ba7f0b4.

## Why this job exists

`TESTING.md` decided the factory defect on 2026-10-08 as option A: fix `AtomicUpdateProcessorFactory` in this branch before the PR, with a re-gate. The fix is in the head (`e06aa762485`, `isAlreadyAtomicUpdate`), and four commits followed it: `e4b77fa7ae5` (child-document guard in the merger), `8d1dd2bde06` (mixed plain and operation values rejected in either order), `bcae04d77bd` (null guard), and `04208ba7f0b` (changelog title). The 2026-10-15 audit (`audits/update-processing/SOLR-6045.md`) records that no gate and no CI run covers the live head: the newest CI run covers `dcdef50d700`, the head before all eight commits since the 2026-10-08 audit. The decided re-gate is this job.

## Run shape

- Intended host: any capable Linux host, under the WORKFLOW.md claim rules (claim first under `claims/`, first push wins, hourly heartbeat). Windows never claims build work.
- Gradle route: the claiming host's route per its file in `hosts/` (on vm1, `~/workspace/tools/solr-gradle.sh` and never `./gradlew`; on vm2, per `hosts/vm2.md`). Copy `gradle.properties` into the worktree before any Gradle run.
- All Gradle steps run serially behind the host's test-queue flock. Never two Gradle builds at once on a host.
- Test counts come from fresh JUnit XML only. Seed: `6045C0FFEE6045`. Module: `:solr:core`.

## Steps, in order

1. Claim under `claims/` (WORKFLOW.md), then confirm the base: `git ls-remote fork refs/heads/solr-6045-submit` must equal `04208ba7f0b45837738e2afe1c428292f9fecae3`. If it has moved, stop and record the new tip in this file; do not gate an unrecorded head.
2. Changelog YAML parse with a real YAML parser of the fragment the branch adds, `changelog/unreleased/SOLR-6045-atomic-update-repeated-field.yml`. It must parse.
3. Tidy, with the GATE INVALID guard: run tidy for the branch's module scope; restore `gradle/libs.versions.toml` afterwards; then check `git status`. If tidy modified anything beyond that restore, the run is GATE INVALID: stop and record what tidy changed in this file. A gate never passes on an uncommitted tree; if a tidy commit is needed, commit it (Nick Shanin identity, no trailer), re-run the gate on the committed tree, and treat the new head as the gated head everywhere below.
4. Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`. rc must be 0.
5. Fail-before proof, judged by content: run the branch's focused test code against merge-base production (`97d973814336101e12475558d7419321c743de79`, branch test files in place). The pass condition is that the branch's new assertions fail there for the recorded reasons: on base, a repeated-operation field's `getValue()` is a collection, so `isAtomicUpdate` is false and the repeated-add test fails; both mixed-order rejection tests fail because base has no such rejection; the child-document test fails on the "Unknown operation" shape. Failures from setup, compilation, or unrelated tests do not count as proof; record the per-test outcomes either way.
6. Focused tests at the head, counted from fresh JUnit XML: `org.apache.solr.update.processor.AtomicUpdatesTest` and `org.apache.solr.update.processor.AtomicUpdateProcessorFactoryTest`. Confirm each class's exact package by grep on the branch before composing the run command, and compose the run in a separate step from the grep. Record tests, skipped, failures, and errors per class.
7. Module check: `:solr:core:check -x test`, rc 0.

## On green

1. Refresh `receipts/SOLR-6045.md` with a section for this gate: the gated head, the changelog parse result, the tidy guard result, the compile and module check results, the step 5 proof outcomes by content, and the step 6 counts with the seed and run date, all from the fresh JUnit XML and the gate log.
2. The branch backs Nick's open PR #5077. If the gate produced no commit, the fork tip does not move and no push or body edit is owed. If a tidy commit moved the head, push it plainly (fast-forward, no force) to `fork` `solr-6045-submit` under the standing authorization for pushes to Nick's own open PRs (2026-10-14), verify the new tip by `git ls-remote`, and re-point PR #5077's body citations and Proof to the new head (read the body through the API before and after the edit).
3. Mark this job DONE with the gated head and the outcome, and close the claim.

## On failure

- Record the failing step, the failing test and seed if a test ran, and the log path in this file and in the receipt. A gate that executed tests and failed is never relaunched by the claimant; the main side investigates first (WORKFLOW.md). A run that never executed tests because of an environment refusal may be relaunched once, with the refusal recorded.
- Note for sequencing, not part of this job: SOLR-12703 changes the same `mergeDocHavingSameId` method; the audit records that the two PRs are sequenced, not merged cold.
