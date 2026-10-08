# solr-12161-submit

- Branch: origin/solr-12161-submit
- Head: 1725cbd8489 (listed head a40db4fdb5c4, plus one review patch commit)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d968, 16 commits behind)
- Scope: the author's 2 commits plus 1 review patch. `solr/core/src/test/org/apache/solr/security/BasicAuthIntegrationTest.java` (+14 in `testBasicAuth`, then the patch), `SOLR-12161-TESTING.md` (kept in place). Test-only: no production code changed.
- Verdict: Needs work
- Patch: 1725cbd8489, `SOLR-12161: expect SolrException in no-credentials batch test (CloudSolrClient throws RouteException, not RemoteSolrException)`
- Reviewer: review-agent A2 (claude-haiku-5-5, Claude Code), 2026-10-08

Nothing here was compiled, run, or tested. No Gradle. Every claim below was checked by reading code, except where marked as hypothesis.

## Premise check (hypothetical-reproduction handoff)

The TESTING doc concludes: "`PKIAuthenticationPlugin` only adds its header when `isSolrThread()` / a `SolrRequestInfo` is present (around line 389), so the described path looks closed." The code says otherwise.

- VERIFIED: `CloudSolrClient`'s request pool is `ExecutorUtil.newMDCAwareCachedThreadPool(...)` (`CloudSolrClient.java:121-123`). That creates a `MDCAwareThreadPoolExecutor` (`ExecutorUtil.java:231-233`, class at `:261`).
- VERIFIED: that executor's `execute` wrapper sets the server-thread flag unconditionally for every task it runs (`ExecutorUtil.java:371-373`). `isSolrServerThread()` returns true for those tasks (`:430-431`).
- VERIFIED: `PKIAuthenticationPlugin.isSolrThread()` returns `ExecutorUtil.isSolrServerThread()` (`PKIAuthenticationPlugin.java:417-419`). For a request with no `SolrRequestInfo`, `getUser()` falls to the `else` branch and returns `NODE_IS_USER` when `isSolrThread()` is true (`PKIAuthenticationPlugin.java:375-397`).
- VERIFIED: the pool runs the shard requests (`CloudSolrClient.java:330-335`). So an update without credentials, sent by `cluster.getSolrClient()`, runs on a thread that counts as a Solr server thread. That is the mechanism the JIRA describes (Noble Paul's diagnosis), and it matches the code.
- HYPOTHESIS: the test JVM's `cluster.getSolrClient()` has the PKI interceptor installed, so the node-identity header is attached and the in-JVM nodes accept it. The JIRA says so, but it was not traced here.

Consequence: the TESTING doc's "looks closed" conclusion does not hold. If the hypothesis holds, the update succeeds without credentials, the new `expectThrows` gets no exception, and the test fails on the current head. That would be a reproduction, not a pin, and the doc's `NOT_PROVEN` expectation would be wrong.

## Findings (ranked)

MEDIUM (verified in part, hypothesis in part): The branch may reproduce the bug instead of pinning it. See the premise check. The fail-before expectation in the TESTING doc is probably wrong, and the Linux gate should expect a real failure on the current head, not `NOT_PROVEN`.

MEDIUM (direction call): If the test reproduces, the fix changes how Solr identifies server threads. The JIRA suggests a flag, so a pool used outside Solr does not set the PKI header. Other options are moving `CloudSolrClient`'s pool off the server-pool executor, or making `ExecutorUtil`'s flag opt-in. Each changes shared thread-identity behavior. This is an owner design call. It is not patched here, and it is not test-only.

LOW (verified, patched in 1725cbd8489): The expected exception type was wrong. `CloudSolrClient` throws `RouteException` on the parallel path (`CloudSolrClient.java:354-362`, class at `:1706`). `RouteException` extends `SolrException`, not `RemoteSolrException`. So `expectThrows(RemoteSolrException.class, ...)` would fail with "unexpected exception type" even when the server correctly returns 401. The patch changes the expected type to `SolrException` and adds the import. `parallelUpdates` defaults to true (`CloudSolrClient.java:1298`), and `MiniSolrCloudCluster` does not override it.

## Verified correct (by reading; not run)

- Placement. The new block runs after the `update` permission is restricted to `admin` (`BasicAuthIntegrationTest.java:236-240`) and after the credentialed `deleteByQuery` (`:244-246`), so the restriction is in force.
- The batch has 30 documents (ids 200-229). Across the 3-shard collection, that produces several routes and takes the parallel path, which is what the patch's type assumes.
- The patched `assertEquals(401, batchFailure.code())` relies on `RouteException`'s code being 401. That follows from `ErrorCode.getErrorCode(e.code())` on the first `SolrException` (`CloudSolrClient.java:355-358`). The mapping itself was not traced.
- The patch is limited to the expected type and its import (`git diff` shows 3 insertions and 2 deletions).
- The commits are authored as the ICLA identity, with no Co-Authored-By trailers.

## Owner decisions posed (not decided, no fix made)

1. If the test reproduces on the current head: which fix is wanted for the server-thread flag (a flag on `CloudSolrClient`'s pool, a non-server pool, or an opt-in on `ExecutorUtil`). See the second MEDIUM.
2. Whether the branch should stay a test-only reproduction (expecting a failure) or be held until a fix is decided.

## Not checked

- Nothing was compiled or run. Whether the new test fails on the current head is not determined.
- How `cluster.getSolrClient()` gets the PKI `HttpClientBuilderPlugin` in the test JVM. This is the HYPOTHESIS above.
- The BasicAuth plugin's `interceptInternodeRequest` return value for these requests (`PKIAuthenticationPlugin.java:320-336`).
- `ErrorCode.getErrorCode(401)` mapping (see the verified-correct note).
- Upstream conflicts. The branch is 16 commits behind `upstream/main`.
