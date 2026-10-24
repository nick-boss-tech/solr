# Claim: pr-5204-phase-r-negative-control-vm4-round-1

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: LIVE.
- Job or assignment: `assignments/pr-5204-picocli-review-implementation-and-gate-vm4-round-1.md`, Phase R, the original-head negative control only. This is the discriminating reproduction of the reviewed selector finding. It is not the final-head gate (Phase G), which needs its own claim after any fix.
- Tags used: gate, bats (the BATS capability is verified in `hosts/vm4.md`, 2026-10-24T20:52Z). The Phase R review was already claimed in `claims/pr-5204-picocli-review-vm4-round-1-phase-r.md`.
- Input SHA: `7eb288a6f5777770d4472fc2c550b66e0d1cec12` (PR 5204 reviewed head, unmodified). Merge base `79b23df58daac14b92cc35f23b8b81f72098b4ba`. Worktree `/workspace/worktrees/pr-5204-r`, detached.
- Decision ladder: rung 2 (probe). Rung 1 has already settled the selector reading from source, so this run either reproduces the expected mismatch or retracts the claim with evidence.
- Command (run under `/workspace/tools/.gradle-vm4.lock`): `env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests --tests test_help.bats --rerun`. The `--rerun` flag forces this one BATS task, which the assignment's `--rerun-tasks` shape would extend to the whole build graph. If `--rerun` does not force the BATS run, record that and rerun with `--rerun-tasks`.
- Expected: assertion failures on the picocli wording in test_help.bats (lowercase `usage: bin/solr ...` expected where the legacy launcher prints `Usage: solr ...`). Failures must be attributable to the selector, not to compilation, startup, tooling, credentials or ports. Also recorded: the restart-help test is skipped under false.
- Log path: `/workspace/gates/logs/vm4/pr-5204/phase-r-negative-control/`. The JUnit XML is copied out of the worktree build directory before any later run clears it.
- Started: 2026-10-24T20:53:00Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pr-5204-phase-r-negative-control-vm4-round-1@2026-10-24T20:53:00Z.
- Constraints: no push in this claim (the probe changes nothing in git). No public PR or JIRA comment. Disk guard: stop and record if `/` free space drops below 2 GB or free inodes fall to 15% or less.

Heartbeat: 2026-10-24T20:53:00Z
