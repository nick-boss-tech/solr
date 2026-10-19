# Skip record: SOLR-4533

- Ticket: SOLR-4533, Synonyms, created in custom filters are ignored after tokenizers.. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-4533-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C1, slice 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Synonyms, created in custom filters are ignored after tokenizers..

## Code reading

unclear | solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java:525 maps every AND term to MUST. Same-position synonym Occur handling is in external Lucene QueryBuilder, not in the tree. Missing: Lucene QueryBuilder synonym Occur handling at the dependency version, and a runtime reproduction with the custom filter.

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
