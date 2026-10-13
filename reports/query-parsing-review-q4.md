# Query parsing review, sub-batch q4: live PR text review and tickets without a draft

Reviewer: Windows review host (claim `claims/query-parsing-review.md`), 2026-10-13.

## SOLR-16130: live PR text review (text only; no draft file in this folder)

No file under `pr-drafts/` in this tree is for SOLR-16130. The only record here is `receipts/SOLR-16130.md`. So there was no draft file to edit. The review read the live PR body, read-only, through `research\gh.ps1 pr view 5004 --repo apache/solr`. This lane makes no PR edits and no comments. Edits to the PR description stay with the main side.

Live PR 5004: OPEN, head `3c48dec4b79faed625c4fb79833d0975a55f3ed7`, which equals the receipt's gated head.

Checked and holding:
- Head, gate date (2026-10-06), the 10 of 10 count for `CrossCollectionJoinQueryTest`, and the GitHub run ID 37581056636: all match the receipt.
- The fail-before base is `70c1a28995d`, the parent of guard commit `56ec140e363`. The NullPointerException text and `ExportWriter.java` line 863 match the receipt's log extract.
- The changelog exemption: `dev-docs/changelog.adoc` line 130 exempts pull requests that touch only tests. The body's claim holds.
- "Only `CrossCollectionJoinQueryTest` uses that configset": the only test under `solr/core/src/test` that names `ccjoin` at head is `CrossCollectionJoinQueryTest.java`. Holds.
- Test-only change: no production diff is claimed, and none is needed.
- Length is about 2,700 characters, under the 3,500-character guide.

Wording findings for the main side (FIX, to apply to the live body):
1. Proof heading: "The new test fails against the parent of the fix commit". The PR has no fix commit, and the bullet below says "the parent of the guard commit". Use "guard commit" in the heading too.
2. Proof line: "(tidy clean, Error Prone compile clean, module check passes)". These are internal gate steps. The formula keeps internal checks out of the proof unless they change behavior. Suggest dropping the parenthetical.
3. "The class also passed in a GitHub Actions run on this code (run 37581056636, CI commit `4c151ce1b79`, which adds only the workflow file)". The run ID is in the receipt. The CI commit `4c151ce1b79` is not in the receipt and was not checked. It also refers to fork CI setup rather than the upstream change. Suggest removing the CI-commit clause.

Not checked:
- The body's description of the test (the two-segment assertion and the four expected ids). The test was not read in full.
- The claim that `useFilterForSortedQuery` routes `/export` through `sortDocSet` and skips `getLeafCollector` for trailing unmatched leaves. That is the #4953 analysis, not verified here.

## SOLR-13202: EXCLUDED (merged upstream)

- The PR is merged. The receipt records PR #5012 APPROVED 2026-10-06 and merged into apache/solr main. The status line was corrected on 2026-10-10.
- Live tip `c0aec3a7ae01aa629ba1e5e06249568054dc267f` equals the receipt head. Nothing to review, per the assignment's exclusion.

## SOLR-13838: NO DRAFT YET (record gap)

- Reconciled green at `4520c25abe28228bfa011102814b9b1063a4415a`, which equals the live tip. Both test classes pass 1 of 1, with a NaN pre-fix proof.
- The record is a reconcile-table row in the receipts ledger. It names no gate log and no date. Before a draft can cite proof counts, the main side needs a gate log or a dated record. This is a record gap, not a review finding.

## SOLR-13903: NO DRAFT YET (record gap)

- Reconciled green at `73ef411dd117cc826caf15baa1c71e50ff5d622e`, which equals the live tip. Both test classes pass 1 of 1.
- Same record gap as 13838: no gate log, no date. The main side should supply them before drafting.

## SOLR-15906: NO DRAFT YET

- Gate green at `50139a8e979cd33319a30317d9371ac0b9198e73`, which equals the live tip. TestSolrQueryParser 39 of 39 (gate log `g15906r35-gate.log`, pushed 2026-10-07). Base failure: `testTextAfterLocalParamsWithExplicitValueIsNotIgnored`.
- The receipt's note says an earlier head regressed (the lucene fallback recursed on subquery suffixes), and the shipped head fixes it. That history belongs in a draft's Choice or Limits when one is written.
- It touches `QParser.java`. The auto-fix premises of 8977 and 12212 may be affected. That interaction was not audited here (no draft to audit against).

## SOLR-16570: NO DRAFT YET (audit only in round 1)

- Receipt: GATE GREEN at `9d3466101a29b70ac0b45aa0770b4cf4f34e6cb3`, which equals the live tip. TestCollapseQParserPlugin 20 of 20; base has one failure, `testTopFcHintOnDocValuesWithoutUninversion`. Gate finished 2026-10-09.
- Round 1 recorded NO GATE at `974c44f96`. That was the head then. The branch has moved since, and the receipt now records a gate at the new head. See the summary for the disagreement.
- Collapse pairing with 17796: 16570 changes `CollapsingQParserPlugin.getTopFieldCacheReader`. 17796's tests are in `TestCollapseQParserPlugin`, the same class 16570 extends. Noted, not audited.

## SOLR-17882: EXCLUDED (premise dead, retire candidate)

- Live tip `d328eb392d9c9acf3d925bb20729798e0d17720f` equals the receipt head.
- The receipt records the premise as dead: the LTR coordinator failure does not reproduce as a defect this branch fixes. The branch is a retire candidate. The retire-or-re-aim call is the owner's, and it is still pending on the main side. Nothing to draft, per the assignment.
