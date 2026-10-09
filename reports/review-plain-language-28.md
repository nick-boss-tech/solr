# Verification of the plain-language pass over the 28 drafts

Claim: `claims/review-plain-language-28.md` (commit `004575c037b`). Rulings: `material/review-opened-28-answers.md` (commits `555412ae8d4`, `9f567474f62`). Pass: commit `78ce00d90af`, which changed 24 drafts. Previous version compared: `bc61a3ce3d3`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two read-only subagents checked 25 drafts, 12 in one group and 13 in the other. Group reports: `reports/review-plain-language-28-a.md` and `reports/review-plain-language-28-b.md`. The lead merged them. No draft, receipt, branch, or PR was changed.

## Result

- **Commit heads.** No head was dropped from any draft. All heads resolve in the repository, except the merged tree `1ddbf36202d` in SOLR-5754 and SOLR-5939, which is a local merge commit and is not present in this clone. That is prose only, as before.
- **Counts and dates.** No count or date changed or disappeared.
- **Internal vocabulary.** None left in public text, apart from the approved AI footer line, and no em dashes.
- **Proof against receipts.** Several sentences are not supported by their receipts. They are listed below. The drafts are final by the owner's ruling, so none has been edited.

## Findings for the owner

1. **SOLR-7022: text-only claim.** The receipt says the commits from the gate to the live tip change only text. Commit `6a233ab2fdb` adds `DirectUpdateHandler2CommitWaitTest.java`, 162 lines. The owner ruled to drop the Proof line about that class, so the public text no longer names it. Confirm that the receipt wording should be corrected, or that the claim should stay as it is.
2. **SOLR-6973: "by reading".** The draft says the new test is the one that breaks "by reading". The receipt records a base run with one failure, and it does not name the failing test. Replace the sentence with the receipt's statement, or keep the reading and say so. Proposed text: "On base production, the class runs 7 tests with 1 failure."
3. **SOLR-13696: pre-fix heads.** The four pre-fix heads in Proof (`98ad9d3fcc33`, `a4e0da422327`, `08f9384e47c0`, `c3cdf7b46e8`) are not named by the SOLR-13696 receipt. They come from the round 3 close-out answers and the round 8 addendum on the branch. Confirm that those records are an acceptable source, or remove the heads.
4. **SOLR-13943: normal-mode wording.** The draft says the counts for CategoryRoutedAliasUpdateProcessorTest (6 of 6), DimensionalRoutedAliasUpdateProcessorTest (2 of 2), and CreateAliasAPITest (13 of 13) were "all in normal mode". The receipt gives normal mode only for the 1 of 1 class. Confirm whether to keep "all in normal mode".
5. **"Live tip" wording.** "Live tip" appears in four drafts. It is internal shorthand for the branch head. Confirm whether to replace it with "head" or "current head".
6. **Minor, not blocking.** SOLR-5941 says "checked by reading the base code", which the receipt does not record. SOLR-13265 says "with only this test file applied", which the receipt does not record. Both are about how a check was made, not about a count or head.

## Corrections to the subagents' reports

- The group A report says the commit `7fbe0128d8b0` appears in no receipt. That is wrong. The SOLR-5754 receipt names it as the gated head of the combined run. The finding is withdrawn.

## Not done

No draft was edited in this round. No PR body or title was changed, and no submit branch was edited. No build, Gradle run, or test was run.
