# Claim: pr-5204-phase-g-final-gate-attempt2-vm4-round-1

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: LIVE.
- Supersedes: `claims/pr-5204-phase-g-final-gate-vm4-round-1.md` for legs 2 to 4 only. Leg 1 of that claim passed (27 of 27, `gate-leg1-focused`) and is kept. Leg 2 attempt 1 is incomplete: the gate process ended at about 00:52:36Z with no result and no test failed. Its log is kept at `gate-leg2-legacy-full-attempt1-incomplete/` with an INCOMPLETE.txt.
- Input head: `daa29b06e035b57fc5098180113f764dff9cf57d`, unchanged. Worktree `/workspace/worktrees/pr-5204-impl`.
- Legs in this claim: (2) default legacy full suite with SOLR_PICOCLI unset; (3) picocli full suite; (4) tidy and `:solr:packaging:check -x test`.
- Runner: `/workspace/tmp/pr5204/run-gate2.sh`, started detached with setsid and nohup under `/workspace/tools/.gradle-vm4.lock`.
- Logs: `/workspace/gates/logs/vm4/pr-5204/gate-leg2-legacy-full/`, `gate-leg3-picocli-full/`, `gate-leg4-checks/`. Status file: `gate-status`.
- Started: the log's `start_utc` line is authoritative.
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 2 for legs 2 to 4. Idempotency key: pr-5204-phase-g-final-gate-attempt2-vm4-round-1.
- Constraints: no push from the gate, no public comment. The receipt is written only if every leg in both claims passes.

Heartbeat: see the log's `start_utc` line
