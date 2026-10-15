# Claim: SOLR-12705 gate at the live head (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T19:19:34Z by vm2. Job: gates/SOLR-12705-live-head-gate.md (head aa56b7b1be4c5491723c58f296e20c906d7b7334, fork tip verified by ls-remote at claim time).
- Capability tags: gate. Runner: /workspace/gates/g12705-gate.sh on vm2, using the shared runner /workspace/gates/lib/gate-run.sh. Seed 12705C0FFEE12705.
- Stops before any push. A tidy change (GATE INVALID), a proof-leg failure of an unexpected shape, or a failed focused run is recorded in the job file and escalated; not relaunched.
- Status: CLAIMED. The run ended 2026-10-15T20:11:01Z with proof rc=1 (fail-before leg), head rc=0, check rc=0. Awaiting the main-side proof verdict by content. Not relaunched.
