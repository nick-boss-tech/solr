# Claim: solr-16013-overseer-shutdown-order-round-1, phase 1 implementation (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/solr-16013-overseer-shutdown-order-round-1.md` (carried to this board from `coordination` at 3b53a1c638). Phase 1 only; the gate is a separate claim after the phase 1 report lands.
- Slice: phase 1 as written: census, confirm the shutdown ordering at main, implement or record a verdict, tests or a stated proof limit, report. Push to fork branch `solr-16013-submit` only if a change is needed.
- Capability tags used: implementation, records.
- Started: 2026-11-01T05:19:16Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: solr-16013-overseer-shutdown-order-round-1-phase1@main-555e1bf239.
- Census: prior work on the same ticket exists on the archived board and on the fork. The fork branch `solr-16013-submit` is at `37b01337eb` (ZkController close sequencing, plus OverseerCloseOrderingTest). Its gate record is `receipts/SOLR-16013.md` on `coordination` (GREEN at 37b01337eb, fixture route approved by owner 2026-10-13). The shared Solr checkout is at main `555e1bf239`; the census tool named in the assignment (`solr-work.sh`) is not on this host.
- Ordering at main, read by rung 1: `solr/core/src/java/org/apache/solr/cloud/ZkController.java` `close()` submits the overseer elector context close and the overseer close as two independent tasks on `closeThreadPool`, and the session-ending `zkClient.close()` runs on the calling thread without waiting for them.
- Links: Ticket or parent: SOLR-16013 (https://issues.apache.org/jira/browse/SOLR-16013). Category: implementation of an existing ticket. Siblings: `pool/` records for SOLR-16013 on the archived board. Supersedes: none.

Heartbeat: 2026-11-01T05:19:16Z
