# Claim: skip-SOLR-15679-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-15679, item 22 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 22. Start from `material/skip-audit/remediation-current.json` (item SOLR-15679) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T19:29:36Z (UTC).

Heartbeat: 2026-10-19T19:29:36Z

Result: 2026-10-19T19:29:36Z: keep-open. Code path: SolrCore.java:1721-1730 (close() decrements refCount; count < 0 logs and asserts). The over-releasing caller is not identified; the report has no stack trace. Not reproduced.

DONE: 2026-10-19T19:29:36Z. Deliverables: `drafts/SOLR-15679/record.md` and `drafts/SOLR-15679/jira-comment.md` on branch `solr-15679-draft-open` (13704d7602), `material/skip-audit/queue-current.json` (SOLR-15679 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 22).
