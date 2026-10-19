# Claim: skip-SOLR-14493-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-14493, item 14 of `assignments/pool-aaa-skip-verdict-remediation-04.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-04.md`, item 14. Start from `material/skip-audit/remediation-current.json` (item SOLR-14493) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T22:02:02Z (UTC).

Heartbeat: 2026-10-19T22:02:02Z

Result: 2026-10-19T22:02:02Z: close-fixed (changed from keep-open). Guard ColStatus.java:163-165 added by 83770854a35 (SOLR-14506, ancestor of the pin). Linked by symptom and code path; the commit does not name this ticket. Reporter's sequence not run on the pin.

DONE: 2026-10-19T22:02:02Z. Deliverables: `drafts/SOLR-14493/record.md` and `drafts/SOLR-14493/jira-comment.md` on branch `solr-14493-draft-close` (621020ae01), `material/skip-audit/queue-current.json` (SOLR-14493 close-fixed on that branch), `reports/skip-verdict-remediation-04.md` (row 14, verdict changed).
