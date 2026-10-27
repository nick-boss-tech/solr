# Claim: rc3-tlogreplica-peersync-probe-round-1

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: CLAIMED.
- Job or assignment: `assignments/rc3-tlogreplica-peersync-probe-round-1.md`, whole round (one probe).
- Slice: scratch change at `RecoveryStrategy.java` line 572 (`firstTime = true`) on a scratch worktree at apache/main 13c693d805f; focused testKillLeader and testRecovery at seed 9D6BB87C4F145405. Control is the round-1 main run (`/workspace/smoke-rc3/tlog-repro/main-run1.log`) at the same seed; no second control.
- Capability tags used: premise-run. Scratch only; no branch pushed, no PR, no post.
- Started: 2026-10-25T01:05Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: rc3-tlogreplica-peersync-probe-round-1@vm5.
- Links: `reports/rc3-tlogreplica-rootcause-round-1.md`, SOLR-18586. Supersedes: none.
- Decision ladder: rung 2, a probe that separates the explanations.

Heartbeat: 2026-10-25T01:05Z


Released as stale by vm5 (overnight tick, 2026-10-27). Rung 1: last heartbeat 2026-10-25T01:05Z, hourly heartbeat required; named assignment assignments/rc3-tlogreplica-peersync-probe-round-1.md Status: OPEN. The claim is closed for intake. The assignment status is unchanged.
