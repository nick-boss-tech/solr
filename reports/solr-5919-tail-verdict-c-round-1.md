# Verdict: SOLR-5919, AbstractFullDistribZkTestBase control server thinks it is part of the cloud, takes overseer role

Verdict: STILL LIVE, promote to implementation, not closure. On current
main the control node still starts first, still joins the Overseer
election under the default node roles, and still can never be killed by
the ChaosMonkey, so the coverage gap the ticket's discussion ends on,
that ChaosMonkey tests never exercise Overseer death, is still open.
Nothing closes without a maintainer's or Nick's go.

Ticket: https://issues.apache.org/jira/browse/SOLR-5919 (2014, reporter
Chris Hostetter, Open, Major, last updated 2018-01-11, no fix version).
It is linked "Required" to SOLR-5823, which stands Resolved Incomplete,
so the motivating utility need never landed either. The description
says the control server thinks it runs in cloud mode in a cluster of
itself and acts as Overseer, while no node in the tested cluster thinks
it is the Overseer.

## Discussion read (Jira, comments end 2014-10-31)

Shalin's 2014-03-27 reply says the control server belonging to the
cluster is as designed: one Overseer across the whole ensemble, and it
can be any node aware of the same ZooKeeper. Miller agrees. Hostetter
then raises two concerns: the control-versus-cloud comparison is really
cloud multi-shard versus cloud single-shard, and ChaosMonkey shutting
down the control node could corrupt comparisons. Miller answers that
the comparison shape is expected and that the monkey will not shut
anything in the control collection, then reframes the ticket: the real
gap is missing coverage, because the control collection is safe and
therefore Overseer kills are never exercised in ChaosMonkey tests.
Shalin suggests resolving as invalid; Hostetter defers but restates
the crux question, that if the control collection stays in the cloud
it will get elected Overseer quickly and then the Overseer is never at
risk; Miller's last word (2014-10-31) is that he does not know how
whoever solves it will tackle it, and lists three shapes: start the
control node after the cluster jetties so it ends up last in line,
use the overseer-preference mechanism to prefer the collection1
jetties, or a deeper separation of the control node from the cloud.
No later comment adopts one, and the ticket stays Open.

## Code read (current main as checked)

Checked against the local main checkout at e55d2e7bcb (2026-09-29).
None of Miller's three shapes is implemented:

- The control node still starts first. AbstractFullDistribZkTestBase
  .createServers
  (solr/test-framework/src/java/org/apache/solr/cloud/AbstractFullDistribZkTestBase.java:378-383)
  creates and starts controlJetty, creates control_collection pinned
  to it with setCreateNodeSet, and only then calls initCloud and
  createJettys for the cluster nodes. The "start control after the
  jetties" shape was never done.
- The control node still joins the Overseer election. Nothing in the
  test framework sets node roles for the control jetty, so it runs
  under the default roles "data:on,overseer:allowed"
  (solr/core/src/java/org/apache/solr/core/NodeRoles.java:32).
  ZkController.init
  (solr/core/src/java/org/apache/solr/cloud/ZkController.java:1150-1155)
  joins the election whenever
  cc.nodeRoles.isOverseerAllowedOrPreferred(), and the same gate
  applies after session expiration (480-492). There is no
  overseer:disallowed, overseer designate, or preference set for
  the control node anywhere in the test framework: a grep for
  ADDROLE, OverseerRole, and overseer preference across
  AbstractFullDistribZkTestBase and ChaosMonkey returns nothing,
  so the second shape was never done either.
- The ChaosMonkey still cannot kill the control node. Its kill set
  comes from shardToJetty, populated only inside createJettys
  (AbstractFullDistribZkTestBase.java:979) for cluster nodes;
  controlJetty is never added to it. ChaosMonkey
  (solr/test-framework/src/java/org/apache/solr/cloud/ChaosMonkey.java)
  stops jetties drawn from those maps only, and has no Overseer
  kill path at all: no reference to stopping the Overseer node.
  The control node starts first, never goes down, and is never a
  kill target, which is exactly the "elected pretty quick, then
  never at risk" shape Hostetter named.

A nuance, stated so the verdict is not overstated: in distributed
cluster-state mode the elected Overseer still exists for cluster
singleton processing (ZkController.init comment, "even when using
distributed processing, we still create an Overseer anyway"), so the
election-participation point does not dissolve under the
distributed default. And the adjacent coverage ask is independently
still open: this program's SOLR-4944 verdict found
ChaosMonkeyShardSplitTest still @Ignore("SOLR-4944") on the same
checkout, with its Overseer-kill TODO still in place.

## Relation to the earlier skip record

A 2026-10-19 pool record (fork branch solr-5919-draft-open, commit
c3bcee01e0587560b3dbd71f3248b279ef015775) already reached
keep-open for this ticket from a code reading, noting
AbstractFullDistribZkTestBase still creates and uses
control_collection, but it states Jira was not re-read for it and
no reproduction was attempted. This record re-read Jira and
re-ran the code read on current main, and agrees with its
conclusion while adding the election-eligibility and kill-set
evidence that turns "code path present" into the specific live
mechanism. Nothing from that draft has been posted to Jira; this
record posts nothing to Jira either.

## What was not done

No test run: the verdict rests on a code read plus the ticket's
own discussion, which settles it, since every element of the
reported mechanism (first start, election eligibility, never a
kill target) is still present in the tree. No reproduction with
logging was attempted; a future implementation assignment could
confirm the control node holding the Overseer role by logging the
elected Overseer node in a ChaosMonkey run, but the verdict does
not need it. No Jira post, no closure, no PR. This record is a
verdict only.

## Next step

Implementation assignment, not a closure draft. The narrowest
slice follows Miller's own list: either start the control jetty
after the cluster jetties, or keep the start order and exclude
the control node from the Overseer election (overseer:disallowed
role for the control node, or overseer designates preferring the
collection1 nodes), with a ChaosMonkey run that logs the elected
Overseer node as the check that the Overseer is at risk again.
The assignment should also decide, explicitly, whether the
control-versus-cloud comparison shape (cloud single-shard, not
standalone) stays as designed per Miller, since that half of the
ticket was answered as designed and needs no code change.

Posted by the phone agent overnight 2026-11-01, as item 3 of the
overnight backlog (direct verdict record, batch C, smallest ticket
first after SOLR-5833). Source assignment:
assignments/overseer-tail-reproductions-c-round-1.md, claim 7.
