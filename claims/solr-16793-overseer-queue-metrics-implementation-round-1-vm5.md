# Claim: SOLR-16793 Overseer queue length metrics, implementation phase, round 1

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: DONE; phase 1 (implementation). Deliverable: `reports/solr-16793-implementation-round-1.md`, head `747afc8153f703b778c5ed4848b36bc611d0e5d5` on `solr-16793-submit`. Phase 2 (gate) is a separate claim.
- Job or assignment: `assignments/solr-16793-overseer-queue-metrics-round-1.md`.
- Slice: phase 1, items 1 to 5 of the assignment.
- Capability tags used: implementation, records.
- Started: 2026-11-01T05:14:12Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-16793-impl-round-1@origin/main 498a09aea97aac86c2cc7ac114113b946d249942.
- Links: Ticket SOLR-16793 (Minor, Open). Category: Solr core, SolrCloud. Siblings: SOLR-13570 and SOLR-16065 two-phase Overseer assignments on this board (different surfaces). Supersedes: none.
- Census: `~/workspace/tools/solr-work.sh` is not on this host, so the census was done by hand. No `solr-16793*` branch on the fork, no draft or PR, and no other record for SOLR-16793 on `coordination`, `coordination-ui`, or `archive/coordination-2026-10-23` beyond the assignment and the OPEN-WORK line.
- Decision rung so far: rung 1. Code read at `origin/main` 498a09aea9: `Overseer.start` creates one `Stats` and passes it to both `ClusterStateUpdater` (state-update queue) and `OverseerCollectionConfigSetProcessor`, which builds the collection queue with `overseer.getCollectionQueue(client, stats)`. Both queues call `setQueueLength` on that instance. Mechanism confirmed in the code; fix to follow.

Heartbeat: 2026-11-01T05:49:30Z

Result: 2026-11-01T05:49:30Z: final tree `747afc8153` passes the focused test (1 test, 0 failures); base fails `expected:<1> but was:<3>`; core tidy clean. Receipt-level detail in the report.

DONE: 2026-11-01T05:49:30Z. Deliverables: `reports/solr-16793-implementation-round-1.md`, branch `solr-16793-submit` at `747afc8153`.
