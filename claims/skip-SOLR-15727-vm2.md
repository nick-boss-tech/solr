# Claim: skip-SOLR-15727-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-15727, item 19 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 19. Start from `material/skip-audit/remediation-current.json` (item SOLR-15727) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T19:12:47Z (UTC).

Heartbeat: 2026-10-19T19:12:47Z

Result: 2026-10-19T19:12:47Z: keep-open. Code path: CommitTracker.java:73-95, UpdateLog.java:383 (numRecordsToKeep default 100). Recovery path (PeerSync and replay) not read. Not reproduced: needs a multi-node cluster run.

DONE: 2026-10-19T19:12:47Z. Deliverables: `drafts/SOLR-15727/record.md` and `drafts/SOLR-15727/jira-comment.md` on branch `solr-15727-draft-open` (1982c7f975), `material/skip-audit/queue-current.json` (SOLR-15727 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 19).
