# Gate job: SOLR-18541 (numFoundExact false on multi-sub-query merges)

Status: DONE

- Branch: `combinedquerycomponent-merge-totals`. Head: `81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d` (fork tip, confirmed by ls-remote on 2026-10-16). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `a93f9f6a997e748514cf2575a6e94c5f1c01f85e` (GREEN, receipts/SOLR-18541.md). Merge-base with upstream/main: `597ed67740831566b708b793f52554b2e49e7e17`.
- Intended host: vm1 (claimed by the implementing lane, claims/pool-vm1-solr-18541-numexact-round-1.md).
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest`. Seed: `18541C0FFEE18541`.
- Diff scope against the prior gated head: one commit, `81a88e40dd`. `CombinedQueryComponent.java` (+14: count the sub-queries that contribute to the merge and clear the merged exactness flag when more than one contributes), `CombinedQueryComponentMergeTotalsTest.java` (the two `largestSubQueryTotalWins*` tests dropped, `multipleContributingSubQueriesClearExactness` added, class javadoc updated), the changelog fragment (title extended with the one-sub-query condition). Anything else in the tree is out of scope.
- Why: a maintainer directed this minimum change on the ticket on 2026-10-16 (assignments/pool-vm1-solr-18541-numexact-round-1.md). The prior head reported numFoundExact true next to the largest-sub-query count on multi-sub-query merges; the new head is ungated.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-18541-combined-query-merge-totals.yml`.
- Step 1: tidy. If tidy changes any file, the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg, two runs, both judged from fresh JUnit XML:
  - Leg A: merge-base production (`597ed6774083`, production tree verified untouched) with the head's test file copied in. Expected, by content: the branch's premise failures (the fallback and skip totals, and the exactness flag inverted in both directions in the single-sub-query exactness tests), with `skippedPartialShardWithoutCountClearsExactness`, `skippedPartialShardWithoutHitsClearsExactness` and the new `multipleContributingSubQueriesClearExactness` passing there, for the reasons recorded in the fail-before analysis claim (claims/pool-18541-fail-before-analysis-round-1.md): the base fold is inverted, so a test asserting false on an all-exact merge passes on base by coincidence of the inversion. Any other failure shape stops the job.
  - Leg B: the prior head's production (`a93f9f6a997e`, the new test's real parent) with the head's test file copied in. Expected: exactly one failure, `multipleContributingSubQueriesClearExactness`, with numFoundExact expected false and found true. This is the discriminating fail-before for this commit.
- Step 4, focused head run: cleanTest first, then the focused class at the seed. Expected 9 tests, 0 failures. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route decision.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Gate failure after tests executed | decided: record and stop | Record the failure here and in the receipt with the failing step and the XML counts; do not relaunch | main agent |
| Tip moved at claim time | decided: stop | Record the new tip here and hand back | main agent |

## On green

1. Write `receipts/SOLR-18541.md` with a new top section: head, gate date and host, log path, step results, both proof legs' results (test names and failure text), and the focused counts from the fresh XML.
2. No public text edits in this job. The PR body sentences this change falsifies are flagged to the main side by the implementing lane.
3. Mark this job DONE and the claim DONE in the same push as the receipt.

## Outcome

GATE GREEN at head `81a88e40dd42f90fe6c67b2aeb6f678c437d7c0d` on vm1, 2026-10-16 (log `~/workspace/tools/g18541-numexact-gate.log`, GATE RUNNER DONE 06:39Z; XML archived at `~/workspace/tools/g18541-numexact-xml/`).

- Step 0: changelog YAML parse rc=0.
- Step 1: tidy rc=0, tree unchanged after tidy (no GATE INVALID).
- Step 2: Error Prone compile rc=0.
- Step 3A (merge-base production, head test file): 9 tests, 6 failures, all premise contents (approximateTotalHits 5 vs 15 and 5 vs 7; numFound 3 vs 7 and 0 vs 2; numFoundExact inverted in both directions). The new test and the two no-count/no-hits skip tests pass on base by the inverted fold, as this job's step 3 expected.
- Step 3B (prior head production, head test file): 9 tests, exactly 1 failure, `multipleContributingSubQueriesClearExactness`, numFoundExact expected:<false> but was:<true>.
- Step 4: focused head run 9 tests, 0 failures, 0 errors, 0 skipped, from fresh JUnit XML.
- Step 5: module check rc=0. Post-gate tree clean.

Receipt: `receipts/SOLR-18541.md`, new top section.
