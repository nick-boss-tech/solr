# Claim: pool-vm5-review-2026-10-16-code-round-2-item-2

- Claimant host: vm5, matching a file in `hosts/` (`hosts/vm5.md`).
- Status: ACTIVE; mark DONE with the deliverable path when the work lands.
- Job or assignment: `assignments/pool-review-2026-10-16-code-round-2.md`, item 2 (SOLR-9124, PR 5147).
- Slice: item 2 only: (a) run TestDistributedGrouping, BasicDistributedZkTest and DistribCursorPagingTest at head `3dd985b93ac0e26a3850c338765ba1235bd811d4`; (b) remove the within-group clause in a scratch copy with no commit and run the grouped stats tests.
- Within-group clause (single match in the diff against `cabedd1d9680`): `needsDistributedStats` in `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java`, the term `rb.grouping() && rb.getGroupingSpec().getWithinGroupSortSpec().includesScore()`.
- Capability tags used: implementation, gate.
- Started: 2026-10-17T06:40Z (UTC).

Heartbeat: 2026-10-17T06:40Z. Fork branch `solr-9124-submit` confirmed at `3dd985b93ac0e26a3850c338765ba1235bd811d4` by `git ls-remote`. No build started yet.

Append one heartbeat line at least once an hour while the claim is active, and push it.

Heartbeat: 2026-10-17T21:50Z. Partial results from runs at 3dd985b93a (logs /workspace/gates/logs/vm3/9124r2-*.log, gradle rc 0 and 1). (a) BasicDistributedZkTest 1 of 1 and DistribCursorPagingTest 1 of 1 passed. TestDistributedGrouping was not run: the filter used org.apache.solr.handler.component, the class is org.apache.solr.TestDistributedGrouping; rerun pending. (b) Mutant with the within-group clause removed: TestDefaultStatsCache 1 of 1, TestExactSharedStatsCache 1 of 1, TestLRUStatsCache 1 of 1 passed; TestExactStatsCache 1 of 2 failed at checkWithinGroupOrder (TestBaseStatsCache.java:117, assertEquals(controlDocs, shardDocs)) with expected [g0=4, g1=3] but was [g0=4, g1=5], seed 9124C0FFEE9124.
