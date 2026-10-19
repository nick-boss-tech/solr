# Claim: skip-SOLR-14226-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-14226, item 21 of `assignments/pool-aaa-skip-verdict-remediation-04.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-04.md`, item 21. Start from `material/skip-audit/remediation-current.json` (item SOLR-14226) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T22:39:00Z (UTC).

Heartbeat: 2026-10-19T22:39:00Z

Result: 2026-10-19T22:39:00Z: close-fixed (changed from keep-open). Fix c7b58302d33 (SOLR-16929, ancestor of the pin), 401/403 check at SolrStream.java constructParser. Linked by symptom; the commit does not name this ticket. Reporter's test not run on the pin.

DONE: 2026-10-19T22:39:00Z. Deliverables: `drafts/SOLR-14226/record.md` and `drafts/SOLR-14226/jira-comment.md` on branch `solr-14226-draft-close` (30e0d90854), `material/skip-audit/queue-current.json` (SOLR-14226 close-fixed on that branch), `reports/skip-verdict-remediation-04.md` (row 21, verdict changed).
