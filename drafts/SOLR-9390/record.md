# Skip record: SOLR-9390

- Ticket: SOLR-9390, "LeaderFailoverAfterPartitionTest failures: Connection refused, too few active replicas, and no registered leader". JIRA status read 2026-10-20T14:36:48Z (UTC): Open, resolution None, updated 2019-06-08. Linked: SOLR-9927 (Open).
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-9390-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T14:36:48Z (UTC).

## Ticket reading

A seeded run of `LeaderFailoverAfterPartitionTest.test` fails with "No live SolrServers available" from a direct update to a replica, and the other failures are connection refused and too few active replicas. A 2016 run of 100 iterations gave 8 failures. Two 2016 commits (SOLR-9305, SOLR-9390) stopped using directToLeaders updates in the test.

## Code reading (pinned head)

- `solr/core/src/test/org/apache/solr/cloud/LeaderFailoverAfterPartitionTest.java` exists (196 lines, `@Test test()` at line 48-49, extends `HttpPartitionTest`). It has no `@Nightly` or `AwaitsFix` annotation in the lines checked.
- `git grep -n "directToLeaders\|DIRECT_UPDATES_TO_LEADERS" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/test/org/apache/solr/cloud/LeaderFailoverAfterPartitionTest.java`: empty.
- The message in the ticket is produced by `CloudSolrClient.directUpdate`, which the record did not re-read at the pinned revision.

## History

- evidence.py at the pinned head: no commits naming SOLR-9390.
- The 2016 fix commits (`d326adc8bf`, `a41dd86e3c`, SOLR-9305 and SOLR-9390) are on the upstream history, not named in this tree's search.

## Reproduction attempts

Not run. A seeded run of the test at the pinned head, with the ticket's seed `49939C8CF78131E7`, is the check; the flake is a timing failure and one run does not settle it.

## Gap analysis

The test has changed since 2016 (the directToLeaders use is gone), so the 2016 failure may no longer apply. Whether the test still flakes on this head is the open question.

## Verdict justification

keep-open meets the rubric: attempts made are the test-presence check and the history read; the missing piece is a seeded run on this head. close-fixed is not supported: the fix commits are upstream and their effect here is not measured. close-obsolete is not supported: the test exists.

## Limits

This record does not establish the flake's current rate. It does not read CloudSolrClient.directUpdate at the pinned head.
