# solr-9818-submit

- Branch: origin/solr-9818-submit
- Head: 63f2d7ce9267 (matches the listed head; checked against the ticket worktree)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d9680, 16 commits behind, 3 commits ahead)
- Scope: 3 commits, 4 files, +95/-3. `solr/webapp/web/js/angular/app.js` (new global `isRetryableRequest`; `httpInterceptor.failed` status-0 branch replays GET/HEAD after 1 s via `$timeout`, rejects other methods with a user message in `$rootScope.exceptions`), `solr/webapp/src/test/org/apache/solr/webapp/AdminUiRetryPolicyTest.java` (new, predicate only), changelog `changelog/unreleased/SOLR-9818-admin-ui-no-blind-retry.yml` (`type: fixed`, author Nick Shanin), and `SOLR-9818-TESTING.md` (author's hypothetical-reproduction note, left in place).
- Verdict: Nearly
- Reviewer: review-agent A3 (claude-haiku-5-5, Claude Code), 2026-10-08
- Patch: none

Nothing here was compiled, formatted, or run. No Gradle, no spotless, no Selenium test, no fail-before proof. Every claim below rests on reading the diff and the code at the listed head. `SOLR-9818-TESTING.md` is labeled "hypothetical reproduction (not run)" and is treated as unverified.

## Findings (ranked)

1. **MEDIUM, verified. The test does not exercise the fix.** `AdminUiRetryPolicyTest` only calls the global predicate `isRetryableRequest` on hand-built configs (`{method: 'POST', url: '/x'}` and so on). It never produces a status-0 rejection, so it does not show that the interceptor skips the replay for a POST, PUT, DELETE, or PATCH. On `main` the predicate does not exist, so the test fails with a missing-function error, not the behaviour under test. A fail-before PASS would therefore be met trivially. The test needs to drive `httpInterceptor.failed` with a status-0 rejection, or the proof must be read with this gap in mind. Not patched: a browser test cannot be written and checked by reading alone.

2. **LOW, verified. A comment now sits on the wrong code.** `app.js:18` keeps the SOLR-14120 comment ("Providing a manual definition for the methods 'includes' and 'startsWith' ...") directly above the new `isRetryableRequest` function, so the comment describes the function instead of the `String.prototype.includes` polyfill below it. Cosmetic. Not patched.

3. **LOW, verified. Change-describing comments.** The repo's `AGENTS.md` says changes should not carry code comments that communicate the change. The branch adds `app.js` line 19 ("SOLR-9818: only requests that do not change anything ..."), the `// SOLR-9818: a command ...` comment in the interceptor, and the `// SOLR-9818: a collection reload ...` comment in the test. Wording-only. Not patched.

4. **Verified (checked, no issue). Injection and rendering.** `httpInterceptor` is declared with `$q`, `$rootScope`, `$location`, `$timeout`, `$injector` (`app.js:380`), so `$timeout` and `$q` resolve. The `exceptions` entry is rendered by the `#http-exception` `ng-repeat` in `index.html:152`, so the "Connection to Solr lost ... not repeated" message is visible. `started()` deletes the entry on the next request to the same URL (`app.js:387-388`), which matches the author's note.

5. **Verified (checked, no issue). Replay path.** For GET/HEAD on status 0, the handler returns `$timeout(function() { return $http(rejection.config); }, 1000)`. The `$timeout` promise resolves with the replayed response, so the caller sees the same result as the old direct return. For other methods the handler sets the message and returns `$q.reject(rejection)`. The changelog text matches the code: each failed read is replayed after 1 s, and the non-GET message is shown.

6. **Hypothesis, LOW. Callers of non-GET requests.** Callers that issue a POST, PUT, DELETE, or PATCH and attach no error callback may now leave their UI state (spinners, pending flags) in place when the rejection arrives, where the old code replayed the request and eventually resolved it. Not traced across the controllers.

## Owner calls (not decided here)

- **A. Cap on read replays.** Reads are still retried every second without limit when the server stays away (TESTING note, "Guessed / verify first"). The JIRA asks only for the non-GET part. Whether reads need a cap is a product call. Pose it. Not patched.

## Not checked

- Nothing compiled, formatted, or run. No focused test, no fail-before proof, no Spotless, no Selenium run (needs Chrome).
- Callers of non-GET requests across the Admin UI controllers, for finding 6.
- Whether `index.html` `id="index"` exists for the test's `openPage("", By.id("index"))` route. Not checked.
- The bundled AngularJS version was not checked. `$timeout` and `$q` semantics are taken from how the file already uses them.
- `SOLR-9818-TESTING.md` is treated as unverified. Left in place.
