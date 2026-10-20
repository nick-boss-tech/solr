# Claim: skip-SOLR-12268-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T07:00:26Z. Remediation redo of batch 08, item 24, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 24, SOLR-12268).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:59:37Z (UTC).

Heartbeat: 2026-10-20T06:59:37Z

Result: 2026-10-20T07:00:26Z: keep-open on solr-12268-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 3f517cd161, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, 4 comments. Code read: LukeRequestHandler.java:1080-1095 (getIndexInfo sets current via isCurrent), :1112-1118 (closeSafe); CoreStatus.java:51 (INDEX_INFO_DEFAULT_VALUE true), :64-73, :158-168 (index info built on the cores status path by default). Blocking time during a reopen not measured.

DONE: 2026-10-20T07:00:26Z. Deliverables: drafts/SOLR-12268/record.md and jira-comment.md on solr-12268-draft-open (3f517cd161); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
