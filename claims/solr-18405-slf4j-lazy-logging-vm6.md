# Claim: SOLR-18405 SLF4J lazy logging on hot paths (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Job or assignment: none on the board. Chosen from Jira SOLR-18405 (reporter Eric Pugh, Open, Minor, updated 2026-08-26, one comment), from the no-PR list. Picked on Nick's go, 2026-10-27.
- Slice: measure how often hot-path log calls at apache/solr main `e59ce3c97ddb` pass unguarded method-call arguments; decide whether the ticket's change is worth making; draft a Jira comment only.
- Capability tags used: review, draft.
- Started: 2026-10-27T01:35:07Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18405@e59ce3c97d.
- Links: Ticket: SOLR-18405 (split from SOLR-14349). Category: review. Siblings: SOLR-18138 (DONE), SOLR-18145 (DONE).
- Decision ladder: rung 1 (counts from code at the pinned SHA; the ticket's own example).

Heartbeat: 2026-10-27T01:35:07Z

Result: 2026-10-27T01:36:19Z: 760 debug/trace calls in solr/core main; 253 guarded; no clearly expensive unguarded argument found. No code change. Draft asks Eric whether to close or pursue a rule.

DONE: 2026-10-27T01:36:19Z. Deliverables: `reports/solr-18405-slf4j-lazy-logging-round-1.md`, `jira/jira-comment-SOLR-18405-draft.md`.
