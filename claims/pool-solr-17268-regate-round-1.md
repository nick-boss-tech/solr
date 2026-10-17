# Claim: SOLR-17268 full re-gate on the corrected test head (vm2)

- Claimant host: vm2 (Linux). Assignment: assignments/pool-solr-17268-regate-round-1.md. Gate plan: gates/SOLR-17268.md (carried in at 73acc6da8f).
- Slice taken: the whole re-gate, steps 0 to 5 and both proof legs, with the focused counts and the receipt on green.
- Capability tags used: gate.
- Head: solr-17268-submit at 9993394741edbd331e1f38576ec039b1c957b4b1 (ls-remote matched at claim time). Base 714d6ceff852. Seed 17268C0FFEE17268.
- Runner: gates/g17268-regate.sh (shared runner gates/lib/gate-run.sh plus a Leg B step). Leg B runs after the shared steps, not before step 4, so the order differs from the plan. The record will say so.
- Source changes: none. No PR, comment, JIRA or email.

Heartbeat: 2026-10-17T22:00Z (claim).
