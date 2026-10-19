# Claim: skip-SOLR-17881 (closeout)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Closeout of the audit-1 verdict `deferred` in `material/skip-audit/queue-current.json`; pinned head `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main).
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17881); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T07:00:51Z (UTC).

Heartbeat: 2026-10-19T07:00:51Z

Result: 2026-10-19T07:02:26Z: keep-open. Code path read at the pinned head (SolrIndexSearcher.java:2311-2324, CoreContainer.java:450-454, SolrCore.java:2141-2178, IndexFingerprint.java:111-124). The fingerprint opens no file itself. No measurement yet; the missing piece is an fd count across repeated calls on a 500-segment index.

DONE: 2026-10-19T07:02:26Z. Deliverables: `drafts/SOLR-17881/record.md`, `drafts/SOLR-17881/jira-comment.md` on draft branch `solr-17881-draft-open` (head `5ccf4fe5074f255e937dd378d46a81fdee8255d4`), and the queue verdict in `material/skip-audit/queue-current.json`.
