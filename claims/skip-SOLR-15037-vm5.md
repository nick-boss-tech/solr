# Claim: skip-SOLR-15037-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-19T20:25:00Z. Remediation redo of batch 03, item 23, under `assignments/pool-aaa-skip-verdict-remediation-03.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-03.md` (item 23, SOLR-15037).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T20:09:30Z (UTC).

Heartbeat: 2026-10-19T20:09:30Z

Append one heartbeat line at least once an hour while the claim is active, and push it.

Result: 2026-10-19T20:25:00Z: keep-open on solr-15037-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 54906d34ad). Jira read this pass: Open, no resolution, updated 2021-01-12. Cited commit 669aff2108 absent from fork. Code path read: SchemaManager.java:146-160 (handling node reloads synchronously), SolrCore.java:3424-3440 (listener reload on watch). Multi-node run not done.

DONE: 2026-10-19T20:25:00Z. Deliverables: drafts/SOLR-15037/record.md and jira-comment.md on solr-15037-draft-open (54906d34ad); reports/skip-verdict-remediation-03.md line.
