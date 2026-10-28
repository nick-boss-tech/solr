# Claim: solr-17914-browser-fix-round-1-linux-gate

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: DONE. Deliverable: `receipts/SOLR-17914-browser-fix-round-1.md` (red; cause is shared wasm test resources, category c).
- Job or assignment: `assignments/solr-17914-browser-fix-round-1.md`, with the execution handoff `gates/solr-17914-browser-fix-round-1-windows-candidate.md`.
- Slice: the Linux browser proof for the candidate head `252e0a75bb0d6a9183c8cc879da60b809b52c242`. Steps 1 to 5 of the handoff: spotless preflight, original-head browser reproduction at `6170ba10ac`, SOLR-17913 base comparison at `0a04d2239e`, full `./gradlew -p ui check` at the candidate, and the four screenshots.
- Fix commits, if any, go only to `solr-17914-browser-fix-round-1`, category (a) only. Nothing is pushed to `solr-17914-create-collection-dialog-new-design`.
- Capability tags used: ui, gate.
- Started: 2026-10-28T06:58Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-17914-browser-fix-round-1@252e0a75bb.
- Links: Ticket or parent: SOLR-17914, PR #5258 (draft, head unchanged). Category: ui. Siblings: `claims/solr-17914-browser-fix-round-1-windows-implementation.md`, `claims/solr-17914-browser-check-round-1.md`. Supersedes: none.

Heartbeat: 2026-10-28T06:58Z

DONE: 2026-10-28T07:25Z, receipts/SOLR-17914-browser-fix-round-1.md. Red: 6 of 70 wasm browser tests fail on candidate 252e0a75bb, same as original. Nothing running.
