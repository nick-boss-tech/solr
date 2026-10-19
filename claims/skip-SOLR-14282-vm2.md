# Claim: skip-SOLR-14282-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-14282, item 19 of `assignments/pool-aaa-skip-verdict-remediation-04.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-04.md`, item 19. Start from `material/skip-audit/remediation-current.json` (item SOLR-14282) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T22:27:49Z (UTC).

Heartbeat: 2026-10-19T22:27:49Z

Result: 2026-10-19T22:27:49Z: keep-open. Copy-target skip is deliberate in RealTimeGetComponent.java:633, 879, 917; the real-time get guide does not describe it. Design decision open. Not run.

DONE: 2026-10-19T22:27:49Z. Deliverables: `drafts/SOLR-14282/record.md` and `drafts/SOLR-14282/jira-comment.md` on branch `solr-14282-draft-open` (ebda676bdb), `material/skip-audit/queue-current.json` (SOLR-14282 keep-open on that branch), `reports/skip-verdict-remediation-04.md` (row 19).
