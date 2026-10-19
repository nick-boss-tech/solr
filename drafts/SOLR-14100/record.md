# Skip record: SOLR-14100

- Ticket: SOLR-14100, System properties cross test suite boundary. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-16, updated 2020-09-29, no fix versions, no links; twelve comments, the last on 2019-12-27.
- Verdict: keep-open.
- Draft branch: solr-14100-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Grep run at this head (detached worktree).
- Source: remediation redo of batch 05, item 5, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) cited the ticket's title from audit evidence; the thread is read here.

## Ticket reading

The test framework does not save and restore system properties across suites, because the security policy lets tests write any property and the restore rule cannot see them all. The ticket asks for a list of writable properties, a top-level restore rule, and a policy that enables them selectively. The thread (dweiss, rcmuir, krisden; 2019-12-16 to 2019-12-27) calls it a tedious suite-wide task, and names a misspelled property in an error (zkclienttmeout).

## Grep at the pinned head

- `git grep -n "zkclienttmeout" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' | wc -l` gives 0. The misspelled name from the thread is not in the Java sources.
- `git grep -n "zkclienttimeout" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' | wc -l` gives 0. The correctly spelled name also does not appear, so the property name the thread used has changed or been removed; this record does not establish which.
- `git grep -n "TestRuleRestoreSystemProperties" 498a09aea97aac86c2cc7ac114113b946d249942 -- 'solr/test-framework/src/java/*' 'solr/core/src/test/*' | wc -l` gives 10. The restore rule is still used in the test framework and in core tests, so the mechanism the ticket describes is in place for those uses.

## History

- No commit names SOLR-14100. The commit that removed or renamed the misspelled property was not identified in this pass.

## Reproduction attempts

Not run. A reproduction would be a test that writes an unlisted system property and checks whether the next test sees it. This pass did not write or run such a test.

## Gap analysis

The ticket is a suite-wide task: list every writable property, add each to the restore rule, and narrow the policy. The grep shows the rule exists and is used in ten places, but the thread's full sweep (every property read, written or cleared) is not shown to be done. The thread's named typo is not in the code.

## Verdict justification

keep-open: the attempts made are the greps above at the pinned head, the Jira read, and the thread read. The missing piece is the full sweep of writable and cleared properties against the restore rule and the policy, and a test that shows a property written in one suite is restored before the next. close-fixed is not supported: the sweep the ticket asks for is not shown done. close-obsolete is not supported: the restore rule is in use.

Missing for a closing or PR verdict: the sweep list and the policy change, or a split into focused tickets.

## Limits

This record does not run a property-leak test. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
