# Skip record: SOLR-14756

- Ticket: SOLR-14756, "Sync of replicas are failing for two collection among 3". JIRA status Open, resolution none, fix versions none, created 2020-08-18, updated 2020-08-19. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14756-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14756-vm2.md).

## Ticket reading

Three collections on one Linux server, each with one shard and three replicas. Two replicas in two of the collections stay in recovery-failed state across restarts of Solr and ZooKeeper. The logs show "Error while trying to recover ... Replication for recovery failed" and a PeerSync failure. The report gives a partial stack trace and no Solr version, no logs beyond that, and no comments.

## Code reading

- solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java:226-260 (origin/main 498a09aea97): replicate(nodeName, core, leaderprops) performs the replication request to the leader and throws SolrException "Replication for recovery failed." at line 260 when the request does not succeed.
- RecoveryStrategy.java:414 and :721: replicate is called from the recovery paths (the leader-replication fallback). The PeerSync failure in the report leads to this fallback in the recovery flow, which the code read here does not trace further.
- The reason the replication request fails on the reporter's servers (network, leader state, disk, or the index on the leader) is not in the report and is not established by the code.

Commands at the pinned head:
- git grep -n 'Replication for recovery failed' origin/main -- solr/core/src/java: RecoveryStrategy.java:260.
- git grep -n 'replicate(' origin/main -- solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java: lines 226, 414, 721.
- git log --oneline --grep='SOLR-14756' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14756: none.

## Reproduction attempts

Not attempted. The report describes a three-collection, three-replica cluster where recovery fails after restarts, and the cause is not in the report. A reproduction would need a cluster with those collections and a way to make the leader's replication fail, which was not set up in this pass.

## Gap analysis

The failing throw is located (RecoveryStrategy.java:260), and the report's recovery path goes through the replication fallback. The reason the replication fails on the reporter's servers is not in the report. Without logs from the failed replication request on the leader, the cause cannot be separated from the environment.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: no commit names the ticket. close-cannot-reproduce does not hold: no reproduction was made. The missing piece is the leader-side log for the failed replication request (the reporter's logs around the PeerSync failure and the replication error), with the Solr version, so the failing step can be found and reproduced.

## Limits

This record does not reproduce the failure. It does not establish why the replication fails. It does not check the Solr version the reporter used.
