# SCH-R1-16 in the schema lane, round 1

Verdict: **SCH-R1-16 is CLOSED.** The live draft already states the base run correctly, so no draft change is needed. One record description is wrong, and one receipt line conflicts with the proof record. Both are proposed as exact text for the main side.

Scope: `assignments/pool-schema-scr1-16-review-round-1.md`. Claim: `claims/pool-schema-scr1-16-review-round-1.md`. One read-only subagent, as the assignment names. Nothing was edited, built or tested.

## The register entry

- `reports/schema-convergence-round-1.md` line 82 (Notes): "SCH-R1-16 (SOLR-15358's base run must overlay the new test file)". Grade: note. It is listed again at line 14.
- The round 1 response (`reports/schema-convergence-round-1-response.md` line 58) describes SCH-R1-16 as confirming that "the Proof placeholder rides the posted record run". That does not match the register text. The placeholder has since closed, as `reports/schema-convergence-round-2.md` line 34 records.

## The live draft

- `pr-drafts/schema-analysis/SOLR-15358.md` line 27: the base run is "in a run recorded 2026-10-12 at the same head with only CurrencyFieldType.java reverted to the merge-base `b5c71bc5573c`", with CurrencyFieldTypeDocValuesTest running 2 tests and 2 failures.
- The base-run tree therefore keeps the new test file and the new schema fixture in the run, which is the overlay the note asks for.

## The check

- The objects `cbb345f2e7df` (head), `b5c71bc5573c` (merge-base) and `a940d43d7c9` (the proof record commit) exist locally.
- Between the base and the head, five files change: `CurrencyFieldType.java`, `CurrencyFieldTypeTest.java`, and the added `CurrencyFieldTypeDocValuesTest.java`, `schema-currency-docvalues.xml` and the changelog fragment. Reverting only `CurrencyFieldType.java` keeps the new test and fixture in the run.
- At the head, `CurrencyFieldType.java` lines 182 and 184 call the new field creation. The test uses `assumeCurrencySupport`, which exists at base in `CurrencyFieldTypeTest.java` line 60.

## Ladder rung

Information first, and the record and code answered the question. No effort or optional item was involved, so no deferral.

## Side findings for the main side (not part of SCH-R1-16)

- **Receipt conflict.** `receipts/SOLR-15358.md` line 7 says "the gate's pre-fix proof step passed at this head." The proof record in the same receipt, dated 2026-10-12, records two failures on the reverted tree. The older line describes the 2026-10-03 gate and should be marked superseded. Proposed text: "Superseded by the 2026-10-12 proof record below: the pre-fix proof leg fails as recorded there."
- **Register description.** Proposed replacement for `reports/schema-convergence-round-1-response.md` line 58, so it matches the register: "SCH-R1-16 (note): the SOLR-15358 base run must overlay the new test file. The live Proof states the base run with only `CurrencyFieldType.java` reverted, which keeps the new test in the run."

## Convergence line

SCH-R1-16 is closed. The schema lane's only open item is cleared. The two record notes above are for the main side.
