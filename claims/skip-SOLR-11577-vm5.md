# Claim: skip-SOLR-11577-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T07:38:27Z. Remediation redo of batch 10, item 8, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 8, SOLR-11577).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T07:37:39Z (UTC).

Heartbeat: 2026-10-20T07:37:39Z

Result: 2026-10-20T07:38:27Z: keep-open on solr-11577-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 33b4527fff, unchanged verdict; bulk audit-derived record replaced). Jira read this pass: Open, no resolution, 0 comments. Code read: HttpShardHandler.java:194-201 (getURLs, per-request shardToURLs cache), :245-257 (submit: empty urls records SERVICE_UNAVAILABLE "no servers hosting shard" and returns; no refresh or retry). No forceUpdate or retry in HttpShardHandler. Restart-and-query run not done.

DONE: 2026-10-20T07:38:27Z. Deliverables: drafts/SOLR-11577/record.md and jira-comment.md on solr-11577-draft-open (33b4527fff); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
