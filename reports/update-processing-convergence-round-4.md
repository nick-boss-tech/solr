# Update processing convergence, round 4

Verdict: **NOT CONVERGED.** 7 of the 10 drafts converge. Three do not: SOLR-14718, SOLR-3657 and SOLR-5065. Each has one should-fix item, and the replacement text is below. This round is read-only on drafts and receipts, so nothing was edited here. The main side applies the replacements, then a re-check of the three drafts follows.

Assignment: `assignments/pool-update-processing-convergence-round-4.md`. Three read-only subagents checked the groups: A (SOLR-6973, 12705, 16655, 5941), B (SOLR-7022, 14718, 16673) and C (SOLR-3657, 5065, 7504). Their reports were checked by the coordinator against the record lines they cite before this report was written.

Head check: all ten live tips match the heads their drafts name, so no draft is on hold. Checked with `ls-remote` only.

## One line per draft

- **SOLR-6973: CONVERGED.** Line 17 matches the round 3 roll-up text, and the judgment on it is below. Bold line 7, line 9, line 23, line 25 and Limits line 37 match their dispositions.
- **SOLR-12705: CONVERGED.** Line 17 reads "Three" and names three processors. Line 28 matches the round 3 text. The pointer is in What, and Limits ends at "with an atomic update." The line 27 run on `b053944b127` is not covered by a receipt in this round.
- **SOLR-16655: CONVERGED.** Line 25 matches the round 3 text. `b201a57fb3e5` is the parent of `5e2317443f41`, and the test is absent at `b201a57fb3e5`.
- **SOLR-5941: CONVERGED.** Line 41 matches the round 3 text. Bold line 35, line 38 and Limits bold line 45 match. The line 49 trim is applied.
- **SOLR-7022: CONVERGED.** Line 24 ("the formatting check") matches receipt line 5. Line 26 ("is read to fail ... this was not run") matches receipt lines 5 to 7 and audit finding 3. Proof counts match the receipt.
- **SOLR-16673: CONVERGED.** Line 23 matches the receipt's PASS-only record at `d5c19e64ba1b`. The tip's changelog-only claim matches the top-up.
- **SOLR-7504: CONVERGED.** Lines 25, 27 and 38 match receipt lines 6 to 8.
- **SOLR-14718: NOT CONVERGED.** See finding 1.
- **SOLR-3657: NOT CONVERGED.** See finding 2.
- **SOLR-5065: NOT CONVERGED.** See finding 3.

## Findings

### 1. SOLR-14718, draft line 25 (should-fix)

The draft says: "A check on 2026-10-09 removed the copy at this head. The test then failed the same assertion in each of 3 runs, and the unmodified head passed."

`receipts/SOLR-14718.md` records only the 2026-10-07 gate: head `29c09959791a`, SolrCmdDistributorTest 1 of 1, and the base premise with exactly one failure. It does not record a 2026-10-09 check. The count therefore has no receipt. `audits/update-processing/SOLR-14718.md` lines 35 to 39 hold the source: seeds `14718C0FFEE14718`, `14718C0FFEE14719` and `14718C0FFEE14720`, each one AssertionError at `SolrCmdDistributorTest.java:572`, and a control at the unmodified head with 0 failures. The draft's wording matches that audit text.

Fix, for the main side: add the 2026-10-09 settling run to `receipts/SOLR-14718.md` from the main-side record (three seeds, one failure each at line 572, control passing). The draft sentence can then stay as written, or cite the receipt.

### 2. SOLR-3657, bold line 22 (should-fix)

The bold line says: "On base production code, the run without the fix has 2 failures."

`receipts/SOLR-3657.md` line 8 makes the overlay part of the condition: "On base production with this branch's `DocumentBuilderTest.java` applied, DocumentBuilder runs 17 tests with exactly 2 failures." The bold line drops that condition. Read alone, it states a proof the receipt does not support. Bullet line 25 carries the condition and agrees with the receipt on counts and date.

Note: the earlier fix-lane recheck (`reports/review-recheck-round-4.md`) read this bold line as wording only. That reading is superseded here, because the receipt's own condition includes the overlay.

Replacement for line 22: "**The focused tests pass at this head. On the merge-base code at `97d973814336`, with this branch's `DocumentBuilderTest.java` applied, the run without the fix has 2 failures.**"

### 3. SOLR-5065, line 9 and line 25 (should-fix)

Both passages call `0cc328310f8f` the merge-base. It is not. The merge-base of `ab894a996c8a` with upstream/main is `97d973814336`, and `0cc328310f8f` is an older ancestor. The receipt (`receipts/SOLR-5065.md` line 8) and the round 3 response both say "base production `0cc328310f8f`". Only the draft says merge-base. The code under test is the same at both SHAs: the directories the test touches have an empty diff between them.

Replacement for line 25, first clause: "On the base production code at `0cc328310f8f`, with this branch's `DefaultSchemaSuggesterTest.java` added (the class is new on this branch), `testGuessFieldTypeExponentFormsInferDouble` fails."

Replacement for line 9: "(at base `0cc328310f8f`)".

Note, not a finding: the class link at line 25 (`#L31-L49`) runs past the end of the file, which is 48 lines at `ab894a996c8a`. Use `L31-L48`. The test link `L33-L41` is correct.

Correction of record (main side, 2026-10-12, from round 5 item UP-R5-05): Round 4 finding 3 is withdrawn. `0cc328310f8f` is the merge-base of `ab894a996c8a` with upstream/main, so the original label was accurate. The class link stays at L31-L49. The replacement clauses above were applied in round 4 and stand: the applied wording asserts nothing false and matches the receipt. The file count in the note above is also corrected by this withdrawal: `DefaultSchemaSuggesterTest.java` has 49 lines at `ab894a996c8a`. Verified again on 2026-10-12 against a fresh fetch of upstream main: `git merge-base` returns `0cc328310f8f`, and `97d973814336` is not an ancestor of the head. The full record is in `reports/update-processing-convergence-round-5-response.md`.

## SOLR-6973 judgment (the blocking item from round 3)

The line 17 statement is accurate for both new and existing documents. Group A traced the code:

- On the merge-base `cabedd1d968`, `SignatureUpdateProcessorFactory.processAdd` throws in its loop (lines 169 to 178) only when a signature field is present in the partial update. With none present, it builds the empty signature (line 199) and writes it with `doc.addField` (line 200). With `overwriteDupes`, it sets `cmd.updateTerm` (lines 202 to 204).
- `DistributedUpdateProcessor.versionAdd` calls `getUpdatedDocument` at line 394; that method (line 723) is gated on `isAtomicUpdate` at line 724. For a new document, the merge at line 778 starts from an id-only document, so the empty `signatureField` is stored. For an existing document, the merge at line 782 puts the normal field into the merged document (`AtomicUpdateDocumentMerger` lines 195 and 196), replacing the stored value. The `updateTerm` then deletes the documents that match it (`DirectUpdateHandler2` lines 1177 to 1180).
- At the branch head `51fcd05e695`, the partial update returns to the next processor at lines 165 to 179 (pass-through at line 177, return at line 178) before any signature work, whether or not the target exists.

So a new document has no `signatureField`, an existing document keeps its stored value, and no `updateTerm` is set. The statement's wording matches both cases. The reading assumes the signature processor runs before the auto-inserted DistributedUpdateProcessor. The reference guide's dedupe example (`update-request-processors.adoc` lines 92 to 96 at `51fcd05e695`) puts Signature before Log and Run, with DUP inserted just before Run, so the assumption matches the documented order.

## Notes for the main side (no finding; do not hold the drafts)

- **N1.** `receipts/SOLR-6973.md` line 4 labels `fa5b59ba07b4` "the live tip". It is stale. The live tip is `51fcd05e695`, which the same receipt's re-gate line records. Suggested header: "Gated head: `fa5b59ba07b4` (superseded by the 2026-10-09 re-gate at `51fcd05e695`, below)."
- **N2.** Round 3 made the blocking replacement conditional on owner confirmation. The response applied it on the main side's reading. The text matches the code. The record should say the confirmation was waived under the decision ladder, or Nick should confirm before posting.
- **N3.** The round 3 response's SOLR-7022 section cites a base `97d973814336` run from 2026-10-06 as the basis for line 26. That run is not in `receipts/SOLR-7022.md`. The response sentence should cite the receipt's premise run instead (seed `7022C0FFEE7022`, AwaitSearcherTest 1 of 2 in both runs).
- **N4.** UPC-R2-509 (SOLR-5941, round 2 notes) is deferred and was not restated in round 3. If the main side takes it, line 47 could add: "The two classes that set `commit_end_point` on requests they build directly are CommitTracker (true, `a4df7bfd214b` `CommitTracker.java` line 278) and DistributedZkUpdateProcessor (leaders and replicas, lines 225 and 242)."

## Not checked

- The SOLR-6973 line 35 claim about ticket comments: the JIRA ticket was not read.
- The SOLR-5941 line 48 claim that five classes were not re-run: no Group A receipt covers it.
- The SOLR-12705 line 27 run on `b053944b127`: the object exists, but no receipt in this round records it.
- The master receipts ledger, the takeover log and security material were not opened.

## Next step

The main side applies the three replacements (SOLR-14718 receipt update, SOLR-3657 line 22, SOLR-5065 lines 9 and 25). Then a re-check of those three drafts closes the round. Nothing was posted, filed or built.
