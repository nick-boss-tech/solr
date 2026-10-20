# Claim: skip-SOLR-9468-vm2

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: DONE (keep-open, changed from the recorded close-fixed). Remediation item 22 of `assignments/pool-aaa-skip-verdict-remediation-11.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-11.md` (batch 11, item 22), claim `claims/pool-aaa-skip-verdict-remediation-11.md`.
- Slice: SOLR-9468 closeout, steps 11 to 15 of pool/cron-prompt.md.
- Capability tags used: review, draft.
- Started: 2026-10-20T14:00:51Z (UTC).
- Links: Ticket: SOLR-9468. Category: Replication and backup. Siblings: none known. Supersedes: the recorded close-fixed verdict (no fixing commit named).
- Idempotency key: SKIP-SOLR-9468@498a09aea97aac86c2cc7ac114113b946d249942.

Heartbeat: 2026-10-20T14:00:51Z

Result: 2026-10-20T14:00:51Z: keep-open at pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Draft branch solr-9468-draft-open at 3d9db25b0a (drafts/SOLR-9468/record.md, jira-comment.md). ReplicationHandler.java:655-672 and 374-380 read; backup run not done.

Correction: a keep-open record was first pushed to solr-9468-draft-close by mistake (commit 58fcbc4c9e); that commit was reverted on the close branch (43d419502c) so the close branch is back to its prior state. The record is on solr-9468-draft-open only.

DONE: 2026-10-20T14:00:51Z. Deliverables: drafts/SOLR-9468/record.md and jira-comment.md on solr-9468-draft-open (3d9db25b0a); material/skip-audit/queue-current.json (keep-open); line in reports/skip-verdict-remediation-11.md.
