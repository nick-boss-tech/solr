# Claim: critical-impl-17291-round-1 (vm2)

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: ACTIVE. Assignment `assignments/critical-impl-17291-round-1.md` (rank 6 from `reports/critical-implementation-triage-round-1.md`).
- Started: 2026-10-27T11:03Z (UTC).
- Capability tags used: implementation, premise-run.
- Plan: step 1, premise run: a replica on a configuration with the deletion policy unset, confirm or kill that `openNewSearcher` receives a null policy. Stop with a report if the premise fails. Step 2 only after the premise reproduces: narrow fix on fork branch `solr-17291-submit`, then request the gate.
- Constraints: no PR, no Jira or GitHub comment, no work outside `solr-17291-submit`. Commits as Nick Shanin, no trailers.
- Heartbeat: 2026-10-27T11:03Z
