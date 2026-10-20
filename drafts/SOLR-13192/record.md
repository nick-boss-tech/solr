# Skip record: SOLR-13192

- Ticket: SOLR-13192, Streaming expression - when query consist from only "NOT"("-") condition, Solr returns no results.
- Verdict: keep-open.
- Draft branch: solr-13192-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 10 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-01-30, updated 2019-06-08, no fix versions, no links. The report: a streaming expression whose `q` is only a negative clause (`-field:x` or `NOT field:x`) returns no results, while adding a positive clause works. One comment (2019-02-02, erickerickson): Solr does not implement pure boolean logic; `NOT` removes items from the results that the rest of the query matches, so the expectation should be checked.

## Code reading

Read at the pin:

- `solr/solr-ref-guide/modules/query-guide/pages/standard-query-parser.adoc:413-415`: "Pure negative queries (all clauses prohibited) are allowed (only as a top-level clause)", with the example `-inStock:false` finds all field values where inStock is not false.
- `solr/solr-ref-guide/modules/query-guide/pages/edismax-query-parser.adoc:32`: pure negative nested queries such as `+foo (-foo)` match all documents.
- The guide therefore says a top-level pure negative query is supported and matches the complement. The reporter's "no results" does not match that statement, so the docs do not answer the ticket as a usage question; the question is whether the streaming request is a top-level query.
- The streaming `search()` expression sends `q` to the search handler; this pass did not trace whether any wrapping makes the clause nested (for example a filter or join around it).

## History

No commit at the pin names SOLR-13192 (the evidence tool lists none). The history of the pure-negative handling was not traced.

## Reproduction attempts

Not run. This pass ran no builds or tests. The missing piece is a run at the pin: a streaming `search(...)` with `q="-field:value"` and with `q="NOT field:value"`, and the same clause as a plain select request, comparing the results and noting whether the streaming request wraps `q`.

## Gap analysis

- The guide says the query should work at the top level, which contradicts the report, so either the streaming request nests the clause or the behavior is a defect.
- The comment's answer (Solr does not do pure boolean logic) is a general statement; it does not match the guide's pure-negative passage, so it does not close the ticket.
- No fix commit exists, and the feature is present, so close-fixed and close-obsolete do not fit.

## Verdict justification

keep-open: the guide and the report disagree, the streaming request's handling of `q` was not traced, and the behavior has not been run. close-answered would need the docs to answer the reporter's case, and they describe the opposite result.

Missing for this verdict: a run of the streaming request and the plain select request with a pure negative clause at the pin, and the trace of how the streaming request passes `q`.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
