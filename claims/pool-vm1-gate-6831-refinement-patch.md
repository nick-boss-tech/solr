# Claim: SOLR-6831 refinement patch gate (vm1)

- Claimed: 2026-10-15T03:38:00Z by vm1. Job: gates/SOLR-6831-refinement-patch.md.
- Slice: the whole job. Run org.apache.solr.search.TestQueryLimits at the pre-fix head ae76f048005 (run 1, premise), at the fix head d804a2ce092 (run 2), spotlessApply on :solr:core, Error Prone compile, module check, and the fail-before leg at the merge base 97d973814336.
- Capability tags used: gate.
- Branch state at claim: solr-6831-submit live tip d804a2ce09258fb4cad2b6a5425ac023b23b07b6 (ls-remote), exactly the head the job names; the job is OPEN and not run; no other claim covers it.
- Heartbeat: appended below as the gate progresses; staleness rule 2 hours.
- Status: DONE 2026-10-15. Outcome: the gate ran and is NOT GREEN (see the run record in gates/SOLR-6831-refinement-patch.md and the new top section of receipts/SOLR-6831.md). Run 1 at ae76f048005 and run 2 at d804a2ce092 each ran TestQueryLimits 7 tests with the same single failure, testPivotFacetFailsWhenPartialResultsDisallowed (seed 6831C0FFEE6831); the refinement premise test passed at both heads. Spotless changed TestQueryLimits.java, so the gate stopped before the Error Prone compile, the module check and the fail-before leg, per the job. Nothing was committed or pushed; the branch still sits at d804a2ce092. Decisions owed on the main side are listed in the job file; a failed gate that executed tests is not relaunched until investigated.

## Heartbeats

- 2026-10-15T03:38:00Z heartbeat: claim taken by vm1; gate worktree and runner being prepared.
- 2026-10-15T03:44:00Z heartbeat: runner finished (flock held 03:40Z to 03:43Z); results verified from the fresh JUnit XML copies; records written. Claim closed DONE with the not-green outcome above.
