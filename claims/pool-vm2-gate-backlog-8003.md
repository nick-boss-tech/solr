# Claim: SOLR-8003 gate at head 85a8d32339 (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T19:09:44Z by vm2. Job: gates/SOLR-8003.md (head 85a8d32339394fa58a1f56969cd0e4f1db00fbb8, fork tip verified by ls-remote at claim time).
- Capability tags: gate. Runner: gates/g8003-gate.sh on vm2 (worktree /workspace/gates/solr-8003-gate, detached). Seeds 8003C0FFEE8003 and 8003C0FFEE8004.
- Stops before any push. A tidy change, an unexpected proof-leg shape, or a failed focused run is recorded in gates/SOLR-8003.md and escalated; not relaunched.
- The PR 5140 body edit (Decisions row, pending Nick) is not part of this claim.
- Status: ESCALATED, NOT GREEN (2026-10-15T19:40:41Z). Proof leg failed with an unexpected shape: testGlobJsonTransformer's 400 reads `undefined field: "su"`, not "Error parsing fieldname". Focused runs and tidy, compile and module check passed. Recorded in gates/SOLR-8003.md; no receipt, no DONE, not relaunched. Worktree /workspace/gates/solr-8003-gate kept for the main agent.
