# Assignment: SOLR-8240 head failure, round 1

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
