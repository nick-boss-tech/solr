# Schema re-check, SOLR-15358 Proof after the main-side fill, round 1

Verdict: **NOT CONVERGED on the Proof wording.** The filled Proof matches the 2026-10-12 proof record in every number, test name, message and date. Two should-fix items remain in the Proof paragraph (F1 and F2 below). The draft also stays on HOLD for the two items the internal header names: the docValues-as-stored owner call and landing after SOLR-15357.

Scope: `a940d43d7c9` (the fill), checked against `receipts/SOLR-15358.md` (section "Proof record (vm2, 2026-10-12)") and the branch `solr-15358-submit` at `cbb345f2e7df452e20036d357c82cc63db96ffd3` (ls-remote confirmed; merge-base with upstream/main is `b5c71bc5573c`). The claim is `claims/pool-schema-recheck-15358-proof.md`. Two read-only subagents, four tasks each. Nothing was edited, posted, built or tested.

## Checks that held

- **The change is limited to two lines.** The internal header (line 1) and the Proof line (line 27) changed, and nothing else did.
- **Numbers and names match the receipt.** The date is 2026-10-12. The tree is the head with only `CurrencyFieldType.java` reverted to `b5c71bc557`. CurrencyFieldTypeDocValuesTest ran 2 tests with 2 failures. The two test names are the methods at lines 57 and 74 of the new test file. Both quoted messages are exact substrings of the receipt.
- **"Both failures are the missing sub-field docValues this change adds" holds.** `CurrencyFieldTypeDocValuesTest.java` is a new file on the branch, and `f5fc8953e7` is the only branch commit that touches `CurrencyFieldType.java`.
- **The partial revert is named.** Line 27 says the run reverted only `CurrencyFieldType.java`, so the fail-before claim is stated on the right tree.
- **Links and head.** The head SHA appears in full in the links. The base is named by a 12-character prefix of `b5c71bc5573c4e31b4cee5a7965d73587fc0ae58`, which resolves to that object.
- **No process words, dashes or PR numbers** in the outbound Proof line.

## Findings

**SCH-R3-01 | line 27 | should-fix.** "CurrencyFieldTypeTest passed on that tree as well (42 tests, 21 skipped, 0 failures)." The receipt records 0 failures and 21 skipped. It never records a pass. The 21 skips are the suite's currency-locale assumption, so they did not run. "Passed" overstates the outcome.
Replacement: "CurrencyFieldTypeTest reported no failures on that tree as well (42 tests, 21 skipped by its currency locale assumption, 0 failures)."

**SCH-R3-02 | line 27 | should-fix, shape.** `pr-formula.md` section 3 asks for one line per test class, with counts inline and the verification date and head. Line 27 puts both classes in one paragraph, and the second class has no date or head.
Replacement, as two paragraphs. Paragraph 1 is the current sentence up to "Both failures are the missing sub-field docValues this change adds." Paragraph 2:
"CurrencyFieldTypeTest reported no failures on that tree as well (42 tests, 21 skipped by its currency locale assumption, 0 failures), in the same run at head `cbb345f2e7df452e20036d357c82cc63db96ffd3`, recorded 2026-10-12."

**SCH-R3-03 | internal header, line 1 | note, internal only.** "(report, finding 8)" is ambiguous. Two schema-analysis reports cite a landing-order finding 8. The header is removed before posting, so this does not reach the outbound text. Optional: "(schema-analysis-round-1-s2.md, finding 8)".

**SCH-R3-04 | internal header, line 1 | note, internal only.** The header's own instruction says to remove the block before posting. The placeholder hold it used to carry is closed, so the block now lists only two holds.

**SCH-R3-05 | `reports/schema-convergence-round-2.md` line 34 | note, record correction.** That line still reads "the placeholder" as an open hold and says `gates/SOLR-15358-proof-record.md` is OPEN and unclaimed. The gate file now reads DONE by vm2, 2026-10-12T09:37Z, and the receipt holds the proof section. Optional record wording: "The placeholder hold closed when the proof record landed on 2026-10-12 and the Proof was filled from it (`a940d43d7c9`)."

## Holds that stand

- The docValues-as-stored default is an owner call and stays in the choice section. The draft does not close on it.
- Landing after SOLR-15357 stays open until that branch lands.

## Not checked

- The gate log `15358-proof.log` on VM2. The Proof cites the receipt, which is the named source.
- The owner ruling on docValues-as-stored, which is not in this round's inputs.

## Convergence line

NOT CONVERGED on the Proof wording. SCH-R3-01 and SCH-R3-02 are should-fix items for the main side to apply. The draft also waits on the two holds named in its header. Nothing was edited, posted, built or tested.
