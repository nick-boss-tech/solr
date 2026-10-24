# Claim: pr-5204-phase-r-post-probe-reaper-vm4-round-1

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: LIVE.
- Supersedes: `claims/pr-5204-phase-r-post-probe-lsof-vm4-round-1.md`. That run (log `/workspace/gates/logs/vm4/pr-5204/phase-r-post-probe-lsof/`) repeated the selector failure at test 1 (`test_post.bats` line 42, picocli wording under false). Its teardown failed again, at `solr stop --all` after a 180-second wait, followed by a forced kill that did not succeed.
- Root cause of the teardown failure, observed on this VM: PID 1 is a `python3` process that does not reap orphaned children. Solr's JVM is an orphan after `solr start` returns, so it becomes a zombie under PID 1. The stop script's liveness check treats the zombie as alive and waits. The Solr process had already exited (its log shows `Exiting JVM`). This is an environment defect, not a test or product result. About 50 zombie Solr and flock processes from earlier runs are still present under PID 1.
- Environment fix recorded before this run: `/workspace/tools/reap-run.py`, a wrapper that sets `PR_SET_CHILD_SUBREAPER` and reaps its descendants until the wrapped command exits. It is wrapped around the whole Gradle command so the wrapper outlives the Solr JVM. A wrapper around only `solr start` and `solr stop` was tried and does not work, because the Solr JVM is reparented to PID 1 when that wrapper exits. Checked on 2026-10-24: a manual start on port 8996 became ready in 4.5 seconds under the wrapper.
- Input SHA: `7eb288a6f5777770d4472fc2c550b66e0d1cec12`, unmodified. Worktree `/workspace/worktrees/pr-5204-r`. Tags used: gate, bats. Decision ladder: rung 2.
- Command (under `/workspace/tools/.gradle-vm4.lock`, lsof on PATH): `python3 /workspace/tools/reap-run.py env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests -PdisableJsClient=true --tests test_post.bats --rerun --no-daemon`.
- Log path: `/workspace/gates/logs/vm4/pr-5204/phase-r-post-probe-reaper/`.
- Started: the log's `start_utc` line is authoritative. This claim is written after the prior run's result and before this run starts.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 3 for test_post on this head. Idempotency key: pr-5204-phase-r-post-probe-reaper-vm4-round-1.
- Constraints: no git push, no public comment. The result stands whatever it shows. Each failure is recorded, not retried.

Heartbeat: see the log's `start_utc` line
