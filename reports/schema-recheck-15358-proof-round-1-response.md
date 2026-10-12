# Schema re-check, SOLR-15358 Proof round 1: main-side response

Settles the two should-fix items in `reports/schema-recheck-15358-proof-round-1.md` (SCH-R3-01 and SCH-R3-02) against `pr-drafts/schema-analysis/SOLR-15358.md`. Each item was checked against `receipts/SOLR-15358.md` (the gate record and the proof record dated 2026-10-12) and `pr-formula.md` section 3 before any edit. Both items held, and both replacements are applied. The two edits land as one change, because the SCH-R3-02 replacement paragraph opens with the SCH-R3-01 replacement sentence.

## SCH-R3-01: APPLIED

The draft said "CurrencyFieldTypeTest passed on that tree as well (42 tests, 21 skipped, 0 failures)." The receipt's proof record states the counts only: CurrencyFieldTypeTest 42 tests, 21 skipped, 0 failures. It records no pass for that class, and the receipt's gate section names the 21 skips as the suite's currency-locale assumption skips, so half the class did not run. "Passed" states more than the record holds. The sentence now reads "CurrencyFieldTypeTest reported no failures on that tree as well (42 tests, 21 skipped by its currency locale assumption, 0 failures)", which is exactly what the receipt records. The same wording now stands in the draft's Proof as its own paragraph (line 29), and the 21-skipped clause already appears in the head-run paragraph (line 25) for the same class and the same skip reason, so the two paragraphs now agree.

## SCH-R3-02: APPLIED

`pr-formula.md` section 3 asks for one line per test class, counts inline, with the verification date and head. The filled Proof put both classes in one paragraph, and the CurrencyFieldTypeTest sentence carried neither a date nor a head. The paragraph now splits at "Both failures are the missing sub-field docValues this change adds.", and CurrencyFieldTypeTest stands as its own paragraph: "CurrencyFieldTypeTest reported no failures on that tree as well (42 tests, 21 skipped by its currency locale assumption, 0 failures), in the same run at head `cbb345f2e7df452e20036d357c82cc63db96ffd3`, recorded 2026-10-12."

The added facts check against the receipt. The proof record is a single run (one log, counts from the one set of fresh JUnit XML) covering both classes, so "the same run" holds. The head is the proof tree's head as the receipt names it, and the date is the proof record's date, 2026-10-12.

## Notes SCH-R3-03 and SCH-R3-04: not acted on in this lane; SCH-R3-05: APPLIED

The round marked SCH-R3-03 and SCH-R3-04 as notes and SCH-R3-05 as a note calling for a record correction; only SCH-R3-03 carries an optional wording suggestion. SCH-R3-03 and SCH-R3-04 concern the draft's internal header, which comes off before posting, and neither touches the outbound Proof text, so this settlement leaves them as standing notes for the schema track to pick up or drop. SCH-R3-05 is applied: `reports/schema-convergence-round-2.md` line 34 now records that the placeholder hold closed on 2026-10-12, when the Proof was filled from the proof record (`a940d43d7c9`), and that the holds that remain are the owner call on the default docValues as stored setting and landing after SOLR-15357.

## State after this settlement

Both should-fix items are settled. The SOLR-15358 draft's Proof is settled, subject to the schema convergence round 2 re-check already posted. The draft's two holds stand unchanged: the owner call on the default docValues as stored setting, and landing after SOLR-15357.

## Corrections (2026-10-12)

The applied re-check (`reports/schema-recheck-15358-proof-applied-round-1.md`) found two record items in this response and in the schema round 2 record. Both are corrected in place: the SCH-R3-01 section above no longer says the reported wording already stood in the draft's head-run paragraph (RC-S1), and the notes section above now records SCH-R3-05 as applied (RC-S3). The settlement is recorded in `reports/schema-recheck-15358-proof-applied-round-1-response.md`.
