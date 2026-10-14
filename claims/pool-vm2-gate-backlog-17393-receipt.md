# Claim: SOLR-17393 receipt at the reviewed head (gate backlog coordinator)

- Claimed: 2026-10-14T13:55Z by vm2. Job: gates/SOLR-17393-receipt.md.
- Head: 626241e647e5fcc5ac6cc485f6a96d4ffe12e00a (solr-17393-submit, confirmed by ls-remote). No fork push in this job.
- Capability tags: gate.
- Runner: /workspace/gates/g17393-gate.sh. Worktree: /workspace/gates/solr-17393-gate (detached at the head).
- Status: DONE (main agent ruling, 2026-10-14): GATE GREEN at the head. The run recorded below finished 14:09:14Z; its pre-fix leg rc=1 is the premise failure by content (see the ruling appended to gates/SOLR-17393-receipt.md); receipt written at receipts/SOLR-17393.md.

## Gate run record (gate backlog coordinator, 2026-10-14)

- Run: GATE START 13:47:14Z, GATE END 14:09:14Z at head 626241e647e5 (log logs/vm2/queue-g17393-gate-20261014T134714Z.out).
- rc: changelog 0, tidy 0 (tree clean), compile 0, pre-fix 1 (expected nonzero: SuggestComponentMergeTest testEqualWeightsDoNotDependOnShardResponseOrder fails on base, 1 failure), head 0 (DistributedSuggestComponentTest 1, SuggestComponentMergeTest 2, SuggestComponentTest 12, 0 failures), core check 0.
- Not GREEN under the all-rc-zero rule because of the expected-nonzero pre-fix leg. Not pushed, not marked DONE, not relaunched. Left for the main agent.
