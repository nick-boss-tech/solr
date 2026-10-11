# Claim: lane coordinator, gate backlog (vm2)

- Claimed by: vm2 (queue monitor), Claude Code on this host, working for Nick Shanin
- Role: gate backlog coordinator, per `material/coordinator-handoff.md` (Nick approved 2026-10-11).
- Capability tags: gate, premise-run, bats (coordination only; gate work is claimed per job).
- Owns: gate queue sequencing, `gates/` job files, the shared backlog list `assignments/pool-shared-gate-backlog-buffer.md`, gate-claim heartbeats, receipt and DONE verification.
- Does not open, flip or close a PR, post any comment, send mail, or relaunch a gate that executed tests and failed. Those stay with the main agent.
- Standing limits: subagent cap of 6 to 7 across all hosts; queue buffer target of at least 3 unclaimed gate jobs; VM3 is down, nothing is sequenced to it; GitHub Actions is unavailable.
- Date: 2026-10-11.
- Started: 2026-10-11T15:01Z (UTC).
- Heartbeat: 2026-10-11T15:01Z (UTC) (coordinator claim taken; no build running on vm2).
- Status: ACTIVE.
