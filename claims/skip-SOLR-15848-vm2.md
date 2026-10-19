# Claim: skip-SOLR-15848-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-15848, item 17 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 17. Start from `material/skip-audit/remediation-current.json` (item SOLR-15848) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T19:01:47Z (UTC).

Heartbeat: 2026-10-19T19:01:47Z

Result: 2026-10-19T19:01:47Z: keep-open (changed from close-fixed). Fix commits b895ff469e4 and 0b002b11819 are on branch_8_11, not on main. ScheduledTriggerIntegrationTest removed from main by cc0c111949d. Three surviving PR #2632 files on main carry no BadApple or AwaitsFix marker (checked). Owner decision on branch_8_11 open; main-side stability not run.

DONE: 2026-10-19T19:01:47Z. Deliverables: `drafts/SOLR-15848/record.md` and `drafts/SOLR-15848/jira-comment.md` on branch `solr-15848-draft-open` (52071c3667), `material/skip-audit/queue-current.json` (SOLR-15848 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 17, verdict changed).
