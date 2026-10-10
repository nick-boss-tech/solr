# Security and authentication round 1, part S4: SOLR-12161 (audit only, test-only, premise never run)

Audit only. No builds, tests, `gh` writes, commits, pushes, posts or file edits. Code citations are repo-relative paths at the SHA named, read with `git show`.

## Verdict

Still undetermined until the premise run, but the reading predicts a pin. The scenario is unpinned on main (item 1). No code path on main attaches a PKI identity to the client the test uses (item 2). The 401 comes from the authorization layer, and the adjusted `SolrException` expectation is correct (item 3). The expected result: the new block passes at the head, with a `RouteException` (a `SolrException`) carrying code 401. Nothing was run, so this is a reading. The run settles it: a pass is a pin (closed, documented); a failure with the batch accepted would be a live defect (item 4).

**Head verification:** `git ls-remote origin refs/heads/solr-12161-submit` returns `1725cbd8489889a1dad75279e4b2c11b0ec93d66`, matching the expected `1725cbd84898` and the receipt. The local `refs/remotes/origin/solr-12161-submit` resolves to the same SHA. The 2026-10-08 move (`a40db4fdb5c` to `1725cbd84898`) is one file, 3 insertions and 2 deletions (`RemoteSolrException` to `SolrException`, plus the import), as the receipt says. `cabedd1d968` to the head is two files, 42 insertions (15 in `BasicAuthIntegrationTest.java`), as the receipt says. The commit message the receipt quotes is present.

**Drift:** `upstream/main` `8e62c2686882` changed `BasicAuthIntegrationTest.java` after the base (commit `8e62c268688`, SOLR-18234: the `StatusTool` block is replaced by a `SystemInfoRequest` 401 check at main lines 283 to 292). That is outside the 12161 hunk (head lines 248 to 260), so the hunk should apply without conflict (not applied). No auth-path production file differs between `cabedd1d968` and `upstream/main`; the drift is in `UpdateLog.java`, `AbstractFullDistribZkTestBase.java` and the Gradle lockfiles.

## Item 1: existing 401 checks in `BasicAuthIntegrationTest` on main

Line numbers are on main.
- Lines 146 to 148: `cluster.getSolrClient().request(genericReq)` with no credentials, an admin set-user request; asserts 401. This is `CloudSolrClient`, but it is admin, not an update.
- Lines 215 and 217: `solrClient2` (`secondRandomJetty.getSolrClient()`, a node-local Jetty client) reloads without credentials. It asserts only `RemoteSolrException`, not the 401 code.
- Lines 225 to 233: a reload through the cluster client with a wrong password. It asserts only the exception type.
- Lines 259 to 262: `new UpdateRequest().deleteByQuery("*:*").process(aNewClient, COLLECTION)` with no credentials, where `aNewClient` is `aNewJetty.getSolrClient()` (a node-local `HttpJettySolrClient`, `JettySolrRunner.java` line 134). It asserts 401. This is the only update without credentials that expects 401 in the file, but it is not through `CloudSolrClient` and is one request, so it never reaches the `CloudSolrClient` pool.
- Lines 288 to 290: `SystemInfoRequest` on a Jetty client, no credentials, 401. Not an update.
- Lines 299 to 301: `cluster.getSolrClient().query(COLLECTION, params)` with no credentials, 401. A query through `CloudSolrClient`, not an update.

Other tests asserting 401 (a grep over `solr/core/src/test`, the SolrJ tests, the test framework and the modules):
- `cloud/DistribDocExpirationUpdateProcessorTest.java` lines 113 to 135: an unauthenticated query through the cluster client, 401. Its updates use `setAuthIfNeeded` (lines 71 to 77 and 147), so they carry credentials.
- `security/AuditLoggerIntegrationTest.java` lines 370 to 383: an anonymous CREATE through `CloudSolrClient`, 401 (line 378). Admin, not an update.
- `cloud/TestAuthenticationFramework.java` lines 61 to 77: the 401 comes from the first `createCollection` (admin). The update at line 111 is never reached.
- `security/BaseTestRuleBasedAuthorizationPlugin.java` lines 135 to 145: an anonymous POST to `/update/json/docs` gives PROMPT (401) in the unit-level `authorize()` check (subclass `TestExternalRoleRuleBasedAuthorizationPlugin.java` line 32). Its `checkRules` helper swallows `IOException` (lines 844 to 846). `security/ExtractingRequestHandlerPermissionTest.java` lines 93 to 99: an anonymous `/update/extract` gives PROMPT. Both are authorization decisions with mock contexts, not HTTP, not a client, not PKI.
- `RuleBasedAuthorizationCoreCollectionTest` and `TestAuthorizationFramework`: no unauthenticated update 401 assertion.

**Result:** no test on main sends an update without credentials through `CloudSolrClient` and asserts 401. The scenario is unpinned, as the receipt says. The TESTING note's list of existing checks is incomplete: it omits the cluster-client 401s at lines 148 and 301, and the reload checks do not assert 401 (see receipt disagreement 2).

## Item 2: the PKI-header path on main

- **Header writer:** `PKIAuthenticationPlugin.java` lines 315 to 365 install a Jetty listener. `onQueued` (lines 321 to 344) caches the user; `onBegin` (lines 347 to 355) adds the `SolrAuthV2` header (lines 352 to 354). `getUser()` (lines 375 to 398): a request principal is used when a `SolrRequestInfo` exists and is not a server token; otherwise, if `!isSolrThread()`, it returns empty (lines 389 to 394), and if `isSolrThread()`, it returns `NODE_IS_USER` `"$"` (line 396). `setHeader()` (lines 411 to 415) has no production caller.
- **The `isSolrThread()` gate is open on `CloudSolrClient` pool threads.** `MDCAwareThreadPoolExecutor.execute` sets the server flag on every task (`ExecutorUtil.java` lines 335 to 373; the class at line 261; the flag at lines 427 to 437). `CloudSolrClient` builds its pool that way (`CloudSolrClient.java` lines 121 to 123; `ExecutorUtil.java` lines 231 to 234). So the receipt's reason (line 389) does not close the path.
- **The path is closed by listener placement.** The listener is installed only on node-owned clients: `CoreContainer.java` lines 605 to 607, which call `HttpShardHandlerFactory.java` lines 325 to 327, `UpdateShardHandler.java` lines 252 and 253, and `HttpSolrClientProvider.java` lines 80 and 81. There is no global or static listener (a grep over `solr/**`). `MiniSolrCloudCluster.newSolrClient` (lines 683 to 692) builds `CloudJettySolrClient` over an `HttpJettySolrClient.Builder` with no listener factory. `HttpJettySolrClient.java` lines 154 to 157 default to an empty list, so the loop at lines 656 to 661 does nothing. So `cluster.getSolrClient()` never gets a `SolrAuthV2` header, on any thread.
- **Standalone:** the PKI plugin is created only in ZooKeeper-aware mode (`CoreContainer.java` lines 824 to 832), so standalone has no PKI path.
- **Server side:** `AuthenticationFilter.java` lines 100 to 103 and 149 to 152 send a request carrying `SolrAuthV2` to the PKI plugin instead of the configured one. `PKIAuthenticationPlugin.java` lines 136 to 139 return 401 without the header. A `"$"` header maps to `CLUSTER_MEMBER_NODE` (lines 157 to 160), and `HttpSolrCall.java` lines 613 to 617 then skip authorization. So a `"$"` header would admit an unauthenticated update, which is the exact failure the pin detects.
- **`BasicAuthPlugin`:** no reference to `SolrAuthV2` at all. With `blockUnknown` false (still false at the batch point; set true only later at head lines 284 to 289), an anonymous request passes through (`BasicAuthPlugin.java` lines 167 to 176). The 401 is then the authorization PROMPT, turned into a 401 failure at `AuthorizationUtils.java` lines 61 to 80. Its internode hook (`BasicAuthPlugin.java` lines 214 to 231) copies a Basic credential only when `forwardCredentials` is on and a `BasicAuthUserPrincipal` exists; for an anonymous request it returns false.
- **Context:** `SolrRequestInfo` is copied into pool tasks (`CoreContainer.java` line 177; `SolrRequestInfo.java` line 286). Inside a user's request, a pool task therefore carries that user's identity. The test thread has no request info, so the node-identity branch is the one that would fire if a listener were ever put on the test client. The pin is what catches that.
- **JIRA diagnosis** (`research/jira-context/SOLR-12161.json`, Noble Paul comment, 2018-04-11): the proposed fix was a pool flag so that SolrJ outside Solr would not set the header. That flag is not on main; `ExecutorUtil.java` line 373 sets it unconditionally. The closure is structural (listener placement), not the flag the JIRA proposed. A node-embedded SolrJ client used from a pool thread would be the exposure to watch (not checked; see Not checked).

## Item 3: exception type on the update path, and the adjusted expectation

- **Test** (head lines 256 to 260): `expectThrows(SolrException.class, () -> noCredentialsBatch.process(cluster.getSolrClient(), COLLECTION))`, then `assertEquals(401, batchFailure.code())`.
- **Path:** `SolrRequest.process(client, collection)` (`SolrRequest.java` lines 301 to 304) calls `client.request`. `CloudSolrClient.request` (lines 593 to 611) goes to `requestWithRetryOnStaleState` to `sendRequest` (lines 898 to 927) to `directUpdate` (lines 245 to 424).
- **Parallel branch** (the default, since `parallelUpdates` is true at `CloudSolrClient.java` line 1298): each leader route is submitted to `threadPool` (lines 322 to 335), and the call is `getLbClient().request(req)`. `LBSolrClient.request` (lines 571 to 597) goes to `doRequest` (lines 633 to 657). A 401 is not in `RETRY_CODES` (`LBSolrClient.java` lines 132 and 133: 404, 403, 503, 500), so the `RemoteSolrException` is rethrown unchanged (catch at line 643, throw at line 656). The Jetty LB client adds no override (`LBAsyncSolrClient`'s retry logic is in its async path only).
- **The 401 becomes a `RemoteSolrException` with code 401** in `HttpSolrClient.processErrorsAndResponse` (lines 197 to 264; the default branch at lines 224 to 231; the parsed-error branches at lines 245 to 262; `checkContentType` at lines 308 to 316 for mime mismatches). `RemoteSolrException` is final and extends `SolrException` (`RemoteSolrException.java` lines 30 and 63 and 64). `ErrorCode.getErrorCode(401)` is `UNAUTHORIZED` (`SolrException.java` lines 38 and 55 to 60).
- Exceptions are collected by route (lines 341 to 351). If the first is a `SolrException` (line 356), the code is mapped to a `RouteException` (lines 357 and 358; constructor lines 1706 to 1716, `super(errorCode, msg, cause)`, so code 401). Back in `requestWithRetryOnStaleState` (lines 699 onward): the root cause is 401, not a communication error, not 503, not stale (INVALID_STATE or 404), so line 884 rethrows the `RouteException` unchanged (lines 881 to 884).
- **Sequential branch** (lines 363 to 377): the `RemoteSolrException` itself is rethrown (lines 370 to 372). It is still a `SolrException` with code 401.
- The `SolrServerException` wrap in the TESTING note (`CloudSolrClient.java` line 886) applies only to non-`SolrException` causes, so it is not reached for a 401.
- **Verdict on the adjusted expectation: correct.** `RouteException` extends `SolrException`, so the `SolrException` expectation holds on both client modes. The original `RemoteSolrException` expectation would fail on the default path, because the thrown object is a `RouteException`, and `RemoteSolrException` is final. The assertion narrows the broader type. Useful contrast: the same client throws `RemoteSolrException` for a single query (head lines 309 to 315), because a query is not split and never goes through the pool.

## Item 4: the premise run

- **Configuration** (main side, not run): module `solr/core`; class `org.apache.solr.security.BasicAuthIntegrationTest`; method `testBasicAuth` (the block is at head lines 248 to 260). Flags `-WithSpotless` and `-WithFailBefore`. Gradle only on the owner's verify request (AGENTS.md, Test Runs). The queue's fail-before stage builds at the merge-base with `upstream/main`, which is `cabedd1d968` here, with the head's `src/test` overlay. So the base run is the same computation as the head run, and one run settles both. A green focused run gives a fail-before verdict of NOT_PROVEN, so the job never reaches SUCCESS (AGENTS.md: only PASS reaches SUCCESS).
- **Pass** (the batch throws `SolrException` code 401 at head lines 256 to 260): the scenario is closed on this tree, and the test documents it. It is a pin, not a fix.
- **Fail with the batch accepted** (`expectThrows` reports that no exception was thrown): a live defect candidate, an unauthenticated `CloudSolrClient` update admitted under basic auth. First check whether a `SolrAuthV2` header reached the nodes (the node logs, and the PKI metrics assertions in this file). Fail-before on the same tree would be PASS, so the ticket becomes a real fix target.
- **Fail with a different type or code** (a non-`SolrException` thrown, or code 500 from a non-`SolrException` first cause): a path or expectation difference, not evidence of an auth hole. Read the logged exception before changing the test.
- **Fail before line 256** (cluster start, `createServers`, `waitForActiveCollection`, the earlier steps): harness noise per AGENTS.md. Repeat narrower (or with the `-DleaderVoteWait` knob the harness note allows) before reading the result.
- **A gap in the pin itself:** the assertion does not check that no document was written. In the parallel path, one 401 route is enough to throw the `RouteException` (`CloudSolrClient.java` lines 354 to 358), so a partial write would still pass. A post-commit count of ids 200 to 229 through an authenticated query would close that.

## Item 5: the test family

From this round's side only. SOLR-18010 was read at head `c3685bb37d9d` for the two named files, and was not audited.
- No shared file. The 12161 hunk is inside `testBasicAuth`. `BasicAuthStandaloneTest.java` lines 21 and 22 import `STD_CONF` and `verifySecurityStatus` from `BasicAuthIntegrationTest`; the hunk touches neither helper.
- `SecurityConfHandlerTest` (`solr/core/src/test/org/apache/solr/handler/admin/`): `testConcurrentEditsToLocalSecurityJson` (lines 203 to 262) and `testConcurrentPersistConfLeavesOneWholeDocument` (from line 271) call `SecurityConfHandlerLocal.handleRequestBody` directly (`postAuthorizationEdit`, lines 318 to 327), with a mock `CoreContainer` and a latching authorization plugin. No HTTP, no credentials, no update request, no 401 assertion.
- `BasicAuthStandaloneTest` at head: the 401 at line 89 is an unauthenticated authentication POST. The added block (lines 107 to 124) sets `set-user-role` and checks the persisted file. The only unauthenticated request after setup is a query (lines 137 to 144) inside a catch-all that accepts any exception.
- **Overlap: none.** 18010 asserts no 401 and no update.
- **Gap:** a no-credentials update in standalone HTTP mode is covered by neither. The nearest coverage is the unit-level decision (`BaseTestRuleBasedAuthorizationPlugin.java` lines 135 to 145; `ExtractingRequestHandlerPermissionTest.java` lines 93 to 99), which never goes through HTTP. The standalone gap is coverage only, not PKI exposure, because there is no PKI path in standalone (item 2).

## Receipt disagreements (exact wording)

1. **Receipt `SOLR-12161.md` (the verify-first bullet):** "on current main, `PKIAuthenticationPlugin` only adds its header when `isSolrThread()` or a `SolrRequestInfo` is present (around line 389), so the described path looks closed." The reason is wrong. The `isSolrThread()` condition at line 389 is true on `CloudSolrClient` pool threads (item 2). The path is closed because the listener is never installed on the test's client. The conclusion (no header reaches the test client) holds.
2. **Receipt and TESTING note:** "the existing 401 checks in the test class use a collection reload, a `deleteByQuery` on a node-local client, and Jetty clients, not this scenario." Incomplete. Two cluster-client 401 assertions exist (lines 148 and 301: admin and query), and the reload checks assert only the exception type. The "unpinned" conclusion holds.
3. **TESTING note:** "The request may surface as a `RemoteSolrException` with code 401 or as a wrapped `SolrServerException`; adjust the exception type if the run shows otherwise." On the default parallel client, the surfaced type is `RouteException` (a `SolrException`, code 401). The wrapped `SolrServerException` does not occur for a 401 (`CloudSolrClient.java` lines 881 to 884; `LBSolrClient.java` lines 643 to 657).
4. **Confirmed without change:** the receipt's "the tip expects `SolrException`; its commit message records that `CloudSolrClient` throws `RouteException`, not `RemoteSolrException`" (commit `1725cbd84898`). The TESTING note's "Assumes the update permission is already in force" (the set-permission update to admin, head lines 236 to 241) holds.

## Owner decisions

1. **When the premise run happens:** a main-side verify session, since Gradle is barred during ticket work.
2. **If it passes:** keep the test as a no-fix regression pin (the queue shows NOT_PROVEN, so it will not reach SUCCESS), or drop it. This decides the PR framing: a pin, not a fix.
3. **Which tree settles it:** the branch base `cabedd1d968` (the queue's fail-before tree), or `upstream/main` `8e62c2686882` after the hunk is applied to the moved test file. Recommendation: run on main before opening anything.
4. **Strengthen the pin** with a post-commit count of ids 200 to 229 (item 4), and decide whether a standalone no-credentials HTTP update pin is wanted (item 5). Neither is in this round's scope.

## Not checked

- No compile of the head test. Read only: `SolrException` is newly imported; `UpdateRequest`, `SolrInputDocument` and `expectThrows` are already used in the file.
- Not run (per the rules). No observed thread, header or response body; the thread identity comes from `ExecutorUtil.java` line 373. Every non-stream 401 branch yields code 401, so the body does not change the code.
- Node-embedded SolrJ clients (for example `SolrClientCache`, `solrj-streaming` `SolrClientCache.java` line 72) were not checked for PKI exposure outside this ticket.
- SOLR-18010: only the two named test files at `c3685bb37d9d` were read. Its other code and its gate were not audited.
- The `expectThrows` implementation was not read (the usual `isInstance` semantics were assumed). The test-framework `SolrCloudAuthTestCase` was read only for the `>=` comparison (line 172).
- JIRA: the local context file was read in full (12 comments). No JIRA call. Registration dates were not checked.
