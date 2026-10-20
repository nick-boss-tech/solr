# Skip record: SOLR-10889

- Ticket: SOLR-10889, Stale zookeeper information is used during failover check. JIRA read in this pass: Open, resolution none, fix versions none, 4 comments (2017-06-14 to 2017-10-03, including the 2017-07-18 comment that the issue does not apply in Solr 7 because of SOLR-10397, and the 2017-10-03 comment that SOLR-10397 had not landed); updated 2019-06-08.
- Verdict: close-obsolete (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J, C, G and L).
- Draft branch: solr-10889-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: OverseerAutoReplicaFailoverThread reads the cluster state once at the start of its per-replica loop, so a long pass over a big cluster can make decisions on stale state (restarting a healthy core, or missing a bad node). The proposal is to read the cluster state inside the loop. The 2017 comments say the issue does not apply in Solr 7 because the old failover implementation was replaced by SOLR-10397.

## Code reading

- The named class OverseerAutoReplicaFailoverThread, the method findDownReplicasInSlice, and the inner DownReplica of the failover thread are absent from the whole solr tree at the pin (git grep -- solr: 0 files for OverseerAutoReplicaFailoverThread and findDownReplicasInSlice; the four "DownReplica" matches are unrelated test names such as clusterStateReflectsActiveAndDownReplicas and testQueryAgainstDownReplica).
- The removal: commit 0f7e3be589 (SOLR-10397, 2017-07-28, "Remove old implementation of autoAddReplicas features") deleted solr/core/src/java/org/apache/solr/cloud/OverseerAutoReplicaFailoverThread.java. Ancestry checked: 0f7e3be589 is an ancestor of the pin.

So the loop that read the cluster state once does not exist at the pin.

## History

- The removal commit is the one the 2017 comments cite (SOLR-10397).
- No commit at the pin names SOLR-10889 (git log --grep at the pin: none).

## Reproduction attempts

No run was attempted. For close-obsolete the rubric's check is the removed code and the whole-tree grep, which are done above.

## Gap analysis

The failover loop the ticket describes is gone. The stale-read concern does not apply to any current code at the pin; if the auto-replica failover is wanted again, that is a new feature and should be a new ticket.

## Verdict justification

close-obsolete: the named class and method are absent from the whole tree at the pin, and the removing commit 0f7e3be589 (SOLR-10397) is an ancestor of the pin. The rubric's close-obsolete check is met.

## Limits

This record does not run the failover tests (the code no longer exists), and does not post anything to JIRA or GitHub.
