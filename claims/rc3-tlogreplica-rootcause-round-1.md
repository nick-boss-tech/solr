# Claim: rc3-tlogreplica-rootcause-round-1 (unit 1)

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: CLAIMED.
- Job or assignment: `assignments/rc3-repeating-failures-rootcause-round-1.md`, unit 1 (TestTlogReplica: testKillLeader, testRecovery; testOnlyLeaderIndexes as observed).
- Slice: reproduce at the RC3 tip (749712b719) and at apache/main with focused runs of `org.apache.solr.cloud.TestTlogReplica`, then root-cause if it reproduces. Deliverable: `reports/rc3-tlogreplica-rootcause-round-1.md`.
- Capability tags used: premise-run, sweep, implementation (the implementation tag only if a fix branch follows). Runs under the vm5 Gradle lock, one at a time.
- Started: 2026-10-24T23:09Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: rc3-tlogreplica-rootcause-round-1@unit1@vm5.
- Links: RC3 smoke loop report `reports/rc3-smoke-loop-round-1.md`. Units 2 and 3 are not claimed here. Supersedes: none.
- Decision ladder: rung 1 for the code and history; rung 2 for the repro runs; choices recorded in the report.

Heartbeat: 2026-10-24T23:09Z


Released as stale by vm5 (overnight tick, 2026-10-27). Rung 1: last heartbeat 2026-10-24T23:09Z, hourly heartbeat required; named assignment assignments/rc3-repeating-failures-rootcause-round-1.md Status: OPEN. The claim is closed for intake. The assignment status is unchanged.
