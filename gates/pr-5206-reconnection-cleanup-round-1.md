# Gate: PR 5206 reconnection assertion and listener cleanup, round 1

Status: OPEN
Tags: gate, review, records.
Intended host: any available capable Linux gate host with JDK 21 and the pinned branch's build dependencies, serialized under its host build lock.
Posted by: windows, on Nick's direct request, 2026-10-27.

## Pinned inputs and scope

- Ticket: SOLR-18576. PR: https://github.com/apache/solr/pull/5206.
- Fork branch: `nick-boss-tech/solr:solr-18576-submit-apache-main`.
- Candidate F: `6c6ff8d9a0e173c02b5a11f8ad3b13a59ebd36fa`.
- Previously gated head O, and F's parent: `4c8f12f526f54e4e90a8ea49d46c1d28d899cb65`.
- Apache merge base: `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Incremental commit: `SOLR-18576: Assert reconnection and always remove expiry listener`, by Nick Shanin, no attribution trailers. Normal fast-forward on the existing public PR branch.
- Exact outbound file: `solr/core/src/test/org/apache/solr/cloud/BasicDistributedZk2Test.java`. No production files, new dependencies, or changelog fragment.
- Implementation claim: `claims/pr-5206-review-fixes-round-1-windows.md`.
- Implementation report: `reports/pr-5206-review-fixes-round-1.md`.

The change asserts the result of the existing 30-second `blockUntilConnected` call, with a reconnection-specific failure message. Expiry, the latch assertion, and the reconnection assertion are inside try/finally so the listener is removed on success, failure, or interruption. Registration remains before forced expiry and indexing remains after the checked waits and cleanup.

The older receipt `receipts/SOLR-18576.md` proves O only. This gate is required for F. Do not carry its green verdict forward to F.

## Claim and preflight

Read the live board workflow, this job, the implementation claim/report, and any competing gate claim. Claim this gate only once. On a clean detached worktree, fetch and verify both the fork ref and the live PR head equal F, and confirm O is F's parent. If the branch moved, record the discrepancy and reconcile the newer head before executing; do not silently test a different commit.

Perform the board's detached tidy preflight before claiming the gate. Record the board snapshot, claimant, UTC start, JDK version, exact commands, worktree, runner path, host lock, and persistent log/XML paths in the add-only Linux claim. Choose host-local worktree and evidence directories; no Windows or shared filesystem build. Run one gate at a time under the host lock.

## Required proof at F

Run sequentially with the supported Solr runner and this branch's toolchain. The commands below are Linux-only. Capture an exit code and separate log for every step, plus fresh JUnit XML and suite output for each test invocation. Preserve the normal assertions, security setup, and test scope.

1. Tidy at F: `./gradlew --no-daemon tidy`. Require no tracked change and record `git status --porcelain` and `git diff --exit-code`.
2. Error Prone compile: `./gradlew --no-daemon :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true --rerun-tasks --no-build-cache`. Require rc 0.
3. Original failure seed: `./gradlew --no-daemon :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.BasicDistributedZk2Test -Ptests.seed=7E5494314774A841 --no-build-cache`. Require one executed test, no skips, failures, or errors.
4. Random iterations: `./gradlew --no-daemon :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.BasicDistributedZk2Test -Ptests.iters=4 --no-build-cache`. Require four executed tests, no skips, failures, or errors; record the actual seed. Do not retain the fixed seed property from step 3.
5. Root checks: `env -u NODE_ENV ./gradlew --no-daemon check -x test --no-build-cache`. Require rc 0; this is the root check, not only `:solr:core:check`. Report the `NODE_ENV` condition explicitly. Classify any environment refusal separately; do not silently exclude a failing check.
6. Final tidy and cleanliness: `./gradlew --no-daemon tidy`, then record clean tracked status, unchanged HEAD F, and matching live fork/PR heads. No formatting change may be left uncommitted.

Review the source at F for both failure paths: an expiry assertion/exception and an interrupted or timed-out reconnection must traverse finally; an unsuccessful reconnection must fail before indexing. Confirm that no production file or unrelated test changed. Report this as source inspection unless actually executed.

## Proof interpretation and repairs

This phase gates a small test-only follow-up. It does not request a synthetic failing baseline, a production mutation, or a new reproduction investigation. The original race was never reproduced before or after the earlier PR change; the new assertion is a pin until demonstrated otherwise. Passing runs support compatibility and do not establish that the original intermittent failure is eliminated. Normal passing executions do not independently exercise the timeout or interruption cleanup paths.

If tidy modifies the candidate, make a formatting-only commit with Nick Shanin's ICLA identity and no attribution trailer, push the fork branch as a fast-forward, verify the new remote and PR head, and post an add-only superseding job pinned to that new head before proof. Obtain the complete required proof at the new head; do not label F green after changing it. Never force-push this public PR branch.

If tests execute and fail, record the command, seed, failing assertion, log and XML, investigate, and follow the board's repair policy. Do not relaunch an unexplained failure as a fresh green attempt. No local Windows queue, Gradle, or test execution is authorized by this handoff.

## Deliverables and completion

- Add a Linux claim for this gate with the host, lock and durable evidence paths.
- Add `reports/pr-5206-reconnection-cleanup-round-1-gate.md`, recording actual results for every step, exact tested head, seeds, test counts, logs/XML paths, cleanliness and live-head verification, failures or repairs, and the proof limitations above.
- Add a new phase receipt at `receipts/SOLR-18576-pr5206-review-fixes-round-1.md`, written only by the gate host. Preserve the older receipt. Include a concise PR-description draft with the current verified head and results, both checked 30-second waits, finally cleanup, and the non-reproduction limit.
- Leave the candidate **Implemented, Proof Pending** until every required step succeeds at the exact live head. A report of failure is a completed investigation record, not a green gate.

Do not edit a shared index, this posted job, or the implementation claim. Do not post a GitHub/JIRA comment, edit the live PR body, or merge/approve the PR as part of this gate assignment. Return the receipt and report links on the board; public text remains a separate action.
