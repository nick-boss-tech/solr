# Dev-list email draft: RC3 fix set (not sent)

Status: DRAFT. For the [VOTE] Release Solr 10.1.0 RC3 thread. Supersedes `drafts/rc3-vote-followup-draft.md` (this version links every fix vehicle, per the owner's direction). Nick Shanin sends it himself.

Subject: Re: [VOTE] Release Solr 10.1.0 RC3

---

A data point from running the RC3 smoke tester in a loop, and a pointer for each failure, since several already have fixes or tickets.

My runs used the RC3 artifacts (rev 749712b719) on JDK 21.0.12.1, aarch64. Five runs completed and all five failed the build. The same tests failed across runs and different random seeds:

* RecoveryAfterSoftCommitTest failed in one run. Its fix is PR #5098 (SOLR-18530), which is not in RC3: https://github.com/apache/solr/pull/5098
* TestCoordinatorRole.testNRTRestart failed in all five runs. Its fix is PR #5106 (SOLR-16630), also not in RC3: https://github.com/apache/solr/pull/5106
* TestTlogReplica.testKillLeader and testRecovery failed in all five runs, and testOnlyLeaderIndexes in four. No fix yet; tracked in SOLR-18586: https://issues.apache.org/jira/browse/SOLR-18586
* CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure failed in all five runs. No fix yet; tracked in SOLR-18587: https://issues.apache.org/jira/browse/SOLR-18587
* One run charged TestTermsQParserPlugin with 16 unreleased SolrMetricsContext objects. That matches SOLR-18564, where the root cause is the close-pool timeout throw in CoreContainer.shutdown() skipping the metrics-context close; the same run also had a closeThreadPool timeout in another test's teardown. A fix for SOLR-18564 is prepared and its PR is being put up: https://issues.apache.org/jira/browse/SOLR-18564

David, on your metrics point: my JDK was 21.0.12.1 throughout, so the version does not explain my failures, but the category you saw may be the SOLR-18564 one.

The caveat: all five runs were on one small ARM host, so I cannot separate a host effect from an artifact effect for the two untracked failures. I did not run the BATS tests or JDK 25. Happy to share logs and seeds if they are useful.
