# Claim: skip-SOLR-16526 (remediation, batch 01 item 14)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 14.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-16526); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T14:15:00Z (UTC).

Heartbeat: 2026-10-19T14:15:00Z

Result: 2026-10-19T14:16:30Z: keep-open, unchanged verdict. JIRA read 2026-10-19: Open, no comments. Solr passes the advertised host to Jetty's URI check unchecked (`AddressUtils.java:25-60`); the docs use underscore-free names and have no warning. Not reproduced; the missing piece is the Solr owner decision on validating or documenting the host.

DONE: 2026-10-19T14:16:30Z. Deliverables: `drafts/SOLR-16526/record.md` and `drafts/SOLR-16526/jira-comment.md` on draft branch `solr-16526-draft-open` (head 370da4a02c); queue verdict `keep-open` (unchanged); batch line in `reports/skip-verdict-remediation-01.md`.
