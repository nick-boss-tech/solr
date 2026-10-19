# Claim: skip-SOLR-15585-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-15585, item 25 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 25. Start from `material/skip-audit/remediation-current.json` (item SOLR-15585) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T19:45:03Z (UTC).

Heartbeat: 2026-10-19T19:45:03Z

Result: 2026-10-19T19:45:03Z: keep-open. Code path: ReplicateFromLeader.java:83-89, 140-145 (PULL poll interval, default 3 s from commit settings). Shutdown path not read; cluster reproduction not run.

DONE: 2026-10-19T19:45:03Z. Deliverables: `drafts/SOLR-15585/record.md` and `drafts/SOLR-15585/jira-comment.md` on branch `solr-15585-draft-open` (876656abef), `material/skip-audit/queue-current.json` (SOLR-15585 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 25).
