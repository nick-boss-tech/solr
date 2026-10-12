# Schema re-check, SOLR-15358 record close, round 1: main-side settlement

Dated 2026-10-12. Settles the one item `reports/schema-recheck-15358-record-close-round-1.md` left open (RC-S5). That re-check closed RC-S1 and RC-S3 on the strength of commit `5005a5204d9`; those closures stand and are not reopened here. RC-S5 was checked against `receipts/SOLR-15358.md`, the draft `pr-drafts/schema-analysis/SOLR-15358.md` as it now stands, and the round 2 record itself before any edit. The item held, and the correction is applied in place.

## RC-S5: APPLIED verbatim

`reports/schema-convergence-round-2.md` line 14, the SOLR-15358 verdict line, still read "The Proof placeholder is unfilled, and the owner call and the land-after-SOLR-15357 hold stand." Item 3 of the same record (line 34) already records that the placeholder hold closed on 2026-10-12, so the two lines disagreed. The underlying facts check out: the receipt holds the proof record (vm2, 2026-10-12; CurrencyFieldTypeDocValuesTest 2 tests, 2 failures on the proof tree, CurrencyFieldTypeTest 42 tests, 21 skipped, 0 failures), commit `a940d43d7c9` filled the draft's Proof from that record, and the draft carries no "TO FILL" text. The line now reads, with the re-check's replacement applied exactly: "The Proof placeholder closed on 2026-10-12 (see Item 3), and the owner call and the land-after-SOLR-15357 hold stand." The verdict on the line (NOT CONVERGED on the two holds) is unchanged, as the re-check prescribed.

## Closure

RC-S5 is applied. With it, every item the record-close re-check raised is settled: RC-S1 and RC-S3 closed by `5005a5204d9` and confirmed by the re-check, RC-S5 corrected here. The SOLR-15358 record set is now fully closed: the draft's Proof matches the receipt, the round 1 response record is accurate, and the schema round 2 record agrees with itself on the placeholder. The draft itself stays on its two holds, the owner call on the default docValues as stored setting and landing after SOLR-15357. Those are an owner call and a landing-order item, not record items, and they are the reason the round 2 verdict for SOLR-15358 stays NOT CONVERGED.
