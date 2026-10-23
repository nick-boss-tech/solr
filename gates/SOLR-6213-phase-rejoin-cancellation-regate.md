# Gate job: SOLR-6213 (rejoin cancellation and retained regressions)

Status: DONE (gate GREEN at 2193e0104a3fa5c6235d4f843f1d6930afeb65d1, 2026-10-23; receipt in receipts/SOLR-6213.md)

- Gate ID: SOLR-6213-phase-rejoin-cancellation-regate.
- Branch: `solr-6213-rejoin-fix`. Head: `2193e0104a3fa5c6235d4f843f1d6930afeb65d1`, confirmed against the fork ref and PR #5210 head on 2026-10-23. If the fork tip has moved at claim time, stop and record the new tip.
- Prior gated head: `1fdc74f35021063d94cc6bb41a86892876ee5abb`. Base and merge-base with upstream/main: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.
- Intended host: any capable Linux host. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.cloud.ShardLeaderElectionRejoinTest`. Seed: `6213C0FFEE`.
- Diff scope against base: `changelog/unreleased/SOLR-6213-rejoin-thread.yml` +12/-0; `solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java` +51/-12; `solr/core/src/java/org/apache/solr/cloud/ZkController.java` +1/-0; `solr/core/src/test/org/apache/solr/cloud/ShardLeaderElectionRejoinTest.java` +238/-0. No other source changes are authorized.
- Why: the prior PR lacked a retained regression and allowed a running rejoin to recreate an election participant after unregister cancelled the context. Commit `2193e0104a3` serializes the asynchronous join and cancellation with a lifecycle lock, closes the context before unregister cancellation, and cleans up an in-flight join when it observes closure. Three tests cover repeated rejoin stack depth, cancellation with node creation paused, and a closed context rejecting new rejoins. The tests use real ZooKeeper participants and an overridden LeaderElector callback, not the separate sync-failure hook. Windows performed source review and `git diff --cached --check` only. This head is Implemented, Proof Pending.

Links: Ticket: SOLR-6213. Category: Solr core, SolrCloud. PR: https://github.com/apache/solr/pull/5210. Sibling: `solr-6213-watch-retry`, PR #5209, unchanged by this work. Supersedes the rejoin-thread proof at `1fdc74f3502` in `receipts/SOLR-6213.md`; preserve that historical section and the independent watch-retry proof. The separate experimental branches `solr-6213-rejoin-fix-test` and `solr-6213-rejoin-hook` are premise evidence only; this gate uses the retained test in the PR branch.

## What to run

Claim this job under the phase-specific ID before running. Use the host's standard serialized gate runner. Do not run the build tool directly. Use an isolated worktree at the exact head. Do not use or change the watch-retry branch.

- Step 0: parse `changelog/unreleased/SOLR-6213-rejoin-thread.yml`; verify author Nick Shanin and no nick field.
- Step 1: run `:solr:core:tidy`. If it changes files, commit and push the mechanical repair under WORKFLOW.md, record the repair and tree SHA, then obtain proof for the resulting head. Do not count evidence from an earlier head as proof for a repaired head.
- Step 2: run `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3a, cancellation fail-before: use a separate worktree at old PR head `1fdc74f35021063d94cc6bb41a86892876ee5abb`. Copy the new `ShardLeaderElectionRejoinTest.java` from the gated head. For compilation only, change the existing `private rejoinLeaderElection(SolrCore core)` to package-private `rejoinLeaderElection()` and update its three callers to omit the unused argument. Keep the old executor, unsynchronized cancellation, and old close/unregister behavior. Do not copy the new lock or cleanup logic. Run only `ShardLeaderElectionRejoinTest.testCancellationWaitsForRunningRejoin` with seed `6213C0FFEE`. Expected assertion: `Cancellation returned before the running rejoin could create its node`. Record fresh XML, exact scaffold diff and logs. A compilation/setup failure is not fail-before proof.
- Step 3b, stack-growth fail-before: use another isolated worktree at base `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Copy the new test class and apply only the same package-private/no-argument signature and three call-site adaptations. Keep base's direct synchronous `leaderElector.joinElection(this, true)` call. Run only `ShardLeaderElectionRejoinTest.testRepeatedRejoinDoesNotGrowStack` at seed `6213C0FFEE`. Expected assertion contains `Stack grew by` and a frame count at least 20. Do not require a literal StackOverflowError; the bounded 20-rejoin test demonstrates growth without overflowing the test runner. Record the exact scaffold diff and fresh XML. Neither proof worktree is pushed.
- Step 4: on the clean final branch head, run all of `ShardLeaderElectionRejoinTest` at seed `6213C0FFEE`. Expect 3 tests executed, 0 skipped, 0 failures/errors. Repeat that class at seeds `6213A11CE` and `6213BEEF`; all three tests must execute in each run. A Mockito assumption skip is not proof and must be handed back with its reason. Read counts from fresh XML immediately after each run. The cancellation test must also assert the election directory is empty and that a subsequent candidate becomes elected.
- Step 4b: run neighboring `org.apache.solr.cloud.LeaderElectionTest`, `org.apache.solr.cloud.LeaderVoteWaitTimeoutTest`, `org.apache.solr.cloud.DeleteReplicaTest`, `org.apache.solr.cloud.ZkControllerTest`, `org.apache.solr.cloud.ReplaceNodeTest`, and `org.apache.solr.cloud.TestCloudRecovery` at seed `6213C0FFEE`. Record total, executed, skipped, failures and errors separately. Investigate any timeout or thread leak as a failure; do not disable the leak checker or discard a failing class.
- Step 5: run `:solr:core:check -x test` at the final head.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Scope | Decided: only the four paths above | Do not incorporate #5209 or alter its receipt | Nick's patch request |
| Proof scaffolding | Decided: signature/visibility and unused-argument adaptations only | Save both proof diffs; any other source adaptation requires hand-back, not an invented proof | Windows implementation |
| Repair | Default: WORKFLOW.md Gate repairs only | Commit and plain-push a mechanical repair, then re-gate at its head once; a failed retry is HANDED BACK | Linux gate host |
| New locking failure | Decided: do not weaken assertions or remove coverage | Deadlock, cancellation timeout, stale nodes or leak: preserve logs and hand back for implementation | Windows implementation |
| Changed tip or live claim | Decided: do not move a claimed head | Stop and record the conflict; do not force or modify another job | Pool rules |
| Public communication | Decided: no public writes in this assignment | No PR body edit, comment, JIRA write or PR state change | Nick's requested scope |

## On green

1. Prepend a rejoin-cancellation section to `receipts/SOLR-6213.md` naming the final head, date, host, exact commands/seeds, raw log/XML paths, both scaffolded fail-before results and all focused counts. Preserve historical rejoin and watch-retry sections.
2. Record repair commits, if any, and confirm the fork and PR #5210 head match the final proven head. If the head differs, report Proof Pending instead of green.
3. Mark this job DONE and its phase-specific claim DONE in the same fast-forward coordination push as the receipt. No public text edits.

## Checklist

- [x] phase claim posted; fork and PR head confirmed
- [x] diff scope confirmed
- [x] step 0 changelog parsed
- [x] step 1 tidy clean or repaired and re-gated
- [x] step 2 Error Prone compile
- [x] step 3a cancellation fails before, assertion recorded
- [x] step 3b stack growth fails on base, assertion recorded
- [x] step 4 three focused seeds, fresh XML counts, no skips
- [x] step 4b neighboring tests, no thread leaks or deadlocks
- [x] step 5 module check
- [x] receipt written at exact final head
- [x] job DONE and claim DONE

## Deliverable and limits

Gate result and receipt on coordination; any allowed mechanical repair on `solr-6213-rejoin-fix`. Fast-forward pushes only. Commit as Nick Shanin with no attribution trailers. No Windows builds, no changes to Apache branches, no PR opening/state/body/comment edits, no JIRA or email writes, no force push. Any required proof that cannot be executed is recorded and handed back, not declared green.
