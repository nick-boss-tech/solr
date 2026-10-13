# Pool assignment: CombinedQueryComponent Jira ticket draft review, round 10

Capability tags: `review`. Intended host: windows. Staffing: 1 or 2 subagents. Claim: under the standing coordinator claim, `claims/coordinator-review-rounds.md`.

## Scope

Round 9 (`reports/combinedquerycomponent-ticket-review-round9.md`, at pr-prepare `8eaafeaacf`) returned STILL OPEN. Its (a) checks held, with one edit (C11, case F is at line 51, so "below" is wrong), and its (b) items all read as proposals. Its (c) sweep found eleven items, S1 to S5 and B1 to B6, each with a replacement text. Its open items were A1 (the C11 citation), A3 (whether cases X1 and X2 used one sub-query), and a note on case P (whether it set `maxHitsTerminatedEarly`).

This round applies the (c) replacements and the A1 citation, with the corrections below, and re-checks them. It also records the two items that need the probe source, which this review cannot settle.

## Edits allowed this round

Apply these in `material/combinedquerycomponent-ticket-draft.md`, each replacement taken from round 9's report, which is the record. Round 9's report gives the passage and the replacement for each.

- S1 (line 21), S2 (line 22), S3 (line 28), S4 (line 28), S5 (line 40).
- B1 (line 48), B2 (line 63), B3 (line 66), B4 (line 68), B6 (line 70).
- A1 (line 66): "fallback case below" becomes "fallback case F in Reproduction".

Two corrections to round 9's replacements:

- **B5 (line 70) must not say the sibling ticket is filed.** Round 9's text says the sibling "is to be covered by its own ticket, filed before this one". The sibling draft, `material/mergeids-ticket-draft.md`, is marked NOT filed, and so is this ticket. Replace the sentence with: "The sibling defect in {{QueryComponent.mergeIds}}, which counts a skipped partial shard in the merged totals and whose {{approximateTotalHits}} fallback adds the running total (from code reading, not run in this ticket), is drafted separately and is not yet filed; this ticket is intended to follow it." Do not describe the sibling's status as anything other than drafted and not filed.
- **Check every replacement against the code at `d76fc14b9857` before applying it.** Round 9 cites line numbers that the coordinator has already checked for lines 335, 341, 343, 348 to 350, 441 to 442, 448, 479 to 487 and 434. Read any other line the replacement cites.

For each change, list the passage before and after in the report. Commit the draft edits in the same push as the report, as Nick Shanin, no trailers, no em dashes.

## Items this review cannot settle

These need the probe source or a run on the main side. Do not guess them, and do not apply a label that says they are settled.

- **A3, cases X1 and X2 and sub-query count.** The premise record (`reports/combinedquerycomponent-premise.md`, line 153) defines X1 and X2 by "both per-query lists", without stating the number of sub-queries. Line 22 scopes the exactness claim to a single sub-query. If the probe ran one sub-query, the draft should say so at lines 48 and 52. The main side must confirm this from the probe source.
- **Case P and `maxHitsTerminatedEarly`.** The premise record (line 112) does not say whether case P set `maxHitsTerminatedEarly`. That flag decides whether the merged `approximateTotalHits` reaches the response at all (`CombinedQueryComponent.java` lines 479 to 487). The main side must confirm it from the probe.

Until both are answered, the verdict cannot be SATISFIED. If the reviewer can close the rest of the round, record the two items as open in the report, with the exact replacement text each one needs once the probe answers it.

## What to check

Read the draft at the pr-prepare tip that carries this assignment.

(a) **Confirm each applied edit in substance**: the sentence it was written for, the label, and any code claim against `d76fc14b9857`.

(b) **Confirm the fix-design items on lines 62 and 66 still read as proposals.** Round 9 found they do. Any present-tense factual claim in them is rephrased or labelled, as before.

(c) **Repeat the sweep for unlabelled factual claims over the whole ticket text.** Anything found that is not in round 9's list is reported with its passage and replacement, and is not applied this round.

(d) **Verdict.** SATISFIED only if (a) and (b) hold, (c) finds nothing unlabelled, and A3 and the case-P note are both answered from the probe. Otherwise STILL OPEN, with the exact remaining items.

On SATISFIED, the ticket text is final. The consolidated new-ticket email goes out only then, and this round does not send it.

## Deliverable

`reports/combinedquerycomponent-ticket-review-round10.md`, verdict first, one line per item under (a) and (b), the (c) sweep result, the two probe items with their status, and, for anything open, the exact passage and the replacement text. Draft edits are committed in the same push, each listed before and after.

## Limits

The draft may be edited only as listed under "Edits allowed this round". No PR, comment, Jira or email. No build, test or gate. Commit as Nick Shanin, no trailers, no em dashes.
