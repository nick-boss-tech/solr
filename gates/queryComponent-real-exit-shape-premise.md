# Premise job: what does a real timeAllowed exit send from a plain QueryComponent shard?

- Code under test: unmodified main `d76fc14b9857e77b830d7a82514b28c523a239c2`, `QueryComponent`, `SearchHandler.shortCircuitedResults`, and `QueryComponent.mergeIds`. No branch, no production edit.
- Status: CLOSED as superseded (2026-10-13, vm2 on verification): the real timeAllowed exit question is covered by the vm1 run in claims/pool-vm1-combinedquerycomponent-real-exit-premise.md (DONE 2026-10-13, seed C0FFEE42C0FFEE42). Not claimable.
- Capability tags: premise-run.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Source: `reports/four-new-prs-strong-review-round-1.md`, section SOLR-18539, the case analysis for the exactness rule. The rule in `review-r1-solr-18539` (any skipped shard clears numFoundExact) rests on the shard shapes below, read from the code. This job turns the reading into a run, the way `gates/combinedquerycomponent-real-exit-premise.md` did for the combined component.
- Job type: premise run only. Throwaway probe class in a worktree; nothing is pushed.

## Questions

**Q1.** A shard-side request (`distrib=false`, `isShard=true`, `fsv=true`, a field sort such as `intDefault asc`) whose `timeAllowed` expires during the main search. What does the response carry: `partialResults`, `sort_values` (absent, or present and empty), the `response` section's numFound and numFoundExact, any `maxScore`, any `approximateTotalHits`?

**Q2.** The same request with `timeAllowed` set to expire during field sort value preparation (`QueryComponent.doFieldSortValues`, which adds an empty `sort_values` and rethrows). Same questions. Reading says this shape keeps the documents already found, so numFound is above zero.

**Q3.** Drive each captured shape, unedited, through `QueryComponent.mergeIds` with a complete second shard and a field sort. Record merged numFound, numFoundExact and approximateTotalHits on unmodified main, and whether the exiting shard is skipped.

## Reading evidence

- `SearchHandler.shortCircuitedResults` (line 779) adds `new SolrDocumentList()` when the response has no `response` section: numFound 0, and `SolrDocumentList.numFoundExact` defaults to true (`solr/solrj/.../SolrDocumentList.java`, line 50).
- `QueryComponent.doFieldSortValues` catches `ExitableDirectoryReader.ExitingReaderException`, adds an empty `sort_values`, and rethrows (premise report for the combined component, "Where the per-query sort values come from").
- `mergeIds` skips a shard when it is partial, has no sort values, and the sort needs unmarshalling. Expected: Q1 skipped with numFound 0 and an exact flag; Q2 skipped with numFound above 0.

## Spec

- Real exit, not fabricated: use the shard-side process path with a limit that actually expires (the `timeAllowed` / ExitableDirectoryReader path), as in the combined-component probe `CombinedQueryComponentRealExitPremiseTest` (vm1 worktree `~/workspace/solr-worktrees/cqc-real-exit`), whose corpus setup can be reused: shard A 20,000 documents, shard B 1,500. Read the landing position from the produced shape; if the exit cannot be forced into one phase, record what was forced.
- Seed `C0FFEE42C0FFEE42` for continuity, unless the harness needs another; record the seed used. Read outcomes from fresh JUnit XML and the run log.
- Any different outcome from the reading is the finding. Record it exactly, with values.

## Deliverable

Verdicts per question in this file (CONFIRMED BY RUN matching the reading, or the observed behavior with values), the produced shapes, run counts, the seed, and the log and XML paths. Append the same verdicts to `reports/four-new-prs-strong-review-round-1.md` as a premise addendum for SOLR-18539, in the same push that marks this job DONE.

## Step checklist

- [ ] Q1 shape captured (exit during the main search)
- [ ] Q2 shape captured (exit during sort value preparation)
- [ ] Q3 merges driven unedited; totals recorded
- [ ] verdicts recorded here and in the report; job DONE

## Claim (vm1, 2026-10-13T17:10Z)

- Claimed by vm1: claims/pool-vm1-querycomponent-real-exit-shape-premise.md.
- Runner: ~/workspace/tools/gate-strong-review-vm1-chain.sh (chained, serial on the test-queue flock). Log: ~/workspace/tools/gqc-real-exit-shape.log. Worktree: ~/workspace/solr-worktrees/qc-real-exit-shape. Probe: QueryComponentRealExitShapePremiseTest (throwaway, in the worktree only; source kept at ~/workspace/tools/probes/QueryComponentRealExitShapePremiseTest.java).
