# Claim: skip-SOLR-12302-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:43:21Z. Remediation redo of batch 08, item 20, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 20, SOLR-12302).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:42:36Z (UTC).

Heartbeat: 2026-10-20T06:42:36Z

Result: 2026-10-20T06:43:21Z: keep-open on solr-12302-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head d341fa6e9e, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, 4 comments, updated 2020-11-25; link: duplicate SOLR-12980 (Resolved). Code read: DirectUpdateHandler2.java:419-445 (the "Exception writing document id" BAD_REQUEST path); BasicDistributedZkTest.java:751 (distrib-dup-test-chain-explicit). No fix commit at the pin. A run of BasicDistributedZkTest with the seeds in the ticket not done; the Gradle build was not run.

DONE: 2026-10-20T06:43:21Z. Deliverables: drafts/SOLR-12302/record.md and jira-comment.md on solr-12302-draft-open (d341fa6e9e); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
