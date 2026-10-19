# Skip record: SOLR-15997

- Ticket: SOLR-15997, "Sporadic Zk BadVersionException in overseer when perReplicaState is flipped to true". JIRA status Open, resolution none, fix versions none, created 2022-02-09, updated 2022-05-12. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-15997-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15997-vm2.md).

## Ticket reading

Steps from the report: create a few hundred collections, restart all nodes, set perReplicaState=true with a MODIFY collection request, and occasionally the Overseer logs a ZooKeeper BadVersionException. The report has no stack trace, no logs, and no comments (0 comments in JIRA). The ticket names no test class.

## Code reading

- solr/core/src/java/org/apache/solr/cloud/overseer/CollectionMutator.java:118-133 (origin/main 498a09aea97): for a MODIFY that sets perReplicaState, the mutator reads the collection's per-replica state with PerReplicaStatesOps.fetch(coll.getZNode(), zkClient, null), then calls PerReplicaStatesOps.enable(coll, prs) (or disable). The collection object coll is the one read before this call; it carries the znode version the update will be checked against.
- solr/core/src/java/org/apache/solr/cloud/overseer/ZkStateWriter.java:349: the catch for KeeperException.BadVersionException in the write path. The field comment at line 74 says a BadVersionException disables further operations on that writer instance until it is reset.
- Not read in this pass: PerReplicaStatesOps.enable and fetch (solr/solrj or common cloud package), to establish whether enable() changes the collection znode's version before the state write, which is the mechanism the report's symptom suggests.

Commands at the pinned head:
- git grep -n 'BadVersionException' origin/main -- solr/core/src/java/org/apache/solr/cloud: Overseer.java:476, ZkStateWriter.java:349, DistributedClusterStateUpdater.java:504, ZkController.java:2699, ZkShardTerms.java:360, and collection API classes. The Overseer hit at line 476 was not read in this pass.
- git grep -n -i 'perReplicaState' origin/main -- solr/core/src/java/org/apache/solr/cloud/overseer: CollectionMutator.java hits as above.

## History

- Commits on origin/main naming SOLR-15997: none.
- Commits touching the per-replica flip path: not traced with git log -S in this pass.

## Reproduction attempts

Not attempted. The symptom needs a multi-node cluster with a few hundred collections, a restart of all nodes, and a timed MODIFY. That is not a single unit test and was not run in this pass.

## Gap analysis

The code path shows the flip reads the collection state, then writes it back with a version check. A BadVersionException therefore means the collection node changed between the read and the write. Which writer changed it (per-replica state updates, another Overseer message, or the restart) is not established. The report gives no log to tell them apart.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. The missing pieces are (1) the Overseer log and stack trace from an occurrence, and (2) a reproduction on a multi-node cluster or a read of PerReplicaStatesOps.enable to confirm the mechanism. A close verdict needs a fix or a reproduction showing the case cannot happen; a pr-needed verdict needs the mechanism confirmed.

## Limits

This record does not reproduce the exception. It does not read PerReplicaStatesOps. It does not establish which writer races with the flip.
