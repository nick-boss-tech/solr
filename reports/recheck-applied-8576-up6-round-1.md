# Re-check of applied items: SOLR-8576 landing re-check and update processing round 6

Verdict: **CONFIRMED.** Both re-checks close their items. The SOLR-8576 draft converges, and the SOLR-14718 audit items UP-R6-01 and UP-R6-02 are closed. Two notes are recorded for the main side, and neither blocks closure.

Scope: the main side's application commit `69b44655d5b` (`reports/core-admin-8576-landing-recheck-response.md` and `reports/update-processing-convergence-round-6-response.md`), checked against `reports/core-admin-8576-landing-recheck-round-1.md` and `reports/update-processing-convergence-round-6.md`. The branch head `solr-8576-submit` is `883bb522caf97ca59f5303117efa81d1ec12dbfe`, confirmed by ls-remote. The claim is `claims/pool-recheck-8576-and-up6-applied.md`. Two read-only subagents, four tasks each. Nothing was edited, posted, built or tested.

## SOLR-8576 (CA-LR-01, CA-LR-02): CONVERGED

- **Line 15 (CA-LR-02): applied verbatim.** The sentence matches the replacement word for word. The branch test at `883bb522` asserts the collection under its name (lines 1186 and 1187) and that the alias resolves (lines 1188 to 1190). The wording matches those assertions.
- **Line 19 (CA-LR-01, bold Proof line): applied verbatim.** `pr-formula.md` section 3 asks for this statement on a test-only change.
- **Line 22 (CA-LR-01, bullet): applied with the stated deviation, acceptable.** The bullet matches the receipt's pin run (1 test, 0 failures on `b6b2b8f10e`). It drops the sentence "No fail-before run was made." The receipt records no fail-before run, and line 19 already states that the proof is inconclusive by construction, so nothing is lost.
- **Whole draft.** Only lines 15, 19 and 22 changed against the pre-application text. Every GitHub link uses `883bb522` or the base `b6b2b8f10e`, as the citation rule asks. There are no em dashes, no OWED marker and no process words.

## Update processing round 6 (UP-R6-01, UP-R6-02): CLOSED

- **UP-R6-01, audit line 18: applied verbatim.** The cell now reads "(see the section 'Settling run for finding 1 (2026-10-09)')", and that heading exists at line 35 of the audit. No remaining "end of this file" reference points at the settling run.
- **UP-R6-02, audit line 43: applied verbatim.** The sentence now reads "the finding texts are unchanged, and finding 2's evidence cell gains one appended sentence pointing here". Finding 2's evidence cell carries that sentence, and it is accurate.
- **Response record.** Every quoted before and after text in `reports/update-processing-convergence-round-6-response.md` matches the files.
- **Drafts.** `pr-drafts/update-processing/` is unchanged by `69b44655d5b`.

## Notes (not blocking)

**RC-01 | `audits/update-processing/SOLR-14718.md` row 2, evidence cell | note.** The cell still says "Not in `TESTING.md`." beside the new sentence that points to the update. The two read as contradictory on a first pass. Optional replacement for "Not in `TESTING.md`.": "Not in `TESTING.md` at the audit date."

**RC-02 | main-side response `reports/update-processing-convergence-round-6-response.md`, closing line | note, record wording.** The response says "Update processing is fully converged." This check verified the audit items and the drafts' follow-up sentences. It did not re-verify every update-processing draft. The record of the drafts is in rounds 4, 5 and 6. Optional replacement: "Every update-processing draft has a converged record in rounds 4 to 6; this round re-checked only the audit items."

**CA-LR-03 to CA-LR-06 | `reports/core-admin-8576-landing-recheck-round-1.md` | note.** These were optional and stay as recorded. They do not affect closure.

## Convergence line

CONFIRMED. The SOLR-8576 draft converges at head `883bb522caf9`. The update processing round 6 items UP-R6-01 and UP-R6-02 are closed. RC-01 and RC-02 are notes for the main side. Nothing was edited, posted, built or tested in this round.
