# Update processing convergence, round 4: main-side response

Source report: `reports/update-processing-convergence-round-4.md` (verdict NOT CONVERGED; three should-fix items). Each item below was verified against the live files and the underlying records before anything was applied. The two SOLR-7022 run claims from the same period were corrected earlier and are not part of this response.

Round 5 (`assignments/pool-update-processing-convergence-round-5.md`) re-verifies exactly these items: the SOLR-14718 receipt record and draft line 25, the SOLR-3657 bold line 22, the SOLR-5065 lines 9 and 25, and the SOLR-7022 corrections.

## 1. SOLR-14718 (finding 1): applied, receipt side; the draft sentence stands

The round 4 fix prescribed adding the 2026-10-09 settling run to `receipts/SOLR-14718.md`, after which the draft sentence at line 25 can stay as written. The run was verified on this host before the receipt was touched:

- Log `g14718-settle.log` (2026-10-09): three reverted runs at seeds `14718C0FFEE14718`, `14718C0FFEE14719` and `14718C0FFEE14720`, each ending rc=1, then a control run at the unmodified head ending rc=0.
- The fresh JUnit XML kept with the log (`g14718-settle-xml/reverted-1.xml`, `reverted-2.xml`, `reverted-3.xml`, `control.xml`): each reverted run is 1 test with exactly 1 failure at `SolrCmdDistributorTest.java:572`; the control is 1 test with 0 failures.
- The audit section "Settling run for finding 1 (2026-10-09)" in `audits/update-processing/SOLR-14718.md` records the same run: the AssertionError text is "failed request lost its document", the production file was restored from merge-base `9b3a84b1c460`, and the branch test was kept. `git merge-base` for the live head `29c09959791a` against current main returns `9b3a84b1c460`, matching the audit and the draft.

The receipt now carries a "Settling run (2026-10-09...)" line with the seeds, the per-run failure, and the passing control. Draft line 25 is unchanged: its sentence ("A check on 2026-10-09 removed the copy at this head. The test then failed the same assertion in each of 3 runs, and the unmodified head passed.") now has the receipt behind it.

## 2. SOLR-3657 (finding 2): applied verbatim

Bold line 22 of `pr-drafts/update-processing/SOLR-3657.md` now reads exactly the recorded replacement: "**The focused tests pass at this head. On the merge-base code at `97d973814336`, with this branch's `DocumentBuilderTest.java` applied, the run without the fix has 2 failures.**"

Verification before applying: `receipts/SOLR-3657.md` states the proof condition with the overlay ("On base production with this branch's `DocumentBuilderTest.java` applied, DocumentBuilderTest runs 17 tests with exactly 2 failures"), and `git merge-base` for the live head `14edaca577c0` against current main returns `97d973814336`, the SHA the replacement names. The bullet below the bold line already carried the condition and is unchanged.

## 3. SOLR-5065 (finding 3): applied verbatim, with a verification note

Both recorded replacement clauses are applied:

- Line 9 now ends "(at base `0cc328310f8f`)" in place of "(at the merge-base 0cc328310f8f)".
- Line 25's first clause now reads "On the base production code at `0cc328310f8f`, with this branch's `DefaultSchemaSuggesterTest.java` added (the class is new on this branch), `testGuessFieldTypeExponentFormsInferDouble` fails." The rest of the bullet is unchanged.

The applied text matches the receipt's own wording ("On base production 0cc328310f8f...", `receipts/SOLR-5065.md`), so draft and receipt now say the same thing.

Verification note, stated plainly because it cuts against the finding's premise: `git merge-base` for the live head `ab894a996c8a` against current upstream main returns `0cc328310f8f` itself, and `97d973814336` is not an ancestor of the branch head (`git merge-base --is-ancestor` fails; `0cc328310f8f` is an ancestor of `97d973814336`, not the other way around). On that evidence the draft's original "merge-base" label for `0cc328310f8f` was accurate too. The replacement is applied anyway: it asserts nothing false, it aligns the draft with the receipt, and the word "base" is all the proof claim needs. Round 5 can judge whether the label should be restored; nothing else in the draft depends on it.

The round 4 note about the class link range at line 25 (`#L31-L49` against a 48-line file) was not a finding and is not changed here; the round 5 assignment carries it as a note.

## Summary

- SOLR-14718: applied (receipt gained the verified 2026-10-09 settling run; draft unchanged by design).
- SOLR-3657: applied verbatim.
- SOLR-5065: applied verbatim, with the merge-base verification note above.
- Nothing rebutted, nothing held.
