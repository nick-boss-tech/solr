# Skip record: SOLR-11675

- Ticket: SOLR-11675, v2 API should gracefully decline unexpected HTTP methods. JIRA read in this pass: Open, resolution none, fix versions none, 1 comment (2017-12-03, gerlowskija pointing at getApiInfo in V2HttpCall); updated 2019-06-08.
- Verdict: keep-open.
- Draft branch: solr-11675-draft-open (unchanged status; this record replaces the bulk audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: a DELETE, PATCH or PROPFIND sent to /api/c returns an error body with "Index: 1, Size: 1" and an IndexOutOfBoundsException trace from V2HttpCall.init (the 2017 trace at V2HttpCall.java:110, an ArrayList.get). The reporter wants the request declined gracefully. The 2017 comment points to getApiInfo as the place the method lookup happens.

## Code reading

- solr/core/src/java/org/apache/solr/api/V2HttpCall.java:111 and :133: pathSegments.get(0) and get(1) are read; the get(1) read at :133 is inside the branch that requires pathSegments.size() > 1 (line 131 check), so the path /api/c with no collection name does not reach it.
- V2HttpCall.java:114-115 (init): getApiInfo is called with the request method. At :233-275 (getApiInfo): if the method has no API, v2lookup returns null and getApiInfo returns null (the path is retried with a null method only for introspect requests).
- V2HttpCall.java:169-180 (the "cores" branch) and :184-187: the read pathSegments.get(1) on the core-level branch runs only after core is non-null, and a null core for /c with no collection name goes to initAdminRequest and returns at V2HttpCall.java:~176-180 before line 187.
- V2HttpCall.java:202 (catch RuntimeException) is the handler for runtime errors during init; its response shape was not traced.

So the specific IndexOutOfBounds of the 2017 trace is not reachable by reading at the pin for the /c path, because the size guard now precedes the read. What a client receives for DELETE, PATCH or PROPFIND on /api/c at the pin (status code and body) is not established by reading the code.

## History

- No commit at the pin names SOLR-11675 (git log --grep at the pin: none).
- The commit that added the size guard around the collection read was not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs a running Solr at the pin and requests with DELETE, PATCH and PROPFIND to /api/c and to /api/c/<collection>, recording status code and body. Not built or run in this pass.

## Gap analysis

The IndexOutOfBounds in the ticket is not reachable on the path the ticket names, by reading. Whether the other methods now decline gracefully with a 4xx and a useful message, or still return an unhelpful body, is the open question the ticket asks. Neither a close-fixed (no commit names the ticket) nor a close-obsolete (the message text is produced at runtime, so a whole-tree grep cannot settle it) verdict is supported without that run.

## Verdict justification

keep-open: the reported failure is not reachable by reading, but the client-visible result for the named methods is unverified, so neither close verdict has its check. close-cannot-reproduce needs a run attempt, and none was made.

Missing for a closing verdict: a run at the pin sending DELETE, PATCH and PROPFIND to /api/c and to /api/c/<collection>, recording the status code and body for each.

## Limits

This record does not run a Solr server, does not trace the guard's commit history, and does not post anything to JIRA or GitHub.
