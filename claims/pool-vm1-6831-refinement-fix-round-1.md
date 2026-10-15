# Claim: SOLR-6831 refinement fix, round 1 (vm1)

- Claimed: 2026-10-15T03:50:00Z by vm1. Assignment: assignments/pool-solr-6831-refinement-fix-round-1.md.
- Slice: the whole assignment. Settle the TestQueryLimits test shapes by run (rework testPivotFacetFailsWhenPartialResultsDisallowed to the distributed shape and testPivotFacetRefinementStopsWhenPartial until each discriminates, or record the honest-record outcome), take the spotless formatting change, run the full hardening gate at the resulting head, and on green push, refresh the receipt, re-point PR #5134, and mark gates/SOLR-6831-refinement-patch.md superseded.
- Capability tags used: implementation, gate.
- Branch state at claim: solr-6831-submit live tip d804a2ce09258fb4cad2b6a5425ac023b23b07b6 (ls-remote, re-verified at claim time); no other claim covers this assignment.
- Heartbeat: appended below as the work progresses; staleness rule 2 hours.
- Status: IN PROGRESS.

## Heartbeats

- 2026-10-15T03:50:00Z heartbeat: claim taken by vm1; reading the branch code and the failing tests before the first development run.
