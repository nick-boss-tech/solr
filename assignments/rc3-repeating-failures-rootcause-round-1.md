# Assignment: root-cause the repeating RC3 smoke failures (round 1)

Status: OPEN
Tags: premise-run, sweep, implementation

## Evidence in hand

The RC3 smoke loop's final report (`reports/rc3-smoke-loop-round-1.md`) shows five completed runs of the Solr 10.1.0 RC3 smoke tester on vm5 (JDK 21.0.12.1, aarch64), all BUILD FAILED, with the same tests failing across runs and different seeds:

- In all five runs: `TestTlogReplica.testKillLeader`, `TestTlogReplica.testRecovery`, `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure`, `TestCoordinatorRole.testNRTRestart`.
- In four of five: `TestTlogReplica.testOnlyLeaderIndexes`.
- Once each: `RecoveryAfterSoftCommitTest.test` (run 001) and `TestTermsQParserPlugin.classMethod` (run 003, "ObjectTracker found 16 object(s) that were not released", all `SolrMetricsContext` from `CoreAdminHandler.initializeMetrics`).

Two of these already have fixes in open PRs that are not in RC3 (testNRTRestart via PR 5106, RecoveryAfterSoftCommitTest via PR 5098) and are out of scope here. The TlogReplica methods and the CoreAdminHandler method have no known fix. The loop ran on one small ARM host only, so a host effect is not separated from an artifact effect; that separation is part of this assignment.

## Units (claim one at a time)

Unit 1: TestTlogReplica. Reproduce `testKillLeader` and `testRecovery` failing: first at current origin/main on this host class, then at the RC3 tip `749712b71940c9f96d824c533f32f5c56dcd3924`, repeated runs with the loop's seeds where recorded. If it reproduces, root-cause it (the routing and timeout family around tlog replicas is the starting hypothesis, not the conclusion). Deliverables: `reports/rc3-tlogreplica-rootcause-round-1.md`; if a cause is found and a fix follows from it, a branch in the usual shape and a gate job for it.

Unit 2: CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure. Same shape: reproduce at main and at the RC3 tip, root-cause, report at `reports/rc3-coreadmin-deleteinstance-rootcause-round-1.md`, fix branch if the cause supports one.

Unit 3 (watch item): TestTermsQParserPlugin metrics leak. Repeated focused runs of the class at the RC3 tip to see whether the unreleased SolrMetricsContext leak repeats; report at `reports/rc3-termsqparser-metrics-leak-round-1.md` with the run count and result either way. No fix in this unit; a repeat is what turns it into work.

## Rules

Every failure claim cites its log file and fresh JUnit XML on the claiming host. A run that does not reproduce is a result and gets reported as one, with counts. No Jira comment, no PR, and no dev-list text comes out of this assignment; reports go to the board and the owner decides the next step.
