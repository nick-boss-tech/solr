# Report: SOLR-18602 alias opt-out control test, round 1

Status: IMPLEMENTED, PROOF PENDING.

## Result

Added `deleteReplicaByCountDoesNotFollowAliasesWhenDisabled` to `DeleteReplicaTest.java` on the separate Nick fork branch `solr-18602-pr5259-alias-optout-test`, based on Amrit Sarkar's exact PR #5259 head `ec8e706a41a0213dd924b286b1f5ebfc476f7cd0`. The pushed implementation head is `b92dd3d7bdae76fd9fb2ac00adc26fcbcb3ba0a7`; the fork ref was read back separately and matched. The public PR remains open at its original head; its branch was not changed.

The test creates a one-shard, three-replica collection and a simple alias, sends a count-based shard deletion through that alias with `followAliases=false`, expects `BAD_REQUEST` naming the unresolved alias, and checks the collection remains at one shard and three replicas. The production request contract is unchanged; this is a coverage control for the opt-out branch.

## Control-flow basis

At the exact PR parent, `solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteReplicaCmd.java:123-129` selects `resolveSimpleAlias(extCollectionName)` only when `FOLLOW_ALIASES` is true. With the default false value, it passes the external alias name through to `getCollection(collectionName)`. The cluster-state lookup rejects that unknown collection with `BAD_REQUEST` before shard selection and replica planning. The existing `deleteReplicaByCountFollowsAliases` test covers the true branch, including a named shard and all shards, but does not pin this false-branch behavior.

The new method is in `solr/core/src/test/org/apache/solr/cloud/DeleteReplicaTest.java:244-267`. A discriminating mutation for the new assertion is to make the count path resolve the alias unconditionally. On that synthetic tree, this request succeeds and deletes a replica, so the unchanged `expectThrows(SolrException.class, ...)` assertion must fail. The requested Linux gate records this as mutation proof for this assertion only; it does not claim the whole class is mutation-proven.

## Scope and verification

- Outbound change: only `solr/core/src/test/org/apache/solr/cloud/DeleteReplicaTest.java`; no production change and no changelog fragment.
- `git diff --check` passed. The exact scoped diff was reviewed.
- No Gradle task or test ran on Windows. Runtime proof is pending the Linux gate in `gates/pr-5259-solr-18602-alias-optout-control-test-round-1.md`.
- The adjacent SOLR-18589 / PR #5255 alias-lock follow-up is a separate change. This test does not integrate or assert lock behavior.
- Claim: `claims/pr-5259-solr-18602-alias-optout-control-test-windows-round-1.md`.
