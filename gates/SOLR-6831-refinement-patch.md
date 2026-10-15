# Gate job: SOLR-6831 pivot refinement stops on a partial shard response

- Branch: solr-6831-submit. Head this applies to: d804a2ce092 (two commits on 24bd15ebb84: ae76f048005 tests only, d804a2ce092 fix in FacetComponent).
- Filed by: the Windows review lane, 2026-10-14, at Nick's direction after review of live PR #5134. Status: RUN 2026-10-15 by vm1; NOT GREEN, gate stopped at the spotless step; a decision is owed (see the run record at the bottom of this file). Do not relaunch until the recorded failure is investigated.
- Intended host: vm1 (capability tag: gate). Windows does not build.
- Merge base with upstream main (git merge-base, read 2026-10-14): 97d973814336101e12475558d7419321c743de79.
- Files: solr/core/src/test/org/apache/solr/search/TestQueryLimits.java (ae76f048005); solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java (d804a2ce092). Neither was compiled, formatted, or run on the Windows host.

## Question this run settles first

Does a limit that stays tripped make the coordinator re-request pivot refinements? Reading `PivotFacetField.processDefiniteCandidateElement` and `FacetComponent.refinePivotFacets` says it may. This is not confirmed.

Run 1 is the pre-fix head, ae76f048005. Expected: `testPivotFacetRefinementStopsWhenPartial` fails, and the other seven pass. The failure text (the request count, or the missing partial marker) goes in this file.

If run 1 is green, the refinement loop is not reproduced. Do not treat d804a2ce092 as needed. Record the result and stop for a decision.

## Gate steps

- [x] claim recorded under claims/ (WORKFLOW.md): claims/pool-vm1-gate-6831-refinement-patch.md, vm1, 2026-10-15T03:38Z.
- [x] run 1: `org.apache.solr.search.TestQueryLimits` at ae76f048005 (pre-fix). Record each test's result and the seed. Done 2026-10-15: 7 tests, 1 failure (NOT the expected one; see the run record).
- [x] run 2: `org.apache.solr.search.TestQueryLimits` at d804a2ce092. Done 2026-10-15: 7 tests, 1 failure, the same test as run 1 (see the run record).
- [x] spotlessApply on :solr:core. It changed one file (TestQueryLimits.java); the list is in the run record and the gate stopped there, per this step's own rule. Nothing was committed or pushed.
- [ ] Error Prone compile: :solr:core compileJava and compileTestJava. NOT RUN: the gate stopped at the spotless step above.
- [ ] module check: :solr:core:check -x test. NOT RUN: same stop.
- [ ] fail-before per the test queue (-WithFailBefore): record the verdict. NOT RUN: same stop. The run 1 result above is the pre-fix evidence this gate produced.

## On red

- Record the failing assertion, the seed, and the log path in this file.
- Do not relaunch. A gate that executed tests and failed is investigated before any relaunch (WORKFLOW.md).

## After the run

- If run 1 is red and run 2 is green: record both head SHAs in receipts/SOLR-6831.md (new section). The PR #5134 body still cites 24bd15ebb84 and "Verified 2026-10-14"; that edit is a separate step, not part of this gate.
- If run 1 is green: record that R1 was not reproduced and stop. Do not push further.

## Run record, 2026-10-15, vm1 (claim claims/pool-vm1-gate-6831-refinement-patch.md)

Runner: ~/workspace/tools/g6831-refine-gate.sh (blocking flock) on vm1. Gate log: ~/workspace/tools/g6831-refine-gate.log; step logs g6831-refine-run1.log, g6831-refine-run2.log, g6831-refine-spotless.log; fresh JUnit XML copies g6831-refine-run1-TEST.xml and g6831-refine-run2-TEST.xml in ~/workspace/tools. Seed for both runs: 6831C0FFEE6831. Step 0 changelog YAML parse: rc 0. All counts below are from the fresh XML, per test.

- Run 1 at ae76f048005 (pre-fix): 7 tests, 1 failure, 0 errors. The failure is testPivotFacetFailsWhenPartialResultsDisallowed: "junit.framework.AssertionFailedError: Expected exception SolrException but no exception was thrown" (TestQueryLimits.java:230). The other six pass, including testPivotFacetRefinementStopsWhenPartial, the test this job expected to fail. The refinement re-request loop this job posed is therefore NOT reproduced by that test at this seed: its tripped-request count stayed within the asserted bound of 3 without the fix.
- Run 2 at d804a2ce092 (fix head): 7 tests, 1 failure, 0 errors. The failure is the same test with the same message at the same line. The other six pass. The two heads produce identical results for this class at this seed: no test in the class fails before the fix and passes after it, and one new test fails at both heads. The failing test queries with distrib=false, so it exercises the single-node pivot path; the fix sits in FacetComponent.refinePivotFacets, the distributed refinement path. Whether the test needs a distributed shape or its expectation is wrong for the local path is a decision item, not settled here.
- Spotless: `:solr:core:spotlessApply` rc 0, and it changed exactly one file: solr/core/src/test/org/apache/solr/search/TestQueryLimits.java (the params(...) call inside testPivotFacetFailsWhenPartialResultsDisallowed reformatted to one argument per line; 12 insertions, 6 deletions). Per this job's rule the gate stopped before committing; nothing was committed or pushed, and the branch still sits at d804a2ce092. The formatting delta was left in the gate worktree only and then restored; re-running spotlessApply reproduces it. Error Prone compile, the module check and the fail-before leg did not run.
- Verdict: NOT GREEN. This gate executed tests and a test failed at both heads, so under WORKFLOW.md it is not relaunched until the failure is investigated. Owed on the main side: (1) a decision on testPivotFacetFailsWhenPartialResultsDisallowed (rework it to the distributed shape the fix covers, or correct its expectation for the local path); (2) a decision on the spotless formatting change to the test file; (3) only after those, a fresh gate covering the steps that did not run. PR #5134's body was not edited by this gate.
- Reproduce: in a worktree at d804a2ce092 (the failure is identical at ae76f048005), run `~/workspace/tools/solr-gradle.sh :solr:core:test --tests "org.apache.solr.search.TestQueryLimits" -Ptests.seed=6831C0FFEE6831` on vm1; testPivotFacetFailsWhenPartialResultsDisallowed fails with the message above.
