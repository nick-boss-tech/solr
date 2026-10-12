# Schema re-check, SOLR-15358 record close, round 1

Verdict: **RC-S1 and RC-S3 are CLOSED.** The main side's correction in `5005a5204d9` fixes both record items the applied re-check left open. One new should-fix item is on the same schema round 2 record: line 14 still says the Proof placeholder is unfilled (RC-S5). It stays open until that line is corrected.

Scope: `5005a5204d9` (the corrections to `reports/schema-recheck-15358-proof-round-1-response.md` and `reports/schema-convergence-round-2.md` line 34), checked against the draft `pr-drafts/schema-analysis/SOLR-15358.md`, the proof record in `receipts/SOLR-15358.md`, and the branch head `cbb345f2e7df452e20036d357c82cc63db96ffd3` (ls-remote confirmed). The claim is `claims/pool-recheck-15358-record-close.md`. Two read-only subagents. Nothing was edited, posted, built or tested.

## Closed

- **RC-S1 (response record, the "already stands" sentence): CLOSED.** The sentence is gone. The response now says the wording "now stands in the draft's Proof as its own paragraph (line 29)", and that the 21-skipped clause "already appears in the head-run paragraph (line 25)". Both draft lines match.
- **RC-S3 (schema round 2 line 34, the placeholder hold): CLOSED.** The line now reads "The placeholder hold closed on 2026-10-12, when the Proof was filled from the proof record (`a940d43d7c9`). The holds that remain are the owner call on the default docValues as stored setting and landing after SOLR-15357." The response records SCH-R3-05 as applied.

## Open

**RC-S5 | `reports/schema-convergence-round-2.md` line 14 | should-fix, contradiction in the same record.** The SOLR-15358 verdict line still reads "The Proof placeholder is unfilled, and the owner call and the land-after-SOLR-15357 hold stand." Line 34 of the same record now says the placeholder hold closed, so the two lines disagree. The verdict itself (NOT CONVERGED on the two holds) still stands.
Replacement for "The Proof placeholder is unfilled, and": "The Proof placeholder closed on 2026-10-12 (see Item 3), and".

## Checks that held

- The draft's internal header (line 1) lists the two holds, and no "TO FILL" text remains.
- The Proof tree in the draft (line 27) matches the receipt's proof tree: the head with only `CurrencyFieldType.java` reverted to `b5c71bc557`.
- The response's "Corrections (2026-10-12)" section names both items and the settlement file, `reports/schema-recheck-15358-proof-applied-round-1-response.md`.
- No em dash or en dash in either record, and the branch head resolves to the head the draft names.

## Notes (not blocking)

- The response's "passed" quotation is accurate for the draft before the fix at `06fef4fe0a0`. It is not accurate for `032584f2e02`, which had a placeholder, not that sentence. No change is needed; the quote points at the right commit.
- The response's line 21, "re-check already posted", relies on the round 2 record whose SOLR-15358 verdict is NOT CONVERGED. RC-S4 from the earlier round already covers this as a note.

## Not checked

- The owner ruling on the default docValues as stored setting, which is not in this round's inputs.

## Convergence line

RC-S1 and RC-S3 are CLOSED. RC-S5 is a new should-fix on the schema round 2 record, line 14, and it is the only item left in this lane. Nothing was edited, posted, built or tested in this round.
