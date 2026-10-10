# Admin UI round 1, part U1: SOLR-9759 (Stream screen POST and visible failures)

Audit only. Nothing has run: no build, no Gradle, no test, no Selenium or Chrome. No `gh` write, commit, push, post or file edit.

## Refs

Branch `solr-9759-submit`, head `31e702622dae43a36ed8d7ec8477c1547e5ebc42` (it matches the claim; read from the existing local ref `origin/solr-9759-submit`, not re-fetched). Merge-base `cabedd1d968`. Five files, +86 and -1. Paths are repo-relative at the SHA named, read with `git show`.

Main reference: the round names `upstream/main` `8e62c2686882`. This worktree's local `upstream/main` is `3f5d4c5bf8ac`, one range ahead. This part cites `8e62c2686882`. The cited main files (`partials/stream.html`, `controllers/stream.js`, `services.js`, `app.js`, `controllers/core-overview.js`, `AdminUiStreamScreenTest.java`, `StreamHandler.java`, `SolrRequestParsers.java`, `PingRequestHandler.java`, `ImplicitPlugins.json`, and `server/etc/jetty.xml`) are identical between the two refs (`git diff --stat` is empty).

## Verdict

- **Premise on current main: fails on a reading.** The Stream screen no longer sends a GET. Main commit `c6f910d3ccf` ("SOLR-9759: Admin UI should post streaming expressions (#5048)", Eric Pugh, dated 2026-10-08, in main's history) carries the same fix to the same two files, plus its own changelog and test. **The branch duplicates main.**
- **Residual on main, from reading:**
  - (i) The error callback never runs for HTTP-status failures, because the Query opt-out turns them into the success path (`app.js` lines 425 and 426). So the "HTTP `<status>`" text is never printed, on main or on the branch.
  - (ii) A non-JSON failure body reaches main's success path, which catches the parse error and shows the raw body (`stream.js` lines 51 to 63).
  - (iii) A failure with no body (status 0, or the ten-second default timeout at `app.js` lines 392 to 394) reaches the success path with an empty body, so main's box stays empty with no message.
- **Head verification:** nothing has run. Static reading only. The branch's success callback has no guard (head `stream.js` line 53), so a non-JSON failure body throws and leaves the box blank. The branch's error callback (head `stream.js` lines 70 to 74) cannot run, for the same reason as (i). **The branch's visible-failure claim does not hold at the head.**

## Item 1: the premise on main

- **Base `cabedd1d968`:** `stream.js` lines 44 to 46 call `Query.query(params, success)` with no error argument. `services.js` lines 358 to 365 define the GET query action with `doNotIntercept` at line 364. The TESTING note's `jetty.xml` claim holds on main: `server/etc/jetty.xml` line 63 sets `requestHeaderSize` to a default of 8192. A GET URL carrying a 10,000-space expression exceeds that.
- **Base opt-out:** `app.js` lines 425 and 426 return the rejection before the status-0 and status branches, so a failed opted-out request resolves into the success callback. Base `stream.js` line 48 does `JSON.parse(data.toJSON().data)` with no guard, so an HTML error body throws there and the box stays blank. This matches the Jira's `JSON.parse` console error (`research/jira-context/SOLR-9759.json`, Description). The invisibility holds at base, but the cause is the unguarded success parse. An error callback alone cannot fire under the opt-out.
- **Main `8e62c2686882`:** `services.js` lines 372 to 392 define `query` (GET, lines 373 to 377, opt-out at line 376), `queryPost` (POST, lines 380 to 388; `transformRequest` `toQueryString` at lines 358 to 368; the form Content-Type and opt-out at lines 385 and 386), and `resource.url` (display only, lines 390 to 392). `stream.js` lines 44 to 46 keep the GET URL for display. `stream.js` lines 51 to 68 are `showResult`, with a guarded parse (lines 53 to 63). `stream.js` lines 70 to 74 are the call, with success and error. **The Stream screen's request is a POST on main.**
- **Interceptor on main:** `app.js` lines 425 and 426 (return the rejection for `doNotIntercept`). The status-0 branch (lines 435 to 441) and the final `return $q.reject` (line 475) are skipped for opted-out requests. `started()` (lines 377 to 396) applies to all requests.
- **AngularJS 1.8.0** (`libs/angular.min.js` line 124 chains `responseError` as a promise handler): a returned value resolves the request.

## Item 2: the TESTING note's guesses

- **(a) Holds.** `StreamHandler.java` line 173 reads `req.getParams()`, and line 186 parses `params.get(StreamParams.EXPR)`. `StreamParams.java` line 23 defines `EXPR = "expr"`. The form body is merged by `SolrRequestParsers.java` lines 762 to 773 (POST form data goes to `FormDataRequestParser`, lines 609 to 657). `isFormData` (lines 681 to 694) requires `application/x-www-form-urlencoded` after stripping parameters. The branch and main both send that type (head `services.js` line 382; main `services.js` line 385). `/stream` is implicit at `ImplicitPlugins.json` lines 103 to 105. The documented curl form exists: `solr-ref-guide` `modules/query-guide/pages/streaming-expressions.adoc` line 63 (`curl --data-urlencode 'expr=search(...'`).
  - **Size:** `FormDataRequestParser` rejects bodies over `formdataUploadLimitInKB` (lines 621 to 630). `SolrConfig.java` line 358 defaults it to `Integer.MAX_VALUE` when unset, and the `_default` configset leaves it unset (its `requestParsers` element sits inside an XML comment, `solrconfig.xml` lines 505 to 521). So the POST has no size cap by default.
- **(b) Holds on the call shape** (AngularJS 1.8.0). The minified `angular-resource.min.js` argument switch maps (params, data, success, error) for four arguments (line 31). The `hasBody` test (`/^(POST|PUT|PATCH)$/i`) is true for POST. The head call is `Query.post({core, handler}, body, success, error)` at head `stream.js` lines 51 and 70. `transformRequest` receives the body object (head `services.js` lines 370 to 378). The Content-Type is explicit in the action headers (head `services.js` line 382). Static only.
- **(c) The shape holds, but the callback never runs.** AngularJS applies `transformResponse` to every response, error included (`angular.min.js`, function `f`: `b.data = Bd(a.data, a.headers, a.status, g.transformResponse)`, then reject for non-2xx). Head `services.js` lines 379 to 381 return `{data: data}`, so `error.data.data` is the body (head `stream.js` line 73). But `app.js` lines 425 and 426 turn the rejection into a success first, so head `stream.js` lines 70 to 74 never run for HTTP-status failures, status 0, or timeout.

## Item 3: the changelog title

The branch changelog (`changelog/unreleased/SOLR-9759-admin-ui-stream-post.yml` line 2): "The Admin UI Stream screen sends the streaming expression in a POST body (long expressions no longer fail with HTTP 413) and shows request failures instead of staying blank."

It claims three things:
1. **The POST half:** supported by the reading in item 2(a).
2. **The parenthetical "long expressions no longer fail with HTTP 413":** an outcome claim, not bounded in the title. The form limit is in item 2(a). The 413 code on Jetty 12 is unverified (see Not checked).
3. **"Shows request failures instead of staying blank":** this overclaims. A non-JSON failure body throws at head `stream.js` line 53 and leaves the box blank, and the error callback never runs.

Main already ships a changelog for this ticket: `changelog/unreleased/solr-9759-admin-ui-stream-post.yml` at `8e62c2686882` (author Eric Pugh; the title says a failed request now shows an error instead of leaving the screen blank). Two fragments would collide in the release notes.

## Item 4: the premise-run spec (main-side work owed; not run)

- The branch test `testLongStreamingExpressionViaUi` is a near copy of main's `testLargeExpressionSucceedsViaUi` (main `AdminUiStreamScreenTest.java` lines 56 to 80). Main's file also has `testFailedRequestShowsErrorInsteadOfHanging` (lines 82 to 94), which is the only real error-path check. The run that matters for a merge decision is main's file.
- **Base run:** `cabedd1d968` with only the branch's test file overlaid. Run `testLongStreamingExpressionViaUi`. Record the failing request's actual HTTP status (the test does not assert it), the `#result` text, and the console error. Expected per the note and receipt: the request fails, and `#result` never shows `stream-doc-1` (a wait timeout).
- **Head run:** `31e702622dae`. Expected: all three documents.
- **Main run:** `8e62c2686882`, all three tests in its `AdminUiStreamScreenTest`.
- **Outcome A** (base fails with the request-size failure and a blank box; the head returns all three documents): the mechanism is real on the old tree, and the POST path works. It does not make the branch mergeable. Main already has the change, so the branch is a duplicate and needs a retire decision, not a PR. It exercises no error path, so it says nothing about the error callback.
- **Outcome B** (base does not fail as predicted, or the head fails): if base passes, the 10,000-space GET fits this Jetty's header limit, or the JavaScript input is not reaching the expression as intended. The premise is then wrong for this Jetty, and the test is not a fail-before proof. If the head fails, the POST path has a fault the reading missed (form parsing or the resource call), and the note is wrong at that point. Either way, no PR from this branch.
- **Batch:** the shared `AdminUiTestBase` harness. If the owner wants the run at all, place it last in the batch (after 9831 and 9818, per the assignment's order), and run main's test file, not the branch's.

## Item 5: the opt-out interaction (from this side; SOLR-9818's change is not audited)

- Confirmed from main: the Query actions carry `doNotIntercept` (`services.js` lines 376 and 386). `app.js` lines 425 and 426 return before the status-0 branch (lines 435 to 441). Opted-out requests never reach the handler SOLR-9818 changes, and non-opted-out requests never take the early return. So no request gets both treatments.
- No opted-out resource loses reporting from 9818's change, because 9818 does not touch the early return. But the branch's own error callback adds no reporting, since it cannot run (item 2(c)).
- All opt-outs on main (`git grep doNotIntercept`, `webapp/web`): `app.js` line 425; `services.js` line 340 (Ping enable), line 341 (Ping disable), line 342 (Ping status), line 376 (`Query.query`), and line 386 (`Query.queryPost`). Callers: `Query.query` at `controllers/query.js` line 280 and `controllers/sqlquery.js` line 55; Ping at `controllers/core-overview.js` line 58 and lines 81 to 92; `Query.queryPost` at `controllers/stream.js` line 70.
- **Silent bypass 1: `Query.queryPost`** (`services.js` line 386). Streaming expressions can write. Update, delete and commit are registered in `solr/solrj-streaming/.../io/Lang.java` lines 342 to 345, `DefaultStreamFactory` calls `Lang.register` (`DefaultStreamFactory.java` line 30), and `StreamHandler` uses that factory (`StreamHandler.java` line 87). So a Stream-screen POST can change data. A connection drop or the ten-second timeout reaches the success path with no body, never the status-0 branch, so the user gets no "not repeated" message and cannot tell whether the write happened. The branch's POST has the same bypass (head `services.js` line 382; head `stream.js` lines 51 to 75).
- **Silent bypass 2: Ping enable and disable** (`services.js` lines 340 and 341). `PingRequestHandler.java` lines 102 to 104 document that enable creates and disable deletes the healthcheck file, so they change server state. They are sent as GET (no method set), so a method-based rule (SOLR-9818's GET and HEAD replay) classes them as reads. They are idempotent, so a replay is harmless, but the classification is by method, not by effect. On a refused request (`core-overview.js` lines 59 to 63 say enable and disable answer 503 when no healthcheck file is configured), the success callbacks run: `core-overview.js` lines 83 and 89 set `healthcheckStatus = true`, so the toggle shows lit after a refused enable until the next refresh. This is an existing main defect caused by the opt-out.
- `Query.query` callers (`query.js` line 280 and `sqlquery.js` line 55) are reads with the same success-path behavior. They are not state-changing, so they are not in SOLR-9818's message scope.
- **Recommendation for the SOLR-9818 side** (not audited here): the "not repeated" treatment should not depend on the opt-out's early return for state-changing calls, and the owner should decide whether a status-0 message reaches opted-out calls.

## Receipt disagreements (exact wording)

1. **`receipts/SOLR-9759.md` line 6:** "the tip has not moved since registration." The tip check is right (`31e702622dae`). The receipt does not record that main already carries the change: `c6f910d3ccf` "SOLR-9759: Admin UI should post streaming expressions (#5048)", dated 2026-10-08. Its "premise unverified" line is settled on a reading: superseded on main.
2. **`receipts/SOLR-9759.md` line 8:** "Verification owed is main-side: compile, then the test on base (...) and at the head (expected pass: all three documents return)." "Base" is ambiguous now that main has the fix. The owed run is item 4.
3. **`receipts/SOLR-9759.md` line 7:** the tip commit "adds only that TESTING.md note". Verified true (`SOLR-9759-TESTING.md` only). Noted for completeness.
4. **Branch `SOLR-9759-TESTING.md`, "What the code shows on main":** "`controllers/stream.js` calls `Query.query` (a GET with `expr` in the query string)... the controller passes no error callback, so a 413 is invisible." True at `cabedd1d968` (`stream.js` lines 44 to 46). False on `8e62c2686882` (`stream.js` line 70 calls `Query.queryPost`; lines 72 and 73 have the error callback).
5. **Assignment, SOLR-9759 starting state:** "the controller passes no error callback". True at base only. The Interactions entry's "that is why 9759 carries its own error callback" is also wrong in effect: the callback cannot fire for HTTP failures (`app.js` lines 425 and 426).
6. **Branch changelog title:** "shows request failures instead of staying blank". Contradicted by head `stream.js` lines 51 to 53 and lines 70 to 74 (item 3).
7. **Jira context file** (`research/jira-context/SOLR-9759.json`): Status "Open", Updated 2019-06-08, Versions 6.2.1. It is a snapshot, so it cannot show whether the ticket was closed after `c6f910d3ccf`. Not checked live (no JIRA call, per the round's rule).

## Owner decisions

1. **Retire `solr-9759-submit` as superseded by `c6f910d3ccf`:** no PR, no draft, and mark the TESTING note superseded. Recommendation: retire.
2. **Whether to chase main's residual:** the error callback never fires, and the "HTTP `<status>`" text never shows. A status-0 or timeout gives an empty box with no message (`app.js` lines 392 to 394). Options: report the failure in the success path, where it is already reached, with the status; or remove `doNotIntercept` from `queryPost`, so the global handler reports it (this changes the global banner, so it needs a look). A new ticket if wanted; not this round.
3. **Whether the Stream screen's POST should stay opted out,** given that it can carry update, delete and commit. Recommendation: at minimum, a status-0 failure for this request should show an unknown-outcome message.
4. **Ping enable and disable:** whether the toggle should read the server outcome (`core-overview.js` lines 83 and 89), and whether the opt-out (`services.js` lines 340 and 341) stays. A separate ticket if wanted.
5. **SOLR-9818 scope** (for the roll-up; not audited here): classing state-changing calls by method (Ping enable and disable are GET), and the opt-out bypass in item 5. Recommendation: key the "not repeated" treatment on the call's effect, and let status-0 messages reach opted-out calls.
6. **The ten-second default timeout** (`app.js` lines 392 to 394) turns a slow stream into an empty box on main. The owner decides whether the Stream screen should raise it or report it.
7. **Whether to spend a Chrome run on this ticket at all,** since main carries the change. If yes, run main's `AdminUiStreamScreenTest`, not the branch's test.

## Not checked

- Nothing ran. The head result is a static reading only.
- The HTTP status Jetty 12.1.12 returns for an oversized request header (main pins `eclipse-jetty` 12.1.12 at `gradle/libs.versions.toml` line 84). The 413 in the Jira (6.2.1 era) and in the note is unverified for this Jetty, and the jar is not in the repo. A run must record the actual code.
- Whether Jetty or a servlet filter reads the POST body before `SolrRequestParsers` (its check is at lines 645 and 646). Not traced.
- AngularJS behavior was read from the minified `angular.min.js` and `angular-resource.min.js` (1.8.0). Not executed.
- Drift: only the cited files were compared between `8e62c2686882` and the local `upstream/main` `3f5d4c5bf8ac`. `SolrConfig.java`, `StreamParams.java`, `Lang.java`, `DefaultStreamFactory.java`, `solrconfig.xml`, the ref guide, `libs.versions.toml` and the Angular libraries were read at `8e62c2686882` only. Upstream commit `cdc1707abb4` (SOLR-16640) changed `sqlquery.js` after `8e62c2686882`; it was not read.
- `query.js` and `sqlquery.js` were read only at their `Query.query` call sites. The Ping partial (the core-overview HTML) was not read; the toggle behavior comes from the controller only.
- SOLR-9818's own change was not audited, per the round's rule.
