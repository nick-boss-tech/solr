# Flaky tests round 1, test 2: RecoveryAfterSoftCommitTest.test, HTTP/2 channel closed mid-request

Result: most likely a stale pooled HTTP/2 connection to the cut replica's socket proxy. The test closed that proxy shortly before the add at line 106. The cloud client, in any-replica mode (`sendUpdatesToAnyReplica`), picked that replica for the add. The write committed before the client saw the peer close, and no layer retries a committed update that fails with ClosedChannelException. Confidence about 80 percent on the mechanism. The routing mode and the failing URL port are not in the record. They are the first two checks in Part 4.

Base: repo-relative paths at `upstream/main` `8e62c2686882`. Test files are under `solr/core/src/test/...`, framework files under `solr/test-framework/src/java/...`. Nothing was built, run, or edited.

## Part 1: ranked hypotheses

### H1 (most likely): stale pooled HTTP/2 connection to the cut replica; the write committed before the client saw the close

1. **Client.** `cloudClient` is set in `initCloud` (`AbstractFullDistribZkTestBase.java` line 355) from `createCloudClient` (lines 368 to 371), which passes `random().nextBoolean()` as the shard-leaders-only flag. `createNewCloudSolrClient` (lines 2431 to 2449) calls `sendUpdatesOnlyToShardLeaders()` or `sendUpdatesToAnyReplica()` (`solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java` lines 1416 to 1431) and builds an `HttpJettySolrClient` (lines 2444 to 2447). The object is a `CloudHttp2SolrClient` (`CloudSolrClient.java` lines 1582 to 1596). Its Jetty transport is HTTP/2 unless `solr.http1` is set (`solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java` lines 265 to 284; default at `solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java` line 452).

2. **Every node sits behind a proxy.** The test's `createJetty` override (`RecoveryAfterSoftCommitTest.java` lines 64 to 75) calls `createProxiedJetty`, which builds each runner with the proxy enabled (`AbstractFullDistribZkTestBase.java` line 849). The runner creates a `SocketProxy` on an ephemeral port and advertises it as `hostPort` (`solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java` lines 192 to 198, 322 to 323, 741). Each replica's ZooKeeper base URL is therefore the proxy URL, which is how `getProxyForReplica` finds the proxy (`AbstractFullDistribZkTestBase.java` lines 881 to 900).

3. **The cut applies to all clients.** The test calls `proxy.close()` on the non-leader (`RecoveryAfterSoftCommitTest.java` lines 94 to 98). `SocketProxy.close` closes every bridged socket (`solr/test-framework/src/java/org/apache/solr/util/SocketProxy.java` lines 168 to 179; `Bridge.close` at lines 336 to 342) and closes the listening socket (`Acceptor.close` at lines 491 to 498), so new connects are refused. `JettySolrRunner.java` lines 744 to 747 document that all client traffic flows through this proxy. The cut therefore blocks client-to-replica traffic too, not only leader-to-replica traffic.

4. **ZooKeeper still says ACTIVE.** The non-leader's Solr node is not stopped. The test body makes no stop or start call (`RecoveryAfterSoftCommitTest.java` lines 77 to 121). State is refreshed at line 94 (`AbstractFullDistribZkTestBase.java` lines 2735 to 2736). The client keeps only ACTIVE replicas on live nodes (`CloudSolrClient.java` lines 1004 to 1006), so the non-leader is still a valid target. The leader changes that only after a forwarded update fails (`solr/core/src/java/org/apache/solr/update/processor/DistributedZkUpdateProcessor.java` lines 1217 to 1231; leader-initiated recovery at lines 1268 to 1327).

5. **Routing of the add at line 106.** `add(doc)` runs `SolrClient.java` lines 147 to 149, then 130 to 132, then 162 to 167, then `SolrRequest.process` (`solr/solrj/src/java/org/apache/solr/client/solrj/SolrRequest.java` lines 301 to 304), then `CloudSolrClient.request` (lines 593 to 612) and `sendRequest` (line 898). In any-replica mode `sendToLeaders` is false (lines 902 to 903; no direct update, lines 905 to 925). Every active replica goes into one list (lines 998 to 1015), which is shuffled (lines 1019 to 1024) by `ShufflingReplicaListTransformer.java` lines 32 to 35 under the default factory (`solr/solrj/src/java/org/apache/solr/client/solrj/routing/RequestReplicaListTransformerGenerator.java` line 83). That generator uses a static, unseeded `new Random()` (line 37), so the test seed does not control the shuffle. Each replica becomes an endpoint (`CloudSolrClient.java` lines 1034 to 1035). The non-leader's proxy URL comes first about half the time.

6. **Pooled connection.** `LBSolrClient.request` (`solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java` lines 571 to 597) calls `doRequest` (lines 633 to 689), which calls `doRequest` (lines 611 to 631), which calls `HttpJettySolrClient.requestWithBaseUrl` (`HttpJettySolrClient.java` lines 537 to 541 and 552 to 560). That creates a per-endpoint client sharing the parent's Jetty HTTP/2 client (lines 836 to 841). The write goes onto a pooled HTTP/2 connection to the non-leader's proxy port. This is an inference: the earlier adds and commit (`RecoveryAfterSoftCommitTest.java` lines 86 and 92) probably opened that connection. Under the shuffle, each of those requests had a 50 percent chance of picking the non-leader, so the pool probably holds such a connection.

7. **Committed write, then failure.** `HttpJettySolrClient.request` sets `committed` when the request headers reach the network (lines 470 to 476; the comment at lines 470 to 471 says so). If the client has not yet seen the peer close from step 3, the write succeeds, `committed` becomes true, and the stream fails with ClosedChannelException. This is an inference from a binary scan of the pinned Jetty 12.1.12 jars (`gradle/libs.versions.toml` line 84): jetty-io, jetty-http2-client-transport and jetty-http2-common all reference ClosedChannelException. Jetty source was not read.

8. **Wrapping.** With `committed` true, the code throws `SolrServerException("IOException occurred when talking to server at: " + url, cause)` on the synchronous write path (lines 477 to 485) or the listener path (lines 502 to 513). The record's message matches only these. The "Connection failed before the request was sent" (lines 486 to 488 and 514 to 516), "Server refused connection" (lines 505 to 507) and "Connection lost" (lines 519 to 525) messages do not match.

9. **No retry.** For an update, `LBSolrClient.doRequest` is non-retryable (`LBSolrClient.java` line 576). It moves to another endpoint only for a connect exception (lines 675 to 677; `isConnectException` at lines 691 to 698) or a RequestNotSentException in the cause chain (line 677). ClosedChannelException is neither, so it is rethrown (lines 681 to 683). `CloudSolrClient.requestWithRetryOnStaleState` marks a communication error only for SocketException, UnknownHostException or RequestNotSentException (`CloudSolrClient.java` lines 213 to 217), so `wasCommError` is false (line 725). The error code is UNKNOWN, so the 503 path does not apply (lines 720 to 729). The method rethrows (lines 779 to 781 and 881 to 884). The test fails at line 106.

If the client had seen the close first, the write would fail before commit. The error would be RequestNotSentException (`HttpJettySolrClient.java` lines 486 to 488), and LB would fail over to the leader (`LBSolrClient.java` lines 675 to 680). A fresh connect would hit the closed listener (`SocketProxy.java` lines 491 to 498), giving a connect refusal and the same failover. So the outcome is set by a race between the client's I/O thread and the test thread. This is an inference: Jetty keeps read interest on an open HTTP/2 connection, so the close is normally seen quickly. That is why the failure is a flake and not every run.

Objects at line 106 (for the test 3 comparison):
- Client object: `cloudClient` (`AbstractFullDistribZkTestBase.java` line 355), a `CloudHttp2SolrClient` (`CloudSolrClient.java` line 1596). One parent `HttpJettySolrClient` owns one Jetty HTTP/2 HttpClient. Per-endpoint calls use `NoCloseHttpJettySolrClient` on that same HttpClient (`HttpJettySolrClient.java` lines 552 to 560 and 836 to 841). The LB object is created at lines 564 to 566.
- Endpoint: the non-leader's `replica.getBaseUrl()`, which is `http://<host>:<proxyPort>/solr`. That is the proxy port, not the Jetty port (`JettySolrRunner.java` lines 322 to 323 and 741). Built at `CloudSolrClient.java` lines 1034 to 1035. `<host>` is not in the record.
- Session object: a pooled HTTP/2 connection in Jetty's pool for origin `<host>:<proxyPort>`. Jetty-internal, not in the repo. Up to 4 per destination (`HttpJettySolrClient.java` line 283).
- Closer: `SocketProxy.close()` at `RecoveryAfterSoftCommitTest.java` line 98.

### H2 (alternative, lower): a committed ClosedChannelException on a pooled connection closed for another reason

For example an idle close or a GOAWAY on a session to a node the test did not cut. Evidence against: only the non-leader's proxy is closed (`RecoveryAfterSoftCommitTest.java` line 98); the server connector idle timeout is 260 s (`JettySolrRunner.java` lines 110, 277 and 291); the gap from the earlier requests to line 106 is seconds. Confidence about 10 percent.

### Ruled out

- **H3, a node restart or shutdown racing the request.** The test body makes no stop or start call (`RecoveryAfterSoftCommitTest.java` lines 77 to 121), and stress is off (`AbstractFullDistribZkTestBase.java` line 343). `JettySolrRunner.stop` would close the proxy (lines 619 to 620), but nothing calls it here.
- **H4, an in-flight response cut by the close.** The calls before line 98 (lines 79, 86 to 87, 92) are synchronous and have returned before the cut at line 98.

## Part 2: answers to the three questions

**Q1. What closes the channel?** The test does, through `SocketProxy.close()` at `RecoveryAfterSoftCommitTest.java` line 98. That closes the TCP connections the client holds to the non-leader's proxy (`SocketProxy.java` lines 168 to 179 and 336 to 342). The HTTP/2 layer then fails the in-flight stream once it sees the peer close (H1 step 7). It is not a node restart, and the client does not close the session on its own. The only close in the synchronous request path is `req.abort` after a failure (`HttpJettySolrClient.java` lines 529 to 532), which is a consequence, not a cause. The test uses `cloudClient` from the base class (lines 86 and 106), not `cluster.getSolrClient()`.

**Q2. Is there a legitimate window?** Yes. From line 98 until the leader handles a failed forward (`DistributedZkUpdateProcessor.java` lines 1217 to 1327), ZooKeeper and the client both show the non-leader as ACTIVE (`CloudSolrClient.java` lines 1004 to 1006), and the cut blocks client traffic into that node (`JettySolrRunner.java` lines 744 to 747). A write routed there is a valid target by the cluster's view and fails at the transport. The test should absorb this by not sending to the cut node. It should not expect SolrJ to fail over, because SolrJ does not fail over a committed update (Q3). The client also does not mark its cached state stale for this exception (`CloudSolrClient.java` lines 725 to 756 mark only on a communication error or a 503), so later writes in the window can keep choosing the cut node.

**Q3. Does the SolrJ HTTP/2 client retry an update?** The HTTP/2 client does not retry. `HttpJettySolrClient.request` (lines 462 to 530) and the async path (lines 382 to 459) each make one attempt. Retry decisions sit in two layers above it:
- **LB** (`LBSolrClient.java` lines 576 and 644 to 683): an update moves to another endpoint only for a connect exception or a RequestNotSentException. That class is documented as a request that "failed before any of it was written to the network" (`solr/solrj/src/java/org/apache/solr/client/solrj/RequestNotSentException.java` lines 22 to 28; also `LBSolrClient.java` lines 678 to 679, "replaying it elsewhere is safe even though it isn't idempotent").
- **Cloud** (`CloudSolrClient.java` lines 99 to 100, 213 to 217 and 725 to 777): any request is re-sent up to 5 times when the cause chain holds SocketException, UnknownHostException or RequestNotSentException. The request type is not checked. So a committed update that fails with a SocketException is re-sent by the cloud client, which goes against the LB rule. This is a code reading. Whether Jetty's HTTP/2 path produces SocketException here was not confirmed.
- **Jetty**: the only retry mention in the repo is the comment at `HttpJettySolrClient.java` line 862, which refers to a GOAWAY retry inside Jetty. This is an inference that a socket close does not take that path.

**Verdict for this failure:** no layer retries it, and the code's stated rule is that an update may be retried only when it was never sent. Here the bytes went to the proxy's socket, which the proxy had already closed. The client reached the node only through the proxy, so the node probably never received the add (an inference from `SocketProxy.java` lines 310 to 317 and 336 to 342). SolrJ cannot know that, so the test must not depend on a retry. The change belongs in the test's routing.

## Part 3: evidence

**The record proves** (the quoted record, plus the code that fixes the message):
- The add at line 106 threw SolrServerException with "IOException occurred when talking to server". The code emits that text only when `committed` is true (`HttpJettySolrClient.java` lines 485 and 513), so the request headers were written before the failure.
- The cause was ClosedChannelException, which no SolrJ layer retries (`CloudSolrClient.java` lines 213 to 217). That fits a test that stops at line 106 and reaches no assertion.
- Line 106 is the first write after the cut. The second loop starts at `RecoveryAfterSoftCommitTest.java` line 102 with i = 3, after three adds in the first loop (lines 82 to 87).

**The record suggests, but does not show:**
- That the target was the non-leader's proxy. The record has no URL. The CI log is needed.
- That the client was in any-replica mode for seed `A57A22E346C237C6`. The seed does set this flag (`AbstractFullDistribZkTestBase.java` line 370 uses `random()`), but the record does not print the value.
- That a pooled connection to the non-leader existed (H1 step 6, an inference).
- "Jetty's HTTP/2 session shutdown path", the record's phrase. The binary scan supports ClosedChannelException on endpoint close, not the exact path.
- Whether the failure came from the synchronous write path (line 485) or the listener path (line 513). Both give the same text.

**Evidence that would refute H1:**
- A failing URL port equal to the leader's port.
- A failure with the mode set to leaders-only. H1 predicts none there, because the leader comes first (`CloudSolrClient.java` lines 1009 to 1024).
- Zero failures across many any-replica runs with the seed.

**Evidence for H1 from the code:** every link in steps 1 to 9 is in code, and the one-committed-write branch matches the record's message exactly. The Part 3 fix, which removes the non-leader from the write path, is expected to stop this. If the failure stops with it, that is consistent with H1, though it is not proof by itself.

**Evidence against H2:** none in the record. The code evidence is listed under H2.

## Part 4: proposed fix (not applied)

Primary, test-side: `solr/core/src/test/org/apache/solr/cloud/RecoveryAfterSoftCommitTest.java`, method `test()`, lines 101 to 107. Send the post-cut adds through a leaders-only client:

```java
try (CloudSolrClient leaderClient =
    createNewCloudSolrClient(zkServer.getZkAddress(), DEFAULT_COLLECTION, true, 30000, 120000)) {
  for (; i < MAX_DOCS; i++) {
    SolrInputDocument document = new SolrInputDocument();
    document.addField("id", String.valueOf(i));
    document.addField("a_t", "text_" + i);
    leaderClient.add(document);
  }
}
```

Add the import `org.apache.solr.client.solrj.impl.CloudSolrClient`.

Why: the cut is meant to isolate the replica from the leader, but the proxy also cuts client writes (`JettySolrRunner.java` lines 744 to 747). A leaders-only client puts the leader first (`CloudSolrClient.java` lines 1009 to 1024) and falls back to the replica only on a connect-type failure from the leader (`LBSolrClient.java` lines 675 to 680), which a healthy leader does not produce. The leader then forwards to the cut replica, which is the intended partition path. A forward failure to a replica is logged, not returned to the client (`DistributedZkUpdateProcessor.java` lines 1219 to 1231), so the add succeeds. `createNewCloudSolrClient` also randomizes `directUpdatesToLeadersOnly` (`SolrTestCaseJ4.java` lines 2517 to 2518). That is harmless here, because the leader exists. The new client needs its own close, as shown.

Secondary, only if the owner wants to keep the randomized client: retry only the same add, with the same id so a repeat overwrites the same document, and only when the cause is ClosedChannelException, bounded to a few attempts. The retry is a new request. It reaches the leader, or the closed listener refuses it and LB fails over (`SocketProxy.java` lines 491 to 498). This absorbs the race but leaves the routing unclear. It needs a check that `id` is the uniqueKey in this test's schema, which was not checked. Not preferred.

Not recommended:
- A SolrJ change to retry a committed update on ClosedChannelException. The documented rule (`RequestNotSentException.java` lines 22 to 28) exists because a committed write may already be applied, and a retry could duplicate an add. This is a design boundary, not a bug.
- A `SocketProxy` change to isolate leader-to-replica traffic. The proxy is one per node. `SocketProxy.java` has no peer selection, and it sees only one socket pair per connection.

Infrastructure verdict: the shared helper is right for the other tests (random routing is the point of `createCloudClient`), and the proxy does what it says. The defect is in this test: it cuts a node and then writes through a client that can pick that node. Fix the test.

Owner decision: fix this test only (recommended), or change `createCloudClient` routing for all tests (not recommended; it affects every cloud test).

## Part 5: main-side verification

**Step 0, no new run.** Read the CI log for job `114192700428` (run `37987632639`).
- The failing add's message has the form "IOException occurred when talking to server at: http://<host>:<port>/solr/...". Compare `<port>` with the proxy port in the SocketProxy close line "Closing N connections to: http://<host>:<port>/solr" (`SocketProxy.java` line 173) for the test's cut, which is the only close in the test body. Same port: H1 is confirmed at the routing level. The leader's port: H1 is refuted, and H2 moves up.
- N greater than 0 is not decisive, because the leader's own pooled connections to the cut node also count.

**Step 1, settling run on current main.**
- Module `:solr:core`; class `org.apache.solr.cloud.RecoveryAfterSoftCommitTest`; method `test`; seed `tests.seed=A57A22E346C237C6` (option at `gradle/testing/randomization.gradle` lines 83 to 84).
- Tree: `upstream/main` `8e62c2686882`, plus one temporary, uncommitted diagnostic line at the start of `test()` that prints `cloudClient.isUpdatesToLeaders()` (public, `CloudSolrClient.java` lines 1084 to 1085) and the replica base URLs.
- Repetitions: `tests.iters` (`randomization.gradle` line 85), N = 20. First check whether iterations reuse the seed, which is not verified. The shuffle is unseeded either way, so repetition is needed for rate data.
- Confirms H1: the mode prints false (any-replica); failures show ClosedChannelException at `RecoveryAfterSoftCommitTest.java` line 106 with the cut node's proxy port in the URL; failures occur only in any-replica runs. The observed failure rate in those runs gives the race probability.
- Refutes H1: failures with the mode true (leaders-only); failing URLs on the leader port; or no failures in N any-replica runs. In the last case H2 becomes the lead, and the CI log has to explain the recorded failure.

**Step 2, fix run.**
- Tree: `upstream/main` plus the Part 4 change (leaders-only client for the post-cut adds).
- Same class, method and seed; N = 20.
- Confirms the fix: no ClosedChannelException failures in any-replica runs, and the adds in the second loop succeed.
- Refutes H1 as the whole story: failures persist with the leaders-only client. Then the failing URL port and the logs decide between a leader-side path and H2.

**Step 3, control (optional).** The same seed on current main with a leaders-only mode, N = 20. Expect zero failures. Any failure there means H1 is not the only cause.

## Pattern section input (test 2 side)

- Test 2 does not share the test 3 mechanism as the test 3 analysis describes it. Test 2's stale session is to the non-leader's proxy, reached through a pooled connection, and the cut is the test's own `proxy.close()`. Test 3 is a fixed-delay stop of the PULL node (per the test 3 report).
- The common element is the harness: `JettySolrRunner.stop` closes the proxy (`JettySolrRunner.java` lines 619 to 620), and a restart reopens it (lines 526 to 528). A node restarted through `stop` therefore gets the same pooled-connection close that test 2 creates by hand. That is a fact about the harness, not a finding about test 3.
- A shared-infrastructure hazard is named in the test 3 report: `JettySolrRunner.stop()` closes the cached client on stop, and `JettySolrRunner` returns the same cached object to every caller. Test 2 reads the client from the base class, not from the runner, so this hazard does not apply to test 2 directly.
- Test 3 was not read here, per the assignment.

## Not checked

- The CI log for job `114192700428`. Only the quoted record was available, so the URL, stack trace and routing mode are unknown.
- Jetty source. Only binary jars were scanned at class-reference level. The close path, the commit callback timing and whether Jetty keeps read interest on idle HTTP/2 connections are inferences.
- The CI failure rate for this test. It is not in the record.
- Whether `tests.iters` reuses the seed per iteration. `randomization.gradle` line 85 does not say.
- Whether any build file sets `solr.http1`. The search covered `gradle/testing`, `solr/test-framework` and a few build files, not all of them.
- Whether `id` is the uniqueKey in this test's schema. This matters only for the secondary option.
- Other tests that use the same cut. The `getProxyForReplica` callers are ForceLeaderTest, HttpPartitionOnCommitTest, HttpPartitionTest, LeaderFailoverAfterPartitionTest, ReplicationFactorTest and TestPullReplicaErrorHandling. The other `proxy.close` users are FullSolrCloudDistribCmdsTest, LeaderVoteWaitTimeoutTest, TestCloudConsistency and TestTlogReplayVsRecovery. Not checked: whether they write through the cloud client after a cut. This is for the pattern section.
- Side observation for the test 4 port topic: `SocketProxy.reopen` swallows bind failures (`SocketProxy.java` lines 220 to 224). Not analyzed further.
- Leader-side forwarding beyond the lines cited in `DistributedZkUpdateProcessor.java`.
