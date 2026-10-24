# Claim: pr-5204-phase-r-post-probe-lsof-vm4-round-1

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: LIVE.
- Supersedes: `claims/pr-5204-phase-r-post-probe-vm4-round-1.md`. That run (log `/workspace/gates/logs/vm4/pr-5204/phase-r-post-probe/`) recorded two things. Test 1, "Check help command" at `test_post.bats` line 42, failed on the picocli wording under `SOLR_PICOCLI=false`. That is the second call site of the selector finding. Test 13, `teardown_file`, failed because `solr stop --all` could not find Solr: the BATS output says "Please install lsof", and this VM has no lsof and no root to install it. That is an environment failure, not a test result about the selector.
- Environment fix recorded before this run: lsof 4.99.7 from conda-forge in a private prefix, `/workspace/tools/lsof-env` (`micromamba create ... -c conda-forge --override-channels lsof`). Put it on PATH for this run only. The Solr start and stop capability is verified by this run's own start and stop (a manual start in `/workspace/tmp/pr5204/` on port 8999 reached Jetty but did not report ready in 170 seconds; that manual check is not evidence and is not relied on here).
- Why this is a new run and not a retry for green: the first run had a real selector failure and an environment failure. This run keeps the same head, the same command, and the same selector expectation. Its result stands on its own, whatever it shows.
- Input SHA: `7eb288a6f5777770d4472fc2c550b66e0d1cec12`, unmodified. Worktree `/workspace/worktrees/pr-5204-r`.
- Tags used: gate, bats. Decision ladder: rung 2 (probe).
- Command (under `/workspace/tools/.gradle-vm4.lock`, with `/workspace/tools/lsof-env/bin` first on PATH): `env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests -PdisableJsClient=true --tests test_post.bats --rerun --no-daemon`.
- Log path: `/workspace/gates/logs/vm4/pr-5204/phase-r-post-probe-lsof/`.
- Started: 2026-10-24T21:25:00Z (UTC), approximate; the log's `start_utc` line is authoritative.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 2 for test_post on this head. Idempotency key: pr-5204-phase-r-post-probe-lsof-vm4-round-1@2026-10-24T21:25:00Z.
- Constraints: no git push, no public comment.

Heartbeat: 2026-10-24T21:25:00Z
