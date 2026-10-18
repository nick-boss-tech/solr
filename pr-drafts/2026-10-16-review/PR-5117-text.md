# PR 5117 (SOLR-18541): body draft, round 2 item 5

- Head: 81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d (live body read 2026-10-18T04:33Z; branch head matches).
- Visible characters: 3483 (limit 3,500).
- RE-DRAFT NEEDED if code round 2 item 3 changes numFound or approximateTotalHits. That item is HANDED to vm1 and has not run. The draft describes the code as it stands: numFound keeps the largest sub-query count (`mergeIds`; `QueryAndResponseCombiner.simpleCombine` uses `Math.max`).
- Proof numbers come only from receipts/SOLR-18541.md: gate at 81a88e40dd, vm1, 2026-10-16 (focused CombinedQueryComponentMergeTotalsTest 9 tests, 0 failures, 0 errors, XML 2026-10-16T06:35:36Z); proof leg A at merge-base 597ed6774083 (9 tests, 6 failures, named); proof leg B at prior head a93f9f6a997e (1 failure, multipleContributingSubQueriesClearExactness).
- Changes from the stored body: proof paragraph and "verified" date corrected to the receipt (the stored body said seven tests fail on base and dated 2026-10-15; the receipt says 6 of 9 fail and dated 2026-10-16). The counts "21 instead of 11" are dropped: no receipt source. The simpleCombine `max()` note is added to Limits. The "what happens today" and "what this change does" sections are condensed. Trimmed under 3,500 visible characters.

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18541

## What happens today

**Combined queries report totals that count shards the merge dropped, double-count shards with no approximateTotalHits header, and invert the exactness flag.**

In `CombinedQueryComponent.mergeIds`, a shard's totals are added before the partial-results skip is tested ([lines 342 to 343](https://github.com/nick-boss-tech/solr/blob/597ed67740831566b708b793f52554b2e49e7e17/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L342-L343), [lines 399 to 400](https://github.com/nick-boss-tech/solr/blob/597ed67740831566b708b793f52554b2e49e7e17/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L399-L400)). A skipped shard still adds its hits. With fabricated shard responses, the merged numFound was 7 where 3 documents were merged, and approximateTotalHits was 15 instead of 5. A shard with no approximateTotalHits header falls back to the running merged total, so two shards merge to 7 instead of 5. The exactness fold is inverted: all-exact merges report false, and all-inexact merges report true.

## What this change does

**The skip is tested before any total is added, the fallback uses the shard's own count, and numFoundExact is cleared when a shard is skipped or more than one sub-query contributes.**

The skip now sits above the totals in the same loop ([lines 389 to 467](https://github.com/nick-boss-tech/solr/blob/81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L389-L467)). A skipped shard adds nothing to numFound or approximateTotalHits, but it still clears numFoundExact, because it may hold unreported matches. With more than one contributing sub-query, numFoundExact is cleared even if every count is exact. numFound still keeps the largest sub-query count.

## Proof

**Nine tests in `CombinedQueryComponentMergeTotalsTest` pass at head `81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d` (verified 2026-10-16). Six of them fail on the base code.**

- Base (merge-base `597ed6774083`, branch test file in place): 9 tests, 6 failures. `skippedPartialShardApproximateTotalExcluded` (approximateTotalHits 15, expected 5), `fallbackUsesEachShardsOwnCount` (7, expected 5), `skippedPartialShardContributesNothing` (numFound 7, expected 3), `skippedPartialShardWithHitsClearsExactness` (numFound 2, expected 0), and two single-sub-query exactness tests, where the base fold inverts numFoundExact.
- Three tests pass on base only because of the inverted fold, so they are not fail-before proof.
- Prior head `a93f9f6a997e`: exactly one failure, `multipleContributingSubQueriesClearExactness` (`numFoundExact expected:<false> but was:<true>`). It is the discriminating test.

## A choice to check

**This choice is answered on the ticket: numFoundExact is false when more than one sub-query contributes. The distinct-document count is a follow-up.**

numFound keeps the largest sub-query count. The alternative, counting distinct matching documents, is a follow-up. On the ticket, the maintainer directed the minimum change: clear numFoundExact when more than one sub-query contributes.

## Limits

**The tests use fabricated shard responses. One defect from a real timeAllowed exit is not fixed here.**

- A real timeAllowed exit after the first sub-query leaves a shorter list. The merge then throws `IndexOutOfBoundsException` at the unchecked access ([lines 338 to 340](https://github.com/nick-boss-tech/solr/blob/81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L338-L340)). This change leaves that access alone, and the run was not repeated at this head. A size check would fix it.
- `QueryAndResponseCombiner.simpleCombine` takes the largest sub-query count as its total (`Math.max`). This change leaves it alone. The maintainer named it as having the same cause on main.

Changelog: [changelog/unreleased/SOLR-18541-combined-query-merge-totals.yml](https://github.com/nick-boss-tech/solr/blob/81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d/changelog/unreleased/SOLR-18541-combined-query-merge-totals.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
