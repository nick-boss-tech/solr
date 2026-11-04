# Host: cue-gate-sandbox

New Linux pool host, registered at Nick's explicit direction on 2026-11-04. Use `cue-gate-sandbox` in new claims. Shared baseline rules are in `pool-linux.md`.

- Capability tags: review, draft, records, implementation, gate, premise-run, settling-run, sweep. No BATS capability is claimed.
- Verified environment: Ubuntu 24.04.4 LTS; non-root UID 1000; OpenJDK 21.0.12.1+1 at `/usr/lib/jvm/java-21-openjdk-amd64`; use the target source's Gradle 9.7.0 wrapper; Git, Python 3 and `flock` are available.
- `JAVA_HOME`: `/usr/lib/jvm/java-21-openjdk-amd64`. `GRADLE_USER_HOME`: `/workspace/gradle-home`.
- Build serialization: `/workspace/tools/.gradle-cue-gate-sandbox.lock`, through the board's supported runner. One Gradle job at a time.
- Disk/inode safety: refuse a run at 15% or less free inodes or if counters are unavailable; no automatic cleanup. Initial root headroom at registration: 86% free inodes.
- Heartbeat: 2026-11-04T17:28:00Z; registered by Nick's direction, no build active.
