# Follow-up plan for PR #5117: SOLR-18539

Research date: 2026-10-24. This is a scope map only; no implementation, test run, Jira update, or public reply was made.

## Recommendation

Treat the follow-up PR explicitly offered in [PR #5117](https://github.com/apache/solr/pull/5117) as the fix for the `QueryComponent.mergeIds` partial-shard totals defect already tracked by [SOLR-18539](https://issues.apache.org/jira/browse/SOLR-18539). SOLR-18539 is Open and its description matches the offer in the PR body. Use that issue rather than filing a duplicate.

The coordination-ui search found the ticket in the inventory but no separate implementation assignment or claim for SOLR-18539. This claim records scope research only; it does not start implementation.

Keep this PR narrow: fix how `QueryComponent.mergeIds` accounts for a field-sorted partial shard that the existing merge path skips, and fix the same method's per-shard `approximateTotalHits` fallback. Do not combine it with the separate exact-union work in SOLR-18550 or the shorter per-query-list failure in `CombinedQueryComponent`.

## What #5117 says, and what has happened

- #5117 is still open at head `49bcb450a27c2880f23ebea986fdee7b188335f1`. Its rebase is complete: #5172 / SOLR-18550 merged on 2026-10-23 at `79b23df58daac14b92cc35f23b8b81f72098b4ba`, and that merge is the base of #5117.
- The current PR body says: “I can open a follow-up PR for it on request.” The referent is the `QueryComponent.mergeIds` partial-shard leak in the body's Limits section. This is a conditional offer for a focused PR; it does not promise to solve all neighboring count or timeout behavior in one change.
- The commenter’s “Now that PR #5117 is merged” premise was already corrected by Nick in the 2026-10-23 reply: [the PR was rebased and updated](https://github.com/apache/solr/pull/5117#issuecomment-6367130985). No additional reply draft is needed.
- Nick's earlier “union count as a follow-up Jira” was followed by SOLR-18550 and PR #5172. #5172 landed interim semantics; SOLR-18550 remains Open for the exact distinct count across all matches.
- The October 23–24 code review also logged a separate LOW wording issue in #5117's body: its explanation of the old score-only partial-shard skip is inaccurate. That is a text correction, not part of SOLR-18539, and no body edit was made here.

## Code and ticket map

| Area | Evidence | Boundary for the proposed PR |
| --- | --- | --- |
| `QueryComponent.mergeIds` partial-shard totals | On the #5172 merge base, `QueryComponent.mergeIds` starts at line 971. It adds the no-header fallback from the current `numFound` accumulator at line 1162, folds `maxScore` and `numFound` at lines 1167–1171 and exactness at 1173–1174; the partial field-sort skip is later, at line 1193. SOLR-18539 records a reproduction with 3 merged docs but `numFound=5`, `numFoundExact=true`, `maxScore=99` from the skipped shard, and `approximateTotalHits=11` instead of 4. Its second reproduction reports 2 instead of the expected 5 for the missing-header fallback across shards with counts 2 and 3. [Source at the #5172 merge commit](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1160-L1193). | Move/restructure the existing skip so a shard excluded from document merging is excluded from `numFound`, `maxScore`, `approximateTotalHits`, and exactness accounting. Make the no-header fallback use that shard's own count, not the running aggregate. Mark the merged count inexact when an omitted shard may contain hits. Keep the existing partial-result policy otherwise. |
| Combined-query version already addressed by #5117 | The rebased PR changes `CombinedQueryComponent.mergeIds`; its receipt records a GREEN Linux gate at the exact head. The regression pattern is in `CombinedQueryComponentMergeTotalsTest`. [PR-head code around the remaining short-list access](https://github.com/nick-boss-tech/solr/blob/49bcb450a27c2880f23ebea986fdee7b188335f1/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L341). | Use #5117 as a behavioral reference, not as a base branch. The #5117 diff does not modify `QueryComponent.java`, so a SOLR-18539 PR can be based independently on current `main`. |
| Short per-query list after a real `timeAllowed` exit | In `CombinedQueryComponent`, the unchecked `docList.get(queryIndex)` at the linked line can throw when a later sub-query has no list entry. The SOLR-18541 receipt's “Real-exit premise job” records Q2 reproducing `IndexOutOfBoundsException` after a timeout above query index 0 on the unmodified base; it explicitly says the fixed #5117 branch was not tested with that shape. A targeted Jira search did not surface a separate issue for this case. | This is a distinct failure from the `QueryComponent` totals leak. Keep it out of SOLR-18539; it needs its own scope and a proof against the current #5117 head before choosing how missing later-query results should behave. Do not file a new issue as part of this assignment. |
| Full exact union count | [SOLR-18550](https://issues.apache.org/jira/browse/SOLR-18550) remains Open. Its latest substantive comment says the true distinct count should be computed per shard and summed at the coordinator, with the same handling in the single-node path; the per-subquery `DocSet` cost needs review. In current main, `QueryAndResponseCombiner.simpleCombine` tracks the maximum sub-query `matches()` at line 85, unions `DocSet`s when present at lines 89–93, and builds a lower-bound count at lines 124–126. [Source](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/java/org/apache/solr/handler/component/combine/QueryAndResponseCombiner.java#L81-L126). | Separate from SOLR-18539. #5172 reports the distinct fused count when candidate lists are complete; for truncated lists it reports a lower bound and marks it inexact. Do not widen the QueryComponent partial-shard patch into a full-union redesign. |

Two nearby QueryComponent tickets also constrain the implementation. [SOLR-15595](https://issues.apache.org/jira/browse/SOLR-15595) deliberately stopped discarding partial results for non-sort queries; the proposed change must not skip every partial shard, only one the existing field-sort merge path actually excludes. [SOLR-14758](https://issues.apache.org/jira/browse/SOLR-14758) fixed a different `timeAllowed`/sort-values null failure and has a regression check in `DistributedQueryComponentCustomSortTest` (line 240 onward); that test is not evidence for the multi-query short-list case. [SOLR-18496](https://issues.apache.org/jira/browse/SOLR-18496) is another Open issue touching both `QueryComponent.mergeIds` and `CombinedQueryComponent.mergeIds`, but for a renamed unique key under `distrib.singlePass`; check its live branch/PR state before implementation to avoid overlap, and keep its behavior out of this patch.

## Proposed PR shape

**Target:** one SOLR-18539 PR against current `main`, independent of #5117. The production change should stay in `QueryComponent.mergeIds` unless the code review shows a small shared helper is necessary; a larger merge refactor would make the fix harder to review.

**Behavior:** preserve the existing document-selection and sorting behavior. For a partial shard that the current field-sort path skips because it lacks usable sort values, decide the skip before folding its totals. That skipped shard must not raise `numFound`, `maxScore`, or `approximateTotalHits`; because results are omitted, merged exactness must be false. For an accepted shard with no `approximateTotalHits` header, add that shard's `docs.getNumFound()` as the fallback. Do not change the non-sort partial-result behavior from SOLR-15595, or add new behavior for malformed complete-shard responses.

**Focused tests:** add deterministic shard-response tests around `QueryComponent.mergeIds` (a dedicated `QueryComponentMergeTotalsTest` would mirror the focused #5117 test pattern, subject to the existing fixtures).

1. Reproduce the SOLR-18539 field-sort case with one complete response and one partial response that the merge skips. Assert the merged document count and `numFound` agree, the skipped shard does not supply `maxScore` or approximate hits, and `numFoundExact` is false.
2. Reproduce the missing-header fallback with two accepted shards of different counts. Assert the approximate total is the sum of each shard's own count, not a value derived from the running merge total.
3. Demonstrate both new assertions fail on the pre-fix base and pass at the PR head. Retain coverage that non-sort partial results are still merged, consistent with SOLR-15595.

Use the existing deterministic response-fixture approach first. A wall-clock timeout integration test is optional only if a reliable existing harness can create the exact shard shape; the historical SOLR-14758 test had a flaky reproduction history. On this Windows host, do not run Gradle. Before the PR is called done, obtain the phase-specific Linux gate for the exact pushed head, including changelog parse, tidy, Error Prone compilation, focused tests, and core check as the gate assignment requires.

## Commitments and remaining decisions

- Follow-up attached to #5117: the `QueryComponent.mergeIds` totals leak, most naturally implemented under existing SOLR-18539.
- Separate tracked follow-up: true exact union counting, still open under SOLR-18550 after #5172's interim semantics.
- Separate disclosed defect without a surfaced ticket: the #5117 short-list `IndexOutOfBoundsException`; first establish behavior on the fixed branch, then scope it independently.
- No new public reply, Jira filing/comment, PR creation, or source-code change was made as part of this plan. If implementation is requested later, it can proceed on SOLR-18539 without stacking on #5117, subject to checking current branches and SOLR-18496 for overlap.
