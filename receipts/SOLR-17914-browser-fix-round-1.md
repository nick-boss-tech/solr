# Receipt: SOLR-17914 browser fix round 1, Linux gate (vm4)

- Assignment: `assignments/solr-17914-browser-fix-round-1.md`. Handoff: `gates/solr-17914-browser-fix-round-1-windows-candidate.md`. Claim: `claims/solr-17914-browser-fix-round-1-linux-gate.md`.
- Heads: original dialog `6170ba10ac5061203339fa0a96e714c7e64616c2`. Candidate `252e0a75bb0d6a9183c8cc879da60b809b52c242` (on `solr-17914-browser-fix-round-1`). SOLR-17913 base `0a04d2239ee1c9a1dfe68cbe84f094ca231130b9`.
- Environment: vm4, Chrome for Testing 156.0.8078.4 at `/workspace/tools/chrome-156/chrome-linux-arm64/chrome`, `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib`, lock `/workspace/tools/.gradle-vm4.lock`, `--no-build-cache --no-daemon --rerun-tasks`.
- Logs and fresh XML: `/workspace/gates/logs/vm4/solr-17914-browser-fix-round-1/` (`<step>.log`, `<step>.rc`, `<step>-junit/`).

## Steps

| Step | Head | Command | Exit |
|---|---|---|---|
| candidate-spotless | 252e0a75bb | `./gradlew -p ui :shared:spotlessCheck` | 0 |
| repro-original-browser | 6170ba10ac | `./gradlew -p ui :shared:wasmJsBrowserTest` | 1 |
| base-browser | 0a04d2239e | `./gradlew -p ui :shared:wasmJsBrowserTest` | 1 |
| candidate-full-check | 252e0a75bb | `./gradlew -p ui check` | 1 |

## Counts from fresh XML

- Original head, wasm browser: 70 total, 3 skipped, 6 failed, 0 errors. Same six as round 1.
- SOLR-17913 base, wasm browser: 53 total, 3 skipped, 2 failed, 0 errors. Both failures are `CollectionDetailsCardTest` (category b, inherited).
- Candidate, wasm browser: 70 total, 3 skipped, 6 failed, 0 errors. The same six fail. No dialog test is green.
- Candidate, JVM: 66 total, 2 skipped, 0 failed, 0 errors.

## Per-test result, original vs candidate

- `CreateCollectionFormTest.testShowsFieldsAndButtons`: fail, fail.
- `CreateCollectionFormTest.testCreatePassesTheTypedValuesAndAdvancedRouter`: fail, fail.
- `CreateCollectionErrorsTest.testNameExistsShowsTheNameError`: fail, fail.
- `CreateCollectionErrorsTest.testFailureWithoutMessageShowsTheFallback`: fail, fail.
- `CollectionDetailsCardTest.testShowsCollectionDetails` and `testShowsAutoAddReplicasState`: fail on original and candidate, and on base.

## Cause (evidence)

- Original-head errors: `Expected exactly '1' node ... contains '<text>'`. These are string-resource texts.
- Candidate errors for the four dialog tests: `org.jetbrains.compose.resources.MissingResourceException: Missing resource with path: ./composeResources/org.apache.solr.ui.shared.generated.resources/values/strings.commonMain.cvr`. The candidate's added `getString(Res.string.*)` calls throw this exception. The candidate does not fix the cause.
- Build output: `ui/shared/build/processedResources/wasmJs/main/composeResources/` has 38 files, including `values/strings.commonMain.cvr`. `ui/shared/build/processedResources/wasmJs/test/` has 0 files. The Karma bundle `_karma_webpack_*` has no `composeResources`. The wasm test bundle does not receive the main string resources.
- Classification: the four dialog failures and the two card failures share one cause, category (c), shared build/harness. The candidate's approach (category a) is wrong. The candidate's test changes do not fix any test.

## Screenshots

Not captured. The four states depend on the same string resources that are missing in the browser run, so the screenshots would show blank labels. Blocker: the browser test runtime cannot load Compose string resources. Nothing was substituted.

## Verdict

RED. The candidate does not turn the browser leg green. The proof line is not drafted. The candidate patch (`252e0a75bb`) is not landed on PR #5258, because it only adds calls that throw. PR #5258's head is unchanged.

## Needed owner decision

1. Shared build fix in `ui/shared` for the wasm test resource set (category c). This affects all wasm browser tests, so it needs an owner-directed slice.
2. Whether to drop the candidate's `getString` changes. They do not fix anything.
