# Gate job: SOLR-13943 recorded normal-mode run of the moved class on the stacked tree

- Branch: solr-13943-submit. Head pinned at filing: `ca443f6f680606b6c995ae86b88b4841b53addbf` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing; matches the head in `audits/update-processing/SOLR-13943.md`). The branch stacks on the SOLR-13696 repair: repair head `1d0b8a0a73cd` is in its ancestry.
Status: OPEN
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
