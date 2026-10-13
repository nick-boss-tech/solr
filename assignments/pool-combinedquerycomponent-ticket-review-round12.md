# Pool assignment: CombinedQueryComponent Jira ticket draft review, round 12

Capability tags: `review`. Intended host: windows (review coordinator). Staffing: 1 subagent. Claim: under the standing coordinator claim, `claims/coordinator-review-rounds.md`.

## Scope

Round 11 (`reports/combinedquerycomponent-ticket-review-round11.md`) returned STILL OPEN. Its open items are O1, O2, O3 and S1 to S7, each with its replacement text, and one probe item, A5. The probe facts are now exported into this repository (see (1) below), so A5 is answered and no fact for this ticket lives outside the repository anymore. This round needs no main-side input. Its scope is the three parts below and nothing broader.

## What to do

Read the draft at the pr-prepare tip that carries this assignment. Check any code claim against main `d76fc14b9857`.

**(1) Take the probe facts from the export.** `material/combinedquery-probe-export/PROBE-SUMMARY.md` summarizes the probe per case, and the same directory carries the probe source (`CombinedQueryComponentMergeTotalsTest.java`) and the run's JUnit XML. A5 is answered there: in both M1 and M2 the flag is SET (M1 and M2: FLAG SET), and each test read the merged approximateTotalHits from the response header. Use the export, not memory or earlier reports, for every probe fact this round relies on.

**(2) Apply round 11's replacement items under the patch loop**, verbatim as `reports/combinedquerycomponent-ticket-review-round11.md` prescribes them. The items, with draft line numbers as round 11 recorded them:

- O1, line 20. Replace "(15 instead of 5)" with "(15 instead of 5, confirmed by run, case P2 below)".
- O2, line 66. Replace the parenthetical "(case X1 confirms this by run; the single-sub-query scope is from code reading, not run)" with "(case X1 confirms this by run for one sub-query; that the one-line change settles the single-sub-query case is from code reading, not run)".
- O3, line 68. A5 is answered FLAG SET, so take the FLAG SET variant: insert "read from the response header, which carries it only when a shard sets maxHitsTerminatedEarly" after "(run result" in the merged-totals sentence, so the label reads "(run result, read from the response header, which carries it only when a shard sets maxHitsTerminatedEarly ...)". Keep the rest of that label as it stands.
- S1, line 12 (title). Replace the title with "CombinedQueryComponent.mergeIds counts a skipped partial shard that sends a per-query entry in the per-query totals, adds the running total as the approximateTotalHits fallback, and folds hitCountIsExact inverted (from code reading; run results in Reproduction)".
- S2, line 64. Append " (from code reading, not run)" after "lines 1173 to 1174".
- S3, line 64. Append " (from code reading, line 315)" after "The flag is one flag shared by every query".
- S4, line 64. Replace "so that for the lists a single sub-query contributes, the merged flag is true only when every contributing list reports its count exact" with "so that, for the lists a single sub-query contributes, the merged flag would be true only when every contributing list reports its count exact".
- S5, line 66. Replace "which is a fold over every shard that sends a per-query entry (from code reading, not run) and cannot be un-folded without per-shard tracking." with "which is a fold over every shard that sends a per-query entry and cannot be un-folded without per-shard tracking (from code reading, not run)."
- S6, line 70. Replace "counts a skipped partial shard in the merged totals (from code reading, not run in this ticket) and whose approximateTotalHits fallback adds the running total" with "counts a skipped partial shard in the merged totals and whose approximateTotalHits fallback adds the running total (from code reading, not run in this ticket)".
- S7, line 48. Replace "X1 (all shards exact, merged flag false) and X2 (all shards inexact, merged flag true)" with "X1 (one sub-query; all shards exact, merged flag false) and X2 (one sub-query; all shards inexact, merged flag true)".

Apply each edit in the same push as the report and list it before and after. If a passage no longer matches its recorded "before" text, say so in the report and apply the prescription to the matching passage, or record why it cannot be applied.

**(3) Confirm the whole draft once** against the code at main `d76fc14b9857` and the probe export, factual claims and labels only, under the settled labelling scope. The scope ruling in the round 7 settlement (`reports/combinedquerycomponent-ticket-review-round7-response.md`) governs: the labelling rule covers factual claims about current behavior; fix proposals are out of scope provided they read as proposals; a fix-design sentence that also asserts something about today's code carries a factual claim, and the rule applies to that part.

## Verdict

SATISFIED makes the ticket text final and releases the consolidated new-ticket email (the mergeIds and gradlew ticket texts are already final). This round does not send it.

STILL OPEN may list only items that are factually wrong against the code at main `d76fc14b9857` or against the probe export, each with its exact replacement text. Label-placement preferences beyond round 11's list are not new findings and do not hold the verdict open.

## Deliverable

`reports/combinedquerycomponent-ticket-review-round12.md`, verdict first, one line per item under (2) with before and after text, the A5 answer as taken from the export under (1), and the (3) confirmation result.

## Limits

The draft may be edited only as directed under (2). No PR, comment, Jira or email. No build, test or gate; the probe is not re-run, its export is the record. Commit as Nick Shanin, no trailers, no em dashes.
