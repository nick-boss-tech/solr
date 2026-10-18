# SOLR-TBD cross-shard order of a leading _docid_ sort: gate and probe (vm4, 2026-10-18)

Branch solr-slice-order on nick-boss-tech/solr, commit f82db58 on top of 6c033ba6ea (SOLR-6975 head). Ticket is a placeholder, SOLR-TBD.

## Change

- `ShardDoc` gets `sliceName`.
- `QueryComponent.mergeIds` and `CombinedQueryComponent.mergeIds` set it from `rb.slices`, matched to each response through `rb.shards`.
- `ShardFieldSortedHitQueue.lessThan` uses `sliceName` for the cross-shard tiebreak. `ShardDoc.shard` is unchanged, so routing is unchanged.
- New test `ShardFieldSortedHitQueueCycleTest.testCrossShardOrderIgnoresReplicaAddress`.

## Gate at f82db58: GREEN

- Changelog YAML parse of changelog/unreleased/SOLR-TBD-distributed-docid-shard-order.yml: rc=0.
- Tidy: rc=0. The first tidy run reformatted the new test, which was then committed (amend, unpushed). Tidy on the committed tree: rc=0, no changes.
- Error Prone compile (compileJava and compileTestJava, -Pvalidation.errorprone=true): rc=0.
- Focused run, seed 6975C0FFEE6975, fresh JUnit XML: ShardFieldSortedHitQueueCycleTest 2 tests; DistributedQueryComponentOptimizationTest 10; CombinedQueryComponentTest 6; CombinedQuerySearchHandlerTest 2; CombinedQuerySolrCloudTest 9; DistributedCombinedQueryComponentTest 3. Total 32, 0 failures, 0 errors, 0 skipped.
- Module check `:solr:core:check -x test`: rc=0.
- Logs: /workspace/gates/logs/vm4/solr-slice-order/ (XML in xml/).

## Fail-before

- `ShardFieldSortedHitQueue.java` reverted to 6c033ba6ea, new test kept, seed as above: ShardFieldSortedHitQueueCycleTest 2 tests, 1 failure. The failure is `testCrossShardOrderIgnoresReplicaAddress`, `expected:<false> but was:<true>`. Log: failbefore.log, XML failbefore-TEST.xml.

## Paging probe on the fix (default routing)

- Same probe as the SOLR-6975 paging receipt: 2 shards, 2 replicas each, 40 documents, rows=7, pages at 0 to 35, 3 page sets per cluster, four fresh clusters.
- Interleaved clusters (shard addresses interleave as strings): run-2 and run-4, 3 of 3 page sets each returned 40 distinct ids.
- Ordered clusters: run-1 and run-3, 3 of 3 each returned 40 distinct ids.
- 12 of 12 page sets returned all 40 ids. Before the fix, 8 of 9 page sets failed in interleaved clusters (receipts/SOLR-6975.md, Paging mechanism).
- Logs: /workspace/gates/logs/vm4/solr-slice-order/probe/.

## Not run

- No run on the upstream main base. The change depends on the SOLR-6975 comparator.
- No run that checks stable paging after index changes or replica restarts.
- The probe test is not in the repo.
