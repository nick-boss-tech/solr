# Claim: skip-SOLR-16792 (closeout)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Closeout of the audit verdict in `material/skip-audit/queue-current.json`; pinned head `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main).
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-16792); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T08:05:58Z (UTC).

Heartbeat: 2026-10-19T08:05:58Z

Result: 2026-10-19T08:06:38Z: keep-open. CoreStatus.java:118-160 unguarded derefs need a loaded core; lastPublished is never null (CloudDescriptor.java:41, ZkController.java:1917). No path found for the reported NPE. Missing: the reproduction the maintainer asked for (not run).

DONE: 2026-10-19T08:06:38Z. Deliverables: `drafts/SOLR-16792/record.md`, `drafts/SOLR-16792/jira-comment.md` on draft branch `solr-16792-draft-open` (head `8cb805ac4575`), and the queue verdict in `material/skip-audit/queue-current.json`.
