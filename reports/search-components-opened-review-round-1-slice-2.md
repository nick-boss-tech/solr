# Search components opened review, round 1, slice 2 (PRs 5134 to 5137)

- Assignment: `assignments/pool-search-components-opened-review-round-1.md`. Claim: `claims/search-components-opened-review-round-1.md`.
- Reviewer: review agent, Windows host. All four PRs stay draft and open; heads and titles unchanged. No builds, no tests, no pushes, no comments, no ready flips, no Jira reads.
- The formula used is `pr-formula.md` at the root of the branch. The assignment names `material/pr-formula.md` and `files/pr-formula.md`, which are not on the branch.

## PR #5134 (SOLR-6831), head `96b33ba5f683`. BLOCKED (code). Move: code lane.

- Diff: `PivotFacetProcessor` (+5), `TestQueryLimits` (+75) and the changelog, against merge-base `97d973814336`. The receipt matches: 4 of 4 at head, and the premise failure in `testPivotFacetTruncatesWhenLimitsTrip`.
- Edits (description, live):
  1. "Pivot faceting did not." became "Pivot faceting did not check limits inside its value loop." The old wording was loose, because `FacetComponent` checks limits after the pivot pass.
  2. The Proof gained its date: "Verified at this head." became "Verified 2026-10-06 at this head."
  3. The test anchor was corrected from `L135-L188` to `L135-L187` in two places.
- Code finding: the changelog title says "(timeAllowed, cpuAllowed)", but the change also checks `memAllowed`, and the body says so. Gate job filed: `gates/SOLR-6831-review-changelog-limits.md` (OPEN). The proposed patch changes the title to "(timeAllowed, cpuAllowed, memAllowed)".

## PR #5135 (SOLR-6975), head `761aa629bb83`. SATISFIED after 2 edits.

- Diff: `ShardFieldSortedHitQueue` (+4, the DOC tie at L123 to L126). The comparator file is unchanged upstream since the merge-base. The receipt matches: 10 of 10, with the premise failure from the ticket's stack.
- Edits:
  1. "10 of 10 passed at `761aa629bb83`." became "... verified 2026-10-05 at this head."
  2. The Choice option 2 cost said multi-shard users "get results today", which contradicted the "What happens" section. It now reads: "The cost: a request whose hits all come from one shard works today, and it would fail too."

## PR #5136 (SOLR-7390), head `7463dd006a78`. SATISFIED after 2 edits.

- The gate ran at `72caa363e29`. Between that commit and the head, the only difference is the deletion of the internal testing file. No source, test or changelog difference.
- Edits:
  1. "With this change, at head `7463dd006a7`, `ReturnFieldsTest` passes 15 of 15." became "With this change, `ReturnFieldsTest` passes 15 of 15."
  2. "The run used an earlier commit on this branch with identical source and test files. Verified 2026-10-05 at this head." became "Verified 2026-10-05 at commit `72caa363e29`, which has the same source and test files as this head `7463dd006a7`."

## PR #5137 (SOLR-7498), head `2050d8e447a7`. SATISFIED after 1 edit.

- Diff: `ExtractionBackend` (L55 to L58 at head), with a new two-case test. The counts (2/2, 2/2, 6/6) match the receipt.
- Edit: the link label "ContentStream.java L39" became "ContentStream.java L39 at base", because it links the base.

## Gate jobs filed this slice

- `gates/SOLR-6831-review-changelog-limits.md` (answers the #5134 changelog finding; OPEN)

## Record disagreements (flagged)

- SOLR-7390 receipt: the header says the gated head is `7463dd006a78`, while the status line says the gate ran at `72caa363e29`. The receipt should name `72caa363e29` as the gate commit.
- Receipts for 6975, 7390 and 7498 cite GitHub runs as "succeeded at this head". Those runs ran on CI-trigger commits one step above each head (`1c41811903c8`, `d46c4d6b759`, `86cd00fde81`). Each adds only `.github/workflows/fork-test-runner.yml`. The source matches the head, but the receipts should say so.
- Slate line 47 lists 6831 and 6975 as as-is under the advisory-date ruling. The dates were added from the receipts anyway.
- Not checked: the Jira text behind claims such as "the ticket reports" and the 7390 "second comment". No Jira packet was read.
