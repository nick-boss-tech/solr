# Report: new-ui-collections-configsets-round-1

Research only. Base read: apache/main c8ab71430f, `ui/` tree and the v2 API classes under `solr/core/src/java/org/apache/solr/handler/admin/api/` and `handler/configsets/`. Jira REST reads on 2026-10-24T04:39:36Z. Fork branches named below were checked with `git ls-remote`.

Summary: no collections code exists on main. Collections is a disabled sidebar entry. Configsets has a working Overview, create and import dialogs, and blank tabs. SOLR-17913 is done on a fork branch, not on main.

## SOLR-17912: Introduce Collections Management UI

(a) Ask: the Collections section of the Figma design, covering collections, aliases, shards and replicas, in a nested grid view and a table or list view. Open, labels `new-ui`, `ui`. Umbrella with no children linked.

(b) Exists today: the sidebar entry `MainMenu.Collections` (`ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/navigation/MainMenu.kt:33`) is disabled by `isSectionAvailable` (`views/navigation/NavigationSideBar.kt:221`). `components/main/MainComponent.kt:50` has `TODO Add CollectionsComponent once available`. No collections repository or domain model exists.

(c) Missing: every screen, repository and domain type. Server read endpoints exist: `GET /v2/collections` (`admin/api/ListCollections.java`), `GET /api/aliases` (`admin/api/ListAliases.java`), and the collection status API (`admin/api/CollectionStatus.java`). Write endpoints exist for alias create and delete, collection delete (`DELETE /v2/collections/collectionName`), shard create and delete, replica create and delete, and modify (`POST /v2/collections/collectionName`).

(d) Verdict: TOO BIG AS STATED. Do not claim the umbrella. Smallest first slice: enable the Collections sidebar entry and show a read-only list from `GET /v2/collections`, with the component in its own component package. Management actions come as children.

## SOLR-17913: Implement Collections Details Card

(a) Ask: a display-only details card for one collection, passed a collection name or a domain model. Open, labels `new-ui`, `newdev`, `ui`. Parent SOLR-17912.

(b) Exists today on main: nothing. On the fork, branch `solr-17913-collection-details-card` has commit `0a04d2239e` ("SOLR-17913: add the collection details card to the new UI"). The branch is recorded DONE in `claims/pool-ui-SOLR-17913.md`. Its field list comes from the Collections API, not Figma (per the vm2 handoff).

(c) Missing on main: the card is not merged, and there is no collection domain model. The fork branch has no host screen yet.

(d) Verdict: ALREADY DONE on a fork branch. The next step is a review of `0a04d2239e` and a decision on merging it, not new code. The rest of the ticket is the screen that hosts it, which depends on SOLR-17912.

## SOLR-17914: Implement Create Collection Dialog

(a) Ask: a dialog component with an interface, to create a collection from an existing configset. Name, shards, replication factor and configset selection. Advanced options hidden by default with proper labels. Inline error for a name that already exists, an error when creation fails, and a general error fallback. Tooltips and input limits are optional. Open, labels `new-ui`, `newdev`, `ui`. Parent SOLR-17912.

(b) Exists today: `views/configsets/CreateConfigsetDialog.kt` (110 lines, with `CreateConfigsetViewModel`) is a model for the form and the states. Configsets are listed by `GET api/configsets` (`components/configsets/data/HttpConfigsetsRepository.kt`), so the configset picker has a data source. The old UI's create form (`solr/webapp/web/js/angular/controllers/collections.js`, around lines 181 to 251) takes name, `shardNames`, `createNodeSet`, and either `replicationFactor` or the `nrtReplicas`, `tlogReplicas` and `pullReplicas` counts. I did not enumerate every advanced option.

(c) Missing: the dialog, its view model, the collections repository call and the error mapping. The action in the Collections section needs SOLR-17912. The server create endpoint is not confirmed: `admin/api/CreateCollection.java` is described as a V2 API, but its path was not found by the annotation scan. Confirm the path before coding.

(d) Verdict: TOO BIG AS STATED. Smallest first slice: the form alone with name, configset picker, shards and replication factor, plus the inline and general errors, with no advanced options and no host screen. Advanced options and tooltips become a child ticket. The dialog can be built and tested before the Collections screen exists.

## SOLR-17846: Introduce Configset Management in New UI

(a) Ask: the whole Configsets section, to be split into sub-tasks. Open, labels `new-ui`, `ui`. No children linked. Two comments from 2025-08-09 and 2025-08-10 (epugh, malliaridis) discuss the wide field-type table.

(b) Exists on main: Overview (`views/configsets/ConfigsetsOverviewContent.kt`, with the edit-solrconfig button at line 157), create and import dialogs, and the configset dropdown. The tabs Schema, UpdateConfig, Handlers and SearchComponents render a blank box (`ConfigsetsContent.kt`, per the vm2 handoff). Index / Query and Files are on fork branches only (`solr-17849-index-query-screen`, `solr-17887-configset-files`).

(c) Missing on main: the four blank tabs, the Files and Index / Query tabs, and the solrconfig editor.

(d) Verdict: TOO BIG AS STATED. The ticket itself says to split it. Smallest first slice: one blank tab, for example the read-only Schema, on the existing tab routing in `components/configsets/viewmodel/ConfigsetsRouteViewModel.kt`.

## SOLR-18222: Create Configset Overview Scene

(a) Ask: none stated. The description is empty. Open, labels `configset`, `new-ui`, `ui`. Parent SOLR-17846.

(b) Exists on main: `views/configsets/ConfigsetsOverviewContent.kt` and `components/configsets/viewmodel/ConfigsetsOverviewViewModel.kt`. The overview lists configsets and has the solrconfig action.

(c) Missing: the ticket text. Without it, the gap between the existing overview and the intended scene cannot be read.

(d) Verdict: NEEDS CHRISTOS. Question: is the intended scene the existing overview on main, or a different layout from the Configsets Figma (node 14-3248)?

## SOLR-18223: Implement editing for solrconfig.xml

(a) Ask: none stated. The description is empty. No labels. Parent SOLR-17846.

(b) Exists on main: only the "Edit solrconfig.xml" button (`ConfigsetsOverviewContent.kt:157`, string `action_edit_solrconfig`). Its handler `ConfigsetsOverviewViewModel.editSolrConfig` has no target screen. The server reads one file (`handler/configsets/GetConfigSetFile.java`), but `handler/configsets/` has no single-file write. Writes are whole-configset: `UploadConfigSet` takes a zip. The fork branch `solr-17887-configset-files` reads files through v1 `GET solr/{collection}/admin/file`.

(c) Missing: an editor screen, a load and save path, and a decision on how a single file is written back.

(d) Verdict: NEEDS CHRISTOS. Question: should a single-file save be a whole-zip re-upload through `api/configsets/{name}` (PUT), or a new server endpoint?

## Dependencies

- SOLR-17914 needs SOLR-17912 for its action. The dialog itself does not.
- SOLR-17913 needs SOLR-17912 for its host screen. It is already built on a fork branch.
- SOLR-18223 needs a write decision before any UI work. Its verdict is the one that blocks the others.
- SOLR-17846 and SOLR-18222 share the Configsets overview and tab routing. Settle 18222's scope before splitting 17846.

## Ranking as volunteer candidates for a first UI contribution

1. SOLR-17914: a self-contained form, with `CreateConfigsetDialog.kt` as a model. Start with the form-only slice. Confirm the create endpoint path first.
2. SOLR-17913: review and merge the existing fork branch `solr-17913-collection-details-card`. That is the fastest route to a merged collections change, but it is a review task, not new code.
3. SOLR-17846: take a blank Configsets tab as a child slice once it is split.
4. SOLR-17912: a read-only collections list, after the sidebar entry is enabled.
5. SOLR-18222: once the ticket text exists.
6. SOLR-18223: blocked on the write decision.

Top pick: SOLR-17914. It is a form with a model already in the tree, so a newcomer can start from the ticket text and the dialog file. It is not blocked on the Collections screen for the dialog itself.

## Not verified

- The v2 path for collection create. The class is described as V2 but its path was not found by the annotation scan.
- The exact list of old-UI advanced options. I checked the fields named above only.
- Whether the fork branch for SOLR-17913 builds on the current base. It is not on `c8ab71430f`.
- Javadocs for the configset classes (`UploadConfigSet`, `DeleteConfigSet`, `CloneConfigSet`) say "GET /v2/configsets". That looks like a copy error for write operations. Not checked on the wire.

Evidence: Jira REST JSON for the six tickets was read on 2026-10-24T04:39:36Z. Results are recorded here, not in a separate file.

DONE: 2026-10-24T04:39:36Z. Deliverable: `reports/new-ui-collections-configsets-round-1.md`, with the assignment status line and the claim DONE in the same push.
