# SOLR-10265 tail verdict, batch A claim 2: CORRECTION, do not close yet

- Assignment: `assignments/overseer-tail-verdicts-a-round-1.md`, Claim 2.
- Claim: `claims/solr-10265-tail-verdict-a-phone-agent.md`.
- Ticket: SOLR-10265, "Overseer can become the bottleneck in very large
  clusters", Open, assignee Cao Manh Dat, last updated 2019-06-08.
- Ticket problems, in its own terms: (1) Overseer processing rate: a
  400k-replica cluster generates ~1.6M state-change events, the 2017
  measurement was ~170 events/second single-threaded
  (`OverseerTest#testPerformance`, 20k events in 119s), the rate slows
  as the queue grows, a rolling restart backs the queue up, and a
  comment notes only ~60k entries fit the Overseer queue before
  `jute.maxbuffer` becomes a problem; (2) state changes overwhelming
  ZK: every state change writes a big per-collection `state.json`,
  bloating ZK transaction logs.
- Deliverable type: correction report, not a closure draft. The batch
  rules require this when the census premise fails, and it fails in
  two places below. No Jira post, no close.

## Verdict

Do not close SOLR-10265 as retired by Overseer removal yet. The
bottleneck is mitigated, not retired, and one of its two problems is
not a removal problem at all:

1. Problem 1 is mitigated in shape but not retired under the default
   configuration. Since 2017: SOLR-11443 ("Remove the usage of
   workqueue for Overseer") is Fixed (2017-10-17); the collection
   queue is now dispatched in parallel by `OverseerTaskProcessor`
   (thread pool 5 to `MAX_PARALLEL_TASKS = 100`), which directly
   answers the reporter's 2017-10-06 comment that "Overseer is
   processing all messages in a single thread"; and the
   overseer-disabled mode removes the central queue entirely
   (`ZkController.java:383-402`). But the default is still Overseer
   enabled (`solr.cloud.overseer.enabled`, default `true`), and in
   that mode collection operations still funnel through the single
   ZK queue `/overseer/collection-queue-work`, so the rolling-restart
   queue-backup shape and the queue-capacity concern persist for
   default deployments. The ticket's 400k-core scenario was never
   re-measured: `OverseerTest#testPerformance` still exists with the
   same 20k-event shape, and no re-measurement is cited anywhere in
   the ticket thread.
2. Problem 2 is not retired by removal. Decentralized mode still
   writes per-collection cluster state to ZK
   (`DistributedClusterStateUpdater.doSingleStateUpdate` and the
   state-update paths around it); removing the central queue does
   not shrink `state.json` or reduce ZK write volume per change.
   The thread itself rejected the finer split (Noble Paul, 2017-07-29:
   per-shard is the finest useful split, and frequency, not size, is
   the real problem). Problem 2 needs its own disposition, not a
   removal closure.
3. The removal vehicle is not landed. SOLR-17293 (decentralized
   processing as default) is still Open, its description says the
   decentralized paths "have not experienced real-world usage to
   our knowledge" and carry "some scalability concerns", and
   decentralized processing is opt-in, not default.

## What would make this closure-ready

Either SOLR-17293 lands decentralized processing as the default with
evidence at scale (which would retire problem 1's central-queue
shape), plus a separate disposition for problem 2 (state-write
volume), or a maintainer rules that the mitigations (11443,
parallel dispatch) already answer the ticket despite the default
mode's central queue, and says so on the ticket. A closure draft
written today must not claim the bottleneck is gone: it must name
the mitigations, the still-central default queue, and the
unaddressed state-write volume.

## Related tickets noted, not decided here

- SOLR-11443: Closed, Fixed 2017 (workqueue removal), one of the two
  children the reporter cited 2017-10-11 for a 15-20x estimate.
- SOLR-11465 ("Overseer should process independent messages in
  parallel"): still Open; it is batch A claim 3 itself, so its own
  verdict unit decides it separately and is not prejudged here.

## Sources checked

- Jira SOLR-10265 (full thread: description, 2017 comments including
  the 60k-entry queue note, the single-thread comment, and the
  2017-10-11 children note), SOLR-11443 (Closed, Fixed), SOLR-11465
  (Open), SOLR-17293 (Open, umbrella description), read live.
- `solr/core/src/java/org/apache/solr/cloud/ZkController.java:383-402`
  (overseerEnabled default true, distributed replacements).
- `solr/core/src/java/org/apache/solr/cloud/Overseer.java:961`
  (single collection queue path).
- `solr/core/src/java/org/apache/solr/cloud/OverseerTaskProcessor.java:69,212`
  (MAX_PARALLEL_TASKS, thread pool).
- `solr/core/src/java/org/apache/solr/cloud/DistributedClusterStateUpdater.java`
  (state updates still ZK-backed in decentralized mode).
- `solr/core/src/test/org/apache/solr/cloud/OverseerTest.java:1423`
  (`testPerformance` still present, same 20k-event shape).
- Limit: code read against the local main checkout at `e55d2e7bcb`
  (2026-09-29), not a fresh upstream fetch; the cited constructs are
  long-standing, but a re-read at PR time should confirm nothing moved.
