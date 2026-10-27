# Claim: SOLR-18546, Simplify getting started tutorial by using techproducts instead of gettingstarted (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE.
- Job or assignment: none on the board. Chosen from Jira SOLR-18546 (reporter Eric Pugh, Open, no comments), from the no-PR list. Picked on Nick's go, 2026-10-27.
- Slice: measure the Ref Guide and code at apache/solr main `e59ce3c97ddb`; draft a Jira comment only. No doc edits.
- Capability tags used: review, draft.
- Started: 2026-10-27T01:38:31Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-18546@e59ce3c97d.
- Links: Ticket: SOLR-18546. Category: review. Siblings: SOLR-18461 and SOLR-18546 share the examples surface; SOLR-18500 (existing draft) covers curl quoting.
- Decision ladder: rung 1 (Ref Guide and CLI source counts at the pinned SHA).

Heartbeat: 2026-10-27T01:38:31Z

Result: 2026-10-27T01:38:59Z: 177 Ref Guide lines in 36 files; CLI default at RunExampleTool.java:1188; tests assert the name. Docs and CLI default are different risks. No edit. Draft asks Eric which he means.

DONE: 2026-10-27T01:38:59Z. Deliverables: `reports/solr-18546-gettingstarted-round-1.md`, `jira/jira-comment-SOLR-18546-draft.md`.
