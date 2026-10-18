# Claim: pool-vm2-gate-backlog-6009-edismax-p3

- Claimant host: vm2, matching a file in `hosts/` (`hosts/pool-linux.md`, host label vm2).
- Status: ACTIVE. Gate job for SOLR-6009 at head b7695a518cccc0158a6374c61af9760884d1cb80.
- Job or assignment: `gates/SOLR-6009-edismax-p3.md`.
- Slice: the whole gate job (step 0 changelog parse, step 1 tidy, step 4 focused run, step 5 module check).
- Capability tags used: gate.
- Started: 2026-10-18T08:40:27Z (UTC).

Setup: 2026-10-18T08:40Z: `~/.pool-host` and `~/.pool-env` were missing on this VM. `~/.pool-host` was written with `vm2`, matching `/workspace/.pool-host` and AGENTS.md. `~/.pool-env` sets JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1 (Temurin 21.0.12.1, verified) and GRADLE_USER_HOME=/workspace/gradle-home.

Head check: 2026-10-18T08:40Z: `origin/solr-6009-submit` is b7695a518cccc0158a6374c61af9760884d1cb80, matching the job's Head line.

Heartbeat: 2026-10-18T08:40:27Z
