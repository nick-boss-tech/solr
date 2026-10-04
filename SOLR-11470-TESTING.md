# SOLR-11470 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-11470 - "Negative queries always return 'No Results' with rq parameter" (Yuki Yano). No comments; the description diagnoses `QueryUtils#makeQueryable` not seeing through `RankQuery`.
- Branch: `solr-11470-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`ResponseBuilder.getQueryCommand` sets the query to `wrap(getQuery())`, and `AbstractReRankQuery.wrap` stores the raw main query. `SolrIndexSearcher` applies `QueryUtils.makeQueryable` only to the query it is given,
which is now the `RankQuery`, not a `BooleanQuery`, so the pure-negative main query is never given its implicit `*:*`. Lucene rewrites a pure-negative `BooleanQuery` to `MatchNoDocsQuery` (inside `AbstractReRankQuery.rewrite`),
hence `numFound=0`. The code path is still the same on main (`ResponseBuilder.wrap`, `AbstractReRankQuery.wrap`).

## What the branch changes
- `ResponseBuilder.wrap`: passes `QueryUtils.makeQueryable(q)` to `rankQuery.wrap`. Doing it here fixes every `RankQuery` implementation (rerank, LTR), not only `AbstractReRankQuery`. `makeQueryable` returns non-negative queries unchanged.
- `TestReRankQParserPlugin.testReRankWithPureNegativeMainQuery`: three docs, `q=-term_s:ZZZZ` returns ids 1 and 2 with and without `rq={!rerank ...}`.

## What was guessed (verify these first)
1. **Compile**: `QueryUtils` import added in sorted position between `QueryResult` and `RankQuery`; the test uses `ReRankQParserPlugin.NAME` and `delQ`/`adoc`/`commit` already imported/inherited in that class.
2. **Parsing**: `q=-term_s:ZZZZ` is assumed to parse to a pure-negative `BooleanQuery` under the test config's default parser (`solrconfig-collapseqparser.xml`); a different default `defType` could change the shape.
3. **Rank query wrapping**: other `RankQuery` implementations (e.g. LTR's `LTRQParserPlugin` query) may do their own rewrite; this fix only guarantees the *input* is queryable.
4. **Test overlap**: the class's `setUp()` already clears the index; `delQ("*:*")` is repeated for safety. `fl=id` with the default sort/score should not interfere with `count(//doc)` checks.
5. Distributed mode (shards) is not covered; the shard request goes through the same `getQueryCommand`.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.TestReRankQParserPlugin" --tests "org.apache.solr.search.QueryEqualityTest"
```
Fail-before: revert only `ResponseBuilder.java`; the `rq` assertion should return 0 docs.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
