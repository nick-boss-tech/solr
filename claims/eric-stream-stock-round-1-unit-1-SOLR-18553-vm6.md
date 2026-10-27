# Claim: eric-stream-stock-round-1 unit 1 (SOLR-18553, vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Job or assignment: `assignments/eric-stream-stock-round-1.md`, unit 1. The assignment file is not edited; this claim names the unit.
- Slice: unit 1 only, SOLR-18553 plurality and consistency audit of v2 params and path segments at apache/solr main `e59ce3c97d`.
- Capability tags used: review, draft.
- Started: 2026-10-27T00:55:00Z (UTC). Corrected: this claim's first draft carried estimated times; these are the actual times.
- Lease: ended at DONE. Attempts: 1. Idempotency key: eric-stock-u1-SOLR-18553@e59ce3c97d.
- Links: Ticket or parent: SOLR-18553 (reporter Eric Pugh). Category: review. Siblings: unit 2 SOLR-18449, unit 3 SOLR-18369 (census per addendum), unit 4 SOLR-18460, unit 5 fresh scan. Supersedes: the silent 2026-10-24 assignment `assignments/warm-ticket-evidence-units-round-2.md` unit 1.
- Decision ladder: rung 1 only (tally of the endpoint interfaces at the pinned SHA; PR 5064 review thread for the stated preference). No probe needed; no finding depends on a run.

Heartbeat: 2026-10-27T01:08:00Z

Result: 2026-10-27T01:08:00Z: four concrete findings (F1 `detailed` vs `xxxInfo`; F2 `collName` vs `collectionName`; F3 `/schema-designer/configs` vs `/configsets`; F4 camelCase segments `/purgeUnused`, `/requestHandlers`), with file and line citations. Draft is unposted and waits for Nick's go. Report `reports/solr-18553-plurality-audit-round-1.md`.

DONE: 2026-10-27T01:08:00Z. Deliverables: `reports/solr-18553-plurality-audit-round-1.md`, `jira/jira-comment-SOLR-18553-draft.md`.
