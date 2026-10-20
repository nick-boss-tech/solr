# Skip record: SOLR-10952

- Ticket: SOLR-10952, Exact*Stats are ignored due to query result cache. JIRA read in this pass: Open, resolution none, fix versions none, 4 comments (2017-06-26 and 2018-02-20 x3); link: SOLR-10824 (Resolved); updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J, C and L).
- Draft branch: solr-10952-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: with ExactStats (exact distributed term statistics), a query result cached on one shard is replayed after commits without the exact global stats, so scores stay local. The reporter cites SOLR-10824 and a test seed for TestExactSharedStatsCache that needs a rollback of 7c44862 to reproduce. The comments discuss disabling the query result cache for ExactStats, or regenerating the cache with stats.

## Code reading

- solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:1567-1578 (getDocListC): the query result cache is consulted with a QueryResultKey built from the query, filters, sort, flags, minExactCount and cmd.isDistribStatsDisabled(). There is no check of the stats cache type (ExactStatsCache or ExactSharedStatsCache) in this key or in the lookup at this point.
- SolrIndexSearcher.java:744-760: the query result cache regenerator re-runs the query when the searcher is replaced (warming); the regeneration runs on the local searcher, which is the comment's "cache regeneration is local only" concern.
- solr/core/src/java/org/apache/solr/search/stats/ExactStatsCache.java:61 and ExactSharedStatsCache.java:37 hold the exact-stats implementations; grep for queryResultCache or any cache bypass in the stats package returns no hits, so ExactStats does not disable the query result cache at the pin.
- solr/core/src/test/org/apache/solr/search/stats/TestExactSharedStatsCache.java exists at the pin, along with TestExactSharedStatsCacheCloud and TestExactStatsCacheCloud, so the scenario has test coverage in the tree; the test named in the ticket was not run.

So the mechanism the ticket describes (a query-result cache entry that is served without the exact stats after a commit) is not ruled out by reading: nothing at the pin disables or keys the query result cache on the stats mode.

## History

- No commit at the pin names SOLR-10952 (git log --grep at the pin: none).
- SOLR-10824 (Resolved) is linked; its commit was not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs TestExactSharedStatsCache (or the seed in the ticket) at the pin, plus a two-shard query with a commit between two identical queries, recording whether the second result's scores match the exact global stats.

## Gap analysis

The code path matches the ticket: the query result cache is keyed without the stats mode, and no bypass for ExactStats exists at the pin. Whether a stale cached result actually surfaces with the existing tests is the open runtime question.

## Verdict justification

keep-open: the path is present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made. The design choice (disable the cache for ExactStats, or key the cache on stats mode) is for maintainers.

Missing for a closing verdict: a run of TestExactSharedStatsCache at the pin with the seed from the ticket, and a two-shard query-after-commit check; a passing run supports close-cannot-reproduce, and a failure confirms the open defect.

## Limits

This record does not run the test, does not trace SOLR-10824, and does not post anything to JIRA or GitHub.
