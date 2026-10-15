# Claim: pool-proof-shape-audit-round-1

- Claimant host: windows (review agent, review-rounds coordinator lane).
- Job or assignment: `assignments/pool-proof-shape-audit-round-1.md`.
- Slice: the whole assignment, run to SATISFIED.
- Capability tags used: review.
- Started: 2026-10-15T15:35:00Z (UTC).

Heartbeat: 2026-10-15T15:35:00Z

Runs under the standing coordinator claim, `claims/coordinator-review-rounds.md`. Deliverable: `reports/proof-shape-audit-round-1.md`. No builds on this host, no live GitHub writes.

Result: 2026-10-15T16:05:17Z: PARTIAL. 22 of 26 tickets audited in reports/proof-shape-audit-round-1.md. Remaining: 15674, 15863, 18341, 8430. Eight run-job files were drafted in gates/ (uncommitted, not posted, not claimed; SOLR-12998 marked PROPOSED) and are held for coordinator review before posting.

Continuation result: 2026-10-15T16:39:53Z (UTC, same claim): DONE. The four remaining tickets (15674, 15863, 18341, and the sharing half of 8430) are decided in reports/proof-shape-audit-round-1.md and have OPEN run-job drafts in gates/ (uncommitted, not posted, not claimed): twelve drafts in total. No compile, run, or GitHub write. Items for the coordinator: reconcile the "22 of 26" count; approve the SOLR-18341 base-copy subset before a Linux run; the SOLR-15863 Lucene 9.12.0 fixture stays in the scratch tree only.
