# Receipt: SOLR-17849 browser-leg check and screen render, round 1 (vm5)

Assignment: `assignments/solr-17849-browser-check-round-1.md`. Claim: `claims/solr-17849-browser-check-round-1-vm5.md`.

- Head: `cf35ab0329c41cdef11429906d1e669d38b9f3a9` (`solr-17849-header-submit`, checked out in a clean detached worktree, no local changes).
- Command: `./gradlew -p ui check --no-daemon --no-build-cache` with `NODE_ENV` unset, `LD_LIBRARY_PATH=/tmp/ui-egl-stub` (rebuilt from `material/ui-test-setup/egl-stub/stub.c`), `JAVA_TOOL_OPTIONS=-Dskiko.renderApi=SOFTWARE_FAST`, `CHROME_BIN=/opt/playwright-browsers/chromium-current`, under `/workspace/tools/.gradle-vm5.lock`.
- Log: `/workspace/gates/logs/vm5/solr-17849-browser/ui-check.log`. Result: `BUILD FAILED`, rc=1, in `:shared:allTests`. Later `ui check` tasks may not have run; not checked.

## Browser leg (wasm)

| Run | Result | Source |
| --- | --- | --- |
| Full `ui check` | 60 run, 1 failed, 0 errors, 3 skipped | fresh XML, `ui/shared/build/test-results/wasmJsBrowserTest/` |
| Rerun, `IndexQueryHeaderTest` only | 6 run, 1 failed (same test) | `wasm-rerun-header.log` |
| `IndexQueryStateHolderTest` (`components.indexquery`) | 3 run, 0 failed, 0 errors, 0 skipped | `wasm-stateholder.log` |

The failing test, in the full run and in the rerun:

- `org.apache.solr.ui.views.indexquery.IndexQueryHeaderTest.testNoCollectionsDisablesBothDropdownsAndShowsMessage[wasmJs, browser, ChromeHeadless156.0.8078.4]`
- First error: `AssertionError: Failed: assertExists. Expected exactly '1' node but could not find any node that satisfies: (Text + InputText + EditableText contains 'No collections are available' (ignoreCase: false))`.
- Source: `ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/indexquery/IndexQueryHeaderTest.kt:52`. The header renders the string at `IndexQueryHeader.kt:114-121` when collections are empty and not loading.
- Same failure in the rerun, so it reproduces. Not diagnosed. No code was changed.

`IndexQueryHeaderTest` on JVM passes 6 of 6 in the same head (`jvmTest` XML, `IndexQueryHeaderTest[jvm]`). `IndexQueryStateHolderTest[jvm]` passes 3 of 3.

Full JVM run in `ui check`: 56 run, 0 failures, 0 errors, 2 skipped (fresh XML before the reruns).

## Screen render

- Method: a scratch JVM test (not committed, removed afterwards; worktree clean at `cf35ab0329`) rendered `IndexQueryHeader` inside `SolrTheme(useDarkTheme = false)` with collections `films`, `books`; selected `films`; handlers `/select` (`solr.SearchHandler`), `/update` (`solr.UpdateRequestHandler`); selected `/select`. Software renderer. Capture is the root, 1024 by 768.
- Screenshot: `material/screen-checks/SOLR-17849/header-populated.png`.
- Reference: Figma node `137-12066`, `material/figma/render/137-12066.png` (1920 px full-page frame).

Comparison:

- Collection field width: 256 px in the design (x 240 to 496), 256 px in the render (x 16 to 272). Matches.
- Request handler field width: 384 px in the design (x 504 to 888), and the code sets 384 dp on the wrapper. The render's outline is about 308 px (x 288 to 596). Differs. The cause is not confirmed; the inner text field does not fill the wrapper in this render.
- Handler class: shown at the right end of the closed field, in monospace, as in the design. Matches.
- Label text: the design says "Collection / Core"; the component says "Collections". Differs.
- Structure: two outlined fields side by side with legend-style labels on the border, as in the design. Matches in structure.
- Not confirmed: colors, font weights, and spacing against the design. The reference is a thumbnail-scale sample of the full page, and the render is offscreen, so colors are approximate and are not claimed as matched. The render is the header alone, without the page chrome.

## Verdict

- Browser leg: NOT green. One wasm test in `IndexQueryHeaderTest` fails deterministically, in the full run and in the rerun. The other wasm results in this check pass.
- Screen render: partial match. The collection width and the handler class match. The handler width (about 308 px against 384 px), the label text, and colors need a fix or a decision before the render can be called a match.
- No `drafts/pr-5257-limits-line-update.md` is written, because the browser leg is not green. Per the assignment, no code was fixed under this assignment.
