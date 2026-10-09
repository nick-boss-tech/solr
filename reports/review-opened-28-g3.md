# Review of the opened update-processing PRs, group 3

Group 3 of 7 (SOLR-6045 #5077, SOLR-7504 #5081, SOLR-12703 #5085, SOLR-12705 #5096), read-only at pr-prepare `20f84d1b293`. PR heads and bodies were read with `gh pr view`, branches with `git show` and `git merge-file` on scratch copies, and receipts from `origin/pr-prepare`. No builds, no tests.

Title rule, all four: each PR title differs from the changelog title of its branch. The fix is listed once per PR as item C (or E/F) with the exact replacement. The follow-up rule in assignment item 3 is unmet in all four Limits sections, listed per PR.

## SOLR-6045 (PR 5077)

**Verdict: FIX FIRST** (owner call also needed, item B1 below)

Findings:

- **A. Proof omits two changed tests.** The branch adds `testRepeatedOperationMapsPassThroughUnwrapped` (L241) and `testPlainMultiValueFieldStillWrapped` (L271) to `solr/core/src/test/org/apache/solr/update/processor/AtomicUpdateProcessorFactoryTest.java` at `bcae04d77bdb`. The receipt's focused run names only `AtomicUpdatesTest` (32 tests), so these two tests are not gated, and the Proof does not mention them. Corrected: add after the AtomicUpdatesTest bullet: "`AtomicUpdateProcessorFactoryTest` has two new tests, `testRepeatedOperationMapsPassThroughUnwrapped` and `testPlainMultiValueFieldStillWrapped`. They are not in the gated run at this head." Better, if the owner approves a verify run: gate that class and cite its counts.
- **B. Limits has no follow-up plan** (assignment item 3). The four Limits bullets name gaps without a plan. Corrected: add as the last bullet: "A follow-up pull request can add JSON-array and XML-writer tests if maintainers want them." No Jira ticket is promised.
- **C. Title.** Current PR title: "SOLR-6045: atomic updates w/ solrj + BinaryRequestWriter aren't working when adding multiple fields w/ same name in a single SolrInputDocument". Changelog title (`changelog/unreleased/SOLR-6045-atomic-update-repeated-field.yml`, line 2, 361 characters). Corrected: "SOLR-6045: " followed by that line, verbatim, about 370 characters. If the owner wants short titles, the changelog title must be shortened first, which moves the head and needs a re-gate.
- **Owner call B1.** The bold summary of "What this change does" and the changelog both say a field that mixes plain values with operation maps "is rejected, whichever value comes first". That holds on the merger path (`AtomicUpdateDocumentMerger.java` L88-L137 at head). The factory path does not reject it. For a mixed list, `isAlreadyAtomicUpdate` returns false (`AtomicUpdateProcessorFactory.java` L161-L177), so the factory wraps the whole list as one set operation (L139), and the mixed-field check never sees the plain value. Options: (a) reject mixed fields in the factory too, a branch change with a re-gate, which fits the "one rule" in R2; (b) scope the rule to the merger path in the summary, Limits and changelog.
- **Non-blocking, test comment.** `AtomicUpdatesTest.java` L1373-L1374 says "the merger already rejected this order". That is false on base, where the field is not treated as atomic and the first assertion fails. Optional replacement: "// The same mixed field with the operation map first. Base does not treat this field as atomic."
- **Length.** The body is 6,185 characters, above the roughly 3,500 guide. It is a multi-part change, so this is noted, not counted.

Checks that passed: head `bcae04d77bdbb33c33e077c3945a5deca0ce7dc1` matches the receipt; headRefName `solr-6045-submit`; baseRefName `main`; body byte-identical to the draft; every claim in both sections matches the head code (L88-L96, L100, L109-L113, L122-L141, L130-L137, L139, L161-L177, L201-L202, L295, L309, base L88-L97, L157-L158, L186); all 16 blob links resolve at their cited SHAs with anchors on the stated code; proof counts 32, 0 failures, 1 skip and base 6 failures match the receipt; R2 merger path rejects both orders; the sequencing bullet holds (see SOLR-12703); AI header and footer present; sections bold and in order; no em dash; no first-person plural; diff is five files (four Java files and one changelog), no workspace files; changelog YAML valid.

## SOLR-7504 (PR 5081)

**Verdict: FIX FIRST**

Findings:

- **A. Code regression: a counted field with a null value throws NullPointerException.** `solr/core/src/java/org/apache/solr/update/processor/CountFieldValuesUpdateProcessorFactory.java` L78 (`for (Object value : src.getValues())`) at `22b77196e662`. `SolrInputField.getValues()` returns null for a null value (solrj `SolrInputField.java` L124-L134). A JSON null reaches the chain as a null-valued field (`JsonLoader.java` L596-L600 at base, and `SolrInputDocument.setField` keeps it). The base counter stored 0, because `getValueCount()` is 0 for null. At head the loop throws, and `FieldMutatingUpdateProcessor.processAdd` (base L91-L92) catches only SolrException, so the request fails. Current (L77-L82):
  ```
            SolrInputField result = new SolrInputField(src.getName());
            for (Object value : src.getValues()) {
              if (value instanceof Map && !(value instanceof SolrDocumentBase)) {
                return countAtomicUpdate(src, result);
              }
            }
  ```
  Corrected:
  ```
            SolrInputField result = new SolrInputField(src.getName());
            Collection<Object> values = src.getValues();
            if (values != null) {
              for (Object value : values) {
                if (value instanceof Map && !(value instanceof SolrDocumentBase)) {
                  return countAtomicUpdate(src, result);
                }
              }
            }
  ```
  This is a branch change: new head, re-gate. `java.util.Collection` is already imported.
- **B. Description, "What this change does", bullet 5.** Current: "An operation map with no operation is rejected with BAD_REQUEST." The code (L123-L129) rejects only a field with no `set` in any of its maps, so `[{}, {"set": 2}]` is accepted. Corrected: "A field with no operation at all, such as one empty operation map, is rejected with BAD_REQUEST."
- **C. Description, "What this change does", bullet 3.** Current last sentence: "This is the one behavior change for existing updates." Corrected: delete it. A plain value mixed with an operation map is also rejected now, and base counted it.
- **D. Description, Limits, bullet 5 (cross-PR).** Current: "SOLR-12705 changes the field mutating base class, and the two branches merge without conflicts at their heads." This is not true. A textual merge at the two heads (base `0cc328310f8f`) conflicts once, in the import block of `FieldMutatingUpdateProcessorTest.java`, because both branches add imports before `LinkedHashSet`. The reverse order conflicts the same way. The counter source merges cleanly. Corrected: "SOLR-12705 changes the field mutating base class. At their heads the two branches conflict in one import block of FieldMutatingUpdateProcessorTest.java, so the second one to land needs that block merged by hand."
- **E. Proof, cosmetic.** The receipt records the observed failure. Current: "fails on the factory before the fix." Corrected: "fails on the factory before the fix (pre-fix head e3fdc8eb58f2, with 'no error from the count chain assertion')."
- **F. Limits has no follow-up plan** (assignment item 3). Corrected: add as the last bullet: "A follow-up pull request can add an end-to-end atomic update test if maintainers want it."
- **G. Title.** Current: "SOLR-7504: Atomic Update causes solr.CountFieldValuesUpdateProcessorFactory to be wrong". Corrected: "SOLR-7504: " followed by changelog line 2 verbatim (see SOLR-6045 item C).

Owner call: none.

Checks that passed: head `22b77196e662e93aca6ddc926c6738fb69c817fb` matches the receipt; base main; body byte-identical to the draft; R1 holds (the 7504 rule governs, and SOLR-12705 claims no counted-field outcome); R2 holds on the 7504 side (plain value rejected in either order, L78-L82, test L852-L865, and the Limits claim about the 6045 merger matches the 6045 diff); landing order (7504 before 12705) is consistent; receipt counts 29/29 and 42/42, date and head match; all five file links resolve with anchors on the stated code (L76-L85, L78-L82, L88-L132, L134-L144, test L852-L865); diff is the changelog, the counter source and one test file; changelog YAML valid; no em dash; no first-person plural.

## SOLR-12703 (PR 5085)

**Verdict: FIX FIRST**

Findings:

- **A. Limits has no bold one-line summary** (presentation rule, formula item 3). Current: "## Limits" followed directly by "- The test calls the merger directly." Corrected: insert after "## Limits" and a blank line: "**The merger test is direct, and the sequencing with SOLR-6045 is not tested.**"
- **B. Stale cross-PR link.** Limits, sequencing bullet: "[loop](https://github.com/nick-boss-tech/solr/blob/e4b77fa7ae53d363e8869e1073a800c324fd6972/solr/core/src/java/org/apache/solr/update/processor/AtomicUpdateDocumentMerger.java#L180)" points to SOLR-6045's superseded head. Corrected: "https://github.com/nick-boss-tech/solr/blob/bcae04d77bdbb33c33e077c3945a5deca0ce7dc1/solr/core/src/java/org/apache/solr/update/processor/AtomicUpdateDocumentMerger.java#L202". The claim holds at `bcae04d77bdb` (L202 is the loop over `atomicOperations(sif)`).
- **C. Proof, date missing.** Current: "Verified at this head." Corrected: "Verified 2026-10-09 at this head (`63c84919c80b`)." The receipt records that date.
- **D. Changelog title (branch content).** Current: "...is rejected with a clear 400 error instead of surfacing as a confusing RunUpdateProcessor failure." The description says base stored the operand (`doSet`, L479-L482), and neither the description nor the test shows a RunUpdateProcessor failure on base. Corrected ending: "...is rejected with a clear 400 error instead of being stored as a field value." This moves the head and needs a re-gate.
- **E. Limits has no follow-up plan** (assignment item 3). Corrected: add as the last bullet: "A follow-up pull request can add a SolrJ request-path test if maintainers want it."
- **F. Title.** Current: "SOLR-12703: Better validation of bad atomic updates". Corrected: "SOLR-12703: " followed by changelog line 2 verbatim (see SOLR-6045 item C).

Cross-PR check, asked for by the lead. Limits, bullet 2 says: "The two merge without conflicts in either order, but their combined behavior has not been run. The two should be sequenced, not merged cold." It still holds at both heads. A textual merge of `AtomicUpdateDocumentMerger.java` and `AtomicUpdatesTest.java` is clean in both orders against base `97d973814336`. The merged loop reads correctly: the nested check runs on each operation of the flattened list. No combined run appears in either receipt. The SOLR-6045 sequencing bullet holds too.

Owner call: none.

Checks that passed: head `63c84919c80b15c20d5b93bbacaf3f21835648e5` matches the receipt; base main; body byte-identical to the draft; the nested check (L162-L175), `doSet` (L479-L482), the helper (L710-L720) and the test (L1547-L1575) resolve and match the claims; the receipt count (27 tests, 1 skip) matches; `ed95d555e62` is an ancestor of the head; AI header and footer present; diff is two source or test files and one changelog; changelog YAML valid; no em dash; no first-person plural.

## SOLR-12705 (PR 5096)

**Verdict: FIX FIRST** (owner call also needed, item E)

Findings:

- **A. Proof has no fail-before result.** The receipt records pass counts only (25 and 43 tests, 0 failures), with no base run. The formula requires a base failure with its counts, or a plain statement that the proof is inconclusive by construction. Corrected: replace the paragraph after the Proof bullets with: "No fail-before run is recorded for this head. `testAtomicOperandsOptOut` uses the new `mutator` overload, so it is inconclusive by construction. `testParseDateInAtomicUpdateOperations` has not been run against the base." If the owner approves a verify run, a fail-before for `testParseDateInAtomicUpdateOperations` can replace that sentence.
- **B. Landing-order bullet omits a second conflict.** Limits names only the conflict with SOLR-16655. A textual merge with 16655 at `5e2317443f41` conflicts twice in `FieldMutatingUpdateProcessor.java`, so that claim is right. A textual merge with SOLR-7504 at `22b77196e662` also conflicts, once, in the import block of `FieldMutatingUpdateProcessorTest.java`. Corrected: after "The two conflict in `FieldMutatingUpdateProcessor.java`." add "SOLR-7504 also conflicts with this change in one import block of `FieldMutatingUpdateProcessorTest.java`."
- **C. Wording, cosmetic.** Current: "SOLR-16655 is confined to the child-document descent in `mutateDocument`." The 16655 diff also moves the field loop into a new `mutateDocument` method and adds an identity set so no document is mutated twice. Corrected: "SOLR-16655 changes only `processAdd` and `mutateDocument` in this file. It moves the field loop into `mutateDocument`, adds the child-document descent, and mutates each document once."
- **D. Limits has no follow-up plan** (assignment item 3). Corrected: add as the last bullet: "A follow-up pull request can add the add-distinct, remove and dropped-operand cases if maintainers want them."
- **E. Owner call: default scope.** Should atomic operands be mutated by default for every field-mutating processor (as written, with a per-class opt-out), or default off with only ParseDateFieldUpdateProcessorFactory opting in? `mutateAtomicOperands()` defaults to true (`FieldMutatingUpdateProcessor.java` L84-L86), so the trim, regex replace, remove-blank, truncate and parse processors all change atomic operands. By reading the code, an atomic `set` of an empty string through remove-blank would be dropped, and a `set` through trim would be trimmed; base left both alone. Bullet 2 states the width but does not offer the narrower route.
- **F. Title.** Current: "SOLR-12705: ParseDateFieldUpdateProcessorFactory does not work for atomic update values". Corrected: "SOLR-12705: " followed by changelog line 2 verbatim (see SOLR-6045 item C).

Cross-PR check, asked for by the lead. The description claims no counted-field outcome. `testAtomicOperandsOptOut` (L810-L855) is the only test in the diff near the counter, and it uses a custom function, not the counter. "Counted fields follow the rule in SOLR-7504, which lands first" matches the Count factory opt-out (L67-L75) and the landing order. SOLR-7504 and SOLR-16655 are both stated to land before this change, consistent with the rulings. The combined-run numbers (30 of 30, 43 of 43, 2026-10-09) match the receipt.

Checks that passed: head `aa56b7b1be4c5491723c58f296e20c906d7b7334` matches the receipt; base main; body byte-identical to the draft; receipt counts 25 and 43 match; all 11 file links resolve with anchors on the stated code (`ParseDateFieldUpdateProcessorFactory.java` L113-L120; `FieldMutatingUpdateProcessor.java` L74-L86, L109-L111, L118-L120, L151-L182, L176-L177, L358-L374; `CountFieldValuesUpdateProcessorFactory.java` L67-L75; the test and ParsingField anchors); diff is five source or test files and one changelog; changelog YAML valid; formula sections bold and in order; no em dash; no first-person plural.
