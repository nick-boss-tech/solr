# Receipt: SOLR-17887 internal selection on the file-tree branch

Written by vm5, the gating host. Claim: `claims/solr-17887-internal-selection-round-1-vm5.md`. Prior gate and receipt by vm6 remain in `receipts/SOLR-17887.md`, unchanged.

- Branch: `nick-boss-tech/solr:solr-17887-file-tree`. Gated head: `03d5efbfe6a06e49fdf5d5fa3c1f50d7457ce528`, confirmed by ls-remote after the push. Base tip before this round: `a22da1bfe72399204b6fd87b8b084e19e4a9bd90`. Commit: "SOLR-17887: a click selects the file tree item", author Nick Shanin, no trailers, one commit, fast-forward.
- Ticket: SOLR-17887, acceptance criteria 6 ("Single click selects files and folders") and 7 (look and feel, not verified here).
- Runner: vm5, Linux aarch64, detached checkout at `/workspace/gates/solr-17887/work`, under the host lock `/workspace/tools/.gradle-vm5.lock`. JVM test with the software renderer, using `material/ui-test-setup/` (`LD_LIBRARY_PATH=/tmp/ui-egl-stub`, `JAVA_TOOL_OPTIONS=-Dskiko.renderApi=SOFTWARE_FAST`). `NODE_ENV` unset.
- Logs: `/workspace/gates/logs/vm5/solr-17887-selection/`.

## Steps at 03d5efbf

| Step | Command | rc | Log |
| --- | --- | --- | --- |
| spotless | `-p ui :shared:spotlessCheck --no-daemon --no-build-cache` | 0 | `solr17887-fresh/spotless.log` |
| jvmtest | `-p ui :shared:cleanJvmTest :shared:jvmTest --tests org.apache.solr.ui.views.files.FileTreeTest --no-daemon --no-build-cache` | 0 | `solr17887-fresh/jvm.log` |

Fresh JUnit XML: `solr17887-fresh/xml/TEST-org.apache.solr.ui.views.files.FileTreeTest.xml`, timestamp 2026-10-27T21:25:08Z. `FileTreeTest` 8 tests, 0 failures, 0 errors, 0 skipped.

## Tests

- New: `testSingleClickSelectsItem`, `testSelectedPathSelectsItemOnFirstComposition`, `testChangedSelectedPathReplacesSelection`.
- Existing, unchanged: `testHidesChildrenUntilFolderIsExpanded`, `testChevronExpandsAndCollapsesFolder`, `testDoubleClickOnFolderNameExpandsFolder`, `testSingleClickReportsItem`, `testDoubleClickReportsItem`.

## Fail-before check on the new click test

The base code has no selection to fail against, so the check is a mutation: the one selection line in the click handler (`selectedItemPath = row.node.path` before `onItemClick`) was removed in a copy of `03d5efbf`, and only `testSingleClickSelectsItem` was run (seed default, `solr17887-mut/`). Result: rc=1, `ComposeTimeoutException: Condition still not satisfied after 2000 ms` at `FileTreeTest.kt` line 102, the selection wait. This is a behavioral failure, not a compile error. The tree was restored to `03d5efbf` afterward (`git status` clean).

## Repairs during the gate

- First run of the new tests failed to compile: `SemanticsConfiguration.getOrNull` does not exist in this Compose version. Repair: read the property with `config[SemanticsProperties.Selected]`. No behavior change. The first run is in `solr17887-sel/`.
- A run used a cached `:shared:jvmTest` result (`FROM-CACHE`) and was not counted. Repaired by rerunning with `cleanJvmTest` and `--no-build-cache`; that run is the one cited above.

## Limits

- JVM tests only, with the software renderer. The wasmJs browser tests in CI were not run here.
- A chevron click toggles the folder and does not select it. This is not covered by a test.
- No visual check against the Figma designs. No screen hosts the component.

## Summary for the PR draft

The head `03d5efbf` passes spotless and 8 of 8 `FileTreeTest` tests with a fresh, uncached JVM run. The click-selection test fails when its selection line is removed. The PR draft: `drafts/pr-solr-17887-file-tree-round-2.md`. The round-1 draft (`drafts/pr-solr-17887-file-tree.md`) is superseded for the head; the round-1 draft's "caller owns the selected state" wording no longer holds.
