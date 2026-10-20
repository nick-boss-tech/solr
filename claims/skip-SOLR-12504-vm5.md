# Claim: skip-SOLR-12504-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:08:03Z. Remediation redo of batch 08, item 12, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 12, SOLR-12504).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:06:52Z (UTC).

Heartbeat: 2026-10-20T06:06:52Z

Result: 2026-10-20T06:08:03Z: keep-open on solr-12504-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 7d65888718, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, no comments. Code read: RealTimeGetComponent.java processGetVersions (about 1229-1265): fingerprint then recentUpdates.getVersions, two reads with no shared snapshot; PeerSync.java compareFingerprint (506-516) bounds the local fingerprint to the other side's max version. A concurrency reproduction not run; the fix is a design choice.

DONE: 2026-10-20T06:08:03Z. Deliverables: drafts/SOLR-12504/record.md and jira-comment.md on solr-12504-draft-open (7d65888718); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
