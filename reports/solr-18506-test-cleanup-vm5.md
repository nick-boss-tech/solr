# SOLR-18506 TestThinCache cleanup after assertion failure (vm5)

Candidate: `solr-18506-test-cleanup` at `5262d15678d0f21ec1b0a9cfcbf968252a0de5aa`, one commit on the pinned PR head `77c019e1ff0e922e3c2350379dece841651b1d44` (branch `solr-18506-submit`, untouched). Assignment: `assignments/pool-solr-18506-cache-cleanup-vm5.md`. Claim: `claims/pool-vm5-solr-18506-test-cleanup.md`.

## Patch

Only `solr/core/src/test/org/apache/solr/search/TestThinCache.java` changed. `testSimple` now creates its parent `SolrMetricsContext` in try-with-resources, so the parent closes its registered caches and their child contexts when an assertion throws. The explicit `close()` after the last assertion is removed. Assertions, cache sizes, warming and expected values are unchanged. The raw diff is 70 insertions and 71 deletions, mostly re-indentation. Ignoring whitespace, it is 2 insertions and 3 deletions.

## Controlled failure

A temporary edit, reverted before the final runs, threw `AssertionError` once both caches were registered, and an `@After` printed the tracked `SolrMetricsContext` count before the test and after cleanup.

- Run: `TestThinCache`, seed `1668E8BD438E6830`. `testSimple` failed with `SOLR-18506 controlled failure`, as intended. `testInitCore` passed.
- Output: `SOLR18506-PROOF contexts before=60 after=60`. The failed test left no tracked contexts behind.
- Limitation: there is no control run on the pre-fix code, so this shows the fix's result, not a before-and-after pair. The leak on the pre-fix code is evidenced by the CI tracker output recorded in `reports/teardown-objecttracker-leak.md`, and the code path is the same: the parent close sat after the failing assertion.
- Caveat: the `@After` counts only `SolrMetricsContext` objects. The counts are 60 before and after, so the test added none that remain.

## Results

| Step | Command | Result |
|---|---|---|
| Tidy | `tidy` | rc 0, no file changes |
| Error Prone | `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true --rerun` | rc 0, 0 errors |
| Controlled failure | `:solr:core:cleanTest :solr:core:test --tests TestThinCache` at seed | rc 1 from the injected failure; `testSimple` failed on the injected message; PROOF counts above |
| Focused final | same at candidate head, no injection | rc 0; TestThinCache 2 tests, 0 failures, 0 errors, 0 skipped (fresh XML) |
| Module check | `:solr:core:check -x test` | rc 0 |

A first proof attempt failed to compile because the injected `throw` made the following statements unreachable. No test ran in that attempt. The injection was changed to a conditional throw and the proof was rerun.

## Limits

- The Gradle runs bypassed the pool's inode guard. `/workspace` is an NFS mount that reports 0 total inodes, so the guard cannot evaluate it. Root `/` had about 91% of inodes free during the runs. Owner direction: one-off direct runs under the vm5 Gradle lock. Logs are under `gates/logs/vm5/SOLR-18506-test-cleanup/`.
- Only TestThinCache was run, as the assignment requires. No full core suite.
- Not run: the pre-fix control, and any run on the other branches that carry the same test.
- Nothing has been pushed to `solr-18506-submit` or PR 5029. The candidate branch is pushed for review.
