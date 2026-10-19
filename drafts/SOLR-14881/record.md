# Skip record: SOLR-14881

- Ticket: SOLR-14881, "ManagedSchemaRoundRobinCloudTest test failures". JIRA status Open, resolution none, fix versions none, created 2020-09-21, updated 2020-10-09. Checked 2026-10-19 UTC.
- Verdict: close-fixed (changed from keep-open; this redo supersedes the recorded verdict).
- Draft branch: solr-14881-draft-close (the earlier keep-open branch solr-14881-draft-open is not updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14881-vm2.md).

## Ticket reading

The ticket names the test ManagedSchemaRoundRobinCloudTest as failing. Its description is empty. Two JIRA comments (2020-09-21, jira-bot) link the fix commits: 73c06e35e8b on main and 669aff2108f on branch_8x, both by the reporter of the ticket.

## Code reading

- The fix on main is 73c06e35e8b (SOLR-14881: ManagedSchemaRoundRobinCloudTest test failures, 2020-09-21). It is a one-line change in solr/core/src/java/org/apache/solr/core/CoreContainer.java: `if (coreId != null) return;` added in the core-load failure path, before the coreInitFailures lookup.
- The added line is still present at the pinned head: solr/core/src/java/org/apache/solr/core/CoreContainer.java:2060 (`if (coreId != null) return; // yeah, this core is already reloaded/unloaded return right away`).
- The test class is still on main and carries no AwaitsFix or BadApple marker (solr/core/src/test/org/apache/solr/schema/ManagedSchemaRoundRobinCloudTest.java:33; the class body starts at line 33 with setupCluster at 41 and the test at 55).

Commands at the pinned head:
- git merge-base --is-ancestor 73c06e35e8b origin/main: ancestor.
- git show origin/main:solr/core/src/java/org/apache/solr/core/CoreContainer.java | grep -n 'yeah, this core is already reloaded': line 2060.
- git grep -n 'ManagedSchemaRoundRobinCloudTest' origin/main -- solr: the class file and no marker lines.

## History

- 73c06e35e8b (2020-09-21): the fix on main; ancestor of the pinned head.
- 669aff2108f (2020-09-21): the same fix on branch_8x, which is not in this repository (no such branch on the fork).

## Reproduction attempts

1. Command: :solr:core:test --tests org.apache.solr.schema.ManagedSchemaRoundRobinCloudTest --offline, worktree at origin/main 498a09aea97. Log /workspace/gates/logs/vm2/14881/14881-r2.log, XML /workspace/gates/logs/vm2/14881/14881-r2.xml/. Result: BUILD SUCCESSFUL; testsuite tests=1 skipped=0 failures=0 errors=0 (19.2 s). Conclusive: no. One passing run does not rule out an intermittent failure, and the ticket does not record which failure it saw.
- An earlier run (14881-r1) was interrupted by a session restart during compilation and produced no test result; it is not counted.

## Gap analysis

The ticket gives no failure output, so the failure the fix addresses is inferred from the fix's title and its one-line change. The fix is on main and the test passes on the pinned head once. Whether the fix addresses the failure reported in 2020 is supported by the commit link in the ticket, not by a reproduction of the original failure.

## Verdict justification

close-fixed meets the rubric: a commit on main fixes the reported problem (73c06e35e8b, linked from the ticket), the fixing commit is an ancestor of the pinned head, and the changed code is still present at the head (CoreContainer.java:2060). The minimum evidence for close-fixed is the fixing commit sha, the ancestor check, and the check that the changed code is still present; all three are recorded. Requirement met only in part: the original failure output is not in the ticket, so the link between the fix and the failure rests on the commit title and the ticket's link. One passing run is the only reproduction.

## Limits

This record does not reproduce the original failure. It does not run the test repeatedly. It does not check the branch_8x fix, which is not in this repository.
