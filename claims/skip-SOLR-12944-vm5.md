# Claim: skip-SOLR-12944-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T04:48:11Z. Remediation redo of batch 07, item 21, under `assignments/pool-aaa-skip-verdict-remediation-07.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-07.md` (item 21, SOLR-12944).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T04:46:52Z (UTC).

Heartbeat: 2026-10-20T04:46:52Z

Result: 2026-10-20T04:48:11Z: keep-open on solr-12944-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head a79356c463, unchanged verdict). Jira read this pass: Open, no resolution, updated 2019-11-21. Code read: CreateCollection.java:159-170, :234-238 (EMPTY maps to no replicas; the replication factor is stored independently). API half appears fixed; the stored non-zero count with EMPTY is open. The run and the maintainer decision not done.

DONE: 2026-10-20T04:48:11Z. Deliverables: drafts/SOLR-12944/record.md and jira-comment.md on solr-12944-draft-open (a79356c463); queue-current set; reports/skip-verdict-remediation-07.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
