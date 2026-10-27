# Receipt: SOLR-17887 screen check and selected-icon fix

Written by vm5. Earlier receipts: `receipts/SOLR-17887-vm5-selection.md` (head `03d5efbf`), `receipts/SOLR-17887-vm5-ui-check.md` (head `e443d16`). Both are superseded for the head below.

- Branch: `nick-boss-tech/solr:solr-17887-file-tree`. Head: `025164fb98b152c688c139379b0cc4a68cbc3d8a` (ls-remote confirmed after the push). New commit on top of `e443d16`: "SOLR-17887: show the selected file icon in the selected content color". Change: `FileTree.kt` only, the file icon's tint is `onSecondaryContainer` when the row is selected.
- Gate: full `./gradlew -p ui check --no-daemon --no-build-cache` at the head, with `NODE_ENV` unset, host lock `/workspace/tools/.gradle-vm5.lock`. Logs: `/workspace/gates/logs/vm5/solr-17887-uicheck/solr17887-final/`.

## Results at 025164fb

| Step | Result | Evidence |
| --- | --- | --- |
| `ui check` | rc=0, `BUILD SUCCESSFUL` | `ui-check.log` |
| `:shared:jvmTest` (executed 23:12) | 55 run, 0 failures, 0 errors, 2 skipped; `FileTreeTest` 8 of 8 | `ui/shared/build/test-results/jvmTest/` |
| `:shared:wasmJsBrowserTest` (executed 23:14) | 59 run, 0 failures, 0 errors, 3 skipped; `FileTreeTest` 8 of 8 | `ui/shared/build/test-results/wasmJsBrowserTest/` |
| `:shared:spotlessCheck` | pass | `ui-check.log` |
| `git diff --check e443d16..HEAD` | clean | - |

Partial run before the final one: `solr17887-screen3/` ran spotless and `FileTreeTest` 8 of 8 plus the screen check, all green, at the same change.

## Screen check

- Method: a scratch test (not committed) rendered the component inside the app's `SolrTheme` in the JVM test runner with the software renderer, expanded the ZooKeeper folder, selected a configset file, and captured the root to PNG. The result is in `material/ui-screens/solr-17887/`. Before-fix captures are in the same folder.
- Compared against the design frames `227-9235` (Configsets Files) and `13-3977` (Cluster Zookeeper Files), which are in `material/figma/`.
- Structure: matches. Chevron before the folder icon, indented children, file rows with file icons, no header inside the component.
- Selected row: the theme's `secondaryContainer` is `#FF9E87`. The design's selected row samples at `#FE9E85`. The render sample was `#ED937E`, a little darker; the press overlay from the click is the likely cause, not verified.
- Selected file icon: design `#550000`. Before the fix the component showed the text color (`#1B1A1A`). After the fix `#511104`, matching the theme's `onSecondaryContainer` (`#561103`).
- Unselected file icon: design `#605C5B` (darkest sample from a 520 by 572 thumbnail). The component uses the theme's text color. No theme color matches the sample, and thumbnail downscaling makes the sample unreliable, so this is left as not matched.
- Chevron click: verified with `item_zookeeper` semantics that a chevron click does not select the folder (`ZK_SELECTED_AFTER_CHEVRON=false`). The gray tint on that row in the capture is the row's press indication.

## Decisions (decision ladder)

- **Selected file icon color (rung 1 and rung 3).** Code read: `FileTypeIcon` takes no tint. Design read: selected icon is dark red. Stated intent: criterion 7, "matches the look and feel". The fix uses the theme's paired color, `onSecondaryContainer`, which the design matches. Recorded choice: match the design with the theme token.
- **Unselected icon color (rung 2).** Not changed. The sample is not reliable enough to pick a token. Left for a full-size design export; not a blocker for the PR draft, which states it as unverified.

## Limits

- The screen check is an offscreen JVM render, not an in-app run. No screen hosts the component.
- Colors from the design are read from thumbnails, so they are approximate.

## Summary for the PR draft

Head `025164fb` passes the full `ui check`, with `FileTreeTest` 8 of 8 on both runners. The screen check matches the design structure and the selected colors. The round-4 draft is `drafts/pr-solr-17887-file-tree-round-4.md`. The PR is not opened; opening needs Nick's go.
