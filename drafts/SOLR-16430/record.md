# Skip record: SOLR-16430

- Ticket: SOLR-16430, "AbstractIncrementalBackupTest fails on the macOS tests". JIRA status Open, resolution none, fix versions none. Created 2022-09-23, updated 2023-01-25. Checked 2026-10-18 UTC.
- Verdict: keep-open.
- Draft branch: solr-16430-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main). No upstream sha cited.
- Checked by: vm5, 2026-10-18 UTC.

## Ticket reading

The symptom is `expected:<COMPLETED> but was:<RUNNING>` in the incremental backup tests, reported on macOS for "any of the repository implementations". The reporter proposes raising a 20-second wait to 30 seconds.

Comments that matter:
- 2022-09-23: fix commit 9e72a75caa on main and 79d005f2a7 on branch_9x, both "SOLR-16430: Add longer wait in backup tests".
- 2022-10-21: a reporter says the failure is "still happening", naming `testRestoreToOriginalCollection`.

No comment gives a seed, a log, or an environment other than macOS. No linked issues.

## Code reading

Path: solr/test-framework/src/java/org/apache/solr/cloud/api/collections/AbstractIncrementalBackupTest.java (the file the ticket names).

- Line 197, `testRestoreToOriginalCollection` (the method the 2022-10-21 comment names):
  - Line 215: `backupCollection(...).processAndWait(cluster.getSolrClient(), 20)`. This 20-second backup wait was not changed by the 2022 fix.
  - Line 230: `restoreCollection(...).processAndWait(cluster.getSolrClient(), 30)`. This is the 30-second wait the 2022 fix added (the fix's diff shows it changed from 20 to 30 at a restore call).
- Line 494, `testRestoreToOriginalSucceedsWithErrors`:
  - Line 516: `backupCollection(...).setRepositoryName(ERROR_BACKUP_REPO_NAME)...processAndWait(cluster.getSolrClient(), 20)`, followed by `assertEquals(RequestStatusState.COMPLETED, result)` at line 517. This matches the ticket's assertion and still uses 20 seconds.

Grep at the pinned head: `grep -n 'processAndWait' AbstractIncrementalBackupTest.java` gives 20 at lines 215 and 516, and 30 at lines 230, 540 and 569.

Read at head: the 2022 fix changed only the restore wait at line 230. Both 20-second backup waits (215 and 516) are unchanged.

## History

- 9e72a75caa, 2022-09-23, "SOLR-16430: Add longer wait in backup tests". Ancestor of 498a09aea9: yes. Changes one line in AbstractIncrementalBackupTest.java.
- da80d79ae2, 2026-09-09, "SOLR-18358: remove the non-incremental (full-snapshot) backup path (#4808)". Ancestor: yes. Touches the same file; does not touch the waits at 215 or 516.
- 4e5c83d2f0, 2026-05-20, "SOLR-17821: Fix error scenario for ShardInstall or Restore (#3434)". Ancestor: yes. Touches the same file.

## Reproduction attempts

All runs use `:solr:core:test --rerun --tests LocalFSCloudIncrementalBackupTest` at head 498a09aea9. That class runs AbstractIncrementalBackupTest's methods, including `testRestoreToOriginalCollection` and `testRestoreToOriginalSucceedsWithErrors`.

1. 16430-r1: seed 2D8913536249D12A. Result: PASS (7 run, 1 skipped, 0 failures). Log `/workspace/gates/logs/vm5/jira/16430-r1.log`, XML `16430-r1.xml/`. Conclusive: no. A pass does not show the intermittent failure is absent.
2. 16430-r2: seed 4A3BAEC6CC3D5463. Result: PASS (7 run, 1 skipped, 0 failures). Log `/workspace/gates/logs/vm5/jira/16430-r2.log`, XML `16430-r2.xml/`. Conclusive: no, same reason.
3. 16430-load-r1: seed F5D796538F63B4C8, run with two `nice -n 19` CPU-busy loops on the host. Result: PASS (7 run, 1 skipped, 0 failures). Slowest case `testRestoreToOriginalSucceedsWithErrors` took 13.2 s, under the 20 s wait. Log `/workspace/gates/logs/vm5/jira/16430-load-r1.log`, XML `16430-load-r1.xml/`. Conclusive: no. Load did not push the wait over its limit, and the RUNNING state was not seen.

Not attempted: a macOS run. The ticket's environment is not available on the pool hosts.

## Gap analysis

The ticket describes a failure on macOS. Linux runs pass, both with normal load and with added CPU load. The reporter's 2022 change lengthened a restore wait, but two 20-second backup waits remain: line 215 in `testRestoreToOriginalCollection` and line 516 in `testRestoreToOriginalSucceedsWithErrors`. The 2022-10-21 comment points at `testRestoreToOriginalCollection`, so line 215 is a candidate for the timing-sensitive wait. Whether either wait is too short depends on how long the backup takes on the failing machine, which these runs did not show.

Uncertainty that remains: which of the two 20-second backup waits (215 or 516) the macOS failure hits, and whether the backup in that environment takes longer than 20 seconds.

## Verdict justification

keep-open, per the rubric's "inconclusive after the attempts listed" rule. Evidence met: pinned head sha, JIRA status and date, code paths with file and line, git history with ancestry, and three reproduction attempts with command, seed, result, and log path.

Missing for a conclusive verdict: a failure log from the reporter (test method, failing line, timing) or a run on macOS. Either would show whether line 215 or line 516 is the failing wait, and whether the fix should be a longer wait or something else.

## Limits

This record does not establish whether the macOS failure is still present on current main. It does not test macOS. The runs used the Linux pool host only.
