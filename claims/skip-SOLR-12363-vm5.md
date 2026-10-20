# Claim: skip-SOLR-12363-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:31:21Z. Remediation redo of batch 08, item 17, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 17, SOLR-12363).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:30:35Z (UTC).

Heartbeat: 2026-10-20T06:30:35Z

Result: 2026-10-20T06:31:21Z: keep-open on solr-12363-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head 6d4022158b, unchanged verdict; audit-derived record replaced). Jira read this pass: Open, no resolution, 0 comments; full description read from the Jira REST record. Code read: RandomSortField.java:84-91 (getSeed includes top.getVersion() and docBase), :140-146, :160-163 (compare on hash(doc + seed)), :187-189. No fix commit at the pin. A cursor walk on a static index and across a commit not run.

DONE: 2026-10-20T06:31:21Z. Deliverables: drafts/SOLR-12363/record.md and jira-comment.md on solr-12363-draft-open (6d4022158b); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
