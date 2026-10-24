# Claim: pr-5204-phase-g-final-gate-vm4-round-1

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: LIVE.
- Job: `gates/SOLR-18566-pr5204-cli-review-daa29b06e035.md` (immutable). Assignment: `assignments/pr-5204-picocli-review-implementation-and-gate-vm4-round-1.md`, Phase G.
- Input head: `daa29b06e035b57fc5098180113f764dff9cf57d` on `nick-boss-tech/solr:solr-18566-pr5204-cli-review`, read back from the remote at claim time. Worktree `/workspace/worktrees/pr-5204-impl`, which is clean at this head.
- Tags used: gate, bats. Decision ladder: rung 2 (probe). Gate legs run in order and stop at the first failing leg, per the job's rule that a failing or skipped test is recorded and not relaunched.
- Legs, in order: (1) focused false-mode `test_help.bats` and `test_post.bats`; (2) default legacy full suite with `SOLR_PICOCLI` unset; (3) picocli full suite; (4) `tidy`, `:solr:packaging:check -x test`, `git diff --check` of the delta.
- Runner: one script, `/workspace/tmp/pr5204/run-gate.sh`, holds `/workspace/tools/.gradle-vm4.lock` for the whole gate. Each Gradle command is wrapped in `/workspace/tools/reap-run.py` and has `lsof` on PATH.
- Logs: `/workspace/gates/logs/vm4/pr-5204/gate-leg1-focused/`, `gate-leg2-legacy-full/`, `gate-leg3-picocli-full/`, `gate-leg4-checks/`.
- Started: 2026-10-24T21:48:09Z (UTC), the time of the pre-claim check. The leg logs' `start_utc` lines are authoritative per leg.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: pr-5204-phase-g-final-gate-vm4-round-1@2026-10-24T21:48:09Z.
- Constraints: no git push from the gate, no public comment, no PR. The receipt `receipts/SOLR-18566.md` is written only if every leg passes, and only by the gating host (vm4) after this claim's results are pushed.

Heartbeat: 2026-10-24T21:48:09Z
