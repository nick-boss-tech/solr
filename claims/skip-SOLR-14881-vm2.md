# Claim: skip-SOLR-14881-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-14881, item 3 of `assignments/pool-aaa-skip-verdict-remediation-04.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-04.md`, item 3. Start from `material/skip-audit/remediation-current.json` (item SOLR-14881) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T20:52:45Z (UTC).

Heartbeat: 2026-10-19T20:52:45Z

Result: 2026-10-19T20:52:45Z: close-fixed (changed from keep-open). Fix 73c06e35e8b on main (ancestor of the pin); changed line present at CoreContainer.java:2060. ManagedSchemaRoundRobinCloudTest: 1 run at the pin, BUILD SUCCESSFUL, 0 failures (log /workspace/gates/logs/vm2/14881/14881-r2.log). One run only; the ticket has no failure output.

DONE: 2026-10-19T20:52:45Z. Deliverables: `drafts/SOLR-14881/record.md` and `drafts/SOLR-14881/jira-comment.md` on branch `solr-14881-draft-close` (8ffbc4f7e2), `material/skip-audit/queue-current.json` (SOLR-14881 close-fixed on that branch), `reports/skip-verdict-remediation-04.md` (row 3, verdict changed).
