# Gate job: SOLR-9124 test-fix gate at 3dd985b93a

Status: DONE. GATE GREEN at head 3dd985b93a on 2026-10-16 (vm1). See the Result section. Gate for the test fix in assignments/pool-vm1-solr-9124-test-fix-round-1.md (claim claims/pool-vm1-solr-9124-test-fix-round-1.md).

- Branch: solr-9124-submit. Head: `3dd985b93ac0e26a3850c338765ba1235bd811d4` (fork tip, confirmed by ls-remote after the push). If the tip has moved at run time, stop and record the new tip here.
- The fix commit is one fast-forward commit on the failed head 07b5f39e35, test code only: the within-group check in TestBaseStatsCache.checkWithinGroupOrder groups on grp_s1 instead of grp_s, and TestDefaultStatsCache indexes the same g0/g1 group values into grp_s1. Production files are byte-identical to 07b5f39e35 (the runner asserts an empty production diff against that head). Merge-base with upstream main: `cabedd1d968059215188f4e7563fb303241899ed`.
- Module: `:solr:core`. Focused classes: `org.apache.solr.search.stats.TestDefaultStatsCache`, `org.apache.solr.search.stats.TestExactStatsCache`, `org.apache.solr.search.stats.TestLRUStatsCache`, `org.apache.solr.search.stats.TestExactSharedStatsCache`. Seed: `9124C0FFEE9124`.
- Runner and logs on vm1: ~/workspace/tools/g9124-fix-gate.sh, ~/workspace/tools/g9124-fix-gate.log (head worktree ~/workspace/solr-worktrees/solr-9124-fix; proof worktree ~/workspace/solr-worktrees/solr-9124-fix-proof at the merge-base with the two fixed test files copied in; proof XML archived at ~/workspace/tools/g9124-fix-proof-xml/, head XML at ~/workspace/tools/g9124-fix-head-xml/).

## What to run

Use the host's standard gate runner (~/workspace/tools/solr-gradle.sh behind the blocking flock on ~/workspace/tools/.solr-test-queue.lock). Do not run the build tool directly.

- [x] Step 0: changelog YAML parse of `changelog/unreleased/SOLR-9124-grouped-exact-stats.yml`. rc 0.
- [x] Step 1: tidy (`:solr:core:tidy`); tree unchanged after tidy (no GATE INVALID). The fix tree was tidied before the commit. rc 0.
- [x] Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`. rc 0.
- [x] Step 3, proof leg: scratch worktree at the merge-base `cabedd1d9680` (base production, verified untouched under src/java) with the branch's two fixed test files copied in; the four focused classes ran at the seed; counts and failures read from fresh JUnit XML (archived at ~/workspace/tools/g9124-fix-proof-xml/). Result: 5 tests, exactly 1 failure: TestExactStatsCache at checkGroupedScores (TestBaseStatsCache.java:96) with grouped scores diverging from the control node, the established premise. The other three classes pass on base; no XML contains the grp_s FieldCache error, so the within-group check now executes on base with a premise-consistent outcome.
- [x] Step 4, focused head run: cleanTest first, then the four focused classes at the seed in the head worktree; counts from fresh JUnit XML (archived at ~/workspace/tools/g9124-fix-head-xml/). Result: 5 tests, 0 failures, 0 errors (TestDefaultStatsCache 1, TestExactStatsCache 2, TestLRUStatsCache 1, TestExactSharedStatsCache 1).
- [x] Step 5: module check `:solr:core:check -x test`. rc 0.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Group field | decided: grp_s1 via the *_s1 dynamic field (multiValued="false" in schema.xml, schema12.xml and schema15.xml, the variants where *_s is multivalued); evidence in the assignment | none | assignment, recorded before the push |
| Branch push | decided: the fix commit is pushed plainly (fast-forward); the gate runs at the pushed head | if the gate fails on the fix itself, record it and stop; do not stack another fix without a main-side call | main side brief, 2026-10-15 |
| Failure route | default: record the failing step, test, seed and reproduce line here and in the claim | a gate that executed tests and failed is not relaunched unchanged | WORKFLOW.md |

## Result

DONE, GATE GREEN at 3dd985b93ac0e26a3850c338765ba1235bd811d4 (vm1, 2026-10-16; log ~/workspace/tools/g9124-fix-gate.log, seed 9124C0FFEE9124, GATE RUNNER DONE after step 5). All steps green; the head run is 5 tests with 0 failures from fresh JUnit XML, and the proof leg shows exactly the one premise failure (TestExactStatsCache grouped-scores divergence) with no grp_s FieldCache error anywhere. The runner also asserted the production diff against 07b5f39e35 is empty: the fix is test code only. Receipt refreshed in receipts/SOLR-9124.md; the assignment and the claim are DONE in the same push sequence.

## On green

- Refresh receipts/SOLR-9124.md with a new top section at 3dd985b93a (earlier sections stay as history).
- Mark this job DONE, the assignment DONE and the claim DONE in the same push sequence.
- Append one line to the takeover log on the main host.
- Any public text describing the branch as verified at 07b5f39e35 was falsified by the failed gate; PR body and pr-drafts updates are the main side's, from this gate's receipt.
