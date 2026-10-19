# Claim: skip-SOLR-16930 (closeout)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Closeout of the audit verdict in `material/skip-audit/queue-current.json`; pinned head `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main).
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-16930); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T08:01:57Z (UTC).

Heartbeat: 2026-10-19T08:01:57Z

Result: 2026-10-19T08:02:42Z: keep-open. SolrResourceLoader.java:466-470 and 515-526: the legacy shim looks up the factory by name only and never compares the kind written in the config (group 5) with the expected type. Missing: the compatibility decision (reject the mismatch or keep and document it).

DONE: 2026-10-19T08:02:42Z. Deliverables: `drafts/SOLR-16930/record.md`, `drafts/SOLR-16930/jira-comment.md` on draft branch `solr-16930-draft-open` (head `523f44ad750a`), and the queue verdict in `material/skip-audit/queue-current.json`.
