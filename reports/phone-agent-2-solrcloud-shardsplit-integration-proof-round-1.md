# SolrCloud split-child construction-state integration proof — Round 1

**Verdict: BLOCKED / INCOMPLETE; not a confirmed counterexample.** The deterministic ZooKeeper-backed test reached the target state through real SolrCloud APIs, and `DELETESHARD` completed. However, the follow-up left `SPLITSHARD` in `RUNNING` after the test's 150-second terminal-status wait, and the evidence does not establish whether a child being deleted should still count as a required split child. The result demonstrates reachability, not a confirmed contract violation or a completed split.

## Observed state and contract boundary

The two-node `SolrCloudTestCase` staged a split at the first child-replica request, then issued `DELETESHARD` for the sibling child. With the split released, the live ZooKeeper state observed by the test was exactly: parent `shard1=INACTIVE`, surviving child `shard1_1=ACTIVE`, and the child being deleted `shard1_0=CONSTRUCTION`. `DELETESHARD` then reported `completed`.

The linked [failure-pilot report](phone-agent-2-solrcloud-shardsplit-failure-pilot-round-1.md) records the candidate safety invariant that the parent remains routable until every required split child is ready. At the pinned source, `ReplicaMutator.java:528–559` considers same-parent fellow slices only while they are `RECOVERY`; `ReplicaMutator.java:560–616` prepares the parent `INACTIVE` and child `ACTIVE` update once the checked child replicas are active. `SplitShardCmd.java:740–794` moves child slices into `RECOVERY` before creating additional replica cores, while `DeleteShardCmd.java:98–126` puts a deleting child into `CONSTRUCTION` before replica cleanup. This explains how the state can arise, but does not settle the assignment's explicit question: whether a child already being deleted should remain a required split child under Solr's intended contract. The observed state is therefore not labeled a bug.

The original test run's JUnit suite passed, but its raw log also prints the `SPLITSHARD` request status as `running`; that is not evidence of terminal completion. In the same-seed follow-up, the test again reached the state and `DELETESHARD` completed, but `SPLITSHARD` was still `RUNNING` after 120 seconds plus a further 30 seconds. The terminal-status assertion failed. Client/ZooKeeper shutdown messages reported during test-fixture teardown follow that failure and are not treated as its cause.

## Patch and reproducibility

Source snapshot/base: `74197140a666d4f0396b68484c2bf9df7e79221e`.

Fork branch: `solrcloud-shardsplit-integration-proof-20261028`, pushed head `5449b63d5b46eb6048900b9810acd565e3e74b23`.

The branch contains only the requested three source files and the new integration test:

- `solr/core/src/java/org/apache/solr/cloud/api/collections/SplitShardCmd.java:775–789` adds the synchronization point before the first child replica request.
- `solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteShardCmd.java:115–127` adds the synchronization point after the child state update.
- `solr/core/src/java/org/apache/solr/util/TestInjection.java:142–147, 209–225, 557–584` defines and resets the latches; the hook returns immediately when its release latch is unset.
- `solr/core/src/test/org/apache/solr/cloud/api/collections/ShardSplitDeleteInterleavingTest.java:34–178` drives the real cluster APIs, asserts the observed states, waits for request statuses, and resets the hooks in `finally`.

No normal production-path behavior change is intended: the added hooks are inert unless the test explicitly sets their latches. No production fix, PR, Jira action, or upstream write was made.

The focused command used for the initial run was:

```text
bash /workspace/solr-coordination-ui/pool/bin/sweep-run.sh /dev/shm/solrcloud-shardsplit-integration-proof-20261028 /workspace/gates/logs/phone-agent-2/solrcloud-shardsplit-integration-proof-round-1 solrcloud-shardsplit-integration-proof-round-1-test :solr:core:test --tests org.apache.solr.cloud.api.collections.ShardSplitDeleteInterleavingTest.testDeleteOneRecoveryChildWhileSplitActivatesTheOther
```

The JUnit XML recorded randomized seed `35807FE5D31FEF77`. The follow-up used that same seed explicitly:

```text
bash /workspace/solr-coordination-ui/pool/bin/sweep-run.sh /dev/shm/solrcloud-shardsplit-integration-proof-20261028 /workspace/gates/logs/phone-agent-2/solrcloud-shardsplit-integration-proof-round-1 solrcloud-shardsplit-integration-proof-round-1-followup-test -Dtests.seed=35807FE5D31FEF77 :solr:core:test --tests org.apache.solr.cloud.api.collections.ShardSplitDeleteInterleavingTest.testDeleteOneRecoveryChildWhileSplitActivatesTheOther
```

## Durable JUnit and log evidence

Raw evidence is retained under `/workspace/gates/logs/phone-agent-2/solrcloud-shardsplit-integration-proof-round-1/`:

- `solrcloud-shardsplit-integration-proof-round-1-test.xml/TEST-org.apache.solr.cloud.api.collections.ShardSplitDeleteInterleavingTest.xml`: one test, zero failures, zero errors; it records seed `35807FE5D31FEF77`. The companion `solrcloud-shardsplit-integration-proof-round-1-test.log` reports `BUILD SUCCESSFUL in 3m 30s`, logs the exact shard-state observation and completed `DELETESHARD`, and logs `SPLITSHARD` as `running`.
- `solrcloud-shardsplit-integration-proof-round-1-followup-test.xml/TEST-org.apache.solr.cloud.api.collections.ShardSplitDeleteInterleavingTest.xml`: one test, one assertion failure, zero errors. The failure is `SPLITSHARD did not reach a terminal status: running`. The companion `solrcloud-shardsplit-integration-proof-round-1-followup-test.log` logs the same shard-state observation, completed `DELETESHARD`, the additional 30-second wait after the 120-second wait, and `BUILD FAILED in 3m 11s`.
- The exact wrapper invocations are preserved in the corresponding `*.monitor-wrapper.command.txt` files.

**Acceptance remains incomplete.** The interleaving is reachable, so this is not a non-reproduction. But the split did not reach a terminal status in the follow-up, and the deleted-child readiness contract remains unresolved. Do not call the split completed or classify the issue as confirmed.
