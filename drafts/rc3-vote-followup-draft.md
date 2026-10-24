# Dev-list draft: RC3 vote thread follow-up (not sent)

Status: DRAFT. Based on vm5's final smoke report and the comparison (`reports/rc3-smoke-loop-round-1.md`, `reports/rc3-smiley-failure-comparison-round-1.md`). Nick Shanin sends this himself if he decides to; nothing is sent by the factory.

---

A data point from running the RC3 smoke tester in a loop, following David's post. My runs were on JDK 21.0.12.1 (aarch64). Five runs completed and all five failed the build. The same tests failed across runs and seeds: TestTlogReplica.testKillLeader and testRecovery in all five (testOnlyLeaderIndexes in four), CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure in all five, and TestCoordinatorRole.testNRTRestart in all five.

Two of these do not surprise me: testNRTRestart has a fix in open PR #5106 that is not in RC3, and RecoveryAfterSoftCommitTest, which failed in one of my runs, has its fix in PR #5098, also not in RC3. The TestTlogReplica failures and the CoreAdminHandler one have no fix I know of.

On David's metrics point: one of my runs had TestTermsQParserPlugin fail with 16 unreleased SolrMetricsContext objects, which may be the same category he saw on the early JDK 21. My JDK was 21.0.12.1 throughout, so the JDK version does not explain my failures either way.

The caveat: all five runs were on one small ARM host, so I cannot separate a host effect from an artifact effect. I did not run the BATS tests or JDK 25. Happy to share logs and seeds if they are useful.
