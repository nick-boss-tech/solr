# Host: vm6

Pool Linux host. Shared capabilities and operating rules are in [pool-linux.md](pool-linux.md).

- Capability tags: review, draft, records, implementation, gate, premise-run, settling-run, sweep, ui, jvm-tests, build.
- Verified toolchain: Linux 6.1 aarch64 (Amazon Linux 2023); Temurin OpenJDK 21.0.12.1+1; Gradle 9.7.0 from the branch wrapper; Node 22.23.4 and npm 10.9.9 present, no npm or bats capability claimed.
- Local JDK: `/workspace/jdk/jdk-21.0.12.1+1`. Set `JAVA_HOME` and `GRADLE_USER_HOME=/workspace/gradle-home` before builds.
- Build lock: `/workspace/tools/.gradle-vm6.lock`, taken by `pool/bin/sweep-run.sh` from the Host id in `/workspace/AGENTS.md`.
- Role beyond the pool: none. Takes work by claim at Nick's direction, like other pool hosts.
- Heartbeat: 2026-10-24T17:00:35Z; `build` tag granted by the owner (Nick, 2026-10-24). SOLR-17861 claim `pool-ui-SOLR-17861`: checksum slice on `solr-17861-ui-license-check` (022acd4ec9, 92aed103a0), gate green; LICENSE/NOTICE scope is NEEDS-OWNER (`reports/pool-ui-SOLR-17861-round-1.md`).