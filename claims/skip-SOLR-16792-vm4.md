# Claim: skip-SOLR-16792 (remediation, batch 01 item 22)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 22. The earlier vm5 closeout (`claims/skip-SOLR-16792-vm5.md`, DONE) predates this pass and is not reused.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-16792); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T14:56:30Z (UTC).

Heartbeat: 2026-10-19T14:56:30Z

Result: 2026-10-19T14:58:05Z: keep-open, unchanged verdict. JIRA read 2026-10-19: Open, one comment asking for reproduction steps. The only dereference on the loaded-core branch that a null could reach is `CoreStatus.java:146-150`; whether `ZkController.java:1917` can set a null publish state is not established. Not reproduced; the missing piece is the reproduction or a current stack trace.

DONE: 2026-10-19T14:58:05Z. Deliverables: `drafts/SOLR-16792/record.md` and `drafts/SOLR-16792/jira-comment.md` on draft branch `solr-16792-draft-open` (head 94e304ab68); queue verdict `keep-open` (unchanged); batch line in `reports/skip-verdict-remediation-01.md`.
