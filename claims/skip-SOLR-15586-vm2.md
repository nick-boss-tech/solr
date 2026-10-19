# Claim: skip-SOLR-15586-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-15586, item 24 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 24. Start from `material/skip-audit/remediation-current.json` (item SOLR-15586) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T19:38:38Z (UTC).

Heartbeat: 2026-10-19T19:38:38Z

Result: 2026-10-19T19:38:38Z: keep-open. Code path: QueryComponent.java:173-175 (fl score sets GET_SCORES), ResultContext.java:58-62. Slowdown not measured; reporter's query parser and index needed. Not reproduced.

DONE: 2026-10-19T19:38:38Z. Deliverables: `drafts/SOLR-15586/record.md` and `drafts/SOLR-15586/jira-comment.md` on branch `solr-15586-draft-open` (805fbcd07f), `material/skip-audit/queue-current.json` (SOLR-15586 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 24).
