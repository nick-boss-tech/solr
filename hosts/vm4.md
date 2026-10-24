# Host: vm4

Pool Linux host. Capabilities and rules are in `hosts/pool-linux.md`.

Capabilities verified on this host:
- `bats`: verified 2026-10-24T20:52Z. `:solr:packaging:downloadBats` succeeded (log `/workspace/gates/logs/vm4/pr-5204/downloadBats-20261024T204823Z.log`). Runs `solr/packaging/build/bats-libs/bats-core/bin/bats` version Bats 1.8.2 (bats-support 0.2.0, bats-assert 2.0.0, bats-file 0.3.0 from `gradle/libs.versions.toml`). Execution is verified only by the phase claim that runs it.
- Bash 5.1.16 (aarch64). Python 3.11.17 at `/usr/local/bin/python3`. JDK 21.0.12.1 at `/workspace/tools/jdk21`. curl 7.81.0. Git 2.49.1 at `/workspace/tools/git-2.49.1/bin/git`.
- GitHub archive downloads for the BATS libraries: HTTP 200 from this host on 2026-10-24.

Heartbeat:
- 2026-10-24T20:52Z: active claim `claims/pr-5204-picocli-review-vm4-round-1-phase-r.md` (PR 5204 review reproduction). Worktree `/workspace/worktrees/pr-5204-r` at the reviewed head.

Disk record:
- 2026-10-18T15:09Z before `chaos-5516-run1`: `/` 4.6G free, 39% used; inodes 481348 free (9% used).
- 2026-10-18T15:14Z after `chaos-5516-run1`: `/` 4.6G free, 39% used; inodes 481343 free (9% used).
- 2026-10-24T20:52Z after downloadBats: `/` 4.6G free, 39% used; inodes 479268 free (9% used).
