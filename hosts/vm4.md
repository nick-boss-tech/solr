# Host: vm4

Pool Linux host. Capabilities and rules are in `hosts/pool-linux.md`.

Capabilities verified on this host:
- `bats`: verified 2026-10-24T20:52Z. `:solr:packaging:downloadBats` succeeded (log `/workspace/gates/logs/vm4/pr-5204/downloadBats-20261024T204823Z.log`). Runs `solr/packaging/build/bats-libs/bats-core/bin/bats` version Bats 1.8.2 (bats-support 0.2.0, bats-assert 2.0.0, bats-file 0.3.0 from `gradle/libs.versions.toml`). Execution is verified only by the phase claim that runs it.
- Bash 5.1.16 (aarch64). Python 3.11.17 at `/usr/local/bin/python3`. JDK 21.0.12.1 at `/workspace/tools/jdk21`. curl 7.81.0. Git 2.49.1 at `/workspace/tools/git-2.49.1/bin/git`.
- GitHub archive downloads for the BATS libraries: HTTP 200 from this host on 2026-10-24.

Environment added for PR 5204 (2026-10-24):
- `lsof` 4.99.7 from conda-forge at `/workspace/tools/lsof-env/bin` (put first on PATH for BATS runs).
- `/workspace/tools/reap-run.py`: child-subreaper wrapper. This VM's PID 1 does not reap orphaned children, so Solr JVMs left zombies and `solr stop` waited 180 seconds. Wrap each Gradle BATS command with it.

Heartbeat:
- 2026-10-25T02:05Z: PR 5204 re-gate attempt 3 running (claim `claims/pr-5204-phase-g-final-gate-attempt3-vm4-round-1.md`, single launch under the lock). Leg 1 passed (27 of 27). Leg 2 running.
- 2026-10-25T01:36Z: PR 5204 final gate is INCOMPLETE. Leg 1 passed (27 of 27). Leg 2 attempts 1 and 2 are incomplete. Attempt 2 was stopped because of a launch overlap. Verdict and report pushed. Nothing is running.
- 2026-10-25T01:00Z: claim `claims/pr-5204-phase-g-final-gate-vm4-round-1.md` live. Leg 1 passed (27 of 27). Leg 2 at 97+ ok, 0 not ok, still running.
- 2026-10-24T23:57Z: claim `claims/pr-5204-phase-g-final-gate-vm4-round-1.md` live. Leg 1 passed (27 of 27). Leg 2 at 88 ok, 0 not ok, still running.
- 2026-10-24T23:07Z: claim `claims/pr-5204-phase-g-final-gate-vm4-round-1.md` live. Leg 1 passed (27 of 27). Leg 2 at 41 ok, 0 not ok, still running.
- 2026-10-24T21:58Z: active claims `claims/pr-5204-phase-g-final-gate-vm4-round-1.md` (final-head gate, leg 1 running). Earlier: `claims/pr-5204-picocli-review-vm4-round-1-phase-r.md`.

Disk record:
- 2026-10-18T15:09Z before `chaos-5516-run1`: `/` 4.6G free, 39% used; inodes 481348 free (9% used).
- 2026-10-18T15:14Z after `chaos-5516-run1`: `/` 4.6G free, 39% used; inodes 481343 free (9% used).
- 2026-10-24T20:52Z after downloadBats: `/` 4.6G free, 39% used; inodes 479268 free (9% used).
