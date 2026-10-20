# Claim: skip-SOLR-10917-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T08:04:03Z. Remediation redo of batch 10, item 14, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 14, SOLR-10917).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T08:02:39Z (UTC).

Heartbeat: 2026-10-20T08:02:39Z

Result: 2026-10-20T08:04:03Z: close-fixed on solr-10917-draft-close at 498a09aea97aac86c2cc7ac114113b946d249942 (head aadf0fccdf, unchanged verdict; earlier record replaced, now checks the test and the proxy path). Jira read this pass: Open, no resolution, 0 comments. Fix commit b8648c60e7 (SOLR-13793, "Limiting number of forwards to total replicas") is an ancestor of the pin (checked); changed code present at HttpSolrCall.java:930-933; proxy message at :638; test TestQueryingOnDownCollection present (not run). Linkage by code path; the commit does not cite SOLR-10917.

DONE: 2026-10-20T08:04:03Z. Deliverables: drafts/SOLR-10917/record.md and jira-comment.md on solr-10917-draft-close (aadf0fccdf); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
