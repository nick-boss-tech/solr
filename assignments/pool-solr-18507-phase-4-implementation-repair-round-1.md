# Pool assignment: SOLR-18507 Phase 4 incremental fingerprint repair, round 1

Status: DONE

- Capability tags: implementation.
- Category: Core admin and cores.
- Intended host: Windows source implementation by Nick's assignment-specific direction on 2026-10-20. All build and test proof remains on Linux. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: Windows implementation host, 2026-10-20, at the direction of Nick Shanin.
- Parent: SOLR-18507.
- Siblings: `gates/SOLR-18507-phase-3.md`, `gates/SOLR-18507-phase-4.md`, `assignments/pool-solr-18507-fingerprint-benchmark-round-1.md`, `assignments/pool-solr-18507-phase-3-benchmark-followup-round-1.md`.
- Follow-up to: the implementation handback recorded by attempt 3 of `gates/SOLR-18507-phase-4.md`.
- Supersedes: none. The ended gate and its claims remain terminal records.

Each item says what to do, the file to start from, and the output path. If an item needs a decision, its row in the Decisions table gives the answer or the default.

## Items

1. **Repair Phase 4 and prepare a fresh gate.** Start from the current fork tip of `solr-18507-phase-4-submit` (807f9d42db448ee229a00d25eda9c9b63e3ed2f9 at posting time), `gates/SOLR-18507-phase-4.md`, `claims/pool-vm3-gate-SOLR-18507-phase-4-r3.md`, the Phase 4 section of `receipts/SOLR-18507.md`, and the green Phase 3 head `ad706640ffd62907961749dc9319ca8bff35bbb0`. The Phase 4 branch must include the Phase 3 green fix before it is gated. Investigate the five remaining failures from attempt 3: `testMalformedPersistedGrossFingerprintFallsBackToLiveScan`, `testIncrementalFingerprintFallsBackWhenDeletedSnapshotExceedsLimit`, `testStalePersistedGrossFingerprintIsRejectedAfterDocValuesUpdate`, `testGrossFingerprintCacheHitAfterDelete`, and `testPersistedGrossFingerprintSurvivesCoreReloadAndDeletes`. Establish whether the delta implementation or a test assumption is wrong by comparing the result to the uncached scan and the documented behavior. Do not change expected counts just to make the tests green; explain any test expectation change with the independent correctness check. Keep changes limited to Phase 4 implementation and its focused tests. Run the focused class on Linux with seed `18507A004`, tidy the patch, commit and push the ticket branch, then request a new phase-specific gate with ID `SOLR-18507-phase-4-r4`, the exact pushed head, focused class, and standard gate steps. Do not edit or reopen the ended Phase 4 gate file. Output: the updated ticket branch, a new gate request on `coordination`, and `reports/SOLR-18507-phase-4-repair-round-1.md` recording diagnosis, changes, focused result, exact heads, and gate request path.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| The implementation or tests need a redesign outside the four-phase scope | decided: stop and record | Explain the evidence and smallest possible Phase 5 proposal in the report. Do not broaden Phase 4. | Nick Shanin |
| The current branch tip moved before the claim, or a public PR now uses this branch | decided: inspect and preserve current work | Record the new tip and PR state in the claim. Do not overwrite another host's commits or force-push a public PR. Use the lease-protected fork-branch procedure only if a history rewrite is necessary and no public PR exists. | Nick Shanin |
| Correctness requires changing an expected value | default: justify from the uncached oracle | Keep the independent uncached comparison, explain the expected behavior in the report, and include the rationale in the test change. If that rationale is unclear, stop and record the question rather than guessing. | Nick Shanin |
| The focused test still fails after the repair | decided: do not request a gate | Record the failure, seed, and evidence in the report and claim; leave the branch available for the next implementation pass. | Nick Shanin |

No row may leave the choice to the claimant.

## Deliverable

`reports/SOLR-18507-phase-4-repair-round-1.md`: exact starting and ending heads, diagnosis for each failure, code and test changes, focused test and tidy results, and the new gate request path and head. The separate gate owns the full gate proof and receipt.

## Limits

- No Windows test queue, no Windows Gradle, no PR, comment, JIRA, or email.
- Do not measure Phase 4 performance until its new gate is DONE and the receipt matches the branch tip.
- Do not modify the Phase 2 or Phase 3 branches or change any Phase 4 expected value without the independent correctness rationale above.
- Commit as Nick Shanin with no trailers. No em dashes.
