# Claim: critical-impl-14833-round-1 (vm2)

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: ACTIVE. Assignment `assignments/critical-impl-14833-round-1.md` (rank 7 from `reports/critical-implementation-triage-round-1.md`).
- Started: 2026-10-27T11:33Z (UTC).
- Capability tags used: implementation, premise-run.
- Plan: step 1, premise run: replay the ticket's request with `hl.fragmenter=regex` on a small index; confirm or kill that the highlight entry is empty when a match exists. Stop with a report if the premise fails. Step 2 only after the premise reproduces: narrow fix on fork branch `solr-14833-submit`, then request the gate.
- Constraints: no PR, no Jira or GitHub comment, no work outside `solr-14833-submit`. Commits as Nick Shanin, no trailers.
- Heartbeat: 2026-10-27T11:33Z
