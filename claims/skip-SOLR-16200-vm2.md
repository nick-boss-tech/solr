# Claim: skip-SOLR-16200-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-16200, item 2 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 2. Start from `material/skip-audit/remediation-current.json` (item SOLR-16200) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T17:35:51Z (UTC).

Heartbeat: 2026-10-19T17:35:51Z

Result: 2026-10-19T17:35:51Z: keep-open. Code path read at the pinned head: solr/server/etc/jetty.xml:70-74 (ForwardedRequestCustomizer commented out); the root redirect is not in Solr's servlet code. Reproduction not attempted in this pass (needs the packaged server behind a proxy). Missing piece listed in the record.

DONE: 2026-10-19T17:35:51Z. Deliverables: `drafts/SOLR-16200/record.md` and `drafts/SOLR-16200/jira-comment.md` on branch `solr-16200-draft-open` (ec3643f2a6), `material/skip-audit/queue-current.json` (SOLR-16200 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 2).
