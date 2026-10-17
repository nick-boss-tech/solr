# Claim: lane coordinator, review rounds (Windows agent)

Status: CLOSED 2026-10-17 (batch close approved by Nick; no heartbeat within 2 hours; listed in reports/claims-stale-batch-close-2026-10-17.md)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Role: review rounds coordinator, per `material/coordinator-handoff.md` (Nick approved 2026-10-11).
- Capability tags: review, draft (coordination only; no build work of any kind).
- Owns: the review, draft and fidelity assignments under `assignments/`, their claims, and the reports under `reports/`. Posts and staffs review rounds, breaks large rounds into claimable slices, verifies fix lanes land, posts the follow-up round when a round closes, and hands gate-needing findings to the gate coordinator as gate job requests.
- Does not open, flip or close a PR, post any comment, edit a live PR body, or send mail. Those stay with the main agent after Nick approves.
- Standing limits: subagent cap of 6 to 7 across all hosts; queue buffer target of at least two unclaimed review or draft assignments; VM3 is down, so nothing is assigned to it; GitHub Actions is unavailable.
- Date: 2026-10-11.
- Heartbeat: 2026-10-11T14:53Z (coordinator claim taken).
- Heartbeat: 2026-10-11T20:17Z (refreshed; coordination is ongoing in this session: the mergeIds round 3 closed SATISFIED, the mergeIds fix review closed STILL OPEN, and scheduled checks run each cycle; vm2 had flagged the 14:53Z heartbeat as stale).
- Status: ACTIVE.

- Heartbeat: 2026-10-15T16:25:03Z (refreshed; round 1 wave run: standing calls, landing order, ungated and Groups C/D audits DONE; eDisMax, proof-shape and SolrCloud hold first rung PARTIAL; new ticket texts superseded; SOLR-4841 build and eDisMax patches blocked on branch-creation permission).
