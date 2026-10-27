🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-17887

## What happens today

**`ui/shared` has no composable that shows a folder hierarchy.** The module at the merge-base has no file tree: [no `FileTree` file exists under `ui/` at `c8ab71430f`](https://github.com/nick-boss-tech/solr/tree/c8ab71430f/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/files). A screen that needs nested, expandable folders has to build one from scratch.

## What this change does

**A reusable `FileTree` composable adds nested folders with expand and collapse.** Folders open and close from the chevron or a double click on the folder name. A single click on any row calls `onItemClick`, and a double click calls `onItemDoubleClick`. The caller owns the selection through `selectedPath`. The expanded state stays inside the component. The data model, `FileTreeNode` with `FileTreeFile` and `FileTreeFolder`, has no API dependency.

Behavior change, stated openly: [a single click reports after the platform double-click window](https://github.com/nick-boss-tech/solr/blob/a22da1bfe72399204b6fd87b8b084e19e4a9bd90/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/files/FileTree.kt#L128), because `combinedClickable` must wait to tell a single click from a double click. Selection lags a single click by that window.

Christos Malliaridis scoped this ticket and asked for the PR in his [Jira comment](https://issues.apache.org/jira/browse/SOLR-17887?focusedCommentId=18133017). He said he will review it.

## Proof

**The tests cover the behavior, but they cannot fail on the base.** The component is new, so there is no base code to run against. The proof is inconclusive by construction.

[`FileTreeTest`](https://github.com/nick-boss-tech/solr/blob/a22da1bfe72399204b6fd87b8b084e19e4a9bd90/ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/files/FileTreeTest.kt#L36-L90) passes 5 of 5 on the JVM with the software renderer. Verified 2026-10-24 at head `9c7f72aa0e`; the PR head has the same component files. The five tests are `testHidesChildrenUntilFolderIsExpanded`, `testChevronExpandsAndCollapsesFolder`, `testDoubleClickOnFolderNameExpandsFolder`, `testSingleClickReportsItem`, and `testDoubleClickReportsItem`. `spotlessCheck` passes.

## Limits

- No screen hosts the component yet, so it has not been checked on a screen.
- Nothing in the app uses `FileTree` yet. Christos asked that the components that use it come in later tickets. This PR does not add an integration. A follow-up ticket for that is Christos's to create, and we can open the PR for it on request.
- Out of scope per the ticket: right-click menus, search, and the file viewer (SOLR-17911, separate branch).

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
