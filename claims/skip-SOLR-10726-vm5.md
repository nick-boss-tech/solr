# Claim: skip-SOLR-10726-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T08:39:27Z. Remediation redo of batch 10, item 23, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 23, SOLR-10726).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T08:38:38Z (UTC).

Heartbeat: 2026-10-20T08:38:38Z

Result: 2026-10-20T08:39:27Z: keep-open on solr-10726-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head fa0b423935, unchanged verdict; earlier record replaced). Jira read this pass: Open, no resolution, 1 comment. UpdateLog.java:940-955 (openRealtimeSearcher, openNewSearcher(true, true)), :958-968 (deleteAll). Other call sites on replica and replay paths not fully traced. No fix commit. Listener-count run for replica add and restart not done.

DONE: 2026-10-20T08:39:27Z. Deliverables: drafts/SOLR-10726/record.md and jira-comment.md on solr-10726-draft-open (fa0b423935); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
