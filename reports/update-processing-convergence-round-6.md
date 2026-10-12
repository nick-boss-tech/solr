# Update processing convergence, round 6

Verdict: **NOT CONVERGED overall**, on two should-fix wording items in the SOLR-14718 audit record (UP-R6-01 and UP-R6-02). The drafts converge. The main side's rebuttal of UP-R5-01 stands.

Assignment: `assignments/pool-update-processing-convergence-round-6.md`. Two read-only subagents: group 1 checked the SOLR-14718 rebuttal and the ruling citation (UP-R5-01, UP-R5-03), group 2 checked the SOLR-3657 line, the SOLR-14718 audit record and the SOLR-5065 record (UP-R5-02, UP-R5-04, UP-R5-05). Both checked against the main side's response `reports/update-processing-convergence-round-5-response.md` and the commit `77f2663b1e1`. Nothing was edited, posted, built or tested.

## One line per item

- **UP-R5-02, SOLR-3657 line 25: CONFIRMED.** The parenthesis reads "(verified 2026-10-07)", and the commit changes only that line. The bold line 22 is unchanged. `receipts/SOLR-3657.md` dates the run 2026-10-07 and records DocumentBuilderTest 17/17 and TolerantUpdateProcessorTest 11/11 at head `14edaca577c0`.
- **UP-R5-01, SOLR-14718 line 36: rebuttal stands.** The sentence "A follow-up submission for that flaw is planned." is supported by the owner's ratification of planned follow-up commitments in the 22 drafts (`material/review-opened-28-answers.md` line 35, item 1, and `reports/review-corrections-28.md` line 29, which lists SOLR-14718). The rule that asks for a named follow-up is in `assignments/review-opened-28.md` line 21. The response's TESTING.md citation shows a recommendation only (see UP-R6-03). The draft stays as written.
- **UP-R5-03, SOLR-14718 line 31: CONFIRMED.** The landing-order sentence matches `material/update-29-rulings.md` line 7 and `material/update-29-reconciliation-answers.md` line 19. Line 31 is byte-identical at `8a3438088cc` and at the live tip.
- **UP-R5-04, SOLR-14718 audit: STILL OPEN, two wording items (UP-R6-01 and UP-R6-02).** Finding 1's status cell records the 2026-10-09 run, and the readiness line carries a superseded mark. The dated update matches the settling run section (seeds `14718C0FFEE14718`, `14718C0FFEE14719`, `14718C0FFEE14720`, one AssertionError each at `SolrCmdDistributorTest.java:572`, control 0 failures, matching `receipts/SOLR-14718.md` line 9). The ship-narrow decision is recorded in the root `TESTING.md` at lines 277 to 282. The two wording items below stay open until the main side applies them.
- **UP-R5-05, SOLR-5065 record: CONFIRMED.** The withdrawal note sits in `reports/update-processing-convergence-round-4.md` inside finding 3, at line 52. Git confirms `0cc328310f8f` as the merge-base of `ab894a996c8a` with upstream/main, `97d973814336` is not an ancestor, and the test file has 49 lines. `pr-drafts/update-processing/SOLR-5065.md` is not changed by the commit, and its lines 9 and 25 match `receipts/SOLR-5065.md` line 8.

## Findings

**UP-R6-01 | `audits/update-processing/SOLR-14718.md` line 18 | should-fix.** The status cell for finding 1 says "(see the settling run section at the end of this file)". The settling run section is not last; the dated update is last (line 43). The reference points to the wrong place.
Replacement for "(see the settling run section at the end of this file)": "(see the section 'Settling run for finding 1 (2026-10-09)')".

**UP-R6-02 | `audits/update-processing/SOLR-14718.md` line 43 | should-fix.** The update says "the finding texts and their audit-date evidence are unchanged". Finding 2's evidence cell gained one appended sentence, so the statement is not accurate.
Replacement for "the finding texts and their audit-date evidence are unchanged": "the finding texts are unchanged, and finding 2's evidence cell gains one appended sentence pointing here".

**UP-R6-03 | main-side response, section 1 | note, record wording.** The response cites `TESTING.md` for the follow-up. That file recommends a follow-up (lines 164 and 165) and records the ship-narrow decision (lines 277 to 280), but it makes no follow-up commitment. The commitment comes from the 22-draft ratification. Suggested record wording: "The sentence rests on the owner's 2026-10-09 ratification of planned follow-up commitments in the 22 drafts (`material/review-opened-28-answers.md` line 35)."

**UP-R6-04 | main-side response, section 1 | note.** The response frames a conflict between two records. `material/review-opened-28-answers.md` line 56 is a main-side record, not an owner decision, and the "gap" in `reports/review-corrections-28.md` line 30 is a different limitation (draft line 33, "Two paths still name one request"). No change to the draft is needed. The record can drop the conflict framing.

## Checks that held

- The response's quote of `material/review-opened-28-answers.md` line 56 matches the file word for word.
- The response's paraphrase of TESTING.md lines 164 and 165 and 277 to 280 is accurate.
- The branch head `29c09959791a` has no TESTING file, so the ship-narrow record lives only at the worktree root.
- No em dash or en dash in the added lines of the round 5 application commit.

## Not checked

- The raw JUnit logs and the takeover log.
- The JIRA ticket text.
- The response's claim of a "fresh fetch" for the SOLR-5065 check: no fetch was run in this round, and the local refs match the claim.

## Convergence line

NOT CONVERGED overall. The drafts for SOLR-3657, SOLR-14718 and SOLR-5065 converge on their text, and the rebuttal of UP-R5-01 stands. The SOLR-14718 audit record waits on UP-R6-01 and UP-R6-02. Nothing was edited, posted, built or tested in this round.
