# Verdict: SOLR-5699, test assert that a cluster never runs two Overseers at once

Verdict: STILL LIVE (test-only ask, not implemented by later work found).

Ticket: https://issues.apache.org/jira/browse/SOLR-5699 (2014, reporter
and assignee Mark Miller, Open, Major, last updated 2016-05-09). The
ticket carries no description text in Jira; the ask is the title, and
the only substantive comment is Miller's 2014-02-05 note: given the
right circumstances two Overseers would probably manifest as both
processing the same queues, but there is no guarantee, and then the
failure is a tricky exception to track down, so tests should use an
assert. The other comment is a 2014-04-16 version move to Solr 4.9.
No later comment, fix, link, or reference to this ticket was found on
the ticket or in the code.

## Code read (current main as checked)

Checked against the local main checkout at e55d2e7bcb (2026-09-29).
No source file references SOLR-5699, and no test asserts that exactly
one Overseer is running across a cluster:

- Every node holds an `Overseer` object, and only the election
  leader's runs: `OverseerElectionContext.runLeaderProcess` starts
  the Overseer for the election winner, while `joinedElectionFired`
  and `checkIfIamLeaderFired` close the Overseer on a node that
  loses or leaves the election. Leadership is a single ZooKeeper
  ephemeral node under `/overseer_elect/leader`. That is the
  production mechanism, not the test assert the ticket asks for.
- The nearest existing checks assert a single leader name, not a
  count of running Overseers. `OverseerRolesTest` waits for one
  leader node name via `getLeaderNode` and asserts a preferred node
  becomes that leader; `TestSkipOverseerOperations` asserts the
  Overseer status response carries a non-empty leader name. A second
  running Overseer would not fail either check on its face.
- The test helper that could host the assert does the opposite of
  asserting: `MiniSolrCloudCluster.getOpenOverseer` collects every
  node's Overseer, shuffles the list, and returns the first one
  whose `isClosed()` is false. If two Overseers were open, it would
  silently return one of them and never notice the other.
- No test counts open Overseers across `cluster.getJettySolrRunners()`
  and asserts the count is one, and no shared test base performs
  such a check after cluster startup or after leader failover.

## Scope note

The assignment flagged the distributed default (SOLR-17293). That
narrows, but does not retire, the ask: in distributed mode the
collection API bypasses the Overseer, so the assert belongs to
overseer-enabled clusters, where queue processing by two Overseers
is exactly the hazard Miller described. The natural shape is a
helper that counts non-closed Overseers across the cluster's nodes
and asserts one, run after startup and after an Overseer failover,
in overseer mode only.

## What was not done

No test run: the verdict rests on a code read, which settles it,
since the requested assert is absent from the test tree and no
existing check counts running Overseers. No Jira post, no closure,
no PR. This record is a verdict only.

## Next step

Promote to an implementation assignment, not a closure draft. The
work is the assert itself (count open Overseers across the cluster,
assert exactly one, overseer mode, after startup and after
failover), plus a demonstration that it would catch a second open
Overseer. It needs its own proof pair; the existing single-leader
checks above are not a substitute for it.

Posted by the phone agent overnight 2026-11-01, as item 3 of the
overnight backlog (direct verdict record, batch C, smallest ticket
first after SOLR-4944). Source assignment:
assignments/overseer-tail-reproductions-c-round-1.md, claim 8.
