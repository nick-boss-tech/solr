# Jira ticket draft: TestTlogReplica failures (NOT filed)

Status: DRAFT. Filing needs Nick Shanin's go on this text. Suggested fields: type Test, component solrj or core tests (filing choice), affects 10.1.0 RC3.
Title: TestTlogReplica testKillLeader, testRecovery and testOnlyLeaderIndexes fail repeatedly in the 10.1.0 RC3 smoke tests

h2. What was seen

In five completed runs of the Solr 10.1.0 RC3 smoke tester (artifacts rev {{749712b71940c9f96d824c533f32f5c56dcd3924}}, JDK 21.0.12.1, aarch64), the build failed every time. {{TestTlogReplica.testKillLeader}} and {{TestTlogReplica.testRecovery}} failed in all five runs and {{testOnlyLeaderIndexes}} in four of the five, across different random seeds (for example {{787C6229591C6F1E}}, {{8D8640B367EDAD08}}, {{5AB00AEB1AE2392}}).

h2. Caveat

All five runs were on one small ARM host (4 CPUs, 7 GB), so a host effect is not yet separated from a code or test cause. The failures repeat across seeds on that host; whether they reproduce elsewhere is part of what this ticket tracks.

h2. Related RC3 smoke failures

RecoveryAfterSoftCommitTest is SOLR-18530 (fix in PR #5098) and TestCoordinatorRole.testNRTRestart is SOLR-16630 (fix in PR #5106); neither fix is in RC3. This ticket covers the TlogReplica methods, which have no fix yet.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
