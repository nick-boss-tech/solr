# Gate job: SOLR-7394 review pass 3 (unit 24, vm3)

Status: CLAIMED (claims/pool-newdev-critblock-review-round-1-solr-7394-recovery.md, 2026-10-21T10:00:01Z)

- Branch: `solr-7394-recovery`. Head: `a92a0d89b20908`. Base for the review: `56ec140e3636d5f4150fa87fbf7103529536ac99` (the branch's own merge base).
- Intended host: vm3. Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.cloud.RecoveryStrategyTest`, `org.apache.solr.cloud.ShardTermsTest`, `org.apache.solr.cloud.ZkShardTermsRecoveryFailureTest`, `org.apache.solr.util.TestTestInjection`. Seed: default.
- Diff scope against `56ec140e3636d5f4150fa87fbf7103529536ac99`: 9 files, 386 insertions, 3 deletions: changelog SOLR-7394.yml, RecoveryStrategy.java, ShardTerms.java, ZkShardTerms.java, TestInjection.java, and four test classes.
- Why: the pass 3 regate on the same head. Sign-off for unit 24. The mutation probe is recorded in pass 2.
- Runner: `/workspace/tools/gate-solr-7394-p1.sh`. Worktree `/workspace/worktrees/solr-7394-p1`. Log: `gates/logs/vm3/solr-7394-review-pass-3/`.

Links: Ticket or parent: SOLR-7394 (a live PR exists on apache/solr). Category: 11, SolrCloud and ZooKeeper. Siblings: none known. Supersedes: none.

## What to run

- Step 0: changelog YAML parse. Step 1: tidy. Step 2: Error Prone compile of `:solr:core` with `--rerun`.
- Step 4: the four focused classes, counted from fresh XML.
- Step 5: `:solr:core:check -x test`. The cloud tests are slow; this is the planned cost for the category.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Review base | decided: the branch's own merge base `56ec140e3636d5f4150fa87fbf7103529536ac99` | the round's 14c7aac0d1 would add unrelated main changes | vm3, rung 1 |
| Repair scope | default: WORKFLOW.md Gate repairs only | a finding outside that scope is recorded, not fixed | vm3, rung 4 |
