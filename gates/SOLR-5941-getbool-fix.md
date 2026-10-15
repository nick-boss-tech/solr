# Gate job: SOLR-5941 third getBool site fix (audit finding 3)

- Branch: solr-5941-submit. Head: `a4df7bfd214b15d9c8d4519fdac8fd6df9468f2d` (fork tip, verified by `git ls-remote` on 2026-10-15 at filing). The branch backs the owner's open PR at that head. If the tip has moved when claimed, stop and record the new tip here; do not run at any other head without noting it.
Status: BLOCKED (scope stop at the line 125 fix, 2026-10-15; see the Stop section)
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
