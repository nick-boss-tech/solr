# Claim: pool-ui-SOLR-17849

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`, one ticket per claim. Ticket type: screen (differs from the component ticket SOLR-17911 claimed before, per owner complaint 5).
- Slice: SOLR-17849 (Index & Query section header and screen). The implementation is on fork branches `solr-17849-index-query-header` (fb51fbcb23) and `solr-17849-index-query-screen` (b91f75ed93, which includes the header branch). This claim is the gate: spotless and the four IndexQuery and SearchHandler test classes on a fresh checkout of `b91f75ed93`. No code change.
- Capability tags used: ui, jvm-tests.
- Started: 2026-10-24T08:09:38Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pool-ui-SOLR-17849@b91f75ed93.
- Links: Ticket: SOLR-17849. Category: ui. Siblings: `claims/ui-volunteer-drafts-round-1-SOLR-17849.md` (vm6 check draft to Jane Sandberg, DONE, not posted). Supersedes: none.
- Decision ladder: rung 1 for the branch's test list and the files; the gate is the check. GL: the header and screen use no Canvas, DrawScope or graphicsLayer (to confirm by reading in the Result).
- Constraints: no JIRA or GitHub post, no PR. Commit as Nick Shanin with no trailers.

Heartbeat: 2026-10-24T08:09:38Z
