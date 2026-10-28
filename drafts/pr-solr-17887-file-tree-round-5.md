Title: [Ready] SOLR-17887: Add a reusable FileTree component to the new Admin UI

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

## Ready for review, but held as a draft due to the project's open-PR cap; I'll mark it open on request.

https://issues.apache.org/jira/browse/SOLR-17887

## What happens today

**`ui/shared` has no composable that shows a folder hierarchy.** No `FileTree` file exists under `ui/` at the merge-base [`c8ab71430f`](https://github.com/nick-boss-tech/solr/tree/c8ab71430f/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/files). A screen that needs nested, expandable folders has to build one from scratch.

## What this change does

**A reusable `FileTree` composable adds nested folders with expand, collapse, and selection.** Folders open and close from the chevron or a double click on the folder name. A click or double click on any row selects that row, and the component keeps the selection. A single click calls `onItemClick`, and a double click calls `onItemDoubleClick`. The `selectedPath` input sets the starting selection, and a later change to it replaces the selection. The selected row and its file icon use the app's selected colors. The expanded state stays inside the component. The data model, `FileTreeNode` with `FileTreeFile` and `FileTreeFolder`, has no API dependency.

Behavior change, stated openly: [a single click reports after the platform double-click window](https://github.com/nick-boss-tech/solr/blob/025164fb98b152c688c139379b0cc4a68cbc3d8a/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/files/FileTree.kt#L139), because `combinedClickable` must wait to tell a single click from a double click. Selection and `onItemClick` both wait for that window.

Christos Malliaridis scoped this ticket and asked for the PR in his [Jira comment](https://issues.apache.org/jira/browse/SOLR-17887?focusedCommentId=18133017). He said he will review it.

## Proof

**The new tests check selection, but the component is new, so they cannot fail on the base.** The proof is inconclusive by construction. The click-selection test fails when the selection update is removed from the click handler.

[`FileTreeTest`](https://github.com/nick-boss-tech/solr/blob/025164fb98b152c688c139379b0cc4a68cbc3d8a/ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/files/FileTreeTest.kt#L42-L130) passes 8 of 8 on the JVM and 8 of 8 in the browser wasm run, verified 2026-10-27 at head `025164fb`, with a fresh run and no build cache.

The full `./gradlew -p ui check` passes at the same head: JVM tests 55 run, 0 failures, 2 skipped; browser wasm tests 59 run, 0 failures, 3 skipped; spotless passes.

A screen check renders the component inside the app theme and compares it with the design's file tree frames (Configsets Files and Cluster Zookeeper Files). The structure matches: chevron before the folder icon, indented children, and file rows. The selected row is close to the design's color but a little darker: sampled `#ED937E` in the render and `#FE9E85` in the design. The selected file icon is dark red in both: `#511104` in the render and `#550000` in the design. The unselected file icon is gray in the design, and the component uses the theme's text color. The thumbnails are too small to confirm that gray, so it is not claimed as matched.

## Limits

- A chevron click toggles the folder and does not select it. Selection comes from a click on the row, outside the chevron. Selecting the folder from its chevron is a possible follow-up.
- The screen check is an offscreen render on the JVM with the software renderer, not a run in the app. Colors come from design thumbnails, so they are approximate.
- The unselected file icon color is not confirmed against the design.
- Nothing in the app uses `FileTree` yet. Christos asked that the components that use it come in later tickets. This PR adds no integration. A follow-up ticket for that is Christos's to create, and we can open a PR for it on request.
- Out of scope per the ticket: right-click menus, search, and the file viewer (SOLR-17911, separate branch).

Changelog: [changelog/unreleased/SOLR-17887.yml](https://github.com/nick-boss-tech/solr/blob/025164fb98b152c688c139379b0cc4a68cbc3d8a/changelog/unreleased/SOLR-17887.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
