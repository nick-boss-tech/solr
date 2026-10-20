# Claim: skip-SOLR-12300-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:47:25Z. Remediation redo of batch 08, item 21, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 21, SOLR-12300).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:46:37Z (UTC).

Heartbeat: 2026-10-20T06:46:37Z

Result: 2026-10-20T06:47:25Z: keep-open on solr-12300-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 4274afbefa, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, 2 comments. Code read: SolrReturnFields.java:145-150, :187-193, :377-383 (transformer extra fields added to fetch set only when not wildcard); SolrDocumentFetcher.java:339-341, :778-800 (RetrieveFieldsOptimizer). The count-dependent (2 vs 3 fields) behavior not explained by reading. Blank-transformer run at the pin not done.

DONE: 2026-10-20T06:47:25Z. Deliverables: drafts/SOLR-12300/record.md and jira-comment.md on solr-12300-draft-open (4274afbefa); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
