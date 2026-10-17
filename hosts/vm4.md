# Host: vm4 (tracking label assigned by Nick)

- Environment: Linux aarch64, 4 CPUs, 7 GB RAM, root filesystem 7.8 GB on the shared workspace volume. Tracking name only; it runs on the same infrastructure as vm2 and vm3.
- Capabilities: Linux, JDK 21.0.12.1 (Temurin, `/workspace/tools/jdk21`), Gradle 9.7.0 through the repo wrapper, git 2.34.1. Not installed: BATS, Node, and the vm3 `/workspace/jdk` install.
- Build protocol: one Gradle build at a time per host, serialized with `flock`. Check `df -h /` and `df -i /` before and after each build and record both here. Use caches under `/workspace`, not `/`.
- Source worktrees live under `/workspace/worktrees/`. Never run bare `git stash`.

## Resource snapshot, 2026-10-17T04:56Z

- Root filesystem: 7.8 GB total, 3.8 GB available (49% used). Inodes: 524,288 total, 457,332 free (13% used).
- Memory: 7 GB total, 6 GB available. CPUs: 4.

## Heartbeat

- 2026-10-17T04:56Z: claimed `claims/pool-solr-18551-mmap-test-rerun.md` (SOLR-18551 settling-run).
- 2026-10-17T05:28Z: SOLR-18551 rerun complete (head 3 of 3 green, fail-before failed at openInputCalls as expected). Claim marked DONE. Root filesystem 3.8 GB available, 457,331 inodes free. No Gradle process running.
