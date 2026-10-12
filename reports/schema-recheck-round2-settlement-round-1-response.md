# Schema re-check, round 2 register settlement, round 1: main-side response

Source: `reports/schema-recheck-round2-settlement-round-1.md`, which closed SCH-R2-01 to SCH-R2-05 and left one should-fix (SS-01) and three notes (SS-02 to SS-04) against the main side's records. Each item was verified against the live drafts, the round 2 record and the receipts before anything was edited. Verdicts: SS-01 CORRECTED in `reports/schema-convergence-round-2-response.md`; SS-02, SS-03 and SS-04 APPLIED as record corrections, SS-03 with one stated deviation (the stale citation stands in the round 2 response, not in the file the re-check named). No draft text changed.

## SS-01 | round 2 response, SCH-R2-06 paragraph | should-fix | CORRECTED

The paragraph said the SOLR-15358 record set was closed by the record-close settlements. The correct state: the record-close rounds settled RC-S1, RC-S3 and RC-S5, and no more than that. The round 2 record (line 34) still lists SCH-R1-16 as open, the round 1 response carries SCH-R1-16 as standing, and the SOLR-15358 draft's two owner holds (the call on the default docValues as stored setting, and landing after SOLR-15357) remain open by design; the round 2 response's own closing section lists SCH-R1-16 as standing open, so the file contradicted itself. Action: the clause is replaced with the re-check's replacement verbatim, as its own sentence: "RC-S1, RC-S3 and RC-S5 are settled; SCH-R1-16 and the two owner holds on SOLR-15358 remain open." A dated correction note in the paragraph records what it first said and why it overstated the state.

## SS-02 | round 1 response, SCH-R1-14 entry | note | APPLIED

The entry cited "lines 19 and 31" for the 1.6 and later wording. Verified against the live draft `pr-drafts/schema-analysis/SOLR-15358.md`: the wording stands at lines 19 and 33, and line 31 is the "A choice to check" heading. Action: the citation in the entry now reads "lines 19 and 33", with a dated citation correction note appended to the entry.

## SS-03 | landing-order line citation | note | APPLIED, at the place the citation stands

The re-check names the SCH-R1-15 entry in `reports/schema-convergence-round-1-response.md`. Verified: that entry carries no line citation for the landing-order bullet; a full-text search of that file finds no "line 41" citation (its only "41" strings are SCH-R1-41 and counts in other entries), and its SCH-R1-15 fact check cites the schema round 1 records, not a draft line. The stale citation stands in `reports/schema-convergence-round-2-response.md`, in the SCH-R2-04 paragraph: "draft line 41 reads". The landing-order sentence stands at line 43 of the live draft. Action: the round 2 response now cites draft line 43, with a dated correction note in that paragraph recording the old citation. The deviation from the re-check's file attribution is recorded here; the correction the item asks for is made where the citation stands.

## SS-04 | round 2 response, opening count | note | APPLIED

The opening paragraph counted the round 2 register as five items (SCH-R2-01 to SCH-R2-05). Verified: the register in `reports/schema-convergence-round-2.md` carries six items; SCH-R2-06 (line 61 of the record) is the sixth, and the response itself records it below as outside this settlement. Action: the count now reads "six items against the main side's own records (SCH-R2-01 to SCH-R2-06)", with a dated correction note in the opening paragraph stating that SCH-R2-06 is the sixth item and that the five items this settlement acted on are SCH-R2-01 to SCH-R2-05.

## Closing state

SCH-R2-01 to SCH-R2-05 stand CLOSED, as the re-check found. With SS-01 corrected, the round 2 response no longer claims the SOLR-15358 record set is closed: SCH-R1-16 and the draft's two owner holds remain open, as the same file's closing section already said. What stays open in schema is unchanged by this settlement: the SOLR-14199 draft pass (the owed Limits lines not yet pasted, the owner placeholder standing, the record job open), the SOLR-15357 test-fix job and its owner decision 2, the four branch-item jobs, and the SOLR-15358 holds. SOLR-14199 and SOLR-15358 stay NOT CONVERGED for those standing reasons.
