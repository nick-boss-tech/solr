# Claim: skip-SOLR-16084-vm2

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE. Remediation redo of SOLR-16084, item 11 of `assignments/pool-aaa-skip-verdict-remediation-02.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-02.md`, item 11. Start from `material/skip-audit/remediation-current.json` (item SOLR-16084) and `reports/skip-verdict-audit.md`.
- Slice: the whole ticket, redone under steps 11 to 15 of `pool/cron-prompt.md` at the pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Capability tags used: review, draft.
- Started: 2026-10-19T18:31:14Z (UTC).

Heartbeat: 2026-10-19T18:31:14Z

Result: 2026-10-19T18:31:14Z: keep-open. The client in the report (Http2SolrClient) was renamed to HttpJettySolrClient and moved to Jetty 12 by 2c55000296b (SOLR-18005). Code path: solr/solrj-jetty/.../HttpJettySolrClient.java:224-261 (SSL factory and ClientConnector). Not reproduced: needs an HTTPS server and the current client.

DONE: 2026-10-19T18:31:14Z. Deliverables: `drafts/SOLR-16084/record.md` and `drafts/SOLR-16084/jira-comment.md` on branch `solr-16084-draft-open` (447fef0e91), `material/skip-audit/queue-current.json` (SOLR-16084 keep-open on that branch), `reports/skip-verdict-remediation-02.md` (row 11).
