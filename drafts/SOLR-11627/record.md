# Skip record: SOLR-11627

- Ticket: SOLR-11627, Weird AddReplicaTest failure on jenkins. JIRA read in this pass: Open, resolution none, fix versions none, 3 comments (2017-11-09 attachment note; 2017-11-09 bot note for commit eef8f30da2 "Enable debug logging of overseer messages"; 2022-02-10 a maintainer asking whether the added debug logging produced any result, and noting the fix version is removed before 9.0). Updated 2022-02-10.
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, which named an A1 run whose result is not re-checked here).
- Draft branch: solr-11627-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: AddReplicaTest.test failed on a Jenkins run; the new replica published itself as recovering but the Overseer never processed it, and the leader waited in prep recovery until the test timed out. The reporter attached logs. The only commit linked is eef8f30da2 (2017-11-09), which enables debug logging of Overseer messages; it is a logging change, not a fix. The 2022 comment asks whether the logging gave any result.

## Code reading

- eef8f30da2 is an ancestor of the pin (checked with merge-base); it is the commit the ticket's comments link, and its subject describes debug logging, not a change to replica recovery.
- solr/core/src/test/org/apache/solr/cloud/AddReplicaTest.java:36 (class) and :91-106 (the current test body): the assertion the ticket cites at line 103 is not the one at that line now; the test uses CollectionAdminRequest.addReplicaToShard with processAndWait (120 s) and waitForState for clusterShape(1, 9). So the failing assertion has moved, and the 2017 trace does not map onto the pin.
- The Overseer and recovery paths are not re-traced in this record; the reporter's explanation (a replica published as recovering but not processed by the Overseer) is a claim about runtime behavior, not checked by reading.

## History

- eef8f30da2 (SOLR-11627, 2017-11-09): debug logging for Overseer messages; no fix to recovery.
- The test file's history after 2017 was not traced in this pass beyond the commits listed in the file's recent log.

## Reproduction attempts

Not run in this pass. A reproduction needs repeated runs of AddReplicaTest at the pin (for example with a test-repeat or seed loop), recording whether the add-replica call times out at the clusterShape wait or the processAndWait, and checking the Overseer logs for the stalled replica.

## Gap analysis

The failure the ticket reports cannot be matched to the current test body by reading, and no fix commit exists. Whether the Overseer stall still happens at the pin is an open runtime question. A close verdict needs either a run that fails to reproduce after repeated attempts (close-cannot-reproduce) or a fix commit.

## Verdict justification

keep-open: no fix commit names the ticket (eef8f30da2 is logging only), and the reproduction has not been attempted, so close-cannot-reproduce is not supported. close-obsolete is not supported because the test still exists and runs at the pin, and the current assertion is not shown to be removed.

Missing for a closing verdict: repeated AddReplicaTest runs at the pin recording whether the stall reproduces; a zero-failure run set would support close-cannot-reproduce, and a reproducing run would name the stalled replica and the Overseer path.

## Limits

This record does not run AddReplicaTest, does not re-trace the Overseer recovery path, and does not post anything to JIRA or GitHub.
