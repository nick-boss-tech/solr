# Review round 1, unit 24: SOLR-7394 (branch solr-7394-recovery), vm3

## Pass 1 (vm3, 2026-10-21T09:13:00Z)

- Head reviewed: `a92a0d89b20908`. Review base `56ec140e36` (the branch's own merge base; the round's 14c7aac0d1 adds unrelated main changes). Branch diff: 9 files, 386 insertions, 3 deletions.
- Ticket SOLR-7394 (Critical): shard replicas do not recover after a cluster-wide restart. The branch's mechanism: when recovery gives up, the replica's `_recovering` shard-term entry is cleared and its pre-recovery term restored (`ShardTerms.recoveryFailed`), and RECOVERY_FAILED is published for leader-eligible replicas after the shard-term update.
- Surrounding code check (rung 1): `ShardTerms.recoveryFailed` returns `null` when no marker exists; `ZkShardTerms.mutate` stops on `null`, the same pattern as `doneRecovering`. A shard-term failure is logged and the RECOVERY_FAILED publication still runs (the `finally` in `recoveryFailed` closes the strategy). `recoveryFailed` went from private to package-private for the test; the test-injection hooks are static fields on the production `TestInjection` class, as elsewhere in the repo, and the `assert TestInjection.injectFailRecovery()` hook runs only with assertions enabled.
- Test expectations: `ZkShardTermsRecoveryFailureTest.testFailedRecoveryClearsShardTermsAndRestoresEligibility` asserts that the failed replica has no `_recovering` entry, its term is restored to 0, it is not leader-eligible while the higher-term leader stands, and it becomes leader after that leader stops. That is the ticket's mechanism.
- Proof: the receipt says both target classes fail to compile at base, so no base run discriminates. Pass 2 runs a mutation probe at the head instead (disable the shard-term update in a throwaway copy; the test must fail).
- Repairs in pass 1: none.
- Regate: `gates/SOLR-7394-review-pass-1.md`, GREEN at `a92a0d89b20908`. Logs `/workspace/gates/logs/vm3/solr-7394-review-pass-1/`. Changelog rc 0; tidy rc 0, empty status; Error Prone compile rc 0 (compileJava and compileTestJava executed, no skip warning); focused `RecoveryStrategyTest`, `ShardTermsTest`, `ZkShardTermsRecoveryFailureTest`, `TestTestInjection`: 4 classes, 13 tests, 0 failures, 0 errors (fresh XML); `:solr:core:check -x test` rc 0.

Pass 1 result: GREEN at `a92a0d89b20908`.

## Pass 2 (vm3, 2026-10-21T10:00:01Z)

- Head reviewed: `a92a0d89b20908` (unchanged). Review base `56ec140e36`.
- Test expectations against the ticket: `ZkShardTermsRecoveryFailureTest.testFailedRecoveryClearsShardTermsAndRestoresEligibility` checks the mechanism: no `_recovering` entry after the failed recovery, term restored to 0, no eligibility while the higher-term leader stands, eligibility after it stops.
- Mutation probe (rung 2), single site: the shard-term update in `RecoveryStrategy.recoveryFailed` (line 210) is disabled in a throwaway worktree (`/workspace/gates/logs/vm3/solr-7394-review-pass-2-mut1/diff.txt`: one line changed). Result: `ZkShardTermsRecoveryFailureTest.testFailedRecoveryClearsShardTermsAndRestoresEligibility` FAILED with `AssertionError: failed replica must not retain a _recovering shard-terms entry`. The test discriminates the fix at this head. Log `/workspace/gates/logs/vm3/solr-7394-review-pass-2-mut1/mutation-test.log`.
- Probe note: an earlier probe disabled both `leaderEligible` sites (the second is in `replicate()`, a commit-on-leader call). Its result is not cited.
- Finding: none new beyond pass 1.
- Repairs in pass 2: none.
- Regate: `gates/SOLR-7394-review-pass-2.md`, GREEN at `a92a0d89b20908`. Logs `/workspace/gates/logs/vm3/solr-7394-review-pass-2/`. Changelog rc 0; tidy rc 0, empty status; Error Prone compile rc 0 (compileJava and compileTestJava executed); focused classes: 4 classes, 13 tests, 0 failures, 0 errors (fresh XML); `:solr:core:check -x test` rc 0.

Pass 2 result: GREEN at `a92a0d89b20908`; the mutation probe shows the test pins the shard-term update.

## Pass 3 (vm3, 2026-10-21T14:38:39Z)

- Head reviewed: `a92a0d89b20908` (unchanged). Review base `56ec140e36`. Passes 1 and 2 reused; the mutation probe in pass 2 (fb3f8ff3ea) is the discriminating evidence.
- Review: no new findings. The shard-term update is pinned by `ZkShardTermsRecoveryFailureTest` (the single-site probe fails it). The null-return and the test-injection hook were checked in pass 1.
- Repairs in pass 3: none.
- Regate: `gates/SOLR-7394-review-pass-3.md`, GREEN at `a92a0d89b20908`. Logs `/workspace/gates/logs/vm3/solr-7394-review-pass-3/`. Changelog rc 0; tidy rc 0; Error Prone compile rc 0 (real); focused 4 classes, 13 tests, 0 failures, 0 errors (fresh XML); `:solr:core:check -x test` rc 0.

Pass 3 result: GREEN at `a92a0d89b20908`. Unit 24 is complete after pass 3.
