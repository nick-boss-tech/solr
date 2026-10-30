# Claim: SOLR-17241 wt gate, round 3 (vm5)

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: CLAIMED.
- Job: `gates/SOLR-17241-wt-format-round-3.md`, whole job. Supersedes round 2 (FAIL at `7a5a998ee9`).
- Tested head: `86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b` (fork `solr-17241-submit`), parent `9859d8bb2e8d63061116b8281639a4beb1be480c`.
- Source repair: `MessageBodyWriters.java` (non-hosting request fallback), committed and pushed fast-forward by vm5 after the owner asked for the branch to pass the gate. Focused tests before the claim: integration 14/0 failures, filter 8/0 failures (`/workspace/tmp/gate-17241/quick/`).
- Runner: worktree `/workspace/worktrees/pool-vm5-17241` at `86d86e3d3e`. Control tree: `/workspace/worktrees/pool-vm5-17241-control2`, production filter replaced only, not committed.
- Logs: `/workspace/gates/logs/vm5/solr17241-wt-format-round-3/`.
- JDK: `/workspace/jdk/jdk-21.0.12.1+1`. GRADLE_USER_HOME `/workspace/gradle-home`. Lock: `flock /workspace/tools/.gradle-vm5.lock`.
- Seed: `17241C0FFEE17241`.
- Capability tags used: gate, implementation (source repair). No PR, public comment, or JIRA action.
- Started: 2026-10-30T01:53Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-17241-wt-format-round-3@86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b.
- Decision ladder: rung 1 for the NPE cause (read `JavaBinResponseWriter.write` and `BaseMessageBodyWriter`); rung 2 for the failing test (round 2 XML); rung 3 for the gate's proof shape.

Heartbeat: 2026-10-30T01:53Z
