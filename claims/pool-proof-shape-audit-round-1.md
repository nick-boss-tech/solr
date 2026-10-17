# Claim: pool-proof-shape-audit-round-1

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job assignments/pool-proof-shape-audit-round-1.md: none linked)

- Claimant host: windows (review agent, review-rounds coordinator lane).
- Job or assignment: `assignments/pool-proof-shape-audit-round-1.md`.
- Slice: the whole assignment, run to SATISFIED.
- Capability tags used: review.
- Started: 2026-10-15T15:35:00Z (UTC).

Heartbeat: 2026-10-15T15:35:00Z

Runs under the standing coordinator claim, `claims/coordinator-review-rounds.md`. Deliverable: `reports/proof-shape-audit-round-1.md`. No builds on this host, no live GitHub writes.

Result: 2026-10-15T16:05:17Z: PARTIAL. 22 of 26 tickets audited in reports/proof-shape-audit-round-1.md. Remaining: 15674, 15863, 18341, 8430. Eight run-job files were drafted in gates/ (uncommitted, not posted, not claimed; SOLR-12998 marked PROPOSED) and are held for coordinator review before posting.

Reconciliation: 2026-10-15T19:05Z (coordinator): the audit covers 22 tickets, not 26 (the twenty listed in the sweep, plus SOLR-8430 and SOLR-18391). The main side's response of 2026-10-15 (`reports/main-side-responses-2026-10-15.md`, item d) asked for this. The SOLR-12998 shape-A proposal is not ratified; the 2026-10-13 held-items ruling stands, so no SOLR-12998 run job is to be posted. The twelve run-job drafts named in the DONE line are present, uncommitted, under `gates/`, held and not posted. An earlier reconciliation note in this claim said they were missing; that was a search error, and the note is withdrawn.

Continuation result: 2026-10-15T16:39:53Z (UTC, same claim): DONE. The four remaining tickets (15674, 15863, 18341, and the sharing half of 8430) are decided in reports/proof-shape-audit-round-1.md and have OPEN run-job drafts in gates/ (uncommitted, not posted, not claimed): twelve drafts in total. No compile, run, or GitHub write. Items for the coordinator: reconcile the "22 of 26" count; approve the SOLR-18341 base-copy subset before a Linux run; the SOLR-15863 Lucene 9.12.0 fixture stays in the scratch tree only.
