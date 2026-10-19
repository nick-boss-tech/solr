# Skip record: SOLR-14063

- Ticket: SOLR-14063, give AbstractCloudBackupRestoreTestCase the /location/does/not/exist it wants so badly. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-12, updated 2020-01-21, linked to SOLR-14061 (Open); no comments.
- Verdict: keep-open.
- Draft branch: solr-14063-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Grep and code read run at this head (detached worktree).
- Source: remediation redo of batch 05, item 8, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) cited the ticket's title from audit evidence; the test code is read here.

## Ticket reading

The ticket says the cluster backup/restore test case wants a location that does not exist, so that an error is an IOException and not a SecurityException. The reporter does not want to commit the change until it is shown not to risk corruption.

## Grep and code reading at the pinned head

- AbstractCloudBackupRestoreTestCase.java is in solr/test-framework/src/java/org/apache/solr/cloud/api/collections/ (one class match by grep).
- The path is already used: that file, line 235, sets the cluster property `CoreAdminParams.BACKUP_LOCATION` to "/location/does/not/exist" and expects the backup request to fail with a SolrException (lines 226-246).
- `git log --format='%h %ad %s' -S'/location/does/not/exist' 498a09aea97aac86c2cc7ac114113b946d249942` gives bfe5c5ae49 (2016-07-06, SOLR-9242), the commit that added the path. The path predates this ticket (2019).
- Whether a SecurityException can be raised for that path under the test policy, and whether the test then goes to IOException, is not shown by reading. The ticket's concern is exactly that.

## History

- The path came in with SOLR-9242 in 2016; SOLR-14063 (2019) does not appear in the commits on the pin.

## Reproduction attempts

Not run. The question is answered by running the backup test under the test security policy and checking which exception it raises for that path. This pass did not run the test.

## Gap analysis

The requested path is in the test, so the ticket's literal request is met in the code. What the ticket asks in addition is that the change is safe (no SecurityException path that corrupts state). That is shown only by a run, which this record does not have.

## Verdict justification

keep-open: the attempts made are the grep and the code read above at the pinned head, the Jira read, and the history check of the path. The missing piece is a test run on the pin that shows which exception is raised for the path under the test policy, and whether that affects the cluster state. close-fixed is not supported without that run, because the ticket's own condition (safe to commit) is not shown; the path itself predates the ticket.

Missing for a closing or PR verdict: the run output for the backup test with the path, showing the exception and the cluster state after it.

## Limits

This record does not run the backup test. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
