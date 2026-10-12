# Record cleanups, SOLR-11129 and the update-processing summary, round 1

Verdict: **item 1 keeps its sentence, and item 2's closing sentence overstates the record.** The SOLR-11129 residual is shown by the code at the gated head, so the draft's sentence stays and the receipt should carry the mechanism. The update-processing closing sentence should be reworded to what the rounds show. Both changes are proposed as exact text for the main side. No receipt or response record was edited here.

Scope: `assignments/pool-record-cleanups-11129-update-processing-round-1.md`. Claim: `claims/pool-record-cleanups-11129-update-processing-round-1.md`. One read-only subagent, as the assignment names. The code was read at the SOLR-11129 gated head `dc82676b8096`.

## Item 1: SOLR-11129 offset caveat — KEPT, receipt line proposed

- **Rung.** Information from the code. The code shows the residual, so the sentence stays in the draft unchanged.
- **Code.** At `dc82676b8096`:
  - `FacetComponent.java` lines 1404 to 1406: the coordinator keeps the request-level offset only.
  - `FacetComponent.java` lines 554 and 580: shard requests drop the request-level offsets, so a local offset stays in `facet.field`.
  - `SimpleFacets.java` line 191: shard parameters are the local parameters layered over the global ones, local first.
  - `SimpleFacets.java` lines 442 and 1100: each shard skips its own first offset terms.
  - `DistributedFacetLocalParamsOffsetTest.java` line 37: every shard holds identical terms, so the gated test does not reach the residual. That is why the receipt does not state it.
- **Proposed receipt line for the main side.** Add after `receipts/SOLR-11129.md` line 8: "Residual at this head: each shard applies a local facet.offset to its own terms (SimpleFacets.java:191, 442, 1100). The coordinator does not skip again (FacetComponent.java:1404-1406), so a distributed page can differ from a single-node page when shards hold different terms."

## Item 2: update-processing closing sentence — REWORD proposed

- **Finding.** Round 6 itself returned "NOT CONVERGED overall" (`reports/update-processing-convergence-round-6.md`). The closing sentence "Update processing is fully converged" overstates it.
- **Counts across the rounds.** Rounds 2 to 4 and round 5 give CONVERGED verdicts to 26 drafts between them. SOLR-3657 and SOLR-14718 have no CONVERGED verdict, and round 6 says only that they "converge on their text". Two drafts changed after their round 3 verdict and have not been re-checked: SOLR-13265 (commit `4b95c02224f`) and SOLR-13943 (commit `a73269c850d`). The SOLR-13943 change was re-checked this round, and SOLR-13265 was not.
- **Exact before text** (`reports/update-processing-convergence-round-6-response.md`): "Update processing is fully converged."
- **Exact after text:** "Update processing is not fully converged. Round 6 returned NOT CONVERGED overall, and its UP-R6-01 and UP-R6-02 applications are not yet re-checked. Rounds 2 to 5 give 26 of 28 drafts a CONVERGED verdict. SOLR-3657 and SOLR-14718 have no CONVERGED verdict, and SOLR-13265 changed after its round 3 verdict and has not been re-checked."

## Ladder

Item 1 was settled by reading the code, which is rung 1. Item 2 needed the record counts, which are also rung 1. Neither needed a deferral.

## Open for the main side

- SOLR-13265 has changed since its round 3 verdict and needs a re-check. That is a new assignment, not a record edit.

## Convergence line

Item 1: the draft sentence stays, and the receipt should carry the residual. Item 2: the update-processing summary should be reworded as above. Nothing was edited in the receipts or the response record.
