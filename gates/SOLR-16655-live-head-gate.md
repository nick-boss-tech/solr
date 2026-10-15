# Gate job: SOLR-16655 gate at the live head (decided descent fix)

- Branch: solr-16655-submit. Head pinned at filing: `5e2317443f4116a9e97330b0d6ce175cf44b54df` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing; matches the head in `audits/update-processing/SOLR-16655.md`). Merge-base with upstream main: `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Job type: full hardening gate at the pinned head. No implementation step. Pushes only if the gate itself produces a commit (see On green).

## Why this job exists

`TESTING.md` ("Decisions on the four remaining calls (2026-10-09)") resolved the wider-descent question: "Gate the descent: descend into a field's child documents only when the selector selects that field", and records that re-gate follows the fix. The fix is in the head: `5e2317443f4` guards the descent in `FieldMutatingUpdateProcessor.mutateDocument` with `selector.shouldMutate(fname)` and adds `testTrimFieldsRecurseIntoNestedDocumentsOnlyWhenParentSelected`; `b201a57fb3e` set the changelog type to `changed`, as decided. The last recorded gate is at `aa7898d972a` (2026-10-07 records), which predates both commits. The 2026-10-15 audit (`audits/update-processing/SOLR-16655.md`) records that no gate covers `5e2317443f4` and that the descent test has been read, not run. This gate is the re-gate the decision calls for.

## Run shape

- Intended host: any capable Linux host, under the WORKFLOW.md claim rules (claim first under `claims/`, first push wins, hourly heartbeat). Windows never claims build work.
- Gradle route: the claiming host's route per its file in `hosts/` (on vm1, `~/workspace/tools/solr-gradle.sh` and never `./gradlew`; on vm2, per `hosts/vm2.md`). Copy `gradle.properties` into the worktree before any Gradle run.
- All Gradle steps run serially behind the host's test-queue flock. Never two Gradle builds at once on a host.
- Test counts come from fresh JUnit XML only. Seed: `16655C0FFEE16655`. Module: `:solr:core`.

## Steps, in order

1. Claim under `claims/` (WORKFLOW.md), then confirm the base: `git ls-remote fork refs/heads/solr-16655-submit` must equal `5e2317443f4116a9e97330b0d6ce175cf44b54df`. If it has moved, stop and record the new tip in this file; do not gate an unrecorded head.
2. Changelog YAML parse with a real YAML parser of the fragment the branch adds, `changelog/unreleased/SOLR-16655-field-mutating-urps-nested-docs.yml`. It must parse, and its `type` must read `changed`.
3. Tidy, with the GATE INVALID guard: run tidy for the branch's module scope; restore `gradle/libs.versions.toml` afterwards; then check `git status`. If tidy modified anything beyond that restore, the run is GATE INVALID: stop and record what tidy changed in this file. If a tidy commit is needed, commit it (Nick Shanin identity, no trailer), re-run the gate on the committed tree, and treat the new head as the gated head everywhere below.
4. Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`. rc must be 0.
5. Fail-before proof, judged by content: run the live head's `FieldMutatingUpdateProcessorTest` against the pre-fix branch production at `b201a57fb3e5` (the changelog-type commit, before the descent guard; the descent test was added by `5e2317443f4`, so the branch's test file goes in place over that tree). The pass condition is that `testTrimFieldsRecurseIntoNestedDocumentsOnlyWhenParentSelected` fails there by content: without the guard, the unselected parent's child documents are mutated. Record the per-test outcomes either way; failures from setup or compilation do not count as proof.
6. Focused tests at the head, counted from fresh JUnit XML: `org.apache.solr.update.processor.FieldMutatingUpdateProcessorTest` and `org.apache.solr.update.processor.ParsingFieldUpdateProcessorsTest` (the second covers the labelled-child case, `testParseDateNestedLabelledChildDocuments`). Confirm each class's exact package by grep on the branch before composing the run command, and compose the run in a separate step from the grep. Record tests, skipped, failures, and errors per class.
7. Module check: `:solr:core:check -x test`, rc 0.

## On green

1. Refresh `receipts/SOLR-16655.md` with a section for this gate: the gated head, the changelog parse result, the tidy guard result, the compile and module check results, the step 5 proof outcome by content, and the step 6 counts with the seed and run date, all from the fresh JUnit XML and the gate log.
2. The branch backs Nick's open PR #5093. If the gate produced no commit, the fork tip does not move and no push or body edit is owed. If a tidy commit moved the head, push it plainly (fast-forward, no force) to `fork` `solr-16655-submit` under the standing authorization for pushes to Nick's own open PRs (2026-10-14), verify the new tip by `git ls-remote`, and re-point PR #5093's body citations and Proof to the new head (read the body through the API before and after the edit).
3. Mark this job DONE with the gated head and the outcome, and close the claim.

## On failure

- Record the failing step, the failing test and seed if a test ran, and the log path in this file and in the receipt. A gate that executed tests and failed is never relaunched by the claimant; the main side investigates first (WORKFLOW.md). A run that never executed tests because of an environment refusal may be relaunched once, with the refusal recorded.
- Note for sequencing, not part of this job: SOLR-12705 conflicts textually with this branch in `FieldMutatingUpdateProcessor.java`; the audit records that the two PRs are sequenced and the second is rebased.
