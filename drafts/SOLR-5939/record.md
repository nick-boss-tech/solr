# SOLR-5939 closeout record

Verdict: pr-needed (draft branch `solr-5939-draft-pr`).

- Pinned head: fork `origin/main` `498a09aea97aac86c2cc7ac114113b946d249942`. Upstream history is not cited.
- JIRA checked 2026-10-19: status Open, no resolution, fix versions 4.9 and 6.0 (the note in the queue says fixed in 4.9 and 6.0, which the code does not support). Created 2014-03-31, updated 2026-10-09. Description read in full. Five comments read in full: the reporter's demo patch (not to be committed), a note that a FIXME was left in the code for it, and a bulk fix-version move.
- Audit verdict on record: `deferred` (audit-3, 2026-10-06): still present; the audit drafted a per-request hook and backed it out as too deep to guess without a compiler.

## Code paths read at the pinned head

- `solr/core/src/java/org/apache/solr/update/StreamingSolrClients.java:66-78`: `getSolrClient(req)` caches one `ErrorReportingConcurrentUpdateSolrClient` per node URL. The client is built with the request that happened to be first (`req` passed to the builder at line 78).
- `StreamingSolrClients.java:131-146`: `handleError` attributes each error to that cached `req` (`error.req = req`, line 143 onward), and calls `req.trackRequestResult` and `req.shouldRetry` against it. A later request on the same node is reported against the first request's record.

## Existing work

- Fork branch `solr-5939-submit` has two commits by Nick Shanin (2026-10-07 and 2026-10-09) titled "Record streaming update errors against each request in the failed stream" and "Drop stream registry entries on success and count shared tolerant errors once". They were not reviewed in this closeout, and their base is not checked here.

## Attempts

- Read the cached-client construction and the error handler at the pinned head. The code path matches the ticket and the audit.
- No test run. A reproduction needs a test with two requests on one node where the second fails, asserting that the error is attributed to the second request. Writing that test is source work and was not done overnight.

## What the verdict does not cover

- Whether the work on `solr-5939-submit` fixes the attribution. Not reviewed.
- Whether the fix can be kept small enough to avoid the deep change the audit named.

## What the PR would add

A focused test with two requests on one cached client, one of which errors, asserting that the error record carries the failing request. The fix is the owner's and the claimant's to pick up in the daytime, with a compiler.
