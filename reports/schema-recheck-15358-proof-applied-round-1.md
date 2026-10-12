# Schema re-check, applied SOLR-15358 Proof items, round 1

Verdict: **the draft items are CONFIRMED, and the record is not yet closed.** SCH-R3-01 and SCH-R3-02 are applied in `pr-drafts/schema-analysis/SOLR-15358.md` as the round 1 report asked. The main side's response record (`reports/schema-recheck-15358-proof-round-1-response.md`) has one inaccurate sentence (RC-S1), and it leaves the stale schema round 2 line open with no recorded decision (RC-S3). The round closes when those two record items are corrected.

Scope: commit `8dacbfb0155` (the draft change and the response), checked against `receipts/SOLR-15358.md` (section "Proof record (vm2, 2026-10-12)") and the branch head `cbb345f2e7df452e20036d357c82cc63db96ffd3` (ls-remote confirmed). The claim is `claims/pool-recheck-15358-proof-applied.md`. Two read-only subagents. Nothing was edited, posted, built or tested.

## Draft: CONFIRMED

- **SCH-R3-01 (the "passed" wording): applied.** The word "passed" no longer appears for CurrencyFieldTypeTest. The sentence now reads "reported no failures on that tree as well (42 tests, 21 skipped by its currency locale assumption, 0 failures)".
- **SCH-R3-02 (one paragraph per class): applied.** The second class has its own paragraph, with the head `cbb345f2e7df452e20036d357c82cc63db96ffd3` and the date 2026-10-12, matching the receipt.
- **Scope of the change.** The diff against `06fef4fe0a0` changes only the Proof paragraph (one hunk) and adds the blank line and the new paragraph. The internal header is unchanged.
- **Hygiene.** No em dash or en dash, no process words, and no "#" followed by digits in the draft.
- **Holds.** The header still lists the two holds: the owner call on the default docValues as stored setting, and landing after SOLR-15357. The branch head resolves to the head the draft names.

## Response record: two items open

**RC-S1 | `reports/schema-recheck-15358-proof-round-1-response.md` line 7 | should-fix, record accuracy.** The response says the reported wording "already stands in the draft's head-run paragraph". It does not. That paragraph (the head-run line, line 25 of the draft) has only "21 skipped by its currency locale assumption". The words "reported no failures" and "0 failures" were not there before the fix.
Replacement for "already stands in the draft's head-run paragraph": "now stands in the draft's Proof as its own paragraph (line 29), and the 21-skipped clause already appears in the head-run paragraph (line 25)".

**RC-S3 | `reports/schema-convergence-round-2.md` line 34 | should-fix, record decision.** The line still says "the placeholder" is an open hold and that line 27 reads "[TO FILL before posting ...]". The placeholder closed when `a940d43d7c9` filled the Proof, so the line is stale. The response records no decision on SCH-R3-05. It says only that the items stand as notes for the schema track.
Replacement for the line 34 hold sentence: "The placeholder hold closed on 2026-10-12, when the Proof was filled from the proof record (`a940d43d7c9`). The holds that remain are the owner call on the default docValues as stored setting and landing after SOLR-15357."
The response must then record that SCH-R3-05 is applied, with that wording.

## Notes (not blocking)

**RC-S2 | response, line 17 | note.** The response calls SCH-R3-03, SCH-R3-04 and SCH-R3-05 "optional". The round 1 report labels only SCH-R3-03 optional, and it gives SCH-R3-04 and SCH-R3-05 as notes, not optional. The label should match the report.

**RC-S4 | response, line 21 | note.** The sentence "subject to the schema convergence round 2 re-check already posted" relies on a round 2 record whose SOLR-15358 verdict is NOT CONVERGED (`reports/schema-convergence-round-2.md` line 14). Suggested replacement: "subject to the two holds named in the draft's header".

## Not checked

- The owner ruling on the docValues-as-stored default, which is not in this round's inputs.
- The gate log on VM2. The Proof cites the receipt, which is the named source.

## Convergence line

Draft: CONFIRMED. SCH-R3-01 and SCH-R3-02 are closed in the draft. Record: NOT CLOSED until RC-S1 and RC-S3 are applied in the response and the schema round 2 record. RC-S2 and RC-S4 are notes. Nothing was edited, posted, built or tested in this round.
