# Staging notes (not part of the PR text)

- Short version of the PR text, written after the JIRA text of SOLR-18539 was shortened. The long version is saved at material/mergeids-skipped-shard-totals-v1-long.md.
- Branch: `mergeids-skipped-shard-totals` on the fork. Packaged head 4eb996121804b043f1ee53f5bb7e22b9c3021b0a. Gate record: receipts/mergeids-fix.md.
- Proof: 17 of 17 at 4eb996121804 (2026-10-13 02:51 UTC) and at 9e85ce5c16 (2026-10-11).
- Title: "SOLR-18539: Skip partial shards before adding their totals in QueryComponent.mergeIds"

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18539

## What happens today

**A skipped partial shard's counts still reach the merged totals.**

In a sorted distributed search, `QueryComponent.mergeIds` adds each shard's totals before it checks for the partial-results skip ([L1160-L1175](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1160-L1175), skip at [L1193-L1195](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1193-L1195)). A test on unmodified main gave 3 documents with numFound 5, numFoundExact true, maxScore 99.0 from the skipped shard, and approximateTotalHits 11 instead of 4. The fallback for a shard with no `approximateTotalHits` header also adds the running total, not that shard's own count. Two headerless shards of 2 and 3 documents give 2 instead of 5.

## What this change does

**The skip runs first, so a skipped shard adds nothing to the totals. The fallback adds the shard's own count.**

Skipped shards are excluded before the totals are added ([L1180-L1203](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1180-L1203)). The fallback adds `docs.getNumFound()` for the current shard ([L1189-L1193](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1189-L1193)). An all-skipped zero is reported exact only when no skipped shard reported hits ([L1238-L1243](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1238-L1243)).

Behavior change: merged numFound, maxScore, approximateTotalHits and numFoundExact now cover only the shards whose documents are merged. `partialResults` and `maxHitsTerminatedEarly` still fold in from every shard, and the per-shard numbers in `shards.info` are unchanged.

## A choice to check

**Merged numFound now counts only merged shards.** The skipped shard's documents did match the query, so the count can be lower than the number of hits. Was leaving skipped shards out the right call?

## Proof

**QueryComponentPartialResultsTest: 17 of 17 pass at head `4eb996121804`, verified 2026-10-13 02:51 UTC.** The same 17 pass at `9e85ce5c16`, verified 2026-10-11. The two heads differ only in the changelog file.

With `QueryComponent.java` reverted to the base commit and the new tests kept, 7 of the 17 fail with the values above. The other 10 pass on the base too, so they check that existing merge results stay the same. The tests are in [QueryComponentPartialResultsTest.java](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/solr/core/src/test/org/apache/solr/handler/component/QueryComponentPartialResultsTest.java).

## Limits

- The tests stop at the merge step. No multi-node run covers a timed-out shard.
- `CombinedQueryComponent.mergeIds` has the same defects. It is not changed here. A follow-up ticket and PR can be opened on request.
- The fallback fix is one line with its own tests. It can be split into a separate PR on request.

Changelog: [changelog/unreleased/SOLR-18539-mergeids-skipped-shard-totals.yml](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/changelog/unreleased/SOLR-18539-mergeids-skipped-shard-totals.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
