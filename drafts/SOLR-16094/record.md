# Skip record: SOLR-16094

- Ticket: SOLR-16094, "TestSolrConfigHandlerCloud is failing on OSX due to hanging SolrCore.reload calls". JIRA status Open, resolution none, fix versions none, created 2022-03-10, updated 2022-05-12. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-16094-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-16094-vm2.md).

## Ticket reading

The reporter sees TestSolrConfigHandlerCloud fail on macOS because SolrCore.reload() hangs for the rest of the test. The reload runs on the ZooKeeper event thread, so it is only cleaned up when the test fails and the stale threads are killed. The reload then continues and logs "Opening new SolrCore at ...", so the hang is before the new core is created. The reporter could not reproduce it locally. The report has no comments and names no fix.

## Code reading

- solr/core/src/java/org/apache/solr/core/SolrCore.java:775-778 (origin/main 498a09aea97): reload(ConfigSet) begins with `synchronized (getUpdateHandler().getSolrCoreState().getReloadLock())`. A second reload of the same core waits here while another holds the lock. That is before any of the work that could hang, and it matches the reporter's observation that the hang is before "Opening new SolrCore".
- SolrCore.java:781-788: increfSolrCoreState, getNewIndexDir and getIndexDir comparison, then cloneForReloadCore. The reporter's hang point is between the lock and the log line at line 1073, so it lies in this block or in cloneForReloadCore.
- SolrCore.java:1073: the log line "Opening new SolrCore at ..." that the reporter says follows the hang.
- solr/core/src/test/org/apache/solr/handler/TestSolrConfigHandlerCloud.java:42: the class has no AwaitsFix or BadApple marker at the pinned head. The test runs in the normal suite.

Commands at the pinned head:
- git grep -n 'AwaitsFix\|BadApple' origin/main -- solr/core/src/test/org/apache/solr/handler/TestSolrConfigHandlerCloud.java: no output.
- git grep -n 'SOLR-16094' origin/main: no output.

## History

- Commits on origin/main naming SOLR-16094: none.
- Commits touching SolrCore.reload's lock: not traced in this pass.

## Reproduction attempts

Not attempted. The reporter's failure is macOS-specific and intermittent, and this host runs Linux. A run of TestSolrConfigHandlerCloud here would be a Linux run, which the ticket does not describe as failing. A useful reproduction needs a macOS run or a thread dump from a hung run.

## Gap analysis

The hang is located between the reload lock (SolrCore.java:778) and the log line (line 1073). Which of the blocks in between blocks, and why only on macOS, is not established. Two candidates are the reload lock itself (a second reload waiting on a lock that a stuck thread holds) and cloneForReloadCore (a filesystem or directory operation that behaves differently on macOS). Neither is confirmed.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. The missing piece is a thread dump from a hung run on macOS, or a macOS reproduction, to show which block the reload is in. A close verdict needs a fix or a clean long run on the reporter's platform; a pr-needed verdict needs the blocking point.

## Limits

This record does not reproduce the hang. It does not test on macOS. It does not identify the blocking call.
