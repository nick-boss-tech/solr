# Assignment: SOLR-8240 head failure, round 1

Status: DONE

Capability tags: [implementation, gate]. Intended host: vm1. Posted by the vm2 gate backlog coordinator on 2026-10-15T17:42:52Z, after the vm2 gate at the head failed. Per the failed-run rule, vm2 does not relaunch the gate.

Claim first: before any work, file a claim under claims/ naming this assignment, per WORKFLOW.md. The vm2 claim claims/pool-vm2-gate-backlog-8240.md is handed to this assignment and stays open until this assignment is DONE.

## Grounding

- Branch solr-8240-submit, head 97cd22159ba0d2dd2de9f8a5bdb52de93bd2bffa. Prior gated head fad7a1dd8e2397330e0973e809aeba60490d2f08. Diff scope against fad7a1dd8e: 3 files, 99 insertions (JsonLoader.java is not in that diff; it is a production change at the head against e432df19c4).
- The full run record and the vm2 facts are in gates/SOLR-8240.md, section "Gate run record, vm2, 2026-10-15".

## What failed

- Head run, seed 8240C0FFEE824001: TestInitParams.testUpdateJsonDocsInitParamsRejectsFieldMappings fails with "Expected exception SolrException but no exception was thrown". This is the same text the proof leg shows at the base (JsonLoader.java at e432df19c4), so the head does not reject a field mapping when the inherited mapUniqueKeyOnly=true comes from initParams.
- Also at the head: TestInitParams classMethod fails with "ObjectTracker found 147 object(s) that were not released!!!". Not verified: it may be a consequence of the failing test leaking cores.
- JsonLoaderTest is 33 of 33 at the head.

## Scope

1. Find out why the head does not reject the initParams case. Check whether the JsonLoader.java change at the head covers the initParams path, whether solrconfig-update-json-docs-initparams.xml is loaded by the test, and whether the test asserts the right thing.
2. If the head production code is wrong, the fix is a new fast-forward commit to solr-8240-submit, with a new gate job. Do not push a head whose gate is not green.
3. If the test is wrong, record the reason and escalate to the main agent before changing a test that the PR body describes.
4. Use the host's own gate runner with the blocking flock on the test queue. One Gradle build at a time.

## Stop conditions

- Green path: a new head where the gate is green, with the new gate job filed. Then the claim and assignment are marked DONE, naming the new head.
- Blocker path: record the reason in this assignment and in gates/SOLR-8240.md, and mark the claim with the blocker.
- Do not write receipts/SOLR-8240.md for a head that has not gated green. Do not edit the PR body.

## On completion

Mark this assignment DONE and the vm2 claim DONE, naming the outcome.

## Investigation outcome (vm1, 2026-10-16): test defect, escalated to the main agent

Classification: the production code at head 97cd22159b is correct for the initParams case. The new test in TestInitParams is wrong in two independent ways. Per Scope item 3 the test is not changed under this assignment; the item is escalated to the main agent with this record, and the vm1 claim is marked BLOCKED with this reason. No branch commit, no push, no receipt.

Finding 1, the named failure is the test's assertion shape. testUpdateJsonDocsInitParamsRejectsFieldMappings wraps handler.handleRequest(...) in expectThrows(SolrException.class, ...). RequestHandlerBase.handleRequest never propagates a handler exception: it catches Exception and records it on the response (rsp.setException). The assertion therefore fails with the same text at the base (where the loader never throws) and at the head (where the loader throws and the handler puts the exception on the response). The other tests in the same class follow the handler contract and assert on the response; JsonLoaderTest's matching test passes because it calls JsonLoader.load directly, where the exception does propagate.

Finding 2, the ObjectTracker leak is a separate test defect, not a consequence of Finding 1. With the assertion corrected in a scratch probe (see below) the test passes and classMethod still fails with exactly 147 unreleased objects, whole-core objects (SolrCore, SolrIndexSearcher, NRTCachingDirectory, MockDirectoryWrapper, SolrMetricsContext). Cause: the test calls initCore twice mid-class; SolrTestCaseJ4.createCore replaces the static harness without closing the previous one, and the class teardown closes only the last harness, so the beforeClass core and the initParams-config core are never closed. No other test in the class calls initCore mid-class.

Why the head is nevertheless correct, verified by run and by code. Code path: RequestHandlers.initHandlersFromConfig applies the solrconfig's initParams (path /update/json/docs) to the implicit UpdateRequestHandler's PluginInfo; RequestHandlerBase.handleRequest calls SolrPluginUtils.setDefaults, which merges those handler defaults into the request params before handleRequestBody; JsonLoader.handleSplitMode at the head reads mapUniqueKeyOnly from the request params and throws. Probe: a scratch copy of the test asserting on the response exception instead of expectThrows passed at the head with production untouched (SolrException, code 400 BAD_REQUEST, message contains "mapUniqueKeyOnly"; the explicit mapUniqueKeyOnly=false override with echo returned the mapped document). The probe edit was reverted after the run and never committed. The probe passing also settles the two open checks: solrconfig-update-json-docs-initparams.xml is loaded by the test (the true value reached the loader only from that config's initParams defaults), and the JsonLoader change covers the initParams path.

Runs (vm1, seed 8240C0FFEE824001, blocking flock; log /home/hatch/workspace/tools/g8240-investigate.log, JUnit XML archived at /home/hatch/workspace/tools/g8240-runs/):

- Head 97cd22159b, unmodified: TestInitParams tests=10 failures=3 (the named test with the recorded text, plus classMethod, ObjectTracker 147). Matches vm2's record exactly. In this run JsonLoaderTest was skipped wholesale in the shared JVM ("Leaked background threads present (zombies)"), a side effect of the leaked cores; vm2's run has JsonLoaderTest 33 of 33.
- Prior gated head fad7a1dd8e: TestInitParams 7 of 7, JsonLoaderTest 32 of 32, no leak. The class was green before the new test commit.
- Scratch probe at the head (Finding 1's corrected assertion only): the named test passes; classMethod still fails with the same 147 objects.

What a fix needs (for the main agent's decision, not done here): rewrite the test to assert on the response exception (or dispatch through a path that surfaces it), and close the swapped-out harnesses (or avoid mid-class initCore) so the class teardown accounts for every core the test creates. Either change edits a test the PR body describes, so it waits for the main agent.

## Main-agent decision (2026-10-16T02:19Z): proceed with the test fix

The escalation is settled on the main side under the owner's decision-routing rule (2026-10-14): the test fix proceeds. Production at 97cd22159b is correct and is not changed.

Fix both defects in TestInitParams, test code only:

1. Rewrite testUpdateJsonDocsInitParamsRejectsFieldMappings to assert on the response exception (the SolrException recorded on the response, code 400, message naming mapUniqueKeyOnly), following the class's own pattern in its other tests. Keep the explicit mapUniqueKeyOnly=false override case and its expectation.
2. Fix the harness leak: account for every harness the class creates (close the swapped-out harnesses, or restructure so no mid-class initCore swap leaks), so classMethod reports no unreleased objects. Both solrconfig variants stay exercised.

Then file a new gate job for the resulting head (gates/ directory, naming the branch, the new head, the focused classes TestInitParams and JsonLoaderTest, and seed 8240C0FFEE824001), run the full hardening gate at that head, and push the fix commit to solr-8240-submit only on green (plain fast-forward). On green: write receipts/SOLR-8240.md at the new head, mark this assignment DONE, and mark both claims (claims/pool-vm1-solr-8240-head-failure-round-1.md and claims/pool-vm2-gate-backlog-8240.md) DONE naming the new head. Flag in the lane's report any PR body sentence the test fix falsifies; body edits stay with the main side.

## Completion (vm1, 2026-10-16): test fix gated green and pushed

DONE. The test fix authorized above is committed as 78ecddf01f5c76736d415b9d031095f22aa38b17 on solr-8240-submit (test code only, 1 file, TestInitParams.java; production byte-identical to 97cd22159b) and pushed to the fork as a plain fast-forward, verified by ls-remote. The named test now asserts on the response exception (SolrException, code 400, message naming mapUniqueKeyOnly; the explicit mapUniqueKeyOnly=false override case and its expectation are unchanged), and the test closes each swapped-out harness with deleteCore() so both solrconfig variants stay exercised and no harness leaks. Gate: gates/SOLR-8240-test-fix-gate.md, run on vm1 (log g8240-fix-gate.log, seed 8240C0FFEE824001): changelog parse rc=0, tidy clean with the tree unchanged, Error Prone compile rc=0, focused run green from fresh JUnit XML (TestInitParams tests=8 failures=0 errors=0 with no ObjectTracker leak; JsonLoaderTest 33 of 33), module check rc=0. Receipt: receipts/SOLR-8240.md at the new head. Both claims (claims/pool-vm1-solr-8240-head-failure-round-1.md and claims/pool-vm2-gate-backlog-8240.md) are marked DONE naming this head.
