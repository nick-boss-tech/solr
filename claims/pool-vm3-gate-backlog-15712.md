# Claim: SOLR-15712 UDVAS version gate (corrected test class, full gate)

Status: CLAIMED 2026-10-17T22:20:57Z by vm3 (takes over from the STALE vm2 claim claims/pool-vm2-gate-backlog-15712.md, whose run failed and was classified by the job file).

- Claimed: 2026-10-17T22:20:57Z by vm3. Job: gates/SOLR-15712-udvas-version-gate.md.
- Head: 2d65ed6329 (rebuilt from cb988a2ff4, see job file and gates/SOLR-15712-udvas-test-fix.md).
- Capability tags: implementation, gate.
- Runner: ./gradlew with the env file tools/vm3-env.sh, under flock /workspace/tools/.vm3-gradle.lock. Worktree: /workspace/worktrees/15712.
- Push rule: fast-forward only, and only on green. Gate failures are recorded in the job file before any relaunch.

Heartbeat: 2026-10-17T22:20:57Z (claim).
