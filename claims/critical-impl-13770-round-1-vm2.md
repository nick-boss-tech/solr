# Claim: critical-impl-13770-round-1 (vm2)

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: ACTIVE. Assignment `assignments/critical-impl-13770-round-1.md` (rank 10 from `reports/critical-implementation-triage-round-1.md`).
- Started: 2026-10-27T12:32Z (UTC).
- Capability tags used: implementation, premise-run.
- Plan: step 1, premise run: restart a node holding several indexes with tlog replay in progress, following the ticket's steps; confirm or kill that nested directories appear and recovery fails. Stop with a report if the premise fails. Step 2 only after the premise reproduces: narrow fix on fork branch `solr-13770-submit`, then request the gate.
- Constraints: no PR, no Jira or GitHub comment, no work outside `solr-13770-submit`. Commits as Nick Shanin, no trailers.
- Heartbeat: 2026-10-27T12:32Z
