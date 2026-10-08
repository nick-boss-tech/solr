# solr-11356-submit

- Branch: origin/solr-11356-submit
- Head: 8474e5a3a26d (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d968, 18 commits behind)
- Scope: 3 commits. `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/ConcurrentUpdateJettySolrClient.java` (+20/-2: `OutStream` remembers the opening request's user, password, and headers; `belongToThisStream` compares them), `solr/solrj/src/test/org/apache/solr/client/solrj/impl/ConcurrentUpdateSolrClientTestBase.java` (+50: `testRequestsWithDifferentCredentialsAreNotSentOnOneStream`, and `TestServlet` records each document's `Authorization`), the changelog fragment, and `SOLR-11356-TESTING.md` (kept in place).
- Verdict: Nearly
- Reviewer: review-agent A2 (claude-haiku-5-5, Claude Code), 2026-10-08

Nothing here was compiled, run, or tested. No Gradle. Every claim below was checked by reading code, except where marked as hypothesis or not traced. Patches: none.

## Premise check (hypothetical-reproduction handoff)

The TESTING doc says queued requests with different credentials could share one streaming HTTP request, so the second request went out under the first request's identity. The code supports this on main.

- VERIFIED: `initOutStream` sets the stream's credentials once, from the request that opened it (`ConcurrentUpdateJettySolrClient.java:224`, `client.decorateRequest(postRequest, updateRequest, false)`). One HTTP request therefore carries one identity.
- VERIFIED: the streaming loop asks `belongToThisStream` before it adds a queued request to the open stream (`:88`). On main that method compared only the update params and the collection, so requests with different users shared one stream.
- VERIFIED: `SolrRequest` exposes `getBasicAuthUser()`, `getBasicAuthPassword()`, and `getHeaders()` (`solrj/.../SolrRequest.java:145-151`, `:374-377`), and the fix uses them.
- VERIFIED: the JIRA's 2017 symptom ("Basic Authentication not supported") is obsolete, because the client now sends credentials per request. This branch addresses a different and real problem: credentials from one request leaking onto another request's documents.

## Findings (ranked)

MEDIUM (hypothesis, fail-before is racy): The test may pass on main as well. One runner thread drains the queue while the test enqueues 20 requests. If the runner drains each request before the next one is enqueued, every request gets its own stream, and the test passes on main. The TESTING doc says so: "NOT_PROVEN or a flaky fail-before verdict". The Linux gate will read that as unproven. Owner call: accept a racy fail-before, or make the test deterministic (for example, hold the runner until all requests are queued). A deterministic version changes the test's design, so it is not patched here.

LOW (verified, accepted trade-off): Requests with no credentials and requests with credentials, or with different custom headers, no longer share a stream. Mixed callers therefore get more and shorter HTTP streams. The throughput cost was not measured. The TESTING doc records this as a known effect.

## Verified correct (by reading; not run)

- The fix keeps the existing check. `belongToThisStream` is still called at `:88` and `:241`. The new comparisons only add user, password, and headers. Both a `null` header map and an equal header map compare correctly, because `Objects.equals` handles `null` and `Map.equals` compares contents. `getHeaders()` returns an unmodifiable view, which compares by content.
- The stream's identity is taken from `updateRequest`, the request that opened it (`initOutStream` parameter at `:206`, and the `OutStream` construction at `:231`).
- The `TestServlet` change records the `Authorization` header on the servlet thread that reads the request. `currentAuthorization.set` is in `post` (`ConcurrentUpdateSolrClientTestBase.java:140-143`), and `update` runs on that same thread (`:169-171`). This confirms the TESTING doc's first guess.
- The other concrete subclass, `ConcurrentUpdateJdkSolrClientTest`, also runs the new test. The JDK client does not share streams: `doSendUpdateStream` creates one request per `Update` (`ConcurrentUpdateJdkSolrClient.java:39-43`). It also sends each request's own credentials (`HttpJdkSolrClient.java:476-501`, `setBasicAuthHeader`). So the test passes there by construction, and the shared fix does not break it.
- The changelog fragment matches the upstream format (title, `type: fixed`, ICLA author, JIRA link).
- All three commits are authored as the ICLA identity, with no Co-Authored-By trailers.

## Owner decisions

1. The fail-before outcome: accept a racy result, or make the test deterministic (see the first MEDIUM).

## Not checked

- Nothing was compiled or run.
- The `Authorization` format the test expects (`"Basic " + Base64(user:password)`, UTF-8) was not traced in the Jetty decorate path. The JDK path uses `basicAuthCredentialsToAuthorizationString`, which was not read either.
- The base test helpers `solrClient(null)` and `concurrentClient(...)` were not traced for their signatures.
- Other runner or queue behavior in `ConcurrentUpdateBaseSolrClient` beyond the `:88` loop check.
- Upstream conflicts. The branch is 18 commits behind `upstream/main`.
