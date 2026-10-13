# Claim: SOLR-15319 premise run, is the null-response path the SOLR-8051 case (vm2, gate backlog coordinator)

- Claimed: 2026-10-12T23:20Z by vm2. Job: gates/SOLR-15319-8051-premise.md.
- Head at claim: 4bda91f46fc18142809c919d7c563129fdf7fb96 (solr-15319-submit). Premise run only: no branch commit, no push. The 8051 test class ExactStatsCacheMergeTest is copied into the worktree, uncommitted.
- Capability tags: gate.
- Runner: gates/g15319-gate.sh, worktree /workspace/gates/solr-15319-gate (detached).
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: DONE. Run head rc=1 at 4bda91f46fc18142809c919d7c563129fdf7fb96: testShardWithoutResponseIsSkipped threw NullPointerException at ExactStatsCache.java:120 (null response in SolrResponse.getException). Log: logs/vm2/15319-head.log. The rc=1 is the predicted premise failure. Verdict SAME CASE recorded by the main side 2026-10-12 in gates/SOLR-15319-8051-premise.md and receipts/SOLR-15319.md. Not relaunched.
