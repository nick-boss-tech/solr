# Claim: SOLR-6831 omitted-refinement gate at the pushed head (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T05:47ZZ by vm2. Job: gates/SOLR-6831-omitted-refinement-round-2.md (pool commit d79f6bf9e9); assignment assignments/pool-solr-6831-omitted-refinement-round-2.md.
- Head: 3e07d801df598573823cc997bbabb848fa2b1cfb (solr-6831-submit, ls-remote matched by the runner at start).
- Capability tags: gate. Runner: gates/g6831-r2-gate.sh, worktree /workspace/gates/solr-6831-r2 (detached). Seed 6831C0FFEE6831. Proof leg against production 2ee19dc646 (FacetComponent.java).
- The runner pushes nothing. Fork pushes and PR body edits are made by the coordinator after the result is read.
- Status: FAILED at 3e07d801df598573823cc997bbabb848fa2b1cfb: focused TestQueryLimits head leg 8 tests, 1 failure (testPivotFacetRefinementStopsWhenOmittedPartialResults, SolrServerException: No live SolrServers available). Not relaunched; left for the main agent. Details in gates/SOLR-6831-omitted-refinement-round-2.md run record.
