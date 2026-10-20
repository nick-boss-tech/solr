# Claim: skip-SOLR-12315-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:39:28Z. Remediation redo of batch 08, item 19, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 19, SOLR-12315).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:38:37Z (UTC).

Heartbeat: 2026-10-20T06:38:37Z

Result: 2026-10-20T06:39:28Z: keep-open on solr-12315-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head c892a17ce5, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, 2 comments; full description read through the Jira REST record. Code read: QueryComponent.java:836-846 (per-shard rows), :1729-1745 (shard-side group collection limit), :276-296 (group sort spec). The merge across shards and the request planning the reporter describes not traced. A grouped-query run at rows 1 to 10 not done.

DONE: 2026-10-20T06:39:28Z. Deliverables: drafts/SOLR-12315/record.md and jira-comment.md on solr-12315-draft-open (c892a17ce5); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
