# Claim: skip-SOLR-17615 (closeout)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Closeout of the audit verdict in `material/skip-audit/queue-current.json`; pinned head `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main).
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17615); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T07:26:58Z (UTC).

Heartbeat: 2026-10-19T07:26:58Z

Result: 2026-10-19T07:27:37Z: close-answered. The documented option excludeTags on the knn parser answers the facet drill-down question (dense-vector-search.adoc:814-817, option at :397; tested in KnnQParserTest). The request to change the default pre-filter behavior is a product decision, not closed here.

DONE: 2026-10-19T07:27:37Z. Deliverables: `drafts/SOLR-17615/record.md`, `drafts/SOLR-17615/jira-comment.md` on draft branch `solr-17615-draft-close` (head `d7bbf66c31b2`), and the queue verdict in `material/skip-audit/queue-current.json`.
