# PR 5241 overview replication error fix, Windows round 1

Status: IMPLEMENTED, PROOF PENDING.

- Assignment: Nick's direct request to patch the review finding, add a regression, and hand it to a gate agent.
- Implementation claim: `claims/pr-5241-overview-errors-windows-round-1.md`, published at board commit `aa3eaa0fdd260adb1121e3b8e400875e6970ff04`.
- Original reviewed PR head: `aa8272ef089c39df449c519551ef439557b876f8` on `epugh/solr:SOLR-16398-admin-ui-replication-details-v2`.
- Fix commit: `80ca9374f48f292af159c7e9600019b5947adc49`, author Nick Shanin, no trailers; direct child of the original PR head.
- Fork branch: `nick-boss-tech/solr:solr-16398-submit`. Plain push completed; GitHub ref API separately returned the exact fix SHA.
- Gate: `gates/pr-5241-overview-errors-round-1.md`, OPEN to a capable Linux host with a browser.
- Expected receipt: `receipts/PR-5241-overview-errors.md`.

## Change

`CoreOverviewController` now injects `ApiErrorHandler` and delegates replication API failures to it. This restores the shared 401 login flow, 403 permission warning, and server error reporting after the move from Angular $resource to the generated superagent client. It replaces the unconditional missing-configuration message without changing shared authentication logic or the API contract.

`AdminUiCoreOverviewErrorsTest` adds three browser tests using the existing standalone UI harness and real generated client. A test-only Jetty filter returns 401, 403, or 503 only for this core's replication status URL. Other HTTP requests pass through. Each test first waits for successful overview data and completion of initial Angular HTTP reads, then invokes the real replication refresh method. The test waits for the injected HTTP status before checking the rendered permission warning, actual server message, or login form. The 401 case also checks stale authorization-header removal and the saved overview return location. The filter and browser session are reset after each method.

This tests the HTTP/client/controller/rendering boundary. No fake callback or replacement ApiErrorHandler is used. Waiting for initial reads avoids unrelated successful $http responses clearing the permission warning during the assertion.

## Outbound files

Only these two files are changed by the follow-up commit (175 additions, two deletions):

- `solr/webapp/web/js/angular/controllers/core-overview.js`
- `solr/webapp/src/test/org/apache/solr/webapp/AdminUiCoreOverviewErrorsTest.java`

The contributor's parent commit is preserved. Its existing migration changelog remains unchanged; this small review fix does not need a second entry for the same unreleased migration. No dependencies, server permissions, shared test harness, or unrelated UI files changed.

## Verification and limits

Source and test paths were reviewed against the original PR head; `git diff --check aa8272ef089c39df449c519551ef439557b876f8 80ca9374f48f292af159c7e9600019b5947adc49` passed. Worktree status before commit contained only the two outbound files. Git commit author and parent were checked. The fork ref was read back separately after pushing and matches the full fix SHA.

No Gradle, Java compilation, formatter, browser test, or local test queue was run on Windows. The tests are written and statically reviewed, not claimed passing. The gate requests a clean tidy preflight, all three positive methods, five neighboring browser classes, root check, JS syntax check, and all three new tests failing on the original PR production code with only the new test file overlaid. The original upstream CI green at `aa8272ef089c` does not prove the fix head.

The live PR head and discussion must be refreshed before accepting final proof or offering the fixes upstream. A changed source/test head requires a new pinned gate. The contributor's public PR head has not moved, and no public comment, PR body edit, review submission, JIRA write, or PR creation was performed by this implementation.

Decision ladder: rung 1, restoring the shared error behavior established by app.js/httpInterceptor, services.js/ApiErrorHandler, and the dedicated replication controller. Browser runtime discrimination is explicitly delegated to the Linux fail-before leg.
