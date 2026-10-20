# Claim: skip-SOLR-9427-vm2

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: DONE (pr-needed, changed from the recorded keep-open). Remediation item 24 of `assignments/pool-aaa-skip-verdict-remediation-11.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-11.md` (batch 11, item 24), claim `claims/pool-aaa-skip-verdict-remediation-11.md`.
- Slice: SOLR-9427 closeout, steps 11 to 15 of pool/cron-prompt.md.
- Capability tags used: review, draft.
- Started: 2026-10-20T14:11:23Z (UTC).
- Links: Ticket: SOLR-9427. Category: Replication and tests. Siblings: none known. Supersedes: the recorded keep-open verdict (rounding-sensitive assertion found).
- Idempotency key: SKIP-SOLR-9427@498a09aea97aac86c2cc7ac114113b946d249942.

Heartbeat: 2026-10-20T14:11:23Z

Result: 2026-10-20T14:11:23Z: pr-needed at pinned head 498a09aea97aac86c2cc7ac114113b946d249942. Draft branch solr-9427-draft-pr at 6028ddeb42 (drafts/SOLR-9427/record.md, jira-comment.md). TestReplicationHandler.java:1419-1420, 1452, 1459: whole-second rounding in the throttled-time assertion. Seeded run not done.

DONE: 2026-10-20T14:11:23Z. Deliverables: drafts/SOLR-9427/record.md and jira-comment.md on solr-9427-draft-pr (6028ddeb42); material/skip-audit/queue-current.json (pr-needed); line in reports/skip-verdict-remediation-11.md.
