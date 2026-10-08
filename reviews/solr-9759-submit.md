# solr-9759-submit

- Branch: origin/solr-9759-submit
- Head: 31e702622dae (matches the listed head; checked against the remote ref and the ticket worktree)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d9680, 16 commits behind, 3 commits ahead)
- Scope: 3 commits, 5 files, +86/-1. `solr/webapp/web/js/angular/services.js` (new `Query.post` action, form-encoded body), `solr/webapp/web/js/angular/controllers/stream.js` (`doStream` uses `Query.post`, new error callback), `solr/webapp/src/test/org/apache/solr/webapp/AdminUiStreamScreenTest.java` (new `testLongStreamingExpressionViaUi`), changelog `changelog/unreleased/SOLR-9759-admin-ui-stream-post.yml` (`type: fixed`, author Nick Shanin), and `SOLR-9759-TESTING.md` (author's hypothetical-reproduction note, left in place).
- Verdict: Nearly
- Reviewer: review-agent A3 (claude-haiku-5-5, Claude Code), 2026-10-08
- Patch: none

Nothing here was compiled, formatted, or run. No Gradle, no spotless, no Selenium test, no fail-before proof. Every claim below rests on reading the diff and the code at the listed head. `SOLR-9759-TESTING.md` is labeled "hypothetical reproduction (not run)" and is treated as unverified.

## Findings (ranked)

1. **LOW, verified. Ticket-tagged and change-describing comments.** The repo's `AGENTS.md` says changes should not carry code comments that communicate the change. The branch adds: `services.js:366-367` ("SOLR-9759: the parameters travel in the body ..."), `AdminUiStreamScreenTest.java:56` (javadoc "SOLR-9759: an expression larger than ..."), and `stream.js:71` ("the request opted out of the global error handler, so report the failure here"). Wording-only; not a defect. Not patched.

2. **LOW, verified. The address-bar link still shows the GET URL.** `stream.js:44` builds `url = Query.url(params)`, and `params.expr` still carries the full expression, so `$scope.url` (`stream.js:67`) is the same long GET URL that returns 413. The link is rendered under the form (`solr/webapp/web/partials/stream.html:35-36`, `ng-href="{{url}}"`). The author's note calls this display-only. Confirmed in code. Not patched. See owner call A.

3. **LOW, hypothesis. Error text rendered with the JSON highlighter.** `$scope.lang = "json"` is set before the request (`stream.js:40`) and is not changed in the error callback (`stream.js:70-75`), so the plain-text "Request failed with HTTP ..." message may be highlighted as JSON. Not traced in the template or the highlighter, so this is unverified.

4. **Verified (checked, no issue). The request path.** `Query.post` (`services.js:368-383`) is a POST on the existing `:core/:handler` resource, with `core` and `handler` filled from the first argument. The body `{expr, wt, explain}` is form-encoded by `transformRequest`, and the `Content-Type` header is `application/x-www-form-urlencoded`. Upstream `SolrRequestParsers` has `FormDataRequestParser`, and `isFormData` accepts `application/x-www-form-urlencoded` (`SolrRequestParsers.java` around lines 681-690 at `upstream/main`), so `expr`, `wt`, and `explain` reach the handler as request params. The default `formUploadLimitKB` was not traced.

5. **Verified (checked, no issue). `explain` is unchanged in effect.** The old GET sent `explain=[true]`, which serialises as `explain=true`. The new body sets `explain` only when `doExplanation` is truthy, which gives the same value.

6. **Verified (checked, no issue). Error path and success path shapes.** `transformResponse` returns `{data: ...}` for both success and error responses, so `error.data.data` is the body text as the error callback reads it. The success path keeps `data.toJSON().data`, unchanged from the GET version.

7. **Verified (checked, no issue). Test selectors and input event.** `#expr` is the `ng-model="expr"` textarea, `#stream button[type=submit]` exists, and `#result` is inside `#stream` (`partials/stream.html:17-35`, same selector the existing test uses). Dispatching an `input` event matches how ng-model listens on a textarea. `waitFor`, `click`, and `waitForTextContains` exist in `AdminUiTestBase` (lines 332, 435, 467), and `driver` is `protected static` (line 107).

8. **Hypothesis. The new test would fail on `main`.** A 10,000-space expression in the GET URL is over Jetty's 8 KiB request header limit, per the author's note. Reasoned from the header size, not run. Whether the GET path actually returns 413 in the test harness is unverified.

## Owner calls (not decided here)

- **A. The address-bar link (finding 2).** Options: hide the link when the expression is long, rebuild it as a bare `/stream` page link with no `expr`, or leave it as is and note the limitation. Pose it. Not patched.

## Not checked

- Nothing compiled, formatted, or run. No focused test, no fail-before proof, no Spotless, no Selenium run (needs Chrome).
- The bundled AngularJS version was not checked. The `$resource` action signature `(params, body, success, error)` is taken from the pattern the file already uses, not from the library source.
- `formUploadLimitKB` default and whether the default configset's `/stream` handler reads `expr` from form params were not traced past the parser.
- `TESTING.md` is treated as unverified. Left in place.
- Finding 3 (error-path highlighting) is unverified.
