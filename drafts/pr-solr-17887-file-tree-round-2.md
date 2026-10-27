🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-17887

## What happens today

**`ui/shared` has no composable that shows a folder hierarchy.** The module at the merge-base has no file tree: [no `FileTree` file exists under `ui/` at `c8ab71430f`](https://github.com/nick-boss-tech/solr/tree/c8ab71430f/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/files). A screen that needs nested, expandable folders has to build one from scratch.

## What this change does

**A reusable `FileTree` composable adds nested folders with expand, collapse, and selection.** Folders open and close from the chevron or a double click on the folder name. A click or double click on any row selects that row, and the component keeps the selection. A single click on any row calls `onItemClick`, and a double click calls `onItemDoubleClick`. The `selectedPath` input sets the starting selection, and a later change to it replaces the selection. The expanded state stays inside the component. The data model, `FileTreeNode` with `FileTreeFile` and `FileTreeFolder`, has no API dependency.

Behavior change, stated openly: [a single click reports after the platform double-click window](https://github.com/nick-boss-tech/solr/blob/03d5efbfe6a06e49fdf5d5fa3c1f50d7457ce528/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/files/FileTree.kt#L135), because `combinedClickable` must wait to tell a single click from a double click. Selection and `onItemClick` both wait for that window.

Christos Malliaridis scoped this ticket and asked for the PR in his [Jira comment](https://issues.apache.org/jira/browse/SOLR-17887?focusedCommentId=18133017). He said he will review it.

## Proof

**The new tests check selection, but they cannot fail on the base.** The component is new, so there is no base code to run against. The proof is inconclusive by construction. The click-selection test fails when the selection update is removed from the click handler.

[`FileTreeTest`](https://github.com/nick-boss-tech/solr/blob/03d5efbfe6a06e49fdf5d5fa3c1f50d7457ce528/ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/files/FileTreeTest.kt#L36-L128) passes 8 of 8 on the JVM with the software renderer. Verified 2026-10-27 at head `03d5efbf`, with a fresh run and no build cache. The eight tests are `testHidesChildrenUntilFolderIsExpanded`, `testChevronExpandsAndCollapsesFolder`, `testDoubleClickOnFolderNameExpandsFolder`, `testSingleClickReportsItem`, `testDoubleClickReportsItem`, `testSingleClickSelectsItem`, `testSelectedPathSelectsItemOnFirstComposition`, and `testChangedSelectedPathReplacesSelection`. `spotlessCheck` passes.

## Limits

- A chevron click toggles the folder and does not select it. Selection comes from a click on the row, outside the chevron. Selecting the folder from its chevron is a possible follow-up.
- No screen hosts the component yet, so it has not been checked on a screen or against the Figma designs.
- The tests run on the JVM only. The browser tests in CI have not run on this change.
- Nothing in the app uses `FileTree` yet. Christos asked that the components that use it come in later tickets. This PR does not add an integration. A follow-up ticket for that is Christos's to create, and we can open the PR for it on request.
- Out of scope per the ticket: right-click menus, search, and the file viewer (SOLR-17911, separate branch).

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
