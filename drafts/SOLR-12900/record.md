# Skip record: SOLR-12900

- Ticket: SOLR-12900, NPE in OneComparatorFieldValueHitQueue.lessThan while indexing.
- Verdict: keep-open.
- Draft branch: solr-12900-draft-open (verdict unchanged; this record replaces the earlier record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 23 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-10-23, updated 2019-06-08, no fix versions, no links, no comments. The report: an occasional `NullPointerException` in `OneComparatorFieldValueHitQueue.lessThan` during a search that finishes the Solr process. The logged request is a deep-paging sort: `q=*:*&sort=ID asc&start=2300000&rows=100000&facet.limit=10000&facet.sort=index`, with 4,598,058 hits. The stack frame in the ticket text is Lucene's, and the description says the NPE happens "occasionally".

## Code reading

Read at the pin:

- The comparator `OneComparatorFieldValueHitQueue` and `FieldValueHitQueue` are Lucene classes; they are not in the Solr tree (the comparator is built by `TopFieldCollectorManager`, `SolrIndexSearcher.java:1822-1824`).
- `solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:1827` (`getDocListNC`): the sort path creates a top-field collector sized `len`; the length is `cmd.getSupersetMaxDoc()`, which is `start + rows` for the request. A request with `start=2300000` therefore asks the Lucene queue for about 2.4 million entries.
- No null guard on the sort field value appears in `getDocListNC`; a document with no `ID` value would give a null sort value in Lucene's comparator. The ticket does not say whether the `ID` field can be missing.

## History

No commit at the pin names SOLR-12900 (the evidence tool lists none). The sort path's history was not traced in this pass.

## Reproduction attempts

Not run. This pass ran no builds or tests. The missing piece is a run: a collection where some documents lack the sort field, a request sorted on that field with a large `start`, and the stack trace, to show whether the NPE is the missing-value case.

## Gap analysis

- The NPE is in Lucene's comparator, outside this tree, so the defect location cannot be named by Solr-side reading alone.
- The deep-paging size is a Solr-side factor (the queue is sized from `start + rows`), but the ticket does not show the NPE depends on it.
- The ticket gives no Lucene version and no failing document shape, so a run needs the field data the reporter had.

## Verdict justification

keep-open: the NPE is in Lucene's comparator and the Solr-side path that sizes the queue is present; the missing-value case is a hypothesis; the reporter's data and a run are missing. close-fixed, close-obsolete, and close-cannot-reproduce do not fit without a run.

Missing for this verdict: a run with a sort field that has missing values and a large `start` at the pin, with the stack trace, and the reporter's Lucene version or data shape.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
