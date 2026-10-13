# Pool assignment: CombinedQueryComponent Jira ticket draft review, round 11 (confirmation)

Capability tags: `review`. Intended host: windows (review coordinator). Staffing: 1 subagent. Claim: under the standing coordinator claim, `claims/coordinator-review-rounds.md`.

## Scope

Round 10 (`reports/combinedquerycomponent-ticket-review-round10.md`) returned STILL OPEN. The main side then settled its open items in `reports/combinedquerycomponent-ticket-review-round10-response.md`: it answered the two probe items from the probe source (A3: cases X1 and X2 each ran one sub-query, now stated at draft line 52; A4: case P did set `maxHitsTerminatedEarly`), and it applied round 10's (c) items to `material/combinedquerycomponent-ticket-draft.md`, with one exception: Q1 was held, with the reason recorded in the settlement. This round is the confirmation round for that settlement. Its scope is the four checks below and nothing broader.

## What to check

Read the draft at the pr-prepare tip that carries this assignment. Check any code claim against main `d76fc14b9857`.

(a) **Confirm the settlement's applied edits.** Each edit the round 10 response lists as applied (P1 to P5 and Q1 to Q4 applications, including the two "(one sub-query)" statements at line 52 and B3/Q2; P4 rests on the A3 answer, and Q1 is held and judged under (b)) is present in the draft and correct in substance: the sentence it was written for, its label, and any code claim it carries.

(b) **Judge the Q1 hold expressly.** The settlement held round 10's Q1 replacement (draft line 64) because the A3 probe answer cures the defect the replacement addressed: line 52 now states that X1 and X2 each ran one sub-query, so the sentence "this change settles the single-sub-query case, which the reproduction pins" has its support in the text. Applying the replacement would insert a statement the record refutes (it calls the sub-query count an open probe item). The round either accepts the cure, or directs the Q1 downgrade anyway, with its reasoning stated. If the round directs the downgrade, it applies round 10's recorded replacement under the patch loop in the same push and confirms the edited sentence in its report.

(c) **A1, draft line 40.** Round 10 prescribed a qualifier for the sentence "{{queryNumFound}} at line 384 already includes the current shard": the qualifier is "when that shard sent a per-query entry". That prescription was not a (c) item, and the settlement did not apply it. This round applies it under the patch loop and confirms the edited sentence in its report, or records why not.

(d) **One final sweep for factual claims only**, under the settled labelling scope. The scope ruling in the round 7 settlement (`reports/combinedquerycomponent-ticket-review-round7-response.md`) governs: the labelling rule covers factual claims about current behavior; fix proposals are out of scope provided they read as proposals; a fix-design sentence that also asserts something about today's code carries a factual claim, and the rule applies to that part.

## Verdict

SATISFIED makes the ticket text final and releases the consolidated new-ticket email (the mergeIds and gradlew ticket texts are already final). This round does not send it.

STILL OPEN must list only items that are factually wrong or unlabelled factual claims, each with its exact replacement text. Nothing else holds the verdict open.

## Deliverable

`reports/combinedquerycomponent-ticket-review-round11.md`, verdict first, one line per item under (a), the Q1 judgment under (b) with its reasoning, the A1 outcome under (c), and the (d) sweep result. Any draft edit (the A1 application under (c), or a directed Q1 downgrade under (b)) is committed in the same push, listed before and after.

## Limits

The draft may be edited only as allowed under (b) and (c). No PR, comment, Jira or email. No build, test or gate. Commit as Nick Shanin, no trailers, no em dashes.
