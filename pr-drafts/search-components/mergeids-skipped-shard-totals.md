# Staging notes (not part of the PR text)

- Short version of the PR text, written after the JIRA text of SOLR-18539 was shortened. The long version is saved at material/mergeids-skipped-shard-totals-v1-long.md.
- Draft text round 1 (reports/mergeids-pr-draft-text-round-1.md) applied the two reviews' findings that held, and the confirmation pass. Verdict there: READY for Nick's review. Opening still needs Nick's approval.
- Branch: `mergeids-skipped-shard-totals` on the fork. Packaged head 4eb996121804b043f1ee53f5bb7e22b9c3021b0a. Gate record: receipts/mergeids-fix.md.
- Proof: 17 of 17 at 4eb996121804 (2026-10-13 02:51 UTC) and at 9e85ce5c16 (2026-10-11 21:43 UTC).
- Title: "SOLR-18539: Skip partial shards before adding their totals in QueryComponent.mergeIds"

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18539

## What happens today

**A skipped partial shard's counts still reach the merged totals.**

In a search sorted on a field, `QueryComponent.mergeIds` adds each shard's totals before it tests for the partial-results skip. A shard is skipped only when its response is partial, it sent no sort values, and the sort uses a field other than score or doc. The links below point to upstream/main, `d76fc14b9857`. The totals are added at [L1160-L1175](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1160-L1175), and the skip is at [L1193-L1195](https://github.com/nick-boss-tech/solr/blob/d76fc14b9857e77b830d7a82514b28c523a239c2/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1193-L1195).

In `mergedNumFoundCountsOnlyMergedShards`, the skipped shard holds 2 documents and the other shard holds 3. On unmodified main, the merged response has 3 documents, but numFound is 5 and numFoundExact is true. The skipped shard's maxScore, 99.0, also comes through. approximateTotalHits was 11, not 4.

A shard with no `approximateTotalHits` header also adds the running total, not its own count. In `approximateTotalHitsFallsBackToEachShardsNumFound`, two shards with no approximateTotalHits header, holding 2 and 3 documents, give 2 instead of 5.

## What this change does

**The skip runs first, so a skipped shard adds nothing to the totals. The fallback adds the shard's own count.**

The skip and the totals are at [L1180-L1203](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1180-L1203). The fallback is at [L1188-L1193](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1188-L1193). If every shard is skipped, the merged total is 0. That 0 is marked exact only when no skipped shard reported hits ([L1238-L1243](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1238-L1243)).

Behavior change: merged numFound, maxScore, approximateTotalHits and numFoundExact now cover only the shards whose documents are merged. `partialResults` and `maxHitsTerminatedEarly` still fold in from every shard ([L1144](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1144), [L1156-L1161](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1156-L1161)). The per-shard numbers in `shards.info` are unchanged.

## Proof

**QueryComponentPartialResultsTest: 17 of 17 pass at head `4eb996121804`, verified 2026-10-12 20:51 MDT (2026-10-13 02:51 UTC).** The same 17 pass at `9e85ce5c16`, verified 2026-10-11 15:43 MDT (21:43 UTC).

At `4eb996121804`, with `QueryComponent.java` reverted to the base commit and the new tests kept, 7 of the 17 fail. Five are the values under What happens today. The other two are a mixed-header total of 13 instead of 14, and an all-skipped total of 5 instead of 0. The other 10 pass on the base too, so they check that existing merge results stay the same. The same 7 failed at `9e85ce5c16`.

The tests are in [QueryComponentPartialResultsTest.java](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/solr/core/src/test/org/apache/solr/handler/component/QueryComponentPartialResultsTest.java).

## A choice to check

**Merged numFound, maxScore, approximateTotalHits and numFoundExact now cover only the merged shards.** A skipped shard contributes to none of the four. Setting numFoundExact false alone would leave the counts wrong, so the change excludes the shard instead. Subtracting the shard afterwards cannot work, because maxScore cannot be decremented. Should numFoundExact also go false when a skipped shard reported hits, as the all-skipped zero rule does?

## Limits

**Not covered: multi-node runs and CombinedQueryComponent.**

- The tests stop at the merge step. No multi-node run covers a shard that timed out and returned a partial response.
- `CombinedQueryComponent.mergeIds` has the same ordering: it adds totals and the running-total fallback before its skip. It is not changed here. It is tracked in [SOLR-18541](https://issues.apache.org/jira/browse/SOLR-18541).

Changelog: [changelog/unreleased/SOLR-18539-mergeids-skipped-shard-totals.yml](https://github.com/nick-boss-tech/solr/blob/4eb996121804b043f1ee53f5bb7e22b9c3021b0a/changelog/unreleased/SOLR-18539-mergeids-skipped-shard-totals.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
