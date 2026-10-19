# Claim: skip-SOLR-17219 (closeout)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Closeout of the audit verdict in `material/skip-audit/queue-current.json`; pinned head `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main).
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17219); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T07:43:06Z (UTC).

Heartbeat: 2026-10-19T07:43:06Z

Result: 2026-10-19T07:43:50Z: keep-open. ZkSolrResourceLoader.java:109-110 wraps non-session-expiry ZK errors as "Error opening" with no retry (the reported text). The 9.6.1 fixing commit is not identified on the fork, and no reproduction was run. Missing: the failing ZK error type and the fixing commit.

DONE: 2026-10-19T07:43:50Z. Deliverables: `drafts/SOLR-17219/record.md`, `drafts/SOLR-17219/jira-comment.md` on draft branch `solr-17219-draft-open` (head `502a75239bbd`), and the queue verdict in `material/skip-audit/queue-current.json`.
