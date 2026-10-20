# Claim: skip-SOLR-12446-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:22:23Z. Remediation redo of batch 08, item 15, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 15, SOLR-12446).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:20:49Z (UTC).

Heartbeat: 2026-10-20T06:20:49Z

Result: 2026-10-20T06:22:23Z: keep-open on solr-12446-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 530bd80816, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, 0 comments; the response example read from the Jira REST record. Code read: SolrIndexSearcher.java:280 (buildAndRunCollectorChain exists); HttpSolrClient.java:256-258 (non-v2 500 with error body built with skipRetry false); RemoteSolrException.java:63-65, 143-144; LBSolrClient.java:133 (RETRY_CODES incl. 500), 644-652 (retry and zombie), 931-937 ("No live SolrServers" message). A two-node reproduction not run.

DONE: 2026-10-20T06:22:23Z. Deliverables: drafts/SOLR-12446/record.md and jira-comment.md on solr-12446-draft-open (530bd80816); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
