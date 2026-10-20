# Skip record: SOLR-9427

- Ticket: SOLR-9427, "TestReplicationHandler.testRateLimitedReplication() failure". JIRA status read 2026-10-20T14:09:53Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: pr-needed. This replaces the recorded keep-open: the assertion that fails is a timing check with whole-second rounding on both sides, and the fix is test-only.
- Draft branch: solr-9427-draft-pr (the earlier keep-open branch solr-9427-draft-open stays as the prior record).
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T14:09:53Z (UTC).

## Ticket reading

A seeded branch_6x run of `testRateLimitedReplication` fails with a bare AssertionError at line 1410. The reporter says it reproduces only without the method filter. A 2016 comment proposes more timing precision and the actual and expected values in the assertion message.

## Code reading (pinned head)

- `solr/core/src/test/org/apache/solr/handler/TestReplicationHandler.java:1385-1450`: `approximateTimeInSeconds` is `Math.round(totalBytes / 1024 / 1024 / 0.1)` (line 1419-1420), an integer. `timeTakenInSeconds` is `TimeUnit.SECONDS.convert(timeTaken, NANOSECONDS)` (line 1452), which truncates.
- The check is `assertTrue(timeTakenInSeconds - approximateTimeInSeconds > 0)` (line 1459). Both sides are rounded or truncated to whole seconds, so a throttled replication that takes slightly longer than the estimate can still give a difference of zero or less, and the check fails. The message carries no values.
- The throttle itself is set by `solrconfig-leader-throttled.xml` (`solr/core/src/test-files/solr/collection1/conf/`), which the test copies at line 1393.

## History

- evidence.py at the pinned head: no commits naming SOLR-9427.

## Reproduction attempts

Not run in this pass. The reproduction is the seeded run from the ticket (`-Dtests.seed=8D4745BDBC59A3E3`); the 2016 seed may not reproduce on this head, so the record names it as a check, not a result.

## Gap analysis

The failing assertion is a rounding-sensitive timing check. The fix the ticket proposes (millisecond precision and values in the message) addresses it directly; the rounding is in the test code only.

## Verdict justification

pr-needed meets the rubric: the code-path argument with the lines that cause the failure (TestReplicationHandler.java:1419-1420, 1452, 1459), and the change the PR would make (test-only: compare in milliseconds and include actual and expected values in the assertion). The PR itself is not opened here.

## Limits

This record does not run the test with the seed. It does not change the throttle configuration.
