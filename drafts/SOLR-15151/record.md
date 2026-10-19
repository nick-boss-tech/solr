# Skip record: SOLR-15151

- Ticket: SOLR-15151, It's not possible anymore to indicate a relative path to the core data folder. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-02-11, updated 2021-02-23, linked to SOLR-14561 (Closed), one comment (2021-02-23, tmortagne) on gaps in assertPathAllowed.
- Verdict: keep-open.
- Draft branch: solr-15151-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and grep run at this head (detached worktree).
- Source: remediation redo of batch 03, item 22, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, deferred) cited the path check from audit evidence; the lines are confirmed here below.

## Ticket reading

Since SOLR-14561, a core's dataDir that contains ".." is refused, so a relative data folder outside the core directory cannot be set. The reporter wants the ".." check removed and relative paths checked against the allowed paths instead.

## Code reading and grep at the pinned head

- solr/core/src/java/org/apache/solr/core/SolrPaths.java:65, `assertPathAllowed(Path, Set<Path>)`. It returns early for the catch-all allow set (line 66), returns for a null path (line 67), normalizes the path (line 70) and calls `assertNoPathTraversal(path)` (line 73). It returns for relative paths (line 74), and for absolute paths it requires a prefix match with an allowed path (lines 75-81).
- solr/core/src/java/org/apache/solr/core/SolrPaths.java:85-86, `assertNoPathTraversal(Path)`: throws when `pathToAssert.toString().contains("..")`. So any path with ".." is refused before the relative-path case is considered.
- solr/core/src/test/org/apache/solr/core/TestCoreContainer.java:651 asserts `/var/log/../solr/foo` is allowed in the allow-list test; lines 654-656 and 667 assert that `../../false`, `./../../false`, `/var/solr/../../etc` and `.\../\..\false` are blocked. So the ".." refusal is tested as the intended behavior.
- `git grep -n "assertPathAllowed\|assertNoPathTraversal" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java` shows the callers at CoreContainer.java:1525, :1540 (dataDir) and the instance-level wrapper at :1621-1622.

The reporter's case is refused by the code and tested as refused. The fix the ticket asks for (resolve the relative dataDir against the instance directory and check the result against allowPaths) changes the security contract that the test at TestCoreContainer.java:654-667 pins down.

## History

- SOLR-14561 is linked as Closed; it introduced the check. Its commit is not identified in this record (not searched in this pass).

## Reproduction attempts

Not run. The refusal is in code and in a test that asserts it; a run would show the same refusal.

## Gap analysis

The behavior is deliberate and tested. The ticket asks for a change to the security rule, and the reporter's own comment and tmortagne's comment both describe the trade-off. That is a maintainer decision, not a defect with a clear fix.

## Verdict justification

keep-open: the code and test reads above at the pinned head establish the refusal and that it is intended. The change requested is a security-contract change (decision ladder rung 5: owner-level). The missing piece is that decision, plus a choice between resolving relative dataDir paths and disabling the allow-list check, which the ticket leaves open. No close verdict is supported (the behavior is current and tested); no PR is supported without the decision.

Missing for a closing or PR verdict: (1) the maintainer decision on the security rule; (2) if approved, the chosen resolution.

## Limits

This record does not reproduce the refusal. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
