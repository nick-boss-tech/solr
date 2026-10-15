# Gate job: SOLR-6831 pivot refinement stops on a partial shard response

- Branch: solr-6831-submit. Head this applies to: d804a2ce092 (two commits on 24bd15ebb84: ae76f048005 tests only, d804a2ce092 fix in FacetComponent).
- Filed by: the Windows review lane, 2026-10-14, at Nick's direction after review of live PR #5134. Status: OPEN. Not run.
- Intended host: vm1 (capability tag: gate). Windows does not build.
- Merge base with upstream main (git merge-base, read 2026-10-14): 97d973814336101e12475558d7419321c743de79.
- Files: solr/core/src/test/org/apache/solr/search/TestQueryLimits.java (ae76f048005); solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java (d804a2ce092). Neither was compiled, formatted, or run on the Windows host.

## Question this run settles first

Does a limit that stays tripped make the coordinator re-request pivot refinements? Reading `PivotFacetField.processDefiniteCandidateElement` and `FacetComponent.refinePivotFacets` says it may. This is not confirmed.

Run 1 is the pre-fix head, ae76f048005. Expected: `testPivotFacetRefinementStopsWhenPartial` fails, and the other seven pass. The failure text (the request count, or the missing partial marker) goes in this file.

If run 1 is green, the refinement loop is not reproduced. Do not treat d804a2ce092 as needed. Record the result and stop for a decision.

## Gate steps

- [ ] claim recorded under claims/ (WORKFLOW.md)
- [ ] run 1: `org.apache.solr.search.TestQueryLimits` at ae76f048005 (pre-fix). Record each test's result and the seed.
- [ ] run 2: `org.apache.solr.search.TestQueryLimits` at d804a2ce092. Expected: 7 of 7 pass (the four existing tests plus testPivotFacetStopsBelowTopLevel, testPivotFacetFailsWhenPartialResultsDisallowed, testPivotFacetRefinementStopsWhenPartial). Record the seed.
- [ ] spotlessApply on :solr:core. If it changes files, record the list in this file and stop before committing. Do not push a formatting change without a decision.
- [ ] Error Prone compile: :solr:core compileJava and compileTestJava
- [ ] module check: :solr:core:check -x test
- [ ] fail-before per the test queue (-WithFailBefore): record the verdict. The PR's existing fail-before proof is still testPivotFacetTruncatesWhenLimitsTrip.

## On red

- Record the failing assertion, the seed, and the log path in this file.
- Do not relaunch. A gate that executed tests and failed is investigated before any relaunch (WORKFLOW.md).

## After the run

- If run 1 is red and run 2 is green: record both head SHAs in receipts/SOLR-6831.md (new section). The PR #5134 body still cites 24bd15ebb84 and "Verified 2026-10-14"; that edit is a separate step, not part of this gate.
- If run 1 is green: record that R1 was not reproduced and stop. Do not push further.
