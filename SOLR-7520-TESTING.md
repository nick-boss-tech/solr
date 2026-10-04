# SOLR-7520 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-7520 - "Post filter DelegatingCollector.finish not called for multi-shard queries specifying grouping" (Eric Wheeler). The reporter suggested calling the post filter's finish at the end of `CommandHandler.searchWithTimeLimiter`; the method was later renamed `complete()`.
- Branch: `solr-7520-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`SolrIndexSearcher` (non grouped) and `Grouping` (non distributed grouping) call `DelegatingCollector.complete()` after the search. `grouping.CommandHandler.searchWithTimeLimiter` (used for the shard side of distributed grouping) wraps the collector with `filter.postFilter` but never completes it, so post filters that emit results in `complete()` produce nothing. Still true on main.

## What the branch changes
- `CommandHandler.searchWithTimeLimiter`: after `searcher.search(...)`, call `filter.postFilter.complete()` (in a `finally`, inside the existing `ExitingReaderException` catch, mirroring `SolrIndexSearcher`).
- `AnalyticsMergeStrategyTest`: extra grouped query (`group=true&group.field=sort_i`) with `fq={!count}`; asserts the `analytics` section exists in the response.

## What was guessed (verify these first)
1. Grouping runs the first-phase search and the second-phase search separately, so `complete()` can run more than once per shard and `TestAnalyticsCollector.complete()` would add the `analytics` entry twice on the shard response. The test only checks presence; exact `mycount` is not asserted because of this.
2. The coordinator may not run the merge strategy for grouped responses, in which case `analytics` might still be missing at the coordinator even with the fix. If so the test needs a different assertion (e.g. a shard-side check with `distrib=false`).
3. `sort_i` is a valid single-valued group field in `schema15.xml` (used for sorting in this test already).
4. `DelegatingCollector.complete()` is public and throws `IOException` (as used in `SolrIndexSearcher`).

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.AnalyticsMergeStrategyTest"
```
Fail-before: revert only `CommandHandler.java`.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
