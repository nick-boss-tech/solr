# Pool assignment: SOLR-18507 fingerprint performance benchmark, round 1

Status: HANDED (item 1 partial, item 2 blocked; reports/SOLR-18507-fingerprint-benchmark-round-1.md; claim claims/pool-solr-18507-fingerprint-benchmark-round-1.md, 2026-10-20T10:35:16Z)

- Capability tags: sweep.
- Category: Core admin and cores.
- Intended host: any pool Linux host with sweep. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: Windows implementation host, 2026-10-20, at the direction of Nick Shanin.
- Parent: SOLR-18507.
- Siblings: `gates/SOLR-18507-phase-3.md` and `gates/SOLR-18507-phase-4.md`.
- Supersedes: none.

Each item says what to do, the file to start from, and the output path. If an item needs a decision, its row in the Decisions table gives the answer or the default.

## Items

1. **Measure Phase 3 persisted-summary costs and benefits.** Compare the Phase 2 gate-green head `137e77a4c8baa2684ea758edb6bbaa6bda7b7de7` with the Phase 3 gate-green head `ad706640ffd62907961749dc9319ca8bff35bbb0`. Use the same Linux host, JDK, JVM settings, index shape, and workload for both. Measure cold fingerprint generation after core reopen, repeated fingerprint generation, and the added segment flush and merge costs. Cover at least three documented index sizes and deletion ratios of 0%, 1%, 10%, and 50%. Record the max-version deletion case separately. Capture elapsed time, CPU or allocation data when available, and an estimate or measurement of retained summary memory and persisted metadata size. Use warmup runs and at least five measured repetitions per case; report medians and spread, not only the fastest run. Start from the phase gate jobs and `receipts/SOLR-18507.md`. Output: `reports/SOLR-18507-fingerprint-benchmark-round-1.md` and raw rows at `reports/data/SOLR-18507-fingerprint-benchmark-round-1.csv`.
2. **Measure Phase 4 incremental-delete costs and benefits.** After the Phase 4 implementation is repaired and its new gate is DONE, use the Phase 3 and Phase 4 green heads recorded in the matching sections of `receipts/SOLR-18507.md`; record the Phase 4 gate ID with the result. Use the same host and workload settings as item 1. Measure repeated fingerprint generation after sequential non-maximum deletes, and record the maximum-version deletion fallback separately. Include the per-call live-doc scan cost and retained cache memory in the report. If a Phase 4 claim is active, its gate is HANDED BACK or ENDED, no matching green receipt exists, or the branch tip differs from the receipt, prepare the case matrix but do not run this leg against a stale head. Add the Phase 4 results to the same report and raw data file.

Use the existing Solr or Lucene benchmark facilities where they can measure these paths. Do not change either product branch. If the existing facilities cannot measure a path, use a throwaway harness outside the ticket branches and document how it was built and run. If measuring a path requires a new product API or a committed product-code change, record the missing instrumentation and a minimal proposal in the report, then stop for direction.

Record hardware, operating system, JDK, JVM flags, storage, seeds, commit SHAs, index settings, commands, raw data, and any other concurrent benchmark or build activity. Run the comparison legs on the same Linux host. Do not claim an optimization win unless read savings exceed added flush, merge, and memory costs on the measured workloads.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Phase 4 implementation is unrepaired, its gate is active, HANDED BACK or ENDED, no matching green receipt exists, or its branch head differs from the receipt | decided: pause item 2 | Prepare the matrix only, record the missing gate result or head, and do not benchmark a stale tree. Item 1 may proceed. | Nick Shanin |
| A benchmark facility requires a product API or committed product-code patch | decided: stop | Do not edit or push product code. Record the missing instrumentation and minimal proposal, then stop. | Nick Shanin |
| Results vary materially between measured repetitions | default: investigate and repeat once | Record the initial raw rows and likely environment causes. Repeat once after controlling those causes; preserve both sets and report the spread. | Nick Shanin |
| Measurements show no net performance benefit | decided: report no benefit | Record the results and stop. Do not redesign or modify the implementation in this assignment. | Nick Shanin |

No row may leave the choice to the claimant.

## Deliverable

`reports/SOLR-18507-fingerprint-benchmark-round-1.md`: one section per item with status, exact heads, setup, raw results, analysis, and any limitation. Mark item 2 blocked until its gate prerequisite is met.

## Limits

- No Windows test queue, no Windows Gradle, no PR, comment, JIRA, or email.
- No source changes, public branch changes, or gate edits in this assignment.
- Commit as Nick Shanin with no trailers. No em dashes.
