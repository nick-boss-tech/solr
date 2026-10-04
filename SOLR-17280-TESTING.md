# SOLR-17280 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-17280 - "SolrRangeQuery can trigger IllegalStateException: Recursive update in CaffeineCache / ConcurrentHashMap" (Chris Hostetter, 2024). Related: SOLR-16707. Reopened from a skip by the skip audit (audit-1).
- Branch: `solr-17280-submit` off `apache/solr` main `e2cdb2d7e8ae`

## The bug, as understood
`SolrRangeQuery.ConstWeight.getSegState` builds a DocSet for ranges with more than 16 terms and does a "naked" `filterCache.put(rangeQuery, docSet)`. When this runs inside `filterCache.computeIfAbsent(...)` for an enclosing query on the same async Caffeine cache (the mapping function evaluates the query), the `put` re-enters the underlying ConcurrentHashMap and, if the keys land in the same bin, throws `IllegalStateException: Recursive update`. This is the sporadic `TestFiltering.testRandomFiltering` failure.

## What the branch changes (deliberately the smallest fix)
- Remove the naked `put`. A range query used directly as a filter is still cached by `SolrIndexSearcher` itself. The cost: a many-term range nested inside a bigger filter is no longer cached on its own (a possible perf regression for that shape).
- Test: `TestFiltering.testNestedRangeQueryNotPutInFilterCache` (40 docs, `val_s:[v00 TO v99]` nested in `... OR id:nomatch`) asserts the bare range query is not in the filterCache afterwards. It is a deterministic stand-in for the bin-collision exception, which can't be forced.

## Guesses to verify first
1. That the nested filter actually reaches `getSegState` with `doCheck` true (ord == 0 and a SolrIndexSearcher with a filterCache) and the range exceeds the 16-term threshold; `val_s` must be a string field in `schema_latest.xml` that uses SolrRangeQuery.
2. That `QParser.getParser(rangeStr, null, req).getQuery()` yields a `SolrRangeQuery` equal to the one the filter built (no caching of the rewritten form).
3. Whether keeping the cache insertion but making it safe (e.g. skip when `computeIfAbsent` is in progress, or non-async put) is preferable to dropping it. Michael Gibney's PR 1481 on SOLR-16707 took a different direction and was not merged; check that discussion before submitting.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.TestFiltering"
```
Fail-before: revert `SolrRangeQuery.java` only; the new test should fail on the `assertNull`.

## Not done
No JIRA comment, no PR.
