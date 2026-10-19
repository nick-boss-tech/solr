# Claim: skip-SOLR-15858-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-15858, item 7 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 7. Start from `material/skip-audit/remediation-current.json` (item SOLR-15858) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T17:56:53Z (UTC).

Heartbeat: 2026-10-19T17:56:53Z

Result: 2026-10-19T17:56:53Z: close-obsolete (changed from keep-open). The named method ConfigSetsHandler.createZkNodeIfNotExistsAndSetData is absent at the pinned head (empty grep); removed by 1a922dc99aa (SOLR-15258), an ancestor. Current upload path: UploadConfigSet.java:101-115 and ZkConfigSetService.java:218 (makePath creates parents). End-to-end upload not run in this pass; listed as a gap in the record.

DONE: 2026-10-19T17:56:53Z. Deliverables: `drafts/SOLR-15858/record.md` and `drafts/SOLR-15858/jira-comment.md` on branch `solr-15858-draft-close` (bc02c94698), `material/skip-audit/queue-current.json` (SOLR-15858 close-obsolete on that branch), `reports/skip-verdict-remediation-02.md` (row 7, verdict changed).
