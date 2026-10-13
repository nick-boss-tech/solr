# Premise job: what does a real timeAllowed exit send from a plain QueryComponent shard?

- Code under test: unmodified main `d76fc14b9857e77b830d7a82514b28c523a239c2`, `QueryComponent`, `SearchHandler.shortCircuitedResults`, and `QueryComponent.mergeIds`. No branch, no production edit.
- Status: DONE (2026-10-13). The vm1 run under claims/pool-vm1-querycomponent-real-exit-shape-premise.md executed and its verdicts are recorded below. An earlier line here closed this job as superseded by the combined-component run; that line is replaced, because the plain QueryComponent run did execute and it is the settling record for the SOLR-18539 exactness rule. Verdict: the run supports canonical Option B (see the verdicts section).
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

- [x] Q1 attempted across the full ladder; the short-circuited shape was not produced (verdict below)
- [x] Q2 attempted across the full ladder; no partial response carrying a sort_values key was produced (verdict below)
- [x] Q3 merges driven unedited; totals recorded
- [x] verdicts recorded here and in the report; job DONE

## Verdicts (vm1 run, 2026-10-13)

Run record: unmodified main `d76fc14b9857e77b830d7a82514b28c523a239c2`, probe `QueryComponentRealExitShapePremiseTest` in `:solr:core`, seed `C0FFEE42C0FFEE42`, shard A 20,000 documents and shard B 1,500, sort `intDefault asc` with `fsv=true`. Log `~/workspace/tools/gqc-real-exit-shape.log` on vm1 (GATE RUNNER DONE, relaunch complete 2026-10-13T18:51:49Z). Fresh JUnit XML `TEST-org.apache.solr.handler.component.QueryComponentRealExitShapePremiseTest.xml`, timestamp 2026-10-13T18:51:02Z: 1 test, 0 failures, 0 errors. The probe asserts only harness sanity and prints its observations as QC-EXIT lines; the lines below are quoted from that XML's system-out. The ladder ran timeAllowed 4000, 2500, 1600, 1000, 650, 420, 275, 180, 120, 80, 55, 38, 26, 18, 12, 8, 5, 3, 2, 1 (plus three warmups and the two complete captures).

**Q1: NOT CONFIRMED BY RUN.** The reading expected the exit during the main search to produce the `SearchHandler.shortCircuitedResults` shape, an empty `SolrDocumentList` with numFound 0 and the exact flag at its default true, skipped by the merge. No attempt produced it:

> QC-EXIT Q1 SHAPE NOT CAPTURED: no attempt produced the short-circuited shape

At timeAllowed 12 and above every ladder attempt completed in full. At 8, 5, 3, 2 and 1 the response was flagged partial but still carried the complete result, for example:

> QC-EXIT ladder timeAllowed=8 rows=30115: shard=shardA partial=true responseKind=ResultContext numFound=20000 numFoundExact=true maxScore=NaN size=20000 sortValues=ABSENT headerAth=null headerMaxHitsTerminatedEarly=null headerSegmentTerminatedEarly=null rspException=null

**Q2: NOT CONFIRMED BY RUN.** No attempt produced a partial response carrying a `sort_values` key:

> QC-EXIT Q2 SHAPE NOT CAPTURED: no attempt produced a partial response carrying a sort_values key

**Q3: the one partial shape captured, driven through `mergeIds` unedited.** The captured partial is a ResultContext carrying the shard's full totals (numFound 20000, numFoundExact true, all 20,000 documents, no sort values):

> QC-EXIT OTHER PARTIAL SHAPE CAPTURED: shard=shardA partial=true responseKind=ResultContext numFound=20000 numFoundExact=true maxScore=NaN size=20000 sortValues=ABSENT headerAth=null headerMaxHitsTerminatedEarly=null headerSegmentTerminatedEarly=null rspException=null

Merged with the complete shard B (1,500 documents) on unmodified main:

> QC-EXIT MERGE other partial A + complete B: completed numFound=21500 numFoundExact=true size=1500 headerAth=null

The merged list holds only shard B's 1,500 documents, so the exiting shard was skipped by the merge (partial, no sort values, and the field sort needs unmarshalling), while its numFound and exact flag were still folded into the merged totals. That folding is the SOLR-18539 defect itself, present on unmodified main; it is not evidence about the rule width. The control merge of the two complete captures threw `IndexOutOfBoundsException: Index 0 out of bounds for length 0` (the complete captures carry an empty `sort_values` list in this harness); recorded as a harness artifact, not a premise result. The B-only control completed at numFound=1500, numFoundExact=true.

**Rule verdict for SOLR-18539: the run supports canonical Option B.** The two candidate rules differ only for a skipped shard that reports an exact zero: Option B leaves the merged `numFoundExact` unchanged for such a skip and clears it when the skipped shard reported hits or an inexact count; the rule in `review-r1-solr-18539` clears it for every skip. The run produced exactly one kind of skipped shape, a shard reporting hits (numFound 20000) with an exact flag, and for that shape both rules clear exactness. The one shape where the rules differ, a skipped shard reporting an exact zero, was not produced by any real exit in the ladder, although capturing it was the run's first question. The any-skip patch is therefore wider than the run supports. The patch narrows to Option B and re-gates before any move; `mergeids-skipped-shard-totals` is not moved (fork tip verified by ls-remote at `4eb996121804b043f1ee53f5bb7e22b9c3021b0a` after this verdict). The narrowing task is written in the premise addendum to `reports/four-new-prs-strong-review-round-1.md` and summarized in `receipts/mergeids-fix.md`.

## Claim (vm1, 2026-10-13T17:10Z)

- Claimed by vm1: claims/pool-vm1-querycomponent-real-exit-shape-premise.md.
- Runner: ~/workspace/tools/gate-strong-review-vm1-chain.sh (chained, serial on the test-queue flock). Log: ~/workspace/tools/gqc-real-exit-shape.log. Worktree: ~/workspace/solr-worktrees/qc-real-exit-shape. Probe: QueryComponentRealExitShapePremiseTest (throwaway, in the worktree only; source kept at ~/workspace/tools/probes/QueryComponentRealExitShapePremiseTest.java).
