# Claim: skip-SOLR-16122-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-16122, item 9 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 9. Start from `material/skip-audit/remediation-current.json` (item SOLR-16122) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T18:19:50Z (UTC).

Heartbeat: 2026-10-19T18:19:50Z

Result: 2026-10-19T18:19:50Z: keep-open. The test is @AwaitsFix(SOLR-16122) at the pinned head (TestLeaderElectionZkExpiry.java:43). Reproduction: 8 iterations with the awaitsfix group enabled, 0 failures (log /workspace/gates/logs/vm2/16122/16122-flake-r2.log). Inconclusive for a 10 percent rate. Longer repeat not run.

DONE: 2026-10-19T18:19:50Z. Deliverables: `drafts/SOLR-16122/record.md` and `drafts/SOLR-16122/jira-comment.md` on branch `solr-16122-draft-open` (54ff0399a9), `material/skip-audit/queue-current.json` (SOLR-16122 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 9).
