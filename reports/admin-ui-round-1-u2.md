# Admin UI round 1, part U2: SOLR-9818 (branch `solr-9818-submit`, head `63f2d7ce9267`)

Nothing has run. There was no build, no Gradle, no test, no Selenium or Chrome run, no `gh` write call, no commit, no push, no post, and no file edit in the repo. There was no checkout. Temporary copies (extracted `app.js` and `services.js` versions, and a patch file) are in the scratchpad only. The git reads were `git show`, `grep`, a diff stat, and a dry-run apply against a temporary index in the scratchpad, plus a read-only `git ls-remote`. Main is `8e62c2686882`, as directed. The local `upstream/main` ref is `3f5d4c5bf8a`. `app.js`, `services.js` and `index.html` do not differ between the two, so the readings hold for both.

## Verdict

- **Premise on current main: holds in part, on a reading. It fails for the two commands the claim names.**
  - Holds: a status-0 request that reaches the interceptor is replayed at once with `$http(rejection.config)`, with no delay and no method check (`app.js` lines 435 to 441 on main). The note's quote is accurate.
  - Fails for collection reload and ADDREPLICA: on main both go through the generated v2 client (`collections.js` line 238, `reloadCollection`; `collections.js` line 303, `ReplicasV2.createReplica`). That client is superagent-based and does not pass through `$httpProvider`, so `httpInterceptor` never sees them. The 23-replica report (ticket comment 15714706) comes from the older `$http`-based UI.
  - Holds for other state-changing requests that do reach the interceptor. Some of them are GET-method state changes that the branch's GET and HEAD rule would still replay (item 3, scope point iii).
- **Head verification (`63f2d7ce9267`):** the live tip was confirmed by `git ls-remote` today. Four files, as the receipt says. The tip commit adds only `SOLR-9818-TESTING.md`. The `app.js` hunk applies cleanly to main `8e62c2686882` (a dry-run apply, exit 0). The code matches the note. Nothing is verified by a run.
- The test pins the helper only (item 4).

## Items 1 and 2

**Item 1: the premise on main** (`8e62c2686882`; base `cabedd1d968` has the same block at lines 432 to 438)
- `app.js` lines 425 to 427, the opt-out: `if (rejection.config.headers.doNotIntercept) return rejection`.
- `app.js` line 435, `if (rejection.status === 0)`; line 436, broadcast `connectionStatusActive`; lines 437 and 438, `retryCount`; line 439, `var $http = $injector.get('$http')`; line 440, `var result = $http(rejection.config)`; line 441, `return result`. No delay, no method check. Confirmed.
- **The named commands:** `collections.js` line 238, `CollectionsV2.reloadCollection`; `collections.js` line 303, `ReplicasV2.createReplica`. `CollectionsV2` and `ReplicasV2` are `new solrApi.CollectionsApi()` and `new solrApi.ReplicasApi()` (`services.js` lines 91 to 95 and 127 to 131). The `solrApi` client is the OpenAPI JavaScript generator (`solr/api/build.gradle` lines 106 to 111). The UI states that these calls are superagent-based and never pass through `$httpProvider` (`app.js` lines 484 to 486; `services.js` lines 45 to 48; `cloud.js` lines 745 and 746). The superagent plugin is at `app.js` lines 491 to 494. The generated source is not in the tree, so this rests on the UI's own statements.
- On connection loss, those calls do not replay. The callback gets no response, and `ApiErrorHandler.handle` returns at `services.js` lines 51 to 53 with no message. The reload path sets `reloadFailure` (`collections.js` lines 241 and 242). The addReplica path shows nothing (`collections.js` line 305).
- **State-changing requests that do reach the interceptor on main:** `Replication.command` (`services.js` lines 243 to 249; `replication.js` line 58; the buttons at `partials/replication.html` lines 225 to 234 for fetchindex, abortfetch, enablepoll, disablepoll, enablereplication and disablereplication). `Logging.setLevel` (`services.js` lines 186 to 197; `logging.js` line 175). `Security.post` (`services.js` line 412; `security.js` line 497). `ParamSet.submit` (`services.js` line 264; `paramsets.js` lines 140 and 167). `FileUpload.upload` (`services.js` line 280, `$http.post`). `Replication.command` and `Logging.setLevel` send GET by default. This is a reading only: angular-resource 1.8.0 copies only the keys present on the action, and `$http` defaults to method "get" (`angular.min.js`).

**Item 2: the branch's own file** (head `63f2d7ce9267`)
- **(a) `$timeout` and `$q` are in scope.** `app.js` line 380: `.factory('httpInterceptor', function($q, $rootScope, $location, $timeout, $injector)`. `$timeout` is used at line 445, and `$q.reject` at line 452. Main has the same injection at line 374. Holds on a reading. The factory uses implicit injection, as main already does.
- **(b) The exceptions mechanism.** `index.html` lines 152 to 154 render `ng-repeat="(url, exception) in exceptions"` with `{{exception.msg}}`, so the `{msg: ...}` shape at head lines 449 to 451 matches. `ApiErrorHandler` uses the same shape (`services.js` lines 84 and 85). The key is `rejection.config.url`, the same key the generic branch uses (head line 483; main line 472). **Clearing:** the note says `started()` clears the entry on the next request to the same URL. That is true (main `app.js` lines 381 to 383; head lines 387 to 389) but incomplete. The entry is also cleared on every route change (main `app.js` lines 679 and 680; head lines 690 and 691), and `login.js` line 23 resets the map.
- **(c) Global placement.** `app.js` lines 20 to 23 declare `function isRetryableRequest(config)` at top level. `index.html` line 75 loads `app.js` as a classic script, with no `type="module"`. No wrapper function encloses `app.js`. So the function is a window global, and `executeScript("return isRetryableRequest(...)")` can see it. No other file defines the name on base or main. A reading only.
- **Replay recursion:** a replayed request that fails again calls `failed()` again and schedules another one-second replay (head line 445). This is unbounded while the server is away, as the note says.
- The `$timeout` promise adopts the `$http` response, so the caller still receives the response. Holds on a reading of `$q` adoption; not run.

## Item 3: the scope points, with recommendations

**(i) Reads are retried with no cap** (head lines 442 to 446: once a second, indefinitely). Ticket comment 15885278 (Ere Maijala, 2017-02-27), exact: "It would be ok to poll the server automatically to see if it's available again, but only like at max once a second or so and with a safe request."
- Recommendation: add a fixed retry count the owner chooses, then stop, keep the connection banner up, and let the user retry. This keeps the reporter's once-a-second, safe-request shape and ends the open-ended loop. If the owner keeps it unbounded, the PR text must say that reads retry until the connection returns.

**(ii) The maintainer request to drop the buffer.** Comment 17111893 (Jan Høydahl, 2020-05-20), exact: "Can we please fully get rid of the retry buffer? Instead just display the errors and not retry the original request? Will this patch [^SOLR-9818.patch] work?" The JSON gives no role for the author. The branch does this only for non-GET and non-HEAD requests. Reads are still replayed.
- Recommendation: do not adopt silently. Keep bounded read replay (per (i)) and answer Jan's question in the PR text with the reason. The alternative, no replay for any method, is a single rule that matches the request literally, and the owner chooses. The `SOLR-9818.patch` attachment is not in the JSON and was not checked.

**(iii) Not in the assignment's list: the GET and HEAD rule misses state-changing GETs sent through `$http`.** `Replication.command` and `Logging.setLevel` are GET by default, so the branch replays them after one second. Mark Miller's comment 15727942 names the real concern, exact: "A lot of these commands are not idempotent and the browser doesn't know what happened depending on the fail."
- Recommendation: mark those actions non-retryable per action, in this branch or a follow-up. The owner's call.

## Interactions with SOLR-9759

From main's `services.js` and `app.js`. SOLR-9759's own change was not re-audited.
- The opt-out is checked before the status-0 branch (`app.js` lines 425 to 427). Opted-out requests never get the not-repeated message or the replay. `failed()` returns the rejection as a plain value, which AngularJS treats as recovery, so the success callback runs. `stream.js` lines 47 to 50 on main say this in their own comment. This is pre-existing and unchanged by SOLR-9818.
- **Opted-out resources on main:** Ping enable, disable and status (`services.js` lines 336 to 344; enable and disable are GET and change state); `Query.query` (lines 356 to 377, GET); `Query.queryPost` (lines 378 to 388, POST; stream expressions can carry write operations). So the not-repeated message is silently bypassed for these. A reading: `core-overview.js` lines 81 to 89 set `healthcheckStatus = true` in the enable success callback, which would also run on a status-0 failure. Not run.
- **SOLR-9759 is already on main:** commit `c6f910d3ccf`, "SOLR-9759: Admin UI should post streaming expressions (#5048)" (Eric Pugh, 2026-10-08), between base and main. It adds `queryPost` and the `stream.js` change. The opt-out interaction is therefore with the merged SOLR-9759 code. The lead must reconcile this with the SOLR-9759 receipt.
- The not-repeated message ignores the schema-designer exemption that the generic branch applies. The head skips it for `/api/schema-designer/` at lines 480 to 484; the new branch at lines 449 to 451 does not. An owner point.

## Item 4: the test's limit

- `AdminUiRetryPolicyTest` (`solr/webapp/src/test/org/apache/solr/webapp/AdminUiRetryPolicyTest.java`) calls `isRetryableRequest` through `executeScript` for GET, HEAD, POST, PUT, DELETE and PATCH. It sends no request, so no status 0 ever occurs. It does not check the one-second delay, the number of replays, that the rejection passes on, the exceptions entry or its text, the banner, the route-change clear, or the opt-out. It also does not check a lowercase method or a missing method, which defaults to GET.
- **What an end-to-end check needs:**
  1. A real `$http` request from the page, through the app's injector or a real controller action.
  2. A status 0 for that request in the browser. The options are (a) an in-page XHR stub that fails chosen URLs with status 0 and counts sends (deterministic, and it runs the real `$http` and interceptor); (b) ChromeDriver's CDP `Network.emulateNetworkConditions` offline, then online (closer to a real drop, but timing-sensitive); or (c) a proxy in front of a node that can forward a request and then sever the response, which is the ticket's "may or may not have been processed" case. The feasibility of (a) and (b) under ChromeDriver was not checked.
  3. A server-side arrival counter for the path (for example a servlet filter in the test cluster), because the browser cannot show whether the server received a POST.
  4. A side-effect-free state-changing target for the POST case (for example an empty update POST to a test collection).
  5. Assertions: one POST arrival after a forward-then-sever; no second arrival; reads spaced about one second apart (lower bounds only, since CI is loaded); the text in `#http-exception`; and the entry cleared after `$routeChangeStart`.
  6. Run on main with no fix (expect a replay), and at the head (expect one POST arrival).

## Item 5: the premise-run spec (main-side work owed; not run)

**Preconditions:** webapp Gradle tests with `-Ptests.selenium=true`; the JS client enabled (no `-PdisableJsClient=true`; `solr/webapp/build.gradle` lines 95 to 104); Chrome present (`AdminUiTestBase` fails without it, around lines 186 to 189); JDK 21, per the workspace notes.

- **Run A** (the receipt's "base"): base `app.js` (`cabedd1d968`) with only the branch's test file overlaid. The receipt does not say this. Expected: FAIL at the first `executeScript`, with a JavaScript error that `isRetryableRequest` is not defined, before any assertion. Any other failure cause (Chrome, ChromeDriver, cluster start, the bundle) is not evidence about the change.
- **Run B** (the receipt's "head"): `63f2d7ce9267`, the full branch. Expected: PASS on all four assertions. This shows only the helper's truth table.
- **Run C** (not in the receipt; needed to settle the interceptor premise in a browser, using the item 4 harness): on main `8e62c2686882` with no fix, a forced status-0 POST arrives more than once (the immediate replay). At the head, there is one arrival, the not-repeated text in `#http-exception`, no replay, and GET arrivals about one second apart.
- **Run D** (optional): Ping enable on main (opted out). Expected: no replay and no banner.
- **Batching:** the SOLR-9818 runs can share one harness run with SOLR-9831 (smallest first, then 9818, then 9759, per the assignment). SOLR-9759 is already on main, so its run is a main-side run, and the lead should re-scope it.

## Receipt disagreements (exact wording)

1. **Receipt line 8:** "Verification owed is main-side: compile, then the test on base (expected fail: the helper is absent) and at the head (expected pass)". The base has no test file, so the base run needs the branch's test file overlaid. Clarify.
2. **Receipt line 7:** "Diff against merge-base `cabedd1d968059215188f4e7563fb303241899ed` (4 files): `solr/webapp/web/js/angular/app.js` ..., the Selenium test `AdminUiRetryPolicyTest`, and `changelog/unreleased/SOLR-9818-admin-ui-no-blind-retry.yml`." It names three files. The fourth, `SOLR-9818-TESTING.md`, is in the tip commit. This is a gap in the list, not a disagreement about the diff.
3. **Assignment, the SOLR-9818 entry:** "a state-changing command such as a collection reload or ADDREPLICA can be issued many times". For those two commands this fails on main (item 1). It holds for other `$http`-routed commands.
4. **Assignment, SOLR-9759 entry:** "The branch (5 files, tip `31e702622dae`) gives the Query resource in `services.js` a form-encoded post action". Main already has that (`queryPost`, `c6f910d3ccf`). This is a cross-ticket point for the lead to reconcile.
5. **The TESTING note:** "The `exceptions` entry is cleared by `started()` on the next request to the same URL." Incomplete. It is also cleared on every route change (`app.js` lines 679 and 680 on main; lines 690 and 691 at the head).
6. **Changelog title** (`changelog/unreleased/SOLR-9818-admin-ui-no-blind-retry.yml` line 2): "...and replays reads at most once a second." Reads have no count cap, so "at most once a second" reads as a count bound. The title also lists "POST, PUT, DELETE and PATCH", while the code rule is every method other than GET and HEAD. Suggested: "replays reads once a second until the connection returns", and "any method other than GET or HEAD". Titles must be accurate.
7. **Receipt line 6, the round 36 Group A routing on 2026-10-07:** not checked (there is no record in the repo).

**Agreement:** the head tip `63f2d7ce9267` (`ls-remote`), four files, the tip commit adds only the TESTING note, the base has no helper, and the Chrome requirement.

## Owner decisions

1. **Reads:** add a retry count, or keep reads unbounded at one second. Recommendation: a count.
2. **Jan Høydahl's request:** keep bounded read replay and answer in the PR, or drop all replay. Recommendation: keep and answer.
3. **State-changing GET actions** (the `Replication.command` actions and `Logging.setLevel`): mark non-retryable per action, now or in a follow-up. Recommendation: per action.
4. **The schema-designer exemption** for the new message (head lines 449 to 451): decide. Recommendation: match the exemption at head lines 480 to 484.
5. **The PR text must not claim** that the branch fixes collection reload or ADDREPLICA: they never reached the interceptor on main. The v2 connection-loss gap (no message, `services.js` lines 51 to 53) is a separate question.
6. **Changelog title wording** (receipt disagreement 6).
7. **The item 4 end-to-end harness:** build it, or accept the helper-only test with its limit stated in the PR.
8. **Reconcile SOLR-9759's receipt** with its merged state on main (the lead).

## Not checked

- Nothing was run: no build, no Gradle, no tests, no Selenium or Chrome.
- The generated JavaScript client source is not in the tree. The superagent use rests on the UI's own statements and the plugin code.
- The GET default for no-method `$resource` actions is a reading of the vendored `angular-resource` 1.8.0 and `angular.min.js`; not run.
- The CDP offline emulation and the XHR stub under ChromeDriver: feasibility not checked.
- Whether the `Replication` or `Logging` GET calls fire in a live session: only the call sites were read.
- The `SOLR-9818.patch` attachment named in Jan's comment is not in the JSON.
- The round 36 routing record is not in the repo.
- SOLR-9759 was not compared with its merged main version (that is part U1's scope).
- The ticket JSON was read only; there was no JIRA call. `git ls-remote` was read only; there was no fetch.
