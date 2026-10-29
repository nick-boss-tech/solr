# Claim: SOLR-17241 wt gate, round 2 (vm5)

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: CLAIMED.
- Job: `gates/SOLR-17241-wt-format-round-2.md`, whole job. Supersedes round 1 (`gates/SOLR-17241-wt-format-round-1.md`), whose head was reflowed by tidy.
- Tested head: `7a5a998ee9b07d36b6389dd3e2ef2d4246a1588e` (fork `solr-17241-submit`), parent `9859d8bb2e8d63061116b8281639a4beb1be480c`.
- Round 1 tidy preflight (`/workspace/gates/logs/vm5/solr17241-wt-format-round-1/tidy-preflight.log`, BUILD SUCCESSFUL) at `bded8997e5` reflowed one line in `MediaTypeOverridingFilterTest.java`; repaired and pushed fast-forward as `7a5a998ee9`.
- Runner: worktree `/workspace/worktrees/pool-vm5-17241` at `7a5a998ee9`. Control tree: `/workspace/worktrees/pool-vm5-17241-control`, production file replaced only, not committed.
- Logs: `/workspace/gates/logs/vm5/solr17241-wt-format-round-2/`.
- JDK: `/workspace/jdk/jdk-21.0.12.1+1`. GRADLE_USER_HOME `/workspace/gradle-home`. Lock: `flock /workspace/tools/.gradle-vm5.lock`.
- Seed: `17241C0FFEE17241`.
- Capability tags used: gate, implementation (tidy repair only). No PR, public comment, or JIRA action.
- Started: 2026-10-29T23:57Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: SOLR-17241-wt-format-round-2@7a5a998ee9b07d36b6389dd3e2ef2d4246a1588e.
- Decision ladder: rung 1 for the tidy diff (line wrap only); rung 3 for the gate's proof shape.

Heartbeat: 2026-10-29T23:57Z
