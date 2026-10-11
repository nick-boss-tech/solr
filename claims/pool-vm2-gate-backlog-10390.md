# Claim: SOLR-10390 premise run (round 1, tranche 1, job 12)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-1.md. Job: gates/SOLR-10390-premise.md.
Capability tags: premise-run (BATS). Staffing: 1.
Started: 2026-10-11T01:25:00Z (UTC).

Heartbeat: 2026-10-11T01:25:00Z.

Result 2026-10-11T01:49Z: premise HOLDS on the test body. Base cabedd1d96 bin/solr fails at refute 'Please install lsof' (test_start_solr.bats:129). Head 4af4a6834e bin/solr passes the body; the bats report is "not ok" only from teardown (solr stop --all, 30s wait, force-killed on this host; reproduced manually on base and head). Receipt receipts/SOLR-10390.md; no gate run.
DONE: 2026-10-11T01:49Z (UTC), outcome PREMISE HELD.

## Full-gate claim (shared backlog)

Claimant host: vm3. Assignment: assignments/pool-shared-gate-backlog-buffer.md. Job: gates/SOLR-10390.md.
Capability tags: gate, bats. Staffing: 1.
Branch: `solr-10390-submit` at `4af4a6834e2b43251381027c24b18761fc5a9513` (live tip verified by ls-remote at claim time).
Merge-base: `cabedd1d968059215188f4e7563fb303241899ed`.
Runner: Linux; BATS 1.10.0 at `/usr/bin/bats`. Worktree: `/workspace/gates/solr-10390-build`. Logs: `/workspace/gates/logs/vm3/10390-*.log`.
Started: 2026-10-11T07:54:04Z (UTC).
Heartbeat: 2026-10-11T07:54:04Z (UTC).
Status: CLAIMED. Packaging, changelog parse, proof, and focused BATS run remain; Java checks are not applicable per the gate file.

Packaging result 2026-10-11T07:57:40Z: removed only `SOLR-10390-TESTING.md`; commit `5a86f908052f031b6ce0f0004c0bdf3e825a12c5` pushed fast-forward and verified as the live `solr-10390-submit` tip.
Status: CLAIMED. Changelog parse, proof, and focused BATS run remain; Java checks are recorded not applicable.

Changelog result 2026-10-11T08:00:01Z: repository YAML validator exited 0; 223/223 unreleased fragments valid, including `changelog/unreleased/SOLR-10390-start-without-lsof.yml`. Logs: `/workspace/gates/logs/vm3/10390-changelog.log` and `/workspace/gates/logs/vm3/10390-changelog-headroom.log`.
Status: CLAIMED. Proof leg, focused BATS test and receipt remain; Java checks are not applicable.

Heartbeat/resume 2026-10-11T10:06:48Z: the same vm3 holder resumed after the two-hour monitoring-shell wait limit. Verified no competing claim, topic live at `5a86f908052f031b6ce0f0004c0bdf3e825a12c5`, and build log stable at 15,029 bytes with mtime 2026-10-11T08:10:54Z. The only Gradle process was a sleeping daemon (1 CPU tick over 4 seconds); no Gradle client or worker was active.
Distribution result: final Gradle output says `BUILD SUCCESSFUL in 7m 45s` (85 actionable tasks, no tests), including `:solr:packaging:downloadBats` and `:solr:packaging:assembleDist`. Project BATS 1.8.2 and its three helper libraries are present; full and slim archives are 270,412,436 and 83,747,538 bytes. Logs: `/workspace/gates/logs/vm3/10390-dist-build.log`, `/workspace/gates/logs/vm3/10390-dist-build-headroom.log`, `/workspace/gates/logs/vm3/10390-dist-build-post-recovery.log`.
Status: CLAIMED. Full archive expanded to `SOLR_TIP`; proof and focused head BATS tests remain; Java checks remain not applicable.

Expansion result 2026-10-11T10:13:34Z: extracted the already-built full archive to `/workspace/gates/solr-10390-build/solr/packaging/build/solr-11.0.0-SNAPSHOT`; no Gradle rerun. Its executable `bin/solr` is byte-identical to the packaged branch source (SHA-256 `6eddf7f7eaa2f599410db6accaaa56dcbc93663d4c952697a3d96a360b8b3899`); 1,626 regular files. Logs: `/workspace/gates/logs/vm3/10390-dist-extract.log`, `/workspace/gates/logs/vm3/10390-dist-extract-headroom.log`. Pre/post: 21 GB free, 59,763 to 58,044 inodes free (93% to 94%); only sleeping Gradle daemon PID 221714, zero CPU ticks in the pre-check sample. Host `/usr/bin/lsof` is present; next create and verify a no-lsof PATH for both BATS runs.


Heartbeat: 2026-10-11T10:19:18Z (UTC).

SOLR-10390 BATS preparation: removed 33 verified ignored/untracked task build directories outside `solr/packaging/build` (8,390 entries; 559,663,571 bytes), retaining the archives, extracted distribution, BATS libraries, caches, source, `.git`, and logs. The task-local PATH without lsof is verified; project BATS 1.8.2 and host BATS 1.10.0 are distinguished in the gate file. The unsupported `--list-tests` option check executed no tests; supported `--count --filter` selected exactly one case. Pre-selection resource snapshot: 22 GB available and 65,056 inodes free (93% reported use); only sleeping Gradle daemon PID 221714, 1 CPU tick in 4 seconds, no client or worker. Logs are recorded in `gates/SOLR-10390.md`.
Status: CLAIMED. Proof leg at the base and focused BATS test at packaged head remain; Java checks are not applicable.
