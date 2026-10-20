# Claim: skip-SOLR-12487-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:12:11Z. Remediation redo of batch 08, item 13, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 13, SOLR-12487).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:10:44Z (UTC).

Heartbeat: 2026-10-20T06:10:44Z

Result: 2026-10-20T06:12:11Z: keep-open on solr-12487-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head ead1daf142, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, 2 comments; full description read from the Jira REST record (root cause: StackOverflowError in JavaBinCodec.readMap during tlog replay). Code read: UpdateLog.java:1604-1607, 1914-1919, 2347-2353 (catch ClassCastException and Exception only); TransactionLog.java:578, 756, 933 (codec.readVal); CoreContainer.java:1752-1763. No StackOverflow handling under solr/core or solrj at the pin. A corrupt-tlog reproduction not run.

DONE: 2026-10-20T06:12:11Z. Deliverables: drafts/SOLR-12487/record.md and jira-comment.md on solr-12487-draft-open (ead1daf142); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
