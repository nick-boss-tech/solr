# Staging notes (not part of the PR text)

- Short version of the PR text, written after the JIRA text of SOLR-18539 was shortened. The long version is saved at material/mergeids-skipped-shard-totals-v1-long.md.
- Rewritten for the narrowed head f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff (fast-forward from 4eb996121804b043f1ee53f5bb7e22b9c3021b0a, the head the PR was opened on). The changelog fragment is unchanged at both heads. Gate record: receipts/mergeids-fix.md.
- Proof at f9b0e5d722: 19 of 19 (seed 18539C0FFEE18539, 2026-10-14 00:31 UTC). Fail-before legs: 7 at d76fc14b9857, 3 at 4eb996121804, 2 at fbf7610644d1.
- Title: "SOLR-18539: Skip partial shards before adding their totals in QueryComponent.mergeIds"
- Live body of PR #5115 is not yet edited. The rewrite brief bars AI wording in the body, but the PR formula (section 6, Nick, 2026-10-04) keeps the AI header and footer on every PR description. The two AI lines below stay until that call is made.

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18539

## What happens today

**A skipped partial shard's counts still reach the merged totals.**

In a search sorted on a field, `QueryComponent.mergeIds` adds each shard's totals before it tests for the partial-results skip. A shard is skipped when its response is partial, it sent no sort values, and the sort uses a field other than score or doc. The links point to upstream/main, `d76fc14b9857`: the totals are added at [L1160-L1175](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1160-L1175), and the skip is at [L1193-L1195](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1193-L1195).

In `mergedNumFoundCountsOnlyMergedShards`, the skipped shard holds 2 documents and the other holds 3. On unmodified main the merged response has 3 documents, but numFound is 5 and numFoundExact is true. The skipped shard's maxScore (99.0) and approximateTotalHits (11, not 4) also come through. `mergedMaxScoreComesFromMergedShards` and `mergedApproximateTotalHitsExcludesSkippedShard` pin those values.

A shard with no `approximateTotalHits` header also adds the running total, not its own count. In `approximateTotalHitsFallsBackToEachShardsNumFound`, two such shards holding 2 and 3 documents give 2 instead of 5.

## What this change does

**A skipped shard adds nothing to the counts or the max score, and the fallback adds the shard's own count.**

The skip is at [L1179-L1186](https://github.com/nick-boss-tech/solr/blob/f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1179-L1186), before the totals and the fallback at [L1188-L1203](https://github.com/nick-boss-tech/solr/blob/f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1188-L1203). If every shard is skipped, the merged total is 0. The merged count is marked inexact when a skipped shard reported hits or an inexact count, whether or not other shards were merged ([L1180-L1184](https://github.com/nick-boss-tech/solr/blob/f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1180-L1184), [L1238-L1243](https://github.com/nick-boss-tech/solr/blob/f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1238-L1243)). A skipped shard that reported an exact zero leaves the flag alone.

Behavior change: merged numFound, maxScore and approximateTotalHits now cover only the merged shards. `partialResults` ([L1143](https://github.com/nick-boss-tech/solr/blob/f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1143)), `segmentTerminatedEarly` and `maxHitsTerminatedEarly` ([L1145-L1160](https://github.com/nick-boss-tech/solr/blob/f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1145-L1160)) still fold in from every shard. The per-shard numbers in `shards.info` are unchanged.

## Proof

**QueryComponentPartialResultsTest: 19 of 19 pass at head `f9b0e5d722`, verified 2026-10-13 18:31 MDT (2026-10-14 00:31 UTC), random seed `18539C0FFEE18539`.** The core module's check task, run without tests, also passes. The tests are in [QueryComponentPartialResultsTest.java](https://github.com/nick-boss-tech/solr/blob/f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff/solr/core/src/test/org/apache/solr/handler/component/QueryComponentPartialResultsTest.java).

With `QueryComponent.java` reverted to the base commit and the new tests kept, 7 of the 19 fail. Four fail on the values under What happens today. The other three are `skippedShardWithHitsMakesMergedCountInexact`, `allShardsSkippedDoesNotClaimExactZero` (a total of 5 instead of 0) and `approximateTotalHitsMixesHeaderAndFallbackShards` (13 instead of 14). The other 12 pass on the base too.

With the production code from the previous head, `4eb996121804`, 3 fail: `mergedNumFoundCountsOnlyMergedShards`, `skippedShardWithHitsMakesMergedCountInexact` and `allShardsSkippedWithInexactZeroDoesNotClaimExactZero`. With the code from `fbf7610644d1`, an earlier commit that cleared numFoundExact for any skipped shard, 2 fail: `skippedShardWithExactZeroLeavesMergedCountExact` and `allShardsSkippedWithNoHitsKeepsExactZero`.

## A choice to check

**A skipped shard clears the exact flag only when it reported hits or an inexact count.**

The other option clears the flag for any skipped shard. That is simpler and stricter, but it would also mark the count inexact when a skipped shard reported an exact zero, which adds no hits. The cost of the chosen rule is that it trusts a skipped shard's exact zero to be a true zero. No timeout run we made produced a skipped shard with an exact zero, so that case is not tested end to end. Was the narrower rule the right call?

## Limits

**Not covered: multi-node runs and CombinedQueryComponent.**

- The tests stop at the merge step. No multi-node run covers a shard that timed out and returned a partial response.
- `CombinedQueryComponent.mergeIds` has the same ordering and is not changed here. It is tracked in [SOLR-18541](https://issues.apache.org/jira/browse/SOLR-18541).

Changelog: [changelog/unreleased/SOLR-18539-mergeids-skipped-shard-totals.yml](https://github.com/nick-boss-tech/solr/blob/f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff/changelog/unreleased/SOLR-18539-mergeids-skipped-shard-totals.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
