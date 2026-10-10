# Flaky tests root causes, round 1

Claim: `claims/flaky-tests-root-cause-round-1.md` (commit `27782809cbf`). Assignment: `assignments/flaky-tests-root-cause-round-1.md` (commit `3dbcd5ac70d`). Part reports with the full citations: `reports/flaky-tests-root-cause-round-1-t1.md` through `-t4.md`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Four subagents, one per test, in parallel. No build, Gradle run, or test was run. No branch, draft, live PR, or production or test code was changed. Nothing was posted.

Code basis: `upstream/main` at `8e62c2686882`. The assignment's path for `JettySolrRunner` does not exist there. The class is at `solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java`.

## Summary

| Test | Most likely mechanism | Confidence | Verdict on the fix location |
|---|---|---|---|
| 1. GCSInstallShardTest, teardown | A non-restore exception escapes one node's stop during shutdown. The RCA's restore route does not reach the check in the code. | Medium. The suppressed cause decides it. | Settle with the log first. Shared check unchanged. |
| 2. RecoveryAfterSoftCommitTest.test | A committed write goes to the cut replica's proxy on a stale pooled connection. No layer retries it. | About 80 percent on the mechanism | The test. Route post-cut writes through a leaders-only client. |
| 3. TestCoordinatorRole.testNRTRestart | The test stops the PULL node on a fixed timer, which closes the client the add loop is using. The loop catches only SolrException. | About 70 percent | The test. Keep PULL up until an add succeeds. |
| 4. LeaderElectionIntegrationTest | A process outside the JVM took the pinned port during the restart gap, and the framework holds no reservation. | About 60 percent | Shared framework (`JettySolrRunner`). Hold the released port. |

## Test 1: GCSInstallShardTest, suite teardown collects restore errors

**Ranked hypotheses.**
- **H1 (most likely, medium).** A non-restore exception escapes one `JettySolrRunner.stop()` during `MiniSolrCloudCluster.shutdown()`. The restore `IndexNotFoundException` lines are logs from expected failures in the same suite. `checkForExceptions` (`MiniSolrCloudCluster.java` lines 718 to 736) sees only the futures from the stop callables. Any unguarded call inside `CoreContainer.shutdown()` (`CoreContainer.java` lines 1220 to 1330) or a TimeoutException from the reserved-executor wait (`JettySolrRunner.java` lines 651 to 652) could be the cause. The record does not quote the suppressed cause.
- **H2 (the RCA's mechanism, rejected by the code).** A restore exception recorded on a server thread reaches `checkForExceptions`. No set and no route was found. Each restore-path catch traced logs instead (`RestoreCore.java` lines 178 and 218; `SolrCores.java` lines 118 to 124; `CoreContainer.java` lines 1258 to 1292).
- **H3 (lower).** A TimeoutException from the reserved-executor wait, which the comment at `JettySolrRunner.java` lines 646 to 650 says "doesn't always seem to work". It is the first thing to check in the log.
- **H4 (rejected as the teardown cause, a real hazard).** Leftover injected state. The injected port set is never cleared (`AbstractIncrementalBackupTest.java` line 583). It leaks into later tests in the class, which can cause test-level failures but not this teardown error.

**Answers to the four questions.** The restore exceptions are expected: both come from test methods that expect them (`AbstractInstallShardTest.java` lines 190 to 219 and 249 to 286). The injected copy throws UnsupportedOperationException, and `RestoreCore` rethrows it before line 207, so the injected error cannot be the `IndexNotFoundException`. The `IndexNotFoundException` comes from the IndexWriter open at `RestoreCore.java` line 211, which is reached after an empty download list (`GCSBackupRepository.java` lines 202 to 225 return an empty array for a missing location). That is evidence for the route, not proof, because the exception text is not quoted in full. The same hazard exists in the LocalFS, S3 and GCS installs and incremental-backup suites that share `AbstractInstallShardTest` and `AbstractIncrementalBackupTest`.

**Proposed fix (not applied).**
- Do not change the shutdown check (`MiniSolrCloudCluster.java` lines 636 to 640). The fix depends on the suppressed cause:
  - TimeoutException at `JettySolrRunner.java` line 652: log a warning on timeout in `stop()`, or drop the wait as the comment suggests. This is shared by every MiniSolrCloudCluster suite.
  - RuntimeException from an unguarded call in `CoreContainer.shutdown()`: wrap that call in the try and log pattern at `CoreContainer.java` lines 1261 to 1271.
  - IndexNotFoundException with RestoreCore frames: the route analysis is wrong, and there is no fix until those frames are read.
- Hardening that applies whatever the cause: reset `ErrorThrowingTrackingBackupRepository.portsToFailOn` to `Set.of()` in `AbstractIncrementalBackupTest.setUpTrackingRepo()` (lines 109 to 112), and the same reset in `AbstractInstallShardTest.deleteTestCollections()` (lines 91 to 96).
- Separate product defect, not this flake's fix: `RestoreCore.java` line 227 deletes index.properties on rollback instead of restoring the previous pointer. Correct for a core on the default index, wrong if the core had a non-default index before the install. The tests use fresh collections, so they do not hit it.

**Main-side verification.** Read the log for job `114192758248` of run `37987579785` first. Find "Error shutting down MiniSolrCloudCluster" and the logged cause at `MiniSolrCloudCluster.java` line 726. Then the settling run: `GCSInstallShardTest`, seed `B94347D2600CC75D`, current main, repeated five times. A reproduced failure with the same cause confirms it. Five passes mean the flake is timing-bound, not seed-bound.

## Test 2: RecoveryAfterSoftCommitTest.test, HTTP/2 channel closed mid-request

**Ranked hypotheses.**
- **H1 (most likely, about 80 percent).** `proxy.close()` on the non-leader (`RecoveryAfterSoftCommitTest.java` line 98) closes the TCP connections the client holds to that node's proxy (`SocketProxy.java` lines 168 to 179). The client's pooled HTTP/2 connection to that proxy port is stale. The add at line 106 goes to the cut replica, because any-replica routing picks it about half the time (`CloudSolrClient.java` lines 1019 to 1024). The write commits (`HttpJettySolrClient.java` line 476), and then the stream fails with ClosedChannelException before the client sees the close. `CloudSolrClient` and `LBSolrClient` do not retry it (`LBSolrClient.java` lines 675 to 683; `CloudSolrClient.java` lines 213 to 217).
- **H2 (about 10 percent).** A committed ClosedChannelException on a pooled connection closed for another reason, such as an idle close or GOAWAY. Evidence against: only the non-leader's proxy is closed, the idle timeout is 260 s, and the gap is seconds.
- **Ruled out.** A node restart racing the request (the test makes no stop or start call). An in-flight response cut by the close (the earlier calls have already returned).

**Answers to the three questions.** The test closes the channel, through `SocketProxy.close()` at line 98. It is not a restart. There is a legitimate window: from line 98 until the leader handles a failed forward (`DistributedZkUpdateProcessor.java` lines 1217 to 1327), ZooKeeper and the client both show the non-leader as ACTIVE, and the cut blocks client traffic into that node (`JettySolrRunner.java` lines 744 to 747). The HTTP/2 client does not retry, and neither does LB for a committed update. The cloud layer retries on SocketException, which is a code reading whose effect on this path was not confirmed.

**Proposed fix (not applied).** In `RecoveryAfterSoftCommitTest.test()` (lines 101 to 107), send the post-cut adds through a leaders-only client created with `createNewCloudSolrClient(zkServer.getZkAddress(), DEFAULT_COLLECTION, true, 30000, 120000)`, and close it. The leader comes first (`CloudSolrClient.java` lines 1009 to 1024). A forward failure to the cut replica is logged, not returned, so the add succeeds (`DistributedZkUpdateProcessor.java` lines 1219 to 1231). The infrastructure is right for the other tests, because random routing is the point of `createCloudClient`. The defect is in this test. Not recommended: a SolrJ change to retry a committed update, which could duplicate an add, and a SocketProxy change to isolate peer traffic, which the proxy cannot do.

**Main-side verification.**
- Step 0, no run: the CI log for job `114192700428`. Compare the failing URL's port with the proxy port in the SocketProxy close line (`SocketProxy.java` line 173). The same port confirms H1 at the routing level. The leader's port refutes H1 and moves H2 up.
- Step 1: `RecoveryAfterSoftCommitTest`, `test`, seed `A57A22E346C237C6`, N = 20, on current main with a temporary diagnostic that prints `cloudClient.isUpdatesToLeaders()` and the replica URLs. Failures should appear only in any-replica runs.
- Step 2: the same with the Part 3 change. No ClosedChannelException in any-replica runs.

## Test 3: TestCoordinatorRole.testNRTRestart, same exception shape after a restart

**Ranked hypotheses.**
- **H1 (most likely, about 70 percent).** The PULL node is stopped on a fixed timer while the add loop is still adding through it. The loop's client is the PULL node's cached client (`TestCoordinatorRole.java` line 221). The test's timer sleeps `pullServiceTimeMs` (lines 241 and 259) and then calls `pullJettyF.stop()` (line 261). `JettySolrRunner.stop()` closes that same cached client first (lines 616 to 617), then stops its Jetty HTTP client (`HttpJettySolrClient.java` lines 340 to 365). The in-flight add, or the next one, fails at the transport and surfaces as SolrServerException (lines 477 to 485). Nothing retries it. The loop catches only SolrException (line 309), so the error escapes at line 306. Once PULL is stopped before the first successful add, every later add fails the same way.
- **H2 (about 10 percent).** A stale pooled connection to PULL with no restart or stop. Evidence against: PULL is not restarted before line 306, and the idle timeout is 260 s.
- **H3 (under 5 percent).** A stale session to NRT, the restarted node. The test client has no connection to NRT.

**Answers.** The add's client object is the PULL node's cached `HttpJettySolrClient`. Its session object is the Jetty HTTP client it owns. The restarted node is NRT, which the client never contacts. The closing event is the test's own `pullJettyF.stop()`. The pattern answer for this test on its own is no: no client session to a restarted node is reused.

**Proposed fix (not applied).** In `testNRTRestart`:
- (a) Add `CountDownLatch addDone = new CountDownLatch(1);` before the executor submit (line 242).
- (b) After `Thread.sleep(pullServiceTimeMs);` at line 259, add `if (!addDone.await(2, TimeUnit.MINUTES)) { log.warn(...); }`. The lambda's catch at line 263 already wraps exceptions.
- (c) After `client.commit(COLL);` at line 307, call `addDone.countDown();`.

PULL then stays up until an add has succeeded, which matches the test's own comments (lines 256 to 258 and 299 to 300). No new `random()` calls are added, so seed `681E2A715B2CE1D3` keeps the same sleep values. The main thread does not wait on the manipulation future until line 354, so the await cannot deadlock.

Secondary change, only together with the primary: at line 309, also catch SolrServerException and IOException. The added document has a fixed id (line 297), so a resend overwrites rather than duplicates. Alone, it would spin until the test timeout, because the same client fails every time after a close. Rejected: changing `JettySolrRunner.stop()` to stop closing the cached client, which is an infrastructure change with broad reach. The shared code behaves as documented.

**Main-side verification.**
- Tree A, current main: `TestCoordinatorRole`, `testNRTRestart`, seed `681E2A715B2CE1D3`. Confirms H1 if the log shows "NRT jetty restarted.", then "stopping PULL jetty", then "PULL jetty stopped.", all before the failure, with no "successfully added another doc" line. Refutes H1 if the failure is logged before "stopping PULL jetty", or the URL is not the PULL node's. The settling run is already queued on the main side.
- Tree B, main plus the primary change: the test passes, and the log shows "successfully added another doc" before "stopping PULL jetty". Then run three other seeds on Tree B to check the class.

## Test 4: LeaderElectionIntegrationTest.testSimpleSliceLeaderElection, port already in use

**Ranked hypotheses.**
- **H1 (most likely, about 60 percent).** While runner A was stopped, a process outside this test JVM took A's pinned port P as a listening socket and kept it for the whole 60-second retry budget. A's restart at `LeaderElectionIntegrationTest.java` line 95 failed with BindException. The framework releases the port at stop (`JettySolrRunner.java` lines 611 to 670) with no reservation. The restart reuses the same port (lines 497 and 501 to 502), and `retryOnPortBindFailure` (line 559) retries only that port for 60 seconds (`JettyConfig.java` line 83). Other forks bind port 0 in the same host pool (`defaults-tests.gradle` lines 44 to 48 and 106). The holder is not named in the record.
- **H2 (possible, less likely).** The holder is a non-listening socket of another process, such as a client socket or a client-side TIME_WAIT (a kernel rule). It needs a socket that lasts about 60 seconds.
- **H3 (rejected).** An in-JVM holder. The gap has no in-JVM start, and in-JVM client sockets set SO_REUSEADDR.
- **H4 (contributing, not a cause).** The design makes a transient holder fatal. No test covers the retry, and no test sets `withPortRetryTime` or forces a bind conflict.

**Answers.** The port is chosen by the kernel at first bind, not by Solr code. The restart keeps that port. The retry does engage for this exception (Jetty's `_openEarly` path, checked in bytecode). Two runners in one JVM cannot grab the same port, and an in-JVM listener is excluded for the window. The pinned restart path covers 44 suites directly and up to 113 with the shared helpers. `TestCoordinatorRole` also restarts on the pinned path, but its failure is ClosedChannelException, so its restarts did bind.

**Proposed fix (not applied). The fix goes in the shared framework.**
- **Fix A (recommended).** In `JettySolrRunner` (`solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java`), keep a static map of reserved ports. In `stop()`, after the server stop and join loop, bind a `ServerSocket` to `127.0.0.1:jettyPort` with reuse off and store it. In `start(boolean)`, before the retry block, release the reservation for that port. Release all of a cluster's reservations in `MiniSolrCloudCluster.shutdown()` (line 615). Residual window: a few milliseconds between the release and Jetty's bind. Closing it fully would need SO_REUSEPORT or passing the bound channel to Jetty, which was not checked.
- **Fix B (test-side fallback, test 4 only).** Change `LeaderElectionIntegrationTest.java` lines 94 to 96 to `cluster.startJettySolrRunner(runner, false)`, which takes a fresh port. It leaves the other 44 to 113 suites exposed, and it changes node identity. Not recommended as the primary fix.
- Not changed: `retryOnPortBindFailure`. A longer budget does not help if the holder outlives it.

**Main-side verification.**
- V1, diagnostic: `LeaderElectionIntegrationTest`, seed `3E3D9FF553211ED6`, `-Ptests.jvms=4` with other cluster suites in parallel. On BindException, log `ss -tanp` for port P and the elapsed time. Confirms H1 if a LISTEN socket on P belongs to another PID, the elapsed time is near 60 seconds, and about 20 attempts were made. The seed may not reproduce the other forks, so V1 may not fire. If it does not, it settles nothing.
- V2, deterministic: a new test in `TestJettySolrRunner` that holds a pinned port with a foreign-style listener, then calls `start()`. Current main: BindException after about 5 seconds, which shows the gap. Main plus Fix A: the holder fails to bind and `start()` succeeds. This is an in-JVM stand-in, so it proves the gap, not which process held P in CI.
- V3, regression: the real test on main plus Fix A, with the 44 pinned-restart suites, and a spotless check for `solr/test-framework`.

## Cross-test pattern

**Do tests 2 and 3 share one mechanism? No.** They surface as the same exception type and share the harness path, but they close different objects.
- **Test 2** closes the server side of one pooled connection, through the proxy (`RecoveryAfterSoftCommitTest.java` line 98). The client object stays open. Its next write uses a stale connection in its pool.
- **Test 3** closes the client object itself, through the harness stop (`JettySolrRunner.java` lines 616 to 617), while the add loop still uses it. The loop catches only SolrException.

Both fail the same way at the transport level, and neither layer retries a committed update. But the object that is closed differs, and so does the fix. Each test has its own fix, so the proposed fix is not written once.

**Shared hazard in the harness.** `JettySolrRunner.stop()` closes the cached client and the proxy (lines 616 to 620). Any test that keeps the cached client across a stop gets a closed client. Test 3 does this. Test 2 does not, because it uses the base class's `cloudClient`.

**Test 4 reach.** The pinned-port restart path covers 44 suites directly and up to 113 with the shared helpers (grep-level counts). Fix A covers those restarts. It does not cover first starts on port 0, or `startJettySolrRunner(jetty)` (start(false)). `SSLMigrationTest` is covered only if the reservation is keyed by port across objects.

**Test 1 and the others.** Test 1's RCA is not what the code shows. The `IndexNotFoundException` route exists, but no route from it to the shutdown check was found. The shared `portsToFailOn` leak affects every suite built on `AbstractIncrementalBackupTest`.

## Owner decisions

1. **Test 1: which cause to fix.** The main side's RCA (restore) and the code (non-restore escape) disagree. Recommendation: read the suppressed cause from the log before any change. Options: (a) read the log, then decide; (b) fix the restore route as the RCA says, without the log. Recommendation: (a).
2. **Test 1 hardening: reset `portsToFailOn`.** A small change to the shared test framework that stops the leak across suites. Options: (a) yes; (b) no. Recommendation: (a).
3. **RestoreCore rollback (`RestoreCore.java` line 227).** A product defect outside this flake. Options: (a) file a separate ticket; (b) leave it. Recommendation: (a).
4. **Test 2: where the fix goes.** Options: (a) fix this test, with a leaders-only client for the post-cut adds (recommended); (b) change `createCloudClient` routing for all tests (not recommended, it affects every cloud test).
5. **Test 3: the timer.** Options: (a) keep PULL up until an add has succeeded, the primary change only (recommended); (b) also catch SolrServerException and IOException (only with (a)).
6. **Test 4: the framework.** Options: (a) Fix A, hold a stopped node's port until restart or cluster shutdown (recommended); (b) Fix B, a fresh port for test 4 only; (c) accept the flake. Recommendation: (a).
7. **Test 1 and the framework wait.** If the log shows the TimeoutException from the reserved-executor wait, the fix to `JettySolrRunner.stop()` is shared. Decide after the log is read.

## Main-side runs, in order

No new run first: the CI logs for job `114192758248` (test 1), job `114192700428` (test 2), the test 3 RCA log, and run `37991927523` (test 4). Read the causes and the failing URLs and ports.

Then the settling runs, which the assignment says are main-side work:
- Test 1: `GCSInstallShardTest`, seed `B94347D2600CC75D`, current main, five repetitions. Run 2 is already queued.
- Test 2: `RecoveryAfterSoftCommitTest`, seed `A57A22E346C237C6`, N = 20, with the routing diagnostic.
- Test 3: `TestCoordinatorRole.testNRTRestart`, seed `681E2A715B2CE1D3`, Tree A (current main, already queued) and Tree B (with the primary change, three extra seeds).
- Test 4: V1 (diagnostic), V2 (deterministic test in `TestJettySolrRunner`), V3 (regression with the 44 suites).

## Not done

- No build, Gradle run, or test. No edit to any branch, draft, or production or test code. No `gh` call and no fetch. No JIRA access.
- The CI logs and the full failure text were not available. Every cause above that depends on the suppressed exception, the URL, or the holder is marked as unconfirmed.
- Jetty and kernel behavior were checked, where at all, by bytecode or general knowledge. The Jetty source was not read.
- Test 4's reach counts are grep-level, and each file was not read.
