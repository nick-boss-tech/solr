# Claim: SOLR-18541 fail-before for the two new review tests (vm2, gate backlog coordinator)

Status: CLOSED 2026-10-17 (batch close approved by Nick; no heartbeat within 2 hours; listed in reports/claims-stale-batch-close-2026-10-17.md)

- Claimed: 2026-10-13T22:46Z by vm2 (gate backlog coordinator). Job: gates/SOLR-18541-review-r1-fail-before-new-tests.md.
- Head: a93f9f6a997e748514cf2575a6e94c5f1c01f85e (combinedquerycomponent-merge-totals fork tip read by ls-remote at claim time). Fail-before leg: production CombinedQueryComponent.java reverted to merge-base d76fc14b9857e77b830d7a82514b28c523a239c2 in a scratch worktree only. No fork push, no PR touched.
- Capability tags: gate.
- Runner: /workspace/gates/g18541-review-r1-fail-before-new-tests.sh, worktree /workspace/gates/solr-18541-failbefore (detached). Log: logs/vm2/18541-review-r1-failbefore-*.log.
- Status: FAILED (finding), not DONE. Runner queued 2026-10-13T22:46Z; run 22:53Z to 22:59Z. Focused rc=1 with 8 of 10 failing as the premise records; the two new tests `skippedPartialShardWithoutCountClearsExactness` and `skippedPartialShardWithoutHitsClearsExactness` PASSED on the merge-base production, which the job treats as a finding. Recorded in gates/SOLR-18541-review-r1-fail-before-new-tests.md (Run record). Not relaunched; left for the main agent.
