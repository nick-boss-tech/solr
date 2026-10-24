# Jira ticket draft: TestTermsQParserPlugin unreleased SolrMetricsContext objects (NOT filed)

Status: DRAFT. Filing needs Nick Shanin's go on this text. Suggested fields: type Bug, affects 10.1.0 RC3. This is the one of the three that may be a product bug (metrics contexts created and not released), not only a test problem.
Title: TestTermsQParserPlugin reports 16 unreleased SolrMetricsContext objects in the 10.1.0 RC3 smoke tests

h2. What was seen

In one of five completed runs of the Solr 10.1.0 RC3 smoke tester (artifacts rev {{749712b71940c9f96d824c533f32f5c56dcd3924}}, JDK 21.0.12.1, aarch64), {{TestTermsQParserPlugin}} failed at class teardown: "ObjectTracker found 16 object(s) that were not released". All 16 were {{SolrMetricsContext}} objects created through {{CoreAdminHandler.initializeMetrics}} during {{CoreContainer.loadInternal}}.

h2. What is not known yet

The failure appeared in one run of five, so whether it repeats, and which path creates the contexts without releasing them, is still to be established. A similar category of failure (a metrics-related test failing) was reported on the RC3 vote thread by David Smiley on an early JDK 21 build; he did not name the test, so whether it is the same failure is unknown.

h2. Related RC3 smoke failures

RecoveryAfterSoftCommitTest is SOLR-18530 (fix in PR #5098) and TestCoordinatorRole.testNRTRestart is SOLR-16630 (fix in PR #5106); neither fix is in RC3.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
