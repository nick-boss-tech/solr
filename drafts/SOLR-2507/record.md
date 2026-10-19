# Skip record: SOLR-2507

- Ticket: SOLR-2507, Parsing issue with double quotes in WildCardQuery. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-2507-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C1, slice 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Parsing issue with double quotes in WildCardQuery.

## Code reading

unclear | The message "not allowed as first character" is absent from Solr source. SolrQueryParserBase.java:137 allows leading wildcards; QueryParser.jj:306-322 suggests the quoted-then-star query still fails. Missing: the actual ParseException text, which needs a parser run.

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
