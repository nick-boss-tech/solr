# Receipt: SOLR-17914 browser-leg check, round 1

- Assignment: `assignments/solr-17914-browser-check-round-1.md`
- Host: vm4
- Branch and head: `solr-17914-create-collection-dialog-new-design` at `6170ba10ac5061203339fa0a96e714c7e64616c2` (clean worktree `/workspace/worktrees/solr-17914-bc`, no code changes)
- Command: `flock /workspace/tools/.gradle-vm4.lock ./gradlew -p ui check --no-build-cache --no-daemon --rerun-tasks`, with `CHROME_BIN=/workspace/tools/chrome-156/chrome-linux-arm64/chrome` (Chrome for Testing 156.0.8078.4, headless) and `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib`
- Result: `BUILD FAILED in 5m 41s` (exit 1), failing task `:shared:allTests` (from `:shared:wasmJsBrowserTest`)
- Log: `/workspace/gates/logs/vm4/solr-17914-browser-check-round-1/check.log`
- JUnit XML: `/workspace/gates/logs/vm4/solr-17914-browser-check-round-1/check-junit/` (`jvmTest/`, `wasmJsBrowserTest/`)
- An earlier launch of the same command was cut off when the session ended before it wrote a result (log kept as `check-interrupted-1.log`). It is not counted here.

## Verdict

Browser leg RED. Not green, so no proof line is drafted for PR #5258.

## Counts from the fresh XML

| Layer | Total | Skipped | Failures | Errors |
|---|---|---|---|---|
| `:shared:jvmTest` | all green | 2 | 0 | 0 |
| `:shared:wasmJsBrowserTest` (ChromeHeadless156) | 70 | 3 | 6 | 0 |

Spotless ran (`:webApp:spotlessCheck` and others) with no failure reported in the log.

## Named results in the browser run

- `HttpCollectionsRepositoryTest`: 5 of 5 passed.
- `CreateCollectionFormTest`: 7 of 9 passed. Failed: `testShowsFieldsAndButtons`, `testCreatePassesTheTypedValuesAndAdvancedRouter`.
- `CreateCollectionErrorsTest`: 1 of 3 passed. Failed: `testNameExistsShowsTheNameError`, `testFailureWithoutMessageShowsTheFallback`.
- `CreateCollectionFormTest`, `CreateCollectionErrorsTest`, `HttpCollectionsRepositoryTest` in the jvm run: 9 of 9, 3 of 3, 5 of 5 passed.

## Failing browser tests (first error text)

All six use `ChromeHeadless156.0.0.0, Linux`.

1. `org.apache.solr.ui.views.collections.CreateCollectionFormTest.testShowsFieldsAndButtons`: `Expected exactly '1' node but could not find any node that satisfies: (Text + InputText + EditableText contains 'Cancel')`.
2. `CreateCollectionFormTest.testCreatePassesTheTypedValuesAndAdvancedRouter`: `Failed to inject mouse input. Expected exactly '1' node but could not find any node that satisfies: (Text + InputText + EditableText contains 'Composite ID')`.
3. `CreateCollectionErrorsTest.testNameExistsShowsTheNameError`: `Expected exactly '1' node ... contains 'A collection with this name already exists'`.
4. `CreateCollectionErrorsTest.testFailureWithoutMessageShowsTheFallback`: `Expected exactly '1' node ... contains 'Could not create the collection.'`.
5. `org.apache.solr.ui.views.collections.CollectionDetailsCardTest.testShowsCollectionDetails`: `Expected exactly '1' node ... contains 'Collection Details'`.
6. `CollectionDetailsCardTest.testShowsAutoAddReplicasState`: `Expected exactly '1' node ... contains 'Enabled'`.

Scope note on failures 5 and 6: `CollectionDetailsCardTest` is not in this PR's diff. It comes from the stacked base commit `0a04d2239e` (SOLR-17913). The diff `39efaef802..6170ba10ac` does not touch it. Whether it is also red on the SOLR-17913 base in the browser leg has not been checked here.

Observation, not diagnosed: in every failure the composable text is not found in the wasm browser run, while the same tests pass on jvm. The common pattern points at the wasm browser test setup or rendering, not at the form logic. The cause is not established. No code was changed under this assignment.

## Not done under this assignment

- No code changes, no pushes to the branch, no PR or Jira posts or edits.
- No screenshots were produced. The owner's extra request for screenshots is not in this assignment and is pending a decision.
