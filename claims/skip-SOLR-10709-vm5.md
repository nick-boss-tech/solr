# Claim: skip-SOLR-10709-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T08:47:40Z. Remediation redo of batch 10, item 25, under `assignments/pool-aaa-skip-verdict-remediation-10.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-10.md` (item 25, SOLR-10709).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T08:46:39Z (UTC).

Heartbeat: 2026-10-20T08:46:39Z

Result: 2026-10-20T08:47:40Z: close-fixed on solr-10709-draft-close at 498a09aea97aac86c2cc7ac114113b946d249942 (head 24774069cf; unchanged verdict; earlier record replaced, now checks the parser route). Jira read this pass: Open, no resolution, 0 comments. Fix c64a35adbc (SOLR-17631, Lucene upgrade) ancestor of the pin (checked). Route: SolrQueryParserBase.java:1278-1285 (foo:* to getExistenceQuery); FieldType.java:1062-1078 (FieldExistsQuery for docValues fields); FieldType.java:1170-1180 (DocValuesRewriteMethod remains for other wildcard forms). Query not run; linkage by code path.

DONE: 2026-10-20T08:47:40Z. Deliverables: drafts/SOLR-10709/record.md and jira-comment.md on solr-10709-draft-close (24774069cf); queue-current set; reports/skip-verdict-remediation-10.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
