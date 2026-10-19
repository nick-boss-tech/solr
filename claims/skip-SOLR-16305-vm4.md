# Claim: skip-SOLR-16305 (remediation, batch 01 item 25)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 25.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-16305); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T15:10:50Z (UTC).

Heartbeat: 2026-10-19T15:10:50Z

Result: 2026-10-19T15:11:49Z: keep-open, unchanged verdict. JIRA read 2026-10-19: Open, seventeen comments (design debate). SOLR-17006 (`0f5a8f929e`) is an ancestor of the pin and covers creation-time persistence only; the modify-to-core gap is open. Not reproduced; the missing piece is the maintainers' decision on the property API.

DONE: 2026-10-19T15:11:49Z. Deliverables: `drafts/SOLR-16305/record.md` and `drafts/SOLR-16305/jira-comment.md` on draft branch `solr-16305-draft-open` (head d32e52a658); queue verdict `keep-open` (unchanged); batch line in `reports/skip-verdict-remediation-01.md`.
