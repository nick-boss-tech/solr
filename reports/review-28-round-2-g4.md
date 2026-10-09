# Review round 2, group 4: SOLR-6065, SOLR-6973, SOLR-7022, SOLR-11475

Checked 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Read only. The final drafts and receipts were read from `origin/pr-prepare`, and the round 1 and corrections reports and the owner's answers from the same branch. The live PRs were read with `gh pr view` only. Diffs were run read-only against each head's merge base with `upstream/main`. No build, Gradle run or test was run. No draft, receipt, branch or PR was changed.

## Summary

| PR | Ticket | Verdict |
|---|---|---|
| 5078 | SOLR-6065 | READY TO FLIP |
| 5079 | SOLR-6973 | NOT READY: Limits names a gap with no planned follow-up |
| 5080 | SOLR-7022 | NOT READY: the commit-level test class is not named in Proof, and the bold Proof summary is broader than the runs |
| 5082 | SOLR-11475 | READY TO FLIP (one optional wording change) |

## SOLR-6065 (PR 5078)

**Verdict: READY TO FLIP.** The title stands under the owner's title ruling.

Part 1
- Title (round 1): not applied as the group text specified. The owner ruled that the 24 Jira-summary titles stand. The title is accurate: the change gives a clear message for this case.
- Bold Limits summary (round 1): applied as specified.
- Proof wording (round 1, item 3): applied differently. The draft uses the receipt's wording, "fails with the generic analysis-error shape", and drops "400". Same fact.
- "Review" line (corrections): applied. It now reads "Whether to keep it is a question for maintainers."
- Planned follow-up (O2, corrections): applied. "A follow-up submission is planned for those paths."
- Internal vocabulary (gate, receipt, ledger, JUnit XML, rc=, takeover, live tip, pre-fix, "we"): none found.

Part 2: not among the six dispositions.

Body and head
- The live body equals the final draft (compared as UTF-8 text, neither has a BOM or CR).
- Head 3d2cec9e1ab3a19f6b948682b116c2780da4b65b matches the receipt head. isDraft is true.
- Title: "SOLR-6065: Solr should give you clear error if you try to add too many docs".

## SOLR-6973 (PR 5079)

**Verdict: NOT READY.** Limits names a gap and states no planned follow-up. Add the sentence in replacement 1.

Part 1
- Title (round 1): not applied as the group text specified. The owner ruling keeps the title as opened. Accuracy question for the owner: the title says documents "will not update", while the body says a partial update "can delete unrelated documents" and that the reporter's symptom is "not shown to come from this path". See replacement 3.
- Proof build-check sentence (round 1, item 2): applied differently. The draft reads "The core module checks also pass." The receipt records one module check, rc=0. Same fact, and no rc= in the draft.
- Planned follow-up (O2, corrections): missing. The Limits bullet names a gap (the reporter's symptom), and the draft has no follow-up sentence. The corrections report's confirmation item 1 lists SOLR-6973 as carrying one.
- Internal vocabulary: none found.

Part 2
- "(by reading)" framing in Proof: applied. Proof states the base run (7 tests, 1 failure) and identifies the failure as the new test. The receipt's pre-fix verdict is PASS, which supports the attribution. The receipt does not name the failing test, and the gate log is not in the repository, so the attribution was not checked against the log.
- Limits still reads "checked by reading only". It is accurate, because the new test is a processor-level check with no index, but it is the same framing. Optional replacement 2.
- rc=0: removed from Proof. Applied.

Body and head
- The live body equals the final draft.
- Head fa5b59ba07b47fca7bd513ef619bec4632595b98 matches the receipt head (fa5b59ba07b4). isDraft is true.
- Title: "SOLR-6973: Some documents will not update on a cloud server using SignatureUpdateProcessorFactory".

Proposed replacement text
1. Limits, required. Append to the last sentence of the bullet: " A follow-up submission is planned if the shard-routing path is confirmed as the cause of the reported symptom."
2. Limits, optional. Replace "The effect on new documents was checked by reading only." with "The effect on new documents was confirmed by reading the code. No index-level test covers it."
3. Title, optional, needs owner ruling: "SOLR-6973: A partial update with no signature fields no longer gets an empty signature, which can delete unrelated documents."

## SOLR-7022 (PR 5080)

**Verdict: NOT READY.** Limits refers to a second test class that Proof never names and that has no recorded run at head. Bold Proof summary is broader than the recorded runs. Apply replacements 4 and 5.

Part 1
- Round 1, item 1 (Proof contradicts the receipt): applied differently. The Proof follows the restored receipt: original gate at a2ed957063b on 2026-10-05, current head db357868610b. It does not use the round 1 replacement text, which named 6a233ab2fdb on 2026-10-06. The facts match the receipt. The 2026-10-06 counts are dropped, as the owner ruled.
- Round 1, item 2 (receipt date line): applied. The receipt has no bare "Date" line now. Its gate date is 2026-10-05 and the top-up is labelled as a later entry.
- Round 1, item 3 (title): not applied as specified, by ruling. The title stands. It names the logged error that the change stops emitting at ERROR for interrupts, so it is accurate as the reported symptom.
- Round 1, item 4 (bold Limits summary): applied as specified.
- Round 1, unchecked CI run number: removed. The draft carries no run number.
- Corrections, "live tip" replaced by "current head": applied. Grep finds no "live tip".
- Corrections, planned follow-up: none, as the owner ruled. The corrections report lists SOLR-7022 in its confirmation item 1 as carrying one, and in item 2 as without one. The owner ruling is the second.
- Corrections, DirectUpdateHandler2CommitWaitTest line in Proof: dropped, as ratified. See item (a).
- Internal vocabulary: none found.

Part 2
- Receipt disposition: applied and accurate. Checked against git: a2ed957063b to 6a233ab2fdb adds only DirectUpdateHandler2CommitWaitTest.java (162 lines, one test method). 5b822b7e8e9 changes only the changelog title. db357868610b changes only one Javadoc comment in DirectUpdateHandler2.java.
- Text-only claim: "The two last commits change wording only." is accurate as scoped. The draft does not mention 6a233ab2fdb, which adds the 162-line test file that is in the PR diff. See item (a).
- Live tip disposition: applied ("current head").

Items for the owner
- (a) Limits says "the commit-level test is timing-sensitive" and "two test classes cover a small change". The PR diff adds DirectUpdateHandler2CommitWaitTest, which Proof never names, and the receipt records no run of it at head. A reader cannot tell which test is meant. The Proof sentence "the result does not depend on timing" refers to testInterruptStatusIsRestored in DirectUpdateHandler2AwaitSearcherTest, a different class. Replacement 4 names the class and adds no count. It needs ratification, because the owner dropped the Proof line.
- (b) The bold Proof summary "The fix passes its tests" is broader than the recorded runs, since the commit-level class has no recorded pass at head. Replacement 5.

Proposed replacement text
4. Limits, first bullet. Replace the whole bullet with: "- The commit-level test, DirectUpdateHandler2CommitWaitTest, is timing-sensitive. It relies on a single-threaded searcher executor and short joins, and it has not been checked for seed sensitivity. No run of this class is counted in the proof above."
5. Proof, bold line. Replace "**The fix passes its tests, and the interrupt test fails without the change.**" with "**The recorded runs pass, and the interrupt test fails without the change.**"

Body and head
- The live body equals the final draft.
- Head db357868610b92709335129ebada4646ecf11e26 matches the receipt head. isDraft is true.
- Title: "SOLR-7022: ERROR UpdateHandler java.lang.InterruptedException". Stands.

## SOLR-11475 (PR 5082)

**Verdict: READY TO FLIP.** The title stands under the owner's title ruling. One optional wording change is below.

Part 1
- Title (round 1): not applied as the group text specified, by ruling. The title stands. It names the bug the change fixes, so it is accurate.
- Bold Limits summary and bullet 1 (round 1): applied as specified. The ratified follow-up sentence follows bullet 1.
- Planned follow-up (O2, corrections): applied in both bullets.
- Internal vocabulary: none found.
- Other round 1 items: none for this PR. The draft matches the receipt: five classes at 1 of 1, verified 2026-10-08 at 0de48e492fd4, and the pre-fix result stated as inconclusive.

Part 2: not among the six dispositions.

Proposed replacement text (optional)
6. Limits, second bullet. Replace the whole bullet with: "- The test runs with both settings of the complete-list flag. Other arrangements of a same-version pair are not tested. A follow-up submission is planned to cover those arrangements." This removes the restatement of the summary's "one set of versions".

Body and head
- The live body equals the final draft.
- Head 0de48e492fd49a15f406b2a5500c337f92ef3d4d matches the receipt head (0de48e492fd4). isDraft is true.
- Title: "SOLR-11475: Endless loop and OOM in PeerSync". Stands.

## Roll-up notes for the lead

- Planned follow-up count: the assignment's 22 drafts and the corrections list do not match the drafts. Twenty drafts contain "follow-up" or "planned" wording. SOLR-6973 is listed as carrying a plan but has none. The other drafts on that list were not checked in this group.
- Titles: the owner's ruling keeps the four titles in this group. Only SOLR-6973 has an accuracy question.
- Bodies: all four live bodies match their drafts. If replacements 1, 4, 5 or 6 are ratified, the four bodies change and must be re-applied and re-compared.
