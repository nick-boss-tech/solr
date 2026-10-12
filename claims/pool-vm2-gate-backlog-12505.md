# Claim: SOLR-12505 FetchStream join field name escaping fix, gate at the new head (vm2, gate backlog coordinator)

- Claimed: 2026-10-12T20:35Z by vm2. Job: gates/SOLR-12505-fetchstream-fix.md.
- Head at claim: 5f20e1171bc890509d76e4f5f7defcc4fc8c102c (solr-12505-submit). Production delta: FetchStream.java only, plus one new test in StreamDecoratorTest; the fix commits are pushed as a fast-forward after the gate is green.
- Capability tags: implementation (FetchStream and test), gate.
- Runner: gates/g12505-gate.sh, worktree /workspace/gates/solr-12505-gate (detached), log gates/logs/vm2/12505-run.out.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: CLAIMED by vm2.
