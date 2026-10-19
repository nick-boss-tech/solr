# Claim: skip-SOLR-15948-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-15948, item 6 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 6. Start from `material/skip-audit/remediation-current.json` (item SOLR-15948) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T17:51:50Z (UTC).

Heartbeat: 2026-10-19T17:51:50Z

Result: 2026-10-19T17:51:50Z: keep-open. Code path at the pinned head: solr/bin/solr:513-570 (stop_solr: stop command, bounded wait, then kill -9). Not reproduced: needs the packaged server with a startup error. The server-side reason the process stays alive is not established.

DONE: 2026-10-19T17:51:50Z. Deliverables: `drafts/SOLR-15948/record.md` and `drafts/SOLR-15948/jira-comment.md` on branch `solr-15948-draft-open` (16ed7ac835), `material/skip-audit/queue-current.json` (SOLR-15948 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 6).
