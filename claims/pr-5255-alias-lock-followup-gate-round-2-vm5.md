# Claim: PR 5255 alias-lock follow-up Linux gate, round 2 (vm5)

- Claimant host: vm5, matching `hosts/vm5.md`. Tags: gate, premise-run, review.
- Started: 2026-10-28T18:46:45Z (UTC), preflight start.
- Job: `gates/pr-5255-alias-lock-followup-round-2.md` (OPEN, superseding round 1). Round 1 job `gates/pr-5255-alias-lock-followup-round-1.md` is unchanged.
- Fork: `nick-boss-tech/solr`, branch `solr-18589-submit`. Gate head `c09411a16cca5ca8948db9e6a5232756f1b7991b`, one commit above round 1's `fd49245a38ba779f78840477dbf6db34b92c5f6b` (tidy-only repair, pushed fast-forward).
- Preflight: tidy at `fd49245a38ba779f78840477dbf6db34b92c5f6b` (`./gradlew --no-daemon tidy` under `flock /workspace/tools/.gradle-vm5.lock`) exited 0 in 3m 22s and changed one file, `AliasCollectionApiLockTest.java` (2 insertions, 4 deletions). Recorded as a gate repair; log `/workspace/gates/logs/vm5/pr-5255-alias-lock-followup-round-1/preflight-tidy.log`. The inode guard in `pool/bin/gate-preflight.sh` was not used because `/workspace` reports 0 inodes (NFS); the same Gradle command ran directly under the host lock per `AGENTS.md`.
- Host toolchain: JAVA_HOME `/workspace/jdk/jdk-21.0.12.1+1`, GRADLE_USER_HOME `/workspace/gradle-home`, `NODE_ENV` unset.
- Build lock: `/workspace/tools/.gradle-vm5.lock`, one Gradle build at a time.
- Worktrees: positive `/workspace/worktrees/gate-pr5255-vm5` (detached at the round 1 head, to be moved to `c09411a16c`); fail-before worktree to be created at `1edd6a113b2a9e1b029f509c9279f4fb58683bba`.
- Logs: `/workspace/gates/logs/vm5/pr-5255-alias-lock-followup-round-1/`.
- Deliverables: `receipts/PR-5255-alias-lock-followup.md` and `reports/pr-5255-alias-lock-followup-gate-round-2-vm5.md`.

Status: CLAIMED, running.
Nothing public (no PR, comment, or JIRA action).
