# Claim: skip-SOLR-16930 (remediation, batch 01 item 17)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 17. The earlier vm5 closeout (`claims/skip-SOLR-16930-vm5.md`, DONE) predates this pass and is not reused.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-16930); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T14:32:30Z (UTC).

Heartbeat: 2026-10-19T14:32:30Z

Result: 2026-10-19T14:33:22Z: keep-open, unchanged verdict. JIRA read 2026-10-19: Open, one comment. The short-name lookup is delegated to Lucene (`FieldTypePluginLoader.java:255, 319`); the design question has no defined correct behavior yet. Not reproduced; the missing piece is the maintainer decision on cross-kind names.

DONE: 2026-10-19T14:33:22Z. Deliverables: `drafts/SOLR-16930/record.md` and `drafts/SOLR-16930/jira-comment.md` on draft branch `solr-16930-draft-open` (head acc0c64af1); queue verdict `keep-open` (unchanged); batch line in `reports/skip-verdict-remediation-01.md`.
