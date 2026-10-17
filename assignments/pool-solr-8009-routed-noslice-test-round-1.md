# Assignment: SOLR-8009 routed no-slice test, round 1

Status: DONE (honest-record outcome, 2026-10-16; see claims/pool-solr-8009-routed-noslice-test-round-1.md and the top section of receipts/SOLR-8009.md: the routed no-slice case is a 400 rejection at both heads and cannot be constructed as a discriminating test)

Capability tags: [implementation, gate]. Intended host: either Linux host (vm1 or vm2); Windows never claims build work. Posted by the main side, 2026-10-15, as the follow-on the moved-tip sweep recorded: the delta's own guard case has no test, and the gap is already named in PR #5141's Limits.

Claim first: before starting, file a claim under `claims/` naming this assignment, per WORKFLOW.md. Heartbeat and staleness follow the pool rules.

## Grounding

Read the record before claiming: the top section of `receipts/SOLR-8009.md` carries the moved-tip verification at the live head and the proof-leg analysis this assignment rests on. The facts below are from that record; the branch head was re-verified by ls-remote at posting time. Re-verify it at claim time.

- Branch solr-8009-submit, live tip `e600375aaee30d66694b69e1d05e276801327e73`. Pre-delta head: `8795661ddc98c8bffdb8db49a7dd5f293216e929` (gated green 2026-10-05; the sections below the receipt's top section cover it). Merge-base with upstream/main: `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`.
- The delta on 8795661ddc98 is two commits: `4c21a626e7` (the guard in `RealTimeGetComponent.java`: the all-shards fallback in `createSubRequests` now also requires that no `_route_` was given; plus test changes in `FullSolrCloudDistribCmdsTest.java`) and `e600375aae` (assert order in the test).
- The branch backs open draft PR #5141 at e600375aaee3. Its body states the no-`_route_` condition in What this change does and names the untested routed case in Limits.
- The sweep's verification at e600375aaee3 (seed `8009C0FFEE8009`, counts from fresh JUnit XML): changelog YAML parse rc=0; tidy rc=0 with the tree clean afterward; Error Prone compile rc=0; `FullSolrCloudDistribCmdsTest` 10 of 10, `TestRealTimeGet` 4 of 4, `TestRandomFlRTGCloud` 2 of 2, `TestAddFieldRealTimeGet` 1 of 1.
- The proof leg for the delta does NOT discriminate: with `RealTimeGetComponent.java` reverted to 8795661ddc98 and the branch tests kept, `FullSolrCloudDistribCmdsTest` passes 10 of 10 at the same seed. The mechanism is visible in the code, and the receipt states it: the fallback bucket is reached only when the router's `getTargetSlice` returns no slice, and that call (`RealTimeGetComponent.java` line 1066 at the live head) passes the request's `_route_`; a `_route_` naming a real slice resolves to that slice, so the routed get the current test adds goes to its own shard and misses at both heads.
- The guard therefore changes an outcome only in the case no test covers: a `_route_` is given AND the router still names no slice of the collection, for example a route naming no slice of the collection. In that case the pre-delta fallback would fan the get out to all shards and could serve a copy held locally on a shard the route does not name; with the guard, the fallback does not engage for a routed request.

## Scope

1. Write the missing test: a routed real-time get whose `_route_` names no slice of the collection. Put it in `FullSolrCloudDistribCmdsTest` or in the class the code reading supports; before composing any run command, grep the branch for the test file and copy its exact package and module. Set up the condition the assertion needs in the shape the existing `testRealTimeGetImplicitRouterWithoutRoute` uses for its fallback case: a document held on a shard, requested with a route that names no slice of the collection. The assertion follows the behavior the code reading establishes: with the guard, the fallback must not serve that copy for a routed request, so the get misses; do not assert an outcome the code reading does not support.
2. Prove the test discriminates by content against the pre-delta head: run it with `RealTimeGetComponent.java` reverted to its 8795661ddc98 content and the branch tests kept; it must fail by content (the fallback serves the copy the guard now withholds). At the resulting head it must pass. Both runs counted from fresh JUnit XML at seed `8009C0FFEE8009`. A test that passes at both heads does not discharge this step; adjust the test shape until it discriminates, or take the honest-record path below.
3. Gate the landing at the resulting head: tidy clean with the GATE INVALID guard (restore `gradle/libs.versions.toml` afterward; if tidy changes anything, that formatting rides in the test commit and the gate re-runs on the committed tree), Error Prone compile (`-Pvalidation.errorprone=true`), and the focused set from the receipt counted from fresh JUnit XML at seed `8009C0FFEE8009`: `FullSolrCloudDistribCmdsTest`, `TestRealTimeGet`, `TestRandomFlRTGCloud`, `TestAddFieldRealTimeGet` (read their fully qualified names from the branch by grep). New test code follows the base-class rule: a test added to an existing class keeps that class's base; any new class extends `SolrTestCase` or `SolrCloudTestCase`, never `SolrTestCaseJ4`.
   - Build rules: take the blocking flock on the test queue; never run two Gradle builds at once. Use `~/workspace/tools/solr-gradle.sh` on vm1, or the claiming host's own wrapper per its host file. Copy `gradle.properties` into every new worktree before building. Test counts stand only on the gate log and fresh JUnit XML.
4. On green: push the branch plainly (it backs the owner's own draft PR; pushes to his own PR branches carry standing authorization), refresh `receipts/SOLR-8009.md` with a top section for the new head (the new test, its discrimination evidence from step 2, the focused counts), and update PR #5141's body under the standing description authorization (read it through the API before and after the edit): the routed no-slice case named in Limits is now covered, so revise that Limits entry and the Proof to name the new test and what it proves.

## Coordination with the full gate

`gates/SOLR-8009.md` (vm2's full gate at e600375aaee3: merge-base proof leg, the four focused classes, module check) also stands open. The two jobs must not gate the same head concurrently. If both are live, this test lands first; the full gate then runs at the combined head, and its claimant records the new tip in that file. If the full gate's claim is already active at e600375aaee3 when this assignment is claimed, wait for that claim to close or go stale under WORKFLOW.md before pushing anything to the branch.

## Stop conditions

- Green path: the new test discriminates per step 2, the step 3 gate passes at the resulting head, and every item in step 4 is done. The deliverables are the pushed branch at its new head, the refreshed receipt, the updated PR body, and the claim marked DONE naming them.
- Failure path: if any run after the test lands executes tests and fails in a way the discrimination evidence does not explain, record the failing test, the seed, and the reproduce line in the claim and in `receipts/SOLR-8009.md`, mark the claim with the blocker, and stop. Do not relaunch, and do not push a head whose gate is not green.
- Honest-record path: if the routed no-slice case cannot be constructed in the test harness, record the attempts and why in the receipt, and leave PR #5141's Limits naming the case as untested. That is a complete outcome for the record; no text anywhere may claim coverage that does not exist.

## On completion

1. Mark the claim for this assignment DONE, naming the outcome: the new head and the discrimination result on the green path, or the recorded blocker on the other paths.
2. Append one line to the takeover log (`files/remaining-branches-takeover-log.md` in the goal records) recording the outcome in one sentence.
