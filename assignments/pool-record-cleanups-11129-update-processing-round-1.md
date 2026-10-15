# Pool assignment: small record cleanups for SOLR-11129 and the update-processing summary, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: one read-only subagent. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

Two small record items were noted in earlier rounds and not yet settled.

1. **SOLR-11129 offset caveat.** The draft's offset bullet says "a distributed page can still differ from a single-node page". That point rests on the code design, and the receipt does not state it for the gated head `dc82676b8096`. The receipt's top section should carry it, or the draft should say it is a design consequence.
2. **Update-processing closing sentence.** `reports/update-processing-convergence-round-6-response.md` says "Update processing is fully converged." Round 6 re-checked the audit items and the drafts it named, not every update-processing draft. The record should say what was checked.

## Ladder

For item 1, get the information first: confirm from the code at `dc82676b8096` that each shard applies the offset to its own results (`solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java` around line 1404). Then either add the statement to the receipt's top section (a main-side record, so propose the exact line in the report and do not edit the receipt) or reword the draft's bullet to say it is a consequence of how shards apply the offset. For item 2, reword the closing sentence to what was checked, using the exact text in the report.

## Deliverable

`reports/record-cleanups-11129-update-processing-round-1.md`: the verdict for each item, the code lines read, the exact before and after text, and the ladder rung used. Apply the item 1 draft wording change if you choose the reword route, and say so. Do not edit receipts or the response record; propose those edits as exact text for the main side.

Rules: no builds, no tests, no Gradle, no posting. WORKFLOW.md binds (claim before work, hourly heartbeats, mark the claim DONE in the same push as the deliverable).
