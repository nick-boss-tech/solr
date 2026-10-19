# Claim: skip-SOLR-13591-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-19T23:00:00Z. Remediation redo of batch 06, item 8, under `assignments/pool-aaa-skip-verdict-remediation-06.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-06.md` (item 8, SOLR-13591).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T22:50:00Z (UTC).

Heartbeat: 2026-10-19T22:50:00Z

Result: 2026-10-19T23:00:00Z: verdict changed from close-obsolete to keep-open on the new branch solr-13591-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 42de24be89). The earlier close-obsolete draft (solr-13591-draft-close, ba9911c2b4) is not supported: ZkClientClusterStateProvider still exists and getZkStateReader (:150-157) still throws AlreadyClosedException after close(); connect() was removed by 0c77e420e8 (SOLR-18380, ancestor). Jira read this pass: Open, no resolution, updated 2021-09-08. No docs page answers the closed-client question (close-answered not available). Reproduction not run.

DONE: 2026-10-19T23:00:00Z. Deliverables: drafts/SOLR-13591/record.md and jira-comment.md on solr-13591-draft-open (42de24be89); queue-current set; reports/skip-verdict-remediation-06.md line.
