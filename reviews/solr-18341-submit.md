# solr-18341-submit

- Branch: origin/solr-18341-submit
- Head: 458b098719d7 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 14c7aac0d151, 45 commits behind, 9 commits ahead)
- Scope: 9 commits, 15 files (+889/-127). Core: `CloudSolrClient.java` (stale-state and route-503 replay gates), `LBSolrClient.java` and `LBAsyncSolrClient.java` (`mayFailOver`), `HttpSolrClient.java` (`wasRequestUnsent`, `wasCommError` moved here), `HttpJettySolrClient.java`, `HttpJdkSolrClient.java`, `SolrRequest.java` (`isRetriable()`, QUERY-only by default), `UpdateRequest.java` (`isRetriable()` overrides), changelog `SOLR-18341.yml` (7 lines). Tests: `CloudSolrClientCacheTest`, `LBSolrClientRetryUnsentTest`, `LBAsyncSolrClientTest`, `SolrRequestRetriableTest`, `WrappedSolrRequestTest`.
- Verdict: Needs work (two owner calls must be answered first; the compatibility and proof gaps are fixable by the author)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head. No `SOLR-18341-TESTING.md` exists on this branch.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-18341-review.md` was written at the same head (458b098719d7), so no delta exists. Its Round-12 disposition is taken from that file and was not re-read.

- Bulk F1 (stale-state retry suppressed for non-replayable requests): **confirmed as a fact, changed to an owner call.** The code and test are as described. The research note (`research/131-solr18341-research-note.md`, lines 310-314) records that this branch intentionally differs from competing SOLR-18402 on `INVALID_STATE`/404 stale-state retries and needs maintainer direction. See Owner call 1.
- Bulk F2 (compatibility scope and changelog): **confirmed.** See finding 2.
- Bulk F3 (protected `wasCommError` removed): **confirmed.** See finding 1.
- Bulk F4 (no SocketProxy coverage): **confirmed**, with the scope caveat in finding 3.
- Bulk F5 (route-503 UPDATE replay): **confirmed as code, reclassified as an owner call.** See Owner call 2.
- Bulk F6 (unrelated SOLR-18368 Javadoc): **confirmed.** See finding 4.

## Owner calls (not decided here)

1. **Stale-state retry for non-replayable requests (bulk F1).** `CloudSolrClient.java:793-799` now requires `mayReplay` (`request.isRetriable()`) before the INVALID_STATE/404 retry, so a non-replayable request gets one attempt and the exception. The head test `CloudSolrClientCacheTest.java:312-334` (`testNonRetriableRequestDoesNotRetryOnStaleState`) asserts exactly that for `DummyUpdateRequest`. Evidence for the other side, verified by reading: the state-version rejection at `solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:433` throws INVALID_STATE before the request is proxied, so on that path the update was not applied. The question to pose: keep the suppression (current head, matches the design record's intent), or allow a retry after an explicit INVALID_STATE because the server rejected the request before applying it. The design record says this is maintainer direction, so it is not re-decided here.

2. **Route-503 replay for UPDATE (bulk F5).** `CloudSolrClient.java:723-724` and `:756` never replay a route-503 `UPDATE`, even when `isRetriable()` is true (a plain `add`). The code comment says an update may have succeeded on one shard. The test `CloudSolrClientCacheTest.java:150-188` fixes the behavior. Base retried this case for every request type, so this is a behavior change. Pose it: is "not replayed after a partial multi-shard 503" the intended policy?

3. **Breadth of the default replay policy (bulk F2).** `SolrRequest.java:253-254` is QUERY-only by default. Compared with base, LB failover (`LBSolrClient.request`, `LBAsyncSolrClient.requestAsync`) and the cloud stale-state retry now treat ADMIN-with-collection, SECURITY, STREAMING, and UNSPECIFIED as non-retriable. Base excluded only UPDATE and ADMIN-without-collection. Pose it: is the wider default intended, and should the changelog say so?

## Findings (ranked)

1. **MEDIUM, verified. Protected `wasCommError` removed from `CloudSolrClient`.** The diff removes `protected boolean wasCommError(Throwable)` from `CloudSolrClient.java` (hunk `@@ -206,16 +203,6`). Base defines it at `upstream/main:solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:213`. At head it is defined only on `HttpSolrClient.java:402`. In-tree, `git grep wasCommError` shows no subclass of `CloudSolrClient` overriding it (the only override is `HttpJettySolrClient.java:569`, which extends `HttpSolrClient`). Downstream subclasses that override the former protected method on `CloudSolrClient` will fail to compile under `@Override`, or silently stop being consulted. Compatibility path: keep a deprecated protected method on `CloudSolrClient` that delegates to `getHttpClient().wasCommError`, or add a migration note.

2. **MEDIUM, verified. Compatibility and changelog scope understated.** The changelog (`changelog/unreleased/SOLR-18341.yml`, 7 lines, `type: changed`) describes only the double-application fix for `inc`/`add`. It does not mention the replay-policy change (owner call 3), the LB failover change, the route-503 UPDATE change, or the protected-method removal. Proposed fix (not applied): extend the changelog text to cover each behavior change, once the owner calls are answered.

3. **MEDIUM, verified in the diff. No SocketProxy integration coverage.** A grep of the full three-dot diff for `SocketProxy` returns no matches. The new tests are mock-based (`CloudSolrClientCacheTest`, `LBSolrClientRetryUnsentTest`, `LBAsyncSolrClientTest`). The bulk review states that the ticket asks for SocketProxy mid-request and connection-refused cases. That JIRA text was not re-read in this review, so the ticket-requirement part is carried from the bulk review, not verified here.

4. **LOW, verified. Unrelated Javadoc hunk.** `CloudSolrClient.java:1550` (in `Builder.internalClientBuilder` Javadoc) adds "This replaces the Solr 9.10-deprecated builder method name, which was removed in Solr 11 (SOLR-18368)." That text belongs to a different ticket. Proposed fix (not applied): drop that paragraph from this branch.

5. **MEDIUM, hypothesis. Name-based connect-timeout check may not reach the HTTP failover path.** `LBSolrClient.isConnectException` (head `LBSolrClient.java:697-702`) still matches any exception whose class name ends in `ConnectTimeoutException`. The new HTTP path `LBSolrClient.wasRequestUnsent` calls `HttpSolrClient.wasRequestUnsent` (`HttpSolrClient.java:290-293`), which checks only `RequestNotSentException` and `ConnectException`. `HttpJdkSolrClient` adds `HttpConnectTimeoutException` (`HttpJdkSolrClient.java:248-253`). `HttpJettySolrClient` adds nothing. Base `IOException` handling let a connect timeout fail over a non-retryable request through `isConnectException`. If Jetty's connect timeout is not a `ConnectException` and carries the name-based class, an update that base would fail over would now throw. Not checked against Jetty's actual exception types; confirm before any patch.

6. **LOW, hypothesis. `wasRequestUnsent` calls `getClient(endpoint)` in the failure path.** `LBSolrClient.java` (new private `wasRequestUnsent(Endpoint, Exception)`) calls `getClient(endpoint)` to pick the classifier. Base did not call it in the error path. Not checked whether `getClient` has side effects for the LB implementations.

## Proposed fixes (not applied; the owner decides)

- Finding 1: add a deprecated `protected boolean wasCommError(Throwable)` on `CloudSolrClient` that delegates to the transport.
- Finding 3: add one SocketProxy case for a mid-request reset and one for a refused connection, on the Jetty client, per the ticket text.
- Finding 4: remove the SOLR-18368 Javadoc paragraph.
- Finding 5: add a Jetty connect-timeout case to `LBSolrClientRetryUnsentTest`, after checking what Jetty throws.
- Owner calls 1-3: no patch until the owner answers.

## Interactions with other branches

- Competing work: SOLR-18402 / Apache Solr PR #4829 changes the same Cloud/LB retry boundary and differs on INVALID_STATE/404 stale-state retries (per `research/131-solr18341-research-note.md`, lines 310-314). Do not submit this branch separately until that is consolidated.
- The `wasCommError` move touches the same code as the SOLR-15478 and SOLR-17612 branches reviewed in this round (LB/Cloud retry paths). Not compared line by line here.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- The saved queue result (`research/test-queue/results/SOLR-18341.json`, FAILED from 2026-10-01 per the bulk review) was not re-read. The bulk review says it predates current commits.
- The JIRA ticket text (SocketProxy requirement) was not re-read.
- `UpdateRequest.isDocumentRetriable` body and the `LBAsyncSolrClientTest` and `LBSolrClientRetryUnsentTest` assertions were not read line by line.
- No GitHub or JIRA writes were made.
