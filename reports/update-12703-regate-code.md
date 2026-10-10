# SOLR-12703 draft: code citation check at bdd29ba19900

Read only. No builds, Gradle, tests, edits, or public writes.

- Draft: `pr-drafts/update-processing/SOLR-12703.md`.
- Branch: `origin/solr-12703-submit` fetched; head `bdd29ba19900628e13c311adc72a00b3eebcc8ca`.
- Base `97d973814336101e12475558d7419321c743de79` is an ancestor of the head.
- Head diff against base: `AtomicUpdateDocumentMerger.java` +26/-0, `AtomicUpdatesTest.java` +30, changelog +8. The merger change is additive only.

## Links

1. doSet, head L479-L482: ok. The body is `toDoc.setField(name, getNativeFieldValue(name, fieldVal))`. The same body is unchanged at base L465-L468, so the pre-change sentence holds. For a Map operand, base `FieldType.toNativeType` (FieldType.java L1453-L1458) and the numeric, date and bool overrides return the Map unchanged, so it is stored as the field value.
2. check, head L162-L175: ok. The range is exactly the 14 inserted lines. They sit inside the entry loop, before `switch (key)` at L176.
3. helper, head L710-L720: ok. `ATOMIC_OPERATIONS` lists the six operations that match the base switch cases. Optional wording: the helper also returns false for `SolrDocumentBase` (L715), which the sentence omits. No change required.
4. test, head L1547-L1575: ok. The range runs from `@Test` through the closing brace of `testNestedAtomicOperationIsRejected`.
5. commit `ed95d555e62`: exists. By reading, the head test would fail there. The ed95 message is "is itself an atomic update operation map: " plus the operand, which lacks "with operation(s): [set]" and echoes "bbb". The ed95 version of the test passes on ed95, so "the updated test" means the head test. Not re-run.
6. SOLR-6045 loop, `bcae04d77bdb` L202: ok. `atomicOperations(sif)` builds the flattened list (L127).

## Other checks

- Pre-change claim against base: the base switch (L162-L181) sends `set` to `doSet` with no nested-operation check. Holds.
- Post-change claim: the in-place path also reaches the check, because `doInPlaceUpdateMerge` calls `mergeDocHavingSameId` at head L464. The "rejects" sentence holds on both paths.
- Changelog at head reads "instead of being stored as a field value." It has no RunUpdateProcessor wording, so the round 2 item is resolved at this head.

## Replacements

None required.

SOLR-12703 code citations: clean
