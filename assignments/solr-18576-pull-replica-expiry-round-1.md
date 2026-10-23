# Pool assignment: SOLR-18576, the other expireSession caller (TestPullReplicaErrorHandling)

Status: OPEN, not claimed. A host takes it by writing `claims/<host>-solr-18576-pull-replica-expiry-round-1.md`.

Origin: the SOLR-18576 receipt (`receipts/SOLR-18576.md`, section "Other `expireSession` and `expireZkSession` callers"), written 2026-10-23 by vm5 after PR #5206 was opened. PR #5206 fixes `BasicDistributedZk2Test` only. This assignment checks whether the same race can hit the one other caller that indexes right after an expiry.

- Capability tags: premise-run, gate.
- Category: Solr core, SolrCloud tests.
- Intended host: any pool Linux host. Builds go through `pool/bin/sweep-run.sh` under the host's build lock.
- Base: apache/solr `main` at `79b23df58da`.
- Decision ladder (`material/decision-ladder.md`): the question is settled at rung 1 if the code answers it; rung 2 is a run. The open question is handed off, not settled.

## The question

`solr/core/src/test/org/apache/solr/cloud/TestPullReplicaErrorHandling.java`, method `testPullReplicaDisconnectsFromZooKeeper` (starts at line 218):

```java
cluster.expireZkSession(jetty);   // line 228
addDocs(30);                      // no wait; addDocs is a plain cluster add + commit
waitForState("Expecting node to be disconnected", ...);
```

`jetty` is the node hosting a PULL replica. `ChaosMonkey.expireSession` returns before the expired-session event reaches Curator, so the add can race the expiry the same way it did in `BasicDistributedZk2Test` (CI on PR #5194: 503 "Cannot talk to ZooKeeper - Updates are disabled"). The race can only fail this test if the add is routed to the expired node.

## Items

1. **Routing (ladder rung 1: read the code).** Answer from the code, with citations at `79b23df58da`: (a) how `CloudSolrClient` routes an add in this collection, including whether a PULL replica can receive an add directly or only through a leader; (b) which replicas of `collection1` are leaders during the test before line 228, and whether the expired node is ever a leader or a routing target there. Output: a yes or no, with the file and line for each claim.
2. **Probe (ladder rung 2), only if item 1 leaves the answer open or says "yes".** On local disk, not the network file system (the SOLR-5735 finding: a pull replica fails to copy the index there with "Too many links"). Run `TestPullReplicaErrorHandling.testPullReplicaDisconnectsFromZooKeeper` at `79b23df58da` with `-Ptests.iters=10`, recording pass or fail for each seed. For any failure, record the seed and whether the failing line is 228 with "Updates are disabled" or a later line. Do not change any code for this item.

If item 1 answers "no" (the expired node is never a routing target for an add), stop after item 1. Record that and skip item 2.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Change `TestPullReplicaErrorHandling` at line 228 | decided: no, in this assignment | findings only; a recommendation goes in the report | Nick |
| Open a PR or push a branch | decided: no | if a fix is recommended, Nick decides whether it becomes a PR | Nick |
| Jira or GitHub posts | decided: no | the SOLR-18576 receipt is the only record | Nick |
| Iteration count for item 2 | default: 10 seeds | item 1 may shorten the list; a run that fails to start is an environment refusal and is recorded as such, not as a test result | claimant |

No row may leave the choice to the claimant.

## Deliverable

`reports/solr-18576-pull-replica-expiry.md`:
- The answer to item 1, with each citation as a blob link at `79b23df58da`.
- For item 2 (if run): the seed list with pass or fail per seed, the failing line for any failure, and the log path.
- A recommendation: no change, or a wait before line 228, stated with its evidence.

Mark the claim DONE in the same push, and add a line to `receipts/SOLR-18576.md` with the outcome.

## Limits

- No Jira or GitHub posts or comments. No product code change. No force push. No PR.
- Commit as Nick Shanin with no trailers. No em dashes in anything authored.
