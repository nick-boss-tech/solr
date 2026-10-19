# Skip record: SOLR-14861

- Ticket: SOLR-14861, "CoreContainer shutdown needs to be aware of other ongoing operations and wait until they're complete". JIRA status Open, resolution none, fix versions none, created 2020-09-12, updated 2020-12-31. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14861-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14861-vm2.md).

## Ticket reading

The reporter found a race while investigating TestBulkSchemaConcurrent failures: a core reload that has begun can still be running when CoreContainer.shutdown() proceeds to close cores, and the reload then fails. The report proposes that shutdown wait for in-flight operations (reloads), and suggests an AtomicInteger counter on entry to CoreContainer operations as a crude test of the idea. Eleven comments (2020-09 to 2020-12). The ticket's own commits section finds no commit on main that names it.

## Code reading

- solr/core/src/java/org/apache/solr/core/CoreContainer.java:1228-1262 (origin/main 498a09aea97): shutdown() now (a) waits for ZooKeeper pending tasks, (b) shuts down the async task and index searcher executors, (c) sets isShutDown = true (line 1244), (d) calls pauseUpdatesAndAwaitInflightRequests() (line 1248), and (e) shuts down the coreZkRegister executor with a comment that async reloads complete before cores are closed (lines 1254-1257) before closing cores (solrCores.close()).
- So the shutdown path now has an in-flight wait and an executor drain for async reloads, which is the mechanism the ticket asks for. The reload path's check against isShutDown (the reported AlreadyClosed) is at CoreContainer.java:1223-1224 (isShutDown accessor).

Commands at the pinned head:
- git grep -n 'public void shutdown()' origin/main -- solr/core/src/java/org/apache/solr/core/CoreContainer.java: line 1228.
- git log --oneline --grep 'SOLR-14861' origin/main: no output.
- git log -S 'pauseUpdatesAndAwaitInflightRequests' origin/main -- solr/core/src/java/org/apache/solr/core/CoreContainer.java: did not finish within the time limit of this pass.

## History

- Commits on origin/main naming SOLR-14861: none.
- The commit that introduced the in-flight wait and the executor drain was not identified (the history search timed out).

## Reproduction attempts

Not attempted. The reported race is in TestBulkSchemaConcurrent and related managed-schema tests, which are intermittent. A run of the class would show whether it fails, but one run cannot establish an intermittent race either way, and the run was not made in this pass.

## Gap analysis

The shutdown code now contains the wait and the drain the ticket asks for, which suggests the ticket is addressed by code that does not name it. Closing needs the commit that added the wait (to cite as the fix, and to check the ancestor and the changed lines), and a repeated run of the affected test class showing it no longer fails. Neither is in hand.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. close-fixed does not hold yet: the code appears to address the ticket, but no fixing commit is identified and no repeated run was made, so the minimum evidence for close-fixed is not met. The missing pieces are (1) the commit that added pauseUpdatesAndAwaitInflightRequests and the executor drain, and (2) repeated runs of TestBulkSchemaConcurrent and the managed-schema tests named in the report.

## Limits

This record does not identify the fixing commit. It does not run the tests. It does not establish that the race is closed.
