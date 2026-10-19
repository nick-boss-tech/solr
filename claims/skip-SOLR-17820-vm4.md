# Claim: skip-SOLR-17820 (remediation, batch 01 item 1)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 1.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17820); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T13:05:53Z (UTC).

Heartbeat: 2026-10-19T13:05:53Z

Result: 2026-10-19T13:09:09Z: pr-needed, changed from keep-open. JIRA read 2026-10-19: Resolved, Fixed (commit 7e3b175021 on Apache main, not an ancestor of the pinned head 498a09aea9). Code path at the pinned head: `ClusterStatus.java:358` runs the health computation before `:375` runs the live-node cross-check. Not reproduced; the record says so.

DONE: 2026-10-19T13:09:09Z. Deliverables: `drafts/SOLR-17820/record.md` and `drafts/SOLR-17820/jira-comment.md` on draft branch `solr-17820-draft-pr` (head 7ccbf985b8); queue verdict `pr-needed`; batch line in `reports/skip-verdict-remediation-01.md`.
