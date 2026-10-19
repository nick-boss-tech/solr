# Claim: skip-SOLR-16020-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-16020, item 4 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 4. Start from `material/skip-audit/remediation-current.json` (item SOLR-16020) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T17:44:02Z (UTC).

Heartbeat: 2026-10-19T17:44:02Z

Result: 2026-10-19T17:44:02Z: keep-open. The failing computation is in Lucene (BaseFragmentsBuilder), not in this repository; Solr's call site is DefaultSolrHighlighter.java:539-541. Not reproduced: the report has no query or document. Missing piece listed in the record.

DONE: 2026-10-19T17:44:02Z. Deliverables: `drafts/SOLR-16020/record.md` and `drafts/SOLR-16020/jira-comment.md` on branch `solr-16020-draft-open` (219fca2252), `material/skip-audit/queue-current.json` (SOLR-16020 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 4).
