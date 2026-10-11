# Premise job: what does a real timeAllowed exit do to CombinedQueryComponent merge totals?

- Code under test: unmodified main d76fc14b9857e77b830d7a82514b28c523a239c2, file solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java. No branch; no production edit.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: premise-run.
- Job type: premise run only. No branch edit, no gate, no draft edit. Posted by the main side's adjudication of the CombinedQueryComponent ticket review (2026-10-11), appended to reports/combinedquerycomponent-ticket-review.md. The ticket draft (material/combinedquerycomponent-ticket-draft.md) states the code-reading account of the real exit and claims neither outcome this job settles; the main side settles the draft from the verdict when this job lands.

## Questions

**Q1.** Does a real timeAllowed exit on a shard inflate any total in `CombinedQueryComponent.mergeIds`? The code reading says the exiting shard's `numFound` and exactness are never folded for the unprepared query (there is no per-query entry to fold), while an `approximateTotalHits` value in the shard's response header can still be added at the merge (line 386). Settle by run, per exit position: does the merged `numFound`, `numFoundExact` or `approximateTotalHits` move because of the exiting shard?

**Q2.** When the exit lands on a query index above zero, the shard's per-query list is shorter than the query count, and `mergeIds` calls `docList.get(queryIndex)` at lines 339 to 340 with no size check. Does the merge fail (which exception, with what message), skip the shard, or fold something for the missing entries? Settle by run.

## Reading evidence for the spec (verified by the main side at d76fc14b9857)

- `QueryComponent.doFieldSortValues` catches `ExitableDirectoryReader.ExitingReaderException` at line 571, adds an empty `sort_values` at line 574, and rethrows at line 575.
- `CombinedQueryComponent.process` calls `doFieldSortValues` at line 210 inside its per-query loop with no catch. On the throw, line 213 (`sort_values_<q>`) and lines 215 to 219 (the per-query entry) never run for that query or for any later query.
- `SearchHandler` catches the exception at lines 507 to 509 and calls `shortCircuitedResults` (line 782), which flags partial results and adds no per-query entry.
- In `mergeIds`, the per-query entry is read at lines 338 to 340, `queryNumFound` folds at line 342 and the exactness fold at line 343, the exception continue is at lines 348 to 350, the response header is read afterwards, and the header's approximate value is added at line 386 (the fallback at line 384 when the header carries none).

## Spec

- The exit must be real, not fabricated. The shard-side shape must come from the component's own process path with a time limit that actually expires (the `timeAllowed` / ExitableDirectoryReader path), not from hand-built shard responses with entries removed. The existing scratch probe `CombinedQueryComponentMergeTotalsTest` (vm1 worktree ~/workspace/solr-worktrees/cqc-premise) fabricates responses and does not settle this; its merge-driving half may be reused for step 2.
- Step 1, produce the real shard shape. On a real core under the combined query handler, run the shard-side combined process for at least two combiner queries with a time limit set to expire during the second query's sort-value preparation (exit on query index 1), and in a second run during the first query's (exit on query index 0). Capture each shard response exactly as produced: which per-query entries exist, which `sort_values_<q>` exist, what the response header carries (partialResults, approximateTotalHits, maxHitsTerminatedEarly), and whether an exception is attached to the response. If expiring at a chosen query cannot be forced deterministically, record what was forced instead; an exit forced by a very small timeAllowed on a multi-query request, with the landing index read from the produced shape, is acceptable evidence, as long as one run lands the exit above query index zero.
- Step 2, drive the merge. Feed the captured real shard responses, unedited, through `CombinedQueryComponent.mergeIds`, alongside a complete second shard if the shape needs one, and record per exit position: merged `numFound`, merged `numFoundExact`, merged `approximateTotalHits`, and any exception (class and message).
- Run in :solr:core. Seed: C0FFEE42C0FFEE42, for continuity with the earlier probe, unless the harness needs another; record the seed used. Read outcomes from fresh JUnit XML and the run log, not from console summaries.
- Expected under the code reading: exit on query index 0 gives no per-query entry at all, no fold at lines 342 and 343 for that shard, and the header value at line 386 still added if the header carries one. Exit on query index 1 gives an entry for query 0 only, and Q2 decides what query 1 does. Any different outcome is the finding; record it exactly, with values.

## Deliverable

- Record the verdicts here, one per question (CONFIRMED BY RUN matching the reading, or the actual behavior observed, with the values), plus the produced shard shapes, the run counts, the seed, and the log and JUnit XML paths.
- Append the same verdicts to reports/combinedquerycomponent-premise.md as a real-exit addendum, in the same push that marks this job DONE. The main side then settles the ticket draft's Symptom and End-to-end passages from the verdict; do not edit the draft in this job.

## Step checklist

- [ ] real exit produced on query index 0; shard shape captured
- [ ] real exit produced on a query index above zero; shard shape captured
- [ ] captured responses driven through mergeIds unedited; totals and any exception recorded per exit position
- [ ] verdicts recorded here and appended to reports/combinedquerycomponent-premise.md; job and claim DONE
