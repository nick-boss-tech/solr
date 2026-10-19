# Claim: skip-SOLR-17376 (remediation, batch 01 item 6)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 6.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17376); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T13:38:00Z (UTC).

Heartbeat: 2026-10-19T13:38:00Z

Result: 2026-10-19T13:40:01Z: keep-open, unchanged verdict. JIRA read 2026-10-19: Open, four comments. Code path at the pin: `HttpJettySolrClient.java:258-285` builds the client on Jetty's default buffer pool. Not reproduced; the missing piece is a direct-buffer count over a load run and the pool's limit.

DONE: 2026-10-19T13:40:01Z. Deliverables: `drafts/SOLR-17376/record.md` and `drafts/SOLR-17376/jira-comment.md` on draft branch `solr-17376-draft-open` (head 2dedf0a7a2); queue verdict `keep-open` (unchanged); batch line in `reports/skip-verdict-remediation-01.md`.
