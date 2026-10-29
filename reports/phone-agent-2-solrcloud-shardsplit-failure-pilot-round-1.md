# Shard-split recovery with a non-ready sibling: unconfirmed candidate (round 1)

**Status: unconfirmed state-transition candidate; no defect verdict.** This report is for claim `claims/phone-agent-2-solrcloud-shardsplit-failure-pilot-round-1.md`. Source snapshot: Apache Solr `74197140a666d4f0396b68484c2bf9df7e79221e`.

## What the deterministic probe shows

The model is: parent `shard1` is `ACTIVE`; child `shard1_0` is `RECOVERY` with one `DOWN` replica that receives an `ACTIVE` replica update; same-parent child `shard1_1` is still `CONSTRUCTION`. The expected safety condition is that the parent remains active until every split child is ready.

The new state-machine probe invokes production `ReplicaMutator.updateState`. Its assertion fails: expected parent state `ACTIVE`, observed `INACTIVE`, while the fellow child remains `CONSTRUCTION`. The JUnit XML reports one test, one assertion failure, zero errors; the failure is `the parent must remain routable until every split child is ready expected same:<active> was not:<inactive>`. The test source is [the state-machine probe](phone-agent-2-solrcloud-shardsplit-failure-pilot-round-1-state-probe.java); local XML: `/workspace/solr/solr/core/build/test-results/test/TEST-org.apache.solr.cloud.overseer.ReplicaMutatorShardSplitTest.xml`.

This is **not** the required SolrCloud integration test: it is a `SolrTestCase` with a synthetic `DocCollection` and mocked `SolrCloudManager`, with no ZooKeeper-backed cluster. It proves the mutator’s behavior for this input state, not that the full split/delete interleaving occurs end to end.

## Code path and reachability evidence

In [`ReplicaMutator`](https://github.com/apache/solr/blob/74197140a666d4f0396b68484c2bf9df7e79221e/solr/core/src/java/org/apache/solr/cloud/overseer/ReplicaMutator.java#L528-L616), fellow slices are considered only when their state is `RECOVERY` (lines 528–559). If the current child’s replicas are active, the code then prepares the parent-to-`INACTIVE` and child-to-`ACTIVE` update (lines 560–616); a fellow in `CONSTRUCTION` is not part of that update.

This state appears plausible in the real protocol. [`SplitShardCmd`](https://github.com/apache/solr/blob/74197140a666d4f0396b68484c2bf9df7e79221e/solr/core/src/java/org/apache/solr/cloud/api/collections/SplitShardCmd.java#L740-L794) puts children into `RECOVERY` before creating their additional replica cores. [`DeleteShardCmd`](https://github.com/apache/solr/blob/74197140a666d4f0396b68484c2bf9df7e79221e/solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteShardCmd.java#L98-L125) changes a `RECOVERY` child to `CONSTRUCTION` before deleting its cores. `SPLITSHARD` and `DELETESHARD` both use shard-level locks ([`CollectionParams`](https://github.com/apache/solr/blob/74197140a666d4f0396b68484c2bf9df7e79221e/solr/solrj/src/java/org/apache/solr/common/params/CollectionParams.java#L94-L95)); the lock factory takes a write lock on the target shard and a read lock on its collection ([`CollectionApiLockFactory`](https://github.com/apache/solr/blob/74197140a666d4f0396b68484c2bf9df7e79221e/solr/core/src/java/org/apache/solr/cloud/api/collections/CollectionApiLockFactory.java#L44-L53)). That permits distinct shard targets to proceed independently, but this run did not reproduce their overlap in a live cluster.

## Proof boundary and next step

The existing `TestInjection.splitLatch` is reached from `ReplicaMutator` only after its all-active decision, so it cannot stage the needed pre-activation interleaving. A full reproduction would need a deterministic SolrCloud test that pauses after child states become `RECOVERY` but before additional replica creation, deletes one child, then observes the other child’s activation and parent state. I did not add new test hooks or claim a defect without that run.

One related cleanup edge is **not assessed as a separate finding**: [`buildCleanupShardStateUpdates`](https://github.com/apache/solr/blob/74197140a666d4f0396b68484c2bf9df7e79221e/solr/core/src/java/org/apache/solr/cloud/api/collections/SplitShardCmd.java#L1012-L1042) dereferences each named child while the parent is inactive (lines 1014–1026); a child already removed by deletion could expose a null-safety issue, but this was not tested. The history for this rollback path is [`0cc328310f8f4162572ea5bd5256cea29543e261`](https://github.com/apache/solr/commit/0cc328310f8f4162572ea5bd5256cea29543e261), “Restore parent shard state on failed split.”

The previously excluded `ShardSplitTest.testSplitAfterFailedSplit` and `offlineSlices` leads remain excluded; this work does not reclassify either. No production code changed, no existing test was weakened, and no Jira/GitHub comment or PR was opened. Root inode free space after the probe was 131,116 of 837,408, above the 15% floor; no further build is planned.
