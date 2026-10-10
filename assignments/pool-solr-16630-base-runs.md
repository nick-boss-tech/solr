# Pool assignment: SOLR-16630 base runs and changelog title fix

Capability tags: `premise-run`, `implementation`. Intended host: vm2 (it built and gated the branch). Staffing: 1. Claim path: claims/pool-solr-16630-base-runs.md.

Background: branch solr-16630-submit at 9bea59741ac30ffd11ed11be5269ea025cf17f68 is GATE GREEN (receipts/SOLR-16630.md). The flaky-fix review (slice 4) held the draft's proof claim on owner decision O1: no run of TestCoordinatorRole.testNRTRestart on the base code is on record, so the change has not been shown to fail without the fix. The main side's decision: run the base check, then open either way.

Work:
1. Base runs: on the merge-base of the branch (upstream main at the branch's base), run org.apache.solr.cloud.TestCoordinatorRole.testNRTRestart with seed 681E2A715B2CE1D3, up to 6 runs, stopping early on a failure. Record each run's outcome; a failure counts as premise evidence only if its shape matches the t3 mechanism (reports/flaky-tests-root-cause-round-1-t3.md): the stop of the PULL node landing while the first add is in flight. Any other failure shape is recorded as its own finding, not as premise proof.
2. Changelog title fix on the branch: the fragment title claims the test "no longer stops the PULL node while an add through it is still in flight", but only the first add is awaited. Retitle it to state that scope, for example: "TestCoordinatorRole.testNRTRestart waits for the first add to complete before stopping the PULL node". Changelog-only commit; the gate at the previous head carries over the changelog-only delta. Keep the YAML valid.
3. Push the branch, and update receipts/SOLR-16630.md with the base-run outcomes and the new head. Mark this claim DONE in the same push.

Rules: WORKFLOW.md binds. One Gradle build at a time on the host; GRADLE_USER_HOME stays under /workspace on vm2.
