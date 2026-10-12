# Pool assignment: CombinedQueryComponent Jira ticket draft review, round 5 confirmation

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-combinedquerycomponent-ticket-review-round5.md.

## Scope: confirmation of the round 4 settlements only

Round 4 (reports/combinedquerycomponent-ticket-review-round4.md) returned STILL OPEN: items 2 and 3 confirmed, and item 1 left open with five prescribed fixes. The main side applied all five; the record is `reports/combinedquerycomponent-ticket-review-round4-response.md`. Confirm each settlement below against that record and the draft and gate job at the current pr-prepare tip. Nothing else is in scope: do not re-open what rounds 1 to 4 confirmed, and do not edit the draft or the gate job; flag instead. Findings B2 and C1 in the round 4 report were notes marked optional and were not applied; they are not open items. The real-exit premise job (gates/combinedquerycomponent-real-exit-premise.md, with Q1, Q2 and Q3) is still open on a Linux host; this confirmation does not depend on its verdict, and the draft must still claim neither unsettled outcome when it lands.

## Items to confirm

1. **Finding A1: draft line 50 label (blocking in round 4).** The Partial skip bullet in `material/combinedquerycomponent-ticket-draft.md` now reads "...and it is not the shape a real {{timeAllowed}} exit produces (from code reading, not run; see Symptom)." Confirm the label is present with the exact placement and punctuation round 4 prescribed.
2. **Finding A2: draft line 56 label (blocking in round 4).** The first sentence of the End to end paragraph now reads "End to end, no traced shard path produces the constructed shape (from code reading, not run)." Confirm the label is present exactly as prescribed.
3. **Finding A3: draft line 20 label (should-fix in round 4).** The score-only sort sentence in Symptom bullet 1 now reads "...under a score-only sort an exit cannot stop in sort-value preparation and stops in the search itself (from code reading, not run),..." Confirm the label is present exactly as prescribed.
4. **Finding A4: draft line 56 label (should-fix in round 4).** The score-only sort sentence in the End to end paragraph now reads "...under a score-only sort the exit cannot stop in sort-value preparation, so it stops in the search itself with the same search handler products (from code reading, not run);..." Confirm the label is present exactly as prescribed.
5. **Finding B1: gate job line 31 punctuation (should-fix in round 4).** The "Expected under the code reading" line in `gates/combinedquerycomponent-real-exit-premise.md` now reads "...gives no per-query entry and no skip. The expected outcome under the code reading for Q3 is a NullPointerException at line 411..." Confirm the comma splice is gone and the rest of the line is unchanged.

With these five, also confirm that no general real-exit claim in the ticket text remains unlabelled, which was round 3's condition carried through round 4.

## Deliverable

`reports/combinedquerycomponent-ticket-review-round5.md`, verdict first: SATISFIED or STILL OPEN, one line per item above (confirmed, or the exact passage and the evidence it disagrees with, with exact replacement text for anything still open).

When this round lands SATISFIED, the CombinedQueryComponent ticket text is final, and it completes the set for the one consolidated email Nick receives carrying every new-ticket text: the mergeIds text and the gradlew Git Bash ticket are already final, and this CombinedQueryComponent ticket is the last of the three. The real-exit premise job runs separately on a Linux host; its verdict can still correct the draft's exit passages, and the main side owns that step. The reviewer does not file anything in Jira, does not create the ticket, and does not email anyone.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
