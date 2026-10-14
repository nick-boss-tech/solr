# Gate job: SOLR-6831 changelog line names every query limit the change checks

- Branch: solr-6831-submit. Head this applies to: 96b33ba5f6f834038e570d0b93f8684dc887fbe8 (live PR #5134 head, read 2026-10-14).
- Filed by: the Windows review lane (review round 1, slice 2), 2026-10-14. Status: OPEN. Not run.
- Intended host: a Linux host with the Gradle toolchain (capability tag: gate), under the WORKFLOW.md claim rules. Windows does not build.
- Merge base with upstream main (git merge-base, read 2026-10-14): 97d973814336101e12475558d7419321c743de79.

## Finding

The changelog title says the change checks "(timeAllowed, cpuAllowed)". The code checks every limit that QueryLimits.shouldExit() knows, so memAllowed is checked too. The PR body already says so ("memAllowed is included"). The user-facing changelog line should say the same thing.

Evidence: solr/core/src/java/org/apache/solr/search/QueryLimits.java shouldExit() loops over all limits; MemAllowedLimit.java is one of them. The new check in PivotFacetProcessor.java (L328-L333) calls maybeExitWithPartialResults(), which calls shouldExit().

## Branch change

1. In changelog/unreleased/SOLR-6831-pivot-facet-limits.yml, in the folded title, change "(timeAllowed, cpuAllowed)" to "(timeAllowed, cpuAllowed, memAllowed)". No other line changes.
2. One new commit on solr-6831-submit with the ICLA author (branch-work.ps1 commit), message along the lines of "SOLR-6831: name memAllowed in the changelog line". No Co-Authored-By trailer. Do not rewrite the existing commits.

## Gate steps

- [x] claim recorded under claims/ (WORKFLOW.md): claims/pool-vm1-gate-6831-review-changelog-limits.md (vm1)
- [x] changelog YAML parse with a real YAML parser on the gate host: passes
- [x] tidy with a clean tree afterwards: rc=0, tree unchanged, versions file restored
- [x] Error Prone compile: :solr:core:compileJava and :solr:core:compileTestJava, rc=0
- [x] pre-fix proof legs, counted from fresh JUnit XML after cleanTest:
  - premise leg at 97d973814336 with the branch test file: TestQueryLimits runs 4, exactly 1 failure, testPivotFacetTruncatesWhenLimitsTrip (verified by content)
  - head leg at the new head: TestQueryLimits runs 4, 0 failures
- [x] focused test class at the new head: org.apache.solr.search.TestQueryLimits, 4 of 4 (seed 6831C0FFEE6831)
- [x] module check: :solr:core:check -x test, rc=0
- [x] push with the fork lease, pinned to the old tip (Linux equivalent run by vm1: git push --force-with-lease=solr-6831-submit:96b33ba5f6f8...): pushed 24bd15ebb84e6ae4ecd97dd62f07e86250d694ed, verified with ls-remote.

## On green

- Record the new head SHA here and in receipts/SOLR-6831.md (new top section).
- The head moves, so every blob link in the PR #5134 body that points at 96b33ba5f6f8 (including the changelog link and the TestQueryLimits link) must be re-pointed to the new head SHA, then read back. The PR body is not changed by the gate lane; the description edit is a separate step.
- Re-review PR #5134 at the new head. No ready flip; the PR stays a draft.

## Completion (vm1, 2026-10-14)

- Status: DONE. GATE GREEN at 24bd15ebb84e6ae4ecd97dd62f07e86250d694ed. Receipt: receipts/SOLR-6831.md (new top section). Claim: claims/pool-vm1-gate-6831-review-changelog-limits.md, DONE.
- Re-review at the new head done as a verification read (head, draft state, three-file diff, title). Still OWED as the separate step this file names: re-point the PR #5134 body's blob links from 96b33ba5f6f8 to 24bd15ebb84e6ae4ecd97dd62f07e86250d694ed (changelog link and TestQueryLimits link included) and read the body back.
