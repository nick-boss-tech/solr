# Claim: skip-SOLR-17224 (remediation, batch 01 item 8)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 8.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17224); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T13:48:09Z (UTC).

Heartbeat: 2026-10-19T13:48:09Z

Result: 2026-10-19T13:49:37Z: close-fixed, unchanged verdict. JIRA read 2026-10-19: Open, two comments. Fixing commit `a1ebd0433c` (SOLR-17721) is an ancestor of the pin 498a09aea9; the null check it adds is at `AtomicUpdateDocumentMerger.java:549-553`. Not reproduced; the record says so.

DONE: 2026-10-19T13:49:37Z. Deliverables: `drafts/SOLR-17224/record.md` and `drafts/SOLR-17224/jira-comment.md` on draft branch `solr-17224-draft-close` (head a5332eae15); queue verdict `close-fixed` (unchanged); batch line in `reports/skip-verdict-remediation-01.md`.
