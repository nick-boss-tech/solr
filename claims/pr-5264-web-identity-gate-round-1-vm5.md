# Claim: SOLR-18610 web identity gate, round 1 (vm5)

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: CLAIMED.
- Job: `gates/pr-5264-web-identity-test-round-1.md`, whole job.
- Implementation claim it verifies: `claims/pr-5264-web-identity-test-round-1-windows.md`.
- Fork branch: `nick-boss-tech/solr:solr-18610-submit`. Tested head: `c47026394be7a14bf303c9a0c84c8895afaadb2b`, which is the windows head `dbacd6951e7692d1b55920eddb239284df33b3b0` plus one tidy line-wrap commit (`SOLR-18610: Wrap the web identity test to the tidy line length`), pushed fast-forward by vm5.
- Preflight: detached `./gradlew tidy` at `dbacd6951e7` (log `/workspace/tmp/gate-18610-logs/tidy-preflight.log`, BUILD SUCCESSFUL, exit 0) changed only line wrapping in the new test file. Repaired at `c470263` as the gate's tidy repair.
- Runner: worktree `/workspace/worktrees/pool-vm5-18610` at `c470263`.
- Fail-before proof checkout: `/workspace/worktrees/pool-vm5-18610-failbefore`, same head, with only `solr/server/etc/security.policy` replaced by the `3db71000514e1edaa8e6ae0426fce92ca97990e3` version.
- Logs: `/workspace/tmp/gate-18610-logs/`.
- JDK: `/workspace/jdk/jdk-21.0.12.1+1`, OpenJDK 21.0.12.1 (Temurin 21.0.12.1+1). GRADLE_USER_HOME `/workspace/gradle-home`.
- Lock: `flock /workspace/tools/.gradle-vm5.lock` for every Gradle invocation.
- Seed: `18610C0DE`.
- Capability tags used: gate, implementation (tidy repair only). No PR, public comment, or JIRA action.
- Started: 2026-10-29T15:31Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pr-5264-web-identity-gate-round-1-vm5@vm5.
- Decision ladder: rung 1 for the tidy diff (line wrapping only, no semantic change); rung 3 for the gate's own proof shape.

Heartbeat: 2026-10-29T15:31Z
