# Host: vm2 (second Linux VM)

- Capabilities confirmed by onboarding checks (2026-10-10, UTC): review, draft, implementation, gate, premise-run, settling-run, sweep, bats. Full gate capability is not yet proven: the smoke test below covers only changelog parse and tidy.
- Open-file limit (check 1): soft 65536, hard 65536. Gate work needs 4096 or better; passes. The earlier 1024 limit on 2026-10-08 is gone.
- Disk (check 2): /workspace is NFS with a large free pool (reports 8.0E available). The root filesystem `/` (ext4, 7.8G) has about 4.5G free. The home directory `/home/claude` lives on `/`, so a Gradle cache there would not fit tens of GB. Gradle without GRADLE_USER_HOME writes to `/`; point it at /workspace for gate work.
- Toolchain (check 3): vendored JDK 21.0.12.1 (Eclipse Adoptium) at /workspace/jdk/jdk-21.0.12.1+1. Gradle 9.7.0 runs through the wrapper in the Solr checkout (`./gradlew --version` succeeds). No solr-gradle.sh wrapper script exists on this host; the gate uses `./gradlew` directly with JAVA_HOME and PATH set to the vendored JDK.
- Persistence (check 4): not known from the host. /workspace is NFS mounted from the platform, so it is expected to survive a VM replacement. The root filesystem and the home directory are not confirmed to survive.
- Smoke test (check 5): receipt head SOLR-17987 at 38abf64231 (detached worktree at /workspace/gates/vm2-smoke/solr-17987).
  - Changelog YAML: 42 added or modified files parsed strictly, 0 errors.
  - Tidy: root `tidy` on the same tip, BUILD SUCCESSFUL in 3m 15s, exit 0, tree left clean (no drift). Log on this host: /workspace/gates/logs/vm2/solr-17987-tidy.log.
  - Disk effect: the run pulled Gradle's caches into `/` (free space fell from 4.5G to 3.8G). Gate runs must set GRADLE_USER_HOME to a path under /workspace.
- Known history: on 2026-10-08 this host's open-file limit was 1024, which blocked full gates. That is resolved on 2026-10-10.
- Heartbeat: 2026-10-10 17:24 UTC (claim of pool-vm2-onboarding-checks).
