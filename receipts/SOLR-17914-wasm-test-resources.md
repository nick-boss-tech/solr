# Receipt: SOLR-17914 wasm test resources and browser leg (vm4)

- Claim: `claims/solr-17914-wasm-test-resources-vm4.md`. Assignment: `assignments/solr-17914-browser-fix-round-1.md`.
- Supersedes the red result in `receipts/SOLR-17914-browser-fix-round-1.md` for candidate `252e0a75bb`. That candidate's test-side change was correct once resources were served, but it could not pass without the harness fix below.
- Verdict: GREEN at PR #5258 head `bb7a4f32eb`. The full `./gradlew -p ui check` passes, and every wasm browser test passes or is skipped. The PR body was updated with the proof line and screenshots, as the owner directed on 2026-10-28.

## Inputs

- PR #5258 head `bb7a4f32eb` on `nick-boss-tech/solr` branch `solr-17914-create-collection-dialog-new-design`. Fast-forward from `6170ba10ac`.
- Commits on top of `6170ba10ac`, in order:
  - `c4441b0604` SOLR-17914: serve compose resources to the wasm browser test run. Adds `ui/shared/karma.config.d/compose-resources.js`.
  - `042203ede5` SOLR-17914: await the dialog string resources before asserting. Test-only, in `CreateCollectionFormTest.kt`.
  - `995ce069c0` SOLR-17914: await the card detail strings before asserting. Test-only, in `CollectionDetailsCardTest.kt`, which is inherited from SOLR-17913 base `0a04d2239e`.
  - `bb7a4f32eb` SOLR-17914: await the error message strings before asserting. Test-only, in `CreateCollectionErrorsTest.kt`.
- Fix branch `solr-17914-browser-fix-round-1` (candidate `252e0a75bb`) is superseded and left in place.

## Cause

- Karma did not serve `composeResources`. The generated `karma.conf.js` had `"proxies": {}` and only the test bundle in `files`. This is a shared harness problem (category c), and the file fixes it for every wasm browser test.
- Once resources are served, Compose loads them asynchronously. Tests that assert right after `setContent` race the load. Preloading each asserted string with `getString` before composition, and calling `awaitIdle()` after, removes the race. The card test had the same race (category a in the claim, but it is inherited base code).

## Environment

- vm4, Chrome for Testing 156.0.8078.4 at `/workspace/tools/chrome-156/chrome-linux-arm64/chrome`, `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib`, lock `/workspace/tools/.gradle-vm4.lock`, JDK 21, Gradle 9.7.0, `--max-workers=2`, `--no-daemon --no-build-cache`.
- Logs: `/workspace/gates/logs/vm4/solr-17914-wasm-test-resources/` (driver steps, `state.txt`, per-step `.rc` and logs) and `/workspace/gates/logs/vm4/solr-17914-pr5258-full-check/`.
- The host rebooted twice during the first attempts, once during the original browser reproduction and once during the test-bundle build. Steps resumed from their `.rc` checkpoints.

## Runs

| Step | Head | Command | Exit | Result |
|---|---|---|---|---|
| repro, stock tests, no karma fix | `6170ba10ac` | `:shared:wasmJsBrowserTest --tests CreateCollectionFormTest` | 1 | 9 completed, 2 failed (`could not find any node`) |
| karma fix only, stock tests | `c4441b0604` | same | 1 | 9 completed, 2 failed |
| karma fix plus dialog test change | `042203ede5` | same | 0 | 9 of 9 pass |
| full wasm browser leg, card test unchanged | `042203ede5` plus the errors test change (committed as `bb7a4f32eb`) | `:shared:wasmJsBrowserTest` | 1 | 70 total, 3 skipped, 2 failed (`CollectionDetailsCardTest`), 0 errors |
| full wasm browser leg, card fix added | `bb7a4f32eb` | `:shared:spotlessCheck :shared:wasmJsBrowserTest` | 0 | 70 total, 3 skipped, 0 failed, 0 errors |
| shared check at final head | `bb7a4f32eb` | `:shared:check` | 0 | JVM 66 total, 2 skipped, 0 failed, 0 errors; wasm 70 total, 3 skipped, 0 failed, 0 errors |
| full check at final head | `bb7a4f32eb` | `./gradlew -p ui check --no-build-cache --no-daemon --rerun-tasks` | 0 | BUILD SUCCESSFUL in 3m31s, 85 tasks executed. Fresh XML: JVM 66/2/0/0 and wasm 70/3/0/0 (total/skipped/failed/errors). `webApp` and `desktopApp` produced no test XML. |

Counts come from fresh XML under `ui/shared/build/test-results/` for the last two rows. The earlier rows are from the driver's log lines.

## Per-test result (dialog and card)

- `CreateCollectionFormTest.testShowsFieldsAndButtons`: fail, then pass.
- `CreateCollectionFormTest.testCreatePassesTheTypedValuesAndAdvancedRouter`: fail, then pass.
- `CreateCollectionErrorsTest.testNameExistsShowsTheNameError`: fail, then pass.
- `CreateCollectionErrorsTest.testFailureWithoutMessageShowsTheFallback`: fail, then pass.
- `CollectionDetailsCardTest.testShowsCollectionDetails`: fail, then pass.
- `CollectionDetailsCardTest.testShowsAutoAddReplicasState`: fail, then pass.
- No assertion was removed or weakened, and no test was ignored.

## Known intermittent failure

On the Skiko wasm runtime, some runs fail before any test body with `org_jetbrains_skia_Surface__1nMakeRasterN32Premul is not a function`. This happened in several runs, usually on the first run after a compile, and an immediate rerun passed. It is documented in `dev-docs/ui/wasm-browser-tests.adoc` on PR #5262.

## Screenshots

- Captured in headless Chrome 156 from the production `CreateCollectionForm` inside `SolrTheme(useDarkTheme = false)`, 1024x768, with a temporary wasm test fixture that is not committed. The four states are empty, advanced section open, name-exists error, and generic failure.
- The PNGs are in `/workspace/gates/logs/vm4/solr-17914-wasm-test-resources/screens/`. They are linked from the PR #5258 body through a secret gist. They are not in `receipts/SOLR-17914-screens/` yet. Whether to publish them here is an owner decision.

## Gaps

- The browser tests are not part of the default CI pipeline (see `dev-docs/ui/testing-and-deployment.adoc`), so these results are local to vm4.
- The shared-module card test fix edits an inherited SOLR-17913 test file. Reviewers should see it in the PR body.
- The draft proof-line file `drafts/pr-5258-proof-line-update.md` was not written. The PR body was edited directly, as the owner directed.
