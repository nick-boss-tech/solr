# SOLR-11356 - hypothetical reproduction (not run)

Nothing here was compiled or run. The change and test were guessed from reading `upstream/main`.

## JIRA context
"Basic Authentication not supported on ConcurrentUpdateSolrClient" (2017). The audit note said it was obsolete because
the client was rewritten on the Jetty client and sends credentials per request.

## What the code shows on main
Credentials are sent: `initOutStream` calls `client.decorateRequest(postRequest, updateRequest, false)`, which adds the
`Authorization` header from the request that opens the stream. But the streaming loop in `doSendUpdateStream` keeps
draining the queue into that same HTTP request as long as `OutStream.belongToThisStream` says yes, and that method only
compared the update params and the collection. Two queued `UpdateRequest`s with different `setBasicAuthCredentials`
(or different custom headers) therefore shared one stream, and the second one went out under the first one's identity.
That is the same class of problem as SOLR-12803 (documents sent to the wrong collection through a reused stream), here
for the caller's identity.

## Change
`ConcurrentUpdateJettySolrClient.OutStream` remembers the user, password and headers of the request that opened it, and
`belongToThisStream` also requires them to be equal. A request that differs is put back on the queue and the stream is
closed, exactly as already happens for different params or collection (a new runner opens the next stream).

Test: `ConcurrentUpdateSolrClientTestBase.testRequestsWithDifferentCredentialsAreNotSentOnOneStream` (runs for every
concrete subclass, so the Jetty client): one runner thread, 20 single-document requests alternating `alice` and `bob`.
`TestServlet` now records the `Authorization` header of the HTTP request each document arrived in; every document must
have arrived under its own user's Basic header.

## Guessed / verify first
- `TestServlet.update` runs on the servlet thread that read the request, so the thread-local header is the right one.
- The queue may not hold two items at once if the runner drains faster than the test enqueues; the test is then a pin
  (passes on main). With a single runner thread and 20 quick enqueues it should usually fail without the fix; expect
  `NOT_PROVEN` or a flaky fail-before verdict.
- Requests with no credentials versus credentials now split streams too (more, shorter streams for mixed callers).
- `getHeaders()` returns an unmodifiable view; equality is by content (`Map.equals`), which is what is wanted.
