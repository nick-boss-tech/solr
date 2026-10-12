# Pool assignment: CombinedQueryComponent Jira ticket draft review, round 4 confirmation

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-combinedquerycomponent-ticket-review-round4.md.

## Scope: confirmation of the round 3 settlements only

Round 3 (reports/combinedquerycomponent-ticket-review-round3.md) returned STILL OPEN: items 1, 2 and 5 confirmed, and three items left open. The main side settled all three; the record is `reports/combinedquerycomponent-ticket-review-round3-response.md`. Confirm each settlement below against that record, the draft at the current pr-prepare tip, the gate job, and the evidence the settlement cites. Nothing else is in scope: do not re-open what rounds 1 to 3 confirmed, and do not edit the draft or the gate job; flag instead. The real-exit premise job (gates/combinedquerycomponent-real-exit-premise.md, with Q1, Q2 and Q3) is still open on a Linux host; this confirmation does not depend on its verdict, and the draft must still claim neither unsettled outcome when it lands.

## Items to confirm

1. **Finding A: draft line 20 label.** The first sentence of Symptom bullet 1 in `material/combinedquerycomponent-ticket-draft.md` now reads "The real {{timeAllowed}} exit is not the shape that produces this (from code reading, not run)." Confirm the general claim carries the label with the exact placement and punctuation round 3 prescribed, and that no other general real-exit claim in the ticket text is left unlabelled.
2. **Finding B: gate job Q3 expected outcome.** The "Expected under the code reading" line in `gates/combinedquerycomponent-real-exit-premise.md` now states the expected outcome for Q3 as a NullPointerException at line 411, with the reasoning round 3 prescribed (`docs` null because no per-query entry assigns it at line 341, the loop at 411 calling `docs.size()` with no guard, and no earlier throw at line 399 because `sortFieldValues` defaults to an empty list at lines 394 to 396 and `needsUnmarshalling` is false). Confirm the replacement is present verbatim and matches the code at main `d76fc14b9857e77b830d7a82514b28c523a239c2`.
3. **Item 3 check record: M2 merged `numFound`.** The main side read the probe assertion on VM1 and recorded it in the round 3 response: file `solr/core/src/test/org/apache/solr/handler/component/CombinedQueryComponentMergeTotalsTest.java`, method `maximumFoldOtherQueryWinning()` (line 343), asserting at line 362 `assertEquals("M2 numFound", 5, rb.getResponseDocs().getNumFound())`. The probe source sits in the VM1 worktree and cannot be read from the review host; confirm that the recorded citation is consistent with the values the earlier rounds verified (the run addendum's M2 case and the draft's statement of a merged `numFound` of 5), and flag any inconsistency instead of re-opening the settled values.

## Deliverable

`reports/combinedquerycomponent-ticket-review-round4.md`, verdict first: SATISFIED or STILL OPEN, one line per item above (confirmed, or the exact passage and the evidence it disagrees with, with exact replacement text for anything still open).

When this round lands SATISFIED, the CombinedQueryComponent ticket text is final: the main side includes the final text of `material/combinedquerycomponent-ticket-draft.md` in the one consolidated email Nick receives carrying every new-ticket text (the updated mergeIds text, the gradlew ticket, and this CombinedQueryComponent ticket), sent only once each included ticket has been completely reviewed. The real-exit premise job runs separately on a Linux host; its verdict can still correct the draft's exit passages, and the main side owns that step. The reviewer does not file anything in Jira, does not create the ticket, and does not email anyone.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
