# solr-17976-submit

- Branch: origin/solr-17976-submit
- Head: 56ea43c448ed (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 56ec140e3636, 56 commits behind, 6 commits ahead)
- Scope: 6 commits, 7 files. `QueryComponent.java` (new `shardNamesByShardAddress(rb)`, which maps each `rb.shards[i]` replica list to `rb.slices[i]`; `ShardDoc.shardName` set in `mergeIds`), `CombinedQueryComponent.java` (same mapping and `shardName` set in the RRF/combined merge), `ShardDoc.java` (new `shardName` field), `ShardFieldSortedHitQueue.java` (tie-break compares `shardName`, falls back to `shard`), tests `TestShardTieBreak.java` (unit: name wins over address; address fallback) and `TestShardTieBreakCluster.java` (cluster: NRT and PULL preferences give identical order), changelog `SOLR-17976.yml` (`type: fixed`).
- Verdict: Nearly (unchanged from the bulk verdict). The id chain from request to comparator holds on every path I traced. The remaining gaps are test coverage on the combined path and a changelog that does not mention explicit `shards=` URLs.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-17976-review.md` (Nearly) reviewed this head (`56ea43c448e`). No delta. The older round files for this ticket (`research/branch-reviews/round-*/SOLR-17976-review.md`) were background only.

- Bulk LOW (the new combined-query path, `CombinedQueryComponent`, has no direct regression test): **confirmed.** `TestShardTieBreakCluster.java` covers the plain `QueryComponent` path only (`testTieBreakDoesNotDependOnReplicaPreference`, `:150-160`). No test sends a combiner (RRF) request. See finding 1.
- Bulk line references: **confirmed at head.** Helper `QueryComponent.java:978`; `shardName` set at `QueryComponent.java:1232` (map built at `:1056`); `CombinedQueryComponent.java:323` and `:424`; comparator `ShardFieldSortedHitQueue.java:110` and `tieBreakShard` at `:116`.

## Findings (ranked)

1. **LOW, verified. No regression test on the combined (RRF) path.** `CombinedQueryComponent` sets `shardDoc.shardName` at `:424` from a map built at `:323`. The only cluster test uses a plain query. A combiner request with equal scores across replica preferences would exercise that caller and is not covered. The bulk review's suggestion stands: add a combined-query tie case when practical.

2. **LOW, verified (checked, no defect). The name lookup is aligned on every path I traced.** The chain holds:
   - `rb.slices[i]` comes from `replicaSource.getSliceNames()` (`HttpShardHandler.java:510`) or is a `null`-filled array for explicit URLs (`:552`). `rb.shards[i]` is `createSliceShardsStr(getReplicasBySlice(i))` at `:557`. Both are indexed by slice, so the pairing is by index.
   - The shard string the comparator sees is the one `submit` receives: `prepareShardResponse(sreq, shard)` calls `srsp.setShard(shard)` (`HttpShardHandler.java:225,248`). `SearchHandler.java:554` sets `actualShards = shards`, and `ActiveTaskQuerySupport.java:91-92` copies the same array. So `srsp.getShard()` equals `rb.shards[i]`, and the map lookup succeeds.
   - Multi-collection requests do not collide. `CloudReplicaSource` prefixes slice names with the collection when `multiCollection` is set (`CloudReplicaSource.java:81-85`, `ClientUtils.addSlices(..., multiCollection)` at `:200`), so `a_shard1` and `b_shard1` stay distinct.

3. **LOW, verified (scope). The changelog does not mention explicit `shards=` URLs.** `QueryComponent.shardNamesByShardAddress` leaves explicit URLs out of the map (`rb.slices[i]` is null there). `ShardFieldSortedHitQueue.tieBreakShard` then falls back to the address, so ties for explicit-URL requests still depend on the replica address. The code comment says so, and the unit test `testTieBreakFallsBackToShardAddress` pins the fallback. The changelog title says ties now break "by shard name instead of replica address", with no exception. Owner call 1.

4. **LOW, verified (checked, no issue). Tie order is only changed where scores are equal.** `lessThan` only reaches the shard tie-break when the score comparison returns 0 (`ShardFieldSortedHitQueue.java:104-111`). Two docs from the same slice share one address and one name, so they compare as before. The changelog's note that tied order "differs from earlier releases" is accurate.

5. **Not a finding. Realtime-get ids fetch.** `RealTimeGetComponent.java:1081-1090` builds its own shard strings with `sliceToShards`, which are not in `rb.shards`. Those lookups miss and fall back to the address. Whether that path ever reaches `ShardFieldSortedHitQueue` was not checked (see Not checked).

## Owner calls (not decided here)

1. **Explicit `shards=` URLs.** Should explicit-URL requests get a deterministic tie order too, or is the address fallback a documented limit? If it stays a limit, the changelog should say so (finding 3). The bulk review and this review leave the decision to the owner.

## Proposed fixes (not applied; the owner decides)

- Finding 1: one combined-query cluster test with equal scores and NRT and PULL preferences, asserting the same order twice.
- Finding 3: add a sentence to the changelog that explicit shard URLs keep the address fallback, or change the behavior per owner call 1.

## Not checked

- Nothing compiled, formatted, or run. The unit and cluster test bodies were read at the assertion lines, not executed.
- Whether `RealTimeGetComponent`'s shard strings can reach `ShardFieldSortedHitQueue` (finding 5).
- Whether `ShardDoc` objects created outside `mergeIds` (for example in re-ranking or the ids-fetch stage at `QueryComponent.java:1448`) carry a `shardName`. Those paths were not traced for ordering.
- The JIRA packet was not re-read; the maintainer's comment on the shard-name tie-break is taken from the bulk review.
