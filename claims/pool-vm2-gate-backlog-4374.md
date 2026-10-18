# Claim: SOLR-4374 digit-leading names in fl gate at the head (vm2, gate backlog coordinator)

Status: CLOSED 2026-10-17 (batch close approved by Nick; no heartbeat within 2 hours; listed in reports/claims-stale-batch-close-2026-10-17.md)

- Claimed: 2026-10-15T01:20ZZ by vm2. Job: gates/SOLR-4374.md (head f0e23fca1b7, fork tip verified by ls-remote).
- Capability tags: gate (no push; the job's on-green steps are a receipt and a job update).
- Runner: gates/g4374-gate.sh, worktree /workspace/gates/solr-4374-gate (detached). Seed 4374C0FFEE4374.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: GATE RAN, NOT GREEN (2026-10-15T01:55Z). Log: logs/vm2/queue-g4374-gate-20261015T012048Z.out. Tidy, Error Prone compile, focused 17 of 17 and core check all rc 0. Proof leg rc 1 with the expected shape (17 tests, 1 failure, testDigitLeadingFieldName). The coordinator rule needs every rc 0, so no receipt, no DONE. Left for the main agent.

- 2026-10-15T02:33ZZ vm2 gate backlog coordinator: DONE. Green at f0e23fca1b7 (head 17 of 17; proof leg 1 failure as expected).

STALE: no live work by vm2 since 2026-10-18; marked by the vm2 coordinator so the pool tick does not resume it. Work not finished, see the job or assignment file.
