# Gate job: SOLR-7504 gate at the live head (every-value scan and null guard)

- Branch: solr-7504-submit. Head pinned at filing: `2fe06bfd917f193b3591dfa4c088ef0994279ed6` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing; matches the head in `audits/update-processing/SOLR-7504.md`). Merge-base with upstream main: `97d973814336101e12475558d7419321c743de79`.
Status: DONE. GATE GREEN at 2fe06bfd917f; the proof verdict by content is recorded in receipts/SOLR-7504.md (vm2 coordinator, rung 1, 2026-10-15). Earlier record: GATE ENDED 2026-10-15T22:11:05Z, not marked green, escalated to the main agent.
- Capability tags: gate.
- Job type: full hardening gate at the pinned head. No implementation step. Pushes only if the gate itself produces a commit (see On green).

## Why this job exists

Two commits past the last gated and CI-covered head (`e3fdc8eb58f`) change production code in `CountFieldValuesUpdateProcessorFactory.java`: `22b77196e66` (the counting scan now reads every value for operation maps, so a plain value ahead of an operation map is rejected instead of silently dropped; the changelog title was narrowed in the same commit) and `2fe06bfd917` (the scan is guarded against null-valued fields). The 2026-10-15 audit (`audits/update-processing/SOLR-7504.md`) records that no gate and no CI run covers either commit: the newest CI runs cover the parent of `e3fdc8eb58f`. Finding 3 of the audit is fixed in code but not gated. This gate is what certifies the live head.

## Run shape

- Intended host: any capable Linux host, under the WORKFLOW.md claim rules (claim first under `claims/`, first push wins, hourly heartbeat). Windows never claims build work.
- Gradle route: the claiming host's route per its file in `hosts/` (on vm1, `~/workspace/tools/solr-gradle.sh` and never `./gradlew`; on vm2, per `hosts/vm2.md`). Copy `gradle.properties` into the worktree before any Gradle run.
- All Gradle steps run serially behind the host's test-queue flock. Never two Gradle builds at once on a host.
- Test counts come from fresh JUnit XML only. Seed: `7504C0FFEE7504`. Module: `:solr:core`.

## Steps, in order

1. Claim under `claims/` (WORKFLOW.md), then confirm the base: `git ls-remote fork refs/heads/solr-7504-submit` must equal `2fe06bfd917f193b3591dfa4c088ef0994279ed6`. If it has moved, stop and record the new tip in this file; do not gate an unrecorded head.
2. Changelog YAML parse with a real YAML parser of the fragment the branch adds, `changelog/unreleased/SOLR-7504-count-values-atomic-update.yml`. It must parse.
3. Tidy, with the GATE INVALID guard: run tidy for the branch's module scope; restore `gradle/libs.versions.toml` afterwards; then check `git status`. If tidy modified anything beyond that restore, the run is GATE INVALID: stop and record what tidy changed in this file. If a tidy commit is needed, commit it (Nick Shanin identity, no trailer), re-run the gate on the committed tree, and treat the new head as the gated head everywhere below.
4. Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`. rc must be 0.
5. Fail-before proof, judged by content: run the live head's focused test code against the pre-fix branch production at `22b77196e662` (the every-value-scan head, before the null guard). The pass condition is that exactly the null test fails there: `testCountValuesNullValue` fails by content (the counting scan dereferences the values of a null-valued field), and the remaining tests in the run pass. Record the per-test outcomes either way; failures from setup or compilation do not count as proof.
6. Focused tests at the head, counted from fresh JUnit XML: `org.apache.solr.update.processor.FieldMutatingUpdateProcessorTest` and `org.apache.solr.update.processor.ParsingFieldUpdateProcessorsTest`. Confirm each class's exact package by grep on the branch before composing the run command, and compose the run in a separate step from the grep. The run must include `testCountValuesAtomicUpdatePlainValueFirst` (the plain-first pin for the every-value scan) and `testCountValuesNullValue`; record tests, skipped, failures, and errors per class.
7. Module check: `:solr:core:check -x test`, rc 0.

## On green

1. Refresh `receipts/SOLR-7504.md` with a section for this gate: the gated head, the changelog parse result, the tidy guard result, the compile and module check results, the step 5 proof outcome by content, and the step 6 counts with the seed and run date, all from the fresh JUnit XML and the gate log.
2. The branch backs Nick's open PR #5081. If the gate produced no commit, the fork tip does not move and no push or body edit is owed. If a tidy commit moved the head, push it plainly (fast-forward, no force) to `fork` `solr-7504-submit` under the standing authorization for pushes to Nick's own open PRs (2026-10-14), verify the new tip by `git ls-remote`, and re-point PR #5081's body citations and Proof to the new head (read the body through the API before and after the edit).
3. Mark this job DONE with the gated head and the outcome, and close the claim.

## On failure

- Record the failing step, the failing test and seed if a test ran, and the log path in this file and in the receipt. A gate that executed tests and failed is never relaunched by the claimant; the main side investigates first (WORKFLOW.md). A run that never executed tests because of an environment refusal may be relaunched once, with the refusal recorded.
- Note for sequencing, not part of this job: SOLR-12705 conflicts textually with this branch in `FieldMutatingUpdateProcessorTest.java`; the audit records that the two PRs merge in a fixed order and the conflict is resolved by hand, keeping this branch's rejection.
