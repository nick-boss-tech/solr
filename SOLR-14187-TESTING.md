# SOLR-14187 - hypothetical reproduction (nothing was compiled or run)

JIRA (2020): SolrJ async admin helpers do not work with per-request basic auth (credentials set on the request, not on the
client). The audit note said "already fixed by SOLR-15575 (propagateBasicAuthCreds covers `processAndWait` -> RequestStatus and
`waitFor` -> deleteAsyncId)". That is right for `AsyncCollectionAdminRequest.processAndWait` and for `RequestStatus.waitFor`
(its DELETESTATUS inherits the status request's credentials), but one async helper is still unreachable with credentials:
the static `CollectionAdminRequest.waitForAsyncRequest(requestId, client, timeout)` builds a bare `requestStatus(requestId)`,
so every poll and the delete go out unauthenticated, and the signature offers no way to set credentials.

## What main does (read on `upstream/main`)
- `processAndWait`: `propagateBasicAuthCreds(requestStatus(asyncId)).waitFor(...)` (~L260), fine.
- `RequestStatus.waitFor`: `this.process(client)` polls with `this`' credentials; `propagateBasicAuthCreds(deleteAsyncId(requestId))` (~L1874), fine.
- `waitForAsyncRequest(String, SolrClient, long)` (~L1829): `requestStatus(requestId).waitFor(client, timeout)`, no credentials possible.
  (Callers can build `requestStatus(id)` themselves, set credentials and call `waitFor`; the static helper is the odd one out.)

## Change
New overload `waitForAsyncRequest(String requestId, SolrClient client, long timeout, String basicAuthUser, String basicAuthPassword)`
that sets the credentials on the status request before `waitFor`. The existing three-argument form is unchanged.
New `CollectionAdminRequestAsyncAuthTest` (stub `SolrClient` recording requests; with credentials both the poll and the delete carry them,
without credentials neither does).

## Guesses to verify first
- `RequestStatus.process(client)` / `DeleteStatus.process(client)` against a stub client returning only `status/state=completed`
  (the delete response is parsed as a plain `CollectionAdminResponse`).
- API shape is a pick (explicit user/password). An alternative is taking the original `SolrRequest` and copying its credentials.
- `SolrRequest.getBasicAuthUser/getBasicAuthPassword` are public (used by `propagateBasicAuthCreds` in the same package hierarchy, not verified for the test package).

## Fail-before
The five-argument overload does not exist on `upstream/main`, so the new test fails to compile there (a compile failure, not a
runtime one; the queue's fail-before stage would report `INCONCLUSIVE`).
