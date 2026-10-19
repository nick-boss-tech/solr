# Claim: skip-SOLR-17003 (remediation, batch 01 item 16)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 16. The earlier vm5 closeout (`claims/skip-SOLR-17003-vm5.md`, DONE) predates this pass and is not reused.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17003); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T14:27:30Z (UTC).

Heartbeat: 2026-10-19T14:27:30Z

Result: 2026-10-19T14:29:10Z: keep-open, unchanged verdict. JIRA read 2026-10-19: Open, four comments. The failing call is in Lucene 10.4.0 (`NumberRangePrefixTree.readCell`); the Solr highlighter has no DateRangeField guard (`UnifiedSolrHighlighter.java:26-29`). Not reproduced; the missing pieces are the Lucene issue status, the owner decision on a Solr guard, and a highlighter run.

DONE: 2026-10-19T14:29:10Z. Deliverables: `drafts/SOLR-17003/record.md` and `drafts/SOLR-17003/jira-comment.md` on draft branch `solr-17003-draft-open` (head 91eda48361); queue verdict `keep-open` (unchanged); batch line in `reports/skip-verdict-remediation-01.md`.
