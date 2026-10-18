# Claim: SOLR-16887 restore ErrorFile and BATS gate (steps 4 to 6 rerun)

Status: CLOSED 2026-10-18 (vm1 completed the gate: GREEN at a1ab4a348d44, receipts/SOLR-16887.md)

- Claimed: 2026-10-17T22:20:57Z by vm3. Job: gates/SOLR-16887-errorfile-bats-name.md.
- Head: 8c1a3faa530271a471393f3acf313d9ff7dacf2b (rebuilt from 42675f65d6, see job file).
- Capability tags: implementation, gate.
- Runner: ./gradlew with the env file tools/vm3-env.sh, under flock /workspace/tools/.vm3-gradle.lock. Worktree: /workspace/worktrees/16887.
- Push rule: fast-forward only, and only on green. Gate failures are recorded in the job file before any relaunch.

Heartbeat: 2026-10-17T22:20:57Z (claim).

- 2026-10-17T23:33:41Z: heartbeat. Head rebuilt as b0223fd235 (from 42675f65d6, four edits per the job file). BATS premise leg blocked on the JS client build on this host (npm babel plugin missing; see gates/SOLR-16887-*.md once recorded). Not relaunched.

- 2026-10-18T00:31Z: BLOCKED. Premise leg fails at the flag assertion as expected. Head target test body passes; teardown solr stop does not end the Solr JVM on this host. Details in the gate file.

- 2026-10-18T00:50Z: HANDED OFF to vm1. Job, patch (gates/SOLR-16887-head-b0223fd235.patch) and host notes are in the gate file. vm3 holds no active build.

- 2026-10-18: closed. vm1 gate is green and pushed. vm3 BATS blockage did not reproduce there: vm1 has lsof, which vm3 lacked. The vm3 shim test was not a reliable substitute and the JVM-stop hypothesis in the gate file is not confirmed; both are left as recorded.
