🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-17887

## What happens today

**`ui/shared` has no composable that shows a folder hierarchy.** The module at the merge-base has no file tree: [no `FileTree` file exists under `ui/` at `c8ab71430f`](https://github.com/nick-boss-tech/solr/tree/c8ab71430f/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/files). A screen that needs nested, expandable folders has to build one from scratch.

## What this change does

**A reusable `FileTree` composable adds nested folders with expand, collapse, and selection.** Folders open and close from the chevron or a double click on the folder name. A click or double click on any row selects that row, and the component keeps the selection. A single click on any row calls `onItemClick`, and a double click calls `onItemDoubleClick`. The `selectedPath` input sets the starting selection, and a later change to it replaces the selection. The expanded state stays inside the component. The data model, `FileTreeNode` with `FileTreeFile` and `FileTreeFolder`, has no API dependency.

Behavior change, stated openly: [a single click reports after the platform double-click window](https://github.com/nick-boss-tech/solr/blob/e443d1644caecebf4bba1e0f16347532d9815b81/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/files/FileTree.kt#L137), because `combinedClickable` must wait to tell a single click from a double click. Selection and `onItemClick` both wait for that window.

Christos Malliaridis scoped this ticket and asked for the PR in his [Jira comment](https://issues.apache.org/jira/browse/SOLR-17887?focusedCommentId=18133017). He said he will review it.

## Proof

**The new tests check selection, but they cannot fail on the base.** The component is new, so there is no base code to run against. The proof is inconclusive by construction. The click-selection test fails when the selection update is removed from the click handler.

[`FileTreeTest`](https://github.com/nick-boss-tech/solr/blob/e443d1644caecebf4bba1e0f16347532d9815b81/ui/shared/src/commonTest/kotlin/org/apache/solr/ui/views/files/FileTreeTest.kt#L37-L130) passes 8 of 8 on the JVM and 8 of 8 in the browser wasm run. Verified 2026-10-27 at head `e443d16`, with a fresh run and no build cache.

The full `./gradlew -p ui check` passes at the same head: JVM tests 55 run, 0 failures, 2 skipped; browser wasm tests 59 run, 0 failures, 3 skipped; spotless passes.

## Changelog

[changelog/unreleased/SOLR-17887.yml](https://github.com/nick-boss-tech/solr/blob/e443d1644caecebf4bba1e0f16347532d9815b81/changelog/unreleased/SOLR-17887.yml)

## Limits

- A chevron click toggles the folder and does not select it. Selection comes from a click on the row, outside the chevron. Selecting the folder from its chevron is a possible follow-up.
- No screen hosts the component yet. The Figma references were compared at the level of structure (chevron, folder icon, indented children), not color, spacing, or selected-row style. A screen check is still open.
- Nothing in the app uses `FileTree` yet. Christos asked that the components that use it come in later tickets. This PR does not add an integration. A follow-up ticket for that is Christos's to create, and we can open the PR for it on request.
- Out of scope per the ticket: right-click menus, search, and the file viewer (SOLR-17911, separate branch).

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
