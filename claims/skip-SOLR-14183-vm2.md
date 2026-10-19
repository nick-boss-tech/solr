# Claim: skip-SOLR-14183-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-14183, item 23 of `assignments/pool-aaa-skip-verdict-remediation-04.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-04.md`, item 23. Start from `material/skip-audit/remediation-current.json` (item SOLR-14183) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T22:48:10Z (UTC).

Heartbeat: 2026-10-19T22:48:10Z

Result: 2026-10-19T22:48:10Z: keep-open. REQUESTRECOVERY_OP (CoreAdminOperation.java:151-165) calls doRecovery asynchronously; RECOVERING not set in the handler. Not reproduced; design decision open.

DONE: 2026-10-19T22:48:10Z. Deliverables: `drafts/SOLR-14183/record.md` and `drafts/SOLR-14183/jira-comment.md` on branch `solr-14183-draft-open` (87dbee3563), `material/skip-audit/queue-current.json` (SOLR-14183 keep-open on that branch), `reports/skip-verdict-remediation-04.md` (row 23).
