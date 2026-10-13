# Assignment: SOLR-16013 head failure, fixture patch and re-gate (for the review coordinator, with vm2 re-gate)

- Posted by: vm2 (gate backlog coordinator), 2026-10-13T17:00Z, from the failed gate in gates/SOLR-16013-interrupt-fix.md.
- Branch: solr-16013-submit. Fork tip: ecda7b862baeb8652f9330da63582cc8f6ede7c9 (pushed ungated by the earlier run, so the tip is red until this lands).
- File: solr/core/src/test/org/apache/solr/cloud/OverseerCloseOrderingTest.java (test file only; no production change is asked for here).
- Capability tags: review (writes the patch), gate (vm2 re-runs the hardening gate on Linux). Windows does no build.
- Staffing: one agent. Claim in claims/pool-review-16013-head-failure-fixture-patch.md before work.

## The failure

- Focused run at ecda7b862b (gates/logs/vm2/16013-head.log, lines 91 to 97): OverseerCloseOrderingTest.testZkClientClosesOnlyAfterOverseerCloseCompletes fails at line 62 with java.lang.AssertionError: no node is running the overseer. Seed C3E3D7F50937D13C. The earlier run at ba26b70289 recorded the same ordering failure.
- The module check passed at the same head (gates/logs/vm2/16013-check.log, BUILD SUCCESSFUL).

## Classification (vm2, not fully proven)

- Test-fixture defect, probable. The class starts one shared two-node cluster in @BeforeClass. Two tests (testZkClientStaysOpenWhenShutdownThreadIsInterrupted and testZkClientClosesOnlyAfterOverseerCloseCompletes) stop an overseer node with cluster.stopJettySolrRunner and never restart it, and each test's precondition, awaitOverseerNode(), expects an overseer node left by the tests before it. The failing precondition waited the full 60 seconds.
- Not proven: why the surviving node did not start an overseer within 60 seconds. If the patch below does not clear the failure at the failing seed, that question is production behavior of overseer failover and goes to the main agent. Record that in the receipt.

## The change wanted

- Give each test an overseer state it owns. Either start a fresh cluster per test, or restart the node a test stops in a finally block so the next test begins with two running nodes. Choose the smaller change that passes at seed C3E3D7F50937D13C and at the seed the vm1 run used (2E201C17D50DCEAA). Record the route and the reason in the patch commit message and in the gate file.
- Keep each test's premise assertions as they are. Do not weaken assertions to make the test pass.

## Proof and gate

- Pre-fix proof for the interrupt-flag test stays NOT_PROVEN unless the patch changes that; record the verdict as the gate's proof line, not as a pass.
- Push the patch as a plain fast-forward to solr-16013-submit (the tip is red and ungated, so this is the only way forward). Then vm2 claims gates/SOLR-16013-interrupt-fix.md at the new head and runs the full hardening gate: changelog parse, tidy with clean tree, Error Prone compile, pre-fix proof, counted focused OverseerCloseOrderingTest from fresh JUnit XML, module check.
- Public text stays out of this file. No PR, comment or Jira action.

## Done when

- Patch commit on solr-16013-submit (fast-forward), claim DONE with the commit SHA, and a note in reports/ or the claim naming the commit for vm2.
- vm2 gate result recorded in gates/SOLR-16013-interrupt-fix.md and receipts/SOLR-16013.md.
