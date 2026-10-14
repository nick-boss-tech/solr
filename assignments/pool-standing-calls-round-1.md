# Assignment: standing owner calls, round 1

Capability tags: [review, decision, draft]. Intended hosts: the review agent, or a lane coordinator on either side. Posted by the main side, 2026-10-14, under Nick's decision-routing instruction of the same day: an item that would otherwise wait for his decision is posted as a pool assignment and settled under the decision ladder, or settled on the main side under the decision default.

Claim by item: before starting an item, file a claim under `claims/` naming this assignment and the item number (for example `claims/pool-standing-calls-round-1-3.md`), per WORKFLOW.md. Items are independent; take them in any order, one at a time. Heartbeat and staleness follow the pool rules.

## How each item settles

Work the decision ladder in order:

1. Where information is missing, get more information: read the branch, the ticket, and the records named in the item before deciding anything.
2. Where the cost is effort, do the hard thing.
3. Where the item is optional, defer it as a named follow-up, recorded where a reader of the draft will find it.
4. Where the item is a genuine choice between defensible behaviors, pose it as a Choice in the draft rather than settling it silently in code.
5. Only an item that survives all four rungs returns to Nick, and the settlement note states which rung it survived and why.

A settlement is: the decision and its reasons recorded in the item's category record (the file each item names), any draft or record change the decision implies, and a closing note in the claim file. Branch tips below were verified by ls-remote from the main side on 2026-10-14; re-verify before relying on one, and stop and record if a tip has moved.

Limits for every item: this assignment authorizes no PR opening, no ready flip, and no comment post, on any platform. A settlement that ends in "open it" or "post it" is recorded as a recommendation in the category record and stops there; openings and posts stay with Nick's explicit approval. No branch is deleted under any settlement; a settled branch that goes nowhere is banked or labeled, never deleted.

## Item 1: SOLR-12161, which state governs: READY TO OPEN or AWAITING PIPELINE

- Branch: `solr-12161-submit`. Live tip verified 2026-10-14: `cdd265168a2adc76fb788f3d1f5587a3eee05e42`.
- The conflict: `reports/openings-census-round-1.md` counts the branch READY TO OPEN at that tip, gate green at the live tip per the security round 2 record. The round 36 intake record (`branch-focus-inventory-2026-10-08.md`, the awaiting-pipeline list) carries it as AWAITING PIPELINE: a test-only pin (the no-credentials batch update case in `BasicAuthIntegrationTest`) registered in the pipeline intake under the intake pause. Draft: `pr-drafts/security/SOLR-12161.md`. Receipt: `receipts/SOLR-12161.md`.
- Question: which state governs the branch today? Settle one of: the census governs and the branch stands ready (the pause no longer applies to it, with the reason stated); the pause governs and the branch stays AWAITING PIPELINE (state what discharges the pause, or that nothing short of Nick lifting it does); or a narrower state that fits the evidence better.
- Main-side note, for the record: the pause was Nick's own throttle on intake, and nothing on record discharges it. That does not block a settlement: settle with reasons, including a reasoned finding that the pause is spent or does not reach this branch, if the evidence supports one.
- Settlement shape: the decision recorded in the census row's terms (an annotation in `reports/openings-census-round-1.md` or the security category record, matching wherever the next reader will look), and the draft left consistent with the decision.

## Item 2: SOLR-17866, the submit-ahead-of-design flag

- Branch: `solr-17866-submit`. Live tip verified 2026-10-14: `3f9367d796a6446fa2f0549d8e9763e07831e6e8`.
- State: the draft (`pr-drafts/solrj/SOLR-17866.md`) is written at that head: `GenericSolrRequest` becomes collection-aware when a collection is named, via a new hook in `SolrRequest.process`; the proof in the draft records `GenericSolrRequestTest` 4 of 4 at the head with two of the four failing on the base. Receipt: `receipts/SOLR-17866.md`. The flag stands in the SolrJ category records (the convergence rounds, `assignments/pool-solrj-convergence-round-1.md` and `assignments/pool-solrj-convergence-round-2.md`, and the census follow-up for SolrJ): the branch submits a design (a new protected hook and a behavior change on a public request type) ahead of any design discussion on the ticket, and the draft itself names behavior beyond the ticket (the flag staying set on the request object is read by the cloud client's admin check and by the load-balancing client's retry decision).
- Question: does the flag still describe the branch as it stands? Verify it against the current branch diff and the ticket, then settle one of: submits as-is (the flag is answered by the draft as written, with the reason); waits for the design (state what design step is missing and where it would happen); or narrows (name the narrower shape and record it as the branch's direction, without implementing it under this assignment).
- Settlement shape: the decision and the flag's disposition recorded in the SolrJ category record, and the draft adjusted to match (for example a Limits or Choice entry carrying the design question, if the settlement poses one).

## Item 3: SOLR-9637, submission shape against SOLR-17393

- Branch: `solr-9637-submit`. Live tip verified 2026-10-14: `c50fa4ffd932ede86c36b0748ee6aabe34d30e0a`, gated at that head. The branch is stacked on SOLR-17393's branch: `solr-17393-submit`, live tip verified the same day: `626241e647e5fcc5ac6cc485f6a96d4ffe12e00a`. Draft: `pr-drafts/suggester/SOLR-9637.md`, written against the stacked base. There is no receipt file for SOLR-9637; the gate record lives in the draft's Proof and the suggester category records (`audits/suggester/SOLR-17393.md` and the suggester rounds).
- The recorded options: submit SOLR-17393 first and let 9637 follow; fold both into one PR; or rework 9637 onto plain main.
- The rule already recorded for this pair: SOLR-17393 ships with the merge-only scope, and SOLR-9637 follows as its own PR once 17393 lands, rebased onto it (follow-up rule, recorded with the SOLR-17393 disposition). A settlement either confirms the branch's shape under that rule, from both branches' current states, or overturns it with reasons.
- Question: settle the submission shape. Check both branches' current states first: whether 17393's head still carries only the merge scope, whether the 9637 stack still sits cleanly on it, and what folding or rebasing would actually cost at these tips.
- Settlement shape: the shape recorded in the suggester category record and in the draft (its base and landing-order statements), so the openings slate for suggester can cite it without re-asking.

## Item 4: SOLR-16290, disposition of a gated pin

- Branch: `solr-16290-submit`. Live tip verified 2026-10-14: `ff760120c81792022458a5a290b6e58d57b9cd17`, a gated pin at that head. Receipt: `receipts/SOLR-16290.md`. The landing record: `reports/branch-review-verdicts-landing-round-1.md`.
- The recorded options: open it as a pin PR (the pin documents current facet behavior and stands as the record); fund the real facet fix instead (the pin stays banked while the fix is built); or bank the branch with no PR.
- Question: settle the disposition under the ladder. Weigh what the pin buys on its own, what the real fix would cost and whether anything on record scopes it, and what banking loses. If the settlement is "fund the fix", name the follow-up in the disposition record with its scope as far as the record supports it; building the fix is not part of this assignment.
- Settlement shape: the disposition recorded in `reports/branch-review-verdicts-landing-round-1.md` terms and in the receipt, with the branch's next step stated in one line.

## Item 5: SOLR-15358, the docValues-as-stored call

- Branch: `solr-15358-submit`. Live tip verified 2026-10-14: `cbb345f2e7df452e20036d357c82cc63db96ffd3`.
- State: the record is otherwise closed. `reports/schema-recheck-15358-record-close-round-1.md` closed the remaining record items and names the holds that were left: the owner call on the default docValues as stored setting, and landing after SOLR-15357 (a sequencing fact, not part of this item's question). Draft: `pr-drafts/schema-analysis/SOLR-15358.md`; receipt: `receipts/SOLR-15358.md`. The branch changes `CurrencyFieldType` to build its amount and currency sub-fields with `createFields`, so a sub-field type configured with `docValues="true"` gets its docValues written; the draft notes that documents indexed before the change carry no docValues for these sub-fields until reindexed.
- Question: settle the default docValues as stored setting: what a sub-field that does not declare docValues ends up with under this change, and whether that outcome is the one the branch should ship. If the call turns on a genuine choice between defensible behaviors (for example, matching how other multi-valued or sub-field types treat docValues as stored by default, against changing no default at all), pose it as a Choice in the draft with the implemented position stated, rather than settling it in code.
- Settlement shape: the decision recorded in the schema category record, and the draft carrying either the settled statement (in Limits, if it is a scope fact) or the Choice section (if it is a behavior choice), so the record reads closed on this item.

## Deliverable

Each settled item: its decision and reasons in its category record, the draft or record change the decision implies, and the claim file closed with the outcome. An item that returns to Nick is named in the claim file with the rung it survived and the reason, and is the only kind of item this assignment leaves open.
