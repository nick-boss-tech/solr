# Claim: skip-SOLR-14966-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-14966, item 2 of `assignments/pool-aaa-skip-verdict-remediation-04.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-04.md`, item 2. Start from `material/skip-audit/remediation-current.json` (item SOLR-14966) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T20:23:03Z (UTC).

Heartbeat: 2026-10-19T20:23:03Z

Result: 2026-10-19T20:23:03Z: keep-open (changed from close-fixed). stateVer comparison still uses the uncached overload (ZkStateReader.java:538; ClusterState.java:128-144). No fixing commit on the path. Read counts not measured; staleness trade-off is an owner decision.

DONE: 2026-10-19T20:23:03Z. Deliverables: `drafts/SOLR-14966/record.md` and `drafts/SOLR-14966/jira-comment.md` on branch `solr-14966-draft-open` (dfb26c4f72), `material/skip-audit/queue-current.json` (SOLR-14966 keep-open on that branch), `reports/skip-verdict-remediation-04.md` (row 2, verdict changed).
