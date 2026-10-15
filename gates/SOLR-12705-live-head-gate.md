# Gate job: SOLR-12705 gate at the live head, with the counter-level check the audit names

- Branch: solr-12705-submit. Head pinned at filing: `aa56b7b1be4c5491723c58f296e20c906d7b7334` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing; matches the head in `audits/update-processing/SOLR-12705.md`). Merge-base with upstream main: `0cc328310f8f` (as recorded in the audit).
Status: OPEN
- Capability tags: gate.
- Job type: full hardening gate at the pinned head, plus the counter-level check in step 6. No implementation step and no new test written under this job. Pushes only if the gate itself produces a commit (see On green).

## Why this job exists

The branch has a recorded GREEN gate at `8624b7c3238b` (2026-10-09: the production commit for `TESTING.md` decision (a), excluding counting processors from the atomic-operand path). The next commit, `aa56b7b1be4`, rewrote part of `FieldMutatingUpdateProcessorTest.java` (53 lines added, 17 removed) after that gate, replacing the counter outcome test with an opt-out test, so the counts in the GREEN record do not describe the live test file and no gate covers the live head. The 2026-10-15 audit (`audits/update-processing/SOLR-12705.md`) makes that its first blocking item.

The audit's second blocking item is the counter-level gap, and this job carries it as step 6: `TESTING.md` decision (a) says the counter exclusion ships "with a test". The test that pinned the counter's outcome (`testCountValuesAtomicUpdate`) was removed in `aa56b7b1be4`; the remaining pin (`testAtomicOperandsOptOut`) uses a generic function. This branch therefore pins no counter-level outcome for an atomic update; the counter's outcomes are tested on SOLR-7504 only.

## Run shape

- Intended host: any capable Linux host, under the WORKFLOW.md claim rules (claim first under `claims/`, first push wins, hourly heartbeat). Windows never claims build work.
- Gradle route: the claiming host's route per its file in `hosts/` (on vm1, `~/workspace/tools/solr-gradle.sh` and never `./gradlew`; on vm2, per `hosts/vm2.md`). Copy `gradle.properties` into the worktree before any Gradle run.
- All Gradle steps run serially behind the host's test-queue flock. Never two Gradle builds at once on a host.
- Test counts come from fresh JUnit XML only. Seed: `12705C0FFEE12705`. Module: `:solr:core`.

## Steps, in order

1. Claim under `claims/` (WORKFLOW.md), then confirm the base: `git ls-remote fork refs/heads/solr-12705-submit` must equal `aa56b7b1be4c5491723c58f296e20c906d7b7334`. If it has moved, stop and record the new tip in this file; do not gate an unrecorded head.
2. Changelog YAML parse with a real YAML parser of the fragment the branch adds, `changelog/unreleased/SOLR-12705-field-mutating-atomic-updates.yml`. It must parse.
3. Tidy, with the GATE INVALID guard: run tidy for the branch's module scope; restore `gradle/libs.versions.toml` afterwards; then check `git status`. If tidy modified anything beyond that restore, the run is GATE INVALID: stop and record what tidy changed in this file. If a tidy commit is needed, commit it (Nick Shanin identity, no trailer), re-run the gate on the committed tree, and treat the new head as the gated head everywhere below.
4. Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`. rc must be 0.
5. Fail-before proof, judged by content: run the live head's focused test code against merge-base production (`0cc328310f8f`, branch test files in place). The pass condition is that the tests pinning the atomic-operand behavior fail there by content, including `testAtomicOperandsOptOut`: base has no atomic-operand path and no opt-out, so the operands reach the mutator in their base shape. Record the per-test outcomes either way; failures from setup or compilation do not count as proof.
6. Focused tests at the head, counted from fresh JUnit XML: `org.apache.solr.update.processor.FieldMutatingUpdateProcessorTest` and `org.apache.solr.update.processor.ParsingFieldUpdateProcessorsTest`. Confirm each class's exact package by grep on the branch before composing the run command, and compose the run in a separate step from the grep. Record tests, skipped, failures, and errors per class. Counter-level check, in the same step: establish by grep what counter-level coverage this branch's own tree offers for the decided exclusion (a test that runs `CountFieldValuesUpdateProcessorFactory` through an atomic update and asserts the counted outcome). If such a test exists on this branch, it must be in the run and its count recorded. If none exists, the run cannot supply the check from this branch's tree, and the claimant records the gap plainly in the receipt and in this file, in these terms: the decided counter exclusion has no counter-level test on this branch; the counter's atomic-update outcomes are tested on SOLR-7504 only, so SOLR-12705 and SOLR-7504 must land together for the counter path to be tested. Recording the gap discharges this step; writing a new test does not belong to this job.
7. Module check: `:solr:core:check -x test`, rc 0.

## On green

1. Refresh `receipts/SOLR-12705.md` with a section for this gate: the gated head, the changelog parse result, the tidy guard result, the compile and module check results, the step 5 proof outcomes by content, the step 6 counts with the seed and run date, and the counter-level check's result or the recorded gap, all from the fresh JUnit XML and the gate log.
2. The branch backs Nick's open PR #5096. If the gate produced no commit, the fork tip does not move and no push or body edit is owed. If a tidy commit moved the head, push it plainly (fast-forward, no force) to `fork` `solr-12705-submit` under the standing authorization for pushes to Nick's own open PRs (2026-10-14), verify the new tip by `git ls-remote`, and re-point PR #5096's body citations and Proof to the new head (read the body through the API before and after the edit).
3. Mark this job DONE with the gated head and the outcome, and close the claim.

## On failure

- Record the failing step, the failing test and seed if a test ran, and the log path in this file and in the receipt. A gate that executed tests and failed is never relaunched by the claimant; the main side investigates first (WORKFLOW.md). A run that never executed tests because of an environment refusal may be relaunched once, with the refusal recorded.
- Note for sequencing, not part of this job: SOLR-16655 and SOLR-7504 both conflict textually with this branch (in `FieldMutatingUpdateProcessor.java` and `FieldMutatingUpdateProcessorTest.java` respectively); the audits record fixed landing orders for both pairs.
