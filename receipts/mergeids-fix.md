## Narrowing patch, review round 1 (vm2, 2026-10-14)

- Patch: gates/SOLR-18539-narrowing-round-1.patch, applied to fbf7610644d178049e1110ffc0ec34dad8c5b32f. Pushed as a fast-forward to review-r1-solr-18539: old fbf7610644d1, new f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff. Not pushed to mergeids-skipped-shard-totals; PR #5115 untouched.
- Run: GATE START 2026-10-14T00:14:38Z, GATE END 2026-10-14T00:42:37Z. Log logs/vm2/queue-g18539-narrow-gate-20261014T001438Z.out. Seed 18539C0FFEE18539. Tidy changed nothing; Error Prone compile rc=0.
- Focused, patched head (QueryComponentPartialResultsTest, fresh JUnit XML, 00:31:54Z): 19 tests, 0 failures, 0 errors, 0 skipped.
- Fail-before leg 1, merge-base d76fc14b9857 (7 expected): 7 failures, by name: approximateTotalHitsFallsBackToEachShardsNumFound, allShardsSkippedDoesNotClaimExactZero, skippedShardWithHitsMakesMergedCountInexact, mergedNumFoundCountsOnlyMergedShards, mergedApproximateTotalHitsExcludesSkippedShard, mergedMaxScoreComesFromMergedShards, approximateTotalHitsMixesHeaderAndFallbackShards. Each text matches the job file.
- Fail-before leg 2, PR head 4eb9961218 (3 expected): 3 failures, mergedNumFoundCountsOnlyMergedShards, skippedShardWithHitsMakesMergedCountInexact, allShardsSkippedWithInexactZeroDoesNotClaimExactZero. Each text matches.
- Fail-before leg 3, fbf7610 (2 expected): 2 failures, skippedShardWithExactZeroLeavesMergedCountExact and allShardsSkippedWithNoHitsKeepsExactZero. Each text matches.
- Module check (:solr:core:check -x test): rc=0. Restore of QueryComponent.java verified clean.

# MergeIds fix gate receipt

Branch `mergeids-skipped-shard-totals` on the fork. Ticket: SOLR-18539, filed by Nick and verified against live Jira on 2026-10-12 (the branch is not named for the ticket, at Nick's filing setup; the changelog fragment carrying the number was added at packaging, see the packaging gate section at the end of this receipt). This receipt exists so the gate state is visible to every host; the gate logs themselves live on vm1.

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
- Re-gate at 73d20a0f93: GATE GREEN on vm1, GATE RUNNER DONE 2026-10-11T20:36:48Z (runner ~/workspace/tools/gmergeids-regate.sh, log ~/workspace/tools/gmergeids-regate.log; finished by the unified queue check on 2026-10-11 15:13 MDT).

## Re-gate at head 73d20a0f93: GREEN

- Head: `73d20a0f939f0ddbaea89a9bd590ce9527f7dd29`. Merge-base: `d76fc14b9857e77b830d7a82514b28c523a239c2`. Seed: `F2C2B3B7EFFDB021`. Fork tip verified by ls-remote at this head in the same pass.
- Step 0: changelog YAML parse rc=0 (no fragment on this branch; owed at packaging).
- Step 1: tidy rc=0, tree clean after tidy.
- Step 2: Error Prone compile rc=0.
- Step 3: pre-fix proof, QueryComponent.java reverted to the merge-base with the branch tests kept: QueryComponentPartialResultsTest 13 tests, 0 skipped, 7 failures, 0 errors (JUnit XML copy gmergeids-regate-proof-TEST-org.apache.solr.handler.component.QueryComponentPartialResultsTest.xml, timestamp 2026-10-11T20:33:53Z). The failures are the predicted contents: numFound expected 3 but was 5 (twice, including the exact-flag case), maxScore expected 3.0 but was 99.0, approximateTotalHits expected 4 but was 11, fallback expected 5 but was 2, mixed headers expected 14 but was 13, all-shards-skipped expected 0 but was 5. Head tree restored and hash verified. The runner script printed a warning that its premise-content check did not fully match; the queue check verified all seven failures by content from the XML itself, and they match the predicted set.
- Step 4: focused tests at head: QueryComponentPartialResultsTest 13 tests, 0 skipped, 0 failures, 0 errors, rc=0 (JUnit XML copy gmergeids-regate-head-TEST-org.apache.solr.handler.component.QueryComponentPartialResultsTest.xml, timestamp 2026-10-11T20:34:34Z).
- Step 5: `:solr:core:check -x test` rc=0. Final git status clean in the gate worktree.
- Note: the round 2 confirmation review (reports/mergeids-fix-review-round2.md) returned STILL OPEN with six should-fix items, mostly in the response record and the ticket draft, plus two test gaps in the branch. Those items are for the main side; they do not change this gate result, and the branch head may move again if the main side applies them.

## Re-gate at head 9e85ce5c16: GREEN

- Head: `9e85ce5c162086ae806f617419c4405512792dde`. Merge-base: `d76fc14b9857e77b830d7a82514b28c523a239c2`. Seed: `F2C2B3B7EFFDB021`.
- The change since 73d20a0f93 is the test file only: the four pin tests from the round 2 adjudication (reports/mergeids-fix-review-round2-response.md), 17 tests in the class. Production code is identical to the GREEN 73d20a0f93 head.
- Status: GATE GREEN on vm1, GATE RUNNER DONE 2026-10-11T21:46:01Z (runner ~/workspace/tools/gmergeids-regate2.sh, log ~/workspace/tools/gmergeids-regate2.log).
- Step 0: changelog YAML parse rc=0 (no fragment on this branch; owed at packaging).
- Step 1: tidy rc=0.
- Step 2: Error Prone compile rc=0.
- Step 3: pre-fix proof, QueryComponent.java reverted to the merge-base with the branch tests kept: QueryComponentPartialResultsTest 17 tests, 0 skipped, 7 failures, 0 errors (JUnit XML copy gmergeids-regate2-proof-TEST-org.apache.solr.handler.component.QueryComponentPartialResultsTest.xml, timestamp 2026-10-11T21:42Z). The failures are the same seven premise contents as at 73d20a0f93: numFound expected 3 but was 5 (mergedNumFoundCountsOnlyMergedShards and skippedShardNumFoundExactDoesNotFoldIntoMergedFlag), maxScore expected 3.0 but was 99.0, approximateTotalHits expected 4 but was 11, fallback expected 5 but was 2, mixed headers expected 14 but was 13, all-shards-skipped expected 0 but was 5. The four new pin tests pass on base production, as pins.
- Step 4: focused tests at head: QueryComponentPartialResultsTest 17 tests, 0 skipped, 0 failures, 0 errors, rc=0 (JUnit XML copy gmergeids-regate2-head-TEST-org.apache.solr.handler.component.QueryComponentPartialResultsTest.xml, timestamp 2026-10-11T21:43Z).
- Step 5: `:solr:core:check -x test` rc=0. Final git status clean in the gate worktree.

## Not run

- No end-to-end MiniSolrCloud reproduction of a timed-out shard emitting the skip shape; the evidence is merge-level, as in the verification report. The review's call was that merge-level tests plus the SOLR-14758 scenario are enough for the arithmetic.

## Packaging gate at head 4eb9961218: GREEN

- Head: `4eb996121804b043f1ee53f5bb7e22b9c3021b0a`. Merge-base: `d76fc14b9857e77b830d7a82514b28c523a239c2`. Seed: `F2C2B3B7EFFDB021`.
- The only change since the GREEN 9e85ce5c162 head is the packaging commit `4eb9961218` adding the changelog fragment `changelog/unreleased/SOLR-18539-mergeids-skipped-shard-totals.yml` for the filed ticket SOLR-18539 (title states the fix plainly; type `fixed`, the project's vocabulary for a bug fix). No production or test code changed.
- Status: GATE GREEN on vm1, GATE RUNNER DONE 2026-10-13T02:51:05Z (runner ~/workspace/tools/gmergeids-package.sh, log ~/workspace/tools/gmergeids-package.log; step logs gmergeids-package-proof.log and gmergeids-package-head-tests.log; JUnit XML copies gmergeids-package-proof-TEST-org.apache.solr.handler.component.QueryComponentPartialResultsTest.xml and gmergeids-package-head-TEST-org.apache.solr.handler.component.QueryComponentPartialResultsTest.xml).
- Step 0: changelog YAML parse of the new fragment rc=0 (~/workspace/tools/check-changelog-yaml.sh: 1 file parses OK).
- Step 1: tidy rc=0, tree clean after tidy (no GATE INVALID).
- Step 2: Error Prone compile rc=0.
- Step 3: pre-fix proof, QueryComponent.java reverted to the merge-base with the branch tests kept: QueryComponentPartialResultsTest 17 tests, 0 skipped, 7 failures, 0 errors. The failures are the same seven premise contents as at 9e85ce5c162: numFound expected 3 but was 5 (twice), maxScore expected 3.0 but was 99.0, approximateTotalHits expected 4 but was 11, fallback expected 5 but was 2, mixed headers expected 14 but was 13, all-shards-skipped expected 0 but was 5 (verified by content from the parsed JUnit XML; the runner's literal grep chain printed its known warning because the XML escapes the angle brackets). Head tree restored and hash verified.
- Step 4: focused tests at head: QueryComponentPartialResultsTest 17 tests, 0 skipped, 0 failures, 0 errors, rc=0.
- Step 5: `:solr:core:check -x test` rc=0. Final git status clean.
- The branch was pushed plainly to the fork at this head (fast-forward 9e85ce5c162 to 4eb9961218, verified by ls-remote). The branch is packaged and ready for the PR opening slate; the PR draft pr-drafts/search-components/mergeids-skipped-shard-totals.md carries SOLR-18539 and the fragment link at this head.

## Draft PR opened

- PR: apache/solr #5115, https://github.com/apache/solr/pull/5115, opened as a draft on 2026-10-12 MDT at Nick's direction, from the packaged head with the text of pr-drafts/search-components/mergeids-skipped-shard-totals.md.
- Verified after opening by a fresh read of the PR: state open, draft true, head `4eb996121804b043f1ee53f5bb7e22b9c3021b0a`, title and body match the draft (normalized line endings and trailing whitespace).
- The PR stays a draft until the review flow clears it; the ready flip is a later step, not part of the opening.

## Review patch gate at head fbf7610644: GREEN

- Head: `fbf7610644d178049e1110ffc0ec34dad8c5b32f` (branch `review-r1-solr-18539`, two commits on the PR #5115 head `4eb996121804b043f1ee53f5bb7e22b9c3021b0a`). Merge-base: `d76fc14b9857e77b830d7a82514b28c523a239c2`. Fork branch verified by ls-remote at this head. Not the PR branch: no push, PR #5115 untouched.
- Scope: `QueryComponent.java` and `QueryComponentPartialResultsTest.java` only (delta-only rc=0).
- Tidy rc=0 with the tree clean after tidy; spotless rc=0; Error Prone compile rc=0.
- Focused run at head: `QueryComponentPartialResultsTest`, BUILD SUCCESSFUL, seed `B21017F9ACC7AF8E` (log `logs/vm2/18539-review-r1-head.log`). The class has 18 test methods (JUnit XML of the same class at 2026-10-13T18:01:02Z reports `tests="18"`); the head XML was overwritten by the later fail-before run, so the 0 failures count rests on the BUILD SUCCESSFUL line.
- Fail-before at the merge-base (production `QueryComponent.java` reverted, branch tests kept): rc=1, 18 tests, 9 failed, seed `5FDB7D60C3EFB7CB` (log `logs/vm2/18539-review-r1-failbefore-base.log`). Failing: `mergedNumFoundCountsOnlyMergedShards`, `skippedShardWithHitsMakesMergedCountInexact`, `skippedShardWithNoHitsMakesMergedCountInexact`, `allShardsSkippedDoesNotClaimExactZero`, `allShardsSkippedWithNoHitsDoesNotClaimExactZero`, `mergedMaxScoreComesFromMergedShards`, `mergedApproximateTotalHitsExcludesSkippedShard`, `approximateTotalHitsFallsBackToEachShardsNumFound`, `approximateTotalHitsMixesHeaderAndFallbackShards`.
- Fail-before at the PR head `4eb9961218` (production reverted to the parent, branch tests kept): rc=1, 18 tests, exactly four failed, seed `B69AF26191F29EA6`, fresh JUnit XML at 2026-10-13T18:01:02Z: `mergedNumFoundCountsOnlyMergedShards`, `skippedShardWithNoHitsMakesMergedCountInexact`, `skippedShardWithHitsMakesMergedCountInexact`, `allShardsSkippedWithNoHitsDoesNotClaimExactZero`, each `numFoundExact expected:<false> but was:<true>`. No other failure.
- Both fail-before exits are nonzero by design: the job expects those failure sets. The runner's rc rule does not special-case them, so the coordinator classified the legs by content (classification recorded in `gates/SOLR-18539-review-r1-patch.md`).
- Module check `:solr:core:check -x test` rc=0 (log `logs/vm2/18539-review-r1-check.log`).
- Gate ran 2026-10-13T17:38:49Z to 18:05:35Z on vm2 (runner `/workspace/gates/g18539-review-r1-gate.sh`, queue log `logs/vm2/queue-g18539-review-r1-gate-20261013T173849Z.out`).

## Real-exit settling run: the run supports Option B; the review patch narrows (2026-10-13)

- The settling run for the exactness rule width executed on vm1 under `gates/queryComponent-real-exit-shape-premise.md` (claim `claims/pool-vm1-querycomponent-real-exit-shape-premise.md`): unmodified main `d76fc14b9857e77b830d7a82514b28c523a239c2`, probe `QueryComponentRealExitShapePremiseTest`, seed `C0FFEE42C0FFEE42`, shard A 20,000 documents and shard B 1,500, timeAllowed ladder 4000 down to 1. Log `~/workspace/tools/gqc-real-exit-shape.log` (GATE RUNNER DONE 2026-10-13T18:51:49Z); JUnit XML timestamp 2026-10-13T18:51:02Z, 1 test, 0 failures, 0 errors.
- Observed, quoted from the probe lines in the XML: "QC-EXIT Q1 SHAPE NOT CAPTURED: no attempt produced the short-circuited shape"; "QC-EXIT Q2 SHAPE NOT CAPTURED: no attempt produced a partial response carrying a sort_values key"; the one partial shape captured carried the shard's full totals (numFound 20000, numFoundExact true, size 20000, no sort values) and, merged with the complete shard B on unmodified main, completed at numFound 21500, numFoundExact true, size 1500, so the exiting shard was skipped while its totals were folded in (the defect at the merge-base, not evidence about the rule width).
- Verdict: the two candidate rules differ only for a skipped shard reporting an exact zero, and that shape was not produced by any real exit in the ladder. Every skipped shape the run produced reported hits, where canonical Option B and the any-skip rule agree. The run supports canonical Option B.
- Consequence: the review patch at `fbf7610644d178049e1110ffc0ec34dad8c5b32f` does NOT move onto this branch. `mergeids-skipped-shard-totals` stays at `4eb996121804b043f1ee53f5bb7e22b9c3021b0a` (verified by ls-remote after the verdict). The patch narrows to Option B under the patch-loop task in the premise addendum to `reports/four-new-prs-strong-review-round-1.md` (the zero-hit assertions flip back, an all-skipped inexact-zero pin is added) and re-gates before any move is proposed again.
