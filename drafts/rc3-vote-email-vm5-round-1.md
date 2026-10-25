Subject: Re: [VOTE] Release Solr 10.1.0 RC3

Hi all,

I ran the RC3 smoke tester in a loop on one machine (aarch64, 4 CPUs, JDK 21.0.12.1), from the artifacts at rev 749712b71940. Five runs finished and all five failed the build. None passed. Two runs were cut short, one by a host reboot and one stopped on request, so I am not counting them.

Several failures repeat across all five runs and across different random seeds:

* TestCoordinatorRole.testNRTRestart. Its fix is PR #5106 (SOLR-16630), which is not in RC3: https://github.com/apache/solr/pull/5106 . The PR's post-fix run is still in progress, so its evidence is not complete yet.
* CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure. Filed as SOLR-18587: https://issues.apache.org/jira/browse/SOLR-18587 . No fix yet, and I have not found the cause.
* TestTlogReplica.testKillLeader and testRecovery, and testOnlyLeaderIndexes in four of the five runs. Filed as SOLR-18586: https://issues.apache.org/jira/browse/SOLR-18586 . No fix yet, and I have not found the cause.

Two other failures happened only once each:

* RecoveryAfterSoftCommitTest.test failed in one run. Its fix is PR #5098 (SOLR-18530), which is not in RC3: https://github.com/apache/solr/pull/5098 . Sanjay Dutt reported a 100-run pair for that fix. I am citing that as his result, not as mine.
* One run reported 16 unreleased SolrMetricsContext objects from TestTermsQParserPlugin, with a close-pool timeout in another test's teardown in the same run. That matches SOLR-18564 (PR #5192, open): https://github.com/apache/solr/pull/5192 . The fix's own pair is from its 2026-10-22 gate, not from my runs.

The repeating failures do not show up on the release artifacts I checked, and I have not run them anywhere else, so please treat this as one data point from one small machine. I have not checked whether any of these fail on 10.0.0 or on another host.

This message does not cast a vote.

Nick
