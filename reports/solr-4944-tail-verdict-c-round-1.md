# Verdict: SOLR-4944, ChaosMonkeyShardSplitTest does not kill the Overseer

Verdict: STILL LIVE (test defect, not fixed by later work found).

Ticket: https://issues.apache.org/jira/browse/SOLR-4944 (2013, reporter
Shalin Shekhar Mangar, Open, Major, unassigned). Ticket text, read
2026-11-01: "ChaosMonkeyShardSplitTest does not actually kill the
overseer so it is not very useful in testing splits in failure
scenarios." The ticket's comments end in 2013: Shalin disabled the test
until it is fixed (2013-06-21), Mark Miller asked why it does not kill
the Overseer yet (2013-11-25), Shalin said he would take a stab at it
(2013-11-25). No later comment or fix is recorded on the ticket.

## Code read (current main as checked)

Checked against the local main checkout at e55d2e7bcb (2026-09-29),
file solr/core/src/test/org/apache/solr/cloud/ChaosMonkeyShardSplitTest.java:

- Line 48: the class is still `@Ignore("SOLR-4944")`. The test runs
  nowhere while ignored, so the split-in-failure scenario it was meant
  to cover gets no coverage from this test.
- Lines 141-142: the split section still reads
  `// Kill the overseer` followed by
  `// TODO: Actually kill the Overseer instance`. The TODO from the
  original report is still in the source.
- The `OverseerRestarter` helper (lines 173 onward) does not kill the
  cluster's Overseer. Its `electNewOverseer` builds a new `Overseer`,
  calls `overseer.close()` on it before joining the election, and
  returns only the helper's own `SolrZkClient`. The killer loop then
  closes that client and re-elects. Closing the test helper's own
  ZooKeeper client never stops the Jetty node that hosts the cluster's
  Overseer and never closes the cluster Overseer itself. The
  `splitShard` call that follows therefore runs with no real Overseer
  kill, exactly the defect the ticket reports.
- The premise is not retired by later architecture changes found here:
  `solr/core/src/java/org/apache/solr/cloud/api/collections/SplitShardCmd.java`
  still enqueues Overseer queue operations (`QUEUE_OPERATION` for
  DELETESHARD, CREATESHARD, ADDREPLICA, and UPDATESHARDSTATE), so the
  split path still touches Overseer work. No later change was found
  that makes the test kill the Overseer, removes the TODO, or lifts
  the `@Ignore`.
- Note, not a fix: `OverseerTest` has a helper of the same name
  (`OverseerRestarter`) with the same client-close pattern. It is a
  separate test's helper, not a fix for this test.

## What was not done

No test run: the verdict rests on a code read, which settles it, since
the test is ignored and its killer code on its face does not touch the
cluster Overseer. No Jira post, no closure, no PR. This record is a
verdict only.

## Next step

Promote to an implementation assignment, not a closure draft. The fix
is one of two shapes, for the implementer to settle against current
code: make the killer actually stop the node hosting the Overseer
(identify it through the Overseer election state, stop that Jetty, run
the split, restart it), or delete this test if split-failure coverage
elsewhere is judged sufficient and the ignored test is dead weight.
Either shape needs its own proof pair; un-ignoring the test without
fixing the killer would just re-enable a test that does not test what
it claims.

Posted by the phone agent overnight 2026-11-01, as item 3 of the
overnight backlog (direct verdict record, batch C, smallest ticket
first). Source assignment:
assignments/overseer-tail-reproductions-c-round-1.md, claim 9.
