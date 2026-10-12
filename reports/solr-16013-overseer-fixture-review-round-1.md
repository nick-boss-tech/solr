# SOLR-16013 overseer fixture, review step 1

Verdict: **UNDECIDED from reading.** The failure is not established as a test-only defect. Reading the code points to a candidate production mechanism in the branch's own new close path in `ZkController`. Per the assignment's step 1, that is a stop-and-report result, so this round writes no test-only patch request (step 2 is withheld). The question needs a run, which this Windows host does not do.

Assignment: `assignments/solr-16013-overseer-fixture.md`. Claim: `claims/pool-review-16013-overseer-fixture.md`. Branch head `ba26b7028917ef29d5b5e1cfe75ee83862cccf63` (ls-remote confirmed), base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` (merge-base with upstream/main). One read-only subagent, as the assignment names. Nothing was edited, built, run or posted.

## What the reading shows

- **Survivor's election path is unchanged by the branch.** `OverseerElectionContext.java`, `LeaderElector.java`, `ElectionContext.java` and `Overseer.java` are identical at base and head. The survivor's watch on the node ahead (`LeaderElector.java` lines 137 to 151) fires when the old leader's ephemeral node is deleted, then it becomes leader and starts the overseer (`OverseerElectionContext.java` lines 60 to 68).
- **The old leader's node disappears only when its ZooKeeper session closes.** `OverseerElectionContext.close()` (lines 80 to 82) does not remove the election node. The branch does not change that.
- **The branch changes the close order in `ZkController.java` (head lines 876 to 936).** At base, the overseer close is submitted to a pool and not awaited before the ZooKeeper client closes. At head, the close waits on the overseer future through interrupts (lines 913 to 927), then closes `zkStateReader` and `zkClient` (lines 930 and 936). The interrupt flag is restored at line 926, before those two closes run.
- **The second test method depends on the first.** `OverseerCloseOrderingTest.java` starts a 2-node cluster in `@BeforeClass` (lines 38 to 41). The first method stops the overseer node (line 136) and nothing restarts it. `awaitOverseerNode()` (lines 45 to 56) waits 60 seconds for a node with an updater thread, and line 61 asserts it is not null. This dependence is a fixture defect whatever the production answer is.

## The open question

The survivor could win only if the old leader's session closes and its node is deleted. The branch's close path runs `zkClient.close()` with the interrupt flag set (head line 926 sets it, line 936 closes the client). `SolrZkClient.close()` (`solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/SolrZkClient.java` lines 826 to 836) then closes the Curator client with that flag still set. The reading does not show what the client close does with the flag set, so whether the old session closes in the interrupt-method run is not settled. If it does not close, the old node stays until the session times out, and the survivor's watch never fires in the window the test waits.

So there are two candidate causes, and the reading cannot separate them:

1. **Test-state:** the second method inherits a cluster with no overseer node after the first method's stop, and the fixture should give each method its own cluster or restore one.
2. **Production (branch code):** the new close path leaves the old session open when the interrupt flag is set, so the survivor never takes over. The gated head had one method, so this ordering was never exercised before this head.

Both can be true. The fixture fix does not remove cause 2.

## What would settle it (not run here)

- A focused run that executes only the close-ordering method at the head, to see whether it passes without the interrupt-method leftover.
- A run of the interrupt method alone with a check on whether the old leader's ephemeral node is deleted when the test ends.

These need a Linux host with a gate or premise-run tag. The Windows host does not build or run tests.

## Recommendation for the main agent

Do not write the test-only patch yet. Hold the SOLR-16013 gate and report to the main agent that the failure has a production candidate in the branch's `ZkController` close path (head lines 913 to 936). If the settling run shows cause 2, the fix belongs in production code and needs a separate decision. If it shows cause 1, the test fixture fix in step 2 of the assignment applies, and the fixture should still give each method its own cluster.

## Not checked

- The SOLR-16013 gate log (`logs/vm2/16013-gate.out`), which is on VM2.
- The Curator and ZooKeeper client code behaviour with an interrupted thread. The reading names the call but does not show its behaviour.
- The test run order. The reading shows the stop and restart lines, not the order JUnit uses.

## Convergence line

UNDECIDED. The assignment's step 1 is not settled by reading, and the step 2 test-only patch request is withheld until a run decides between the two causes. Nothing was edited, built, run or posted.
