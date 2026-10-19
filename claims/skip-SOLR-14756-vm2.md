# Claim: skip-SOLR-14756-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-14756, item 6 of `assignments/pool-aaa-skip-verdict-remediation-04.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-04.md`, item 6. Start from `material/skip-audit/remediation-current.json` (item SOLR-14756) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T21:13:38Z (UTC).

Heartbeat: 2026-10-19T21:13:38Z

Result: 2026-10-19T21:13:38Z: keep-open. Failing throw at RecoveryStrategy.java:260 (replicate, lines 226-260). Leader-side cause not in the report; not reproduced.

DONE: 2026-10-19T21:13:38Z. Deliverables: `drafts/SOLR-14756/record.md` and `drafts/SOLR-14756/jira-comment.md` on branch `solr-14756-draft-open` (6c122435a3), `material/skip-audit/queue-current.json` (SOLR-14756 keep-open on that branch), `reports/skip-verdict-remediation-04.md` (row 6).
