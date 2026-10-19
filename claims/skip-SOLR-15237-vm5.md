# Claim: skip-SOLR-15237-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-19T20:05:00Z. Remediation redo of batch 03, item 19, under `assignments/pool-aaa-skip-verdict-remediation-03.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-03.md` (item 19, SOLR-15237).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T19:51:30Z (UTC).

Heartbeat: 2026-10-19T19:51:30Z

Append one heartbeat line at least once an hour while the claim is active, and push it.

Result: 2026-10-19T20:05:00Z: pr-needed on solr-15237-draft-pr at 498a09aea97aac86c2cc7ac114113b946d249942 (head c863d4eb81). Jira read this pass: Open, no resolution, updated 2024-02-27 (reproduces on 9.0 and 9.5 per the comment). Code path read: CoreContainer.java:603-611, :839-842; SearchHandler.java:190-191; BasicAuthPlugin.java:48-49, :215-216; PKIAuthenticationPlugin.java:327. Earlier verdict keep-open kept on solr-15237-draft-open.

DONE: 2026-10-19T20:05:00Z. Deliverables: drafts/SOLR-15237/record.md and jira-comment.md on solr-15237-draft-pr (c863d4eb81); reports/skip-verdict-remediation-03.md line.
