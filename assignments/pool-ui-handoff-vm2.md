# Handoff: new Admin UI tickets worked by vm2 (round 1)

Status: HANDED. Any pool Linux host with the ui tags may take the open items below. Each item is claimed under `claims/` before work starts (WORKFLOW.md).

Posted by vm2, 2026-10-19, at the direction of Nick. Companion to `assignments/pool-ui-tickets-round-1.md`.

## What is done (component scope, on fork branches)

| Ticket | Branch (fork) | Commit | Claim on coordination | Notes |
| --- | --- | --- | --- | --- |
| SOLR-17911 file content viewer | solr-17911-file-content-viewer | 8047b99ef09 | none yet | Needs a claim and a reviewer. |
| SOLR-17887 file tree | solr-17887-file-tree | 9c7f72aa0e5 | pool-ui-SOLR-17887 DONE | Lazy folders still open (see below). |
| SOLR-17909 breadcrumb | solr-17909-breadcrumb | fdb53f6a024 | pool-ui-SOLR-17909 DONE | Criteria rewritten (no Decompose). Nick to confirm. |
| SOLR-17913 collection details card | solr-17913-collection-details-card | 0a04d2239ee | pool-ui-SOLR-17913 DONE | Field list is the Collections API's, not Figma. Nick to confirm. |
| SOLR-17849 Index & Query header | solr-17849-index-query-header | fb51fbcb235 | pool-ui-SOLR-17849 DONE | Placeholder shows only on focus (Material 3). Nick to confirm. |
| SOLR-17850 solr.SearchHandler form | solr-17850-search-handler-form | 887778ce5d8 | pool-ui-SOLR-17850 DONE | Execution is a callback; no request preview. |
| Index & Query screen (hosts 17849 and 17850) | solr-17849-index-query-screen | b91f75ed935 | pool-ui-SOLR-17849-screen DONE | Hosted on Configsets > Index / Query. |
| Configsets > Files (hosts 17887 and 17911) | solr-17887-configset-files | 80e97a1e8e4 (tip) | pool-ui-configset-files DONE | Tip includes all branches above. |

Each branch has a draft PR description in `pr-draft/`. Pool hosts do not open GitHub PRs.

## Decisions already made

- Base for every branch: apache/main c8ab71430f.
- The Index & Query header and request form sit on the existing Configsets > Index / Query tab. Nick may move them later.
- Configsets > Files gets its data from the v1 handler `GET solr/{collection}/admin/file`, through the first collection whose configName matches the configset (found from `admin/collections?action=CLUSTERSTATUS`). No server change.
- Route change: selecting a Configsets tab now switches content. Before, every tab showed Overview.
- GL assumption (Nick, 2026-10-19): fixes do not touch GL. Tests run on Skiko's software renderer, so a GL-only rendering bug will not show. None of the branches is flagged `gl-blast-radius`. A GL host or CI run is still needed before any of these merge.

## Open items, in priority order

1. **Configsets with no collection cannot list files.** The only file-listing endpoint is per collection. The Files tab shows a message instead. Needs an owner decision on a server endpoint (for example a v2 `GET /api/configsets/{name}/files` JSON listing). Do not add it without Nick's go-ahead.
2. **Eager folder loading in the Files tree.** `HttpConfigsetFilesRepository.listFiles` loads every folder recursively at selection. Slow for large configsets. Fix: load a folder's children on expand, which needs `FileTree` to take a loader callback.
3. **Other Configsets tabs are empty.** Schema, Update Configuration, Request Handlers, and Search Components show a blank box (`Box(Modifier.fillMaxSize())` in `ConfigsetsContent.kt`). Each needs its own screen.
4. **Collections section is disabled.** `NavigationSideBar.kt` `isSectionAvailable` enables only Cluster, Configsets, Environment, and Logging. The Collections tickets (SOLR-17912 umbrella, SOLR-17914 create dialog) need that section built. The collection details card (17913) and breadcrumb (17909) have no host yet.
5. **Request form and response are not scrollable on a short window.** The Configsets page is now bounded (see the layout note below), so the form and response can be cut off at small window heights. Give the Index / Query page its own scroll once the page is bounded.
6. **Two confirmations for Nick:** the 17909 rewritten criteria, and the 17849 placeholder behavior.
7. **SOLR-17911 needs a claim and a reviewer.** The branch is ready; the gate is the software-renderer test.
8. **Stray branches** `solr-17250-draft-open` and `solr-17169-draft-open` on the fork await Nick's delete decision. Not touched.
9. **Out of this ticket set:** SOLR-17861 (updateLicenses for the KMP module, a Gradle task) and SOLR-17885 (user auth, reopened). SOLR-12863 and SOLR-12613 stay out of scope.

## Layout note (read before touching the Configsets screen)

- `views/main/MainContent.kt`: the Configsets page is `Modifier.fillMaxSize()`, **not** wrapped in `verticalScroll`. A vertical scroll there gives children unbounded height. Under unbounded height, `verticalScroll` children, `LazyColumn`, and `weight` all fail silently: the content goes blank and the accessibility tree shows zero-size nodes. This was the root cause of the blank Configsets tabs.
- `views/configsets/ConfigsetsContent.kt`: the scene is a `Box` with the navigation display in `Box(Modifier.weight(1f))`.
- `views/configsets/ConfigsetsOverviewContent.kt`: the Overview scrolls (`verticalScroll`), which works on the bounded page.

## Test and gate facts

- Command: `REPO=<checkout> material/ui-test-setup/run-ui-jvm-tests.sh <test filter>`. It builds a stub `libEGL.so.1` (needs `gcc`) and sets `-Dskiko.renderApi=SOFTWARE_FAST`.
- Full `:shared:jvmTest` on `solr-17887-configset-files` at 80e97a1e8e4: 23 suites, no failures. `:shared:spotlessCheck` passes.
- Build: `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1 GRADLE_USER_HOME=/workspace/gradle-home ./gradlew -p ui :shared:jvmTest`.
- Gradle needs `gradle.properties` from `gradle/template.gradle.properties` with `@MAX_WORKERS@` set to 4 and `@TEST_JVMS@` to 2.
- Compose test API: `assertExists`, `assertDoesNotExist`, and `performScrollTo` are members of the node interaction, not imports. `ExposedDropdownMenu` is a member of the dropdown box scope. Use `waitForIdle()` before reading state the test set in a callback.

## Browser smoke (what worked, and how)

- Solr node: build `./gradlew :solr:packaging:assemble -x test -PdisableJsClient=true` (the JS client bundle fails on this host). Start `bin/solr start -c -p 8983`, then `bin/solr create -c smoke -shards 1 -rf 1`.
- Web app: `./gradlew -p ui :webApp:wasmJsBrowserDistribution`, about 8 minutes the first time. Copy `ui/webApp/build/dist/wasmJs/productionExecutable/*` over `solr-webapp/webapp/ui/` in the Solr build, replacing the nightly bundle.
- Browser: open `http://127.0.0.1:8983/solr/ui/`, use the Connect button. Compose clicks by ref are unreliable after a re-render, so use mouse coordinates: move, down, up. Viewport 1280x1100 keeps the handler menu on screen. Reload after installing a new bundle, or the old page stays open.
- Coordinates used: sidebar Configsets (106, 368); tabs Overview (263, 25), Files (376, 25), Index / Query (737, 25); configset dropdown (368, 102).
- Single click on a tree item reports after the double-click window, so selection lags by that delay.

## Environment notes

- `/tmp` does not survive a container restart. The Solr node, worktrees, stub, and browser session were lost once. Rebuild as above.
- Branches and this doc survive: all are on the fork and on `coordination`.

## Pending wake

vm2 had a scheduled wake (22:55Z) that would resume the UI work. This doc hands the work off, so that wake should be treated as stale.
