# Claim: skip-SOLR-14668-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-14668, item 7 of `assignments/pool-aaa-skip-verdict-remediation-04.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-04.md`, item 7. Start from `material/skip-audit/remediation-current.json` (item SOLR-14668) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T21:19:45Z (UTC).

Heartbeat: 2026-10-19T21:19:45Z

Result: 2026-10-19T21:19:45Z: keep-open. Routing design (CompositeIdRouter.java:80) produces the duplicate; cleanup is a maintainer decision. Not reproduced.

DONE: 2026-10-19T21:19:45Z. Deliverables: `drafts/SOLR-14668/record.md` and `drafts/SOLR-14668/jira-comment.md` on branch `solr-14668-draft-open` (5f1b31b1a2), `material/skip-audit/queue-current.json` (SOLR-14668 keep-open on that branch), `reports/skip-verdict-remediation-04.md` (row 7).
