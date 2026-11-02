# Claim: vm2 license gate for PR 5278, round 1

- Claimant: vm2 (host id per AGENTS.md). Tags used: gate, records.
- Started: 2026-11-02T00:44:29Z. Architecture: aarch64 (Linux 6.1).
- Job: `gates/pr-5278-license-metadata-round-1.md`.
- Pinned head H: `cb8c6ab2752b5202b52c7479b9b77ee7806b350e`. Parent P: `82f60b1b7976c187541a6f6808a781bb7f8b9228` (ancestor verified). Fork branch `nick-boss-tech/solr:pr-5278-jcl-license-metadata`, remote read verified H.
- Implementation claim: `claims/pr-5278-license-metadata-windows-round-1.md`.
- Toolchain: JAVA_HOME `/workspace/jdk/jdk-11.0.32.1+1` (OpenJDK 11.0.32.1), GRADLE_USER_HOME `/workspace/gradle-home`, repo wrapper Gradle 8.10. Run with `./gradlew --no-daemon`.
- Capability: no Docker required for this job; vm2 holds gate and records tags.
- Preflight: `pool/bin/gate-preflight.sh` on H, tidy clean, rc=0. Log `/workspace/logs/vm2/pr-5278-license-metadata-round-1/preflight/`.
- Clean checkout: detached worktree `/workspace/worktrees/gate-5278-license-h` at H.
- Host lock: `/workspace/tools/.gradle-vm2.lock`, `flock -n`. Runs after the 5276 gate on this host, never concurrently.
- Log archive: `/workspace/logs/vm2/pr-5278-license-metadata-round-1/`.
- Heartbeat: once an hour in `hosts/vm2.md`.
