# Claim: solr-17914-wasm-test-resources-vm4

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: `assignments/solr-17914-browser-fix-round-1.md`, follow-on to `receipts/SOLR-17914-browser-fix-round-1.md` (red: wasm test bundle has no Compose string resources).
- Slice: make the wasm browser test bundle serve `composeResources` (shared `ui/shared` build config only), then rerun the browser leg and full `ui check` at the PR #5258 branch head plus the harness commit. Dialog test source is not changed.
- Owner direction 2026-10-28: land the patches on PR #5258 and do as much verification and patching as possible.
- Capability tags used: ui, gate.
- Started: 2026-10-28T16:05Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-17914-wasm-test-resources@6170ba10ac.
- Links: Ticket or parent: SOLR-17914, PR #5258 (draft). Category: ui. Siblings: `claims/solr-17914-browser-fix-round-1-linux-gate.md`. Supersedes: none.

Heartbeat: 2026-10-28T16:05Z
