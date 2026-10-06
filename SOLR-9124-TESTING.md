# SOLR-9124 - hypothetical reproduction (nothing was compiled or run)

JIRA (2016, 5.5): "Grouped Results does not support ExactStatsCache". The audit note said "fixed in 6.2/7.0 (fix version set)" - a
fix version only, no symbol checked.

## What main does
`QueryComponent.groupedDistributedProcess` calls `createDistributedStats(rb)` at `STAGE_PARSE_QUERY`, so the term stats ARE
collected and merged into the global stats. But the requests built by `SearchGroupsRequestFactory` (top groups) and
`TopGroupsShardRequestFactory` (execute query) never get `StatsCache.sendGlobalStats(...)` or `PURPOSE_SET_TERM_STATS`; the only
callers on main are `QueryComponent.createMainQuery` (ungrouped path) and `DebugComponent`. The shards therefore score groups
with local stats.

## Change
In `groupedDistributedProcess`, for the two group stages (not the stored-fields stage), when the request needs distributed stats
(`needsDistributedStats`, the condition already used by `createDistributedStats`, extracted as a helper) each outgoing request gets
`PURPOSE_SET_TERM_STATS` and `sendGlobalStats`. Test: `TestBaseStatsCache.checkGroupedScores` (new hook called from
`TestDefaultStatsCache.test`, no-op for the default `LocalStatsCache`) compares the top score per `shard_i` group between the
control client and a sharded query.

## Guesses to verify first
- `shard_i` is groupable in the distributed test schema (single valued int); switch to `id` groups if not.
- `ExactStatsCache.doSendGlobalStats` needs `TERMS_KEY` in the request context, which `retrieveStatsRequest` sets during the
  parse stage - assumed to happen for grouped queries too.
- `ShardRequest.purpose` is copied to `shards.purpose` when the request is sent, so the shard's `QueryComponent.prepare`
  receives the global stats (same as the ungrouped path).
- Docs with equal scores are not asserted in order, only top score per group.

## Fail-before
Expected: `TestExactStatsCache`, `TestLRUStatsCache` and `TestExactSharedStatsCache` fail on `upstream/main` (group scores differ
from the control's). Enqueue `org.apache.solr.search.stats.TestExactStatsCache` with `-WithFailBefore`.
