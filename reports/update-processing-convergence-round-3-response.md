# Update processing convergence, round 3: main-side response

Round 3 returned NOT CONVERGED: 6 of the 16 re-checked drafts converged, 10 did not, with one blocking item (the SOLR-6973 behavior statement) and 23 should-fix replacements. When this lane started, the open review fixes lane (reports/review-open-fixes-lane-round-1.md) had already applied 18 of the 23 replacements and had excluded five, plus the blocking item, as main-side work. This response verifies the 18 against the receipts and the live draft text, applies the remaining five and the blocking item, and dispositions the round 3 record items. Application method in this lane: exact-string replacements, each asserted to occur exactly once before applying; the full diff was then reviewed file by file.

## Counts

| Grade | Items | Already applied by the open fixes lane, verified here | Applied in this lane | Not applied |
|---|---|---|---|---|
| Blocking | 1 (SOLR-6973 line 17) | 0 | 1 | 0 |
| Should-fix | 23 | 18 | 5 | 0 |

## The blocking item: SOLR-6973, the behavior statement

The round 3 report calls this an owner-confirm item. Under Nick's decision ladder, a statement of behavior follows the code evidence, so the main side verified the reading itself instead of asking for a ruling. The reading holds on every point:

- On the merge-base `cabedd1d968`, `SignatureUpdateProcessorFactory.processAdd` throws in its loop (lines 169 to 178) only when a signature field is present in the partial update. With none present, it computes the empty signature and writes it unconditionally (line 200, `doc.addField(signatureField, sigString)`), and with `overwriteDupes` it sets `cmd.updateTerm` to that signature (lines 202 to 204).
- The write reaches existing documents as well as new ones. `DistributedUpdateProcessor.versionAdd` calls `getUpdatedDocument` (line 394; the method at line 723, gated on `isAtomicUpdate` at line 724). The merged document is built at line 778 (no stored document: a new document) or line 782 (stored document found), and the partial document's `signatureField` is a normal field in that merge: `AtomicUpdateDocumentMerger` line 195 states that normal fields are treated as a "set", and line 196 puts the update's field into the merged document, replacing the stored value. So on the merge-base an existing document also receives the empty signature in `signatureField`, and `updateTerm` deletes other documents carrying that term.
- At the branch head `51fcd05e695`, a partial update whose document holds none of the signature fields passes to the next processor and returns (lines 165 to 179; the pass-through call at line 177 and the return at line 178; the round 3 report cites 174 to 177, the same block). The skip does not depend on whether the target document exists, so it covers both cases the merge-base mishandled.

The statement at line 17 now reads: "Behavior change: a partial update with no signature fields no longer gets an empty signature. This covers new documents and existing ones. A new document is stored without `signatureField`. An existing document keeps its stored `signatureField`, and the update no longer deletes other documents that share the empty signature."

Scope check, as required: the corrected statement does not widen the PR's claimed scope beyond its title. The branch changelog title already claims the general behavior ("SignatureUpdateProcessorFactory no longer adds an empty signature to (or deletes duplicates for) a partial update that contains none of the signature fields"), with no new-document restriction, and the draft's What section already describes the deletion of unrelated documents. No scope flag.

## Fact checks before applying

- **SOLR-16655 overlay (line 25 replacement):** confirmed from this host's records. The gate log g16655-gating-gate.log step 3 records the pre-fix proof against pre-fix head `b201a57fb3e5d9f9d9a60b0058b4ea13e30e8733`, and g16655-gating-prefix.log shows `testTrimFieldsRecurseIntoNestedDocumentsOnlyWhenParentSelected` FAILED at line 136 of the test file. That test does not exist in the tree at `b201a57fb3e5` (it was added by the last commit `5e2317443f41`, which changes both the processor and the test file), so the proof run necessarily used the pre-fix production head with the branch's test file applied. The replacement is applied, and receipts/SOLR-16655.md now states the overlay.
- **SOLR-5941 new test identity (line 41 replacement):** confirmed from the fix gate log. g5941-fix-gate.log step 3 records the pre-fix proof against `62516cc338ef42f87e98989b2da02796375f2558`, and g5941-fix-prefix.log shows the failing test is `CommitThroughNonLeaderTest.testCommitSentToNonLeaderIsSeenByAllReplicas`. The test file is absent at `62516cc338ef` and was added by the fix commit `c82045e838`, so the proof run added the branch's file to the pre-fix head, as the replacement states.
- **SOLR-12705 combined run (line 28 replacement):** the replacement names only the SOLR-7504 head, and that much the record supports (receipts/SOLR-12705.md and receipts/SOLR-7504.md both record the combined-tree verification of 2026-10-09 with SOLR-7504 at `22b77196e662`, both suites 30 of 30 and 43 of 43). Which SOLR-12705 head the combined tree used is not named in any surviving record this lane checked (both receipts, the receipts ledger, and the gate and settling logs on this host), and the replacement does not claim it. The round 2 response's sentence attributing `22b77196e662` to SOLR-12705 is corrected in that file (see Record items).
- **Replacements already applied by the open fixes lane:** each was verified against its receipt before being counted here. SOLR-14718 (receipts/SOLR-14718.md: head `29c09959791a`, SolrCmdDistributorTest 1 of 1, 2026-10-07). SOLR-16673 (receipts/SOLR-16673.md: the gate at `d5c19e64ba1b` records tidy, Error Prone compile and module check, with no changelog parse at that head; the proof is recorded as PASS only, matching both applied sentences). SOLR-6973 line 23 (receipts/SOLR-6973.md: gate 2026-10-08 at `fa5b59ba07b4`, 7 tests with 1 failure on base production). SOLR-3657 (receipts/SOLR-3657.md: 17 tests, exactly 2 failures at the destination-prefix assertions, 2026-10-07; the merge-base `97d973814336` per the round 2 verification). SOLR-5065 (receipts/SOLR-5065.md: the pre-fix failure on base `0cc328310f8`; the applied text uses the 12-character form `0cc328310f8f` per the round 3 instruction, and the test class is absent at that base, verified against the tree). SOLR-7022 (the round 2 response's recorded premise run: base `97d973814336` with the branch's commit-level test added, 1 of 1 failing at the interrupt-restored assertion, 2026-10-06). SOLR-7504 (receipts/SOLR-7504.md: pre-fix proof at `22b77196e662`, exactly the new test failing, 1 in 30; the combined verification with SOLR-12705 the same day). No applied replacement contradicts its receipt.

## Per-finding dispositions

### SOLR-6973
- Blocking, line 17: applied in this lane (verified above; the roll-up text is used unchanged).
- Bold line 7, line 9 link, line 23, line 25 deletion, Limits line 37: applied by the open fixes lane; verified against the receipt and the code citations.

### SOLR-12705
- Line 17 ("Two" to "Three"): applied by the open fixes lane; verified.
- Line 28 (combined tree): applied in this lane, exactly as specified (fact check above).
- Restated pointer, What and Limits: applied in this lane. The main side agrees the pointer belongs in What: the CountFieldValues bullet there already carries "Counted fields follow the rule in SOLR-7504, which lands first", so the Limits bullet now ends at "with an atomic update."

### SOLR-7022
- Line 26 last sentence and line 24 ("tidy" to "the formatting check"): applied by the open fixes lane; verified against the recorded premise run.

### SOLR-14718
- Line 24: applied by the open fixes lane; verified against the receipt.

### SOLR-16673
- Line 23, both items (the parse claim deleted from the earlier-head sentence; the fails-without-the-fix replacement): applied by the open fixes lane; verified against the receipt.

### SOLR-16655
- Line 25: applied in this lane (fact check above). Receipts/SOLR-16655.md refreshed to state the overlay.

### SOLR-3657
- Line 25: applied by the open fixes lane; verified against the receipt.

### SOLR-5065
- Line 27 (the passage sits at line 25 of the draft): applied by the open fixes lane in the 12-character SHA form; verified against the receipt and the tree.

### SOLR-5941
- Line 41: applied in this lane (fact check above).
- Bold line 35 and line 38 (the "By reading the base code" qualifier and the CommitTracker merge-base sentence): applied by the open fixes lane; verified.
- Limits bold line 45 ("three SolrCloud commit paths"): applied by the open fixes lane; verified.
- Length: the round 3 candidate trim is applied in this lane ("This change does not touch that check." and "internal values" are dropped from the Limits bullet on `IgnoreCommitOptimizeUpdateProcessorFactory`, keeping the facts and the follow-up offer). The main side records an acceptance of the remaining length: the draft documents a behavior change across standalone and three SolrCloud commit paths, a three-option Choice, and a client-visible request mark, which is the unusually complex case the formula's guide allows over the usual length.

### SOLR-7504
- Lines 25, 38 and 27: applied by the open fixes lane; verified against the receipt.

## Record items

- **Receipts refreshed** so the pre-fix runs name the branch's test file where the drafts now qualify them: SOLR-16655 (above), SOLR-3657 (the pre-fix line now names this branch's `DocumentBuilderTest.java`), SOLR-5065 (the pre-fix line now states the base production and the branch's added `DefaultSchemaSuggesterTest.java`), SOLR-11483 (the pre-fix line now states the premise tree, base production with the branch's TestRecovery file, per g11483-premise.log).
- **SOLR-12705 combined-run head:** the round 2 response's UPC-R2-505 record sentence (its line 33) is corrected in place: the combined run used SOLR-7504 at its earlier head `22b77196e662`, and that head is SOLR-7504's, not SOLR-12705's.
- **UPC-R2-312 (SOLR-13265 registration link): applied in this lane.** The link now uses the merge-base form (`DirectUpdateHandler2.java on the merge-base`, lines 298 to 300 at `9b3a84b1c460`); the registration lines are identical at the head and the merge-base, verified against both trees, and the branch does not change them.
- **UPC-R2-508 and UPC-R2-510 (SOLR-5941): recorded as applied.** Neither had a disposition in the round 2 response; both were restated as round 3 should-fix items (the Limits bold "three SolrCloud commit paths" and the Proof bold "By reading the base code") and were applied by the open fixes lane, verified above. UPC-R2-511 is the length item, dispositioned with SOLR-5941 above. UPC-R2-509 (naming the two classes that set `commit_end_point` on requests they build directly) was not restated by round 3 and is not applied here; it stays in the main side's notes, and round 4 may re-raise it.

## What round 4 needs to settle

Round 4 re-checks the 10 drafts that were not converged in round 3 (SOLR-12705, 7022, 14718, 16673, 6973, 16655, 3657, 5065, 5941, 7504), verifies each disposition above against the live draft text, and gives a verdict of CONVERGED or NOT CONVERGED per draft. For SOLR-6973 it judges the rewritten behavior statement against the branch and merge-base code on the reading recorded above. The assignment is `assignments/pool-update-processing-convergence-round-4.md`.
