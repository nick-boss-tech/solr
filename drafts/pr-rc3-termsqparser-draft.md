# PR description draft

Status: DRAFT, held behind the watch unit (assignments/rc3-repeating-failures-rootcause-round-1.md, unit 3): this opens only if the metrics leak repeats in focused runs. No Jira ticket exists for it yet.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

SOLR-XXXX: Fix unreleased SolrMetricsContext objects reported by TestTermsQParserPlugin in the 10.1.0 RC3 smoke tests

## Summary

In one of five completed runs of the Solr 10.1.0 RC3 smoke tester (JDK 21.0.12.1), `TestTermsQParserPlugin` failed its class teardown with "ObjectTracker found 16 object(s) that were not released", all of them `SolrMetricsContext` objects created through `CoreAdminHandler.initializeMetrics` during `CoreContainer.loadInternal`. Whether the leak repeats is being established before this opens.

## Root cause

(To be completed if the leak repeats: which path creates the contexts without releasing them, and the fix.)

## Tests

(To be completed: focused run counts at the fix head and on the base.)

## Related

The RC3 smoke tester failed repeatedly on a small set of tests. The fixes are #5098 (RecoveryAfterSoftCommitTest), #5106 (TestCoordinatorRole.testNRTRestart), this PR (TestTermsQParserPlugin), and two more in preparation (TestTlogReplica; CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure).

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
