# Claim: skip-SOLR-17920 (closeout)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout verdict keep-open, recorded in `material/skip-audit/queue-current.json` at pinned head `498a09aea97aac86c2cc7ac114113b946d249942`.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17920); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T06:49:00Z (UTC).

Heartbeat: 2026-10-19T06:49:00Z

Result: 2026-10-19T06:58:00Z: keep-open. Code paths read at the pinned head: `SolrDeletionPolicy.java`, `SolrCore.java:865, 1235, 1800-1802`. The reference guide has no answer, so close-answered does not apply. No reproduction. Missing piece: the open deleted files for the Solr process while usage is high, and the Solr version. Conclusive for the mechanism candidate; not for the cause on this node.

DONE: 2026-10-19T06:58:00Z. Deliverables: `drafts/SOLR-17920/record.md`, `drafts/SOLR-17920/jira-comment.md` on draft branch `solr-17920-draft-open` (head `b0b08394f7`).
