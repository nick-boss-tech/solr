# Skip record: SOLR-15305

- Ticket: SOLR-15305, Multiple nodes from same directory causes bizarre SolrCloud errors. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-04-01, updated 2021-04-12, no fix versions, no links, no comments. Spinoff from SOLR-15288.
- Verdict: keep-open.
- Draft branch: solr-15305-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and every cited name grepped at this head (detached worktree).
- Source: remediation redo of batch 03, item 15, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-2, 2026-10-04, confirmed-skip) cited the design rationale from audit evidence, not from this head; that rationale is not checked here.

## Ticket reading

Two Solr nodes started from the same solr home directory, with a replica placed on the second node. Restarting the second node leaves both replicas down, and the second replica stays ACTIVE after the second node is stopped. The repro is a shell script with `bin/solr -c -p 9000` and `-p 9001` against one ZooKeeper.

## Code reading

- solr/core/src/java/org/apache/solr/cloud/ZkController.java:2190-2240, `checkStateInZk(CoreDescriptor, Replica.State)`: waits up to 10 seconds for the replica named by `coreNodeName` to exist in its shard, and, when a state is given, for it to have that state. The method compares the core node name and the state. It does not compare the replica's node_name with this node's name. So at the pin there is no fail-fast guard on node_name in this method.
- The node name is written into replica properties at ZkController.java:1761 (`ZkStateReader.NODE_NAME_PROP`) and at :1864 and :2028. The replica that a restarted node reads back is whatever the cluster state holds for that core node name.

So the recovery path runs in code, and the two-node case goes through it unchanged. Whether the reporter's replicas end up in the states described is not shown by reading.

## History

- SOLR-15288 fixes are on the pin: 291f9b32c7 (2021-04-05, "Hardening NODEDOWN event") and ddf9dc7d21 (2021-04-11, "fix DOWNNODE issue for PRS collections"). They change DOWNNODE handling for PRS collections. Neither mentions the shared-directory case, so they are not shown to fix SOLR-15305.
- The audit's remark that a node-name guard was considered and rejected was not verified: no such guard exists at the pin, and the discussion that rejected it is not in this repository.

## Reproduction attempts

Not run. The repro needs a ZooKeeper, two Solr nodes from one solr home, and the collection steps in the ticket. This pass did not build or start a cluster.

## Gap analysis

The ticket names a setup (two nodes from one directory) that Solr does not document as supported, and asks for a fix that is a design choice: a fail-fast guard or support for shared directories. Reading does not settle which, and the reporter's two replica states are not reproduced. The ticket has had no comments since 2021.

## Verdict justification

keep-open: the attempts made are the code path reads above at the pinned head, the Jira read, and the history check of SOLR-15288. The missing piece is a run of the ticket's script on the pinned head (the two replica states after the second node's restart and stop), and a maintainer decision between a guard and documenting the setup as unsupported. No closing verdict is supported, and no PR is supported without the run and the decision.

Missing for a closing or PR verdict: (1) the run output with the two replica states, command and log path; (2) the design decision.

## Limits

This record does not reproduce the states. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
