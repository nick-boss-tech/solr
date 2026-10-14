# SOLR-18539 narrowing and the SOLR-18541 fail-before analysis: round 1

Trigger: pr-prepare tip `db023359b1e`. Two new records: the SOLR-18539 settling verdict (Option B) with its narrowing task, and the SOLR-18541 fail-before run that failed its step 4. Round claims: `claims/pool-pr-prepare-tip-db02335-round-1.md`, `claims/pool-18539-narrowing-review-patch-round-1.md`, `claims/pool-18541-fail-before-analysis-round-1.md`.

Scope: patch and report files only. Nothing built, run or gated here. The owner allowed fork pushes; the one push this round needed was blocked by the push wrapper's branch rule (see section 1). No PR, comment or live body touched.

## 1. SOLR-18539: the review patch narrowed to Option B (pushed, gated GREEN at the narrowed head)

Update (2026-10-14, from `receipts/mergeids-fix.md`): the narrowed head `f9b0e5d722bed21ed99d453e6c7d1bf448bb07ff` is on `review-r1-solr-18539` (plain fast-forward from `fbf7610644d1`). vm2 gated it: focused `QueryComponentPartialResultsTest` 19 of 19, seed `18539C0FFEE18539`; fail-before legs 7 at the merge-base, 3 at the previous PR head, 2 at the pre-narrowing review head; module check rc=0. The PR #5115 branch `mergeids-skipped-shard-totals` was then moved to `f9b0e5d722` by plain push, verified by ls-remote; the PR stays a draft. The "unpushed" text below is the state before that update.

- Patch: `gates/SOLR-18539-narrowing-round-1.patch`, against `review-r1-solr-18539` at `fbf7610644d178049e1110ffc0ec34dad8c5b32f`. It touches only `QueryComponent.java` and `QueryComponentPartialResultsTest.java`. A dry-run apply passes against a temporary index read from `fbf7610`. Read by hand: 68 changed lines.
- Production: the unconditional `anyShardSkipped` guard becomes the Option B condition. A skipped shard that reported hits or an inexact count clears the merged `numFoundExact`. A skipped shard that reported an exact zero does not change it.
- Tests:
  - `skippedShardWithNoHitsMakesMergedCountInexact` is renamed `skippedShardWithExactZeroLeavesMergedCountExact`, and its expectation flips from FALSE to TRUE.
  - `allShardsSkippedWithNoHitsDoesNotClaimExactZero` is renamed `allShardsSkippedWithNoHitsKeepsExactZero`, and its expectation flips from FALSE to TRUE.
  - New `allShardsSkippedWithInexactZeroDoesNotClaimExactZero`: one shard an exact zero, one an inexact zero, expectation FALSE.
  - Kept at FALSE: `mergedNumFoundCountsOnlyMergedShards`, `skippedShardWithHitsMakesMergedCountInexact`, `allShardsSkippedDoesNotClaimExactZero`.
- Gate job: `gates/SOLR-18539-narrowing-round-1-gate.md`, OPEN for vm2, nothing ticked. Expected by reading, not run: the merge-base fail-before fails 7 tests; the PR-head fail-before fails 3; the pre-narrowing head fails the 2 flipped TRUE tests; the focused run expects 19 tests and 0 failures.

Push route: at the time of this round, the fork push wrapper (`research\push-fork-ff.ps1`) accepted only `review-<digits>-…` and `solr-<digits>-…` branch names, so `review-r1-solr-18539` did not match. The owner's 2026-10-13 direction is that pushing is not blocked, and the push above was made as a plain fast-forward. The wrapper's branch rule is unchanged and still rejects `review-r<digits>-…` names. Whether to widen it is an owner call.

## 2. SOLR-18541: the two new tests are not a valid fail-before as the gate ran them

Verdict: not valid at the merge-base, and valid against their real parents.

- At the merge-base `d76fc14b9857`, both new tests pass (numFound 3, numFoundExact false). The reason is not the side-branch rule. The inverted fold at merge-base `CombinedQueryComponent.java` line 343, `hitCountIsExact = hitCountIsExact && Boolean.FALSE.equals(docs.getNumFoundExact())`, turns the sibling shard's exact TRUE into FALSE.
- Against the real parents, both fail on `numFoundExact` at `d5ed34125db9` (test lines 330 and 361) and both pass at the head `a93f9f6a997e`. `skippedPartialShardWithoutCountClearsExactness` is valid against `fcd92f23ba65`. `skippedPartialShardWithoutHitsClearsExactness` is valid against `354edb85a2f`.

Decisions for the main side:
1. Re-run the fail-before at `d5ed34125db9`, or per commit, instead of at the merge-base.
2. Choose the expectation for `skippedPartialShardWithoutHitsClearsExactness`. The commit `354edb85a2f` asserted TRUE, on the reasoning that an exact zero count loses no hits. The commit `a93f9f6a997e` flipped it to FALSE with no fixture change.

Test change: none recommended. The only fixture alternative (per-query flags set to FALSE) passes at `d5ed` and stops pinning the rule. The trace is in the scratchpad diff preamble.

## 3. Owner items

1. The PR #5115 body rewrite against `f9b0e5d722` (section 1). Live body edits are allowed under the 2026-10-13 direction; the review coordinator's next slice carries it.
2. The wrapper's branch rule (section 1): widen it to `review-r<digits>-solr-…`, or keep plain pushes for those names. Owner call.
3. The fail-before re-run point and the WithoutHits expectation (section 2).
