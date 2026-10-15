# Assignment: SOLR-16013 focused test fixture fix (patch request)

Status: DONE

Posted 2026-10-12 by vm2 (gate backlog coordinator) after the gate failed. Requested from the review coordinator (Windows) under the gate failure rule in material/coordinator-handoff.md.

- Branch: solr-16013-submit (live tip ba26b7028917ef29d5b5e1cfe75ee83862cccf63). Fork submit branch: solr-16013-submit on the fork.
- File: solr/core/src/test/org/apache/solr/cloud/OverseerCloseOrderingTest.java
- Gate: gates/SOLR-16013.md, FAILED at the tip (logs/vm2/16013-gate.out, GATE END 2026-10-12T09:55:48Z).
- Failing test: testZkClientClosesOnlyAfterOverseerCloseCompletes. Message: `java.lang.AssertionError: no node is running the overseer`, at OverseerCloseOrderingTest.java:61.
- Classification: test-fixture defect, with one open question. Both test methods share a 2-node cluster from @BeforeClass. testZkClientStaysOpenWhenShutdownThreadIsInterrupted runs first and stops the overseer node (127.0.0.1:33597_solr) without restarting it. The survivor (127.0.0.1:36253_solr) never logs winning the overseer election during the second method's 60-second wait, so awaitOverseerNode() returns null.
- Evidence: solr/core/build/test-results/test/outputs/OUTPUT-org.apache.solr.cloud.OverseerCloseOrderingTest.txt, lines 172 to 308 (the first method stops the node at log 9254; the second starts at 14441 and fails at 74484).

## Change wanted

1. Decide whether the surviving node failing to take over is a test assumption or a production problem in the overseer election path after the leader's ZooKeeper session closes. Read OverseerElectionContext and the close path in ZkController. If it is production, stop and report it to the main agent; do not patch production code under this assignment.
2. If it is test-only: make the two methods independent. Preferred: each method gets its own cluster, or the stopping method restores a full cluster before it returns. Pick the narrowest option that keeps both assertions meaningful. Do not weaken the ordering assertion.
3. Pre-fix proof: with production reverted to the merge base, the fixed test must fail for the premise reason (the ordering between overseer close and ZooKeeper client close), checked by message content, not by the failure count.
4. Commit under Nick Shanin's identity, no trailers, push to the fork submit branch as a fast-forward (or with the force-with-lease rule if the branch was rebuilt), then mark the claim and the gate file with the new head.

## Gate after the patch

vm2 runs the full hardening gate at the new head (changelog parse, tidy clean, Error Prone compile, proof leg, counted focused tests from fresh JUnit XML for OverseerCloseOrderingTest, module check). The gate job is re-posted in gates/SOLR-16013.md with the new head. The Windows host does not run builds.

## Staffing and tags

Tags: review (the reading and the patch request), implementation (the patch, on a Linux host), gate (vm2). One subagent at most.
