# Assignment: SOLR-6831 refinement stop on omitted partial answers, round 2

Capability tags: [gate]. Intended host: vm2 (Linux, gate backlog lane). Windows does not build. Posted by the Windows review lane, 2026-10-15, at Nick's direction after review of live PR #5134. The PR is a draft, and its head is now `3e07d801df5`, pushed the same day.

Claim first: file a claim under `claims/` naming this assignment (for example `claims/pool-vm2-gate-backlog-6831-omitted-refinement.md`), per WORKFLOW.md. Heartbeat and staleness follow the pool rules.

## Grounding

- Gate job: `gates/SOLR-6831-omitted-refinement-round-2.md`. It holds the steps, the decision rules, and the run record. Follow it as written.
- Head: `3e07d801df598573823cc997bbabb848fa2b1cfb` on `solr-6831-submit`, one commit on `2ee19dc646329f21ef630df33ae9ebab7e239cbc`. The patch file `gates/SOLR-6831-omitted-refinement-round-2.patch` is the same change. The commit was written on the Windows lane and has not been built or run there.
- Why: `FacetComponent.refinePivotFacets` treated only a shard refinement answer with `partialResults=true` as partial. A shard that answers `"omitted"` (partial results disallowed) was not treated as partial, so the coordinator could keep queuing pivot refinements. The change uses `SolrQueryResponse.haveCompleteResults`, which treats both values as incomplete, as `QueryComponent` already does.
- The change also adds `testPivotFacetRefinementStopsWhenOmittedPartialResults` and drops one history sentence from a test comment.

## Pushes

Agents may push fork branches (`solr-NNNN-submit` and `pr-prepare`) without asking. The PR is a draft, so its head can carry code the gate has not run yet. The receipt and the PR body say what is verified.

## Scope

1. Run the gate job's steps at the head, in order.
2. Decide by the proof leg, as the gate job's decision rules say. The head is already on the PR branch, so the decision is whether to keep it.
3. On green, push any added commits and write the receipt section, as the gate job says.

## Stop conditions

- Green path: the gate job's On green steps are done, the green head (`3e07d801df5`, or a fast-forward on top of it) is on `solr-6831-submit`, the receipt section is in `receipts/SOLR-6831.md`, and the claim is marked DONE naming the head and the proof-leg count.
- Not reproduced: the proof leg passes at the pre-fix production. Record it in the gate job, do not revert, mark the claim with the result, and stop for a decision.
- Failure: a test that executes and fails. Record the test, the message, the seed, and the log path in the gate job, mark the claim with the blocker, and stop. Do not relaunch.

## On completion

Mark the claim DONE with the outcome in one line.
