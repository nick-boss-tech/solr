# Skip record: SOLR-14784

- Ticket: SOLR-14784, "Reproducible failure for DirectUpdateHandlerTest". JIRA status Open, resolution none, fix versions none, created 2020-08-27, updated 2020-10-09. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14784-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14784-vm2.md).

## Ticket reading

The ticket reports that DirectUpdateHandlerTest fails 100% of the time with one seed (2BE3A8682E5E346D, multiplier 2, badapples off, file encoding US-ASCII), and that it appeared with LUCENE-9456. Comments (six, 2020-08-27 to 2020-10-09): erickerickson finds a logged error on the failing run; jpountz finds the test assumes a fully written segment has more files, and that a file is not closed on all paths.

## Code reading

- solr/core/src/test/org/apache/solr/update/DirectUpdateHandlerTest.java (origin/main 498a09aea97): setUp sets solr.directoryFactory to MockFSDirectoryFactory (line 57). The test commits in several places (lines 122, 176, 226, 361, 473).
- The file was last changed in 218ce981ba9 (2026-08-25); that change is not traced here as the fix for this ticket. No commit on main names SOLR-14784 (`git log --oneline --grep=SOLR-14784 origin/main`: no output).
- The segment-completeness assumption jpountz described is not located in the current test: the assertions near the commits were not traced line by line in this pass, so the code-path argument that rules the failure out is not complete.

Commands at the pinned head:
- git log --oneline --grep='SOLR-14784' origin/main: no output.
- git grep -n 'MockFSDirectoryFactory\|commit()' origin/main -- solr/core/src/test/org/apache/solr/update/DirectUpdateHandlerTest.java: the setUp line and the commit calls.

## History

- Commits on origin/main naming SOLR-14784: none.
- 218ce981ba9 (2026-08-25): the last change to the test file; not identified as the fix.

## Reproduction attempts

1. Command: :solr:core:test --tests org.apache.solr.update.DirectUpdateHandlerTest -Ptests.seed=2BE3A8682E5E346D -Ptests.multiplier=2 -Ptests.badapples=false -Ptests.file.encoding=US-ASCII --offline, worktree at origin/main 498a09aea97. Log /workspace/gates/logs/vm2/14784/14784-r1.log, XML /workspace/gates/logs/vm2/14784/14784-r1.xml/. Result: BUILD SUCCESSFUL; testsuite tests=7 skipped=0 failures=0 errors=0 (38.4 s); the log shows "randomization seed: tests.seed=2BE3A8682E5E346D". Conclusive: partly. The ticket says the seed reproduces 100 percent of the time, so one clean run at the same seed is a negative result on this head; it does not cover the other seeds or the ticket's environment.

## Gap analysis

The reported failure does not show with the ticket's exact command and seed on the pinned head. Whether it is gone because the code changed (LUCENE-9456 or the 2026 test change), or because the environment differs, is not established. The code-path argument for why the segment assumption no longer fails is not complete.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. close-cannot-reproduce needs both a reproduction attempt on the pinned head (done, one run, negative) and a code-path argument that rules the failure out, which is not complete here. The missing pieces are (1) the code-path argument: trace the segment and file-close assumptions in the test and in the Lucene version the pin uses, and (2) a repeat of the same command (the ticket's claim is 100 percent, so a second run at the same seed) to confirm the negative.

## Limits

This record does not trace the test's assertions line by line. It does not identify the change that made the seed pass. It does not test other seeds.
