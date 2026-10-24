# Jira ticket draft: CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure (NOT filed)

Status: DRAFT. Filing needs Nick Shanin's go on this text. Suggested fields: type Test, affects 10.1.0 RC3. Note for the owner: SOLR-9433 (Reopened) covers the SolrCore clean-up path this test exercises; filing new keeps the test failure separate from that product bug. The alternative is a comment on SOLR-9433 instead.
Title: CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure fails repeatedly in the 10.1.0 RC3 smoke tests

h2. What was seen

In five completed runs of the Solr 10.1.0 RC3 smoke tester (artifacts rev {{749712b71940c9f96d824c533f32f5c56dcd3924}}, JDK 21.0.12.1, aarch64), the build failed every time, and {{CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure}} failed in all five runs, across different random seeds.

h2. Caveat

All five runs were on one small ARM host (4 CPUs, 7 GB), so a host effect is not yet separated from a code or test cause. The failure repeats across seeds on that host; whether it reproduces elsewhere is part of what this ticket tracks.

h2. Related

SOLR-9433 covers the SolrCore clean-up path this test exercises. Related RC3 smoke failures: RecoveryAfterSoftCommitTest is SOLR-18530 (fix in PR #5098) and TestCoordinatorRole.testNRTRestart is SOLR-16630 (fix in PR #5106); neither fix is in RC3.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
