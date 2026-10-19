# Claim: skip-SOLR-16062-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-16062, item 13 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 13. Start from `material/skip-audit/remediation-current.json` (item SOLR-16062) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T18:39:36Z (UTC).

Heartbeat: 2026-10-19T18:39:36Z

Result: 2026-10-19T18:39:36Z: keep-open. Code path: solr/modules/ltr/.../LTRRescorer.java:119-135 (per-leaf rerank), LTRScoringQuery.java. Not reproduced: the report gives no query, feature store, model or index details.

DONE: 2026-10-19T18:39:36Z. Deliverables: `drafts/SOLR-16062/record.md` and `drafts/SOLR-16062/jira-comment.md` on branch `solr-16062-draft-open` (969460caa4), `material/skip-audit/queue-current.json` (SOLR-16062 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 13).
