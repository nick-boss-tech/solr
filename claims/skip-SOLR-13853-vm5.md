# Claim: skip-SOLR-13853-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-19T21:35:00Z. Remediation redo of batch 05, item 16, under `assignments/pool-aaa-skip-verdict-remediation-05.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-05.md` (item 16, SOLR-13853).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T21:27:00Z (UTC).

Heartbeat: 2026-10-19T21:27:00Z

Append one heartbeat line at least once an hour while the claim is active, and push it.

Result: 2026-10-19T21:35:00Z: keep-open on solr-13853-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 80e9429db4). Jira read this pass: Open, no resolution, updated 2020-01-21. Code path read: SliceMutator.java:211-223 (stored shard state), :212 (ACTIVE clears parent markers), ReplicaMutator.java:613. Shard state is a stored field set by operations, not derived from replicas. Cluster run not done.

DONE: 2026-10-19T21:35:00Z. Deliverables: drafts/SOLR-13853/record.md and jira-comment.md on solr-13853-draft-open (80e9429db4); reports/skip-verdict-remediation-05.md line.
