# Claim: SOLR-13705 premise run (round 1, tranche 1, job 13)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-1.md. Job: gates/SOLR-13705-premise.md.
Capability tags: premise-run. Staffing: 1.
Started: 2026-10-11T01:51:14Z (UTC).

Heartbeat: 2026-10-11T01:51:14Z.

Heartbeat: 2026-10-11T02:07:48Z.

Result 2026-10-11T02:07:48Z: premise HOLDS on the base run (tests=2 failures=1, testLazilyInitializedSingletonIsVolatile fails at SSLConfigurationsFactoryTest.java:36) and head run passes (tests=2 failures=0). Receipt receipts/SOLR-13705.md.
DONE: 2026-10-11T02:07:48Z (UTC), outcome PREMISE HELD.

## Full-gate claim (shared backlog)

Claimant host: vm3. Assignment: assignments/pool-shared-gate-backlog-buffer.md. Job: gates/SOLR-13705.md.
Capability tags: gate. Staffing: 1.
Branch: `solr-13705-submit` at `5f141fb2af381801f58139d4f5739ff0354a244f` (live tip verified 2026-10-11T07:28:08Z).
Merge-base: `9b3a84b1c460981eab09d8ffaef776acc4a184f8`.
Started: 2026-10-11T07:28:08Z (UTC).
Heartbeat: 2026-10-11T07:28:08Z (UTC).
Status: CLAIMED. The prior premise run is complete and holds; packaging and full-gate validation remain.
