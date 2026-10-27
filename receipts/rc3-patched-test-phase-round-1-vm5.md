# Receipt: RC3 smoke test phase on the patched candidate (vm5, 2026-10-26)

Head under test: `23053d08816` on `nick-boss-tech/solr:emlink-copy-fallback`.
Contents above RC3 (`749712b71940`): candidate `8b8c45ca0a7` (SOLR-18587 CoreAdmin fix, SOLR-18530, SOLR-16630 and SOLR-18564 commits), plus `fdb785484c6` (SOLR-18586: fall back to a copy when a hard link fails in `IndexFetcher`), plus `23053d08816` (js-client install with `--include=dev`).
Run: full `smokeTestRelease.py` on the release assembled from this head, on `/workspace` (NFS), local tmp dir `/workspace/smoke-rc3/patch-run2/tmp`. Log `/workspace/smoke-rc3/patch-run2/smoke.log`, status `status.txt` (rc=0 is the wrapper, not the smoke result).

## Result

| Smoke step | Result |
|---|---|
| Basics, OpenAPI check, unpack of binary, slim and src archives | passed |
| `./gradlew rat`, `:solr:documentation:check` | passed (the run continued past them) |
| Java tests, `./gradlew test -Dtests.nightly=false -Dtests.badapples=false` | passed: 24 modules, 0 failing tasks, 0 `FAILED` lines |
| Integration tests, `./gradlew integrationTest` | FAILED (`:solr:packaging:integrationTests`) |

Java test totals (`/workspace/smoke-rc3/patch-run2/tmp/unpack/solr-10.1.0-SNAPSHOT/build/test.log`): `solr:core` 5897 tests, 272 skipped, no failures. The largest others: `solr:solrj` 1358 (45 skipped), `solrj-streaming` 617, `modules:language-models` 115, `modules:ltr` 211.

Checks on the Java log: 0 `Too many links` (EMLINK) lines, 0 `ObjectTracker found` lines. Run-005 on the unpatched candidate had 4 failures in `solr:core` and 33 EMLINK lines.

## Not passed

Integration tests failed. 46 BATS cases were `not ok` in `solr/packaging`. Most of the logged errors were `solr stop` and `solr start` timing out, with "Unable to open socket file /tmp/.java_pid...". The host had no `lsof` on PATH, and orphaned Solr JVMs were not reaped.

Standalone check on the same head, with a local `lsof` shim (`/workspace/tools/bats-shim/bin/lsof`, ss-based) and a child-reaper wrapper (`/workspace/tools/bats-shim/reap-run.py`): `:solr:packaging:integrationTests --rerun` passed, 127 ok, 0 not ok, rc 0 (`/workspace/smoke-rc3/bats-alone/bats.log`). The shim and wrapper are substitutes written on this host, not the vm4 tools.

## Running now

Run 3 (`/workspace/smoke-rc3/patch-run3`) repeats the full smoke on the same head with the shim and reaper on PATH, and no step excluded. As of 02:55 UTC it is in the Java test phase. Its result is not in this receipt.

## What this receipt does not show

- The Java test phase passed once, on one head, with fixed seeds per run. It is not a clean smoke gate, because the gate requires every step to pass.
- Integration tests have not passed inside the full smoke run yet.
- Nothing has been posted to JIRA or to any PR.
