# SOLR-17976 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** This patch was written without running
Gradle (no compile, no tidy, no tests). A reviewer must compile and test
before this goes anywhere near a PR.

## What the patch does

In distributed search result merging, `ShardFieldSortedHitQueue.lessThan()`
broke ties between equal-scoring docs with `-docA.shard.compareTo(docB.shard)`.
`ShardDoc.shard` is the **replica core URL** (it includes the node, e.g.
`http://node1:8983/solr/coll_shard1_replica_n1`), so different requests routed
to different replicas of the same shards produced different orderings —
non-deterministic distributed ranking.

The patch (maintainer-endorsed direction, hossman agreed on the ticket):

- `ShardDoc` gains a `shardName` field: the SolrCloud shard name resolved from
  the replica URL, null when unavailable.
- `QueryComponent.mergeIds()` resolves the request's `DocCollection` from
  cluster state once per merge, then maps each distinct replica URL to its
  shard name (matching `Replica.getCoreName()` / `getCoreUrl()`), caching per
  URL. Each `ShardDoc` gets `shardName` set alongside `shard`.
- `ShardFieldSortedHitQueue.lessThan()` tie-breaks on the shard name when
  present, falling back to the old `shard` comparison otherwise.

`ShardDoc.shard` itself is untouched — it is still the replica address used
for follow-up field fetching (`sreq.shards = ...`) and dedup bookkeeping.

## Recommended reviewer commands

```bash
# copy gradle.properties into the worktree first (worktrees don't inherit it)
cp ~/workspace/solr/gradle.properties .

~/workspace/tools/solr-gradle.sh :solr:core:spotlessApply
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true

# targeted tests, serially (never two Gradle builds at once on this VM)
~/workspace/tools/solr-gradle.sh :solr:core:test \
  --tests "org.apache.solr.handler.component.*" \
  -Pvalidation.errorprone=true
```

Suggested new tests (not written):

1. `ShardFieldSortedHitQueue.lessThan()` unit test: two docs with equal
   comparator results, `shardName` = "shard1"/"shard2" but `shard` URLs
   differing in node IP — ordering must be identical regardless of which
   replica URL is attached, and shard1 must consistently win/lose vs shard2.
2. `resolveShardName` against a fake `DocCollection` (2 shards x 2 replicas):
   assert each replica URL maps to its shard name, and unknown URLs map to
   null (fallback path).

## Patch limits, risks, open questions

- **Not compiled or tested.** The logic is straightforward but the cluster-state
  plumbing (`getRequestCollection`) is new code paths — verify in a real
  SolrCloud test, especially that `rb.req.getCore().getCoreDescriptor()
  .getCollectionName()` returns the right collection on the coordinating node.
- Resolution cost: one cluster-state lookup + one slice/replica scan per
  distinct shard URL per merge. Negligible, but a reviewer who wants it lazier
  could resolve only when a tie actually occurs.
- If the replica URL doesn't match any known replica (stale state, custom
  `ShardHandler`), `shardName` is null and behavior falls back to the old
  tie-break — no crash, just the old non-determinism for that request.
- hossman's caveat stands: this resolves at merge time via cluster state
  rather than plumbing the name through the request layer. That's the
  minimal-touch option; a deeper refactor could thread it through
  `ShardRequest` instead.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).

## What the reviewer should improve

- Confirm the collection-name lookup works for all distributed entry points
  that reach `mergeIds` (aliases? collections API sub-requests?).
- Write the two tests sketched above; prefer `SolrTestCase` style (no J4).
- Run spotless/tidy — formatting was done by hand, not verified.
- Remove this file before opening the upstream PR.
