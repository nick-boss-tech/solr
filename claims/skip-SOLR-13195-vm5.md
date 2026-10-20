# Claim: skip-SOLR-13195-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T03:50:57Z. Remediation redo of batch 07, item 9, under `assignments/pool-aaa-skip-verdict-remediation-07.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-07.md` (item 9, SOLR-13195).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T03:49:46Z (UTC).

Heartbeat: 2026-10-20T03:49:46Z

Result: 2026-10-20T03:50:57Z: keep-open on solr-13195-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head ed6ec54841, unchanged verdict). Jira read this pass: Open, no resolution, updated 2021-02-01. Code read: SearchHandler.java:312-315, :326-334 (distributed gate); HttpShardHandler.java:555 (rb.shards); FacetComponent.java:373. No commit names the ticket. The standalone and cloud run of both requests not done.

DONE: 2026-10-20T03:50:57Z. Deliverables: drafts/SOLR-13195/record.md and jira-comment.md on solr-13195-draft-open (ed6ec54841); queue-current set; reports/skip-verdict-remediation-07.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
