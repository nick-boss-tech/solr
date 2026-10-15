# Pool assignment: CombinedQueryComponent Jira ticket draft review, round 8 confirmation and scope close-out

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1 or 2 subagents. Claim: under the standing coordinator claim, `claims/coordinator-review-rounds.md`.

## Scope

Round 7 (`reports/combinedquerycomponent-ticket-review-round7.md`) confirmed the five round 6 edits (d1 to d5) and returned STILL OPEN. Its full sweep applied three more edits to `material/combinedquerycomponent-ticket-draft.md`, and it found five fix-design items that round 6 had left outside the sweep's scope and parked for a scope decision.

The main side's settlement, `reports/combinedquerycomponent-ticket-review-round7-response.md`, rules on that scope question. The ruling, in brief: the labelling rule governs factual claims about current behavior. Sentences that propose or predict a fix are proposals, not factual claims, and are outside the rule's scope, provided they read as proposals (conditional or future phrasing). A fix-design sentence that also asserts something about today's code carries a factual claim, and the rule applies to that claim. A prediction phrased in the present tense, so it could be read as fact, is either rephrased into proposal form or takes the label. The settlement is the governing record; this assignment applies it.

## What to check

Read the draft at the current pr-prepare tip.

(a) **Confirm round 7's three applied edits**, each in substance, in the sentence it was written for, and against the code at `d76fc14b9857` where the edit asserts code behavior:

- Line 44 (firm label): "Nothing in the skip depends on a total (from code reading, not run)".
- Line 56 (firm label): "the folds at lines 342 and 343 fire on every merge in which a shard sends a per-query entry (from code reading, not run)".
- Lines 20 and 56 (accuracy): the approximate wording, "the approximateTotalHits value carried in its response header, or with no header the running total, can still be added at the merge for it (from code reading, not run)"; a shard with no per-query entry still reaches the approximate block at lines 381 to 387, and with no header the fallback at line 384 adds the running total.

(b) **Check the five fix-design items from round 7's sweep for proposal phrasing.** The items, all in Fix options (draft lines 62 and 66; round 7's report quotes each with a proposed replacement):

1. Line 62: "...so a skipped shard never enters that query's totals."
2. Line 62: "after the reorder, a query whose shards are all skipped contributes a count of zero."
3. Line 66: "Correcting the totals when the skip fires instead means reconstructing what the skipped shard added after the fact".
4. Line 66: "Testing the skip before accumulating leaves each query's totals describing the same set of shards that query merges."
5. Line 66: "the fallback fires for complete shards too, not only skipped ones, so a reorder alone leaves the double count."

For each item: if it already reads as a proposal (conditional or future phrasing), say so and leave it. If it reads as a present-tense factual assertion, fix it in the draft under the patch-loop directive: rephrase it into proposal form, or attach the label where the sentence also asserts current behavior, and commit the draft edits in the same push as the report. Round 7's proposed replacement texts are on record in its report and may be used where they fit the ruling. List every change, before and after. Item 5 is split: its first clause asserts current behavior and is judged under (c) as well; its second clause is a prediction and is judged here.

(c) **Repeat the full sweep for factual claims only**, under the classification the settlement sets: every factual claim about current behavior in the ticket text is labelled, stated as a run result, scoped, or stated as not settled. Fix-design sentences are not swept as factual claims; they are covered by (b) only. For anything the sweep finds, give the exact passage and the replacement text; do not apply (c) findings in this round.

(d) **Verdict.** SATISFIED if (a) holds, (b) is clean or patched clean, and (c) finds nothing unlabelled. Otherwise STILL OPEN, with the exact remaining items and their replacement texts.

On SATISFIED, the ticket text is final. It is the last of the three texts for the consolidated new-ticket email (the mergeIds and gradlew texts are already final), and the email goes out once this round returns SATISFIED.

Out of scope: the gate job wording (its owner is the gate coordinator), the probe source, round 7's optional Reproduction lead-in note (settled as not applied in the settlement record), and anything rounds 1 to 7 confirmed beyond item (a).

## Deliverable

`reports/combinedquerycomponent-ticket-review-round8.md`, verdict first: SATISFIED or STILL OPEN, with one line per item under (a) and (b), the (c) sweep result, and, for anything open, the exact passage and the replacement text. Any (b) draft edits are committed in the same push, each listed before and after.

## Limits

The draft may be edited only for item (b). No PR, comment, Jira or email. No build, test or gate. Commit as Nick Shanin, no trailers, no em dashes.
