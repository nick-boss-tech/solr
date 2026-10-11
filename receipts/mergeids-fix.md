# MergeIds fix gate receipt

Branch `mergeids-skipped-shard-totals` on the fork (no ticket number yet; Nick files the ticket himself, so the branch is not named for one and carries no changelog fragment, which is owed at packaging). This receipt exists so the gate state is visible to every host; the gate logs themselves live on vm1.

## Gate at head d211794596: GREEN

- Status: GATE GREEN on vm1, GATE RUNNER DONE 2026-10-11T19:22:52Z.
- Head: `d211794596bc3fc05757bc9548d999e0bd4322ea`. Merge-base: `d76fc14b9857e77b830d7a82514b28c523a239c2`. Seed: `F2C2B3B7EFFDB021`.
- Commits: `0d900a3b679` (skip decision before the totals accumulation in QueryComponent.mergeIds) and `d211794596b` (approximateTotalHits fallback adds the current shard's own numFound).
- Gate log on vm1: `~/workspace/tools/gmergeids-gate.log`; step logs `gmergeids-gate-proof.log` and `gmergeids-gate-head-tests.log`; JUnit XML copies `gmergeids-gate-proof-TEST-org.apache.solr.handler.component.QueryComponentPartialResultsTest.xml` (timestamp 2026-10-11T19:19:57Z) and `gmergeids-gate-head-TEST-org.apache.solr.handler.component.QueryComponentPartialResultsTest.xml` (timestamp 2026-10-11T19:20:28Z).
- Step 0: changelog YAML parse, no fragment on this branch (owed at packaging), rc=0.
- Step 1: tidy rc=0, tree clean after tidy.
- Step 2: Error Prone compile (`:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`) rc=0.
- Step 3: pre-fix proof, QueryComponent.java reverted to the merge-base with the branch tests kept: QueryComponentPartialResultsTest 9 tests, 0 skipped, 4 failures, 0 errors. The failures are the premise contents: mergedNumFoundCountsOnlyMergedShards `expected:<3> but was:<5>`, mergedMaxScoreComesFromMergedShards `expected:<3.0> but was:<99.0>`, mergedApproximateTotalHitsExcludesSkippedShard `expected:<3> but was:<10>`, approximateTotalHitsFallsBackToEachShardsNumFound `expected:<5> but was:<2>`. Head tree restored and hash verified.
- Step 4: focused tests at head: QueryComponentPartialResultsTest 9 tests, 0 skipped, 0 failures, 0 errors, rc=0.
- Step 5: `:solr:core:check -x test` rc=0. Final git status clean.

## Premise runs (vm1, before the gate)

- Scratch reproduction on unmodified current main: the numFound case failed `expected:<3> but was:<5>` (5 tests, 1 failure; log gmergeids-premise1.log). The Oct 6 numbers reproduce on current main.
- After the first fix: 8 tests, 0 failures (log gmergeids-fix1.log).
- The fallback case, added next and run against the not-yet-fixed fallback line: 9 tests, 1 failure, `expected:<5> but was:<2>` (log gmergeids-fallback-premise.log). This confirms the fallback defect by run: two normal shards, numFound 2 and 3, no approximateTotalHits headers, merged approximateTotalHits 2 instead of 5.
- After the second fix: 9 tests, 0 failures (log gmergeids-fix2.log).

## Adjudication commit and re-gate

- The thorough review (reports/mergeids-fix-review.md) returned STILL OPEN with 1 blocking and 10 should-fix items. The adjudication (reports/mergeids-fix-review-response.md) applied the code and test items in commit `73d20a0f939f0ddbaea89a9bd590ce9527f7dd29`, pushed as the new branch head: the all-shards-skipped exact-zero guard, the discriminating approximateTotalHits header value (4 instead of 3), and four added tests (13 in the class now).
- Local verification of that commit before the push (logs gmergeids-adj-head-tests.log and gmergeids-adj-proof.log, XML copies gmergeids-adj-head-TEST-*.xml and gmergeids-adj-proof-TEST-*.xml): head 13 tests, 0 failures; proof leg on merge-base production 13 tests, 7 failures, each with the predicted content (numFound 5 vs 3 twice, maxScore 99.0 vs 3.0, approximateTotalHits 11 vs 4, fallback 2 vs 5, mixed headers 13 vs 14, all-skipped 5 vs 0).
- Re-gate at 73d20a0f93: GATE PENDING in the main side's takeover log; runner ~/workspace/tools/gmergeids-regate.sh, log ~/workspace/tools/gmergeids-regate.log on vm1. This receipt gets refreshed when that gate finishes.

## Not run

- No end-to-end MiniSolrCloud reproduction of a timed-out shard emitting the skip shape; the evidence is merge-level, as in the verification report. The review's call was that merge-level tests plus the SOLR-14758 scenario are enough for the arithmetic.
