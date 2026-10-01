# SOLR-15319 — testing handoff for external reviewer

## Ticket
https://issues.apache.org/jira/browse/SOLR-15319 — "ExactStatsCache not always producing Distributed IDF"
Reporter: Cameron VandenBerg. Research note: `pipeline/research-notes/SOLR-15319.md`.

## What was changed
`solr/core/src/java/org/apache/solr/search/stats/ExactStatsCache.java` only:

- `doMergeToGlobalStats` no longer keys the per-shard stats maps by the bare
  shard id from the `shard.name` response value. It keys by `ShardResponse.getShard()`,
  the shard URL string, which embeds the collection name and is therefore unique
  per (collection, shard).
- `doSendGlobalStats` looks the per-shard stats up by the `rb.shards` URL entries
  directly, instead of parsing them with `StatsUtil.shardUrlToShard(collectionName, url)`
  (which fell through to the raw URL for any other collection's shards, so those
  stats were silently skipped).
- `doReturnLocalStats` still sends `shard.name` (bare shard id) for older
  aggregating nodes that key by it; this class no longer reads it.
- `LRUStatsCache` extends `ExactStatsCache` without overriding these methods, so it
  inherits the fix (and had the same flaw).

## Why
A multi-collection query (`/solr/collection1,collection2/query`) with ExactStatsCache
could score with one collection's local IDF: `collection1/shard1` and `collection2/shard1`
both stored under `shard1`, the later response clobbering the earlier one
(non-deterministic, matching the reporter's observation), and the lookup side could
never match the other collection's URLs at all.

## What was NOT done
- No Gradle build or test run (per pipeline Phase 2 protocol — intentionally
  uncompiled/untested).
- `StatsUtil.shardUrlToShard` is now unused by this class but left in place; it is
  public API and may have other callers.
- The `shard.name` response key is still emitted for mixed-version back-compat.

## Suggested validation (for the reviewer with a build environment)
- New/existing coverage: `TestExactStatsCacheCloud` — add a two-collection case with
  same-named shards (`shard1` in both), a discriminating term, `debug=true`; assert the
  explain's `docCount` equals the sum across both collections.
- Regression: single-collection queries must produce unchanged stats (keys changed
  from `shard1` to full URL strings, but store and lookup agree).
- Mixed-version sanity: old aggregator + new shard still works via `shard.name`;
  new aggregator + old shard works because the new code keys by URL, which old shards
  also produce via `r.getShard()`.
- Non-cloud distributed search (`shards=` param with URLs): previously the lookup side
  parsed URLs against the local core name and could miss; now both sides use the raw
  URL strings, so this case is fixed as well.
- Run tidy/Error Prone on the module (`-Pvalidation.errorprone=true`); the change
  removed the only `ArrayList` usage — import already cleaned up.

## Reviewer watch-outs
- Keying by full URL strings (possibly `|`-joined replica lists) makes the
  request-scoped map keys longer; maps are discarded after the request, so no
  memory concern beyond the request itself.
- If two `rb.shards` entries ever alias the same physical shard with different URL
  strings, stats would be stored twice under different keys and double-counted in
  the global aggregation. The submit path uses the identical strings for
  `setShard`, so this should not happen, but worth a glance during review.
- This branch must be rebased onto current `apache/solr` main and the handoff file
  removed before any upstream PR. No PR opened, no Jira comment posted.
