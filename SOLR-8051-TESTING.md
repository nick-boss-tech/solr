# SOLR-8051 - hypothetical reproduction (nothing was compiled or run)

JIRA: global stats NPE when not all cores are up; Markus Jelsma pointed at the `// TODO: nl == null if not all
shards respond (no server hosting shard)` line. Fix version 5.5 is a version tag, not proof. On `upstream/main`
`ExactStatsCache.doMergeToGlobalStats` guards only `shards.tolerant` + exception and `res.getException() != null`
(the second guard is later in the loop), but dereferences `res.getResponse()` (`.get(SHARD_NAME)`) with no null check.

## Change
Read `nl = res.getResponse()` first; if null, log at debug and skip that shard. SHARD_NAME is then read from `nl`.

## Test (guessed)
New `ExactStatsCacheMergeTest` (pure unit): `ShardResponse` carrying `new QueryResponse()` (response null, no exception)
passed to `mergeToGlobalStats`; passes if no NPE.

## Guesses to verify first
- `new SolrQueryRequestBase(null, params) {}` is acceptable (core null): `getShardsTolerantAsBool` and `getContext` do not need a core.
- `QueryResponse()` really leaves `getResponse()` null and `getException()` null.
- `ShardResponse` has a public no-arg constructor (`MockShardRequest` uses it); shard name stays null, which only affects a log line.
- Real-world trigger may differ (the original stack was Solr 5.3 ExactStatsCache line 103).

## Fail-before
Revert the guard: NPE at `res.getResponse().get(...)` (or at `nl.get(TERM_STATS_KEY)`).
