# Claim: skip-SOLR-12340-vm5

- Claimant host: vm5, matching a file in `hosts/`.
- Status: DONE 2026-10-20T06:35:55Z. Remediation redo of batch 08, item 18, under `assignments/pool-aaa-skip-verdict-remediation-08.md`.
- Job or assignment: `assignments/pool-aaa-skip-verdict-remediation-08.md` (item 18, SOLR-12340).
- Slice: the whole ticket.
- Capability tags used: review, draft.
- Started: 2026-10-20T06:34:36Z (UTC).

Heartbeat: 2026-10-20T06:34:36Z

Result: 2026-10-20T06:35:55Z: keep-open on solr-12340-draft-open at 498a09aea97aac86c2cc7ac114113b946d249942 (head c260f07270, unchanged verdict; audit-derived record replaced; close-answered considered and not used, since the committer's comment calls the change unfortunate). Jira read this pass: Open, no resolution, 1 comment; description read in full via the Jira REST record. Code read: QueryParser.java:29, :51 (DEFAULT_SPLIT_ON_WHITESPACE false); SolrQueryParserBase.java:144, :534 (autoGeneratePhraseQueries false, phrase only when quoted or enabled); QueryParsing.java:51. Ref guide standard-query-parser.adoc:39-44 documents sow and its default. Introduced by d1b2fb33ef (SOLR-9185). Query run with debugQuery not done.

DONE: 2026-10-20T06:35:55Z. Deliverables: drafts/SOLR-12340/record.md and jira-comment.md on solr-12340-draft-open (c260f07270); queue-current set; reports/skip-verdict-remediation-08.md line.

Append one heartbeat line at least once an hour while the claim is active, and push it.
