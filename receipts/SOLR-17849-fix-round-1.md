# Receipt: SOLR-17849 fix round 1 (vm5)

Follows `receipts/SOLR-17849-browser-check-round-1.md`, which was not green. Owner direction: patch the wasm failure for PR #5257, the rest at lower priority.

- Branch: `nick-boss-tech/solr:solr-17849-header-submit`. Fast-forward from `cf35ab0329` to `d55bb451eb` (one commit, author Nick Shanin, no trailers).
- Gate: full `./gradlew -p ui check --no-daemon --no-build-cache` at `d55bb451eb` content (run before the commit; the commit adds nothing else). Log `/workspace/gates/logs/vm5/solr-17849-browser/ui-check-fix1.log`, rc=0, `BUILD SUCCESSFUL`.
- Counts, fresh XML: JVM 56 run, 0 failures, 2 skipped. Browser wasm 60 run, 0 failures, 3 skipped. `IndexQueryHeaderTest` 6 of 6 on both runners. `IndexQueryStateHolderTest` 3 of 3 on both runners.

## Changes

1. Wasm failure. Diagnosis (rung 2, probe): the message node exists, but its text is empty when the test asserts (`SemanticsProperties.Text` is `[]`). The message is a string resource, and on wasm it is still empty at that point. The test's wait does not let the browser finish loading it. Fix: the empty-state test checks the node by tag `index_query_no_collections` instead of its text. Limit: the message text is not checked in a browser.
2. Handler width. The wrapper was 384 dp, but the outlined text field did not fill it (rendered about 308 px). Fix: the text field gets `fillMaxWidth()`.
3. Label. The design says "Collection / Core"; the header used the sidebar's "Collections" string. Fix: new string `label_collection_core`; the sidebar label is unchanged.

## Screen render after the fix (offscreen, software renderer)

- Screenshot: `material/screen-checks/SOLR-17849/header-populated-fix1.png`.
- Collection field 256 px (x 16 to 272). Handler field 384 px (x 288 to 672). Label "Collection / Core". Handler class in monospace at the right end of the closed field.
- Colors are approximate; not claimed as matched.

## PR body

PR #5257 body edited (allowed under AGENTS.md) and read back: the browser result replaces "Nothing was run in a browser"; the proof cites head `d55bb451eb` and the counts above; the screen-render line now reports the measured widths. The first Limits line about the empty-state test is included.

## Not done

- No live browser run in the app; the wasm test run is headless Chromium only.
- Colors against the design are not confirmed.
