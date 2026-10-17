# Claim: pool-vm5-review-2026-10-16-code-round-2-item-2

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job assignments/pool-review-2026-10-16-code-round-2.md: Status: OPEN)

- Claimant host: vm5, matching a file in `hosts/` (`hosts/vm5.md`).
- Status: ACTIVE; mark DONE with the deliverable path when the work lands.
- Job or assignment: `assignments/pool-review-2026-10-16-code-round-2.md`, item 2 (SOLR-9124, PR 5147).
- Slice: item 2 only: (a) run TestDistributedGrouping, BasicDistributedZkTest and DistribCursorPagingTest at head `3dd985b93ac0e26a3850c338765ba1235bd811d4`; (b) remove the within-group clause in a scratch copy with no commit and run the grouped stats tests.
- Within-group clause (single match in the diff against `cabedd1d9680`): `needsDistributedStats` in `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java`, the term `rb.grouping() && rb.getGroupingSpec().getWithinGroupSortSpec().includesScore()`.
- Capability tags used: implementation, gate.
- Started: 2026-10-17T06:40Z (UTC).

Heartbeat: 2026-10-17T06:40Z. Fork branch `solr-9124-submit` confirmed at `3dd985b93ac0e26a3850c338765ba1235bd811d4` by `git ls-remote`. No build started yet.

Append one heartbeat line at least once an hour while the claim is active, and push it.
