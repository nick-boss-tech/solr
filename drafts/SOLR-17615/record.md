# Skip record: SOLR-17615

- Ticket: SOLR-17615, "facets exclusion does not exclude dense vector search prefilters". JIRA status Open, resolution none, created 2025-01-08, last updated 2025-01-09, checked 2026-10-19 UTC.
- Verdict: close-answered.
- Draft branch: solr-17615-draft-close.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

The reporter uses a `knn` query with `fq` filters that are added as implicit pre-filters, and wants the facet drill-down `ex=` exclusion to remove those filters from the pre-filter when a facet is computed. The reporter's own comment says the workaround is `excludeTags` on the `knn` parser, and asks for the default behavior to change. Comments: abenedetti asks whether `excludeTags` on the knn parser is the intended mechanism; hossman confirms that facet drill-downs are the use case `excludeTags` was added for and points to a test of multi-select facets; abenedetti asks the reporter what is missing in the documentation; the reporter replies that the workaround exists and the ask is about the default.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/solr-ref-guide/modules/query-guide/pages/dense-vector-search.adoc:387` and `:397`: `includeTags` and `excludeTags` are documented as options that select or exclude `fq` filters from implicit Pre-Filtering. Both must not be combined with `preFilter`.
- `dense-vector-search.adoc:814-817`: the guide lists this exact case as a reason to use `includeTags` or `excludeTags`: "when users 'drill down' on Facets, you want the `fq` parameters you add to be excluded from the Pre-Filtering so that the result set gets smaller; instead of just computing a new `topK` set."
- `solr/core/src/test/org/apache/solr/search/vector/KnnQParserTest.java:833`, `knnQueryWithFilterQuery_localParamIncludeExcludeTags`: tests `{!knn f=vector excludeTags=aa topK=6}` and the combined include and exclude forms, and at line 891 the query with `excludeTags=aa` is run against the expected result.
- `solr/core/src/test/org/apache/solr/search/QueryEqualityTest.java:1574`: `{!knn f=vector excludeTags=t2}` is covered in the equality test.

## History

- `b49f3d0769` (2024-06-21), SOLR-17335, is the latest commit that changed `excludeTags` in the core source, found by `git log -S` on `solr/core/src/java`. It is not the commit that introduced `excludeTags`; the introducing commit was not traced in this closeout. The support predates this ticket (2025-01-08); the ticket maintainers say it was added for this use case.
- No commit on the fork names SOLR-17615 (empty `git log --grep` at the pinned head).

## Reproduction attempts

Not attempted. The question is answered by the documented option and its tests, so a reproduction would test the documented workaround, which the existing tests already do. The ticket's request for a different default is not a reproduction question.

## Gap analysis

The reported exclusion does not happen because implicit pre-filtering adds all `fq` filters by default. That is the documented behavior, and the documented way to exclude tagged `fq` filters from pre-filtering is `excludeTags` on the `knn` parser. What is not decided by the code is whether the default should change so that facet exclusion also applies inside the `knn` pre-filter without any extra parameter. That is a product decision for the maintainers, and it is not part of this closeout.

## Verdict justification

close-answered: "A support question that the reference guide or docs answer. Link the page." Minimum evidence: the page and the sentence (`dense-vector-search.adoc:814-817`, and the option description at `:397`) and the tests that cover the option. The ticket asks two things: how to exclude facet filters from the `knn` pre-filter (answered), and whether the default should change (a product decision). This closeout answers the first and does not decide the second; the record and the comment say so.

## Limits

This record does not decide whether the default pre-filter behavior should change. It does not run `KnnQParserTest`. It does not check the reference guide on other release branches.
