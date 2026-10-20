# Skip record: SOLR-13237

- Ticket: SOLR-13237, Not all types of index corruption garuntee a leader will "give up its leadership".
- Verdict: keep-open.
- Draft branch: solr-13237-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 6 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-02-09, updated 2025-12-18, no fix versions. Links: "Incorporates LUCENE-8692 (Open)"; "Reference SOLR-15029 (Closed)"; "Reference SOLR-13027 (Open)". The report: external index corruption can make a leader reject updates without giving up its leadership, because the tragic-event check depends on Lucene's classification of the exception (LUCENE-8692). Fourteen comments (2019-02-09 to 2025-12-18). The 2019 AwaitsFix commits (hossman, March 2019) disabled `LeaderTragicEventTest` until the corruption detection is reliable. A 2020 comment proposes injecting a tragic event by hand. In 2025 janhoy hardened the test (SOLR-18025) and the test failed during teardown.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/handler/RequestHandlerBase.java:321-341` (`processReceivedException`): when `coreContainer.checkTragicException(core)` is true and the container is ZooKeeper-aware, the leader gives up leadership. The criterion is the tragic check, not the corruption itself.
- `solr/core/src/java/org/apache/solr/core/CoreContainer.java:2467` (`checkTragicException`): the check is unchanged in this pass, and it relies on the writer's tragic exception, which is the Lucene behavior LUCENE-8692 covers.
- `solr/core/src/test/org/apache/solr/cloud/LeaderTragicEventTest.java:134-137`: the test retries up to three times "to ensure the tragic event is properly triggered" and names LUCENE-8692 as the reason ("getTragicException() may not reflect all exceptions"). This is the workaround in the test, not a change to the product criterion.
- The 2019 AwaitsFix annotation (commit `e827dc2234`, an ancestor of the pin) is no longer on the test; `5d8220c599` (SOLR-18025, an ancestor) removed the disabling and added the retry.

## History

- `e827dc2234` (2019-03-06, "SOLR-13237: AwaitsFix LeaderTragicEventTest until we can make the underlying corruption detection reliable"): ancestor of the pin.
- `5d8220c599` (SOLR-18025, "Test LeaderTragicEventTest flaky"): ancestor of the pin; test-only change (41 insertions, 11 deletions in `LeaderTragicEventTest.java`).
- No commit at the pin changes `checkTragicException` or the leader criterion for this ticket.

## Reproduction attempts

Not run. This pass ran no builds or tests. A run of `LeaderTragicEventTest` at the pin with the corruption injection, repeated over seeds, would show whether the leader keeps leadership after a corruption that is not classified as tragic; the test retries are the only mitigation in the tree.

## Gap analysis

- The gap is in the product criterion for giving up leadership, which depends on Lucene's tragic classification (LUCENE-8692, Open). The product code at the pin does not broaden it, and the ticket records no agreed fix shape.
- The test-side fix (retries) is a workaround for the test, not for the product behavior.
- Closing needs either the upstream Lucene fix plus a Solr change, or a maintainer decision that a retry-based test is the accepted coverage. Neither is recorded on the ticket.

## Verdict justification

keep-open: the criterion the ticket names is present and unchanged at the pin, the upstream issue it depends on (LUCENE-8692) is open, and no fix shape has been agreed. close-fixed does not fit (no fix commit on the product path). close-obsolete does not fit (the feature is present). pr-needed needs the fix shape and a run.

Missing for this verdict: a run showing the non-tragic corruption case at the pin, and an agreed fix shape (the Lucene fix, or a Solr-side criterion).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
