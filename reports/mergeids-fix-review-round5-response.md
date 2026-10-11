# Response: mergeIds fix review, round 5 settlement

Round 5 (reports/mergeids-fix-review-round5.md) returned STILL OPEN on record wording only: one overstated pin claim in the sort-values scope note (item 1), and one label in the premise record that contradicts its own header (item 5c). The branch needs no change and has not moved: `mergeids-skipped-shard-totals` stays at head `9e85ce5c162086ae806f617419c4405512792dde`, gates GREEN. The ticket draft (item 2) and the PR draft (item 4) pass, and the annotations (items 3, 5a and 5b) are correct; no action on those items. Both findings were verified against the branch test file before settling, as below; the reviewer is right on each, and each is applied as below.

## Item 1: the sort-values pin claim, corrected in every record it stood in

Verified on the branch tree at `9e85ce5c162`. The skip gate in `QueryComponent.mergeIds` is `if (thisResponseIsPartial && sortFieldValues.size() == 0 && needsUnmarshalling)` at QueryComponent.java 1180 to 1185, so the skip fires only on the branch where sort values are absent. In `QueryComponentPartialResultsTest.java` (solr/core/src/test/org/apache/solr/handler/component/), `git grep -n sort_values` over the component test directory finds only two comments, at test lines 201 and 459; no test sends a `sort_values` subsection. The two tests the report names pin only the absent branch: `skippedPartialShardDocsAreAbsent` goes through `mergeWithSkippedPartialShard`, whose partial shard is described at test line 459 as answering with partial results and no sort_values, and `excludesPartialShardResultWhenUsingNonScoreOrDocSortField` goes through the partial-results request built with no sort_values subsection. The present-sort-values branch, where a partial shard carrying sort values is not skipped, is unpinned. The corrected claim therefore states exactly which branch the tests pin: the skip when sort values are absent.

The pin clause in the scope sentence now reads, in each record: "Sort values gate the skip at QueryComponent.java 1180; the existing tests pin the skip when sort values are absent; no test sends sort values; the declined case, sort_values on a merged partial shard, is not skipped, and its totals do not depend on sort values." This is the replacement the report gives, applied verbatim in substance. Locations:

- reports/mergeids-fix-review-response.md, the scope note in the maximum-aggregation paragraph (line 25).
- reports/mergeids-fix-review-round2-response.md, item 2, the closing scope sentence (line 11).
- reports/mergeids-fix-review-round3-response.md, the scope note paragraph (line 9).
- reports/mergeids-fix-review-round4-response.md is the reviewer's own report and is not edited. reports/mergeids-fix-review-round4-response.md, line 7, summarised the same scope sentence "in substance" with the overstated clause; that summary is corrected to the same wording above so it does not keep the overstated claim alive, as a direct consequence of the three corrections.

Nothing else in those passages changes: the totals block still does not read sort values, the declined case still stands for that reason, and the push and overflow sentences are untouched.

## Item 5c: the premise record label, fixed to match its header

Verified against the commit graph. reports/combinedquerycomponent-premise.md line 12 is correct as corrected in round 4: main is `d76fc14b98`, and `cabedd1d968` is an older main snapshot and an ancestor of the branch base, not the merge-base (`git merge-base cabedd1d968 d76fc14b98` returns `cabedd1d968`; the count from `cabedd1d968` to `d76fc14b98` is 43 commits, the reverse 0; the branch merge-base with `d76fc14b98` is `d76fc14b98`). Line 132 still read "Diffs for this file between the merge-base, main and the fixed branch: empty", which, read against line 12, makes "the merge-base" the same commit as "main" and contradicts the header. Line 132 now names the commits, as the report suggested: "Diffs for this file between cabedd1d968, d76fc14b98 and the fixed branch (73d20a0f939): empty." No value in that report changes; the component file is identical across those commits, as the report already states. Line 85 ("Pre-fix, on the merge-base") is left as written, as the report notes it is consistent with `d76fc14b98`.

## What round 6 confirms

assignments/pool-mergeids-fix-review-round6.md is posted, scoped to these corrections only. On SATISFIED, the ticket text in material/mergeids-ticket-draft.md is final for Nick's consolidated ticket email, and the PR draft at pr-drafts/search-components/mergeids-skipped-shard-totals.md is final pending his filing of the ticket and his approval of the opening.
