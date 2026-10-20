# Skip record: SOLR-9352

- Ticket: SOLR-9352, "matchAllDocsQuery instead of WildCardQuery from lucene qp with df and *". JIRA status read 2026-10-20T15:03:41Z (UTC): Open, resolution None, updated 2019-06-08. Linked: SOLR-2996 (Closed).
- Verdict: keep-open (kept; re-read at the pinned head). The behavior is deliberate per the linked issue, but the reference guide does not state it, so the close-answered route is not yet available.
- Draft branch: solr-9352-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T15:03:41Z (UTC).

## Ticket reading

With `df=id`, the query `q=*` is parsed as MatchAllDocsQuery, not as `id:*`. The reporter expected a wildcard on the default field. Hoss's 2016 comment says the behavior was added on purpose because most users kept getting unexpected results when a bare star matched only the default field.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java:1279-1281`: a wildcard term `*` with no explicit field (`getExplicitField() == null`) or with field `*` returns `newMatchAllDocsQuery()`. So the default field is ignored for a bare star, as the ticket describes.
- `solr/solr-ref-guide/modules/query-guide/pages/other-parsers.adoc:1620`: the reference guide lists MatchAllDocsQuery as a builder only, and does not state the bare-star rule with `df`.

## History

- evidence.py at the pinned head: no commits naming SOLR-9352. The linked SOLR-2996 is closed; its commit was not located here.

## Reproduction attempts

Not run. The check is one query: `df=id&q=*` with the parsed query in debug output. The result is already visible in the code path above.

## Gap analysis

The behavior is intended, but the documentation does not say so. A docs sentence stating the rule would allow close-answered; without one, the verdict stays open.

## Verdict justification

keep-open meets the rubric: attempts made are the parser reading and the docs check; the missing piece is a docs statement or the owner's view that the rule should stay undocumented. close-answered would need a docs page that answers the question, and none states the rule here.

## Limits

This record does not change the behavior. It does not read SOLR-2996.
