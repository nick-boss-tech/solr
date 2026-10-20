# Claim: skip-SOLR-12263-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:56:43Z. Remediation redo of batch 08, item 23, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 23, SOLR-12263).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:55:38Z (UTC).

Heartbeat: 2026-10-20T06:55:38Z

Result: 2026-10-20T06:56:43Z: keep-open on solr-12263-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 537c52cc14, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, 0 comments, updated 2022-03-24; full description (1489 characters) read through the Jira REST record. Code read: JavaBinCodec.java:767-805 (readSolrInputDocument, field name cast to String still present); UpdateLog.java:2171 (doReplay), :2346-2349 (ClassCastException caught, REPLAY_ERR logged, replay continues). A replay of a Long-field-name entry not run.

DONE: 2026-10-20T06:56:43Z. Deliverables: drafts/SOLR-12263/record.md and jira-comment.md on solr-12263-draft-open (537c52cc14); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
