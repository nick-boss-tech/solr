# Assignment: standing owner calls, round 2

Status: DONE

Capability tags: [review, decision, draft]. Intended hosts: the review agent, or a lane coordinator on either side. Posted by the main side, 2026-10-15, under Nick's decision-routing instruction of 2026-10-14: an item that would otherwise wait for his decision is posted as a pool assignment and settled under the decision ladder, or settled on the main side under the decision default.

This round has one item: SOLR-15358. It was item 5 of `assignments/pool-standing-calls-round-1.md`, but round 1's brief covered items 1 to 4 only, so it was not worked (`reports/standing-calls-round-1.md` states this in its limits). Round 1 settled its four items; this round carries the fifth, unchanged in substance.

Claim: before starting, file a claim under `claims/` naming this assignment and the item (for example `claims/pool-standing-calls-round-2-1.md`), per WORKFLOW.md. Heartbeat and staleness follow the pool rules.

## How the item settles

Work the decision ladder in order:

1. Where information is missing, get more information: read the branch, the ticket, and the records named in the item before deciding anything.
2. Where the cost is effort, do the hard thing.
3. Where the item is optional, defer it as a named follow-up, recorded where a reader of the draft will find it.
4. Where the item is a genuine choice between defensible behaviors, pose it as a Choice in the draft rather than settling it silently in code.
5. Only an item that survives all four rungs returns to Nick, and the settlement note states which rung it survived and why.

A settlement is: the decision and its reasons recorded in the item's category record (the file the item names), any draft or record change the decision implies, and a closing note in the claim file. The branch tip below was verified by ls-remote from the main side on 2026-10-15; re-verify before relying on it, and stop and record if the tip has moved.

Limits: this assignment authorizes no PR opening, no ready flip, and no comment post, on any platform. A settlement that ends in "open it" or "post it" is recorded as a recommendation in the category record and stops there; openings and posts stay with Nick's explicit approval. No branch is deleted under any settlement; a settled branch that goes nowhere is banked or labeled, never deleted.

## Item 1: SOLR-15358, the docValues-as-stored call

- Branch: `solr-15358-submit`. Live tip verified 2026-10-15: `cbb345f2e7df452e20036d357c82cc63db96ffd3`.
- State: the record is otherwise closed. `reports/schema-recheck-15358-record-close-round-1.md` closed the remaining record items and names the holds that were left: the owner call on the default docValues as stored setting, and landing after SOLR-15357. The landing order is sequencing context for this item, not part of its question: SOLR-15358 lands after SOLR-15357. Draft: `pr-drafts/schema-analysis/SOLR-15358.md`; receipt: `receipts/SOLR-15358.md`. The branch changes `CurrencyFieldType` to build its amount and currency sub-fields with `createFields`, so a sub-field type configured with `docValues="true"` gets its docValues written; the draft notes that documents indexed before the change carry no docValues for these sub-fields until reindexed.
- Question: settle the default docValues as stored setting: what a sub-field that does not declare docValues ends up with under this change, and whether that outcome is the one the branch should ship.
- Settlement shape, one of two, decided by the evidence:
  - If the call is a genuine choice between defensible behaviors (for example, matching how other multi-valued or sub-field types treat docValues as stored by default, against changing no default at all), pose it as a Choice in the draft with the implemented position stated, and record the settlement and the Choice's wording in the schema category record.
  - If the call is a narrow-scope call (the behavior follows from the change's own scope, with no defensible alternative a maintainer would plausibly pick), record it in the draft's Limits with a follow-up offer, and record the settlement in the schema category record.
- Either way the record reads closed on this item afterwards: the draft carries the settled statement or the Choice, and the category record carries the decision and its reasons.

## Deliverable

The settled item: its decision and reasons in the schema category record, the draft change the decision implies (a Choice section or a Limits entry, per the settlement shape above), and the claim file closed with the outcome. If the item returns to Nick, the claim file names the rung it survived and the reason.
