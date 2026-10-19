# Claim: skip-SOLR-17133 (remediation, batch 01 item 23)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: DONE. Closeout redo under `assignments/pool-aaa-skip-verdict-remediation-01.md`, item 23.
- Job or assignment: `material/skip-audit/queue-current.json` (record SOLR-17133); `assignments/pool-aaa-skip-verdict-remediation-01.md`.
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-19T15:01:30Z (UTC).

Heartbeat: 2026-10-19T15:01:30Z

Result: 2026-10-19T15:03:08Z: keep-open, unchanged verdict. JIRA read 2026-10-19: Open, empty description, no comments. PRS is chosen at random per test class unless `@NoPrs` (`SolrCloudTestCase.java:147-155`); `CloudAuthStreamTest` has no `@NoPrs`. The earlier passing run did not record its PRS setting. Not reproduced with PRS forced on; the missing piece is forced-PRS runs at several seeds.

DONE: 2026-10-19T15:03:08Z. Deliverables: `drafts/SOLR-17133/record.md` and `drafts/SOLR-17133/jira-comment.md` on draft branch `solr-17133-draft-open` (head b1ac4f0e0a); queue verdict `keep-open` (unchanged); batch line in `reports/skip-verdict-remediation-01.md`.
