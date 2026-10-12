# Review: mergeIds fix, round 6 confirmation

Verdict: **SATISFIED.** Both round 5 corrections are confirmed against the branch code, the branch tests and the commit graph. On SATISFIED, the ticket text in `material/mergeids-ticket-draft.md` is final for Nick's consolidated ticket email. The PR draft at `pr-drafts/search-components/mergeids-skipped-shard-totals.md` is final, pending Nick's filing of the ticket and his approval of the opening. Nothing was filed, opened or posted by this review.

Branch: `mergeids-skipped-shard-totals` at head `9e85ce5c162086ae806f617419c4405512792dde`, which has not moved. Production and test reads were taken from that commit with `git show` and `git grep`. The record files were read from the worktree, which has no uncommitted changes to them.

## One line per correction

1. **The sort-values pin claim, in the response records: CONFIRMED.**
   - The skip gate at `QueryComponent.java` 1180 to 1185 (`thisResponseIsPartial && sortFieldValues.size() == 0 && needsUnmarshalling`) fires only when sort values are empty, so sort values gate the skip.
   - `git grep` over the component's test directory for `sort_values`, `sortvalues`, `unmarshalSortValues` and `getSchemaFields` finds only comments at `QueryComponentPartialResultsTest.java` lines 201 and 459. No test sends a `sort_values` subsection.
   - `MockShardRequest` adds only `response` and `responseHeader`. `MockSortSpecBuilder` stubs `getCount`, `getSort` (inside `withSortFields`) and `includesNonScoreOrDocField`, and never `getSchemaFields`.
   - The pinning tests use partial shards with no `sort_values`: `skippedPartialShardDocsAreAbsent` (line 82) and `excludesPartialShardResultWhenUsingNonScoreOrDocSortField` (line 71). The other skip-pinning tests are also absent-only. So "the existing tests pin the skip when sort values are absent; no test sends sort values" is accurate.
   - The supporting clauses hold. The totals block (1186 to 1202) reads `docs.getNumFound`, the header and `getNumFoundExact`, with no sort values. `numFound--` at 1233 runs only when the push returns false. The push returns false only at 936, for a duplicate id. The non-duplicate path does `insertWithOverflow` at 944 and returns true at 945, so overflow does not touch `numFound`.
   - `reports/mergeids-fix-review-round4-response.md` line 7 restates the same sentence in substance, consistent with the other three records.

2. **The premise record label, `reports/combinedquerycomponent-premise.md` line 132: CONFIRMED.** Line 132 now reads "Diffs for this file between `cabedd1d968`, `d76fc14b98` and the fixed branch (`73d20a0f939`): empty." No "merge-base" wording remains for these commits. It does not contradict the line 12 header, which calls `cabedd1d968` an older main snapshot and an ancestor of the branch base. `git merge-base cabedd1d968 d76fc14b98` returns `cabedd1d968`, and `git merge-base 9e85ce5c16 d76fc14b98` returns `d76fc14b98`. The `CombinedQueryComponent.java` diff is empty for `cabedd1d968` against `d76fc14b98`, and for `73d20a0f939` against `9e85ce5c16`, so the "empty" statement holds at the current head as well.

## Notes (not findings)

- Line 132 names `73d20a0f939`, the earlier fixed head. The head has moved to `9e85ce5c16` with a test-only change, and the statement still holds.
- Not checked, outside the two corrections: line 85 of the premise report, and the line numbers 1379 and 1398 cited in the round 3 response for `unmarshalSortValues`.

## Scope

Read-only. No branch, draft, receipt, record, claim or PR text was edited, and nothing was posted, built, tested or gated.
