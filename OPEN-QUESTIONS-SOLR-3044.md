# SOLR-3044: open questions (branch note, remove before the PR)

Full review: `research/branch-reviews/round-5/SOLR-3044-review.md` in the local workspace (not on this remote).

Status: not ready. The three source hunks (`QueryComponent`, `CombinedQueryComponent`, `PivotListEntry`) were already
done upstream by SOLR-18373 (`55e3b8838b4`). The new `CombinedQueryComponentPartialResultsTest` does not compile (it
calls a three-argument `MockShardRequest.withShardResponse` that does not exist). Fixing this needs a rebase onto
`upstream/main`, which agents may not do.

Questions for the owner:

1. OK to rebase this branch (taking upstream for the three source files), or start a fresh branch for the test-only
   remainder?
2. Replace the broken test with a `partialResults` header assertion in `QueryComponentPartialResultsTest` (reuse its
   mocks, no reflection)?
3. Keep `TestPivotHelperCode#testPivotListEntryOptionalLookupSkipsEarlierMatches` as coverage (passes without changes)?
4. Retarget the PR to a concrete ticket instead of the SOLR-3044 umbrella?
