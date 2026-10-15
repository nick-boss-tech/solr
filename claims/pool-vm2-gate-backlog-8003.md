# Claim: SOLR-8003 gate at head 85a8d32339 (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T19:09:44Z by vm2. Job: gates/SOLR-8003.md (head 85a8d32339394fa58a1f56969cd0e4f1db00fbb8, fork tip verified by ls-remote at claim time).
- Capability tags: gate. Runner: gates/g8003-gate.sh on vm2 (worktree /workspace/gates/solr-8003-gate, detached). Seeds 8003C0FFEE8003 and 8003C0FFEE8004.
- Stops before any push. A tidy change, an unexpected proof-leg shape, or a failed focused run is recorded in gates/SOLR-8003.md and escalated; not relaunched.
- The PR 5140 body edit (Decisions row, pending Nick) is not part of this claim.
- Status: DONE (2026-10-15). The main agent settled the escalation: the proof leg stands by content (both glob tests fail at the merge-base because a glob cannot take a raw value transformer there; the `su*` form's 400 reads `undefined field: "su"`), so the gate is GREEN at 85a8d32339. Receipt: receipts/SOLR-8003.md (top section). Verdict recorded at the end of gates/SOLR-8003.md.
