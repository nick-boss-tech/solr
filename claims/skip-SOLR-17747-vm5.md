# Claim: skip-SOLR-17747 (closeout)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Closeout of the audit verdict in `material/skip-audit/queue-current.json`; pinned head `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main).
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17747); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T07:17:00Z (UTC).

Heartbeat: 2026-10-19T07:17:00Z

Result: 2026-10-19T07:19:31Z: keep-open. Optimize path read at the pinned head; Lucene 10.4.0 probe (small segments) merged to one segment under both size limits, so the default path is not a plain no-op for small segments. The Solr-level reproduction with the report's schema, request and segment counts is the missing piece.

DONE: 2026-10-19T07:19:31Z. Deliverables: `drafts/SOLR-17747/record.md`, `drafts/SOLR-17747/jira-comment.md` on draft branch `solr-17747-draft-open` (head `a226ce492268`), probe in `/workspace/gates/logs/vm5/skip-probe-17747/`, and the queue verdict in `material/skip-audit/queue-current.json`.
