# Skip record: SOLR-16122

- Ticket: SOLR-16122, "TestLeaderElectionZkExpiry failing frequently". JIRA status Open, resolution none, fix versions none, created 2022-03-29, updated 2024-06-03. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-16122-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-16122-vm2.md).

## Ticket reading

The ticket says the test fails in about 10% of runs and was marked @BadApple before 9.0 (commit 60aa6693ba2, 2022-03-29). A 2024-06-03 comment reports thread leaks ("2 threads leaked from SUITE scope") in CI. The related ticket SOLR-16046 (thread leak in this test) is linked; its fix e03cb5356e7 (2022-09-06) is an ancestor of the pinned head.

## Code reading

- solr/core/src/test/org/apache/solr/cloud/TestLeaderElectionZkExpiry.java:38 and 43 (origin/main 498a09aea97): the class has a single test, testLeaderElectionWithZkExpiry, and is annotated @AwaitsFix(bugUrl="https://issues.apache.org/jira/browse/SOLR-16122"). In a default run the test is skipped with "'awaitsfix' test group is not enabled".
- The BadApple annotation from 60aa6693ba2 is no longer on the file. Its removal is not a fix: c64a35adbcd (SOLR-17631, a Lucene 9 to 10 upgrade) touched the file among many others.
- Not read in this pass: the ZooKeeper expiry code the test drives (the test uses SolrZkClient and the cluster harness), and whether the thread-leak fix SOLR-16046 covers the 2024 leak.

Commands at the pinned head:
- git grep -n 'AwaitsFix\|BadApple' origin/main -- solr/core/src/test/org/apache/solr/cloud/TestLeaderElectionZkExpiry.java: AwaitsFix at SOLR-16122, no BadApple.

## History

- 60aa6693ba2 (2022-03-29): SOLR-16122 Bad-apple TestLeaderElectionZkExpiry test.
- e03cb5356e7 (2022-09-06): SOLR-16046 thread leak fix; ancestor of the pinned head.
- c64a35adbcd (2025-08-07): SOLR-17631 Lucene upgrade; touches the file, not a fix.

## Reproduction attempts

1. Command: :solr:core:test --tests org.apache.solr.cloud.TestLeaderElectionZkExpiry -Ptests.iters=8 --offline, worktree at origin/main 498a09aea97. Log /workspace/gates/logs/vm2/16122/16122-flake-r1.log. Result: BUILD SUCCESSFUL, all 8 iterations skipped by the @AwaitsFix group (not run). Conclusive: no. It ran nothing.
2. Same command with -Ptests.awaitsfix=true. Log /workspace/gates/logs/vm2/16122/16122-flake-r2.log, XML /workspace/gates/logs/vm2/16122/16122-flake-r2.xml/. Result: BUILD SUCCESSFUL; testsuite tests=8 skipped=0 failures=0 errors=0, 118.6 s. Conclusive: no. Eight passes do not rule out a failure rate of about 10% per run: a run with that rate passes eight times in about 43% of cases.
- Not run: a longer repeat (the ticket's rate needs roughly 30 or more iterations to measure), and a run with the 2024 thread-leak conditions.

## Gap analysis

The test is still marked AwaitsFix on main, so default CI does not run it. The 2022 action was a bad-apple marker, not a fix, and the ticket remains open. Eight iterations with the group enabled show no failure, which is weaker than the reported 10% rate. Whether the failure is still present on the pinned head is not established.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. The missing pieces are (1) a longer repeat run (around 30 iterations) with the awaitsfix group enabled, to test the reported rate, and (2) a decision on whether the test stays AwaitsFix. A close verdict needs a fix or a long clean run; a pr-needed verdict needs a failing run with the cause.

## Limits

This record does not establish the failure rate on the pinned head. It does not read the ZooKeeper expiry code. It does not confirm that the 2024 thread leak is still present.
