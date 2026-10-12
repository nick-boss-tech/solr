# Schema re-check, SOLR-14199 landing round 1: settlement

Date: 2026-10-12. Scope: reports/schema-recheck-14199-landing-round-1.md and its three draft corrections (commit 63f2c583cf), checked against receipts/SOLR-14199.md and the branch at head 2c549d9fb384363f741ce62206c37f518744153f, base b5c71bc5573. Two of the three corrections are reversed as unsupported removals; one stands. The draft restorations are committed with this record.

## Line 26: restored

Removed text: the clause "because base loads all three fixtures without error" was replaced by "and it is the only one of the 26 that does".

Evidence:

- The test at the head (BadIndexSchemaTest, lines 35 to 40) runs three fixtures through doTest: bad-schema-pointfield-omitnorms-ft.xml, bad-schema-pointfield-omitnorms-field.xml and bad-schema-pointfield-omitnorms-dynamicfield.xml, each expecting a load error naming omitNorms=false.
- The base run recorded in the receipt (log g14199-premise.log, seed 84C0203974783C5B) failed this test with "Did not encounter any exception from: solrconfig-basic.xml using bad-schema-pointfield-omitnorms-ft.xml", and the log shows that schema loaded and the core created. The first fixture loads without error on base, on the record.
- On base, no other path rejects the remaining two fixtures. PointField has no checkSchemaField override on base and its init throws nothing on omitNorms=false; FieldType.checkSchemaField checks doc values and large fields only; the SchemaField "conflicting 'false' field options" throw fires only for non-indexed fields, and the field and dynamic field fixtures both declare indexed="true". The two rejections the draft describes (PointField.init and PointField.checkSchemaField) are the branch's additions, absent on base.

Verdict: the clause is supported by the code and by the recorded base run. The original sentence is restored. The replacement clause was also accurate (the receipt records exactly 1 of 26 failing on base), but the original wording gives the reason, and the reason checks out.

## Line 32: restored

Removed text: "The numeric point type is randomized in tests, so the run covers one of the point or trie configurations."

Evidence:

- TestSolrQueryParser extends SolrTestCaseJ4 and loads schema12.xml. In schema12.xml the numeric field types are declared with class="${solr.tests.IntegerFieldType}" and the matching properties for the other numeric kinds.
- SolrTestCaseJ4.randomizeNumericTypesProperties sets those properties once per test class: point field classes at 80 percent likelihood, trie field classes otherwise. One run of the class uses one of the two configurations.

Verdict: the sentence is a code fact at the head. The receipt does not mention it, but the removal standard is support in the receipt or the code, and the code establishes it. The sentence is restored verbatim.

## Line 48: removal stands

Removed text: "No declaration in the tree combines a randomized point type with `omitNorms`, and the randomized suites pass".

Evidence:

- A fresh tree search at the head finds no declaration combining a randomized field type property with omitNorms="false", and every omitNorms="false" declaration outside the three bad-schema fixtures sits on string, text or boolean types (schema-behavior.xml; the *_b_norms and *_s_norms dynamic fields in schema12.xml). Read as omitNorms="false", the first clause holds.
- As written, the clause says `omitNorms` without a value, and the shipped test schemas declare randomized numeric types with omitNorms="true" throughout (schema12.xml, lines 36 to 44). The sentence as written does not hold.
- The second clause, "the randomized suites pass", has no record behind it. The receipt records four focused classes at the head (26, 2, 1 and 37 tests) and the two record runs at the prior head, not suite-wide randomized runs.

Verdict: the sentence as written was not established. The removal stands, and the bullet keeps its post-correction form.

## Spot checks of the re-check's positive verdicts

- Head citations: every blob link in the draft uses 2c549d9fb384363f741ce62206c37f518744153f, the changelog line included, and the prior head e743c90da79 does not appear. Confirmed.
- Proof counts: BadIndexSchemaTest 26 of 26, PrimitiveFieldTypeTest 2 of 2, SchemaVersionSpecificBehaviorTest 1 of 1, TestSolrQueryParser 37 of 37. All match the receipt's top section. Confirmed.
- Owed Limits line (a): present verbatim, "The new point check skips non-indexed fields. On those fields it cannot tell an explicit omitNorms="false" from the default, and base already rejects the explicit form (see the first bullet)." Confirmed.
- Owed Limits line (b): present in the final bullet's opening sentence, "The checks do not exempt `TEST_HACK_IGNORE_USELESS_TRIEFIELD_ARGS`." Confirmed, in the form left by the upheld line 48 correction.

## Closing state

With the two restorations, the draft matches the receipt and the code at head 2c549d9fb384 on heads, anchors, counts, dates, placeholders and Limits. Against the draft as the fix lane left it (f9057e454cc), the only surviving re-check change is the line 48 removal. The SOLR-14199 draft stands settled: the prescribed fix is gated green at the live tip, both owed Limits lines are in, and no hold items remain.
