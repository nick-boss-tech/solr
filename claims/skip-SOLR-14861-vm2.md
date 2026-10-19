# Claim: skip-SOLR-14861-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-14861, item 4 of `assignments/pool-aaa-skip-verdict-remediation-04.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-04.md`, item 4. Start from `material/skip-audit/remediation-current.json` (item SOLR-14861) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T21:00:16Z (UTC).

Heartbeat: 2026-10-19T21:00:16Z

Result: 2026-10-19T21:00:16Z: keep-open. Shutdown path has the in-flight wait and executor drain (CoreContainer.java:1228-1262). No fixing commit identified (history search timed out); no repeated test run.

DONE: 2026-10-19T21:00:16Z. Deliverables: `drafts/SOLR-14861/record.md` and `drafts/SOLR-14861/jira-comment.md` on branch `solr-14861-draft-open` (7b69aea5b5), `material/skip-audit/queue-current.json` (SOLR-14861 keep-open on that branch), `reports/skip-verdict-remediation-04.md` (row 4).
