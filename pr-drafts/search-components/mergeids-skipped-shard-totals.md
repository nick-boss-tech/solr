# Staging notes (not part of the PR text)

- Ticket number pending: Nick files the Jira ticket himself. The title below is a placeholder without a ticket number, and the Jira link line and the changelog line in the body carry a SOLR-XXXXX placeholder until he files it. The branch is `mergeids-skipped-shard-totals` on the fork; it renames to solr-<number>-submit at packaging, and the changelog fragment is created then.
- Proof is written at gated head 9e85ce5c162086ae806f617419c4405512792dde (full checks green there on 2026-10-11; receipts/mergeids-fix.md). The change from 73d20a0f939 is the test file only (four pin tests, no production change), so the production line citations are the same lines at the new head.
- Title placeholder: "Fix mergeIds counting a skipped partial shard in the merged totals, and the approximateTotalHits fallback adding the running total"

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-XXXXX

## What happens today

**A partial shard whose documents are dropped at merge time still counts in the merged totals, and the approximateTotalHits fallback adds the running total instead of the current shard's count.**

In a distributed search sorted on a field, a shard that answers with partial results and no sort values (the timeAllowed case) has its documents dropped by `QueryComponent.mergeIds`, but its totals were folded in first: on the base commit, the accumulation sits at [QueryComponent.java L1160-L1175](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1160-L1175) and the skip at [L1193-L1195](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1193-L1195). The merged response shows 3 documents with numFound 5, a maxScore of 99.0 that no returned document attains, and approximateTotalHits 11 instead of 4. In the same block, when a shard sends no approximateTotalHits header, the fallback at [L1162](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1162) adds the running merged numFound, which does not yet include the current shard: two shards of 2 and 3 documents merge to an approximate total of 2 instead of 5 in arrival order (the 2-document shard first; the other order gives 3). And when every shard is skipped, the merged count is the skipped shards' sum and is still reported exact.

## What this change does

**The skip is decided before any totals are folded in, the fallback adds the current shard's own count, and an all-skipped zero is not reported exact.**

The skip test now runs before the accumulation block, so a skipped shard contributes nothing to the merged totals ([QueryComponent.java L1180-L1203](https://github.com/nick-boss-tech/solr/blob/9e85ce5c162086ae806f617419c4405512792dde/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1180-L1203)). The fallback adds `docs.getNumFound()` for the current shard ([L1189-L1193](https://github.com/nick-boss-tech/solr/blob/9e85ce5c162086ae806f617419c4405512792dde/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1189-L1193)). When no shard was merged and a skipped shard reported hits, the merged zero is reported inexact ([L1238-L1243](https://github.com/nick-boss-tech/solr/blob/9e85ce5c162086ae806f617419c4405512792dde/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1238-L1243)). Behavior changes, stated openly: merged numFound, maxScore, approximateTotalHits and the exactness flag now describe exactly the shards whose documents were merged. The partialResults flag and the per-shard numbers in shards.info are unchanged.

## Proof

**QueryComponentPartialResultsTest: 17 of 17 pass at head `9e85ce5c16`, verified 2026-10-11.**

With QueryComponent.java reverted to the base commit and the branch tests kept, 7 of the 17 fail with the pre-change values: numFound 5 instead of 3 (twice, including the exact-flag case), maxScore 99.0 instead of 3.0, approximateTotalHits 11 instead of 4, the fallback 2 instead of 5, mixed headers 13 instead of 14, and the all-skipped count 5 instead of 0. The other 10 pass on the base as well; they pin behaviour the change must not break, including the four added during review (a merged shard's inexact count folds into the merged flag, a merged partial shard's totals are counted in full, the maximum is pinned with the highest score on the later shard, and an all-skipped merge whose shards reported no hits keeps its exact zero). The cases live in [QueryComponentPartialResultsTest.java](https://github.com/nick-boss-tech/solr/blob/9e85ce5c162086ae806f617419c4405512792dde/solr/core/src/test/org/apache/solr/handler/component/QueryComponentPartialResultsTest.java#L82-L447), which extends the mock harness added by SOLR-15595 with realistic totals on the fabricated shard lists.

## A choice to check

**The fallback fix rides in this PR as its own commit; it could be split into a separate PR.**

The fallback change is one line (commit d211794596b) with its own tests, and the ticket describes both defects, since they sit in the same accumulation block and a reader fixing one will meet the other. Splitting it out is a clean cherry-pick if one defect per PR reads better. I kept it here; was that the right call?

## Limits

**Two named limits.**

- The tests work at the merge level with fabricated shard responses, the project's own harness from SOLR-15595; no end-to-end MiniSolrCloud run reproduces a timed-out shard producing this shape. The arithmetic is pinned at the merge level, as in the ticket.
- `CombinedQueryComponent.mergeIds` folds its per-query totals in before the same partial skip, its approximateTotalHits fallback adds the running per-query total in the other direction (2 then 7 where 5 is correct, in arrival order; the other order gives 3 then 8), and its exactness fold is inverted. It is not changed here; I am happy to open a follow-up ticket and PR for it on request.

Changelog: [changelog/unreleased/SOLR-XXXXX.yml](https://github.com/nick-boss-tech/solr/blob/9e85ce5c162086ae806f617419c4405512792dde/changelog/unreleased/SOLR-XXXXX.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
