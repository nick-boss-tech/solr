# PR draft: order a leading _docid_ sort across shards by slice (not filed)

Status: DRAFT. Not opened. Ticket: SOLR-TBD (placeholder).

Base: stacked on the SOLR-6975 branch (PR #5135, solr-6975-submit at 6c033ba6ea). Open against upstream main only after #5135 merges, or retarget then. Until then the diff also shows the #5135 commits.

Branch: solr-slice-order on nick-boss-tech/solr, commit f82db58 (one commit on 6c033ba6ea).

Title: SOLR-TBD: Order a leading _docid_ sort across shards by slice, not by replica list

Body:

```
🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-TBD

## What happens today

**With a leading `_docid_` sort, a distributed search can return overlapping pages for the same query.**

The merge ranks documents from different shards by `ShardDoc.shard`, which is the shard's replica list in the order this request built it. The list is shuffled for each request, so the first replica decides the order between shards. When the shard addresses interleave as strings, the order flips between requests, and paging with `start` and `rows` repeats some documents and skips others.

## What this change does

**The merge ranks documents from different shards by slice name, so the replica that answers no longer changes the order.**

`ShardDoc` gets a `sliceName`. `QueryComponent.mergeIds` and `CombinedQueryComponent.mergeIds` set it from `rb.slices`, matched to each response through `rb.shards`. `ShardFieldSortedHitQueue.lessThan` uses it for the cross-shard tiebreak. `ShardDoc.shard` still names the request target, so routing does not change.

## Proof

**The new unit test fails on the SOLR-6975 comparator and passes with this change.**

- `ShardFieldSortedHitQueueCycleTest.testCrossShardOrderIgnoresReplicaAddress` fails with the comparator from `6c033ba6ea` (`expected:<false> but was:<true>`) and passes with this change. The other test in that class still passes.
- Paging probe with default routing, 2 shards, 2 replicas each, 40 documents, `rows=7`: four fresh clusters, three page sets each, 12 of 12 returned all 40 ids. Two of the clusters had interleaved shard addresses. On the SOLR-6975 head, 8 of 9 page sets failed in the interleaved clusters.
- Focused run, seed `6975C0FFEE6975`: 32 tests across the cycle test, `DistributedQueryComponentOptimizationTest`, and the combined-query tests, 0 failures, 0 skipped.

## Limits

- Stable paging also depends on each replica keeping the same `_docid_` values. This change does not check that.
- Results are ordered by slice name, which is an ordering choice. A reviewer may prefer a different one.

## AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
```
