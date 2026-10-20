# Pool assignment: SOLR-18507 Phase 3 benchmark cost accounting, round 1

Status: OPEN

- Capability tags: sweep.
- Category: Core admin and cores.
- Intended host: any pool Linux host with sweep. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: Windows implementation host, 2026-10-20, at the direction of Nick Shanin.
- Parent: SOLR-18507.
- Siblings: `assignments/pool-solr-18507-fingerprint-benchmark-round-1.md`, `gates/SOLR-18507-phase-2.md`, `gates/SOLR-18507-phase-3.md`, `gates/SOLR-18507-phase-4.md`.
- Follow-up to: the remaining Phase 3 measurements in the round 1 benchmark report.
- Supersedes: none. The round 1 assignment and claim remain terminal records.

Each item says what to do, the file to start from, and the output path. If an item needs a decision, its row in the Decisions table gives the answer or the default.

## Items

1. **Finish Phase 3 cost accounting.** Start from `reports/SOLR-18507-fingerprint-benchmark-round-1.md`, its CSV and harness, `receipts/SOLR-18507.md`, and the Phase 2 and Phase 3 gate records. Use the same Linux host for Phase 2 and Phase 3. Prefer the vm2 host and settings already used for the paired comparison. Complete the missing measurements: segment flush cost, merge cost, persisted fingerprint metadata size per segment, retained summary memory or a clearly described estimate, and the maximum-version deletion case. Cover index sizes 10,000, 100,000, and 1,000,000 with deletion ratios 0%, 1%, 10%, and 50%; record the maximum-version deletion case separately. Use warmups and at least five measured repetitions per case, then report medians and spread. Reuse the existing cold and warm rows; if the original host is unavailable or the workload settings differ, rerun both heads on one host and keep the new paired data separate. Do not edit either product branch. Output: `reports/SOLR-18507-fingerprint-benchmark-round-2.md` and raw rows at `reports/data/SOLR-18507-fingerprint-benchmark-round-2.csv`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| The vm2 host is unavailable or cannot complete the measurements | decided: use one replacement Linux host | Rerun paired Phase 2 and Phase 3 measurements on that host; do not mix its new rows with timings from the earlier host. | Poster |
| Measuring a path requires a product API or committed product-code change | decided: stop | Record the missing instrumentation and a minimal proposal in the report. Do not change a product branch. | Nick Shanin |
| Repetitions vary materially | default: investigate and repeat once | Preserve the first raw rows, control the likely environment cause, then report both sets and their spread. | Nick Shanin |
| The measured read savings do not exceed flush, merge, and memory costs | decided: report no net benefit | Keep the measurements and stop. Do not redesign or modify the implementation in this assignment. | Nick Shanin |

No row may leave the choice to the claimant.

## Deliverable

`reports/SOLR-18507-fingerprint-benchmark-round-2.md`: setup, exact heads, host details, commands, results, analysis, and limitations. Include the Phase 4 prerequisite status and do not run Phase 4 measurements while its gate is ENDED or no matching green receipt exists.

## Limits

- No Windows test queue, no Windows Gradle, no PR, comment, JIRA, or email.
- No source changes, product branch changes, or gate edits.
- Commit as Nick Shanin with no trailers. No em dashes.
