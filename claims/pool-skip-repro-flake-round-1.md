# Claim: pool-skip-repro-flake-round-1

- Claimant host: vm2, matching a file in `hosts/` (`hosts/vm2.md`).
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
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

Append one heartbeat line at least once an hour while the claim is active, and push it.
