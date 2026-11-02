# Claim: vm2 license gate for PR 5276, round 1

- Claimant: vm2 (host id per AGENTS.md). Tags used: gate, records.
- Started: 2026-11-02T00:44:29Z. Architecture: aarch64 (Linux 6.1).
- Job: `gates/pr-5276-license-metadata-round-1.md`.
- Pinned head H: `5f9613e13865b73b992d8321142e61ceddee2b92`. Parent P: `a6d008eb38d508b0179c71bd6a862e2a7b40fb24` (ancestor verified). Fork branch `nick-boss-tech/solr:pr-5276-license-metadata`, remote read verified H.
- Implementation claim: `claims/pr-5276-license-metadata-windows-round-1.md`.
- Toolchain: JAVA_HOME `/workspace/jdk/jdk-11.0.32.1+1` (OpenJDK 11.0.32.1), GRADLE_USER_HOME `/workspace/gradle-home`, repo wrapper Gradle 8.10 (`gradle-8.10-bin.zip`). Run with `./gradlew --no-daemon`.
- Capability: no Docker required for this job; vm2 holds gate and records tags.
- Preflight: `pool/bin/gate-preflight.sh` on H, tidy clean, rc=0. Log `/workspace/logs/vm2/pr-5276-license-metadata-round-1/preflight/`.
- Clean checkout: detached worktree `/workspace/worktrees/gate-5276-license-h` at H.
- Host lock: `/workspace/tools/.gradle-vm2.lock`, `flock -n`, one gate build at a time on this host.
- Log archive: `/workspace/logs/vm2/pr-5276-license-metadata-round-1/`. Fresh XML copies go to `xml-*` subdirectories there before any later cleanTest.
- Heartbeat: once an hour in `hosts/vm2.md`.
