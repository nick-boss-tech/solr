# Schema re-check, RC-S5 on the SOLR-15358 record, round 1

Verdict: **RC-S5 is CLOSED.** The corrected line 14 of the schema round 2 record matches the replacement, agrees with its Item 3, and the draft is unchanged with its two holds intact. The settlement's claim that the SOLR-15358 record set is "fully closed" is not supported. Three record items from earlier rounds are still open, and this round did not check their content.

Scope: `d00d0c5ede9` (the RC-S5 correction and the settlement note), checked against `reports/schema-convergence-round-2.md`, the draft `pr-drafts/schema-analysis/SOLR-15358.md`, and the branch head `cbb345f2e7df452e20036d357c82cc63db96ffd3` (ls-remote confirmed). The claim is `claims/pool-recheck-15358-rcs5.md`. Two read-only subagents. Nothing was edited, posted, built or tested.

## Closed

- **RC-S5 (schema round 2 record, line 14): CLOSED.** The line reads "The Proof placeholder closed on 2026-10-12 (see Item 3), and the owner call and the land-after-SOLR-15357 hold stand." It agrees with Item 3 at line 34, and no other line of that record still calls the placeholder open.
- **The draft.** No "TO FILL" text remains, the internal header lists the two holds, and the draft is unchanged since `8dacbfb0155`.
- **The branch head.** The fork tip for `solr-15358-submit` is the head the draft names.
- **The settlement's quotes.** Its before and after text for line 14 match the record.

## Not closed: the "fully closed" claim

The settlement says "The SOLR-15358 record set is now fully closed". The round 2 record and the round 1 response still hold these open items:

- **SCH-R1-16 (round 2 record, line 34).** The record says "SCH-R1-16 is still open". This round did not check the item's content.
- **SCH-R2-03 and SCH-R2-04 (round 1 response, lines 56 and 57).** The round 2 record calls these response descriptions wrong, and they are not yet corrected. Line 56 still says "schema version 1.5 or earlier", and line 57 still says "can merge and store them".
- **The two holds.** The owner call on the default docValues as stored setting and landing after SOLR-15357 remain open by design.

## Correction to my earlier report

The record-close report (`reports/schema-recheck-15358-record-close-round-1.md`) said RC-S5 was "the only item left in this lane". That was wrong. SCH-R1-16 and the SCH-R2-03 and SCH-R2-04 corrections were already open in the record, and I did not list them. This report corrects that statement. It does not reopen RC-S1, RC-S3 or RC-S5.

## Notes (not blocking)

- The settlement file uses the words "receipt" and "vm2" in its own explanation. That file is internal, so this does not affect outbound text.

## Not checked

- The content of SCH-R1-16, which lives in the round 1 register and was not in this round's scope.
- The owner ruling on the default docValues as stored setting.

## Convergence line

RC-S5 is CLOSED. The SOLR-15358 draft holds: the Proof is filled and matches the receipt, and two owner holds remain. The record set is not fully closed: SCH-R1-16 and the SCH-R2-03 and SCH-R2-04 response corrections are still open, and they need their own check before the lane can be called closed. Nothing was edited, posted, built or tested in this round.
