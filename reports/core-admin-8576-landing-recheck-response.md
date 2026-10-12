# Core admin, SOLR-8576 landing re-check: main-side response

Source report: `reports/core-admin-8576-landing-recheck-round-1.md` (verdict NOT CONVERGED on two should-fix items, CA-LR-01 and CA-LR-02; notes CA-LR-03 to CA-LR-06). Each item was verified against `pr-drafts/core-admin/SOLR-8576.md` at head `883bb522ca`, `receipts/SOLR-8576.md`, and the branch test code at that head before anything was applied. CA-LR-02 is applied verbatim. CA-LR-01 is applied with one stated deviation. The notes are not applied; they stand on the record.

## CA-LR-01, Proof wording (should-fix): applied, with one stated deviation

Line 19 (the Proof bold line) now reads the replacement verbatim: "**This is a test-only change, so the proof is inconclusive by construction: the new test also passes on the base code.**" Verified: `pr-formula.md` section 3 requires a proof that is inconclusive by construction (test-only changes are named in the rule) to be stated plainly, and the template says "say exactly that, and why". The receipt records the change as test-only and the strengthened test passing on merge-base production, so the statement is accurate.

Line 22 carries the replacement's base-run facts: the test, the base commit `b6b2b8f10e`, the counts (1 test, 0 failures) and the date (recorded 2026-10-12), all from the receipt's pin run at the gated head.

Deviation, stated plainly: the replacement's final sentence, "No fail-before run was made.", is not applied as worded. The receipt records a run of the strengthened test on merge-base production `b6b2b8f10e` (1 test, 0 failures), so a reader could take the sentence to deny a run the same bullet has just reported. The applied bullet closes instead with the draft's existing accurate sentence: the test checks error behavior that already exists, so it does not show a failure before the change. The re-check is asked to judge the deviation.

## CA-LR-02, "unchanged" overstates the test (should-fix): applied verbatim

Line 15 now reads: "After that, it checks that the original collection still exists under its name and that the alias still resolves to it."

Verified against the branch at `883bb522ca`, `CollectionsAPISolrJTest.testCreateCollectionNameAlreadyTaken`: lines 1186 to 1187 assert only that the collection fetched under its name has that name; lines 1189 to 1190 assert the alias resolves to the collection; line 1191 asserts no collection carries the alias name. No assertion covers the collection's content or state, so "unchanged" said more than the test asserts, and the replacement matches the assertions exactly.

Overlap with Core admin round 3, stated for the record: this sentence is the closing sentence CA-R1-21 prescribed ("...that the original collection is unchanged and that the alias still resolves to it"), and the round 3 assignment (`assignments/pool-core-admin-convergence-round-3.md`, item 2) asks the reviewer to judge the draft with that post-fix form in place. The applied replacement supersedes CA-R1-21's wording on the code evidence above; round 3 should judge line 15 as it now stands. No other draft edit was made for round 3's sake: the rest of its SOLR-8576 scope (the marker deleted, head, counts and links at `883bb522ca`) was confirmed by the landing re-check itself.

## CA-LR-03 to CA-LR-06 (notes): not applied

The round's convergence line asks only for the two should-fix replacements, and each note offers an optional replacement. They stand on the record for any later pass: the Limits do not name that the checks accept any exception type, so the error code is unchecked (CA-LR-03); the bold line at line 7 could be narrowed to the class the branch touches (CA-LR-04); the class and test name in the Proof are not links (CA-LR-05); a blank line before "## Limits" is doubled (CA-LR-06).

## Convergence line

With CA-LR-01 and CA-LR-02 applied, no blocking or should-fix item from the landing re-check remains open against the draft. A short re-check of lines 15, 19 and 22, as the report proposes, closes the round; Core admin round 3 also covers this draft.
