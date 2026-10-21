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
