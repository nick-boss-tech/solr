# Update processing convergence, round 3

Verdict: **NOT CONVERGED.** 6 of the 16 drafts re-checked here converge, and 10 do not. One blocking item remains (SOLR-6973, the behavior statement, which the owner must confirm). The other open points are small replacements for the main side to apply, and most are wording or citation fixes.

Scope: the 16 drafts left open by round 2 in `pr-drafts/update-processing/`, checked against the round 2 register, the round 2 response, the audits, receipts and material they cite. Every fork tip matched its draft's named head. No draft was edited, and every replacement below is for the main side to apply. Settled decisions in `material/update-processing-*.md` and the round 1 and round 2 responses were not reopened.

## Counts

| Slice | Drafts | Converged | Not converged | Blocking | Should-fix |
|---|---|---|---|---|---|
| C1 | 6 (12705, 7022, 14718, 13265, 16673, 6973) | 1 (13265) | 5 | 1 (6973) | 13 |
| C2 | 6 (11483, 12864, 13943, 16655, 3657, 5065) | 3 (11483, 12864, 13943) | 3 | 0 | 3 |
| C3 | 4 (5939, 5941, 6065, 7504) | 2 (5939, 6065) | 2 | 0 | 7 |
| **Total** | **16** | **6** | **10** | **1** | **23** |

Converged: SOLR-11483, 12864, 13265, 13943, 5939, 6065.

Not converged: SOLR-12705, 7022, 14718, 16673, 6973 (C1); 16655, 3657, 5065 (C2); 5941, 7504 (C3).

## Blocking

- **SOLR-6973, line 17 (blocking): the behavior change is misstated.** The passage reads "Behavior change: a partial update that creates a new document now stores it without signatureField." On the merge-base, a partial update with no signature fields to an existing document also gets the empty signature in `signatureField`, and `updateTerm` deletes other documents with that term. The path is `DistributedUpdateProcessor.getUpdatedDocument` (merge-base lines 723 to 724) and `AtomicUpdateDocumentMerger` line 195 ("normal fields are treated as a set"); the head returns at lines 174 to 177 for both. The formula requires behavior changes to be stated openly.
  - **Replace with** (the roll-up text; the owner confirms it): "Behavior change: a partial update with no signature fields no longer gets an empty signature. This covers new documents and existing ones. A new document is stored without `signatureField`. An existing document keeps its stored `signatureField`, and the update no longer deletes other documents that share the empty signature."

## Should-fix, by draft

### SOLR-12705 (C1)
- **Line 17:** "Two existing processors change output." The bullet names three processors. **Replace "Two" with "Three".**
- **Line 28:** "both suites pass, 30 of 30 and 43 of 43" names no class and no head. The 30 is the count in the SOLR-7504 tree, not this branch's. **Replace with:** "- Combined tree with SOLR-7504 at `22b77196e662` (before its null-count fix), 2026-10-09: FieldMutatingUpdateProcessorTest 30 of 30 and ParsingFieldUpdateProcessorsTest 43 of 43. It does not cover the fixed SOLR-7504 head, `2fe06bfd917f`." The main side confirms which SOLR-12705 head that combined tree used; the record does not say.
- **Restated pointer, lines 19 and 39:** "Counted fields follow the rule in SOLR-7504, which lands first" appears in What and in Limits. **Apply only if the main side agrees** that What line 19 carries the pointer: end Limits line 39 at "with an atomic update."

### SOLR-7022 (C1)
- **Line 26, last sentence:** the outcome claim is accurate as written (the recorded premise run supports "fails at the interrupt-restored assertion"). The "base" in the paragraph is imprecise: it means reverted production code for the first half and untouched merge-base code for the commit-level test. **Replace with:** "On the merge-base production code, with this branch's commit-level test added, `DirectUpdateHandler2CommitWaitTest` fails 1 of 1 at the interrupt-restored assertion (2026-10-06)." The "checked by reading" qualifier must not be applied; it would contradict the record.
- **Line 24:** "tidy" is an internal step name. **Replace "tidy" with "the formatting check".**

### SOLR-14718 (C1)
- **Line 24:** "With this change, the focused test class passes at this head (checked 2026-10-07)." It gives no count or head. **Replace with:** "- At head `29c09959791a`, SolrCmdDistributorTest passes 1 of 1 (verified 2026-10-07)."

### SOLR-16673 (C1)
- **Line 23:** "the changelog parses," is not in the receipt at the earlier head (the YAML parse is recorded at the tip only). **Delete "the changelog parses, "** from line 23; line 24 already says it parses at the tip.
- **Line 23:** "The new test fails without the fix." gives no count and no observed failure. The record says only PASS. **Replace with:** "- Before the fix, at `d5c19e64ba1b`, the new test fails; the gate record gives PASS only, and the observed failure is not recorded." Fill in the count and failure only if the gate log, which is not in the workspace, records them.

### SOLR-6973 (C1; the blocking item above is on line 17)
- **Bold line 7:** the summary omits the condition that the tip rejects partial updates without a fields list (lines 150 to 155, and What line 15). **Replace with:** "**With `overwriteDupes` and a `fields` list, a partial update with no signature fields can delete unrelated documents.**"
- **Line 9:** the loop claim has no link. **Add after the loop sentence:** "([SignatureUpdateProcessorFactory.java on the merge-base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/update/processor/SignatureUpdateProcessorFactory.java#L169-L178))". Lines 169 to 178 at the merge-base are the loop.
- **Line 23, last sentence:** "The failure is the new test: the base code sets both values it checks for absence." This comes from reading the base code, not from a run, and the receipt names no failing test. **Replace the last sentence with:** "Reading the base code, the failing test is the new test: the base code sets both values it checks for absence (base lines 199 to 203)." Add "(gate 2026-10-08, head `fa5b59ba07b4`)" after "1 failure" in the preceding sentence.
- **Line 25:** "The core module checks also pass." The receipt says "module check passes" and never names the core module; a build check is not a test outcome. **Delete the sentence.**
- **Limits, line 37:** "The effect on new documents was checked by reading only." The new test asserts on the processor's output only and indexes nothing. **Replace with:** "The new test checks processor output only, not indexed documents. The effect on existing documents was checked by reading only."

### SOLR-16655 (C2)
- **Line 25:** the test name is absent at `b201a57fb3e5` (the commit that adds it is the branch's last). **Replace with:** "- Without this change, `testTrimFieldsRecurseIntoNestedDocumentsOnlyWhenParentSelected` fails. That run used the pre-fix head `b201a57fb3e5`, the parent of the branch's last commit `5e2317443f41`, with this branch's `FieldMutatingUpdateProcessorTest.java` applied, because that commit adds the test. That commit adds the rule that a child document is walked only under a selected field." The main side confirms the overlay in the pre-fix gate log, and states it in `receipts/SOLR-16655.md`.

### SOLR-3657 (C2)
- **Line 25:** the 17-test count and the two failures need this branch's test file on the base, because the destination test is new. **Replace with:** "- On the merge-base code at `97d973814336`, with this branch's `DocumentBuilderTest.java` applied, `DocumentBuilderTest` runs 17 tests with exactly 2 failures, both at the destination-prefix assertions (gate of 2026-10-07)."

### SOLR-5065 (C2)
- **Line 27:** "It fails on the unmodified base, 0cc328310f8, and passes with this change." `DefaultSchemaSuggesterTest.java` does not exist at the merge-base, so "unmodified base" can only mean base production code with the branch's test file added. **Replace with:** "- On the merge-base production code at `0cc328310f8f4162572ea5bd5256cea29543e261`, with this branch's `DefaultSchemaSuggesterTest.java` added (the class is new on this branch), `testGuessFieldTypeExponentFormsInferDouble` fails. It passes with this change." Also use the 12-character SHA form here, as the receipt does.

### SOLR-5941 (C3)
- **Line 41:** the new test fails at the pre-fix head only with this branch's `CommitThroughNonLeaderTest.java` added (it is absent at that head). **Replace with:** "- With this branch's `CommitThroughNonLeaderTest.java` added to the pre-fix head `62516cc338ef`, the new test fails without the fix. Base `cabedd1d968` was not run for this test, because the defect came in with this branch's own guard." The main side confirms from the fix gate log that `CommitThroughNonLeaderTest` is the test meant by "the new test". This is a fact check on text round 1 applied, not a reopened decision.
- **Bold line 35 and line 38:** "On base, the recording processor never sees an autoCommit." and "On base the processor never sees the autoCommit." rest on reading `CommitTracker.java` line 280, with no base run, and line 38 restates the bold. **Replace bold line 35 with:** "**By reading the base code, the recording processor never sees an autoCommit. At this head, the focused classes pass, including the SolrCloud classes.**" **Delete** "On base the processor never sees the autoCommit." from line 38 and **add:** "The base code commits through the update handler directly ([CommitTracker.java](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/update/CommitTracker.java#L280), on the merge-base)."
- **Limits, bold line 45:** "The tests cover standalone chain behavior and one SolrCloud path." Proof names three SolrCloud classes. **Replace with:** "**The tests cover standalone chain behavior and three SolrCloud commit paths. No live multi-node run was made.**"
- **Length:** the prose is about 4,730 bytes with link URLs removed, against the guide's 3,500. No record accepts this draft's length. The main side records an acceptance or trims. One candidate: drop "This change does not touch that check." and "internal values" from Limits line 49, keeping the facts and the follow-up offer.

### SOLR-7504 (C3)
- **Line 25:** the 22b77196e662 run uses this branch's test file, since that commit has 29 test methods. **Replace with:** "- On the production code before the fix, at `22b77196e662`, with this branch's test file added, only this test fails: 1 failure in 30."
- **Line 38:** "A combined run with SOLR-12705 passed on 2026-10-09 at the earlier head `22b77196e662`, before the null fix." `22b77196e662` is SOLR-7504's head, not SOLR-12705's. **Replace with:** "A combined run of SOLR-7504 at its earlier head `22b77196e662` with SOLR-12705 passed on 2026-10-09, before the null fix." Record for the main side: the SOLR-12705 response (line 33) says the combined run used that head for SOLR-12705, which is wrong; correct the response.
- **Line 27:** "tidy leaves a clean tree" uses the internal step name. **Replace with:** "The changelog YAML parses, the formatting check leaves a clean tree, the Error Prone compile passes, and the module checks pass."

## Notes (no change required unless the main side agrees)

- SOLR-11483 (converged): the receipt says "On base production, TestRecovery runs 21 tests" without naming the branch's file; refresh the receipt. Audit findings 3 and 4 are not named in Limits; optional line: "The test uses literal values and leaves the UpdateLog it creates open."
- SOLR-12864 (converged): Limits line 31 restates bold line 29; delete line 31. `research/pipeline/skips.md` line 502 still conflicts with the decision (a record item).
- SOLR-13265 (converged): the registration link at line 26 (`DirectUpdateHandler2.java#L298-L300`) should use the merge-base form, as round 2 UPC-R2-312 asked; the response files it under "no draft change" without a reason. Apply, or record why not. Line 25 restates line 24.
- SOLR-13943 (converged): line 34 says "the method's comment"; the linked comment is in `concurrentUpdates`, which the test calls. **Replace with:** "that the comment in `concurrentUpdates` describes." Limits line 42 ("That base stays, because this branch does not contain the changelog commit.") is unclear: **replace with:** "The branch stays on `1d0b8a0a73cd`, the head the gate used. It does not contain the later SOLR-13696 commit `da4fa6df117`." The fragment title on the branch still names the old test method; the main side decides whether to edit the fragment.
- SOLR-5939 and SOLR-6065 (converged): notes on link forms, the Lucene version link (the merge-base form is preferred for unchanged content), the changelog title against the 400 to 500 change, and the SOLR-7022 overlap.
- SOLR-5941: the 62516cc338e and 62516cc338ef abbreviations should be one form.
- SOLR-7504: the dedupe sentence on line 35 repeats the bold, and Limits line 37 should add the clause that the SOLR-6045 combined behavior is not tested, so the two drafts agree.
- SOLR-7022: audit finding 3 says the commit-level test was read, not run, while the premise run is recorded; the audit should cite the 2026-10-06 run or say it did not see it. Audit finding 1 is resolved at the tip.
- SOLR-14718: the landing order (SOLR-5939 first) is supported by the recorded decision; the run-without-copy line should say that only `SolrCmdDistributor.java` was restored and name the observed failure.
- SOLR-16673: the Schema Designer links point at the tip, though this branch does not change that file; link the merge-base or say so, and mark "an empty value no longer throws" as checked by reading. The "Title:" line is not in the formula template.
- SOLR-6973: the length is about 3,500 characters; roll-up finding 7 option B is not in the draft and is an owner pick.

## Record items for the main side

- Refresh the receipts for SOLR-11483, 16655, 3657 and 5065 so their pre-fix runs name the branch's test file where the draft now qualifies them; the formula makes the receipt the Proof source.
- Correct the SOLR-12705 response line 33 (the combined run head).
- The response does not disposition several round 2 items (UPC-R2-312, 508, 510). Record each as applied, or as declined with a reason.
- The SOLR-6973 behavior change needs the owner's confirmation of the replacement text (blocking).

## Process

- Read-only. No draft, receipt, audit, branch or record was edited. Nothing was posted, filed or built.
- Main side action: apply the blocking replacement after the owner confirms it, and the should-fix replacements above. Then run round 4 on the drafts still open.
- Convergence: round 3 is NOT CONVERGED.
