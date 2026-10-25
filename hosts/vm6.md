# Host: vm6

Pool Linux host. Shared capabilities and operating rules are in [pool-linux.md](pool-linux.md).

- Capability tags: review, draft, records, implementation, gate, premise-run, settling-run, sweep, ui, jvm-tests, build.
- Verified toolchain: Linux 6.1 aarch64 (Amazon Linux 2023); Temurin OpenJDK 21.0.12.1+1; Gradle 9.7.0 from the branch wrapper; Node 22.23.4 and npm 10.9.9 present, no npm or bats capability claimed.
- Local JDK: `/workspace/jdk/jdk-21.0.12.1+1`. Set `JAVA_HOME` and `GRADLE_USER_HOME=/workspace/gradle-home` before builds.
- Build lock: `/workspace/tools/.gradle-vm6.lock`, taken by `pool/bin/sweep-run.sh` from the Host id in `/workspace/AGENTS.md`.
- Role beyond the pool: none. Takes work by claim at Nick's direction, like other pool hosts.
- Heartbeat: 2026-10-25T02:14:03Z; no live claim. SOLR-16630 final-head gate DONE (receipt section appended). Looking for the next vm6-tag item.