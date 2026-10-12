# Open review fixes lane, round 1

Counts: **applied 29 replacements on 27 lines, already present 0, excluded 11.** Slice F1 (update-processing drafts): 20 replacements on 18 lines across 9 drafts; 7 items excluded. Slice F2 (the other drafts): 9 replacements on 9 lines across 4 files; 4 items excluded.

The lane applied the should-fix replacements that the open review rounds specified. The main side checked the full diff of the 13 edited drafts and material files against each record: 27 changed lines, no added line with an em dash, and no change outside the named passages. No PR, comment, live PR body, branch, receipt or report was edited. Nothing was built, tested, gated or posted. The lane's edits are uncommitted in the worktree until this report is pushed with them.

## Applied: slice F1 (update-processing drafts)

From `reports/update-processing-convergence-round-3.md`.

- **SOLR-12705 line 17:** "Two existing processors change output." becomes "Three existing processors change output."
- **SOLR-7022 line 24:** "tidy" becomes "the formatting check".
- **SOLR-7022 line 26, last sentence:** the round 3 replacement ("fails 1 of 1 at the interrupt-restored assertion (2026-10-06)") asserted a run no record holds. **Corrected in the round 4 re-check** (`reports/review-recheck-round-4.md`): it now reads "is read to fail at the interrupt-restored assertion; this was not run." The line 24 run clause, which the lane did not change, is corrected in the same push.
- **SOLR-14718 line 24:** now reads "At head `29c09959791a`, SolrCmdDistributorTest passes 1 of 1 (verified 2026-10-07)."
- **SOLR-16673 line 23:** the parse claim is removed from the sentence, and "The new test fails without the fix." becomes "Before the fix, at `d5c19e64ba1b`, the new test fails; the gate record gives PASS only, and the observed failure is not recorded."
- **SOLR-6973 line 7 (bold):** now names the `fields` list condition.
- **SOLR-6973 line 9:** a merge-base link to the loop (lines 169 to 178) is added.
- **SOLR-6973 line 23:** the gate date and head are added, and "Reading the base code" qualifies the failing-test sentence.
- **SOLR-6973 line 25:** "The core module checks also pass." is removed.
- **SOLR-6973 Limits line 37:** "The new test checks processor output only, not indexed documents." is added before the reading-only sentence.
- **SOLR-3657 line 25:** the merge-base code and the branch's test file are named.
- **SOLR-5065 line 25:** the merge-base production code at `0cc328310f8f` and the branch's test file are named. The round 3 report cites line 27; the passage sits at line 25 of the draft.
- **SOLR-5941 line 35 (bold):** "On base" becomes "By reading the base code".
- **SOLR-5941 line 38:** "On base the processor never sees the autoCommit." is replaced by a merge-base link to `CommitTracker.java` line 280, with the statement that the base code commits through the update handler directly.
- **SOLR-5941 Limits line 45 (bold):** "one SolrCloud path" becomes "three SolrCloud commit paths".
- **SOLR-7504 line 25:** names the production code before the fix at `22b77196e662` and the branch's test file.
- **SOLR-7504 line 27:** "tidy" becomes "the formatting check".
- **SOLR-7504 line 38:** now names SOLR-7504 at its earlier head, with SOLR-12705 as the combined run.

## Applied: slice F2 (the other drafts)

- **SOLR-18368 line 1 (banner lead-in):** "decides whether a PR opens and under which key" becomes "decides under which key the PR opens". From draft fidelity round 2.
- **SOLR-9396 line 26 (Choice bold summary) and line 28:** both now name `$row.field` and `${row.field}`. **Composed wording:** the record names both spellings but gives no verbatim Choice text. See To verify.
- **gradlew ticket draft line 5:** the date "executed 2026-10-11" is removed. The other 2026-10-11 dates are not in this item and are unchanged.
- **gradlew ticket draft line 51:** the round 3 replacement text is used word for word, with its markdown code spans converted to Jira `{{ }}` markup. **Confirm the conversion, or restore the backtick form.**
- **CombinedQueryComponent ticket draft line 48:** "whose outcome is described with the open question in Fix options" becomes "whose question is posed in Fix options".
- **CombinedQueryComponent ticket draft line 20:** "(from code reading, not run)" is added to both real-exit sentences on the line. See To verify.
- **CombinedQueryComponent ticket draft line 56:** "(from code reading, not run)" is added to the real-exit sentence.
- **CombinedQueryComponent ticket draft line 54:** the clause "which is why this was not visible" is removed.

## Excluded (not edited)

- **SOLR-6973 line 17 (blocking):** the behavior statement. The owner must confirm the roll-up text first.
- **SOLR-6973 and SOLR-5941 length items:** a main-side decision.
- **SOLR-5941 line 41 (`CommitThroughNonLeaderTest` identity):** the main side confirms it from the fix gate log first.
- **SOLR-16655 line 25 (test name and pre-fix overlay):** the main side confirms the overlay first. This was the draft's only should-fix item, so SOLR-16655 has no edit.
- **SOLR-12705 line 28 (combined-tree head):** the main side confirms which SOLR-12705 head that run used.
- **SOLR-12705 lines 19 and 39 (the SOLR-7504 pointer):** applies only if the main side agrees.
- **CombinedQueryComponent ticket draft, Symptom bullet 3 (line 22), fix option 3 (line 64), line 66, and line 68:** the exactness scoping and the overlap wording. Reserved for the main side's wording.

## Not applied: notes and link forms

These are in the round 3 record but were not should-fix. Left for the main side.

- **SOLR-13265 line 26:** the registration link should use the merge-base form, or the response should record why not (round 3 note). Not applied.
- **SOLR-7504:** the record has no length item. Its notes (the dedupe sentence on line 35 and the Limits clause on line 37) were not applied.
- **Other round 3 notes:** SOLR-11483, SOLR-12864 (delete Limits line 31), SOLR-13943 (lines 34 and 42), SOLR-5939 and SOLR-6065 (link forms), the SOLR-5941 SHA form, the SOLR-7022 audit note, the SOLR-14718 run-without-copy wording, the SOLR-16673 link form, and the SOLR-6973 length and option B. Not applied.

## Observations for the coordinator

- **SOLR-3657 bold line 22** still reads "On base production code, the run without the fix has 2 failures." It was not on the should-fix list, so it is unchanged. Check it in the next round.
- **SOLR-16673 line 23** now names the head twice, "At the earlier head `d5c19e64ba1b` ... Before the fix, at `d5c19e64ba1b`". That is the round 3 wording as given.
- **SOLR-6973 line 9** now has two merge-base links with the same label. Consider distinct labels.
- **SOLR-12705 response line 33** names the wrong head for the combined run. The record asks the main side to correct it. Not in this lane.
- **Receipts:** the round 3 record asks the main side to refresh the receipts for SOLR-11483, 16655, 3657 and 5065, so their pre-fix runs name this branch's test file where the drafts now qualify them.

## To verify

- **SOLR-9396 Choice (line 26 and line 28):** the record names both spellings and cites `reports/draft-fidelity-searchcomponents-edismax-s10.md` lines 51 and 52, but gives no verbatim Choice text. The lane composed it. Confirm the wording, or paste the verbatim text.
- **gradlew line 51:** the record's replacement uses backticks. The draft uses Jira `{{ }}` markup, so the lane converted the markup. Confirm, or restore the backtick form.
- **CombinedQueryComponent line 20:** both labels are needed. Each sentence makes a different real-exit claim (the missing fold, then the header value), and the first label does not reach the second sentence. Answered in the round 4 re-check (slice R4-B, Q2); this corrects the earlier suggestion to drop the second label.

The excluded items stay with the main side.
