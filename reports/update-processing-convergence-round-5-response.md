# Update processing convergence, round 5: main-side response

Source report: `reports/update-processing-convergence-round-5.md` (verdict NOT CONVERGED; should-fix items UP-R5-01 and UP-R5-02, notes UP-R5-03, UP-R5-04 and UP-R5-05). Each item below was verified against the live draft, the receipt, and the git and file records before anything was applied. One item is applied verbatim, one is rebutted on records the round did not check, one note is confirmed against a ruling record, and two notes are record corrections, both now recorded.

Round 6 (`assignments/pool-update-processing-convergence-round-6.md`) confirms exactly these items.

## 1. UP-R5-01, SOLR-14718 draft line 36 (should-fix): rebutted, no edit

The finding holds that the sentence "A follow-up submission for that flaw is planned." promises something the record does not show, citing the audit and `TESTING.md`. Two records the round did not check decide the point the other way:

- `material/review-opened-28-answers.md` records the owner's 2026-10-09 ratification that the planned follow-up commitments in the drafts stand. A later entry in the same file, the round 2 owner calls adopted under the standing decision practice, names this draft: "The planned follow-up sentences in SOLR-5065, SOLR-12705, SOLR-14718 and SOLR-14262 stay (they follow the standing follow-up rule; the corrections report's exception list was its own miscount)". The ratification entry earlier in the file had listed SOLR-14718 among the gaps without a plan sentence at that point; the adopted-calls entry is the later and governing record, and it names the sentence as staying.
- `TESTING.md` recommends recording the second flaw as a follow-up and stating it in the draft's Limits, and its decision entry for SOLR-14718 records ship narrow with the second flaw not included. A follow-up named in the Limits with a stated plan to submit it is what the standing follow-up rule calls for, and the sentence is that statement.

The sentence stays as written. Round 6 is asked to judge this rebuttal against the two records.

Correction (2026-10-12, from round 6, UP-R6-03 and UP-R6-04): two points of framing in this section are corrected, and the conclusion is unchanged. The sentence rests on the owner's 2026-10-09 ratification of planned follow-up commitments in the 22 drafts (`material/review-opened-28-answers.md`, "Confirmations ratified by the owner", item 1; `reports/review-corrections-28.md` item 1 lists SOLR-14718 among the drafts stating such a commitment). `TESTING.md` recommends recording the second flaw as a follow-up and records the ship-narrow decision; it makes no commitment itself, so it is not cited here as the commitment's source. There is also no conflict between records to resolve: the gap the ratification lists for SOLR-14718 (its item 2) is the corrections report's item 2 gap, draft line 33 ("Two paths still name one request"), a different limitation from the line 36 sentence; and the "planned follow-up sentences stay" entry quoted above sits in a main-side record of calls adopted under the standing decision practice, not in an owner decision. The rebuttal stands on the ratification alone.

## 2. UP-R5-02, SOLR-3657 draft line 25 (should-fix): applied verbatim

The parenthesis now reads "(verified 2026-10-07)" in place of "(gate of 2026-10-07)". The rest of the bullet is unchanged.

Verification before applying: `receipts/SOLR-3657.md` is dated 2026-10-07 and records `DocumentBuilderTest` 17/17 and `TolerantUpdateProcessorTest` 11/11 at head `14edaca577c0`, with the pre-fix proof at exactly 2 failures at the destination-prefix assertions; the bullet above already uses "verified 2026-10-07" for the same run. The owner's 2026-10-09 ruling recorded in `material/review-opened-28-answers.md` also strikes "gate" as internal vocabulary that public text does not use, so the replacement follows the ruling as well as the finding.

## 3. UP-R5-03, SOLR-14718 draft line 31 (note): confirmed against the ruling record, no edit

The note asked for the landing-order sentence to be confirmed before posting, because no receipt in the round supports it. The supporting record is a ruling, not a receipt: `material/update-29-rulings.md`, item R3, records "SOLR-14718 lands after 5939, and its draft's Limits are rewritten to the post-5939 mechanism", and `material/update-29-reconciliation-answers.md` records the same position as adopted. Draft line 31 states that order, and the draft's Limits are written to the post-5939 mechanism as the ruling directs. Nothing to change.

## 4. UP-R5-04, SOLR-14718 audit (note): applied, with one stated deviation

`audits/update-processing/SOLR-14718.md` is updated in place, dated 2026-10-12:

- Finding 1's status cell no longer reads "Still open"; it records settlement by the 2026-10-09 run.
- The readiness line is marked superseded, and a dated update section at the end of the file records that neither blocking item stands: finding 1 settled by the run, finding 2 decided (ship narrow, recorded in `TESTING.md`).

Deviation, stated plainly: the finding named finding 1's status cell and the readiness section. Finding 2's status cell is updated as well, on the same record, because leaving it at "Still open" beside the corrected readiness section would keep the same kind of mismatch the finding flags. The finding texts are unchanged; finding 2's evidence cell gains one appended sentence pointing at the update.

## 5. UP-R5-05, SOLR-5065 (note, record correction): recorded; no draft change

Round 4 finding 3 is withdrawn, in round 5's record wording, both here and as a dated correction note inside `reports/update-processing-convergence-round-4.md` at finding 3: "Round 4 finding 3 is withdrawn. `0cc328310f8f` is the merge-base of `ab894a996c8a` with upstream/main, so the original label was accurate. The class link stays at L31-L49."

Re-verified on this host on 2026-10-12 against a fresh fetch of upstream main: `git merge-base` for `ab894a996c8a` returns `0cc328310f8f`; `97d973814336` is not an ancestor of the head; `DefaultSchemaSuggesterTest.java` has 49 lines at the head, so the `L31-L49` link is correct and the round 4 suggestion of `L31-L48` is withdrawn with the finding. The draft's current wording ("base production code", applied in round 4) asserts nothing false and matches `receipts/SOLR-5065.md`, so the draft stays as it is and SOLR-5065 stays CONVERGED.

## 6. SOLR-7022 re-verification: confirmed CONVERGED, no action

Round 5 verified the two corrected run claims, and this pass re-checked them against the live files: draft line 24's run at `a2ed957063b` (2026-10-05, `DirectUpdateHandler2AwaitSearcherTest` 2 of 2, `DirectUpdateHandlerTest` 7 of 7) matches `receipts/SOLR-7022.md` line 5, and line 26's last sentence ("read to fail" at the interrupt-restored assertion, "this was not run") matches receipt lines 5 to 7 and `audits/update-processing/SOLR-7022.md` finding 3. The draft is unchanged since round 5. Nothing to apply.

## Summary

- UP-R5-01: rebutted with evidence; the sentence stands.
- UP-R5-02: applied verbatim.
- UP-R5-03: confirmed against ruling R3; no change.
- UP-R5-04: applied with the stated deviation.
- UP-R5-05: recorded; round 4 finding 3 withdrawn.
- SOLR-7022: confirmed converged.
- No draft remains open on a should-fix item unless round 6 upholds UP-R5-01 against the rebuttal.
