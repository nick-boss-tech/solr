# Claim: SOLR-7520 full gate at head 15cc9ba5a6 (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T23:06:44Z by vm2. Job: gates/SOLR-7520.md (re-pinned to 15cc9ba5a63cd79518a72bda1fc67539956f5c31 after the tidy-fix commit, fork tip verified by ls-remote at claim time).
- Capability tags: gate. Runner: /workspace/gates/g7520-gate.sh on vm2, using /workspace/gates/lib/gate-run.sh. Seed 7520C0FFEE752020.
- Stops before any push. A tidy change (GATE INVALID), a proof-leg failure of an unexpected shape, or a failed focused run is recorded in the job file and escalated; not relaunched.
- The PR 5138 body re-point is not part of this claim and waits for the owner's approval.
- Status: CLAIMED, runner queued.
