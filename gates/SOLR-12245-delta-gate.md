# Gate job: SOLR-12245 delta review of the live head's new commit, the coordinator log check, and a gate at the live head

- Branch: solr-12245-submit. Head pinned at filing: `f325d5d0576e582d4488570eabdeabc7410a8a0f` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing; matches the head in `audits/update-processing/SOLR-12245.md`). Merge-base with upstream main: `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`. The commit under review: `f325d5d0576` ("do not name the host twice in the distributed update error"), on top of `4a93167458b`.
Status: CLAIMED (claims/pool-vm2-gate-backlog-12245.md)
- Capability tags: review, gate.
- Job type: a delta review of one commit, a record check the conditional decision requires, and a full hardening gate at the pinned head. No implementation step and no branch edit under this job. Pushes only if the gate itself produces a commit (see On green).

## Why this job exists

The round 36 delta review (verdict Nearly, 2026-10-08) covers `4a93167458b` only. One commit past it, `f325d5d0576`, changes `DistributedUpdateProcessor.describe()` (+12/-1) and adds a test (+31), and the 2026-10-15 audit (`audits/update-processing/SOLR-12245.md`) records that neither a review nor a gate covers the live head. Separately, the conditional decision in `TESTING.md` (2026-10-09, RESOLVED) keeps the target detail in the client response: if the coordinator's forward-to-leader log line already names the collection and shard, the item returns to Nick before any logs-only change; if it does not, the response detail stays and the duplicated host in `describe()` is fixed first. `f325d5d0576` takes the second path, but the audit records that the check's result was never recorded anywhere readable. This job supplies all three missing records.

## Part A: delta review of `f325d5d0576` (review tag)

1. Read the commit's full diff against `4a93167458b` and the audit's findings 6, 7, and 8. Judge at least: (a) the new `describe()` branch appends the core name instead of the URL when the message already contains the base URL, so for a remote error the composed message names the server once; the rule keys on the remote error's wording ("Error from server at " in `RemoteSolrException`), not on its type, and other errors keep the full URL; state whether that dependence is acceptable as written or a defect, with the code cited. (b) The changelog title now overstates the result for remote errors (finding 7): it says the error "now names the replica URL, collection and shard", while for a remote error the replica's full URL no longer appears and the core is named instead. Record the wording fix as owed in the draft text (title to "names the replica, collection and shard", or the description explaining the server-text route); do not edit the branch under this job. (c) The new test `testDistribErrorMessageNamesTheHostOnce` actually pins the behavior it names.
2. Record the verdict (a short section in this file, and a line in `receipts/SOLR-12245.md`): SATISFIED or findings, each finding stated with its file and line evidence. A findings verdict does not block Part C; it is recorded for the main side.

## Part B: the coordinator log check (record)

1. Perform the check the conditional decision names: read the coordinator's forward-to-leader log line on this branch's code path (the forwarding path in `DistributedUpdateProcessor` and `SolrCmdDistributor`) and establish whether that log line names the collection and the shard.
2. Record the result, with the log statement cited by file and line, in three places: a section in this file, `receipts/SOLR-12245.md`, and a dated note appended to the SOLR-12245 conditional decision entry in `TESTING.md`. If the check shows the log line does name the collection and shard, record that plainly and flag it for the main side in the receipt: under the decision's own terms the item then returns to Nick before any logs-only change, and this job makes no such change either way. If it does not, record that the response-stays path the commit took is the decision's path.

## Part C: gate at the live head (gate tag)

- Intended host: any capable Linux host, under the WORKFLOW.md claim rules (claim first under `claims/`, first push wins, hourly heartbeat). Windows never claims build work. Parts A and B are reading and records; Part C is the build.
- Gradle route: the claiming host's route per its file in `hosts/` (on vm1, `~/workspace/tools/solr-gradle.sh` and never `./gradlew`; on vm2, per `hosts/vm2.md`). Copy `gradle.properties` into the worktree before any Gradle run.
- All Gradle steps run serially behind the host's test-queue flock. Never two Gradle builds at once on a host.
- Test counts come from fresh JUnit XML only. Seed: `12245C0FFEE12245`. Module: `:solr:core`.

Steps, in order:

1. Confirm the base: `git ls-remote fork refs/heads/solr-12245-submit` must equal `f325d5d0576e582d4488570eabdeabc7410a8a0f`. If it has moved, stop and record the new tip in this file; do not gate an unrecorded head.
2. Changelog YAML parse with a real YAML parser of the fragment the branch adds, `changelog/unreleased/SOLR-12245-distrib-update-error-names-replica.yml`. It must parse. (The title's wording is Part A's finding 7; the parse is all this step judges.)
3. Tidy, with the GATE INVALID guard: run tidy for the branch's module scope; restore `gradle/libs.versions.toml` afterwards; then check `git status`. If tidy modified anything beyond that restore, the run is GATE INVALID: stop and record what tidy changed in this file. If a tidy commit is needed, commit it (Nick Shanin identity, no trailer), re-run the gate on the committed tree, and treat the new head as the gated head everywhere below.
4. Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`. rc must be 0.
5. Fail-before proof, judged by content: run the live head's `DistributedUpdateProcessorTest` against the pre-fix branch production at `4a93167458b` (the round 36 head, before the duplicated-host fix; the branch's test file goes in place over that tree). The pass condition is that `testDistribErrorMessageNamesTheHostOnce` fails there by content: on the old `describe()` the host is named twice. Record the per-test outcomes either way; failures from setup or compilation do not count as proof.
6. Focused tests at the head, counted from fresh JUnit XML: `org.apache.solr.update.processor.DistributedUpdateProcessorTest`. Confirm the class's exact package by grep on the branch before composing the run command, and compose the run in a separate step from the grep. Record tests, skipped, failures, and errors.
7. Module check: `:solr:core:check -x test`, rc 0.

## On green

1. Refresh `receipts/SOLR-12245.md` with a section for this job: the Part A verdict, the Part B check result, the gated head, the changelog parse result, the tidy guard result, the compile and module check results, the step 5 proof outcome by content, and the step 6 counts with the seed and run date, all from the fresh JUnit XML and the gate log.
2. The branch backs Nick's open PR #5084. If the gate produced no commit, the fork tip does not move and no push or body edit is owed. If a tidy commit moved the head, push it plainly (fast-forward, no force) to `fork` `solr-12245-submit` under the standing authorization for pushes to Nick's own open PRs (2026-10-14), verify the new tip by `git ls-remote`, and re-point PR #5084's body citations and Proof to the new head (read the body through the API before and after the edit).
3. Mark this job DONE with the gated head and the outcome, and close the claim.

## On failure

- Record the failing step, the failing test and seed if a test ran, and the log path in this file and in the receipt. A gate that executed tests and failed is never relaunched by the claimant; the main side investigates first (WORKFLOW.md). A run that never executed tests because of an environment refusal may be relaunched once, with the refusal recorded. Parts A and B stand even if Part C fails; record them before stopping.
