# Claim: skip-SOLR-10960-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T07:46:25Z. Remediation redo of batch 10, item 10, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 10, SOLR-10960).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T07:45:39Z (UTC).

Heartbeat: 2026-10-20T07:45:39Z

Result: 2026-10-20T07:46:25Z: keep-open on solr-10960-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head b8bca3093a, unchanged verdict; earlier record replaced). Jira read this pass: Open, no resolution, 2 comments, updated 2021-10-28. Code read: solr/solrj-streaming/src/test/org/apache/solr/client/solrj/io/stream/JDBCStreamTest.java:51 (@SuppressPointFields bugUrl SOLR-10960: the bug is still open and avoided at the pin); :377-382; 14a0b4fa53 (SOLR-10946) and 8d7e3cc929 (SOLR-11048) ancestors, neither fixes the innerJoin failure. A run with point fields enabled not done.

DONE: 2026-10-20T07:46:25Z. Deliverables: drafts/SOLR-10960/record.md and jira-comment.md on solr-10960-draft-open (b8bca3093a); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
