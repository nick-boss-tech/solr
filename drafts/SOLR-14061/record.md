# Skip record: SOLR-14061

- Ticket: SOLR-14061, TestLocalFSCloudBackupRestore may have exposed corruption possibility. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-12, updated 2020-01-21, linked to SOLR-14063 (Open); six comments, the last on 2019-12-12.
- Verdict: keep-open.
- Draft branch: solr-14061-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Grep and code read run at this head (detached worktree).
- Source: remediation redo of batch 05, item 9, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (A3 other round 1) cited one passing run; the run is not repeated here.

## Ticket reading

The reporter ran TestLocalFSCloudBackupRestore under heavy load and saw AlreadyClosedException, NoSuchFileException for Lucene index files, and leaked threads. The test expects "/location/does/not/exist" to be missing, and a SecurityException from that check may be the start of the failure. The reporter fears an unexpected Throwable could corrupt an index in production. The commenters (rcmuir, dweiss) say the path should be a temp path, and the SecurityException comes from the test policy.

## Grep and code reading at the pinned head

- `git grep -l "class TestLocalFSCloudBackupRestore" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java'` finds the test class (one file).
- solr/core/src/java/org/apache/solr/handler/admin/api/CreateCoreBackup.java:114: `snapShooter.getBackupRepository().exists(snapShooter.getLocation())`. A SecurityException from this call is a RuntimeException, and it goes to the catch-all at :126 (`catch (Exception exp)`), which rethrows it as a server error (SolrException SERVER_ERROR, line 127 onward).
- So the exception from the existence check is reported to the caller. This record does not show that a reported error leaves an index corrupted; the reporter's NoSuchFileException under load is not explained by this path.

## History

- No commit names SOLR-14061. The fix the reporter asked for in SOLR-14063 is not on the pin (that ticket is open).

## Reproduction attempts

Not repeated here. The earlier run (A3 other round 1) passed once, with seed 900B1038B36400AA, and it was marked inconclusive because the test is flaky. A repeated run under load is needed to see the reported failure at all; this pass did not run the test.

## Gap analysis

The code shows the existence-check exception is wrapped and reported. What the ticket is really about (whether an unexpected Throwable during backup can corrupt index files) is not answered by reading: it needs the loaded run that reproduces NoSuchFileException or a leaked resource, and then a check of whether index files are changed by the failing path.

## Verdict justification

keep-open: the attempts made are the grep and code reads above at the pinned head, the Jira read, and the single prior run (inconclusive). The missing piece is a repeated run of the test under load that reproduces the reported exceptions, plus a check of whether the failing path touches index files. close-fixed and close-obsolete are not supported: the failure is not shown fixed or gone. pr-needed is not supported: the failure is not reproduced and the corruption claim is not shown.

Missing for a closing or PR verdict: the repeated loaded run and its logs, and the index-file check.

## Limits

This record does not reproduce the failure. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
