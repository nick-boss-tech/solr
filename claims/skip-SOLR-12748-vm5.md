# Claim: skip-SOLR-12748-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T05:34:08Z. Remediation redo of batch 08, item 5, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 5, SOLR-12748).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T05:32:48Z (UTC).

Heartbeat: 2026-10-20T05:32:48Z

Result: 2026-10-20T05:34:08Z: keep-open on solr-12748-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head bd38d3a3c2, unchanged verdict; earlier record replaced). Jira read this pass: Open, no resolution, updated 2022-03-24. Code read: solr/server/etc/jetty.xml:33-36 (maxThreads 10000; the reporter's 5,568 threads under the cap). The blocking socket reads and the ZooKeeper switch run not done.

DONE: 2026-10-20T05:34:08Z. Deliverables: drafts/SOLR-12748/record.md and jira-comment.md on solr-12748-draft-open (bd38d3a3c2); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
