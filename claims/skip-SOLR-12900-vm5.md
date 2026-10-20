# Claim: skip-SOLR-12900-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T04:58:09Z. Remediation redo of batch 07, item 23, under `assignments/pool-aaa-skip-verdict-remediation-07.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-07.md` (item 23, SOLR-12900).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T04:56:52Z (UTC).

Heartbeat: 2026-10-20T04:56:52Z

Result: 2026-10-20T04:58:09Z: keep-open on solr-12900-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head b48705a4c6, unchanged verdict; earlier record replaced). Jira read this pass: Open, no resolution, no comments. Code read: SolrIndexSearcher.java:1822-1827 (sort collector sized from start+rows, no null guard). The NPE is in Lucene's comparator, outside the tree; a missing-value run not done.

DONE: 2026-10-20T04:58:09Z. Deliverables: drafts/SOLR-12900/record.md and jira-comment.md on solr-12900-draft-open (b48705a4c6); queue-current set; reports/skip-verdict-remediation-07.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
