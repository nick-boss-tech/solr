# Claim: SOLR-16398-core-commands gate (vm6)

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: DONE (see the DONE line below).
- Job or assignment: `gates/SOLR-16398-core-commands.md` (head `d4a7840649c86f970381f8e647291635016923ac`, fork tip verified by ls-remote 2026-10-29).
- Slice: the whole gate job, steps 1 to 5 as listed; plus a route check of the eight converted core endpoints, because the listed steps (compile and `check -x test`) do not run tests, and the branch deletes `V2CoreAPIMappingTest` and `V2NodeAPIMappingTest`.
- Capability tags used: gate.
- Started: 2026-10-29T15:05:00Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-16398-core-commands-gate@d4a7840649c8.
- Links: Ticket or parent: SOLR-16398. Category: see `pool/work-map.md`. Siblings: `gates/SOLR-16398-command-jaxrs.md` (DONE at `b135a03` after round 3). Supersedes: none.
- Registration check (rung 1, code): `CoreAdminHandler.getJerseyResources()` at `d4a7840` lists SplitCore, PrepareCoreRecovery, RequestCoreRecovery, RequestSyncShard, RequestBufferUpdates, RequestApplyCoreUpdates, OverseerOperation and RejoinLeaderElection; `getApis()` returns an empty list. The collection defect of SOLR-16398 round 1 does not repeat here by inspection; the route check decides.
- Constraints: fork push fast-forward only; no JIRA or GitHub post, no PR; commit identity Nick Shanin, no trailers.

Heartbeat: 2026-10-29T15:56:15Z

Result: 2026-10-29T15:56:15Z: gate PASS at cf2bbb0558 (listed steps plus the added route check; mutation shows the route check discriminates). Receipt: `receipts/SOLR-16398-core-commands.md`.

DONE: 2026-10-29T15:56:15Z. Deliverables: `receipts/SOLR-16398-core-commands.md`; `gates/SOLR-16398-core-commands.md` DONE; fork commit `cf2bbb0558` on `solr-16398-core-commands`.
