# Gate job: SOLR-13943 recorded normal-mode run of the moved class on the stacked tree

- Branch: solr-13943-submit. Head pinned at filing: `ca443f6f680606b6c995ae86b88b4841b53addbf` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing; matches the head in `audits/update-processing/SOLR-13943.md`). The branch stacks on the SOLR-13696 repair: repair head `1d0b8a0a73cd` is in its ancestry.
Status: DONE. Normal-mode run GREEN at ca443f6f68 (receipts/SOLR-13943.md, top section): the moved class ran and passed, 1 of 1.
- Capability tags: gate.
- Job type: a gate-shaped recorded run at the pinned head, whose required deliverable is the normal-mode run of the moved class. No implementation step, no push, no rebase under this job.

## Standing facts this job states, not decides

- This branch is not submitted alone. It carries the SOLR-13696 repair (production changes to `TimeRoutedAlias.java` and `CreateAlias.java`, and the routed-alias test changes, including the removal of the base class's `@AwaitsFix(SOLR-13696)`) in the same history as the SOLR-13943 test move. The decided sequence (`TESTING.md`) is that the SOLR-13943 PR is rebased onto the SOLR-13696 PR and ships after or with it. No open PR backs this branch, and this job opens none.
- The original class, `TimeRoutedAliasUpdateProcessorTest`, keeps its class-level `@AwaitsFix(SOLR-13059)`, as the decision says. It is not part of this job's required run; in normal mode the whole class is skipped.

## Why this job exists

`TESTING.md` decided option 3 for SOLR-13943: move `testDateMathInStart` into a class without the class-level annotation, "proven by a normal-mode run before the PR", on the stacked tree. The move is built at the pinned head: the method now lives in `TimeRoutedAliasDateMathInStartTest`, which carries no `AwaitsFix` annotation, and the base class carries none at this head because the 13696 repair removed it. The 2026-10-15 audit (`audits/update-processing/SOLR-13943.md`, finding 2) records that no normal-mode run of the moved class at `ca443f6f680` is recorded anywhere readable; the only earlier run was on the pre-move head. This job supplies the recorded run the decision requires.

## Run shape

- Intended host: any capable Linux host, under the WORKFLOW.md claim rules (claim first under `claims/`, first push wins, hourly heartbeat). Windows never claims build work.
- Gradle route: the claiming host's route per its file in `hosts/` (on vm1, `~/workspace/tools/solr-gradle.sh` and never `./gradlew`; on vm2, per `hosts/vm2.md`). Copy `gradle.properties` into the worktree before any Gradle run.
- All Gradle steps run serially behind the host's test-queue flock. Never two Gradle builds at once on a host.
- Test counts come from fresh JUnit XML only. Seed: `13943C0FFEE13943`. Module: `:solr:core`.
- Normal mode means exactly that: no `-Dtests.awaitsfix=true` anywhere in the run. A run with the awaitsfix group enabled does not discharge this job.

## Steps, in order

1. Claim under `claims/` (WORKFLOW.md), then confirm the base: `git ls-remote fork refs/heads/solr-13943-submit` must equal `ca443f6f680606b6c995ae86b88b4841b53addbf`, and confirm `1d0b8a0a73cd` (the SOLR-13696 repair head) is an ancestor of the pinned head. If the tip has moved, or the repair head is not an ancestor, stop and record what was found in this file; do not run on an unrecorded tree.
2. Changelog YAML parse with a real YAML parser of the fragment the branch adds, `changelog/unreleased/SOLR-13943.yml`. It must parse.
3. Tidy, with the GATE INVALID guard: run tidy for the branch's module scope; restore `gradle/libs.versions.toml` afterwards; then check `git status`. If tidy modified anything beyond that restore, the run is GATE INVALID: stop and record what tidy changed in this file. This job makes no branch commit; a tree that is not tidy-clean at the pinned head is recorded as the outcome, not fixed here.
4. Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`. rc must be 0.
5. The recorded run, in normal mode, counted from fresh JUnit XML: `org.apache.solr.update.processor.TimeRoutedAliasDateMathInStartTest` (the moved class; required) plus the stack sanity classes in the same run, `org.apache.solr.update.processor.CategoryRoutedAliasUpdateProcessorTest`, `org.apache.solr.update.processor.DimensionalRoutedAliasUpdateProcessorTest`, and `org.apache.solr.handler.admin.api.CreateAliasAPITest`. Confirm each class's exact package by grep on the branch before composing the run command, and compose the run in a separate step from the grep. Record tests, skipped, failures, and errors per class. The moved class must show its test actually running (not skipped): a skipped moved class in normal mode means the annotation state is not what the decision requires, and that is recorded as the outcome.
6. Module check: `:solr:core:check -x test`, rc 0.
7. No fail-before proof leg. The premise (the moved class is skipped wholesale in normal mode on the unmoved tree) is established by the annotation state the audit verified; this job's deliverable is the positive normal-mode run at the pinned head.

## On the run proving unreliable

The moved test polls the cluster state provider for a race that has never been reproduced deterministically (audit finding 3). If the moved class fails or flakes in normal mode and the log ties the failure to the routed-alias creation timing, the claimant does not relaunch and does not tune the test under this job. The record (this file, the receipt, and the claim) states plainly: the normal-mode run at the pinned head is unreliable, and under the `TESTING.md` decision the branch waits on SOLR-13059. That statement, with the failing run's log and fresh JUnit XML cited, is a complete outcome for this job.

## On green

1. Refresh `receipts/SOLR-13943.md` with a section for this run: the pinned head, the changelog parse result, the tidy guard result, the compile and module check results, and the step 5 counts per class with the seed and run date, stating in so many words that the moved class ran and passed in normal mode (no awaitsfix group) on the stacked tree. All counts from the fresh JUnit XML and the gate log.
2. No push: no open PR backs this branch and the head does not move under this job. If any record elsewhere needs the run cited, the receipt section is the citation.
3. Mark this job DONE with the outcome, and close the claim.

## On failure

- Record the failing step, the failing test and seed if a test ran, and the log path in this file and in the receipt. A run that executed tests and failed is never relaunched by the claimant; the main side investigates first (WORKFLOW.md), subject to the unreliable-run section above for the moved class. A run that never executed tests because of an environment refusal may be relaunched once, with the refusal recorded.

## Outcome: DONE, run GREEN on vm1 (2026-10-16)

Executed by vm1 under claims/pool-vm1-gate-13943-stacked-run.md. Runner: ~/workspace/tools/g13943-stacked.sh behind the vm1 test-queue flock (acquired 2026-10-16T01:55:14Z); log ~/workspace/tools/g13943-stacked.log; fresh JUnit XML archived at ~/workspace/tools/g13943-stacked-xml/. Scratch worktree at the pinned head; no branch commit, no push, no rebase, as the job requires.

- [x] Step 1, base confirmed: `git ls-remote fork refs/heads/solr-13943-submit` = `ca443f6f680606b6c995ae86b88b4841b53addbf`, and `1d0b8a0a73cd` is an ancestor of the pinned head.
- [x] Step 2, changelog parse: `changelog/unreleased/SOLR-13943.yml` parses with a real YAML parser, rc 0.
- [x] Step 3, tidy guard: `:solr:core:tidy` rc 0; after restoring `gradle/libs.versions.toml` the tree was clean, so the run is not GATE INVALID and no tidy change needed recording.
- [x] Step 4, Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`, rc 0.
- [x] Step 5, recorded run in normal mode (no `-Dtests.awaitsfix=true` anywhere), seed `13943C0FFEE13943`, module `:solr:core`, counted from fresh JUnit XML: TimeRoutedAliasDateMathInStartTest tests=1, skipped=0, failures=0, errors=0 (the moved class ran and passed; it was not skipped); CategoryRoutedAliasUpdateProcessorTest tests=6, skipped=0, failures=0, errors=0; DimensionalRoutedAliasUpdateProcessorTest tests=2, skipped=0, failures=0, errors=0; CreateAliasAPITest tests=13, skipped=0, failures=0, errors=0. Gradle rc 0.
- [x] Step 6, module check: `:solr:core:check -x test`, rc 0.
- [x] Step 7: no proof leg, by the job's design.

The annotation state at the pinned head matches the decision the run supports: the moved class carries no AwaitsFix annotation, and the original class keeps its class-level `@AwaitsFix(SOLR-13059)`. Receipt section for this run is at the top of `receipts/SOLR-13943.md`.

Note for the record: vm2's coordinator recorded (2026-10-16T02:01Z, in claims/pool-vm2-gate-backlog-13943.md) that a separate vm2 normal-mode attempt the same night stopped at the compile step with no rc and no test run. That attempt executed no tests, so it neither discharges nor conflicts with this job; this vm1 run is the recorded run the job required.
