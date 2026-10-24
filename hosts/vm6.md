# Host: vm6

Pool Linux host. Shared capabilities and operating rules are in [pool-linux.md](pool-linux.md).

- Capability tags: review, draft, records, implementation, gate, premise-run, settling-run, sweep, ui, jvm-tests, build.
- Verified toolchain: Linux 6.1 aarch64 (Amazon Linux 2023); Temurin OpenJDK 21.0.12.1+1; Gradle 9.7.0 from the branch wrapper; Node 22.23.4 and npm 10.9.9 present, no npm or bats capability claimed.
- Local JDK: `/workspace/jdk/jdk-21.0.12.1+1`. Set `JAVA_HOME` and `GRADLE_USER_HOME=/workspace/gradle-home` before builds.
- Build lock: `/workspace/tools/.gradle-vm6.lock`, taken by `pool/bin/sweep-run.sh` from the Host id in `/workspace/AGENTS.md`.
- Role beyond the pool: none. Takes work by claim at Nick's direction, like other pool hosts.
- Heartbeat: 2026-10-24T16:38:10Z; `build` tag granted by the owner (Nick, 2026-10-24). SOLR-17861 is not claimed: its staged ask to Christos is unposted, and the implementation direction depends on his answer.
