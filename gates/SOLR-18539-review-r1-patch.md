# Gate job: SOLR-18539 review patch, round 1 (strong review of the four new draft PRs)

- Branch: `review-r1-solr-18539` on the fork. Head: `fbf7610644d178049e1110ffc0ec34dad8c5b32f`. Parent: `4eb996121804b043f1ee53f5bb7e22b9c3021b0a` (the head of draft PR #5115), two commits. Not the PR branch: this patch is gated first and moved onto the PR branch only after it is green and the owner or the main side agrees.
Status: DONE (GATE GREEN at fbf7610644d1)
- Capability tags: gate.
- Intended host: Linux only (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Source: `reports/four-new-prs-strong-review-round-1.md`, section SOLR-18539.
- Job type: full focused gate with two fail-before runs.

## Changes to check

1. `QueryComponent.mergeIds`: the merged numFoundExact is cleared whenever any shard was skipped (`anyShardSkipped`), whether or not the skipped shard reported hits and whether or not another shard was merged. The `anyShardMerged` flag and the `docs.getNumFound() > 0` condition are removed. Reason: `SearchHandler.shortCircuitedResults` sends an empty `SolrDocumentList` (numFound 0, exact by default) for a shard that stopped before collecting anything, so a rule that reads reported hits cannot see that shape.
2. `QueryComponentPartialResultsTest`, now 18 tests. Assertion changes from the PR head, all in the direction numFoundExact false next to a skipped shard:
   - `mergedNumFoundCountsOnlyMergedShards`: numFoundExact now false (was true).
   - `skippedShardNumFoundExactDoesNotFoldIntoMergedFlag` is replaced by `skippedShardWithNoHitsMakesMergedCountInexact`: skipped shard with zero hits and an exact flag, merged shard exact: numFound 3, numFoundExact false.
   - new `skippedShardWithHitsMakesMergedCountInexact`: skipped shard with 2 hits, merged shard exact: numFound 3, numFoundExact false.
   - `allShardsSkippedWithNoHitsKeepsExactZero` is replaced by `allShardsSkippedWithNoHitsDoesNotClaimExactZero`: numFoundExact false (was true).
   - `approximateTotalHitsFallsBackToEachShardsNumFound`: the second shard now has numFound 8 with 3 returned documents, so a fallback that added the returned document count fails.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/review-r1-solr-18539` equals `fbf7610644d178049e1110ffc0ec34dad8c5b32f`.
2. Diff scope against `4eb996121804b043f1ee53f5bb7e22b9c3021b0a`: `QueryComponent.java` and `QueryComponentPartialResultsTest.java` only.
3. Tidy first. The tests were written without a formatter run; if tidy changes the tree, record the change and commit it on the review branch (a formatting-only diff in the two files above is not a reason to call the gate invalid).
4. Error Prone compile.
5. Focused run: `QueryComponentPartialResultsTest`, fresh JUnit XML. Expect 18 tests, 0 failures. Record the seed.
6. Fail-before against the merge-base: revert `QueryComponent.java` to `d76fc14b9857e77b830d7a82514b28c523a239c2`, keep the branch tests. Record every failure text. Expected failures include numFound 3 vs 5 (`mergedNumFoundCountsOnlyMergedShards`, `skippedShardWithHitsMakesMergedCountInexact`), maxScore, approximateTotalHits 4 vs 11, the fallback test (10 expected, 2 observed), the mixed-header test, the all-skipped numFound test, and the exactness assertions in `skippedShardWithNoHitsMakesMergedCountInexact` and `allShardsSkippedWithNoHitsDoesNotClaimExactZero` (`expected:<false> but was:<true>`).
7. Fail-before against the PR head: revert `QueryComponent.java` to `4eb996121804b043f1ee53f5bb7e22b9c3021b0a`, keep the branch tests. Expect failures only in `mergedNumFoundCountsOnlyMergedShards`, `skippedShardWithNoHitsMakesMergedCountInexact`, `skippedShardWithHitsMakesMergedCountInexact` and `allShardsSkippedWithNoHitsDoesNotClaimExactZero`, each `numFoundExact expected:<false> but was:<true>`. Any other failure is a finding.
8. Module check `:solr:core:check -x test`.
9. On green: add a section to `receipts/mergeids-fix.md` for this review branch head. Do not push to `mergeids-skipped-shard-totals` and do not edit PR #5115. Mark this job DONE.

## Checklist

- [x] head confirmed
- [x] diff scope
- [x] tidy
- [x] Error Prone compile
- [x] focused run, 18 tests
- [x] fail-before against the merge-base
- [x] fail-before against the PR head, the four named tests only
- [x] module check
- [x] receipt section; job DONE
