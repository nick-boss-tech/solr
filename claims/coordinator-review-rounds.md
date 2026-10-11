# Claim: lane coordinator, review rounds (Windows agent)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Role: review rounds coordinator, per `material/coordinator-handoff.md` (Nick approved 2026-10-11).
- Capability tags: review, draft (coordination only; no build work of any kind).
- Owns: the review, draft and fidelity assignments under `assignments/`, their claims, and the reports under `reports/`. Posts and staffs review rounds, breaks large rounds into claimable slices, verifies fix lanes land, posts the follow-up round when a round closes, and hands gate-needing findings to the gate coordinator as gate job requests.
- Does not open, flip or close a PR, post any comment, edit a live PR body, or send mail. Those stay with the main agent after Nick approves.
- Standing limits: subagent cap of 6 to 7 across all hosts; queue buffer target of at least two unclaimed review or draft assignments; VM3 is down, so nothing is assigned to it; GitHub Actions is unavailable.
- Date: 2026-10-11.
- Heartbeat: 2026-10-11T14:53Z (coordinator claim taken).
- Status: ACTIVE.
