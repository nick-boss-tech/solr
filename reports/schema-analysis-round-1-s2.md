# s2 report: SOLR-15357, SOLR-15358 (paired), SOLR-10403 (audit only)

Result: 15357 held (wrong test names, an overclaiming changelog title, and an owner call on the real-time get path); 15358 draftable after one fix (the base run line); 10403 audit only, receipt stale, no effect on indexed values; land 15357 before 15358.

Heads checked (claim table matches): origin/solr-15357-submit d717899b873, origin/solr-15358-submit cbb345f2e7d, origin/solr-10403-submit d8e03755cb5 (moved from 1e285cd730c; see finding 13). Branch bases: 15357 and 15358 at b5c71bc5573, 10403 at cabedd1d968 (both ancestors of upstream/main).

## Findings

1. **FIX (SOLR-15357).** `solr/core/src/test/org/apache/solr/schema/CopyFieldSubFieldsTest.java` lines 122, 127, 128, 156, 157.
   Evidence: base `FieldType.java:97` sets `POLY_FIELD_SEPARATOR = "___"`. The currency type builds its sub-field names with it (`CurrencyFieldType.java:206-219` at d717899b873). The test reads `price_c_l_pl` and `price_c_s_c`, which the currency type never writes. Those names match the dynamic fields `*_l_pl` and `*_s_c`, so the test compiles and passes, but the asserts can never fail. Both `/get` tests are vacuous for the leak they name.
   Replacement: line 122 `"id,price,price_c,price_c___l_pl,price_c___s_c"`; lines 127-128 `fetched.getFieldValue("price_c___l_pl")` and `fetched.getFieldValue("price_c___s_c")`; lines 156-157 `inputDoc.getFieldValue("price_c___l_pl")` and `inputDoc.getFieldValue("price_c___s_c")`. Rerun the focused test on the fixed head (main side).

2. **FIX (SOLR-15357).** `changelog/unreleased/SOLR-15357.yml` line 1.
   Evidence: on base and on this head, the currency sub-fields get no docValues. Base `CurrencyFieldType.java:182` and `:184` call the single value `createField`, which does not add docValues (`PointField.java:262` adds them only in `createFields`). With no docValues, real-time get has nothing to return for currency sub-fields on this branch. The title's effect is therefore not present for currency here, and it is untested for PointType and BBoxField.
   Replacement title: `The sub-fields of CurrencyFieldType, PointType and BBoxField copyField destinations are now recorded as copyField targets`

3. **FIX (SOLR-15357 receipt wording).** `receipts/SOLR-15357.md` (origin/pr-prepare), Proof line.
   Evidence: the receipt calls the behavioral tests pins "because the new test calls the FieldType.getSubFields API". Only two of the four methods call it (`CopyFieldSubFieldsTest.java:85-96` through the `subFields` helper, and `:99-110` through `assertSubFieldTargets`). The class does not compile on base because of those two, so the cause is the class. The two `/get` tests would also pass on base, for a different reason: base writes no docValues for currency sub-fields.
   Replacement: "Proof: classified, not a base-failure proof. CopyFieldSubFieldsTest does not compile on base, because two of its four methods call FieldType.getSubFields. The other two read /get output and would not fail on base either, because base writes no docValues for currency sub-fields."

4. **OWNER (pair, 15357 and 15358).** Real-time get and `fl=*` return the docValues sub-fields that 15358 writes.
   Evidence: `RealTimeGetComponent.java:356-357` (base, and unchanged at 15357) calls `decorateDocValueFields(doc, docid, docFetcher.getNonStoredDVs(true), ...)`. That set is `nonStoredDVsUsedAsStored` (`SolrDocumentFetcher.java:185-189`, `:758-759`), built from the schema with no copy target check. `FieldType.java:190` makes docValues as stored the default for schema version 1.6 and later. The 15357 change alters `isCopyFieldTarget`, not that set. A `fl=*` search reads the same set at `SolrDocumentFetcher.java:866`. So once 15358 writes the sub-field docValues, `/get` and `fl=*` return them. Not run; by code reading only.
   Replacement: none in code. Owner decisions 2 and 3 give the options. The 15358 draft Limits and the 15357 draft Limits say this in public terms.

5. **FIX (SOLR-15358 Proof).** `receipts/SOLR-15358.md`, Proof line; the draft Proof placeholder.
   Evidence: the receipt says only "the gate's pre-fix proof step passed at this head." It does not show that either new test fails on base. `research/test-queue/results/` has no SOLR-15358 file. By code reading, `testSubFieldsWriteDocValues` finds no NumericDocValuesField on base (base `CurrencyFieldType.java:182`, `:184` use `createField`), and `testSubFieldsCanBeSortedOnThroughDocValues` sorts on a field with no docValues. Neither result is recorded.
   Replacement: fill the draft placeholder from a base run: name both tests and the failing assertion or error text. Until then the Proof must not claim a base failure.

6. **NOTE (SOLR-15358).** `solr/core/src/test/org/apache/solr/schema/CurrencyFieldTypeTest.java` lines 130-132 at cbb345f2e7d.
   Evidence: `assertEquals(amountDocValues ? 4 : 3, fields.size())` replaces a fixed 3. The test schema `solr/core/src/test-files/solr/collection1/conf/schema.xml:724` declares `*_l1_ns` with no docValues, so only the 3 branch runs. The change loosens the old count without adding coverage there.
   Replacement: none required. The draft Limits says so.

7. **NOTE (SOLR-15358).** `CurrencyFieldType.java:182`, `:184`, and `FieldType.java:342-350` (base).
   Evidence: `FieldType.createFields` throws `UnsupportedOperationException` when a field has docValues and its type's `createField` returns none. The base singular call never threw. A sub-field type that cannot write docValues, with `docValues="true"` set, now fails at index time. Not tested.
   Replacement: the draft Limits line (already in the draft).

8. **NOTE (ordering).** `RealTimeGetComponent.java:879` and `DocumentBuilder.java:483` (base).
   Evidence: the materialization filter skips a field when `(!sf.hasDocValues() && !sf.stored()) || schema.isCopyFieldTarget(sf)`. Without 15357, a currency sub-field that 15358 gives docValues passes that filter, so an atomic update could carry the derived value into the input document it re-sends. 15357 is the only change that adds the copy target skip. So 15357 must land first.
   Replacement: owner decision 1.

9. **NOTE (overlap, clean).** `CurrencyFieldType.java`: 15358 changes lines 182-184; 15357 adds `getSubFields` at 206-219.
   Evidence: `git merge-tree --write-tree` is clean in both orders, tree `45503861a868cf9d788c171dd7a7e4dd1feb8ca5`. No ref was written. The only shared source file is CurrencyFieldType.java, with separate hunks.

10. **NOTE (sub-field set).** The two sub-fields 15358 writes (amount raw, currency code) are the two 15357 counts. Both come from the same suffixes through `getAmountField` and `getCurrencyField` (base `CurrencyFieldType.java:197-202`). 15357 looks them up with `getFieldOrNull` and skips missing ones, and 15358 uses `createFields` on the same fields. The sets match for any valid config.

11. **NOTE (15357 consumer change).** `ManagedIndexSchema.java:503` now refuses deletion of a field that is a sub-field of a copy destination. No test covers it.
    Replacement: the 15357 draft Change bullet (already in the draft).

12. **NOTE (15357 test name).** `CopyFieldSubFieldsTest.java:132`, `testAtomicUpdateMaterializationOmitsDerivedCurrencySubFields`. The test calls `/get` with `getInputDocument` and runs no atomic update.
    Replacement: `testGetInputDocumentOmitsDerivedCurrencySubFields`.

13. **NOTE (10403 receipt stale).** `receipts/SOLR-10403.md` line 3 (origin/pr-prepare).
    Evidence: origin/solr-10403-submit is d8e03755cb5 ("SOLR-10403: remove handoff doc"). Against base cabedd1d968 it changes 3 files (26 insertions, 11 deletions). The receipt names 1e285cd730c and says the branch still carries the handoff doc. Both are stale.
    Replacement: "Live tip: d8e03755cb5 (removes the handoff doc). No premise run and no gate exist."

14. **NOTE (10403 bearing on the pair).** `CurrencyValue.java` lines 137-150 at d8e03755cb5 (the `convertAmount` body). Only `convertAmount` changes. `getAmount()` and `getCurrencyCode()` are untouched, and they feed `CurrencyFieldType.java:182`, `:184`. Callers of `convertAmount` are query time: `CurrencyFieldType.java:289` (base, via `convertTo` in `getFieldQuery`), `:477`, `:484`, `:686` (base), `RangeFacetRequest.java:858`, and `FacetRangeProcessor.java:988`. So 10403 does not change indexed sub-field values. It can change query results.

15. **NOTE (review disagreement).** `research/branch-reviews/round-2/SOLR-15357-review.md` says "Sequencing with SOLR-15358: independent files; no conflict expected." That is right for files and wrong for behavior (findings 4 and 8). That review also ran on an older head (6a010de2ed4).

## Task results

**SOLR-15357: HELD.** The head matches the claim, and the receipt count (4 of 4) matches the four `@Test` methods (`CopyFieldSubFieldsTest.java:84`, `:98`, `:112`, `:131`). The hunks match the assignment. Findings 1 and 2 are FIX items, and owner decision 2 is open. The draft at `pr-drafts/schema-analysis/SOLR-15357.md` is written against d717899b873. It says plainly that the class shows no base failure, and it does not claim a currency real-time get effect. Once FIX 1 lands the head changes, so the Proof and links must be updated.

**SOLR-15358: DRAFTABLE after FIX 5.** The head matches the claim. The change does what the Jira asks: it switches the two sub-field calls from `createField` to `createFields`, and the sub-fields it writes are the two 15357 counts (finding 10). The draft at `pr-drafts/schema-analysis/SOLR-15358.md` is written against cbb345f2e7d. The changelog keeps the reindex note. It stays on hold for the base run and owner decision 3.

**SOLR-10403: AUDIT ONLY, NO GATE.** The live head moved to d8e03755cb5 (finding 13). The receipt is stale. No premise run or gate exists. On the pair: no effect on indexed values (finding 14). It shares `CurrencyFieldTypeTest.java` with 15358. The hunks sit three lines apart and merge cleanly (`git merge-tree` tree `c1f4210c867fa1a004d161e154eabb0089efdbc4`). No draft.

## Interactions

- 15357 and 15358: clean trial merge in both orders, no shared hunks in CurrencyFieldType.java (finding 9).
- Counted sub-fields and docValues sub-fields: the same two fields (finding 10).
- Landing order: 15357 first (finding 8). Even with both landed, `/get` and `fl=*` still return the docValues sub-fields (finding 4).
- 9349 and 14199 interactions belong to s1. Not checked here.

## Owner decisions

1. Land 15357 before 15358. The atomic update path needs 15357's copy target skip (finding 8). Confirm.
2. Real-time get and `fl=*` decoration of copy target docValues (findings 4). Options: (a) widen 15357 to skip copy targets at `RealTimeGetComponent.java:356-357` and `SolrDocumentFetcher.java:866`, with a combined test; this also changes direct docValues copy targets, which are returned today; (b) keep 15357 narrow, list this in Limits, and offer a follow-up. Either way, a combined 15357 plus 15358 run is needed.
3. Default for docValues as stored on the 15358 sub-fields. Options: (a) keep the default, so `fl=*` and `/get` return them; (b) turn it off for the sub-field types by configuration.
4. Scope of 15357. Options: (a) keep the shared bookkeeping (current), which also changes the delete check (finding 11); (b) skip sub-fields only in the two read paths.
5. 10403: a premise run and gate at d8e03755cb5, and whether the ticket's ask (precision at the end of the calculation, not on the intermediate result) is met by the rounding change. Outside this audit.

## Not checked

- No build, test, Gradle, or gh call, per the claim rules. The `/get` and `fl=*` behavior in findings 4 and 8 comes from reading code only. A combined 15357 plus 15358 run would settle it.
- Gate logs named in the receipts (g15357r35-gate-fix.log, g15357-harden3.log, g15358-harden.log) are not in `research/` under those names. `research/test-queue/results/` has no file for 15357, 15358, or 10403. GitHub run 37690315325 was not checked (no gh).
- Heads: read from `git ls-remote --heads origin` and local remote-tracking refs. No fetch was run.
- Jira text: local packets only (`research/jira-context/SOLR-15357.json`, `SOLR-15358.json`, `SOLR-10403.json`). No live Jira read. The 10403 packet is from 2017 and has no plan.
- PointType and BBoxField: whether their sub-fields are stored or carry docValues, and how the test schema's `loc` sub-fields are created. Not traced.
- 10403: whether its ask is met, and whether existing currency tests pin truncated values.
- Lucene 9.x and 10.x: neither draft names Lucene behavior, so no version check was needed.
- The three changelog YAML files were not parsed.
- The test-count claims (4 of 4, 2 of 2, 42 with 21 skipped) are from receipts and were not re-run.
