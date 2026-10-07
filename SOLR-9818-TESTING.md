# SOLR-9818 - hypothetical reproduction (not run)

Nothing here was compiled or run. The change and test were guessed from reading `upstream/main`.

## JIRA context
When the Admin UI loses the connection (status 0) it replays the failed request immediately and without limit; a
collection reload or ADDREPLICA became hundreds of queued commands (one user got 23 new replicas). Jan Høydahl asked
to drop the retry buffer. The audit note said "Admin UI angular, obsolete UI".

## What the code shows on main
The angular UI is still the shipped one. `solr/webapp/web/js/angular/app.js`, `httpInterceptor.failed`:
`if (rejection.status === 0) { ... $http(rejection.config) ... }` replays any request, with no delay and no
method check. Only requests with the `doNotIntercept` header skip it.

## Change
- New global `isRetryableRequest(config)`: true for GET and HEAD only.
- Status 0 on a GET/HEAD: replayed after 1 s (was immediately). The "connection lost" banner behaviour is unchanged.
- Status 0 on any other method: not replayed; `$rootScope.exceptions[url]` gets a message saying the request was not
  repeated and may or may not have been processed, and the rejection is passed on.
- `AdminUiRetryPolicyTest` calls `isRetryableRequest` in the page.

## Guessed / verify first
- Reads are still retried forever (now once a second); a cap was not added.
- `$timeout(...)` returning the replayed promise from a `responseError` handler: angular resolves it as the new
  response, same as the old direct return.
- The `exceptions` entry is cleared by `started()` on the next request to the same URL.
- Selenium test: needs Chrome; not run.
