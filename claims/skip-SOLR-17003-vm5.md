# Claim: skip-SOLR-17003 (closeout)

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE. Closeout of the audit verdict in `material/skip-audit/queue-current.json`; pinned head `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main).
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17003); overnight step 6a.
- Slice: the whole ticket.
- Capability tags used: review.
- Started: 2026-10-19T07:58:56Z (UTC).

Heartbeat: 2026-10-19T07:58:56Z

Result: 2026-10-19T07:59:27Z: keep-open. The failure is raised in Lucene's NumberRangePrefixTree.readCell, reached from Solr's unified highlighter with no field-type check (UnifiedSolrHighlighter.java:135-175; no DateRangeField case in the highlight package). Missing: a reproduction on main and the decision whether the guard belongs in Solr or Lucene.

DONE: 2026-10-19T07:59:27Z. Deliverables: `drafts/SOLR-17003/record.md`, `drafts/SOLR-17003/jira-comment.md` on draft branch `solr-17003-draft-open` (head `aa50491d480c`), and the queue verdict in `material/skip-audit/queue-current.json`.

Correction: : the draft head recorded above is not the branch tip. Same as SOLR-17086: current head of solr-17003-draft-open is 418f1909f780 (bulk record at be5edf5582e2 kept in history).
