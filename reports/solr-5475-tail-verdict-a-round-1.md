# SOLR-5475 tail verdict, batch A claim 1: CORRECTION, do not close yet

- Assignment: `assignments/overseer-tail-verdicts-a-round-1.md`, Claim 1.
- Claim: `claims/solr-5475-tail-verdict-a-phone-agent.md`.
- Ticket: SOLR-5475, "Scale Overseer to work on multiple queues and threads",
  Open, assignee Shalin Shekhar Mangar, last updated 2017-07-29, no comments.
- Ticket ask, verbatim in substance: separate each collection's events to a
  different queue, one thread per queue, via a cluster-wide `collectionsPerQ`
  property; a collection is assigned a queue-id at creation, stored in the
  collection's state; new queues are created when all queues reach the max.
- Deliverable type: correction report, not a closure draft. The batch rules
  require this when the census premise fails, and it fails in the present
  tense for the reason below. No Jira post, no close.

## Verdict

Do not close SOLR-5475 as retired by Overseer removal yet. Two facts block
that closure, both verified against the ticket and the current code:

1. The ticket's mechanism was never built. There is no `collectionsPerQ`
   property, no queue-id in collection state, and no per-collection queue
   anywhere in `solr/core/src/java/org/apache/solr/cloud/` on the main
   checkout read for this unit (apache main at `e55d2e7bcb`, 2026-09-29).
   The Overseer still has one collection queue,
   `/overseer/collection-queue-work` (`Overseer.java:961`, via
   `getCollectionQueue`), plus the separate configset queue.
2. Decentralized processing is not yet the default. `ZkController.java:383`
   reads the `OVERSEER_ENABLED` cluster property with default `true`
   (`solr.cloud.overseer.enabled`, default `true`); with it enabled the
   Overseer still processes all cluster commands and state updates. The
   decentralized path (any/all nodes via `DistributedClusterStateUpdater`
   and `DistributedCollectionConfigSetCommandRunner`) is opt-in. The
   umbrella for making it the default, SOLR-17293, is still Open, and its
   own description says the decentralized paths "have not experienced
   real-world usage to our knowledge" and carry "some scalability
   concerns". A closure citing SOLR-17293 today would cite an open
   umbrella for an opt-in mode, not a landed default.

## What already exists, in a different shape

The scaling goal behind the ask is partly addressed without per-collection
queues: `OverseerTaskProcessor` dispatches queue heads to a shared pool,
`ExecutorUtil.MDCAwareThreadPoolExecutor` with 5 core and up to
`MAX_PARALLEL_TASKS = 100` threads (`OverseerTaskProcessor.java:69,212`),
so tasks from different collections can run concurrently from the single
queue. That is parallel execution, not the ticket's per-collection queues
with per-queue threads, and it does not change the verdict above: the
ticket's mechanism is unbuilt, and the single central queue it wanted to
replace still exists under the default configuration.

## Condition for a future closure

SOLR-5475 becomes closure-ready as "retired by removal" only when
SOLR-17293 lands decentralized processing as the default (or the Overseer
is removed): with no central Overseer queue, there is nothing left to
split per collection. Until then the honest record is: mechanism
unbuilt, goal partly met by parallel dispatch, retirement pending
SOLR-17293. If a maintainer prefers to close it sooner, the closure
text must say the per-collection-queue mechanism was never implemented
and the ask is superseded by the removal direction, not that the
mechanism exists or that decentralized processing is already default.

## Sources checked

- Jira SOLR-5475 (summary, status Open, description, zero comments)
  and SOLR-17293 (Open, umbrella description quoted above), read live
  during this unit.
- `solr/core/src/java/org/apache/solr/cloud/ZkController.java:382-402`
  (overseerEnabled switch, default true, distributed replacements).
- `solr/core/src/java/org/apache/solr/cloud/Overseer.java:942-961`
  (single collection queue path).
- `solr/core/src/java/org/apache/solr/cloud/OverseerTaskProcessor.java:69,212`
  (MAX_PARALLEL_TASKS, thread pool).
- Grep for `collectionsPerQ` and per-collection queue concepts across
  `solr/core/src/java/org/apache/solr/cloud/`: no hits.
- Limit: code read against the local main checkout at `e55d2e7bcb`
  (2026-09-29), not a fresh upstream fetch; the cited constructs are
  long-standing, but a re-read at PR time should confirm nothing moved.
