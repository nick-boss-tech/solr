# Claim: skip-SOLR-12850-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T05:08:04Z. Remediation redo of batch 07, item 25, under `assignments/pool-aaa-skip-verdict-remediation-07.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-07.md` (item 25, SOLR-12850).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T05:06:50Z (UTC).

Heartbeat: 2026-10-20T05:06:50Z

Result: 2026-10-20T05:08:04Z: keep-open on solr-12850-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 984baa59ed, unchanged verdict). Jira read this pass: Open, no resolution, no comments. Code read: ZkController.java:256 and :1989 (replicasMetTragicEvent never populated), ZkController.java:904-942 (leader give-up only), CoreContainer.java:1810-1821 (corrupt-index load path). No DOWN publication at the pin; a corruption run not done.

DONE: 2026-10-20T05:08:04Z. Deliverables: drafts/SOLR-12850/record.md and jira-comment.md on solr-12850-draft-open (984baa59ed); queue-current set; reports/skip-verdict-remediation-07.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
