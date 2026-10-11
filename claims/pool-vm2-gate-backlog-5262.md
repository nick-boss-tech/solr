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
Branch: `solr-5262-submit` at packaged head `b32bfc765e1f912cb5f9ef11e5f7fbcd14aabf83` (pushed and verified live).
Merge-base: `cabedd1d968059215188f4e7563fb303241899ed`.
Started: 2026-10-11T07:12:31Z (UTC).
Heartbeat: 2026-10-11T07:12:31Z (UTC).
Status: BLOCKED. Packaging and changelog validation passed. The root tidy task could not start because the Develocity Gradle plugin download was truncated on both attempts; no tests ran. Details and logs: gates/SOLR-5262.md.
BLOCKED: 2026-10-11T07:25:52Z (UTC), environment dependency-download failure after the single permitted retry.
RELEASED: 2026-10-11T14:10:34Z (UTC) by owner direction that no claim remain held. The job is still blocked, not DONE; no active vm3 holder remains.
