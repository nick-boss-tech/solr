# SOLR-12044 - hypothetical reproduction (nothing was compiled or run)

JIRA (Smiley): optimize `getDocSet` paths for `MatchAllDocsQuery`; reuse live docs, but be clear about whether a cached or shared
result may be returned. Earlier audit note: "optimization, not a bug". On `upstream/main` most paths already shortcut
(`getAndCacheDocSet`, `DocSetUtil.createDocSet`, `getDocSet(List)`), but `getDocSet(Query, DocSet)` with a MatchAll query and a
non-null filter still intersected `liveDocs` with the filter (cached path) or built `combineQueryAndFilter(...)` and ran a
generic collector (no-cache path).

## Change
`SolrIndexSearcher.getDocSet(Query, DocSet)`: after unwrapping `WrappedQuery`, a `MatchAllDocsQuery` returns `filter` when given
or `getLiveDocSet()` otherwise, before the cache/no-cache split. The method already documents that callers must not modify the
returned set, so handing back the caller's own filter is within that contract.

## Test
`TestIndexSearcher.testMatchAllDocsWithFilterReturnsFilter`: 3 docs, one deleted; `assertSame(filter, getDocSet(matchAll, filter))`
for a plain and a `cache=false` wrapped query; live-docs size 2 for the null-filter forms.

## Guesses to verify first
- A filter DocSet never contains deleted docs. DocSets from `getDocSet(Query)` are built from searcher collection (live only);
  a filter built from some raw-reader path or a stale searcher would now leak deleted ids where the intersection used to drop them.
  Check callers that pass a `filter` (grep `getDocSet(` with two args: `QueryComponent`, grouping, `ExpandComponent`, facets).
- `WrappedQuery.setCache(false)` plus `MatchAllDocsQuery` reaches this method unwrapped (the ExtendedQuery branch above it).
- The existing `getAndCacheDocSet` MatchAll bypass stays (still used by `getPositiveDocSet`).

## Fail-before
Expected: `assertSame` fails on main (the intersection returns a new DocSet), unless `BitDocSet.intersection` already returns the
argument when the receiver is all live docs (not read).
