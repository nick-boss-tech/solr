# PR 5282 CI failure follow-up, November 3, 2026

Status: Implemented, Proof Pending at `82954b9df687d8239c4344b292b3afff5005f898`.

- Board snapshot reviewed: `bd73c3ef3ae0cb042f8bb67b64a3673ea4704eb6`.
- Public PR: https://github.com/apache/solr/pull/5282 . Live PR head and fork ref were separately verified at `82954b9df687d8239c4344b292b3afff5005f898`; tree `9294ed1c2de8cb0058a66815d8bcbc4ccba2d570`; parent `200ff5e5c6c214212f72f37485487f3092e05541`.
- Previous upstream browser run: [run 41680303677](https://github.com/apache/solr/actions/runs/41680303677), at parent F.
- Superseding Linux gate: `gates/pr-5282-menu-refresh-round-2.md`.

## CI failure and repair

The downloaded CI artifact XML for `AdminUiMenuRefreshTest` shows 12 executions, 4 failures, 0 errors and 0 skips. Each failure is a repeat of `testSelectorNavigationAndEmptyInput`, with `Option 'shelf' not found in select menu_refresh_selector`; the eight other distinct methods passed. The run was at `200ff5e5c6c214212f72f37485487f3092e05541`.

The fixture cloned the production selector after the landing dashboard initialized in standalone mode. Its DOM control was disabled before SolrCloud data loaded. The test then compiled the clone under a separate mocked SolrCloud scope, leaving the cloned `disabled` attribute in place. The test now removes that attribute before compilation, allowing Chosen to open. This changes only `solr/webapp/src/test/org/apache/solr/webapp/AdminUiMenuRefreshTest.java`; no production source changed.

The repair is commit `82954b9df687d8239c4344b292b3afff5005f898`, pushed by ordinary fast-forward to `nick-boss-tech/solr:solr-16398-admin-ui-showcollection`. The local worktree is clean and `git diff --check` passed before commit. No Gradle or test was run on Windows. The new exact-head gate requires the browser suite and both fail-before comparisons at G.

## Reviewer questions

The `JavascriptExecutor` setup is used to inject controlled API callbacks and invoke the actual registered Angular controllers while retaining the real `$timeout`/digest, production selector attributes, Chosen widget and alias template. The callback queues make out-of-order responses deterministic. Existing webapp Selenium tests also use `JavascriptExecutor` for browser setup/assertions. Keep the regression suite unless exact-head Linux evidence reveals another design problem; the current failure is localized to fixture state.

`menuGeneration` advances only when `resetMenu` starts or a route-change event fires. It does not run periodically or in the background. It is a per-controller generation token; wraparound would require an infeasible number of navigations during one controller lifetime.

The prior upstream failure and its diagnosis are recorded for the gate host. No new GitHub comment was posted; the current test repair is not yet backed by exact-head Linux proof. The two drafted responses are in `review-replies.md` in the local scratch folder.
