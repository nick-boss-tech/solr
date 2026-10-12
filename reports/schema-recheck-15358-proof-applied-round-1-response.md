# Schema re-check, applied SOLR-15358 Proof items, round 1: main-side settlement

Dated 2026-10-12. Settles the two record items in `reports/schema-recheck-15358-proof-applied-round-1.md` (RC-S1 and RC-S3). That re-check confirmed the draft items SCH-R3-01 and SCH-R3-02 as applied; they are not reopened here. Each record item was checked against `receipts/SOLR-15358.md`, the draft as it now stands, and `gates/SOLR-15358-proof-record.md` before any edit. Both items held, and both corrections are applied in place.

## RC-S1: APPLIED

The round 1 response (`reports/schema-recheck-15358-proof-round-1-response.md`, SCH-R3-01 section) said the reported wording "already stands in the draft's head-run paragraph". Checked against the draft: line 25, the head-run paragraph, has only "21 skipped by its currency locale assumption" for CurrencyFieldTypeTest. The words "reported no failures" and "0 failures" appear only in the new Proof paragraph at line 29. The sentence is corrected in place with the re-check's replacement: the wording now stands in the draft's Proof as its own paragraph (line 29), and the 21-skipped clause already appears in the head-run paragraph (line 25).

## RC-S3: APPLIED

`reports/schema-convergence-round-2.md` line 34 still carried the placeholder as an open hold and said the draft's line 27 read "[TO FILL before posting ...]". Checked: the gate file is DONE by vm2 (2026-10-12T09:37Z), the receipt holds the proof section, commit `a940d43d7c9` filled the Proof from that record, and the draft's line 27 is now the without-fix paragraph. Line 34 is corrected in place, dated 2026-10-12: its opening puts the round 2 state in the past tense, and its hold sentence now reads "The placeholder hold closed on 2026-10-12, when the Proof was filled from the proof record (`a940d43d7c9`). The holds that remain are the owner call on the default docValues as stored setting and landing after SOLR-15357." The round 1 response's notes section now records SCH-R3-05 as applied, with that wording.

## Notes RC-S2 and RC-S4

RC-S2 is corrected as part of the RC-S3 edit. The round 1 report labels SCH-R3-03 and SCH-R3-04 as notes and SCH-R3-05 as a note calling for a record correction; only SCH-R3-03 carries an optional wording suggestion. The response's notes section no longer calls all three optional.

RC-S4 is left standing. The response's closing sentence says the Proof is settled subject to the schema convergence round 2 re-check already posted. That re-check is still the open path for the filled Proof, and the sentence after it already names the draft's two holds, so the suggested replacement would only repeat them. The round 2 record's SOLR-15358 verdict line is a round 2 snapshot and is not edited here; line 34 is the line RC-S3 named.

## Closure

Both record items are applied. The SOLR-15358 proof record is now closed: the draft's Proof matches the receipt, the round 1 response record is accurate, and the schema round 2 record no longer carries the placeholder as an open hold. The draft itself stays on its two holds, the docValues-as-stored owner call and landing after SOLR-15357. Those are an owner call and a landing-order item, not record items.
