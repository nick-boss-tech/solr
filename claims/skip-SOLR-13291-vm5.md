# Claim: skip-SOLR-13291-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T00:20:00Z. Remediation redo of batch 06, item 24, under `assignments/pool-aaa-skip-verdict-remediation-06.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-06.md` (item 24, SOLR-13291).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T00:10:00Z (UTC).

Heartbeat: 2026-10-20T00:10:00Z

Result: 2026-10-20T00:20:00Z: keep-open on solr-13291-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 431d36db6d, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, updated 2019-06-08. Code read: DeleteCollectionCmd.java:115-116 (UNLOAD with DELETE_INSTANCE_DIR), DeleteReplicaCmd.java:301, :325 (ZK corenode check); no lock-release wait seen in the path read. "lock held by this virtual machine" is the JDK message, not Solr code. CollectionsAPISolrJTest.java:311-312 @AwaitsFix SOLR-13021 is a different shard case. Race not reproduced; run not done.

DONE: 2026-10-20T00:20:00Z. Deliverables: drafts/SOLR-13291/record.md and jira-comment.md on solr-13291-draft-open (431d36db6d); queue-current set; reports/skip-verdict-remediation-06.md line.
