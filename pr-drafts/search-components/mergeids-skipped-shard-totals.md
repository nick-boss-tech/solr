# Staging notes (not part of the PR text)

- Body trimmed to about 3,200 visible characters on 2026-10-14, at the owner's request. The text after the rule is the live PR body, PATCHed from this file.
- Head f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff (narrowed; fast-forward from 4eb996121804b043f1ee53f5bb7e22b9c3021b0a). The changelog fragment is unchanged at both heads. Gate record: receipts/mergeids-fix.md.
- Proof at f9b0e5d722: 19 of 19 (seed 18539C0FFEE18539, 2026-10-14 00:31 UTC). Fail-before legs: 7 at d76fc14b9857, 3 at 4eb996121804, 2 at fbf7610644d1.
- Title: "SOLR-18539: Skip partial shards before adding their totals in QueryComponent.mergeIds"

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18539

## What happens today

**A skipped partial shard's counts still reach the merged totals.**

In a sorted search, `QueryComponent.mergeIds` adds each shard's totals before it tests for the partial-results skip. A shard is skipped when its response is partial, it sent no sort values, and the sort is on a field other than score or doc. On upstream/main, `d76fc14b9857`, the totals are added at [L1160-L1175](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1160-L1175) and the skip is at [L1193-L1195](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1193-L1195).

In `mergedNumFoundCountsOnlyMergedShards`, the skipped shard holds 2 documents and the other holds 3. The merged response has 3 documents, but numFound is 5 and numFoundExact is true. The skipped shard's maxScore (99.0) and approximateTotalHits (11) also come through. A shard with no `approximateTotalHits` header adds the running total instead of its own count, so `approximateTotalHitsFallsBackToEachShardsNumFound` returns 2 instead of 5.

## What this change does

**A skipped shard adds nothing to the counts or the max score.**

The skip now runs before the totals and the fallback. If every shard is skipped, the merged total is 0. The merged count is marked inexact when a skipped shard reported hits or an inexact count, whether or not other shards merged. A skipped shard that reported an exact zero leaves the flag alone.

Behavior change: merged numFound, maxScore and approximateTotalHits now cover only the merged shards. `partialResults`, `segmentTerminatedEarly` and `maxHitsTerminatedEarly` still fold in from every shard. The per-shard numbers in `shards.info` are unchanged.

## Proof

**QueryComponentPartialResultsTest: 19 of 19 pass at head `f9b0e5d722`, verified 2026-10-14 00:31 UTC, random seed `18539C0FFEE18539`.** The core module's check task also passes.

With `QueryComponent.java` reverted to the base commit and the new tests kept, 7 of the 19 fail: the four value checks above, plus `skippedShardWithHitsMakesMergedCountInexact`, `allShardsSkippedDoesNotClaimExactZero` (a total of 5 instead of 0) and `approximateTotalHitsMixesHeaderAndFallbackShards` (13 instead of 14). With the previous head, `4eb996121804`, 3 fail. With `fbf7610644d1`, which cleared the flag for any skipped shard, 2 fail.

## A choice to check

**A skipped shard clears the exact flag only when it reported hits or an inexact count.**

The other option clears the flag for any skipped shard. That is simpler and stricter, but it marks the count inexact even when a skipped shard reported an exact zero. The cost of this rule is that it trusts a skipped shard's exact zero to be a true zero. No timeout run produced a skipped shard with an exact zero, so that case is not tested end to end. Is the narrower rule the right call?

## Limits

- The tests stop at the merge step. No multi-node run covers a shard that timed out and returned a partial response.
- `CombinedQueryComponent.mergeIds` has the same ordering and is not changed here. It is tracked in [SOLR-18541](https://issues.apache.org/jira/browse/SOLR-18541).

Changelog: [changelog/unreleased/SOLR-18539-mergeids-skipped-shard-totals.yml](https://github.com/nick-boss-tech/solr/blob/f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff/changelog/unreleased/SOLR-18539-mergeids-skipped-shard-totals.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
