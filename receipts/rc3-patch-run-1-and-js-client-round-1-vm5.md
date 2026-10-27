# Receipt: first full smoke run on the copy-fallback head, and the JS client install failure (vm5, 2026-10-26)

## Full smoke run 1 (head `fdb785484c6`, `patch-run/`)

- Release assembled with `NODE_ENV=development`: passed.
- Smoke tester ran basics, RAT, documentation check and the Java tests. Java tests were not separately checked in this record; the run went on to the integration step.
- Integration step failed at `:solr:webapp:js-client:jsClientDownloadDeps` (run 16:32 to 18:48 UTC, `smoke.log`). Cause: Babel plugins missing from `node_modules`.
- Overall status `rc=0` in `status.txt` is the wrapper's exit code, not the smoke result. The smoke log shows the failure.

## Cause of the JS client failure

- `NODE_ENV=production` was set in the shell. npm omits devDependencies in that mode.
- The generated `solr/api/build/generated/js/package.json` lists the Babel plugins as devDependencies. The install line `args.set(listOf("install"))` in `solr/webapp/js-client/build.gradle.kts` is the same in RC3. It came in with the optional js-client module change `5b87c74fefe` (#4603), which has no JIRA ticket.

Probe (scratch directory, same `package.json`, `NODE_ENV=production`):
- `npm install`: `@babel/plugin-syntax-dynamic-import` not installed.
- `npm install --include=dev`: installed.

## Fix and verification

- Change: `args.set(listOf("install", "--include=dev"))`, commit `23053d08816` on `emlink-copy-fallback`.
- `:solr:webapp:js-client:jsClientBuild` under `NODE_ENV=production`: BUILD SUCCESSFUL, plugin present in `jsClientWorkspace/node_modules`.
- Smoke run 2 on `23053d08816` (`patch-run2`): JS step passed; Java tests passed; integration step failed (BATS, see the receipt `rc3-patched-test-phase-round-1-vm5.md`).

Not done: no JIRA ticket exists for this defect yet; no owner approval for one.
