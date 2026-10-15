# Assignment: SOLR-6831 refinement fix, round 1

Status: DONE

Capability tags: [implementation, gate]. Intended host: either Linux host (vm1 or vm2); Windows never claims build work. Posted by the main side, 2026-10-15, under the owner's decision routing: the items the refinement gate left as decisions owed are settled by the pool under the decision ladder, on run evidence, not returned to the owner.

Claim first: before starting, file a claim under `claims/` naming this assignment (for example `claims/pool-solr-6831-refinement-fix-round-1.md`), per WORKFLOW.md. Heartbeat and staleness follow the pool rules.

## Grounding

Read the run record before claiming: `gates/SOLR-6831-refinement-patch.md` carries the full NOT GREEN record, and the top section of `receipts/SOLR-6831.md` states the same outcome. The facts below are from that record; the branch head was re-verified by ls-remote at posting time. Re-verify it at claim time.

- Branch solr-6831-submit, live tip d804a2ce09258fb4cad2b6a5425ac023b23b07b6. Two commits sit on the older gated head 24bd15ebb84e: ae76f048005 (tests only, in TestQueryLimits) and d804a2ce092 (the fix, in FacetComponent.refinePivotFacets: if any shard response header carries partialResults, mark the response partial and return instead of queuing further pivot refinement requests).
- The branch backs open draft PR #5134. Its body still cites the older gated head 24bd15ebb84e and that head's counts. The GATE GREEN record at 24bd15ebb84e covers only that head; the live tip is not gated green.
- The refinement gate (vm1, 2026-10-15, seed 6831C0FFEE6831, counts from fresh JUnit XML) ran TestQueryLimits at both the pre-fix head ae76f048005 and the fix head d804a2ce092: 7 tests at each head, identical results.
  - testPivotFacetFailsWhenPartialResultsDisallowed fails at BOTH heads: "Expected exception SolrException but no exception was thrown" (TestQueryLimits.java:230). The test queries with distrib=false, so it exercises the single-node pivot path; the fix sits in the distributed refinement path.
  - testPivotFacetRefinementStopsWhenPartial, the intended discriminator, passes without the fix at this seed: its tripped-request count stayed within the asserted bound of 3. The test pair therefore gives the fix no discriminating evidence.
  - Spotless reformats exactly one thing: the params(...) call inside testPivotFacetFailsWhenPartialResultsDisallowed, to one argument per line (12 insertions, 6 deletions). The gate stopped there per its own rule, before committing.
  - Because of that stop, the Error Prone compile, the module check, and the fail-before leg have NOT run at this head. They are owed, and step 3 below names them.
- Merge base with upstream main, read 2026-10-14: 97d973814336101e12475558d7419321c743de79. Re-verify at claim time.
- Reproduce the recorded failure: in a worktree at d804a2ce092 (identical at ae76f048005), run the focused class with `-Ptests.seed=6831C0FFEE6831` through the host's Gradle route; testPivotFacetFailsWhenPartialResultsDisallowed fails with the message above.

## Scope

1. Settle the test shape by run. Establish which shape makes the partial-results behavior discriminate:
   - Rework testPivotFacetFailsWhenPartialResultsDisallowed so it exercises the distributed shape the fix covers, and show the discrimination with runs at ae76f048005 and at the fix head: the reworked test fails at the pre-fix head by content and passes at the fix head.
   - If a distributed shape cannot be made to fail before the fix and pass after it, correct the test's expectation for the local path instead, and record why, with the same two runs as the evidence. The record states which route was taken and what the runs showed; an assertion changed without the runs does not settle this step.
   - The same standard applies to testPivotFacetRefinementStopsWhenPartial: strengthen it until it fails without the fix by content, or record that the refinement re-request loop cannot be reproduced in a test. In that case adjust the branch's claims to match: the PR draft text and receipts/SOLR-6831.md must not claim a proof the tests do not give.
2. Take the spotless formatting change to TestQueryLimits.java as part of the fix commit (the tidy-clean tree rule: the pushed commit is the tree the gate sees). No separate formatting commit is needed; it lands with the test rework from step 1.
3. Run the full hardening gate at the resulting head: changelog YAML parse with a real parser, tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward), Error Prone compile (`-Pvalidation.errorprone=true`), the fail-before proof judged by content at the pre-fix head ae76f048005, the focused TestQueryLimits run counted from fresh JUnit XML, and the module check (`:solr:core:check -x test`). The Error Prone compile, the module check, and the fail-before leg are the three steps the stopped gate never ran; all three are owed here, not optional.
   - Build rules: take the blocking flock on the test queue; never run two Gradle builds at once. Use `~/workspace/tools/solr-gradle.sh` on vm1, or the claiming host's own wrapper per its host file. Copy `gradle.properties` into every new worktree before building. Test counts stand only on the gate log and fresh JUnit XML.
4. On green: push the branch (it backs the owner's own draft PR; pushes to his own PR branches carry standing authorization), refresh receipts/SOLR-6831.md for the new head, re-point PR #5134's body to the new head with the corrected counts and honest proof wording (read the body through the API before and after the edit), and mark gates/SOLR-6831-refinement-patch.md superseded by this assignment's outcome.

## Stop conditions

- Green path: the gate in step 3 passes at the resulting head and every item in step 4 is done. The deliverables are the pushed branch at its new head, the refreshed receipt, the re-pointed PR body, the superseded mark in the gate file, and the claim marked DONE naming them.
- Failure path: if the gate, or any run under step 1 after the test rework, executes tests and fails, record the failing test, the seed, and the reproduce line in gates/SOLR-6831-refinement-patch.md or a run record it points to, mark the claim with the blocker, and stop. Do not relaunch, and do not push a head whose gate is not green.
- Honest-record path: if step 1 ends with the loop recorded as not reproducible in a test and the claims adjusted to match, that is a complete outcome for step 1, provided the rest of the gate runs green at the resulting head and the records say plainly what the tests do and do not prove.

## On completion

1. Mark the claim for this assignment DONE, naming the outcome: the new head and gate result on the green path, or the recorded failure on the failure path.
2. Append one line to the takeover log (`files/remaining-branches-takeover-log.md` in the goal records) recording the outcome in one sentence.
