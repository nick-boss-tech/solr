# Skip record: SOLR-13094

- Ticket: SOLR-13094, NPE while doing regular Facet.
- Verdict: keep-open.
- Draft branch: solr-13094-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 14 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-12-24, updated 2019-11-25, no fix versions, no links. The report: a regular facet request (`facet.field=description`, `facet.limit=200`) fails with an NPE on a SolrCloud collection, wrapped as a server error. Three comments (2019-09-12 to 2019-09-13): jsp08 sees the same with group faceting on a long field and says it blocks an upgrade; magibney notes there are fundamental problems with distributed faceting over `SortableTextField` and links an issue about it (the linked number is not in the ticket text).

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/request/SimpleFacets.java:918-924`: a non-SolrException failure in a field facet is wrapped as `"Exception during facet.field: " + facetValue`.
- `SimpleFacets.java` `numDocs` (about line 990): the term query is `ft.getFieldTermQuery(null, sf, term)` passed to `searcher.numDocs`.
- `solr/core/src/java/org/apache/solr/schema/TextField.java:162-166` (`getFieldTermQuery`): returns `new TermQuery(...)`, not null; `analyzeMultiTerm` returns null for an empty analysis (line 203-212), but that is the multi-term path, not `getFieldTermQuery`.
- `solr/core/src/java/org/apache/solr/schema/FieldType.java:1123-1137` (`getFieldQuery`): returns a range query or a `TermQuery`, never null.
- `solr/core/src/java/org/apache/solr/schema/SortableTextField.java:222-231`: the only `return null` is `getUninversionType`, which is not on the term query path.

So the null term query the record's trace assumed is not produced by the term query methods at the pin. The NPE's null key is therefore not identified by reading; the commenter's distributed-faceting issue over `SortableTextField` is the likelier area and was not traced.

## History

No commit at the pin names SOLR-13094 (the evidence tool lists none). The linked SortableTextField issue is not identified in the ticket text.

## Reproduction attempts

Not run. This pass ran no builds or tests. The missing piece is a run: a distributed collection with a `SortableTextField` (or the reporter's field type, which the ticket does not state) and a `facet.field` request, with the stack trace from the failing request.

## Gap analysis

- The code path the record named (a null term query) does not occur through the term query methods at the pin, so the earlier trace does not stand as written.
- The ticket does not state the reporter's field type, so the affected type cannot be confirmed from the ticket.
- The distributed-faceting question over `SortableTextField` is the likelier cause, and it has not been traced.

## Verdict justification

keep-open: the NPE is not reproduced, the reporter's field type is not in the ticket, and the cause is not identified by reading (the earlier trace is not confirmed at the pin). close-fixed, close-obsolete, and close-answered do not fit.

Missing for this verdict: the reporter's field type and stack trace, and a run of a facet request on a distributed collection with that type at the pin.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
