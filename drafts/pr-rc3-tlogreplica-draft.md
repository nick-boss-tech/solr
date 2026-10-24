# PR description draft

Status: DRAFT, cannot open yet: the root-cause unit (assignments/rc3-repeating-failures-rootcause-round-1.md, unit 1) has not reported, and the work has no Jira ticket yet (none exists for this test's flakiness; filing one needs the owner's go). Title subject to the ticket.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

SOLR-XXXX: Fix TestTlogReplica failures seen in the 10.1.0 RC3 smoke tests

## Summary

In five completed runs of the Solr 10.1.0 RC3 smoke tester (JDK 21.0.12.1), `TestTlogReplica.testKillLeader` and `testRecovery` failed in all five runs and `testOnlyLeaderIndexes` in four, across different random seeds. The runs were on one small ARM host, so a host effect is not yet separated from a code cause; the root cause section below is filled in before this opens.

## Root cause

(To be completed from the root-cause report: reproduction at main and at the RC3 tip, the mechanism, and why the fix addresses it.)

## Tests

(To be completed: the focused runs at the fix head, with counts, and the same runs on the base for comparison.)

## Related

The RC3 smoke tester failed repeatedly on a small set of tests. The fixes are #5098 (RecoveryAfterSoftCommitTest), #5106 (TestCoordinatorRole.testNRTRestart), this PR (TestTlogReplica), and two more in preparation (CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure; TestTermsQParserPlugin, seen once).

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
