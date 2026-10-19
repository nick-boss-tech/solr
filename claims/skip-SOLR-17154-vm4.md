# Claim: skip-SOLR-17154 (remediation, batch 01 item 9)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 9.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17154); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T13:53:05Z (UTC).

Heartbeat: 2026-10-19T13:53:05Z

Result: 2026-10-19T13:54:25Z: keep-open, unchanged verdict. JIRA read 2026-10-19: Open, two comments. Upstream CALCITE-2616 read on the Apache JIRA: Open, no fix version; the pin uses Calcite 1.42.0. No Solr charset code in the SQL/JDBC paths. Not reproduced; the missing pieces are a tested charset workaround and the Solr decision on it.

DONE: 2026-10-19T13:54:25Z. Deliverables: `drafts/SOLR-17154/record.md` and `drafts/SOLR-17154/jira-comment.md` on draft branch `solr-17154-draft-open` (head 928f40a08a); queue verdict `keep-open` (unchanged); batch line in `reports/skip-verdict-remediation-01.md`.
