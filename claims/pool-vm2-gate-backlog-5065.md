# Claim: SOLR-5065 gate at the live head (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T20:17:00Z by vm2. Job: gates/SOLR-5065-live-head-gate.md (head ab894a996c8aa9825c4ac5af38536aec1ee865eb, fork tip verified by ls-remote at claim time).
- Capability tags: gate. Runner: /workspace/gates/g5065-gate.sh on vm2, using the shared runner /workspace/gates/lib/gate-run.sh. Worktree: /workspace/gates/solr-5065-gate. Seed 5065C0FFEE5065.
- No implementation step and no branch push, because the gate produces no commit under this job. A tidy change (GATE INVALID), a proof-leg verdict that fails by content, or a failed focused run is recorded in the job file and escalated; not relaunched.
- Status: DONE 2026-10-15. GATE GREEN at ab894a996c8aa9825c4ac5af38536aec1ee865eb by main-side verdict: the proof leg's one failure is the premise assertion by content (`expected:<pdouble> but was:<string>` on base 0cc328310f8f), corroborated by a main-side re-run of the proof leg on vm1 from fresh JUnit XML (2 tests, 1 failure, the same assertion). All head legs rc=0. Verdict and counts recorded in gates/SOLR-5065-live-head-gate.md and receipts/SOLR-5065.md. Claim closed.
