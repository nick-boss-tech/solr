# Skip record: SOLR-12262

- Ticket: SOLR-12262, ReplicationHandler returns invalid/outdated backup status. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments, updated 2019-06-08. The full description (1808 characters) was read through the Jira REST record.
- Verdict: close-fixed.
- Draft branch: solr-12262-draft-close (a status change from the earlier keep-open; the keep-open draft solr-12262-draft-open is not updated by this record).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: after /replication?command=backup, /replication?command=details can return a previous backup's status (status success, old startTime and snapshotCompletedAt) while the new backup runs, because ReplicationHandler.snapShootDetails is only replaced when SnapShooter's async thread finishes. The reporter proposes setting the status to in progress before createSnapAsync starts, so the stale value is never shown.

## Code reading

- solr/core/src/java/org/apache/solr/handler/SnapShooter.java:242-252 (inProgressDetails): builds the in-progress status (startTime, status, snapshotName when known, directoryName).
- SnapShooter.java:267-272 (createSnapAsync): before the snapshot thread starts (new Thread at :274), it calls result.accept(inProgressDetails(Instant.now().toString(), WAITING_FOR_COMMIT_STATUS)). That result is the consumer ReplicationHandler passes in (ReplicationHandler.java:724, :1552 and :664, which assign snapShootDetails = nl). So the stale value is replaced at the start of the backup, which is the proposed fix in the ticket.
- SnapShooter.java:291: the completed status is reported when the thread finishes, unchanged.
- ReplicationHandler.java:1139 (details path): reads snapShootDetails and adds it under the backup key, so the details call now returns the in-progress entry during a new backup.

The code that makes this happen was added by commit 0a0c25046e, titled "SOLR-18344: Report in-progress backup status on /replication?command=details" (PR #4728). That commit is an ancestor of the pin (checked with git log at the pin). The commit's test, TestSnapshotCoreBackup.testBackupReportsProgressWhileRunning, asserts that the in-progress and running statuses are reported during a backup; it was not run in this pass.

## History

- No commit names SOLR-12262 (git log --grep at the pin: none). The fix is recorded under the different key SOLR-18344.
- The ReplicationHandler.snapShootDetails field and the details path existed before the fix; the fix adds the synchronous in-progress accept.

## Reproduction attempts

Not run in this pass. The ticket's steps (backup, then details during the backup) correspond to the test named above, which was not executed here. The closing check is the code and the ancestry, per the rubric.

## Gap analysis

The stale-success path in the ticket is closed at the pin: the details call returns the in-progress status as soon as a backup is requested. The ticket's suggested workaround (compare startTime or snapshotCompletedAt) is no longer needed for the in-progress case. The fix is under a different Jira key, so the closing evidence is the commit, not a ticket reference.

## Verdict justification

close-fixed: a fix commit (0a0c25046e, SOLR-18344) is on main at the pin (ancestor checked), and the changed code is present at the pin (SnapShooter.java:242-272, ReplicationHandler.java:1139). The rubric's close-fixed check is met.

Caveat for the owner: the fix is recorded under SOLR-18344, so the close should cite that commit. The test was not run here.

## Limits

This record does not run TestSnapshotCoreBackup, does not replay the reporter's two-backup sequence, and does not post anything to JIRA or GitHub.
