# SOLR-9759 - hypothetical reproduction (not run)

Nothing here was compiled or run. The change and test were guessed from reading `upstream/main`.

## JIRA context
Gus Heck pasted a large streaming expression into the Admin UI Stream screen and got HTTP 413 (the expression travelled
in the URL, above Jetty's 8 KiB request header size); the UI showed nothing, and the console had a `JSON.parse` error.
The audit note said "Admin UI angular, obsolete UI".

## What the code shows on main
The angular UI is still the shipped one. `controllers/stream.js` calls `Query.query` (a GET with `expr` in the query
string); the `Query` resource opts out of the global error handler (`doNotIntercept`) and the controller passes no
error callback, so a 413 is invisible. `jetty.xml` still defaults `solr.jetty.request.header.size` to 8192.

## Change
- `services.js`: `Query` gets a `post` action, form-encoded body (`expr`, `wt`, optional `explain`).
- `stream.js`: `doStream` uses `Query.post`; a new error callback prints `Request failed with HTTP <status>` and the
  body in the response box.
- `AdminUiStreamScreenTest.testLongStreamingExpressionViaUi`: a 10,000-space padded `search(...)` expression, set
  through the page with JavaScript (an input event for `ng-model`), still returns all three docs.
- The address bar link under the form is still the GET URL form (display only; it will not work for a very long
  expression).

## Guessed / verify first
- `/stream` accepts form-encoded POST parameters (the documented `--data-urlencode 'expr=...'` curl form); the handler
  reads `expr` from the request params.
- `$resource` non-GET action signature `(params, body, success, error)`.
- `error.data` is the `{data: ...}` object from `transformResponse`.
- Selenium test: needs Chrome; not run.
