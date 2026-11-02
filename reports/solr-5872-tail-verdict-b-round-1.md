# SOLR-5872 tail verdict, batch B claim 2: CORRECTION, do not close yet (partial supersession only)

- Assignment: `assignments/overseer-tail-verdicts-b-round-1.md`, Claim 2.
- Claim: `claims/solr-5872-tail-verdict-b-phone-agent.md`.
- Ticket: SOLR-5872, "Eliminate overseer queue", Open, assignee noble,
  last updated 2017-07-29. Its proposal: the queue's raisons d'etre
  are batching state updates and avoiding races/ordering problems; as
  individual collection states move out of the main clusterstate.json,
  batching stops being useful, and races can be solved by ZK
  compare-and-set; so every clusterstate operation should run as
  read-fresh-state, construct-new-state, CAS, retry on failure,
  limited to external collections.
- Census premise to check: the stated path, per-collection state out
  of clusterstate.json, is what PRS delivered (SOLR-15052).
- Deliverable type: correction report, not a closure draft. The
  premise is true for one slice and false for the ticket's title ask.
  No Jira post, no close.

## Verdict

Do not close SOLR-5872 yet. The supersession is partial, in a way
that matters:

1. What PRS delivered (SOLR-15052, Resolved 2021): per-replica state
   znodes nested under the collection's state.json, so replica state
   updates no longer go through the Overseer queue. The code says so
   directly: in the publish path, for a per-replica-state collection,
   the node writes the PRS entries itself, with the comment "as
   overseer does not and should not handle those entries"
   (`ZkController.java:1962-1969`, `flipState` + `persist`). That is
   the ticket's stated path, delivered one level finer than
   per-collection: per-replica instead of per-collection.
2. What PRS did NOT deliver: the elimination of the Overseer queue.
   Collection and configset operations still go through the
   OverseerTaskQueue in default mode (`/overseer/collection-queue-work`),
   and the CAS-loop proposal for all operations was never
   implemented. No code path performs CAS-loop clusterstate operations
   of the kind 5872 proposed.
3. The PRS slice is opt-in, not universal. New collections default to
   non-PRS: `CreateCollectionCmd.java:117-118` reads the
   `solr.cloud.prs.enabled` property with default `false`. So the
   queue-elimination PRS provides covers only PRS collections, and
   only for replica state updates, not for collection operations.
   For a non-PRS collection in default mode, the 2014 picture is
   essentially unchanged: state updates and collection operations
   still flow through the Overseer queue.

## Agreement with the batch A verdict on SOLR-6525 (explicitly checked, as required)

The batch A verdict on SOLR-6525 (same run) said 5872 stays Open as
the design ticket, its CAS proposal was never implemented as proposed,
and the queue was reformed rather than eliminated. This unit AGREES:
no disagreement on shared evidence. The PRS finding refines rather
than contradicts that verdict: the queue was reformed in part by
removing replica state updates from it for PRS collections, but the
queue itself, and the CAS design, remain as 6525 described them.

## What would close this ticket

- The Overseer queue actually going away for the cases 5872 named:
  either SOLR-17293 landing decentralized processing as the default
  (which removes the central queue for all operations, not just
  replica state updates), or a CAS-based replacement for the
  remaining queue traffic; or
- A maintainer ruling that the PRS slice plus the distributed mode
  answer the ticket despite the default mode's queue, stated as a
  scope decision, not as "the queue is gone".

## Sources checked

- Jira SOLR-5872 (full description and 2014-2017 thread), SOLR-15052
  (Resolved 2021, PRS design description), SOLR-17293 (Open umbrella),
  read live.
- `solr/core/src/java/org/apache/solr/cloud/ZkController.java:1962-1969`
  (PRS direct write, bypassing the Overseer),
  `:383` (overseerEnabled default true).
- `solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java:117-118`
  (PRS default false).
- `solr/core/src/java/org/apache/solr/cloud/Overseer.java:961`
  (collection queue still exists).
- Limit: code read against the local main checkout at `e55d2e7bcb`
  (2026-09-29), not a fresh upstream fetch.
