# Claim: skip-SOLR-11675-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T07:14:39Z. Remediation redo of batch 10, item 2, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 2, SOLR-11675).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T07:13:38Z (UTC).

Heartbeat: 2026-10-20T07:13:38Z

Result: 2026-10-20T07:14:39Z: keep-open on solr-11675-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head b3dc563370, unchanged verdict; bulk audit-derived record replaced). Jira read this pass: Open, no resolution, 1 comment. Code read: V2HttpCall.java:111, :133 (get(1) guarded by size > 1), :114-115, :233-275 (getApiInfo returns null for an unsupported method), :169-190, :202. The 2017 IndexOutOfBounds path is not reachable for /api/c by reading; the client response for DELETE, PATCH, PROPFIND not established. A server run not done.

DONE: 2026-10-20T07:14:39Z. Deliverables: drafts/SOLR-11675/record.md and jira-comment.md on solr-11675-draft-open (b3dc563370); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
