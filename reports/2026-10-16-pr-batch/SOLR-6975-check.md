# SOLR-6975 (PR 5135) check: `_docid_` paging across replicas, and merge comparator

Status: resolved. The cycle is fixed in `6c033ba6ea` on `solr-6975-submit`; the paging repeats and gaps are documented as a known limitation in the PR body.

- Head: `solr-6975-submit` at `c666155ed058a0535f60e8c51a49c25f17821f98`. The fix commit is `192d36bc20`.
- Test sources, not committed to the fork: `reports/2026-10-16-pr-batch/SOLR-6975-tests/` (`DocIdPagingWithReplicasTest.java`, `ShardFieldSortedHitQueueCycleTest.java`).
- Command: `:solr:core:test --rerun --tests ...` with `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1`, through `pool/bin/sweep-run.sh`.

## Check 1: paging with replicas (replicationFactor 2)

Setup: 2 shards, 2 replicas each, 40 documents with `id` only, paged with `sort=_docid_ asc`, `rows=7`, `start=0,7,...,35`.

Result at head (log `/workspace/gates/logs/vm5/code-round-1/item3-6975-r4.log`, output in the test XML):

- Before any change, the four replicas return the same `_docid_` order (shard1 and shard2 each agree). Still, the paging is wrong on the first pass: `start=28` and `start=35` repeat ids already returned at `start=7` and `start=14` (for example 15, 16, 20, 22, 24, 27, 28, 32, 34, 35, 36, 38). 40 slots return 28 distinct ids, so 12 documents are never returned. The same output came from two separate runs (r3 and r4), so the result is deterministic for this layout.
- After one shard1 replica is reordered (`REORDERED core_node8`, docids reversed, done with `distrib=false`), each query follows the replica that answers. Page contents then differ between rounds, with the reordered replica's order appearing on some pages and not others. All 20 rounds had a repeat or a gap. This confirms the claim that identical requests can return different paging when replicas order documents differently.

Pre-fix comparison (`192d36bc20^` restored in the scratch worktree, log `item3-6975-prefix-r5.log`): distributed `_docid_ asc` fails with HTTP 500, `IndexOutOfBoundsException: Index 0 out of bounds for length 0`. So the fix removes that crash and exposes the repeat-and-gap paging above.

## Check 2: merge comparator when `_docid_` leads

`ShardFieldSortedHitQueue.getCachedComparator` returns 0 for `SortField.Type.DOC`, so across shards the next sort key decides, and same-shard pairs are ordered by position. With sort `_docid_ asc, price asc`, the queue's `lessThan` forms a cycle among three documents: A1 (shard1, position 0, price 5), A2 (shard1, position 1, price 1), B1 (shard2, price 3). Checked with `ShardFieldSortedHitQueueCycleTest` on the head: `assertFalse` fails at line 44, so the relation is not a strict order.

## Resolution

- Cycle (check 2): the comparator formed a cycle only when `_docid_` was the first sort key. The shard returns its hits in docid order, so within a shard the position no longer matches the next key, and the cross-shard comparison disagrees with the same-shard one. Fix in `6c033ba6ea`: when `_docid_` leads, the keys after it are skipped, so the merge groups by shard name and keeps each shard's order. The cycle test `ShardFieldSortedHitQueueCycleTest` is added to the PR. It fails on the previous head (`assertFalse` at line 44) and passes on `6c033ba6ea`.
- `DistributedQueryComponentOptimizationTest` passes 10 of 10 at `6c033ba6ea`. Run: `:solr:core:test --rerun` with those two classes, log `/workspace/gates/logs/vm5/code-round-1/6975-fix-r1.log`.
- Paging (check 1): the repeats and gaps are observed at the PR head with 2 shards, 2 replicas and 40 documents, and the cause is not isolated. This change does not address paging. The PR body now says so under "Known limitation" (readback verified), instead of claiming stable paging.

The escalation to the main agent is withdrawn: there is no main agent. The decision follows the PR's stated intent: each shard keeps its own order, and shards are grouped by name.
