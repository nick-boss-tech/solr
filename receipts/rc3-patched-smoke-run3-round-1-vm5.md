# Receipt: RC3 patched candidate, full smoke run 3 (vm5, 2026-10-26 to 2026-10-27)

Head under test: `23053d08816` on `nick-boss-tech/solr:emlink-copy-fallback`. Contents above RC3 (`749712b71940`): `8b8c45ca0a7` (SOLR-18587 CoreAdmin fix and its test, with the SOLR-18530, SOLR-16630 and SOLR-18564 commits), `fdb785484c6` (SOLR-18586: copy instead of hard link when replication cannot link), `23053d08816` (js-client install with `--include=dev`).

Run: `smokeTestRelease.py` on the release assembled from this head, tmp dir `/workspace/smoke-rc3/patch-run3/tmp`, on `/workspace` (NFS). Run with a local `lsof` shim (`/workspace/tools/bats-shim/bin/lsof`, ss-based) and a child-reaper wrapper (`/workspace/tools/bats-shim/reap-run.py`) on PATH. No step excluded.

## Result

| Smoke step | Result |
|---|---|
| Assemble release (`NODE_ENV=development`) | passed |
| Basics, OpenAPI check, unpack of all three archives | passed |
| `./gradlew rat`, `:solr:documentation:check` | passed |
| Java tests (`./gradlew test`) | passed, 0 failing tasks, 0 `FAILED` lines, 0 EMLINK, 0 `ObjectTracker` leaks |
| Integration tests (`./gradlew integrationTest`) | passed, `BUILD SUCCESSFUL in 29m 12s`, 0 `not ok` (log `build/itest.log`) |
| Build binary release w/ Java 21 | no result: the smoke process ended after this step started (last log line 04:12 UTC, no `smoke ... rc=` line written). Cause not established. |

Java totals (`build/test.log`): `solr:core` 5897 tests, 285 skipped; `solr:solrj` 1358 tests, 45 skipped. Run 2 on the same head had 272 skipped in `solr:core`. The 13-test difference is not explained yet.

## What this does and does not establish

- The gate is not complete. The final smoke step has no result, so the smoke run is not a pass.
- Every step that ran passed on this head, including integration tests that failed in the earlier full run before the environment workarounds.
- The integration tests were run with the lsof shim and the reaper. The standalone BATS run on this head before this run also passed (127 ok, 0 not ok, `/workspace/smoke-rc3/bats-alone/bats.log`).
- Browser check (receipt for the admin screen is separate): stock standalone start of the patched release, admin Dashboard, Security, Cloud, Collections and Logging views loaded with 0 console errors and 0 failed requests, and no "necessary role" message.
- Nothing has been posted to JIRA, a PR, or the dev list.

## Earlier receipts on this board

- `receipts/rc3-patched-test-phase-round-1-vm5.md`: Java phase and first integration failure, run 2 on the same head. Its integration-failure statement is superseded by this receipt.
