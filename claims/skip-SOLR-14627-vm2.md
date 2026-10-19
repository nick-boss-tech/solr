# Claim: skip-SOLR-14627-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-14627, item 9 of `assignments/pool-aaa-skip-verdict-remediation-04.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-04.md`, item 9. Start from `material/skip-audit/remediation-current.json` (item SOLR-14627) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T21:34:54Z (UTC).

Heartbeat: 2026-10-19T21:34:54Z

Result: 2026-10-19T21:34:54Z: keep-open. Failing call located: SolrQueryParserBase.java:648 delegates AS_SAME_TERM to Lucene QueryBuilder. Synonym input not in report; not reproduced.

DONE: 2026-10-19T21:34:54Z. Deliverables: `drafts/SOLR-14627/record.md` and `drafts/SOLR-14627/jira-comment.md` on branch `solr-14627-draft-open` (35434405d6), `material/skip-audit/queue-current.json` (SOLR-14627 keep-open on that branch), `reports/skip-verdict-remediation-04.md` (row 9).
