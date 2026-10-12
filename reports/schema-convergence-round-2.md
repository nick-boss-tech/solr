# Schema convergence, round 2

Verdict: **NOT CONVERGED** for SOLR-14199 and SOLR-15358. SOLR-15357 converges on its register text, and its internal hold stands, so it is not yet postable. Nothing in the branch-side list has landed.

Counts: 7 drafts or items re-checked. 8 dispositions confirmed in the live text. 4 response records disputed (SCH-R2-02 to 05). Open: the SOLR-14199 and SOLR-15358 drafts, the SOLR-15357 hold, eight branch-side rows, and the notes SCH-R1-07, 08 and 16.

Scope: `assignments/pool-schema-convergence-round-2.md`, checked against `reports/schema-convergence-round-1.md` and the main side's response. One read-only subagent did the review. Heads were checked with `git ls-remote origin refs/heads/<branch>`. Nothing was edited, posted or built.

Note on the basis: `pr-prepare` moved during the review, on an unrelated commit. The schema receipts, gates and drafts are identical at the old and new tips, so the review reads them at the current tip.

## Verdicts

- **SOLR-14199: NOT CONVERGED.** The head `e743c90da79a` matches the draft. The count confirmation, the `SchemaVersionSpecificBehaviorTest` run and the owed Limits lines are all still owed.
- **SOLR-15358: NOT CONVERGED.** The head `cbb345f2e7df` matches the draft. The Proof placeholder is unfilled, and the owner call and the land-after-SOLR-15357 hold stand.
- **SOLR-15357: converged on register text, hold stands.** Its internal hold block is still in place, so the draft is not yet postable.
- **Branch-side rows (SOLR-15945, 16977, 17047, 18134): open.** No branch commit has landed. The other heads equal the round 1 record.

## Item states

**Item 1, SOLR-14199**

- **Count confirmation: OPEN.** `receipts/SOLR-14199.md` at the tip still says 37 of 37 and names no 37th test. `gates/SOLR-14199-record.md` is OPEN and unclaimed.
- **`SchemaVersionSpecificBehaviorTest` run: OPEN.** It is not in the receipt, and the HOLD marker stands.
- **Owed Limits lines: neither landed as written.** The first, "not rejected and not distinguishable", is absent, and draft line 44 says the opposite (SCH-R2-01). The second, "no TEST_HACK exemption", is absent but supportable: the flag at `PointField.java` lines 71 to 80 only drops `precisionStep`. The `[OWNER]` marker stands.
- **Round 1:** SCH-R1-05 and 06 are confirmed, and the anchors at `e743c90` match. SCH-R1-07 and 08 remain open as notes.

**Item 2, SOLR-15357: OPEN.** The register text is converged, and the internal hold stands.

- **Branch fix and rerun: OPEN.** `CopyFieldSubFieldsTest` at `d717899b` still uses `price_c_l_pl` and `price_c_s_c` (lines 122, 127, 128, 156, 157). `gates/SOLR-15357-test-fix.md` is OPEN.
- **Changelog title: OPEN,** unchanged.
- **Owner decision 2 (the real-time get and `fl=*` ruling): OPEN,** no ruling recorded.
- **SCH-R1-09 to 12: CONFIRMED.**

**Item 3, SOLR-15358 Proof: placeholder (corrected 2026-10-12).** At round 2, line 27 still read "[TO FILL before posting ...]", `gates/SOLR-15358-proof-record.md` was OPEN and unclaimed, and the receipt said only "passed at this head". The proof record has since landed (gate DONE by vm2, 2026-10-12T09:37Z, with the proof section in the receipt), and the Proof is filled. SCH-R1-14 and 15 are confirmed in the live text (lines 19, 31 and 41), but the response's descriptions of them are wrong (SCH-R2-03 and 04). SCH-R1-16 is still open. The placeholder hold closed on 2026-10-12, when the Proof was filled from the proof record (`a940d43d7c9`). The holds that remain are the owner call on the default docValues as stored setting and landing after SOLR-15357.

**Item 4, branch-side: all OPEN.**

- SCH-R1-30 (SOLR-15945 title): the old wording is still live.
- SCH-R1-37 (SOLR-15945 comment): `schema-nonindexed-spatial.xml` line 26 still says "invariants hold".
- SCH-R1-31 (SOLR-16977 title): still ends "instead of an internal error while rendering results". Commit `1142f9563ab` added a description only.
- SCH-R1-32 (SOLR-17047 title): no quantized wording.
- SCH-R1-33 (SOLR-17047 javadoc): `DenseVectorField.java` lines 269 to 275 still say "silently ignored".
- SCH-R1-34 (SOLR-18134 title): the old wording.
- SCH-R1-35 (SOLR-18134): `major-changes-in-solr-10.adoc` line 379 and `filters.adoc` line 3884 are unchanged.
- SCH-R1-36 (SOLR-18134): `PathHierarchyTokenizerFactoryTest.java` line 111 still has the "both-sides" shorthand.

**SCH-R1-29 deviation: accepted.** The retained sentences state a limit the summary omits: on schema 1.0 the standard parser also builds phrases, and no test runs the shipped 1.7 configsets. This is consistent with the corrected rule in SCH-R1-35.

## Register

**SCH-R2-01 | SOLR-14199, should-fix.** The owed line (a) contradicts the draft and the base code. Draft line 44 says `indexed="false"` with `omitNorms="false"` already fails to load on base. The base `SchemaField.java` (the `on(falseProps, INDEXED)` block, around lines 376 to 381) throws "conflicting 'false' field options for non-indexed field", and `BadIndexSchemaTest` line 28 pins it with fixture line 27. The draft is right, and the ledger's wording is not, so the owed text must not be pasted as written. Replacement for line (a): "- The new point check skips non-indexed fields. On those fields it cannot tell an explicit omitNorms="false" from the default, and base already rejects the explicit form (see the first bullet)."

**SCH-R2-02 | response record | note.** The response (SCH-R1-05) says the Choice opens with "proceeds as if" wording and the Limits carry a version 1.5 bullet. Neither is in the live draft, which matches the register. The response record should be corrected.

**SCH-R2-03 | SOLR-15358 response record | note.** The response (SCH-R1-14) describes "schema version 1.5 or earlier ... and the base-commit anchor". The live line 31 says "1.6 and later", as the register and base `FieldType.java` line 190 (`schemaVersion >= 1.6f`) require. There is no anchor. The response record should be corrected.

**SCH-R2-04 | SOLR-15358 response record | note.** The response (SCH-R1-15) quotes "can merge and store them". The live line 41 has the register text, "could carry the derived currency values into the document it sends back". Keep the live text unless the owner decides otherwise, and correct the response.

**SCH-R2-05 | SOLR-15945 response record | note.** The response (SCH-R1-37) names `managed-schema.xml`. The comment is in `solr/core/src/test-files/solr/collection1/conf/schema-nonindexed-spatial.xml` line 26. The replacement text is unchanged.

**SCH-R2-06 | SOLR-15358, the placeholder | note.** The placeholder says "a base run at the parent commit". The proof record defines the run as the head with only `CurrencyFieldType.java` reverted to the merge-base `b5c71bc5573c4e31b4cee5a7965d73587fc0ae58`. When filled, write: "Proof tree: the head with only CurrencyFieldType.java reverted to the merge-base b5c71bc5573c4e31b4cee5a7965d73587fc0ae58."

## Not checked

- The master ledger, the takeover log and security material.
- The count of 37 in SOLR-14199's receipt beyond what the receipt states.

## Convergence line

NOT CONVERGED for SOLR-14199 and SOLR-15358. SOLR-15357 is converged on register text, and its hold stands until the branch fix and the owner's ruling land.
