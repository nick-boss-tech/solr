# Claim: SOLR-6831 refinement fix, round 1 (vm1)

- Claimed: 2026-10-15T03:50:00Z by vm1. Assignment: assignments/pool-solr-6831-refinement-fix-round-1.md.
- Slice: the whole assignment. Settle the TestQueryLimits test shapes by run (rework testPivotFacetFailsWhenPartialResultsDisallowed to the distributed shape and testPivotFacetRefinementStopsWhenPartial until each discriminates, or record the honest-record outcome), take the spotless formatting change, run the full hardening gate at the resulting head, and on green push, refresh the receipt, re-point PR #5134, and mark gates/SOLR-6831-refinement-patch.md superseded.
- Capability tags used: implementation, gate.
- Branch state at claim: solr-6831-submit live tip d804a2ce09258fb4cad2b6a5425ac023b23b07b6 (ls-remote, re-verified at claim time); no other claim covers this assignment.
- Heartbeat: appended below as the work progresses; staleness rule 2 hours.
- Status: IN PROGRESS.

## Heartbeats

- 2026-10-15T03:50:00Z heartbeat: claim taken by vm1; reading the branch code and the failing tests before the first development run.
- 2026-10-15T04:15:00Z heartbeat: test shapes settled by run. Diagnostic runs at seed 6831C0FFEE6831 (dev logs g6831-fix-dev*.log and g6831-fix-dev3-*.log in ~/workspace/tools): the refinement test now discriminates (5 tripped refinement requests at ae76f048005 vs 3 at d804a2ce092 with the overrequest zeroed per-field limit shape; the coordinator sends a second refinement round only when it ignores the partial marker). For the partialResults=false test, no distributed shape discriminates: at both heads and in both the local and distributed shapes the request returns with the header partialResults "omitted" and no exception, because a tripped limit is converted to the omitted header by the handler and shard refinement responses carry "omitted" rather than true when partial results are disallowed, so the refinement fix never engages there. The test is corrected to the local path's actual contract (renamed testPivotFacetOmitsResultsWhenPartialResultsDisallowed) per the assignment's fallback. Test changes committed on the branch; the full gate follows.
