# SOLR-15386 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-15386 - "Internal DOWNNODE request will mark replicas down even if their host node is now live" (2021). Deferred in audit batch 7, then implemented by explicit user choice ("pick a design choice and justify it").
- Branch: `solr-15386-submit` off `apache/solr` main `e2cdb2d7e8a`

## The bug, as understood
On shutdown `ZkController.preClose` removes the live node and then calls `publishNodeAsDown`, which queues a `DOWNNODE` message for the Overseer (`NodeMutator.downNode`). The Overseer applies it asynchronously. If the node restarts and re-registers as live before the Overseer gets to the message, the stale message marks the restarted node's replicas DOWN, and nothing re-publishes them ACTIVE until something else touches them.

## Design choice and why
The reporter's guard ("skip if the node is live") broke many tests; David Smiley's comments only sketch options (make liveness a function of live_nodes + state.json; re-check before and after `downNode`). I chose a **per-message opt-in guard evaluated by the Overseer**:

- `DOWNNODE` gains an optional boolean `onlyIfNodeNotLive`. `NodeMutator.downNode` returns no commands when it is set and the node is in the live nodes of the cluster state being updated.
- Only the **shutdown** call site (`preClose`, after the live node is removed) sets it.
- Why opt-in rather than always: the other callers legitimately mark a *live* node's replicas down. `registerAllCoresAsDown` runs at startup, before the live node exists, but the Overseer may process it after registration; skipping it would leave stale ACTIVE replicas. `publishAndWaitForDownStates` (used by `TestQueryingOnDownCollection`, `ZkControllerTest`) is called on a live node. Those are most likely what broke the reporter's blanket guard.
- Why Overseer-side, not sender-side: the race is precisely that the sender's view is stale at processing time; the check must happen where the message is applied. The distributed cluster-state-update path (`executeNodeDownStateUpdate`) runs synchronously in the caller, so it has no such delay and is unchanged.
- Rejected: stamping messages with the ZK session/ephemeral id of the live node (exact, but needs a new live-node identity plumbed through the Overseer; much bigger change); re-checking before and after `downNode` (still racy, since the live-nodes view can change right after the check).

## What the branch changes
- `NodeMutator`: constant `ONLY_IF_NODE_NOT_LIVE`; guard at the top of `downNode`.
- `ZkController`: private `publishNodeAsDown(nodeName, onlyIfNodeNotLive)`; the public one-arg method delegates with `false`; `preClose` passes `true`; the flag is put on the queued message.
- Test: new `NodeMutatorTest` (3 tests: normal mark-down; unconditional message ignores live nodes; conditional message skipped when the node is live).

## Guesses to verify first
1. The Overseer's `clusterState.getLiveNodes()` is up to date enough when the message is processed (the live-nodes watcher feeds the Overseer's `ZkStateReader`). If it lags, the guard only narrows the race rather than closing it.
2. `MapWriter.EntryWriter.put(String, boolean)` compiles in the lambda (otherwise use `Boolean.valueOf`).
3. `new NodeMutator(null)` is fine: `SliceMutator.getZkClient` returns null for a non-`SolrClientCloudManager`.
4. On a real node shutdown with no restart, the node is already out of live nodes when the Overseer processes the message, so replicas still go DOWN (covered by existing cloud tests that shut nodes down).
5. PRS collections: `downNode` already skips them; unchanged.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.cloud.overseer.NodeMutatorTest"
```
Fail-before: revert the guard in `NodeMutator.java` only (keep the constant); `testConditionalDownNodeSkippedWhenNodeIsLiveAgain` should fail. A real restart-race integration test was not attempted: it would need to delay the Overseer, which is flaky.

## Not done
No JIRA comment, no PR.
