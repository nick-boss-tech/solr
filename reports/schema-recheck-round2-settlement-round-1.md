# Schema re-check, round 2 register settlement (SCH-R2-01 to SCH-R2-05), round 1

Verdict: **SCH-R2-01 to SCH-R2-05 are CLOSED.** Each correction is present in its record and matches the live draft text and the base code. One should-fix item remains in the round 2 response record (SS-01): its closing line says the SOLR-15358 record set is closed, which is the same overclaim this lane corrected earlier. Two stale line citations and one count are notes (SS-02 to SS-04).

Scope: `44816ee4d73` (the settlement), checked against `reports/schema-convergence-round-2.md`, `receipts/SOLR-14199.md`, `reports/schema-convergence-round-1-response.md`, `reports/schema-convergence-round-2-response.md`, the drafts `pr-drafts/schema-analysis/SOLR-14199.md` and `pr-drafts/schema-analysis/SOLR-15358.md`, and the base code at `b5c71bc5573c`. The claim is `claims/pool-recheck-schema-round2-settlement.md`. Two read-only subagents, four tasks each. Nothing was edited, posted, built or tested.

## Closed

- **SCH-R2-01 (SOLR-14199 receipt, line (a)): CLOSED.** `receipts/SOLR-14199.md` line 9 carries the replacement text word for word. The wrong wording survives only inside the correction note, labelled as wrong. The base claim holds: `SchemaField.java` throws "conflicting 'false' field options for non-indexed field" (lines 376 to 382 at `b5c71bc5573c`), and `BadIndexSchemaTest` pins the failure with the `bad_field` fixture.
- **SCH-R2-02 (round 1 response, SCH-R1-05): CLOSED.** The entry now quotes the live Choice and Limits bold leads, and the draft has neither the "proceeds as if" wording nor a version-1.5 bullet.
- **SCH-R2-03 (round 1 response, SCH-R1-14): CLOSED.** The draft says "1.6 and later" (lines 19 and 33). Base `FieldType.java` line 190 reads `schemaVersion >= 1.6f`.
- **SCH-R2-04 (round 1 response, SCH-R1-15): CLOSED.** The entry now quotes the live landing-order sentence, which is the register's form.
- **SCH-R2-05 (round 1 response, SCH-R1-37): CLOSED.** The comment sits at line 26 of `solr/core/src/test-files/solr/collection1/conf/schema-nonindexed-spatial.xml`, which the entry now names.

## Open

**SS-01 | `reports/schema-convergence-round-2-response.md` line 17 | should-fix, record overclaim.** The response says "the SOLR-15358 record set was closed by the record-close settlements". The round 2 record still lists SCH-R1-16 as open (its line 34), and the two owner holds stay open by design. The same claim was corrected earlier in the record-close report.
Replacement for that clause: "RC-S1, RC-S3 and RC-S5 are settled; SCH-R1-16 and the two owner holds on SOLR-15358 remain open."

**SS-02 | `reports/schema-convergence-round-1-response.md`, SCH-R1-14 entry (line 56) | note, stale citation.** It cites "lines 19 and 31". Draft line 31 is a heading, and the "1.6 and later" wording is at lines 19 and 33.
Replacement for "lines 19 and 31": "lines 19 and 33".

**SS-03 | `reports/schema-convergence-round-1-response.md`, SCH-R1-15 entry (line 57) | note, stale citation.** It cites "line 41". The landing-order sentence is at line 43 of the SOLR-15358 draft.
Replacement for "line 41": "line 43".

**SS-04 | `reports/schema-convergence-round-2-response.md` line 2 | note, count.** It says the register "carried five items (SCH-R2-01 to SCH-R2-05)", while line 17 also records SCH-R2-06. Replacement: "carried six items (SCH-R2-01 to SCH-R2-06)".

## Still open, outside this settlement

- **SOLR-14199 draft pass.** The owed Limits lines are not yet pasted into `pr-drafts/schema-analysis/SOLR-14199.md`. Line 47 still holds the owner placeholder. The draft stays NOT CONVERGED, as the round 2 record says.
- **SOLR-15358 holds.** The owner call on the default docValues as stored setting and landing after SOLR-15357 remain open.

## Checks that held

- The draft has no "not distinguishable" or "not rejected" wording, and no em dash or en dash.
- `solr-14199-submit` resolves to `e743c90da79a`, the head the receipt names.
- `adb0fd450c06` is a commit in this repo, and the comment at its line 26 matches the entry's text.

## Not checked

- The owner's ruling on the docValues-as-stored default.
- The gate logs on VM2.

## Convergence line

SCH-R2-01 to SCH-R2-05 are CLOSED. The round 2 response's closing sentence on the record set (SS-01) is a should-fix, and SS-02 to SS-04 are notes. The SOLR-14199 draft pass and the SOLR-15358 holds stay open. Nothing was edited, posted, built or tested in this round.
