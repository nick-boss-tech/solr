# Claim: skip-SOLR-12369-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:27:53Z. Remediation redo of batch 08, item 16, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 16, SOLR-12369).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:26:55Z (UTC).

Heartbeat: 2026-10-20T06:26:55Z

Result: 2026-10-20T06:27:53Z: keep-open on solr-12369-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 6baaa2d3f9, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, 2 comments; full description 190 characters, read from the Jira REST record. Code read: ZkController.java:2086-2115 (waitForShardId: waitForState 320 s, no forced refresh); :2054, :2058 (call sites); :2190-2215 (checkStateInZk). forceUpdateCollection not called from waitForShardId. A repeated core-create run not done.

DONE: 2026-10-20T06:27:53Z. Deliverables: drafts/SOLR-12369/record.md and jira-comment.md on solr-12369-draft-open (6baaa2d3f9); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
