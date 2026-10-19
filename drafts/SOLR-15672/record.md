# Skip record: SOLR-15672

- Ticket: SOLR-15672, "Leader Election is flawed". JIRA status Open, resolution none, fix versions none, created 2021-10-03, updated 2022-01-20. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-15672-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15672-vm2.md).

## Ticket reading

The ticket, filed by a maintainer as a note rather than a work item, lists leader-election properties that the current code does not meet (for example, a single thread per elected leader running the leader process, and replicas re-entering the ZooKeeper election line regardless of failure). It proposes moving the election code into LeaderElector, so that one elector owns the election for a shard. Comments (7, 2021-10-03 to 2022-01-20) discuss Curator as a possible first step, and whether the ZooKeeper /collections/<c>/<shard>/leader node is redundant with the cluster state (dsmiley, 2022-01-20; the reply says it was an early prototyping idea). No commit names the ticket.

## Code reading

- solr/core/src/java/org/apache/solr/cloud/LeaderElector.java:52 (origin/main 498a09aea97): class LeaderElector exists; the proposal to consolidate election code into it was not carried out in the form the ticket describes (the election context classes are still separate).
- The shard leader node is still written by solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContextBase.java:69 (ZkStateReader.getShardLeadersPath(collection, shardId)) and read by solr/core/src/java/org/apache/solr/cloud/ZkController.java:1720 (zkClient.getData on the same path). So the node the 2022 comment called redundant is still in use.

Commands at the pinned head:
- git grep -n 'class LeaderElector\b' origin/main -- solr/core/src/java: LeaderElector.java:52.
- git grep -n 'getShardLeadersPath' origin/main -- solr/core/src/java: ShardLeaderElectionContextBase.java:69, ZkController.java:1720.

## History

- Commits on origin/main naming SOLR-15672: none.
- The election-code history was not traced in this pass.

## Reproduction attempts

Not attempted. The ticket describes design properties, not one failing run. A reproduction of the properties (for example, two threads running the leader process) would need a multi-node election test, which was not set up in this pass.

## Gap analysis

The ticket is an architectural proposal. The properties it lists are design claims, and whether the current code meets each of them needs a test per claim. The redesign itself (consolidating the election code and dropping or keeping the redundant leader node) is a decision for the maintainers, not something the code settles.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold, because no commit addresses the proposal. close-obsolete does not hold, because the classes and nodes it names are still in use. The missing pieces are (1) a decision on whether to pursue the consolidation (an owner or maintainer decision), and (2) for each listed property, a test showing whether the current code meets it.

## Limits

This record does not test any listed property. It does not trace the election history. It does not evaluate the Curator suggestion.
