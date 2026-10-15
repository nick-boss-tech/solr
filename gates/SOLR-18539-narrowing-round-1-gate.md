# Gate job: SOLR-18539 narrowing patch, review round 1 (Option B)

- Branch: `review-r1-solr-18539` on the fork. Base head: `fbf7610644d178049e1110ffc0ec34dad8c5b32f`. Patch: `gates/SOLR-18539-narrowing-round-1.patch`, against fbf7610, touching only `QueryComponent.java` and `QueryComponentPartialResultsTest.java`. The narrowed head does not exist yet; record its SHA when the patch is committed.
- PR branch `mergeids-skipped-shard-totals` at `4eb996121804b043f1ee53f5bb7e22b9c3021b0a` (PR #5115) is not touched by this job.
Status: DONE
- Capability tags: gate.
- Intended host: vm2 (Linux), under the WORKFLOW.md claim rules. Windows never claims build work.
- Source: `reports/four-new-prs-strong-review-round-1.md`, "Narrowing task for the review patch loop (SOLR-18539)" under the SOLR-18539 premise addendum. Claim: `claims/pool-18539-narrowing-review-patch-round-1.md`.
- Job type: focused gate with three fail-before legs.

## Changes to check

1. `QueryComponent.mergeIds` (Option B, the canonical rule in the report). Relative to fbf7610, `anyShardSkipped` becomes `skippedShardMayHoldMatches`. The skip branch sets it when the skipped shard reported hits (`docs.getNumFound() > 0`) or an inexact count (`numFoundExact` not TRUE). The post-loop guard clears the merged `numFoundExact` only when that flag is set, whether or not another shard merged. A skipped shard that reported an exact zero leaves the merged flag alone.
2. `QueryComponentPartialResultsTest`, now 19 tests (18 at fbf7610 plus one):
   - `skippedShardWithNoHitsMakesMergedCountInexact` is renamed `skippedShardWithExactZeroLeavesMergedCountExact`; numFoundExact expected TRUE (was FALSE).
   - `allShardsSkippedWithNoHitsDoesNotClaimExactZero` is renamed `allShardsSkippedWithNoHitsKeepsExactZero`; numFoundExact expected TRUE (was FALSE).
   - New `allShardsSkippedWithInexactZeroDoesNotClaimExactZero`: both shards skipped, shard1 an exact zero, shard2 an inexact zero; merged numFound 0, numFoundExact FALSE.
   - Unchanged: `mergedNumFoundCountsOnlyMergedShards`, `skippedShardWithHitsMakesMergedCountInexact` and `allShardsSkippedDoesNotClaimExactZero` (all FALSE; their skipped shards report hits), `approximateTotalHitsFallsBackToEachShardsNumFound` (the 8-vs-3 pin), `mergedShardInexactCountMakesMergedFlagInexact`, and every other assertion.

## Expected results

Focused run (narrowed head, `QueryComponentPartialResultsTest`): 19 tests, 0 failures, 0 errors.

Fail-before leg 1, merge-base `d76fc14b9857e77b830d7a82514b28c523a239c2` (`QueryComponent.java` reverted, narrowed test file kept): expected 7 failures and no others.
- `mergedNumFoundCountsOnlyMergedShards`: `expected:<3> but was:<5>`
- `skippedShardWithHitsMakesMergedCountInexact`: `expected:<3> but was:<5>`
- `allShardsSkippedDoesNotClaimExactZero`: `expected:<0> but was:<5>`
- `mergedMaxScoreComesFromMergedShards`: `expected:<3.0> but was:<99.0>`
- `mergedApproximateTotalHitsExcludesSkippedShard`: `expected:<4> but was:<11>`
- `approximateTotalHitsFallsBackToEachShardsNumFound`: `expected:<10> but was:<2>`
- `approximateTotalHitsMixesHeaderAndFallbackShards`: `expected:<14> but was:<13>`

Passing at this leg: `skippedShardWithExactZeroLeavesMergedCountExact`, `allShardsSkippedWithNoHitsKeepsExactZero` and `allShardsSkippedWithInexactZeroDoesNotClaimExactZero`. The merge-base folds every shard's exact flag, so the new pin is not a merge-base fail-before.

Fail-before leg 2, PR head `4eb996121804b043f1ee53f5bb7e22b9c3021b0a` (`QueryComponent.java` reverted to the PR head, narrowed test file kept): expected 3 failures and no others.
- `mergedNumFoundCountsOnlyMergedShards`: `expected:<false> but was:<true>`
- `skippedShardWithHitsMakesMergedCountInexact`: `expected:<false> but was:<true>`
- `allShardsSkippedWithInexactZeroDoesNotClaimExactZero`: `expected:<false> but was:<true>`

The PR-head rule clears the flag only when no shard merged and a skipped shard reported hits. It misses the first two (a merged shard is present) and the inexact zero (no hits).

Fail-before leg 3, the pre-narrowing review head `fbf7610644d178049e1110ffc0ec34dad8c5b32f` (`QueryComponent.java` reverted to fbf7610; this is the narrowing check): expected 2 failures and no others.
- `skippedShardWithExactZeroLeavesMergedCountExact`: `expected:<true> but was:<false>`
- `allShardsSkippedWithNoHitsKeepsExactZero`: `expected:<true> but was:<false>`

These two are the tests the narrowing changes. `allShardsSkippedWithInexactZeroDoesNotClaimExactZero` passes at fbf7610 as well, as a pin should.

The fail-before legs cover the merge-base and the PR head, and the pre-narrowing head so the narrowing itself is tested. The expected message texts are the JUnit forms for these assertions. They were checked against the earlier gate counts for the fbf7610 tests (9 of 18 at the merge-base, the four named tests at the PR head). The vm2 run records the actual text.

## Steps

1. Confirm the base head: `git ls-remote origin refs/heads/review-r1-solr-18539` equals `fbf7610644d178049e1110ffc0ec34dad8c5b32f`.
2. Apply `gates/SOLR-18539-narrowing-round-1.patch` to a clean checkout of that head: `git apply --check` first, then `git apply`. Diff scope: `QueryComponent.java` and `QueryComponentPartialResultsTest.java` only.
3. Tidy first. Record any formatting change. A formatting-only diff inside the two files is not a reason to call the gate invalid.
4. Error Prone compile.
5. Focused run: `QueryComponentPartialResultsTest`, fresh JUnit XML. Expect 19 tests, 0 failures, 0 errors. Record the seed.
6. Fail-before leg 1: revert `QueryComponent.java` to `d76fc14b9857e77b830d7a82514b28c523a239c2`, keep the narrowed test file. Expect exactly the seven failures above. Record every failure text.
7. Fail-before leg 2: revert `QueryComponent.java` to `4eb996121804b043f1ee53f5bb7e22b9c3021b0a`, keep the narrowed test file. Expect exactly the three failures above.
8. Fail-before leg 3: revert `QueryComponent.java` to `fbf7610644d178049e1110ffc0ec34dad8c5b32f`, keep the narrowed test file. Expect exactly the two failures above.
9. Module check: `:solr:core:check -x test`.
10. On green: commit the patch on `review-r1-solr-18539` as a plain fast-forward and push it to the fork (no force). Record the old and new SHA and add a receipt section to `receipts/mergeids-fix.md`. Do not push to `mergeids-skipped-shard-totals` and do not edit PR #5115. Moving the PR branch is a separate decision for the owner or the main side. Mark this job DONE.

## Checklist

- [ ] head confirmed
- [ ] patch applied, diff scope (two files)
- [ ] tidy
- [ ] Error Prone compile
- [ ] focused run, 19 tests
- [ ] fail-before leg 1, merge-base (7 expected)
- [ ] fail-before leg 2, PR head (3 expected)
- [ ] fail-before leg 3, fbf7610 (2 expected)
- [ ] module check
- [ ] fork commit and receipt section; job DONE

## Notes

- The all-skipped inexact-zero pin fails only at the PR head, not at the merge-base. Its value is the inexact branch of the rule.
- The skip-branch test is `!Boolean.TRUE.equals(...)`, so a null `numFoundExact` on a skipped shard counts as inexact, while the merge fold (`Boolean.FALSE.equals`) treats null as exact. QueryComponent shard responses always carry a non-null flag, so this does not arise on this path and is not tested.
- The patch was written on Windows from the fbf7610 file contents with `git diff --no-index`, headers rewritten to repository paths. `git apply --check --cached` passes against a temporary index read from fbf7610.
- Out of scope here: the CombinedQueryComponent side (SOLR-18541) and any move of the PR branch.
