# Verdict: SOLR-15997, sporadic ZK BadVersionException when perReplicaState flips to true

Verdict: STILL LIVE on main, promote to implementation, not closure.
The pool record left the mechanism unconfirmed; the code read below
confirms one, fully inside the Overseer write path, with no outside
writer needed. A pending batched update for the same collection plus
a perReplicaState flip lands a version-checked write with a stale
version, and on the Overseer path that BadVersionException is terminal
for the writer instance. The fix is narrow: rebase the flip command
to the flushed version before writing it. Nothing closes without a
maintainer's or Nick's go.

Ticket: https://issues.apache.org/jira/browse/SOLR-15997 (2022,
reporter and assignee Noble Paul, Open, Major, last updated
2022-05-12, no fix version, no links, no comments). Steps from the
report: create a few hundred collections, restart all nodes, set
perReplicaState=true with a MODIFY collection request; occasionally
a BadVersionException appears in the Overseer. The report carries no
stack trace and no log.

## Code read (current main as checked)

Checked against the local main checkout at e55d2e7bcb (2026-09-29).
The solr/ paths are unmodified there; the checkout is shallow (363
commits back to June 2026), so no whole-history claim is made here.
No source file on main references SOLR-15997.

The route, on Overseer-enabled clusters:

- CollApiCmds.ModifyCollectionCmd sends a MODIFYCOLLECTION message
  to the distributed updater only when distributed state update is
  on, else offers it to the Overseer queue. ZkController builds the
  distributed updater with !overseerEnabled, so Overseer-enabled
  clusters (the default shape) take the queue path, and the
  distributed updater replaces the Overseer only when the Overseer
  is disabled.
- The Overseer main loop processes queue items with
  enableBatching=true (the fallback queue path uses false).
  STATE_UPDATE_BATCH_SIZE defaults to 10000, so pending batched
  updates are a normal state, not an edge case.
- CollectionMutator.modifyCollection takes coll from the Overseer's
  in-memory ClusterState. For a perReplicaState flip it fetches the
  per-replica states, calls PerReplicaStatesOps.enable (or
  disable), and builds the new DocCollection with
  coll.getZNodeVersion(), returning a ZkWriteCommand carrying the
  replica ops with persistJsonState=true.

The gap the pool record left open, closed here: enable() writes
nothing to ZooKeeper itself. It only computes operations in memory;
ZkStateWriter.writePendingUpdates applies them later with
cmd.ops.persist, creating or deleting children of the state.json
node (DocCollection.getZNode() is
/collections/<name>/state.json). Children changes bump that node's
cversion, not the data version that setData checks, so the flip's
own per-replica writes cannot cause its own BadVersionException.

The race, in ZkStateWriter.enqueueUpdate:

- A single-command message whose new collection isPerReplicaState()
  is written at once, not batched, because per-replica changes gain
  nothing from batching. Before writing, if the pending updates map
  already holds a command for the same collection, it flushes that
  pending command first, with a comment saying this should not
  happen.
- The flushed command's return value, which carries the new version
  from the setData stat, is discarded. The flip command is then
  written with the version it was built with.
- The sequence: (1) a state update for collection X, still
  perReplicaState=false and therefore batchable, is enqueued
  pending while ZooKeeper sits at version V; the Overseer's
  in-memory copy still says V. (2) MODIFY X perReplicaState=true
  arrives; the mutator builds the flip command with version V.
  (3) enqueueUpdate sees the pending X entry and flushes it, so
  ZooKeeper moves to V+1. (4) writeUpdate for the flip calls
  setData with version V and gets BadVersionException. (5)
  ZkStateWriter sets invalidState=true: the error is tragic and
  the writer instance is unusable.
- The guard's assumption fails exactly on the flip: the collection
  was not per-replica-state when the pending update was enqueued,
  and is per-replica-state in the flip command. That is the one
  shape where a pending same-collection entry meets an immediate
  write.

Why the ticket's shape fits: after a full restart with a few
hundred collections, the Overseer queue carries a burst of state
updates, so a pending same-collection update at MODIFY time is
likely. When the queue is quiet the flip finds no pending entry
and succeeds, so the failure is sporadic, as reported.

Contrast, stated plainly: on Overseer-disabled clusters,
DistributedClusterStateUpdater retries a BadVersionException on
the state.json compare-and-swap with exponential backoff, so the
same stale version self-heals there. The Overseer path has no
retry; BadVersion is terminal for the writer instance.

Reachability, stated plainly: this mechanism is established by
code reading, not by a run. No multi-node reproduction was run
here; the ticket's own repro needs hundreds of collections plus a
full restart, and the report carries no log to compare against.
What the reading proves is that the sequence needs no outside
writer and no timing beyond one pending update: it is a property
of the write path itself.

## Existing pool record (agrees, and this verdict closes its gap)

Fork branch solr-15997-draft-open at 84c59b4692 (pool record,
"keep-open, mechanism not confirmed", vm2, 2026-10-19) read the
version-checked write and left exactly one open question: whether
PerReplicaStatesOps.enable changes the collection znode's version
before the state write. This verdict answers it: enable() changes
nothing in ZooKeeper, and the stale version comes from the
enqueueUpdate sequence above, not from the flip's own writes.
No PR, no board receipt, and no draft exist for this ticket
beyond that pool record. No commit naming SOLR-15997 appears in
the local shallow checkout, and the pool record found none on
origin/main at 498a09aea97.

## Verdict justification

STILL LIVE, promote to implementation, not closure: the reported
exception has a confirmed mechanism on the default Overseer path,
narrow enough to fix and test, and no fix commit or reproduction
run closes the ticket. The narrow implementation item is to
rebase the incoming command to the flushed version after the
pending update is flushed in the immediate-write branch (or
recompute the flip against the post-flush state), with a test
that enqueues a same-collection update, then flips
perReplicaState, and asserts no BadVersion and the flip lands.
A close verdict would need either a fix commit naming the ticket
or a run showing the sequence cannot occur; neither exists.
