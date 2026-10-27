# Claim: christos-reply-17849-header-scope-round-1

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE (2026-10-27T20:56:43Z); deliverables below.
- Job or assignment: `assignments/christos-reply-17849-header-scope-round-1.md`.
- Slice: the whole assignment: reduced branch `solr-17849-header-submit` (header only), gate at its exact head, PR draft. No PR opening, no Jira or GitHub post.
- Capability tags used: ui, jvm-tests, gate, implementation.
- Started: 2026-10-27T20:48:34Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: 1. Idempotency key: christos-reply-17849-header-scope@<reduced head sha, set when committed>.
- Links: Ticket or parent: SOLR-17849 (Christos Malliaridis, Jira comment 18133044). Category: ui. Siblings: `claims/pool-ui-SOLR-17849.md` (gate of the source branch, DONE); SOLR-17850 is in the source branch and stays out. Supersedes: none.
- Source tip check: the live `solr-17849-index-query-screen` tip is `375143456a` (2026-10-27). It is the gated head `b91f75ed93` plus removal of `pr-draft/SOLR-17849.md` and `pr-draft/SOLR-17850.md`. The UI code is identical at both, so the gate on `b91f75ed93` covers the code.
- Decision ladder: rung 1. The header imports only `RequestHandlerOption` (domain, moved out of the header at the tip) and four header strings. The data layer, state holder and wiring are not imports of the header or its test, so none is included. Recorded in the deliverable.

Heartbeat: 2026-10-27T20:56:43Z

Result: 2026-10-27T20:56:43Z: reduced branch `solr-17849-header-submit` at `5d90d83d5b` (new on the fork, remote SHA verified), gate job `header-17849-5d90d83d5b-205246`: spotless rc=0; `IndexQueryHeaderTest` 5 of 5 passed, 0 failures, 0 errors; conclusive for the software-rendered JVM tests at this head. Report `reports/christos-reply-17849-header-scope-round-1.md`.

DONE: 2026-10-27T20:56:43Z. Deliverables: `reports/christos-reply-17849-header-scope-round-1.md`, `drafts/pr-solr-17849-header.md`, branch `nick-boss-tech/solr:solr-17849-header-submit` at `5d90d83d5b`.
