# Claim: flaky root-cause reading, GCSInstallShardTest on the SOLR-18506 branch

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-flaky-rca-gcs-install-shard.md`
- Date: 2026-10-11
- Slices: one, one subagent. Reading only: the code paths of `GCSInstallShardTest` and its harness at current main and at the branch head, the ranked hypotheses, and the three-run spec for the Linux lane.
- Split: one subagent, in the wave after the draft fidelity claims.
- Deliverable: `reports/flaky-rca-gcs-install-shard.md` (analysis, ranked hypotheses, run spec). Part report `reports/flaky-rca-gcs-install-shard-s1.md`.
- Scope: read-only. No builds, no test runs, no runs on this host. The three runs stay on the Linux hosts and are only specified here.
- Heartbeat: 2026-10-11T02:36Z (claim taken).
- Status: ACTIVE, queued behind the first wave.
