# SOLR-17887, SOLR-17911: Configsets > Files screen (new UI)

Draft PR description. Not opened as a GitHub PR. Hosts the file tree (SOLR-17887) and the file content viewer (SOLR-17911) on the Configsets Files tab.

## What

- The Files tab shows the configset dropdown (shared with Overview), the file tree of the selected configset, and the content of the selected file with line numbers.
- Data source: the existing v1 handler `GET solr/{collection}/admin/file` lists a configset's files (`wt=json`), subfolders with `file=<dir>`, and raw content with `file=<path>`. The collection is the first one whose `configName` matches the configset, found from `GET solr/admin/collections?action=CLUSTERSTATUS`. No server change.
- A configset with no collection shows a message, because the handler is only served per collection and this baseline has no other endpoint for configset files.
- Folders load recursively at selection (smoke-sized configsets only; a large configset will be slow until the tree loads folders lazily).

## Layout change

- `MainContent.kt`: the Configsets page no longer sits in a vertical scroll. The scroll gave the page unbounded height, which blanked every Configsets tab that needs bounded height (Index / Query, Files) and the Overview after a tab switch. The page now fills the main area. Long Overview lists cannot scroll on this page yet; a follow-up can add a scroll inside the Overview.
- `ConfigsetsContent.kt`: the `FlowRow` becomes a `Box`, and the navigation display takes the remaining height.

## Node types moved

`FileTreeNode`, `FileTreeFile`, `FileTreeFolder` move from `views/files` to `domain`, so the data layer can use them. `FileTreeTest` imports them from there.

## Files

- `components/files/data/ConfigsetFilesRepository.kt`, `HttpConfigsetFilesRepository.kt`
- `components/files/viewmodel/ConfigsetFilesStateHolder.kt`, `ConfigsetFilesViewModel.kt`
- `views/configsets/ConfigsetFilesContent.kt`
- `domain/FileTreeNode.kt`
- Wiring: `ConfigsetsComponent`, `DefaultConfigsetsComponent`, `PreviewConfigsetsContent`, `ConfigsetsContent`, `MainContent`
- Tests: `HttpConfigsetFilesRepositoryTest` (3), `ConfigsetFilesStateHolderTest` (4)

## Testing

- The full `:shared:jvmTest` run passes (23 suites, no failures) on the software renderer (see `material/ui-test-setup/`), including `FileTreeTest` 5 of 5 and `FileContentViewerTest` 3 of 3.
- `:shared:spotlessCheck` passes.
- Browser smoke on a Solr 11.0.0-SNAPSHOT node built from c8ab71430f: the Files tab lists `smoke`'s files with `lang` first, `solrconfig.xml` shows numbered content, and the `lang` folder expands.

## Blast radius

Not flagged. Layout and state, and GET calls to existing endpoints. The file tree and viewer use layout, click handling, and vector icons only.
