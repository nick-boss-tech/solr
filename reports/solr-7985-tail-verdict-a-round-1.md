# SOLR-7985 tail verdict, batch A claim 7: SPLIT VERDICT, do not close as a unit

- Assignment: `assignments/overseer-tail-verdicts-a-round-1.md`, Claim 7.
- Claim: `claims/solr-7985-tail-verdict-a-phone-agent.md`.
- Ticket: SOLR-7985, "Solr Overseer Task Management Improvements", Open,
  unassigned, last updated 2016-05-09, zero comments. It captures four
  cleanup ideas from SOLR-7789 (Closed). The assignment requires working
  through the sub-asks one by one instead of forcing a single verdict,
  and that is what this report does.
- Deliverable type: split report, not a closure draft. Only one sub-ask
  is answered; the rest are open cleanups, not retirements. No Jira
  post, no close.

## Sub-ask 1: simplify or remove OverseerMessageSelector: ANSWERED (overtaken)

The standalone `OverseerMessageSelector` interface from 2015 is gone:
no such type exists anywhere in `solr/core/src/java/` today. Selection
survives only as the nested `OverseerTaskProcessor.OverseerMessageHandlerSelector`
with a single anonymous implementation in
`OverseerCollectionConfigSetProcessor` (dispatch by the `configsets:`
operation prefix, defaulting to the collection handler). The design
around it changed more than the ask imagined (one unified
collection-plus-configset processor thread instead of separate queue
processors), but the ask's substance, one minimal selector instead of
a standalone interface hierarchy, is met. No action needed.

## Sub-ask 2: per-queue processor state object: OPEN (not implemented)

`OverseerTaskProcessor`'s constructor still takes three separate
`DistributedMap` parameters (`runningMap`, `completedMap`,
`failureMap`; lines 139-141), backed by the single shared ZK set at
`/overseer/collection-map-running`, `/overseer/collection-map-completed`,
`/overseer/collection-map-failure` (`Overseer.java:900-924`). No
single state object combines them, and `ZkController` exposes no
per-queue state objects. The original motivation weakened (the unified
processor means there is no longer a pair of queue processors sharing
one map set in the 2015 shape), but the cleanup itself was never done.
Value today is low: it is a constructor-shape cleanup, not a behavior
change. Candidate for wont-do rather than implementation, but that is
a maintainer call, not something this verdict can close.

## Sub-ask 3: JSON-based insertion interface with insertion-time transformations: PARTLY DONE

Half of it landed in a different form: `DistributedQueue` now has an
`offer(MapWriter)` default that serializes with `Utils.toJSON`
(`solr/solrj-zookeeper/.../DistributedQueue.java:43-44`), so callers
can insert structured messages without hand-building JSON. But the
interface itself is still bytes[]-based (`offer(byte[])` is the real
method), and the motivating transformation was never built:
configset callers prefix the operation with `configsets:` themselves
(`ConfigSetAPIBase.java:120`), and the handler strips it
(`OverseerConfigSetMessageHandler.java:76`); no queue inserts a prefix
on the caller's behalf. So: structured insertion exists, insertion-time
transformation does not. OPEN in its second half.

## Sub-ask 4: split insertion-only from insertion/removal: NOT DONE (behaviorally moot)

`DistributedQueue` still exposes the full `java.util.Queue`-like
surface (`peek`, `poll`, `remove`, `take`, `offer`) to every holder;
there is no insertion-only type. In practice the split already holds
behaviorally: no handler code outside `OverseerTaskProcessor` calls
`remove` on a queue (verified by grep over `solr/core/src/java/`),
handlers only offer. So the risk the ask worried about (a handler
removing queue entries) does not occur, but nothing enforces it. OPEN
as a type split, moot as a behavior concern.

## Removal does not retire this ticket

These are code-cleanliness asks about `OverseerTaskProcessor` and the
queue interfaces, not behavior asks. Removing the Overseer would
delete the code rather than answer the asks, and removal has not
landed anyway (SOLR-17293 still Open, decentralized processing
opt-in). A closure draft citing SOLR-17293 would misdescribe all
four sub-asks. The correct disposition is per sub-ask: 1 answered,
2 and 4 open cleanups (4 behaviorally moot), 3 half done. If a
maintainer wants the ticket closed, the closure text must list this
split and close on "answered in part, remainder wont-do", not on
removal.

## Sources checked

- Jira SOLR-7985 (four sub-asks, zero comments), SOLR-7789 (Closed),
  SOLR-17293 (Open), read live.
- `OverseerTaskProcessor.java:132-151` (constructor, three maps),
  `:736` (nested selector interface).
- `OverseerCollectionConfigSetProcessor.java:100-135` (single
  implementation, prefix dispatch).
- `Overseer.java:752-764` (single unified processor thread),
  `:900-924` (shared map set).
- `DistributedQueue.java:41-44` (bytes[] offer, MapWriter JSON default).
- `ConfigSetAPIBase.java:120`, `OverseerConfigSetMessageHandler.java:68-76`
  (caller-side prefixing, handler-side stripping).
- Grep for queue `remove` callers outside the processor: none.
- Limit: code read against the local main checkout at `e55d2e7bcb`
  (2026-09-29), not a fresh upstream fetch.
