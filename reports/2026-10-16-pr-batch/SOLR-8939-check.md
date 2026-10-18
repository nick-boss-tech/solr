# SOLR-8939 (PR 5146) check: duplicate uniqueKey and the null slot

Status: refuted for a string uniqueKey on the non-grouped path. The `ShardDocQueue` subclass list is recorded below. Not tested: a non-string uniqueKey type (see the gap).

- Head: `solr-8939-submit` at `1a4a312bb7ab034fd932018691e76c86309f3b3d`.
- Test source, not committed to the fork: `reports/2026-10-16-pr-batch/SOLR-8939-tests/DuplicateUniqueKeyNullSlotTest.java`.
- Command: `:solr:core:test --rerun --tests org.apache.solr.handler.component.DuplicateUniqueKeyNullSlotTest` with `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1`, through `pool/bin/sweep-run.sh`.
- Setup: implicit-router collection with `shard1` and `shard2`, five unique docs, and the id `dup` added to each shard with `_route_`. Query `*:*`, `rows=20`, `fl=id`.
- Result: pass (0 failures). The log (`item5-8939-r3-implicit.log`) shows `dup` on both shards. The response has `numFound=6`, `size=6`, and no null slots.

## Why no null slot

`QueryComponent.java` (the `newShardDocQueue` implementation, around line 922) keeps `uniqueDoc` and returns `false` from `push` for an id already seen, so the queue never holds two entries for the same key. The branch's `setResultIdsAndResponseDocs` therefore builds the page from unique ids. The null slot the finding describes does not arise on this path.

Two earlier runs were not valid. In the first, the compositeId collection plus `distrib=false` updates put `dup` on shard1 only: the update was routed to the owning shard and the misrouted copy was dropped. In the second, the same direct-to-leader approach failed for the same reason. Both are recorded in the log history and are not evidence about the fix.

## Gap

The branch's commits also touch date unique keys (`0a97b52c7d` "keep same-second date keys apart when merging stored fields", `a855a2d896` "format date unique keys on the grouping stored-fields path too"). A duplicate key whose `id` objects differ in type between shards would bypass `uniqueDoc` and could collapse in `rb.resultIds`, which is where a null slot could come from. This was not tested. Next step: the same test with a date uniqueKey, and a grouped query.

## Subclasses of `ShardDocQueue` and `resultIds`

Searched upstream `origin/main` (local ref in `/workspace/solr`, may trail the remote):

- `QueryComponent.java:913` declares `public abstract Map<Object, ShardDoc> resultIds(int offset)`. The only implementation is the anonymous class in `newShardDocQueue` (`QueryComponent.java:919`, override at `:949`).
- Callers: `QueryComponent.java:1290` and `CombinedQueryComponent.java:438` (`queuePerQuery.resultIds(0)`). `CombinedQueryComponent` extends `QueryComponent` and calls `resultIds` but does not override it.
- No other in-tree subclass of `ShardDocQueue` or override of `resultIds`.

`ShardDocQueue` is `protected abstract static`, so external subclasses are possible. This search cannot see them.

## Escalation

The `resultIds` to `pageDocs` API change has no in-tree subclass to break. The changelog question is for the main agent, and the non-string uniqueKey gap should be checked before the PR is opened.
