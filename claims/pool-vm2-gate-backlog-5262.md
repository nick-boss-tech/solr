# Claim: SOLR-5262 premise run (round 1, tranche 1, job 7)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-1.md. Job: gates/SOLR-5262-premise.md.
Capability tags: premise-run. Staffing: 1.
Started: 2026-10-10T23:02:19Z (UTC).

Heartbeat: 2026-10-10T23:02:19Z.

Result 2026-10-10T23:25:52Z: premise run complete. Base: testUlogDirDefaultsToDataDir fails (expected:<data/> but was:<null>); scratch ${solr.core.ulogDir} substitution fails (No system property or default value). Head: TestCoreDescriptorImplicitProperties 3/3 and the probe pass. Receipt receipts/SOLR-5262.md. No gate run.
DONE: 2026-10-10T23:25:52Z (UTC), outcome PREMISE HELD.


## Full-gate claim (shared backlog)

Claimant host: vm3. Assignment: assignments/pool-shared-gate-backlog-buffer.md. Job: gates/SOLR-5262.md.
Capability tags: gate. Staffing: 1.
Branch: `solr-5262-submit` at `ade8b80264ac126c7c4b932388aa6ebb9b200c0e` (live tip verified at claim time).
Merge-base: `cabedd1d968059215188f4e7563fb303241899ed`.
Started: 2026-10-11T07:12:31Z (UTC).
Heartbeat: 2026-10-11T07:12:31Z (UTC).
Status: CLAIMED. The prior premise run is complete and holds. The full gate has not started; packaging and validation remain.
