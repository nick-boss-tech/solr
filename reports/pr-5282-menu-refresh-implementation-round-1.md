# PR 5282 menu refresh follow-up implementation, round 1

Status: Implemented, Proof Pending.

- Authority: Nick requested patching the existing PR branch for the independent review findings and handing it to the Linux gate.
- Claim: `claims/pr-5282-menu-refresh-windows-round-1.md`.
- Original PR head O: `6ee3c9b6370e6e1e3b4da4afcf968a0806cc747f`.
- New head F: `200ff5e5c6c214212f72f37485487f3092e05541`.
- Fork branch: `nick-boss-tech/solr:solr-16398-admin-ui-showcollection`.
- Public PR: https://github.com/apache/solr/pull/5282 . The authorized ordinary fork push moves its existing head; original ancestry is preserved. No force push or contributor/upstream branch write.
- Diff: https://github.com/nick-boss-tech/solr/compare/6ee3c9b6370e6e1e3b4da4afcf968a0806cc747f...200ff5e5c6c214212f72f37485487f3092e05541 .
- Gate: `gates/pr-5282-menu-refresh-round-1.md`; execution receipt: `receipts/PR-5282-menu-refresh-round-1.md`.

## What changed and why

`MainController.resetMenu` captures the route at invocation and assigns a generation to each refresh. Every core/system/collection/alias callback checks ownership before publishing or surfacing an error. Route-change start also invalidates pending callbacks, covering routes whose controller does not call resetMenu. An old response can no longer restore an old selection or mode after a newer navigation.

Successful collection/alias replies build lists and choose the matching object locally, then publish them together. The old model/options remain available during pending requests and on a current list/alias failure; the error still reaches the existing handler. A successful no-match/root-page refresh clears selection. The empty-input showCollection guard remains in the handler, protecting all its callers.

Alias overview watches the refreshed collection model and only displays an alias matching its route. It no longer retains a one-time empty, unrelated or stale snapshot. Removal clears its displayed data; refreshed targets/properties replace old details.

The changelog states those behaviors accurately and removes the `nick:` metadata per Nick's workspace rule. No endpoint, permission, transport or public API was added. No reference-guide change is needed for this internal UI correction.

## Exact outbound files

1. `solr/webapp/web/js/angular/app.js`
2. `solr/webapp/web/js/angular/controllers/alias-overview.js`
3. `solr/webapp/src/test/org/apache/solr/webapp/AdminUiMenuRefreshTest.java`
4. `changelog/unreleased/SOLR-16398-admin-ui-showcollection.yml`

One ordinary commit authored by Nick Shanin, preserving O as its parent. Local task worktree `wt/PR-5282-menu-refresh-r1`, local branch `pr-5282-menu-refresh-r1`. The custom push helper is restricted to this worktree, local branch, four-file list, original remote head and the existing fork PR branch; it loads the current PAT only in memory.

## Tests and static verification

The new suite has nine methods. It reuses `AdminUiTestBase`, its browser/mini-cluster lifecycle, wait methods, Chosen helper, console checks and failure artifacts. Queued API callbacks are local to this suite because the defect requires controlled cross-request completion order. The actual registered controllers, bundled Angular/Chosen, production selector attributes and alias template are used; no new configset or common helper was needed. The test records navigation with a local location stub and releases callbacks with the real timeout/digest.

Windows completed Node v22.17.0 syntax checks for both changed production JavaScript files and all five embedded Selenium script blocks, plus scoped `git diff --check` and four-file source review. No Gradle, Java compilation, formatting task, browser run, application execution or test queue operation ran on Windows. Nine written tests are not nine passing tests.

The gate requires positive F proof (all nine methods plus the two existing collection-screen suites), tidy/module/root checks and negative overlays against O and the original pre-guard base B `7855ab485d50b44af2fe9b52e77bf172d8ea663e`. Exact criteria and fresh artifact requirements are in the job. Old receipts and CI at O do not cover F. Formatting/source repair must be pushed and get a superseding exact-head gate.

## Review reconciliation and public wording

The earlier narrow review conclusion remains: O already delayed the clear and did not need a duplicated template guard. Nick then authorized repairing the neighboring pre-existing defects and adding coverage, so F includes those repairs. They are not presented as regressions introduced by O.

The explanation that deleting the model necessarily invokes ng-change on every page load remains unsupported. The tests explicitly distinguish retention while callbacks are pending, reversed response ownership, alias synchronization, normal Chosen navigation and direct empty handler input. The gate must report those exact behaviors rather than claiming a universal natural page-load crash.

No live PR body edit, GitHub comment, JIRA write, approval or merge was made. The PR body still describes O and is not verification of F. The corrected local description draft below is for the execution host to finish with the actual receipt; publication of a live body edit requires Nick's explicit direction under root AGENTS.md.

## Corrected PR description draft (proof pending)

> 🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*
>
> Jira: https://issues.apache.org/jira/browse/SOLR-16398
>
> The legacy Admin UI can clear its collection selection while refreshing navigation. Separately, a late response from an earlier navigation can replace the current menu, and alias overview can retain the selection it read before its data finished loading.
>
> This change retains the previous model/options until a successful refresh can replace them, ignores responses from obsolete refreshes, and updates alias overview from the refreshed alias matching its route. A successful refresh clears a removed or unmatched selection. A current list/alias request failure preserves the old model and still reports the error. The collection handler safely ignores empty input.
>
> Nine regression tests use the existing Selenium harness, the bundled Angular/Chosen implementation, controlled callback ordering, the production selector attributes and the actual alias template. Linux execution is pending at `200ff5e5c6c214212f72f37485487f3092e05541`; Node syntax and scoped whitespace checks passed on Windows. The receipt must supply positive results and the specified fail-before comparisons before this is described as validated.
>
> Scope: legacy navigation/alias display only; no API migration change. The tests do not establish that programmatic deletion automatically fires ng-change on every page load. Direct empty-handler proof and the controlled refresh regressions are reported separately.
>
> *Nick Shanin directed the work and takes responsibility for this contribution.*
