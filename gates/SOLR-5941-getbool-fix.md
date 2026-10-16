# Gate job: SOLR-5941 third getBool site fix (audit finding 3)

- Branch: solr-5941-submit. Head: `a4df7bfd214b15d9c8d4519fdac8fd6df9468f2d` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing). The branch backs the owner's open PR at that head. If the tip has moved when claimed, stop and record the new tip here; do not run at any other head without noting it.
Status: CLAIMED (claims/pool-vm2-gate-backlog-5941.md; resumed 2026-10-16T01:29Z; the line 125 predicate is the decided one)
- Capability tags: [implementation, gate].
- Source: `audits/update-processing/SOLR-5941.md` (2026-10-15), finding 3 and readiness item 1. Read the audit in full before claiming. Per the no-new-ticket rule, this fix belongs inside this branch: no new ticket, no separate PR.
- Current gate state (`receipts/SOLR-5941.md`): gated at the pinned head (2026-10-09): AutoCommitUpdateChainTest 2/2, CommitThroughNonLeaderTest 1/1, ParallelCommitExecutionTest 1/1, HttpPartitionOnCommitTest 1/1, module check rc=0. The receipt does not cover finding 3: no test exercises the factory below with a forwarded commit.

## The site, exactly as the audit records it

`solr/core/src/java/org/apache/solr/update/processor/IgnoreCommitOptimizeUpdateProcessorFactory.java`, line 125, on base and at the pinned head (the branch does not touch the file; verified on the branch at filing):

```java
if (cmd.getReq().getParams().getBool(DistributedUpdateProcessor.COMMIT_END_POINT, false)) {
```

Why it is the same defect the branch already fixed twice: `SolrParams.getBool` calls `StrUtils.parseBool`, which throws BAD_REQUEST "invalid boolean value" for any value that is not a true or false word. Forwarded commits carry the strings "leaders" and "replicas" in `commit_end_point`. The branch fixed the two sibling sites by comparing the string to "true" (`DistributedZkUpdateProcessor.java:158` and `RoutedAliasUpdateProcessor.java:92` at head). This third site still calls `getBool`. With `ignoreOptimizeOnly=false` (line 61 of the same file reads the flag with a false default), the line 125 call is reached for every commit that is not optimize-only, so a forwarded commit with "leaders" or "replicas" throws the same 400 wherever this factory sits in a chain before the distributing processor.

## Scope and the stop rule

1. Fix line 125 in production with the same string compare the sibling sites use. The audit describes the fix as one line of production code. **If the fix proves wider than that one line, stop: record the true scope in this file and in the takeover log, and do not proceed.** Do not widen this job on your own authority.
2. Proof: add a SolrCloud commit test that puts `IgnoreCommitOptimizeUpdateProcessorFactory` in a chain and sends a forwarded commit with `commit_end_point=replicas` (the audit's readiness item 1 shape; the audit records that no existing gate test puts this factory in a chain). The test must fail on the pre-fix production by content (the BAD_REQUEST invalid boolean value from the line 125 call) and pass at the fixed head. New tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4.
3. Full re-gate at the resulting head (blocking flock on the test queue, the claiming host's Gradle route per its host file, `gradle.properties` copied into every new worktree, never two Gradle builds at once; results counted from fresh JUnit XML only):
   - Changelog YAML parse with a real parser on the branch's fragment.
   - Tidy, with the GATE INVALID guard: after tidy, with `gradle/libs.versions.toml` restored, the tree must be unchanged; if tidy changes anything, commit the result and re-gate on the committed tree.
   - Error Prone compile (`-Pvalidation.errorprone=true`) for main and test sources of the module.
   - Fail-before proof by content (step 2's test against the pre-fix production).
   - Focused tests at the head, counted from fresh JUnit XML: the new chain test from step 2, IgnoreCommitOptimizeUpdateProcessorFactoryTest, AutoCommitUpdateChainTest, CommitThroughNonLeaderTest, ParallelCommitExecutionTest, HttpPartitionOnCommitTest (the last two are the standing run list from the Oct 8 TESTING decision). Confirm each class's exact package by grep on the branch before composing any run command, and compose the run in a separate step from the grep.
   - Module check: `:solr:core:check -x test`.

## Text rule (audit finding 2, binding on this job)

The round 30 design report (`material/last-reviews/5941-round30-design-report.md`) says `StrUtils.parseBool` returns false for "leaders" and "replicas". That is wrong: it throws. The report's sentence must not appear in any PR text. When you refresh the draft in step 4, check the draft and any text you write for that claim and keep it out; state the behavior as throwing, as the fix commit message already does.

## On green, on failure

4. On green: push plainly to `fork solr-5941-submit` (fast-forward; the branch backs the owner's own open PR, and pushes to it carry standing authorization). Refresh `receipts/SOLR-5941.md` at the new head (new tip, gate date and log name, focused counts including the new test, the fail-before proof result). Refresh `pr-drafts/update-processing/SOLR-5941.md` to the new head: account for the finding 3 fix, and correct the draft's "No live multi-node run was made" sentence per audit finding 5 (say which tests run in a cluster, including the step 2 test, and that no multi-node soak or live run was done). Mark this job DONE and the claim DONE, and append the outcome to the takeover log.
5. On an executed-test failure: record it (failing test, seed, reproduce line, classification if the log establishes one) in this file, the receipt, and the takeover log, mark the claim with the blocker, and stop. Do not relaunch an unchanged gate.
6. Stop condition: either the green path has landed (fix pushed at the gated head, receipt and draft refreshed, job and claim DONE), or a stop under step 1's scope rule or step 5 is recorded with its evidence.

## Stop, 2026-10-15 (step 1 scope rule)

Claimed at a4df7bfd21, live tip verified with git ls-remote. No production change, no test, no push to the fork.

The line 125 fix is not the one-line compare the sibling sites use. Line 125 sits in the branch whose comment reads "this is a targeted commit from replica to leader needed for recovery, so can't be ignored". The forwarded values are "leaders" and "replicas" (DistributedUpdateProcessor sets them at lines 225 and 242). The sibling sites compare with "true" because "true" marks a local end point such as an auto commit, which is a different meaning. Copied to line 125, `"true".equals(...)` returns false for "leaders" and "replicas", so a forwarded commit would enter the ignore branch rather than pass to the distributing processor. That reads from the code and was not run. It would replace a BAD_REQUEST with a silent ignore.

The owner decides the predicate at line 125: which commit_end_point values make a commit non-ignorable (for example any non-null value other than "false", or the explicit set true, leaders, replicas). The step 2 proof depends on that predicate. Re-file the job with the decided predicate to resume.

## Amendment, 2026-10-15: the line 125 predicate is decided

The stop above asked which `commit_end_point` values make a commit non-ignorable at `IgnoreCommitOptimizeUpdateProcessorFactory.java:125`. Decided on the main side from the code at the pinned head `a4df7bfd214b` (merge-base `cabedd1d968`). This amendment replaces the first sentence of step 1 (the fix is not "the same string compare the sibling sites use"); everything else in this job, including steps 2 to 6 and the text rule, stands unchanged. Scope holds: the fix is still one production file and one guard condition, so the step 1 stop rule is not triggered.

### The decided predicate

Read the parameter as a string. The commit passes to the next processor (is not ignorable) when the parameter is present and its value is not `"false"`:

- `commit_end_point` absent: not an end point; the commit is ignorable, as today. Explicit client commits carry no `commit_end_point`; only the commit machinery sets it.
- `"true"`: end point; pass (unchanged from `getBool`).
- `"leaders"`: end point; pass (today this throws BAD_REQUEST).
- `"replicas"`: end point; pass (today this throws BAD_REQUEST).
- `"false"`: not an end point; ignorable (unchanged from `getBool`; no producer writes `"false"`, it is the parameter's one affirmative negative).
- Any other value: treated as an end point; pass. No producer writes any other value, and the rule must never throw on a value a forwarding leg might add later.

In code, the condition at line 125 becomes: the string value of `DistributedUpdateProcessor.COMMIT_END_POINT` is non-null and not equal to `"false"`. Do not call `getBool` or `parseBool` on this parameter in the factory.

### Why presence, not the sibling compare and not an explicit value set

The sibling sites ask a narrower question. `DistributedZkUpdateProcessor.java:158` and `RoutedAliasUpdateProcessor.java:92` ask whether the commit is a local end point (such as an auto commit) that must not be distributed or routed; there, `"leaders"` and `"replicas"` correctly fall through into the distribution logic that handles them (`DistributedZkUpdateProcessor.java:209` commits a `"replicas"` commit locally on a non-leader). The factory asks a different question: whether this commit was generated by the commit machinery and so must not be ignored. Its fall-through is the ignore branch, which does not forward the commit at all, so copying `"true".equals(...)` would silently drop every forwarded commit in any chain where this factory sits before the distributing processor, replacing today's loud 400 with commits that never happen. The codebase's other general consumer already reads the parameter by presence: `DistributedZkUpdateProcessor.java:1251` treats any request carrying `commit_end_point` as an end point commit. An explicit set of `"true"`, `"leaders"`, `"replicas"` was not chosen because it hard-codes today's producer values into a third place; a future leg value would silently become ignorable here.

### Evidence (at head `a4df7bfd214b` unless a base line is named)

Producers, the only writers of the parameter in production code:

- `CommitTracker.java:278` sets it to boolean `true` for an auto commit, with the comment that an auto commit is an end point that applies to this core only and is never distributed.
- `DistributedZkUpdateProcessor.java:225` sets `"leaders"` when forwarding an originating commit to the leaders (only when the parameter is absent, line 222); line 242 sets `"replicas"` when the leader forwards the commit to the replicas. Both producers exist on the merge-base (lines 218 and 235), so the forwarded values predate this branch.

Consumers:

- `DistributedZkUpdateProcessor.java:158`: `"true"` means commit locally and never distribute. Line 209: `"replicas"` on a non-leader means commit locally. Line 1251: presence alone marks an end point commit.
- `RoutedAliasUpdateProcessor.java:92`: `"true"` means do not route the commit to the aliased collections.
- The factory itself, line 125: the file is identical on the merge-base and at the head, and its `getBool` call throws BAD_REQUEST "invalid boolean value" for `"leaders"` and `"replicas"` (`StrUtils.parseBool` throws for any value that is not a true or false word).

What the factory decides: `processCommit` passes a commit down the chain when the line 125 condition holds and otherwise ignores it (the configured error code, or a 200 with a warning). A forwarded commit that reaches this factory must pass, or the distributing processor never sees it and the commit is lost.

### Step 2 expectation, restated against the decided predicate

The new SolrCloud chain test stands as written in step 2 (a chain containing this factory; a forwarded commit carrying `commit_end_point=replicas`). Fail-before by content: on the pre-fix production the request fails with the BAD_REQUEST "invalid boolean value" from the line 125 `getBool` call. At the fixed head the same commit passes the factory and is committed, and the test's assertions pass. No `"leaders"` variant is required to discharge this job: the decided predicate treats both forwarded values the same way, by the same presence rule.

## Resumed, vm2, 2026-10-16T01:29Z

- Fix, local commit 6940409879 on a4df7bfd214b (fast-forward candidate; not pushed). Production: IgnoreCommitOptimizeUpdateProcessorFactory.java line 125 reads commit_end_point as a string and passes when it is present and not "false". Test: IgnoreCommitEndPointCloudTest (SolrCloudTestCase, configset cloud-ignore-commit-chain). Negative case: a plain client commit with no end point is still ignored with 403.
- Gate: g5941-fix-gate.sh, queued behind SOLR-13943. Results to logs/vm2/5941-fix-result.txt. Push only on green.

## 2026-10-16T02:01Z gate stop, tidy guard (vm2 gate backlog coordinator)

Runner g5941-fix-gate.sh (started 01:45:52Z, log logs/vm2/queue-g5941-fix-gate-20261016T014552Z.out) stopped at the tidy step as GATE INVALID: tidy changed solr/core/.../IgnoreCommitOptimizeUpdateProcessorFactory.java. No test ran and no GATE END was written. The runner reverted the tidy change with git checkout; head 6940409879 is unchanged. Not GREEN, not finished, not relaunched. Per step 3, commit the tidy result on the branch and re-gate on the committed tree; left for the main agent.

## 2026-10-16T03:50:51Z gate run stop, compile step (gate backlog coordinator)

Runner g5941-fix-gate.sh (started 03:15:00Z, log logs/vm2/queue-g5941-fix-gate-20261016T031500Z.out) ran at local head 21dbf94e3f (pre-fix a4df7bfd214b, 4 files, 138 insertions). Tidy rc=0 with a clean tree. The Error Prone compile returned rc=1; no test ran and no GATE END was written. The log ends at the proof-leg step. Not GREEN, not finished, not relaunched by the coordinator. The head differs from the claim's local commit 6940409879; the main side should confirm which commit the fix is.

## 2026-10-16T04:17Z gate result, not pushed (gate backlog coordinator)

Runner g5941-fix-gate.sh (started 03:53:00Z, log logs/vm2/queue-g5941-fix-gate-20261016T035300Z.out, results logs/vm2/5941-fix-result.txt) ran at local head 26720c7818, one commit on fork tip a4df7bfd21 (factory line 125 now a presence-and-not-"false" check, plus IgnoreCommitEndPointCloudTest and the cloud-ignore-commit-chain configset). Results:

- tidy rc=0, tree clean after tidy.
- compile-errorprone rc=0.
- fail-before proof rc=1 (expected nonzero): IgnoreCommitEndPointCloudTest 2 tests, 1 failure, testForwardedCommitWithReplicasEndPointPassesIgnoreFactory, "invalid boolean value: replicas" at pre-fix a4df7bfd21. This matches step 2's by-content proof.
- focused head rc=0, counted from fresh XML (04:06-04:09Z): IgnoreCommitEndPointCloudTest 2/0 failures, CommitThroughNonLeaderTest 1/0, HttpPartitionOnCommitTest 1/0, ParallelCommitExecutionTest 1/0, AutoCommitUpdateChainTest 2/0, IgnoreCommitOptimizeUpdateProcessorFactoryTest 1/0.
- module check rc=0.

Not marked GREEN: the coordinator rule requires every rc line to be 0, and the proof rc is nonzero by design. Not pushed to solr-5941-submit (live PR branch). Two items for the main side before any push: confirm the fix commit (the claim names 6940409879; the gated head is 26720c7818), and rule on the proof rc. Receipt and PR draft untouched; not relaunched.
