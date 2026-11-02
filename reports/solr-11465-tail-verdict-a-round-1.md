# SOLR-11465 tail verdict, batch A claim 3: CLOSURE DRAFT (implemented, not removal-pending)

- Assignment: `assignments/overseer-tail-verdicts-a-round-1.md`, Claim 3.
- Claim: `claims/solr-11465-tail-verdict-a-phone-agent.md`.
- Ticket: SOLR-11465, "Overseer should process independent messages in
  parallel", Open, unassigned, last updated 2023-10-03, one 2021 comment
  (no technical content).
- Ticket ask, in full: "Right now, Overseer is processing messages in
  single thread which does not scale well in case of the very large
  cluster (SOLR-10265). Independent messages - messages for different
  collection - should be handled in parallel."
- Deliverable type: closure draft. This is NOT a correction report: the
  census premise (removal retires the ask) is true but secondary. The
  primary ground is stronger: the ask is already implemented in the
  current Overseer path, so the ticket need not wait for removal.
  Draft only; no Jira post, no close, pending a maintainer's or Nick's
  explicit go.

## Verdict

Close as implemented. In the current code, independent Overseer messages
are already handled in parallel, with ordering preserved only where it
matters:

- `OverseerTaskProcessor` no longer processes messages on a single
  thread: its dispatch loop submits each queue head to a thread pool
  (`ExecutorUtil.MDCAwareThreadPoolExecutor`, 5 core threads, up to
  `MAX_PARALLEL_TASKS = 100`) and tracks the futures in `runningTasks`
  without waiting (`OverseerTaskProcessor.java:212,394-396`).
- Independence is exactly the lock granularity. Before dispatch,
  `OverseerCollectionMessageHandler.lockTask` takes an exclusive lock
  keyed on the collection action, task key, shard id, and replica id
  (`OverseerCollectionMessageHandler.java:189-210`); a task whose lock
  cannot be taken goes to `blockedTasks` and is retried in queue order
  (`OverseerTaskProcessor.java:362-367`). So messages for different
  collections take different locks and run concurrently, while messages
  conflicting on the same collection/shard/replica serialize. That is
  the ticket's ask: parallel for independent messages, ordered for
  dependent ones.
- In the overseer-disabled mode the same conclusion holds from the
  other side: cluster commands run distributed on any/all nodes
  (`ZkController.java:390-402`, `DistributedClusterStateUpdater`,
  `DistributedCollectionConfigSetCommandRunner`), with no central
  single-threaded queue at all.

## The SOLR-14927 link, addressed directly

SOLR-11465 references SOLR-14927 ("Remove Overseer", still Open). That
link does NOT mean this ticket must stay open until removal lands:
removal would retire the question only because there would be no
Overseer left to parallelize, but the behavior the ticket asks for
already exists in the Overseer path today, so there is nothing left
for removal to supply here. The removal direction (SOLR-14927 under the
SOLR-17293 umbrella, decentralized processing as default) is noted as
supporting context, not as the closure ground. The closure ground is
implementation, verifiable in the current code paths cited above.

## Suggested closure text (draft, not posted)

> Independent Overseer messages are now handled in parallel:
> `OverseerTaskProcessor` dispatches queue heads to a thread pool (up
> to 100 parallel tasks), with `OverseerCollectionMessageHandler`
> locking per collection action/shard/replica so only conflicting
> messages serialize. The single-threaded processing this ticket
> complained about no longer exists in the Overseer path, and the
> overseer-disabled mode distributes command processing across nodes.
> Closing as implemented; the SOLR-14927 removal direction would only
> moot the question further.

## Sources checked

- Jira SOLR-11465 (summary, status Open, description, Reference link
  to SOLR-14927), SOLR-14927 ("Remove Overseer", Open), SOLR-17293
  (Open umbrella), read live.
- `solr/core/src/java/org/apache/solr/cloud/OverseerTaskProcessor.java:69,212,394-396`
  (pool, parallel submit).
- `solr/core/src/java/org/apache/solr/cloud/api/collections/OverseerCollectionMessageHandler.java:180-210`
  (lockTask granularity and queue-order guarantee).
- `solr/core/src/java/org/apache/solr/cloud/ZkController.java:383-402`
  (overseerEnabled switch, distributed replacements).
- Limit: code read against the local main checkout at `e55d2e7bcb`
  (2026-09-29), not a fresh upstream fetch; the cited constructs are
  long-standing, but a re-read at PR time should confirm nothing moved.
  No test run was made for this records-only unit; the closure rests
  on the code paths above, and a maintainer confirming the pool and
  lock behavior is the remaining check before posting.
