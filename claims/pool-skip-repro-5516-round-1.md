# Claim: pool-skip-repro-5516-round-1

- Claimant host: vm4, matching a file in `hosts/`.
- Status: ACTIVE; mark DONE with the deliverable path when the work lands.
- Job or assignment: `assignments/pool-skip-repro-5516-round-1.md`.
- Slice: the whole assignment (one run of ChaosMonkeyNothingIsSafeTest at the pinned head, the sleep reading, the test reading, the deliverable).
- Capability tags used: sweep, review.
- Started: 2026-10-18T04:58:12Z (UTC).
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main, checked with ls-remote at claim time).
- Worktree: `/workspace/worktrees/pool-skip-repro-5516` (detached at the pinned head).
- Build environment: `/workspace/tools/vm4-env.sh` (JDK 21.0.12.1, GRADLE_USER_HOME under /workspace). `~/.pool-env` is not present on this host.
- Lock: `/workspace/tools/.vm4-gradle.lock` wraps `pool/bin/sweep-run.sh`, which takes `/workspace/tools/.gradle-vm4.lock` inside it, so manual and tick builds cannot overlap.
- Source changes: none. No PR, comment, JIRA or email.

Heartbeat: 2026-10-18T04:58:12Z (claim).
