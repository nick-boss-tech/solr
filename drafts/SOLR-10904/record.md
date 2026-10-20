# Skip record: SOLR-10904

- Ticket: SOLR-10904, Unnecessary waiting during failover in case of failed core creation. JIRA read in this pass: Open, resolution none, fix versions none, 4 comments (2017-10-03 to 2017-11-07, the reporter and maintainers asking for a patch and noting that the autoAddReplicas replacement may have gone in); updated 2019-10-23.
- Verdict: close-obsolete (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J, C, G and L).
- Draft branch: solr-10904-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: the Overseer auto-replica failover thread creates a replacement core on another node through an update executor, then waits for the new replica to appear in the cluster state even when the core creation itself failed, which holds up the failover thread. The proposal is to wait for the creation result first and only then wait for the state change.

## Code reading

- The named class OverseerAutoReplicaFailoverThread and the createSolrCore path are absent from the whole solr tree at the pin: git grep -- solr returns 0 files for OverseerAutoReplicaFailoverThread, AutoReplicaFailoverThread and "createSolrCore(".
- The removal: commit 0f7e3be589 (SOLR-10397, 2017-07-28, "Remove old implementation of autoAddReplicas features") deleted solr/core/src/java/org/apache/solr/cloud/OverseerAutoReplicaFailoverThread.java and its test SharedFSAutoReplicaFailoverUtilsTest.java. Ancestry checked: 0f7e3be589 is an ancestor of the pin.
- Later, 1e77bdf062 (SOLR-17609, 2025-02-16) removed the HDFS module's SharedFileSystemAutoReplicaFailoverTest.
- The ref guide still documents the removal of the automatic replica-add feature in its Solr 8 upgrade notes (solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-8.adoc:477-482).

So the failover thread the ticket describes no longer exists at the pin, and the wait it describes cannot occur in this tree.

## History

- No commit at the pin names SOLR-10904 (git log --grep at the pin: none).
- The autoAddReplicas replacement mentioned in the 2017 comments is not present at the pin (the autoscaling and failover features were removed).

## Reproduction attempts

No run was attempted. For close-obsolete the rubric's check is the removed code and the whole-tree grep, which are done above.

## Gap analysis

The behavior is not present to fix, because the failover thread that waited is gone. If a current failover or auto-add mechanism is wanted, it is a new feature and should be a new ticket.

## Verdict justification

close-obsolete: the named class and path are absent from the whole tree at the pin, and the removing commit 0f7e3be589 (SOLR-10397) is an ancestor of the pin. The rubric's close-obsolete check is met.

## Limits

This record does not run the failover code (it no longer exists), does not check the auto-add replacement claimed in the comments, and does not post anything to JIRA or GitHub.
