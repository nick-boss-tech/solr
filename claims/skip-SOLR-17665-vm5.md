# Claim: skip-SOLR-17665 (closeout)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Closeout of the audit verdict in `material/skip-audit/queue-current.json`; pinned head `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main).
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17665); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T07:21:57Z (UTC).

Heartbeat: 2026-10-19T07:21:57Z

Result: 2026-10-19T07:23:58Z: keep-open. The non-threaded Collector search path does not use the node executor for slicing at the pinned head (Lucene 10.4.0 bytecode checked with javap). The reported regression is not reproduced; the missing piece is a paired benchmark at indexSearcherExecutorThreads 0 and -1 with multiThreaded=false.

DONE: 2026-10-19T07:23:58Z. Deliverables: `drafts/SOLR-17665/record.md`, `drafts/SOLR-17665/jira-comment.md` on draft branch `solr-17665-draft-open` (head `057ed012b8cc`), and the queue verdict in `material/skip-audit/queue-current.json`.
