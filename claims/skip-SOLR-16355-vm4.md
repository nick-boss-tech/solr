# Claim: skip-SOLR-16355 (remediation, batch 01 item 24)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 24.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-16355); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T15:06:30Z (UTC).

Heartbeat: 2026-10-19T15:06:30Z

Result: 2026-10-19T15:07:20Z: close-answered, changed from keep-open. The reference guide answers the question: `maxSegments` has no default and is optional (`indexing-with-update-handlers.adoc:142-150`), and the Solr 8 upgrade note says to set `maxSegments` to control the segment count after an optimize (`major-changes-in-solr-8.adoc:365-367`). Code default at `CommitUpdateCommand.java:56`. Not reproduced; the docs answer it.

DONE: 2026-10-19T15:07:20Z. Deliverables: `drafts/SOLR-16355/record.md` and `drafts/SOLR-16355/jira-comment.md` on the new draft branch `solr-16355-draft-close` (head 346bcd46af); queue verdict `close-answered`; batch line in `reports/skip-verdict-remediation-01.md`.
