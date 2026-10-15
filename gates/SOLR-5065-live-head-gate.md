# Gate job: SOLR-5065 gate at the live head, with the suggester pin test's fail-before verdict

- Branch: solr-5065-submit. Head pinned at filing: `ab894a996c8aa9825c4ac5af38536aec1ee865eb` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing; matches the head in `audits/update-processing/SOLR-5065.md`). Merge-base with upstream main: `0cc328310f8f` (as recorded in the audit; not the merge-base of the other Group C branches).
Status: OPEN
- Capability tags: gate.
- Job type: full hardening gate at the pinned head, whose proof leg is the fail-before verdict for the suggester pin test. No implementation step. Pushes only if the gate itself produces a commit (see On green).

## Why this job exists

`TESTING.md` (2026-10-08 decisions) directed: add the suggester inference pin test to this branch now, with its fail-before run. The pin test was added at `ab894a996c8` (new `DefaultSchemaSuggesterTest.java`, asserting `pdouble` for `["4.5E+10", "1.0e3"]` and `["4.5E10"]`, and `string` for `["4.5E+10", "abc"]`; `DefaultSchemaSuggester` reaches the branch's `parsePossibleDouble` helper change). The 2026-10-15 audit (`audits/update-processing/SOLR-5065.md`, finding 3) records the pin test as addressed in code with its fail-before run not yet recorded: the newest CI run covers `c0a0ce1b8d9`, the head before the pin test commit, and no readable record runs the pin test at `ab894a996c8` or against base. The audit's blocking item is exactly this: the gate for the live head, with a fail-before verdict for `DefaultSchemaSuggesterTest`, recorded before certification. The head is otherwise ready.

## Run shape

- Intended host: any capable Linux host, under the WORKFLOW.md claim rules (claim first under `claims/`, first push wins, hourly heartbeat). Windows never claims build work.
- Gradle route: the claiming host's route per its file in `hosts/` (on vm1, `~/workspace/tools/solr-gradle.sh` and never `./gradlew`; on vm2, per `hosts/vm2.md`). Copy `gradle.properties` into the worktree before any Gradle run.
- All Gradle steps run serially behind the host's test-queue flock. Never two Gradle builds at once on a host.
- Test counts come from fresh JUnit XML only. Seed: `5065C0FFEE5065`. Module: `:solr:core`.

## Steps, in order

1. Claim under `claims/` (WORKFLOW.md), then confirm the base: `git ls-remote fork refs/heads/solr-5065-submit` must equal `ab894a996c8aa9825c4ac5af38536aec1ee865eb`. If it has moved, stop and record the new tip in this file; do not gate an unrecorded head.
2. Changelog YAML parse with a real YAML parser of the fragment the branch adds, `changelog/unreleased/SOLR-5065-parse-double-exponent-plus.yml`. It must parse.
3. Tidy, with the GATE INVALID guard: run tidy for the branch's module scope; restore `gradle/libs.versions.toml` afterwards; then check `git status`. If tidy modified anything beyond that restore, the run is GATE INVALID: stop and record what tidy changed in this file. If a tidy commit is needed, commit it (Nick Shanin identity, no trailer), re-run the gate on the committed tree, and treat the new head as the gated head everywhere below.
4. Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`. rc must be 0.
5. Fail-before proof, judged by content, with the verdict recorded for `DefaultSchemaSuggesterTest`: run the branch's `DefaultSchemaSuggesterTest.java` against merge-base production (`0cc328310f8f`). The class is new on this branch, so the branch's test file is added to the base tree for this leg; say so in the record. The pass condition is that the pin test fails on base by content: on base the `E+` sample is not numeric, so the suggester infers `string` where the test asserts `pdouble`. A pass on base, or a failure from compilation or setup, is not proof; record the actual outcome and the assertion text either way. This verdict is the deliverable `TESTING.md` asks for; a head run without it does not discharge this job.
6. Focused tests at the head, counted from fresh JUnit XML: `org.apache.solr.handler.designer.DefaultSchemaSuggesterTest` and `org.apache.solr.update.processor.ParsingFieldUpdateProcessorsTest`. Confirm each class's exact package by grep on the branch before composing the run command, and compose the run in a separate step from the grep. Record tests, skipped, failures, and errors per class.
7. Module check: `:solr:core:check -x test`, rc 0.

## On green

1. Refresh `receipts/SOLR-5065.md` with a section for this gate: the gated head, the changelog parse result, the tidy guard result, the compile and module check results, the step 5 fail-before verdict for `DefaultSchemaSuggesterTest` stated as its own line, and the step 6 counts with the seed and run date, all from the fresh JUnit XML and the gate log.
2. The branch backs Nick's open PR #5071. If the gate produced no commit, the fork tip does not move and no push or body edit is owed. If a tidy commit moved the head, push it plainly (fast-forward, no force) to `fork` `solr-5065-submit` under the standing authorization for pushes to Nick's own open PRs (2026-10-14), verify the new tip by `git ls-remote`, and re-point PR #5071's body citations and Proof to the new head (read the body through the API before and after the edit).
3. Mark this job DONE with the gated head and the outcome, and close the claim.

## On failure

- Record the failing step, the failing test and seed if a test ran, and the log path in this file and in the receipt. A gate that executed tests and failed is never relaunched by the claimant; the main side investigates first (WORKFLOW.md). A run that never executed tests because of an environment refusal may be relaunched once, with the refusal recorded.
