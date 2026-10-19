# Skip record: SOLR-14648

- Ticket: SOLR-14648, "Creating TLOG with pure multiple PULL replica, leading to 0 doc count". JIRA status Open, resolution none, fix versions none, created 2020-07-14, updated 2020-07-15. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14648-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14648-vm2.md).

## Ticket reading

With a shard that has only PULL replicas, adding a new TLOG (or NRT) replica makes it leader; the new leader does a full replication, which replaces the existing PULL replicas' indexes with its own, so documents the PULL replicas held are lost. The reporter suggests the new TLOG should replicate from a PULL replica that has the latest index before it becomes leader. Two comments (2020-07-14): one maintainer says picking a PULL replica automatically is not safe; another suggests the TLOG should not become leader while it is behind the PULL replicas, and a third agrees that option (2) is safer.

## Code reading

- solr/core/src/java/org/apache/solr/cloud/ZkController.java:1462 (origin/main 498a09aea97): `boolean isTlogReplicaAndNotLeader = replica.getType() == Replica.Type.TLOG && !isLeader;`. The TLOG-specific handling of non-leader replicas is here; the path that decides a new TLOG replica with no TLOG or NRT peer should not lead, or should fetch from a PULL replica first, was not traced to the line that acts on it.
- The leader-election classes (LeaderElector, ShardLeaderElectionContextBase) contain no PULL-specific eligibility check at the pinned head (git grep for 'PULL' and 'isPull' returned no matches there).
- The recovery and replication paths that replace the index (RecoveryStrategy.java, ReplicateFromLeader.java) reference PULL, but the decision that makes a PULL-only shard's new TLOG the leader was not traced.

Commands at the pinned head:
- git log --oneline --grep='SOLR-14648' origin/main: no output.
- git grep -l -i 'PULL' origin/main -- solr/core/src/java/org/apache/solr/cloud: RecoveryStrategy.java, ReplicateFromLeader.java, ZkController.java, DeleteNodeCmd.java.
- git grep -n 'isTlogReplicaAndNotLeader' origin/main: ZkController.java:1462.

## History

- Commits on origin/main naming SOLR-14648: none.

## Reproduction attempts

Not attempted. The reproduction needs a cluster with one shard, several PULL replicas holding documents, and a new TLOG replica added after the TLOG and NRT replicas are removed; that is a multi-node test with timing, which was not set up in this pass.

## Gap analysis

The data-loss path runs from leader selection (no PULL check there) to the full replication that replaces the PULL indexes. The decision point that the comments propose (do not lead while behind PULL replicas, or fetch from a PULL replica first) is not present in the code read here, but whether the current code avoids the loss by another route was not traced.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: no commit names the ticket (bounded --grep, no output). close-cannot-reproduce does not hold: no reproduction was made, and the code path that decides the leader is not fully traced. The missing pieces are (1) the trace from new-TLOG leader selection to the full replication (ZkController and the recovery path), and (2) a cluster reproduction of the loss.

## Limits

This record does not reproduce the data loss. It does not trace the leader-selection path to its end. It does not evaluate the proposed options.
