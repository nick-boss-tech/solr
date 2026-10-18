# Claim: pool-skip-repro-flake-round-1

- Claimant host: vm2, matching a file in `hosts/` (`hosts/vm2.md`).
- Status: DONE: 2026-10-18T08:20Z, deliverable `reports/2026-10-18-skip-repro/A1-flake-round-1.md`.
- Job or assignment: `assignments/pool-skip-repro-flake-round-1.md`.
- Slice: the whole assignment, 28 tickets, at the pinned head `498a09aea9`.
- Capability tags used: sweep.
- Started: 2026-10-18T06:51:11Z (UTC).

Heartbeat: 2026-10-18T06:51:11Z

Setup: 2026-10-18T06:55Z: worktree `/workspace/worktrees/pool-vm2-flake-r1` detached at `498a09aea9` (the pinned head). Gradle via `pool/bin/sweep-run.sh` from the coordination worktree, JAVA_HOME `/workspace/jdk/jdk-21.0.12.1+1`, GRADLE_USER_HOME `/workspace/gradle-home`, from `~/.pool-env`. Command per run: `./gradlew --no-daemon --console=plain <module>:test --tests <FQCN> --rerun`. All 28 test classes exist on the head (none "not on main"). Logs: `/workspace/gates/logs/vm2/flake-r1/<ticket>-<pass>.log`, XML copies in `<ticket>-<pass>.xml/`, df before and after each run in `<ticket>-<pass>.headroom.log`. Free space on `/` stayed at 4.5G and free inodes at 91% through the first 14 runs.

Result (pass 1, no explicit seed, run once each; the seed is the one Gradle printed): all 14 runs so far green, rc=0, zero failures, zero errors.
- SOLR-2865 rc=0 seed 9EB8C4018C18B692, 1 test
- SOLR-3220 rc=0 seed 8798F4E55CD18AE, 1 test
- SOLR-5791 rc=0 seed A0F6793AC7F42061, 1 test
- SOLR-5792 rc=0 seed 2A6A9666CA26C5CE, 1 test
- SOLR-5793 rc=0 seed 84BC03CE7EE0459A, 6 tests
- SOLR-6287 rc=0 seed 5D0AB02F48FB5320, 17 tests
- SOLR-7597 rc=0 seed 9C6BD3B3B9E488EF, 1 test
- SOLR-7992 rc=0 seed 40CFFD8971369095, 16 tests
- SOLR-9144 rc=0 seed C216E516B8C5627A, 25 tests
- SOLR-9328 rc=0 seed 1B3B3071A669191C, 3 tests
- SOLR-9390 rc=0 seed 393515668CFE64B8, 1 test
- SOLR-9427 rc=0 seed 22C6DD368C4D28A3, 25 tests
- SOLR-9428 rc=0 seed C825CEC6D875F28E, 1 test
- SOLR-9965 rc=0 seed 6EF7EAAC35553A06, 25 tests
- SOLR-10592 rc=0 seed F7EF26FDD0F61C31, 4 tests
- SOLR-10759 rc=0 seed 9EEEC0633D8796DF, 11 tests
Conclusive for a passing run only; a flake shows as a failure, so these 14 are "did not reproduce at this seed". Pass 1 continues with SOLR-10841 onward.

Heartbeat: 2026-10-18T07:38:00Z

Result: 2026-10-18T07:38Z: SOLR-10841-p1, no explicit seed, head 498a09aea9, rc=1 FAIL, seed B2EDDADF9B4A881F, 14 tests, 2 failures, 0 errors. Failures: testKillLeader "AssertionError: Expected collection to be 1x2"; testRecovery "AssertionError: Replica didn't recover". The log also has 12 "Index fetch failed" lines, each a `FileSystemException ... -> ...: Too many links` on hard-link creation for a segment file under `tlog_replica_test_kill_leader_shard1_replica_t3/data/index.*`. Log `/workspace/gates/logs/vm2/flake-r1/SOLR-10841-p1.log`, XML `SOLR-10841-p1.xml/`. Conclusive as a failing run at that seed; the failure is in the class.

Result: 2026-10-18T07:46Z: SOLR-10976-p1, no explicit seed, head 498a09aea9, rc=0 PASS, seed 5DF3F7F65065E53C, 33 tests, 0 failures, 0 errors. Log `SOLR-10976-p1.log`, XML `SOLR-10976-p1.xml/`. Did not reproduce at this seed.

Result: 2026-10-18T07:46Z: SOLR-11058-p1, no explicit seed, head 498a09aea9, rc=1 FAIL, seed F982A35C90FB67A5, 14 tests, 3 failures, 0 errors. Failures: testRecovery "Replica didn't recover"; testOnlyLeaderIndexes "Some replicas are not in sync with leader"; testKillLeader "Expected collection to be 1x2". The log has 29 `FileSystemException ... -> ...: Too many links` lines on hard-link creation (`_0.si`, `_2.si`, `_b.fdm`, `_k.fdt`). Log `SOLR-11058-p1.log`, XML `SOLR-11058-p1.xml/`. Conclusive as a failing run at that seed.

Rerun plan, written before the run, 2026-10-18T07:55Z: the Decisions row "Run cadence" allows one more run with the failing seed, passed as `-Ptests.seed=<seed>`. The first run for SOLR-10841 and SOLR-11058 had no `-Ptests.seed`, so this rerun is the first to pin that seed explicitly. Head, JDK, worktree and command are unchanged. This is the second and last run for each failing ticket under the two-run stop rule: SOLR-10841 and SOLR-11058 so far, and any other ticket that fails in pass 1 gets the same single rerun. The rerun starts after pass 1 finishes, because the host lock is held until then. A dedicated script `/tmp/vm2-flake-r1/chain2.sh` runs it.

Heartbeat: 2026-10-18T07:55:00Z

Result: 2026-10-18T08:12Z: SOLR-10841-p2 (rerun at the failing seed `-Ptests.seed=B2EDDADF9B4A881F`), head 498a09aea9, rc=1 FAIL, 14 tests, 2 failures, 0 errors. Same two methods and messages as pass 1 (testKillLeader, testRecovery). Log `SOLR-10841-p2.log`, XML `SOLR-10841-p2.xml/`. Reproduced at this seed; last run for this ticket.

Result: 2026-10-18T08:17Z: SOLR-11058-p2 (rerun at `-Ptests.seed=F982A35C90FB67A5`), head 498a09aea9, rc=1 FAIL, 14 tests, 3 failures, 0 errors. Same three methods and messages as pass 1 (testRecovery, testOnlyLeaderIndexes, testKillLeader). Log `SOLR-11058-p2.log`, XML `SOLR-11058-p2.xml/`. Reproduced at this seed; last run for this ticket.

Result: 2026-10-18T08:19Z, pass 1 tail (no explicit seed, rc=0 PASS, the seeds are the ones printed): SOLR-11247 seed 21FA8A11BC9942C6, 10 tests; SOLR-11463 seed 79F00C0EE1E59DB2, 2 tests; SOLR-11627 seed 130AB2BA39750A2E, 3 tests; SOLR-11826 seed B936D489BA66A869, 4 tests; SOLR-12093 seed 97B5BFB090B42617, 2 tests; SOLR-12302 seed 152ECDC474A04E45, 1 test; SOLR-12844 seed A0D025C0835FBE2A, 1 test; SOLR-14881 seed 80C222C6E18916C8, 1 test; SOLR-16094 seed 64F796917C63A967, 1 test. All 0 failures, 0 errors. Logs `<ticket>-p1.log`, XML `<ticket>-p1.xml/`. Did not reproduce at these seeds.

Result: 2026-10-18T08:19Z: pass 1 complete, 26 of 28 rows green, 2 failing (SOLR-10841, SOLR-11058). Both reproduced at the pinned seed. Deliverable `reports/2026-10-18-skip-repro/A1-flake-round-1.md`.

DONE: 2026-10-18T08:20Z. Deliverables: `reports/2026-10-18-skip-repro/A1-flake-round-1.md` (one row per ticket, seeds, XML paths, the two failing rows with the reproduced methods and exception lines).

Heartbeat: 2026-10-18T08:20:00Z

Append one heartbeat line at least once an hour while the claim is active, and push it.
