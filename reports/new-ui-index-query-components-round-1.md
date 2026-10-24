# Report: new-ui-index-query-components-round-1

- Assignment: `assignments/new-ui-index-query-components-round-1.md`. Claim: `claims/new-ui-index-query-components-round-1.md` (vm4).
- Scope held: research only. No code changed, no Jira or GitHub comment, no PR, no branch push except this closeout.
- Evidence: Jira REST JSON and comments for the ten tickets (the eight in scope plus SOLR-17896 and SOLR-18493, which the set depends on) at `/workspace/gates/logs/vm4/new-ui-index-query/` on vm4.
- Code compared: `origin/main` at `498a09aea9` (fork, 2026-10-07). Figma designs were not opened (no browser used), so any requirement that lives only in Figma is marked not verified.

## Summary

| Ticket | Title | Verdict | Smallest first slice |
|---|---|---|---|
| SOLR-17848 | Introduce Index & Query in New UI | TOO BIG AS STATED | SOLR-17849 header, after the section is wired |
| SOLR-17849 | Index & Query section header | READY TO VOLUNTEER (confirm with Jane Sandberg first) | Two dropdowns with stubbed data and the section route |
| SOLR-17850 | solr.SearchHandler UI form | NEEDS CHRISTOS | none until the parameter set is settled |
| SOLR-17886 | Reusable Components (umbrella) | TOO BIG AS STATED | Use the child tickets, not the umbrella |
| SOLR-17660 | Basic list component | READY TO VOLUNTEER | Stateless list composable with a preview, no logging wiring |
| SOLR-17887 | File Tree Component | TOO BIG AS STATED | UI-free tree model with tests, then the composable |
| SOLR-17909 | Breadcrumb Navigation Component | READY TO VOLUNTEER | Breadcrumb composable over a child-stack list, with a preview |
| SOLR-17911 | File Content Viewer Component | READY TO VOLUNTEER (selection is the risk) | Stateless viewer over a fixed string, with a preview |

## Dependencies and state across the set

- SOLR-17896 (Reusable navigation tabs, parent SOLR-17886) is Closed, Fixed. The component is on `origin/main` as `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/navigation/NavigationTabs.kt` and `components/navigation/TabNavigationComponent.kt`. It was the blocker of SOLR-17849, so that blocker is gone.
- SOLR-18493 (rename of the section, child of SOLR-17848) is Resolved, Fixed. Jane Sandberg's commits `2bea0aef96` (branch_10x) and `02d093afcf` (branch_10_1) landed on 2026-10-08. The commit it was cherry-picked from, `8f926b6532` (#4994), is not on this fork's `origin/main`. On this fork the section is still called "Queries & Operations" (`ui/shared/src/commonMain/composeResources/values/strings.xml:59`).
- The section route is not wired. `MainMenu.QueriesAndOperations` exists (`views/navigation/MainMenu.kt:34`), but its child and configuration are commented out in `components/main/integration/DefaultMainComponent.kt:141-143` and `:195`, `:214` with "TODO Uncomment once QueriesAndOperations available". `MainComponent.kt:75-76` has the matching TODO. So SOLR-17849 needs the route as well as the header.
- The configset Files, Schema, UpdateConfig, IndexQuery, Handlers, and SearchComponents scenes exist as enum values and back-stack entries (`components/configsets/viewmodel/ConfigsetsRouteViewModel.kt:40-48`, `:77-102`). `views/configsets/ConfigsetsContent.kt` only renders the Overview entry in its `NavDisplay`. So the configset Files tab, which SOLR-17887 and SOLR-17911 target, is not rendered yet.
- Labels: `newdev` is on SOLR-17849, SOLR-17850, SOLR-17887, SOLR-17909, and SOLR-17911. It is not on SOLR-17660, SOLR-17848, or SOLR-17886.
- Ordering: Christos (2025-08-09, comment on SOLR-17850) says SOLR-17850 comes after SOLR-17849. Jane Sandberg commented on SOLR-17849 on 2025-10-23 that she would like to work on it, with no assignee recorded. Christos answered her naming question on 2025-10-24. Nothing in the tickets says whether she still intends to take it.

## SOLR-17848: Introduce Index & Query in New UI

(a) Ask: define, design, and implement every section of the Index & Query area (Figma node 23-5124). Open, Major, no assignee. Subtasks: SOLR-17849, SOLR-17850, SOLR-18493.

(b) Exists today: the section name and the main-menu entry (see dependencies above). No Index & Query component or view exists under `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/components/` or `views/`.

(c) Missing: all section content. The ticket closes only when every section is done, so it is an umbrella.

(d) Verdict: TOO BIG AS STATED. Smallest first slice: SOLR-17849, once the route is wired.

## SOLR-17849: Index & Query section header

(a) Ask: two dropdowns in the section header, one for the collection and one for the request handler, with loading, empty, and disabled states. Open, Major, labels `new-ui`, `newdev`, `ui`.

(b) Exists today: the section name ("Queries & Operations") and main-menu entry (see dependencies). The blocker SOLR-17896 is in place (`views/navigation/NavigationTabs.kt`). The drop-down pattern exists in the configsets view (`views/configsets/ConfigsetsDropdown.kt`), which can be used as a model. Configset data is loaded through `components/configsets/data/HttpConfigsetsRepository.kt`, which calls `api/configsets` (lines 35, 54, 65).

(c) Missing:
- A collection-list API client. No `ui/` code calls a collections endpoint (grep of `commonMain` finds only `api/node/system`, `api/node/properties`, and `api/configsets`).
- A request-handler list. The likely source is the server's v2 config read, `GET /v2/c/{collection}/config/{component}` (`solr/core/src/java/org/apache/solr/handler/admin/api/GetConfigAPI.java`, the `/config/{component}` endpoint). Not verified: that `requestHandler` is the component name the handler accepts, and the shape of the response.
- The section route (see dependencies).
- Not verified: the Figma placeholder text and the exact dropdown layout.

(d) Verdict: READY TO VOLUNTEER, with one check first. Jane Sandberg asked for this ticket on 2025-10-23 and nothing records that she dropped it, so confirm with her before claiming publicly. The newcomer work is the two API clients (copy the `HttpConfigsetsRepository` pattern), the dropdowns, and the route. Smallest first slice: the dropdowns and route with stubbed collection and handler lists, then the API calls.

## SOLR-17850: solr.SearchHandler UI form

(a) Ask: a basic form for `solr.SearchHandler` (`/select`) with abbreviations shown beside written-out labels, add and remove for array fields, a required query field, default values, normal and debug execution, and "Clear all". Open, Major, labels `new-ui`, `newdev`, `ui`.

(b) Exists today: no query or select code in `ui/`. Grep of `commonMain` finds no `select` or request-handler call. Views for text fields exist (`views/components/SolrOutlinedTextField.kt`).

(c) Missing: the field set. The ticket does not list the request parameters; it points to Figma node 156-4776, which was not opened here. Also missing: a query execution path, and the request preview is out of scope.

(d) Verdict: NEEDS CHRISTOS. Question for the ticket author: which request parameters must the form cover, and does the list match Figma node 156-4776? Christos also asked that this follow SOLR-17849.

## SOLR-17886: Implement Reusable Components in New UI (umbrella)

(a) Ask: implement every reusable component in the Figma reusable-components set. Open, Major, no parent. Children: SOLR-17660, SOLR-17887, SOLR-17896 (Closed, Fixed), SOLR-17909, SOLR-17911. Assignee: Christos Malliaridis.

(b) Exists today: SOLR-17896 landed as `views/navigation/NavigationTabs.kt` and `components/navigation/TabNavigationComponent.kt`. Other shared views: `views/components/SolrButton.kt`, `SolrCard.kt`, `SolrLinearProgressIndicator.kt`, `SolrOutlinedTextField.kt`, `SolrTextButton.kt`.

(c) Missing: list, file tree, breadcrumb, and file viewer (the four open children).

(d) Verdict: TOO BIG AS STATED. The ticket closes only when all known components exist. Take the children, not the umbrella.

## SOLR-17660: Implement basic list component in Admin UI

(a) Ask: a reusable list with a filter input on top, dynamic actions left and right of the input, a clear button that hides when the input is empty, and horizontal and vertical scrolling. Use the logging section as the first consumer. No headers or sorting. Open, Major, labels `new-ui`, `ui`, parent SOLR-17886.

(b) Exists today: the input to build on, `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/components/SolrOutlinedTextField.kt`, takes `leadingIcon`, `trailingIcon`, and `singleLine`. Preview harness: `ui/shared/src/jvmMain/kotlin/org/apache/solr/ui/preview/`. The logging section is a placeholder: `views/logging/LoggingContent.kt:30-31` renders the text "Logging section" with a TODO, and `components/logging/LoggingComponent.kt` is an empty interface.

(c) Missing: the list composable itself, and a data source for logging. The ticket's logging consumer cannot be built until log data exists, so the first slice does not include it.

(d) Verdict: READY TO VOLUNTEER. The acceptance criteria are concrete, the input exists, and the preview harness lets it be tested without a server. Smallest first slice: a stateless list composable with a preview and fake rows. Wiring to logging waits for a logging data source. The ticket allows an existing Compose library component if one fits.

## SOLR-17887: Implement File Tree Component

(a) Ask: a reusable IDE-style file tree: chevrons for folders, expand and collapse by chevron or double-click, file extensions shown, single and double click reported as outputs, single click selects. Open, Major, labels `new-ui`, `newdev`, `ui`, parent SOLR-17886.

(b) Exists today: `views/files/FileSelector.kt` is a single-file import picker with a clear button, not a tree, so it cannot be extended into one. `views/files/FileTypeIcon.kt` maps extensions to icons, which can be reused for the extension display. The configset Files tab that would host the tree is not rendered (see dependencies).

(c) Missing: the tree model (folder state, flattening to visible rows, selection), the composable, and the file-listing source. No `ui/` code lists files; `HttpConfigsetsRepository.kt` covers only create, import, and list configsets. Not verified: whether a server API lists configset files.

(d) Verdict: TOO BIG AS STATED. Smallest first slice: a UI-free tree model in `commonMain` (expanded state per folder, flatten to visible rows, selection) with tests in `commonTest`. The composable and the chevron and double-click behavior come next.

## SOLR-17909: Implement a Breadcrumb Navigation Component

(a) Ask: a reusable breadcrumb built from the Decompose child stack. Clicking an entry pops back to it and drops the layers after it. The last entry is highlighted and inert. Wraps by default. Open, Major, labels `new-ui`, `newdev`, `ui`, parent SOLR-17886. Its Collections use is optional for now.

(b) Exists today: the child stack is already used in the app: `components/main/MainComponent.kt:40`, `components/main/integration/DefaultMainComponent.kt:55`, and `views/main/MainContent.kt:51`. `views/components/` has no breadcrumb.

(c) Missing: the composable and its navigation call. The Decompose pop-to-index call is not verified against the library version in this repo, and the Collections route was not checked for use.

(d) Verdict: READY TO VOLUNTEER. The stack model is already in use, the criteria are specific, and it can be tested with a fixed list of entries. Smallest first slice: the composable over a list of entries, with a preview. Hooking it to the Collections section comes after.

## SOLR-17911: Implement File Content Viewer Component

(a) Ask: show a file's text with line numbers, line breaks and escapes respected, selectable text, and horizontal and vertical scrolling. Input is a plain string. No editing or syntax highlighting. Open, Major, labels `new-ui`, `newdev`, `ui`, parent SOLR-17886.

(b) Exists today: no viewer or code-display component under `ui/` (grep for viewer, file content, and line-number names finds nothing). Scrolling and text-field building blocks exist in `views/components/`.

(c) Missing: the viewer composable. Selectable text with line numbers is the part to check against Compose's text-selection support in this Compose version. Not verified: the Compose version and whether it offers a selection container that suits line numbers.

(d) Verdict: READY TO VOLUNTEER. The input is a string, so no API is needed. The risk is text selection with line numbers, which should be checked early. Smallest first slice: a stateless viewer over a fixed string with a preview.

## Ranking as volunteer candidates

1. SOLR-17660. Pure UI, concrete criteria, existing input, preview harness, no API.
2. SOLR-17909. Stack model already in use, self-contained composable.
3. SOLR-17911. Self-contained, but text selection with line numbers needs an early check.
4. SOLR-17849. Newdev-labelled and useful, but it needs the section route, two API clients, and a check with Jane Sandberg.
5. SOLR-17887. Needs a tree model and a file-listing source that do not exist yet.
6. SOLR-17850. Needs Christos's answer on the field set.

## Closing: best first contribution

SOLR-17660. It is the only one of the eight that needs no API, no route, and no design answer: a stateless composable on top of an existing text field, testable through the preview harness. Its acceptance criteria are explicit, and the ticket itself says not to aim for perfection.

## Not verified

- Figma designs for all eight tickets (no browser used).
- Compose and Decompose library versions, and the Compose text-selection API.
- Whether a collections list endpoint or a configset file-listing endpoint exists for the UI to call.
- The exact component name the server accepts for request handlers under `/v2/c/{collection}/config/{component}`.
- Whether the Collections route in `components/main` is wired for SOLR-17909's optional use.

DONE: 2026-10-24. Deliverable: `reports/new-ui-index-query-components-round-1.md`; status line in `assignments/new-ui-index-query-components-round-1.md`.
