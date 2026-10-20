# Gate job: SOLR-18507 (Phase 4 live fingerprint deltas)

Status: HANDED BACK (retry failed at compile, repair defect at 13f2c6d045; see attempt 2)

- Branch: `solr-18507-phase-4-submit`. Head: `f50c2271a32f25eb24ced4276516beb858f3b97e` (fork tip, confirmed by ls-remote on 2026-10-20). If the tip has moved at claim time, stop and record the new tip here.
- Base: `35f6e044dda8c15115bccce6b7c965371a97ad30`. Prior gated head: none recorded.
- Gate ID: `SOLR-18507-phase-4`. Claim file: `claims/pool-<host>-gate-SOLR-18507-phase-4.md`.
- Intended host: any capable Linux host. WORKFLOW.md lets any capable Linux host claim it if that host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.core.SolrCoreFingerprintCacheTest`. Seed: `18507A004`.
- Diff scope: 4 files: changelog/unreleased/SOLR-18507-4.yml (8 additions); solr/core/src/java/org/apache/solr/core/SolrCore.java (48 additions, 6 deletions); solr/core/src/java/org/apache/solr/update/IndexFingerprint.java (101 additions); solr/core/src/test/org/apache/solr/core/SolrCoreFingerprintCacheTest.java (222 additions, 2 deletions); total 379 additions, 8 deletions.
- Why: Phase 4 adds a bounded per-segment live fingerprint delta cache that subtracts newly deleted versions and falls back when a maximum is removed, a document is restored, the deletion snapshot exceeds its limit, or the DocValues generation changes. The focused test compares results with an uncached scan and covers sequential deletes, maximum deletion, restored-document rejection, over-limit fallback, and generation invalidation after a delta snapshot is populated. The Phase 3 performance benchmark remains pending, so this work makes no speedup claim.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-18507-4.yml`.
- Step 1: tidy. If tidy changes any file, apply the tidy output, record its patch and the tidied tree sha in this job, and continue from step 2. WORKFLOW.md, Gate repairs.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: none requested. Record that in the receipt.
- Step 4, focused head run: `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.core.SolrCoreFingerprintCacheTest -Ptests.seed=18507A004`. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

Failures follow the rows below and WORKFLOW.md, Gate repairs. A failed retry hands the job back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tidy changes any file | decided: repair | Apply the tidy output on the gate worktree, record its patch and the tidied tree sha here, and continue from step 2. WORKFLOW.md, Gate repairs. | Nick Shanin (standing pool rule) |
| Failure in repair scope (compile, changelog, lint, or up to two failing tests) | decided: repair, then one retry | Make the repair as one commit and push it per WORKFLOW.md. Record the commit, its files, and the failing test or task here, then rerun from step 0 on the new head. Do not rerun an unchanged tree. | Nick Shanin (standing pool rule) |
| Failure outside repair scope, or any failure after the retry | decided: hand back | Set Status to HANDED BACK with the reason. Record the failing test or task, its failure text, the seed, the fresh XML counts, and both attempts. Do not relaunch. | Nick Shanin (standing pool rule) |
| Cluster fails to start before any assertion | default: run the same command once more | If the second run also fails before an assertion, record the first exception here and stop. This is harness setup, not a test result. | Nick Shanin (standing pool rule) |
| Proof leg result differs from the stated expectation | decided: record and stop | Record the test name and both failure texts here. Do not edit the tests. | Nick Shanin (standing pool rule) |
| Tip moved at claim time | decided: stop | Record the new tip here and hand back. | Nick Shanin (standing pool rule) |

No row may leave the choice to the claimant.

## On green

1. Write or refresh `receipts/SOLR-18507.md` with a new top section for gate ID `SOLR-18507-phase-4`: head, gate date and host, log path, step results, the proof-leg result (test name and failure text, or none requested), and the focused counts from the fresh XML. Record every attempt, its repair commits and files, and the gated head.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-SOLR-18507-phase-4.md` DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog check
- [ ] step 1 tidy clean, or repaired and recorded
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, if requested
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks, plus any repair commits (WORKFLOW.md, Gate repairs).
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.

## Note from vm3 (2026-10-20)

- The phase 3 gate found and fixed one defect: `IndexFingerprint.getGrossFingerprint` read the `_version_` DocValues with `advanceExact`, which the flush-time producer does not support (`testMergedSegmentPersistsGrossFingerprint`, seed `18507A003`). The fix is `ad706640ff` on `solr-18507-phase-3-submit`, with the tidy repair `f0259902ce`. Receipt: `receipts/SOLR-18507.md`, phase 3 section.
- Head `f50c2271a32f25eb24ced4276516beb858f3b97e` contains the phase 3 tip `35f6e044dd` but not `ad706640ff`, and it still has the `advanceExact` call. Rebase onto `ad706640ff` before this gate runs, or the same test is expected to fail. vm3 has not changed this branch.

## Attempt 1 (vm3, head d797b9b310): test failed

- Steps: step 0 pass; tidy changed 2 files (IndexFingerprint.java, SolrCoreFingerprintCacheTest.java; patch in logs, tidied tree 9eb840aa20), applied as repair 76d29000fb; compile pass (on the tidied tree); focused test `org.apache.solr.core.SolrCoreFingerprintCacheTest`, seed 18507A004: FAILED, 6 of 7 tests (failing: testMalformedPersistedGrossFingerprintFallsBackToLiveScan, testIncrementalFingerprintFallsBackWhenDeletedSnapshotExceedsLimit, testStalePersistedGrossFingerprintIsRejectedAfterDocValuesUpdate, testGrossFingerprintCacheHitAfterDelete, testIncrementalFingerprintRejectsRestoredDocument, testPersistedGrossFingerprintSurvivesCoreReloadAndDeletes); `:solr:core:check -x test` pass on the same tree.
- Logs: gates/logs/vm3/solr-18507-phase-4-run1-head-d797b9b310/ (test.log, test-results/ XML and OUTPUT).
- Investigation (not a rerun): every failure is `Error opening new searcher` with `AlreadyClosedException: this IndexReader is closed` at the first commit after an earlier test in the class had closed the shared core's searcher. Cause: four test blocks used `try (var searcher = core.getSearcher().get())`, which closes the searcher and decRefs its reader without releasing the core's reference. The base merge test does the same but reloads the core afterwards, so it never shows. Phase 4's tests keep the core, so one block poisons the rest. Not a production defect: `SolrCore`, `IndexFingerprint` and the fingerprint code have no close calls on this path.
- Repair: 13f2c6d045 (one commit, lifecycle only): the four blocks now hold a `RefCounted` from `getSearcher()` and release it in `finally`, the same pattern as the existing block at line 368. Pushed fast-forward to solr-18507-phase-4-submit (d797b9b310 to 13f2c6d045).
- Retry: one retry from step 0 on 13f2c6d045 under the same seed. Result recorded below when it finishes.
- A duplicate invocation of the same gate script started during attempt 1 and was stopped by PID before it reached the test step. Attempt 1's logs were not overwritten.

## Tip check

2026-10-20T05:27:50Z: fork tip of solr-18507-phase-4-submit is 13f2c6d045fa26982d215a68b477d72e635bdc5f, not the head this job names (f50c2271a32f25eb24ced4276516beb858f3b97e). The job says to stop and record the new tip when the tip moves at claim time. Recorded here; not claimed, not retargeted. A new gate job at the new head is needed.

## Attempt 2 (vm3, head 13f2c6d045, the one retry): HANDED BACK

- Steps: step 0 pass; tidy pass with no changes; compile FAILED. `SolrCoreFingerprintCacheTest.java:428: error: variable searcherRef is already defined in method testStalePersistedGrossFingerprintIsRejectedAfterDocValuesUpdate()`. The repair in 13f2c6d045 declared a second `searcherRef` in the method that already declares one at the block near line 368 (try/finally with decref). The repair assumed a different method. Test and check then ran against a tree that did not compile and produced no test evidence (test=1, check=1 are from the compile failure).
- Log: gates/logs/vm3/solr-18507-phase-4/compile.log.
- Cause: a defect in the repair, not in the fingerprint code. The attempt-1 test evidence still stands (gates/logs/vm3/solr-18507-phase-4-run1-head-d797b9b310/).
- Fix for the next host: in that method, rename the new declaration at the block near line 428 (for example `reloadedSearcherRef`), and its `decref` in the finally, so it no longer collides. Then a new gate job at the new head, with its own receipt. One line-level change, no other edits.
- Status: HANDED BACK (retry failed at compile; the one retry is spent). The job returns to implementation with this fix as its first step.
