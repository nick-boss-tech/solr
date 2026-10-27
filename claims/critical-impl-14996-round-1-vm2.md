# Claim: critical-impl-14996-round-1 (vm2)

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: ACTIVE. Assignment `assignments/critical-impl-14996-round-1.md` (rank 5 from `reports/critical-implementation-triage-round-1.md`).
- Started: 2026-10-27T10:31Z (UTC).
- Capability tags used: implementation, premise-run.
- Plan: step 1, premise run at the ticket's query with and without `{!collapse}` on a fresh build; stop with a report if the premise fails. Step 2 only after the premise reproduces: narrow fix on fork branch `solr-14996-submit`, then stop and request the gate.
- Constraints: no PR, no Jira or GitHub comment, no work outside `solr-14996-submit`. Commits as Nick Shanin, no trailers.
- Heartbeat: 2026-10-27T10:31Z
