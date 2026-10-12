# SOLR-13943 draft re-check after the re-gate at ca443f6f68, round 1

Verdict: **NEEDS EDIT, and two edits are applied.** The links, anchors, changelog target and dates hold at the new head. The counts were tied to an earlier head and an earlier date, and two sentences now name both correctly. Nothing else changed.

Scope: `pr-drafts/update-processing/SOLR-13943.md` at `3250bc31c74`, checked against `receipts/SOLR-13943.md` (top section, head `ca443f6f680606b6c995ae86b88b4841b53addbf`, GREEN) and the fork branch fetched locally. The claim is `claims/pool-recheck-13943-landing.md`. Two read-only subagents, one for counts and claims, one for links and anchors.

## Checks

- **Heads and links (PASS).** The fork tip `solr-13943-submit` is `ca443f6f68`. All eleven links resolve: the head paths exist at the head, and the base link `c3cdf7b46e8c` is the pre-change annotation the draft names. The merge-base with `upstream/main` is `c3cdf7b46e8c`, which matches the draft's wording.
- **Line anchors (PASS).** Each range covers what the sentence says, at the commit the link names.
- **Counts and dates (FAIL, fixed).** The counts in the Proof were tied to the earlier head `cc155cf68e1d` and dated 2026-10-09. The top section records them at `ca443f6f68` on 2026-10-12.
- **Awaitsfix run (FAIL, fixed).** The "six tests, five pass" run was recorded at the earlier head, not at this head. The sentence now says so.
- **Stack and file-count claims (PASS, not in the receipt).** Between the stack base `1d0b8a0a73cd` and the head, exactly three files change: the changelog fragment, `TimeRoutedAliasDateMathInStartTest.java` and `TimeRoutedAliasUpdateProcessorTest.java`. The commit `da4fa6df117` adds a SOLR-13696 changelog fragment and touches no file this branch changes. These sentences are not in the receipt, but the code confirms them, so they stay.
- **Dashes and process words (PASS).** None in the draft. The AI header and footer are present.

## Edits applied

- Proof, counts line: "These counts are from a test run at `ca443f6f680606b6c995ae86b88b4841b53addbf` (2026-10-12). The earlier head `cc155cf68e1d` differs from this head only by the changelog fragment." (was: a run at `cc155cf68e1d` on 2026-10-09).
- Proof, awaitsfix sentence: "At the earlier head `cc155cf68e1d`, with the `awaitsfix` group enabled, `TimeRoutedAliasUpdateProcessorTest` runs six tests. Five pass." (was: the same sentence with no head named).

## Notes for the branch owner (not draft edits)

- The changelog fragment on the branch (`changelog/unreleased/SOLR-13943.yml`, line 1) names the old class `TimeRoutedAliasUpdateProcessorTest.testDateMathInStart`, while the method now lives in `TimeRoutedAliasDateMathInStartTest`. Editing the fragment changes the branch, so it is the branch owner's call.
- The draft's Limits say no run of the moved test against base is on record. That stays true and is correctly stated.

## Convergence line

The draft converges after the two edits above. The branch-side changelog title is a note for the branch owner.
