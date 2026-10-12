# Update processing convergence, round 6: main-side response

Source report: `reports/update-processing-convergence-round-6.md` (verdict NOT CONVERGED on two should-fix wording items in the SOLR-14718 audit record, UP-R6-01 and UP-R6-02; notes UP-R6-03 and UP-R6-04 on the round 5 response). Each item was verified against the audit file, the receipt, and the governing records before anything was applied. Both should-fix items are applied verbatim. Both notes are recorded as corrections to `reports/update-processing-convergence-round-5-response.md`. Round 6 confirmed the three drafts (SOLR-3657, SOLR-14718, SOLR-5065) converge on their text and that the rebuttal of UP-R5-01 stands; this response settles the audit record and the response wording.

## 1. UP-R6-01, SOLR-14718 audit finding 1 status cell (should-fix): applied verbatim

Verified: finding 1's status cell in `audits/update-processing/SOLR-14718.md` pointed to "the settling run section at the end of this file". The file's last section is the update dated 2026-10-12; the settling run section sits above it under its own heading. The cell now reads "(see the section 'Settling run for finding 1 (2026-10-09)')", which is the section's exact heading. The settling run itself matches `receipts/SOLR-14718.md`: three seeds (`14718C0FFEE14718`, `14718C0FFEE14719`, `14718C0FFEE14720`), one AssertionError each at `SolrCmdDistributorTest.java:572`, control 0 failures.

## 2. UP-R6-02, SOLR-14718 audit update section (should-fix): applied verbatim

Verified: the update's closing sentence said "the finding texts and their audit-date evidence are unchanged". Finding 2's evidence cell ends with the sentence "The call has since been made; see the update dated 2026-10-12 at the end of this file.", appended in the round 5 application (its response, section 4, states that deviation). The closing sentence now reads "the finding texts are unchanged, and finding 2's evidence cell gains one appended sentence pointing here".

## 3. UP-R6-03, round 5 response citation (note): recorded as a correction

Verified against the records: `TESTING.md` recommends recording the second flaw as a follow-up and stating it in the draft's Limits (lines 164 to 165), and records the ship-narrow decision for SOLR-14718 (lines 277 to 280). It makes no follow-up commitment. The commitment is the owner's 2026-10-09 ratification: `material/review-opened-28-answers.md`, "Confirmations ratified by the owner", item 1 ("the planned follow-up commitments in the 22 drafts stand"), and `reports/review-corrections-28.md` item 1 lists SOLR-14718 among the drafts stating such a commitment. A dated correction note is appended to section 1 of the round 5 response, adopting the round's suggested wording: the sentence rests on the owner's ratification, and `TESTING.md` is no longer cited as the commitment's source.

## 4. UP-R6-04, round 5 response conflict framing (note): recorded as a correction

Verified against the records: the "planned follow-up sentences stay" entry in `material/review-opened-28-answers.md` sits under the heading "Round 2 owner calls adopted under the standing decision practice (2026-10-09)"; the entry's own text says the owner made no further calls and the recorded recommendations were adopted under the standing practice. It is a main-side record, not an owner decision. The gap the ratification lists for SOLR-14718 (its item 2, the five gaps without a plan sentence) is the corrections report's item 2 gap, draft line 33 ("Two paths still name one request"), a different limitation from the line 36 sentence the rebuttal defends. So the round 5 response's framing of an earlier record against a later governing one is dropped in the same correction note. No draft change follows, as the note states; the rebuttal of UP-R5-01 stands on the ratification alone, and round 6 confirmed it stands.

## Convergence statement

All four round 6 items are settled: UP-R6-01 and UP-R6-02 applied verbatim, UP-R6-03 and UP-R6-04 recorded as corrections to the round 5 response. Round 6 confirmed the drafts converge and the one rebuttal stands, and the audit record now matches its own contents. Update processing is fully converged. No round 7 assignment is posted.
