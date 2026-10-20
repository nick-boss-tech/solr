# Claim: skip-SOLR-11776-vm2

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: DONE (keep-open, changed from the recorded close-obsolete). Remediation item 21 of `assignments/pool-aaa-skip-verdict-remediation-09.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-09.md` (batch 9, item 21), claim `claims/pool-aaa-skip-verdict-remediation-09.md`.
- Slice: SOLR-11776 closeout, steps 11 to 15 of pool/cron-prompt.md.
- Capability tags used: review, draft.
- Started: 2026-10-20T07:57:44Z (UTC).
- Links: Ticket: SOLR-11776. Category: Replication and recovery. Siblings: SOLR-12166, SOLR-11815. Supersedes: the recorded close-obsolete verdict (premise persists in the term mechanism).
- Idempotency key: SKIP-SOLR-11776@498a09aea97aac86c2cc7ac114113b946d249942.

Heartbeat: 2026-10-20T07:57:44Z

Result: 2026-10-20T07:57:44Z: keep-open at pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Draft branch solr-11776-draft-open at b32788e549 (drafts/SOLR-11776/record.md, jira-comment.md). Leader error path DistributedZkUpdateProcessor.java:1296-1331 still marks replicas for recovery via terms on any replica error; HttpPartitionTest present; no run.

DONE: 2026-10-20T07:57:44Z. Deliverables: drafts/SOLR-11776/record.md and jira-comment.md on solr-11776-draft-open; material/skip-audit/queue-current.json (keep-open); line in reports/skip-verdict-remediation-09.md.
