# Claim: PR drift round 1, run records, items (a) to (d) (vm2, gate backlog coordinator)

- Claimed: 2026-10-13T09:05Z by vm2. Job: gates/pr-drift-round-1-run-records.md. All four items, one runner.
- Heads and merge-bases at claim (merge-base with upstream main):
  - (a) solr-18280-submit 0da92abd9e96616ed817a25da47078c753766119, merge-base 0d2a4649c79a404e9f4aa1bfe26b88c3bb37407c. No production delta, so the base leg has the head's production by construction; both legs still run.
  - (b) solr-7394-recovery 9857d9ee80297784bd7dea9968a5943fe4d7a3e6, merge-base 56ec140e3636d5f4150fa87fbf7103529536ac99. Production delta: RecoveryStrategy.java, ShardTerms.java, ZkShardTerms.java, TestInjection.java.
  - (c) solr-18178-verify b75e7d4d3c464ceeb9f14fdaf7711212adcccac2, merge-base 0d2a4649c79a404e9f4aa1bfe26b88c3bb37407c.
  - (d) solr-11431-core-init-503 968fad873c4b35fff0605ae88dbecca16d1dfa2f (head run only).
- Base legs: the branch's test tree stays at head; non-test production files are reverted to the merge-base version (added files removed). No branch change and no push to any branch.
- Capability tags: gate (record runs only).
- Runner: gates/g-prdrift-gate.sh, worktree /workspace/gates/prdrift-records (detached), logs gates/logs/vm2/prdrift-*.log and XML copies under gates/logs/vm2/prdrift-xml/.
- Status: CLAIMED, not DONE. Base-leg failures are expected by content and are recorded, not relaunched.
