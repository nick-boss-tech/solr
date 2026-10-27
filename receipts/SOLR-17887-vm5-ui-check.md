# Receipt: SOLR-17887 full UI check and PR-readiness fixes

Written by vm5. Claim: `claims/solr-17887-internal-selection-round-1-vm5.md` (selection round, unchanged). Earlier receipt: `receipts/SOLR-17887-vm5-selection.md` (head `03d5efbf`, superseded for the head below).

- Branch: `nick-boss-tech/solr:solr-17887-file-tree`. Head: `e443d1644caecebf4bba1e0f16347532d9815b81` (ls-remote confirmed after the push). Commits on top of `03d5efbf`:
  - `91ea3e3` "SOLR-17887: separate the license header from the package line" (FileTree.kt and FileTreeTest.kt; the `*/` and `package` lines were joined)
  - `e443d16` "SOLR-17887: add changelog entry for the file tree component" (`changelog/unreleased/SOLR-17887.yml`, type `added`, author name only, link to the JIRA)
- Gate: full `./gradlew -p ui check --no-daemon --no-build-cache`, the same command as `.github/workflows/new-ui-check.yml`.
- Runner: vm5 (Linux aarch64), detached checkout `/workspace/gates/solr-17887/work`, host lock `/workspace/tools/.gradle-vm5.lock`, `NODE_ENV` unset.
- Logs: `/workspace/gates/logs/vm5/solr-17887-uicheck/`.

## Results at e443d16

| Step | Result | Evidence |
| --- | --- | --- |
| `ui check` (spotless, jvmTest, wasmJsBrowserTest, compile, webApp, desktopApp) | rc=0, `BUILD SUCCESSFUL` | `solr17887-uicheck/ui-check.log` |
| `:shared:jvmTest` (fresh, executed) | 55 tests, 0 failures, 0 errors, 2 skipped | `ui/shared/build/test-results/jvmTest/*.xml` |
| `:shared:cleanWasmJsBrowserTest :shared:wasmJsBrowserTest --no-build-cache` (fresh) | rc=0; 59 tests, 0 failures, 0 errors, 3 skipped | `solr17887-wasm/wasm-fresh.log`, `ui/shared/build/test-results/wasmJsBrowserTest/*.xml` |
| `FileTreeTest` in JVM | 8 of 8 pass | `jvmTest/TEST-org.apache.solr.ui.views.files.FileTreeTest.xml` |
| `FileTreeTest` in wasm browser | 8 of 8 pass | `wasmJsBrowserTest/TEST-wasmJsBrowserTest.org.apache.solr.ui.views.files.FileTreeTest.xml` |
| `webApp:wasmJsBrowserTest` | SKIPPED by Gradle (no test sources in that module) | `ui-check.log` |
| `:shared:spotlessCheck` (inside check) | pass | `ui-check.log` |
| `git diff --check` over `03d5efbf..HEAD` | clean | - |

The earlier full run at this same head (`solr17887-uicheck/attempt1-no-stub/`) failed with 27 JVM test failures, all `UnsatisfiedLinkError` from Skiko. Cause: `/tmp/ui-egl-stub` was missing after the tmp directory was cleared. Repair: rebuild the EGL stub from `material/ui-test-setup/egl-stub/stub.c` and rerun. Not a code failure.

The browser tests ran in the Playwright headless Chromium shell, through `CHROME_BIN=/opt/playwright-browsers/chromium-current`. No download was needed.

## Decisions (decision ladder)

- **License header (rung 1, code read).** Sibling files `FileTypeIcon.kt` and `FileSelector.kt` use the form ` */`, a blank line, then `package`. The branch joined them. Fixed to match.
- **Changelog entry, not the `no-changelog` label (rung 3, then rung 4).** `validate-changelog.yml` requires a fragment or the label. The repo rule in `dev-docs/changelog.adoc` and `AGENTS.md` asks for a fragment when a change has a JIRA, and SOLR-17887 has one. The fragment follows the SOLR-10198 format. Recorded choice: fragment. Reasons: the component is a code change, and the label is meant for PRs that need no entry. Owner only at rung 5, not needed here.

## Limits

- The gate ran on the JVM and in the browser wasm runner with a software renderer. Visual output on a real GL host is not checked.
- The PR check in CI runs the same `ui check` on ubuntu-latest; this receipt covers the local run, not CI.
- Look-and-feel against the Figma designs is still not verified beyond structure (see the PR draft's Limits).

## Summary for the PR draft

The head `e443d16` passes the full `./gradlew -p ui check`. `FileTreeTest` passes 8 of 8 on both the JVM and the browser wasm runner, fresh. The PR draft is `drafts/pr-solr-17887-file-tree-round-3.md`. It is not opened; opening needs Nick's go.
